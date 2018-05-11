package com.palmjoys.yf1b.act.majiang.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.network.filter.session.SessionManager;

import com.palmjoys.yf1b.act.cooltime.manger.CheckResetTimeManager;
import com.palmjoys.yf1b.act.corps.manager.CorpsManager;
import com.palmjoys.yf1b.act.corps.manager.PlayerCorpsManager;
import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.majiang.model.CardAttrib;
import com.palmjoys.yf1b.act.majiang.model.GameDataAttrib;
import com.palmjoys.yf1b.act.majiang.model.GameDefine;
import com.palmjoys.yf1b.act.majiang.model.SeatAttrib;
import com.palmjoys.yf1b.act.majiang.model.SeatVo;
import com.palmjoys.yf1b.act.majiang.model.TableAttrib;
import com.palmjoys.yf1b.act.majiang.model.TableBaseVo;
import com.palmjoys.yf1b.act.majiang.model.TableVo;
import com.palmjoys.yf1b.act.replay.manager.RecordManager;
import com.palmjoys.yf1b.act.task.manager.TaskManager;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

@Component
public class GameLogicManager {
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private GameDataManager gameDataManager;
	@Autowired
	private MajiangFaPaiManager fapaiManager;
	@Autowired
	private RecordManager racordManager;
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private CorpsManager corpsManager;
	@Autowired
	private EventTriggerManager eventTriggerManager;
	@Autowired
	private TaskManager taskManager;
	@Autowired
	private HotPromptManager hotPromptManager;
	@Autowired
	private PlayerCorpsManager playerCorpsManager;
	@Autowired
	private CheckResetTimeManager resetTimeManager;
	@Autowired
	private RobotManager robotManager;
	
	//游戏逻辑运行线程
	private Thread gameThread = null;
	//游戏数据同步锁
	private Lock _lock = new ReentrantLock(); 
	//所有桌子列表
	private Map<Integer, TableAttrib> tableMap = new HashMap<Integer, TableAttrib>();
	//房间Id下标号
	private int tableIdIndex = 0;
	//房间号列表
	private List<List<String>> roomNOList = new ArrayList<List<String>>();
	//日志
	private static final Logger logger = LoggerFactory.getLogger(GameLogicManager.class);
	
	@PostConstruct
	protected void init() {
		initRoomNOList();
		robotManager.loadRobot();
		gameThread = new Thread(){
			@Override
			public void run() {
				super.run();
				while(true){
					try{
						runTable();
						Thread.sleep(1);
					}catch(Exception e){
					}
				}
			}
		};
		gameThread.setDaemon(true);
		gameThread.setName("麻将游戏逻辑运行线程");
		gameThread.start();
	}
	
	public void lock(){
		_lock.lock();
	}
	
	public void unLock(){
		_lock.unlock();
	}
	
	private void runTable(){
		_lock.lock();
		try{
			
			List<Integer> dels = new ArrayList<Integer>();
			Collection<TableAttrib> tables = tableMap.values();
			for(TableAttrib table : tables){
				table.run();
				if(table.bRemove){
					dels.add(table.tableId);
					//如果是帮会桌子推送帮会桌子消息
					if(null != table.corpsId && table.corpsId.equalsIgnoreCase("0") == false ){						
						corpsManager.sendCorpsTableDataChanageNotify(table, table.gameName, CorpsManager.CORPS_TABLE_CHANAGE_TYPE_DEL);
					}
				}
			}
			for(Integer tableId : dels){
				tableMap.remove(tableId);
			}
		}catch(Exception e){
		}
		finally{
			_lock.unlock();
		}
	}
	
	public void log(String str){
		logger.warn(str);
	}
	//初始化房间号
	private void initRoomNOList(){
		String[] digets = new String[]{"0","1","2","3","4","5","6","7","8","9"};
		for(int index=0; index<6; index++){
			List<String> tmpList = new ArrayList<String>();
			for(int i=1; i<10; i++){
				tmpList.add(digets[i]);
			}
			if(index != 0){
				tmpList.add(digets[0]);
			}
			Collections.shuffle(tmpList);
			Collections.shuffle(tmpList);
			Collections.shuffle(tmpList);
			roomNOList.add(tmpList);
		}
	}
	
	//获取房间Id
	public int getTableId(){
		int maxRoomIndex = 900000;
		if(tableIdIndex >= maxRoomIndex){
			tableIdIndex = 0;
		}
		long used = 0;
		int _idIndex = tableIdIndex;
		String stableId = "";
		int pow = roomNOList.size() - 1;
		for(List<String> strIds : roomNOList){
			long powVal = (long) Math.pow(10, pow);
			int tmpIndex = (int) (_idIndex/powVal);
			String idStr = strIds.get(tmpIndex);
			stableId += idStr;
			
			used = (powVal*tmpIndex);
			_idIndex -= used;
			pow = pow - 1;
		}
		tableIdIndex = tableIdIndex + 1;		
		return Integer.valueOf(stableId);
	}

	public TableAttrib createTable(long accountId, int tableCfgId, int createCost, String corpsId, List<Integer> rules,
			String password, SessionManager sessionManager, MajiangCfgManager cfgManager){
		int tableId = this.getTableId();
		TableAttrib table = new TableAttrib(tableId, tableCfgId, rules, corpsId, accountId, password,
				sessionManager, accountManager, this, fapaiManager, gameDataManager, 
				cfgManager, racordManager, eventTriggerManager);
		table.createCost = createCost;
		table.walletManager = this.walletManager;
		table.corpsManager = this.corpsManager;
		table.taskManager = this.taskManager;
		table.hotPromptManager = this.hotPromptManager;
		table.playerCorpsManager = this.playerCorpsManager;
		table.resetTimeManager = this.resetTimeManager;
		table.robotManager = this.robotManager;
		tableMap.put(table.tableId, table);
		return table;
	}
	
	public TableAttrib getTable(int tableId){
		return tableMap.get(tableId);
	}
	
	//座位数据转客户端显示对象
	public SeatVo Seat2SeatVo(TableAttrib table, SeatAttrib seat, int myIndex){
		SeatVo seatVo = new SeatVo();
		String headImg = "";
		String nick = "";
		int onLine = 0;
		int sex = 0;
		if(0 != seat.accountId){
			AccountEntity accountEntity = accountManager.load(seat.accountId);
			if(null != accountEntity){
				GameDataAttrib gameDataAttrib = gameDataManager.getGameData(seat.accountId);
				headImg = accountEntity.getHeadImg();
				nick = accountEntity.getNick();
				onLine = gameDataAttrib.onLine;
				sex = accountEntity.getSex();
			}
		}
		
		seatVo.seatIndex = seat.seatIndex;
		seatVo.accountId = String.valueOf(seat.accountId);
		seatVo.headImg = headImg;
		seatVo.nick = nick;
		seatVo.sex = sex;
		seatVo.onLine = onLine;
		seatVo.handCardsLen = seat.handCards.size();
		for(List<CardAttrib> cardList : seat.anGangCards){
			List<Integer> tmpList = new ArrayList<>();
			for(CardAttrib card : cardList){
				tmpList.add(card.cardId);
			}
			seatVo.anGangCards.add(tmpList);
		}
		for(List<CardAttrib> cardList : seat.baGangCards){
			List<Integer> tmpList = new ArrayList<>();
			for(CardAttrib card : cardList){
				tmpList.add(card.cardId);
			}
			seatVo.baGangCards.add(tmpList);
		}
		for(List<CardAttrib> cardList : seat.dianGangCards){
			List<Integer> tmpList = new ArrayList<>();
			for(CardAttrib card : cardList){
				tmpList.add(card.cardId);
			}
			seatVo.dianGangCards.add(tmpList);
		}
		for(List<CardAttrib> cardList : seat.pengCards){
			List<Integer> tmpList = new ArrayList<>();
			for(CardAttrib card : cardList){
				tmpList.add(card.cardId);
			}
			seatVo.pengCards.add(tmpList);
		}
		for(CardAttrib card : seat.outUnUseCards){
			seatVo.outUnUseCards.add(card.cardId);
		}
		for(CardAttrib card : seat.huCards){
			seatVo.huCards.add(card.cardId);
		}
		seatVo.gangType = seat.gangType;
		seatVo.score = seat.score;
		seatVo.btState = seat.btState;
		seatVo.unSuit = seat.unSuit;
		seatVo.huPaiType = seat.huPaiType;
		seatVo.huPaiIndex = seat.huPaiIndex;
		seatVo.moPaiCard = null==seat.moPaiCard?0:seat.moPaiCard.cardId;
		seatVo.outCard = null==seat.outCard?0:seat.outCard.cardId;
		seatVo.tangCardState = seat.tangCardState;
		seatVo.baojiaoState = seat.baoJiaoState;
		seatVo.piaoNum = seat.piaoNum;
		seatVo.trusteeshipState = seat.trusteeshipState;
		for(CardAttrib card : seat.tangCardList){
			seatVo.tangCardList.add(card.cardId);
		}
		
		//不可见数据
		if(seat.seatIndex == myIndex){
			for(CardAttrib card : seat.handCards){
				seatVo.handCards.add(card.cardId);
			}
			for(CardAttrib card : seat.swapCards){
				seatVo.swapCards.add(card.cardId);
			}
			seatVo.breakCard = null==seat.breakCard?0:seat.breakCard.cardId;
			for(int val : seat.breakCardState){
				seatVo.breakCardState.add(val);
			}
		}
		return seatVo;
	}	
	
	//桌子数据转为客户端桌子基础显示对象
	public TableBaseVo Table2TableBaseVo(TableAttrib table){
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		TableBaseVo retVo = new TableBaseVo();
		retVo.tableId = table.tableId;
		retVo.createPlayer = String.valueOf(table.createPlayer);
		retVo.bankerIndex = table.bankerIndex;
		retVo.currGameNum = table.currGameNum;
		retVo.maxGameNum = table.ruleAttrib.maxGameNum;
		retVo.tableCardNum = table.tableCards.size();
		retVo.svrTime = String.valueOf(currTime);
		retVo.actTime = String.valueOf(table.waitTime);
		retVo.gameState = table.gameState;
		retVo.btIndex = table.btIndex;
		retVo.ruleShowDesc = table.ruleShowDesc;
		retVo.tableChatType = table.ruleAttrib.chatType;	
		retVo.prevBtIndex = table.prevBtIndex;
		retVo.destoryQuestPlayer = String.valueOf(table.destoryQuestPlayer);
		retVo.nextGame = (table.bGameNext==true)?1:0;
		retVo.corpsId = table.corpsId;
		retVo.cfgId = table.cfgId;
		retVo.recordId = String.valueOf(table.recordId);
		retVo.tingTips = (table.ruleAttrib.bTingTips==true)?1:0;
		retVo.yaojiReplace = (table.ruleAttrib.bYaoJiRenYong==true)?1:0;
		retVo.handCardNum = table.ruleAttrib.handCardNum;
		retVo.maxPiaoPaiNum = table.ruleAttrib.maxPiaoNum;
		retVo.swapCardType = table.swapCardType;
		for(SeatAttrib seat : table.seats){
			for(CardAttrib canHuCard : seat.tangCanHuList){
				retVo.tangCanHuList.add(canHuCard.cardId);
			}
		}
		return retVo;
	}
	
	//桌子数据转客户端完整桌子显示数据
	public TableVo Table2TableVo(TableAttrib table){
		TableVo retVo = new TableVo();
		retVo.tableBaseVo = this.Table2TableBaseVo(table);
		for(SeatAttrib seat : table.seats){
			SeatVo seatVo = this.Seat2SeatVo(table, seat, seat.seatIndex);
			retVo.seats.add(seatVo);
		}		
		retVo.settlementOnce = table.calculation_OverOnce();
		retVo.settlementAll = table.calculation_OverAll();
		retVo.breakSeats = table.breakSeats;
		retVo.breakState = table.breakState;
		
		return retVo;
	}
	//查找指定帮会桌子
	public List<TableAttrib> findCorpsTables(String corpsId){
		List<TableAttrib> retList = new ArrayList<>();
		Collection<TableAttrib> tables = tableMap.values();
		for(TableAttrib table : tables){
			if(table.corpsId.equalsIgnoreCase(corpsId) == false){
				continue;
			}
			if(table.gameState != GameDefine.STATE_TABLE_IDLE){
				continue;
			}
			boolean bHaveEmptySeat = false;
			for(SeatAttrib seat : table.seats){
				if(0 == seat.accountId){
					bHaveEmptySeat = true;
					break;
				}
			}
			if(bHaveEmptySeat){
				retList.add(table);
			}
		}
		return retList;
	}
}
