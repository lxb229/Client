package com.palmjoys.yf1b.act.dzpker.manager;

import java.util.ArrayList;
import java.util.Collection;
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

import com.palmjoys.yf1b.act.dzpker.entity.DzpkerTableRecordEntity;
import com.palmjoys.yf1b.act.dzpker.model.CardAttrib;
import com.palmjoys.yf1b.act.dzpker.model.DzpkTableBetPool;
import com.palmjoys.yf1b.act.dzpker.model.DzpkerDefine;
import com.palmjoys.yf1b.act.dzpker.model.GameDefine;
import com.palmjoys.yf1b.act.dzpker.model.SeatAttrib;
import com.palmjoys.yf1b.act.dzpker.model.SeatVo;
import com.palmjoys.yf1b.act.dzpker.model.StatisticsAttrib;
import com.palmjoys.yf1b.act.dzpker.model.TableAttrib;
import com.palmjoys.yf1b.act.dzpker.model.TableVo;
import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;
import com.palmjoys.yf1b.act.account.entity.RoleEntity;
import com.palmjoys.yf1b.act.account.manager.AccountManager;
import com.palmjoys.yf1b.act.account.manager.GameDataManager;
import com.palmjoys.yf1b.act.account.manager.RoleEntityManager;

@Component
public class GameLogicManager {
	@Autowired
	private DzpkerRecordManager recordManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private TableIdManager tableIdManager;
	@Autowired
	private GameDataManager gameDataManager;
	@Autowired
	private RoleEntityManager roleEntityManager;
	@Autowired
	private DzpkerCfgManager cfgManager;
	@Autowired
	private EventTriggerManager eventTriggerManager;
	
	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(GameLogicManager.class);
	// 数据操作锁
	private Lock _lock = new ReentrantLock();
	//桌子列表
	private Map<Integer, TableAttrib> tableMap = new HashMap<>();
	//游戏逻辑线程
	private Thread _thread = null;
	
	@PostConstruct
	protected void init() {
		_thread = new Thread(){
			@Override
			public void run() {
				super.run();
				while(true){
					try{
						tableRun();
						Thread.sleep(1);
					}catch(Exception e){
					}
				}
			}
		};
		_thread.setDaemon(true);
		_thread.setName("德州扑克游戏线程");
		_thread.start();
	}
	
	public void lock(){
		this._lock.lock();
	}
	
	public void unlock(){
		this._lock.unlock();
	}

	public void log(String log){
		logger.debug(log);
	}
	
	private void tableRun(){
		this.lock();
		try{
			List<Integer> delIds = new ArrayList<>();
			Collection<TableAttrib> tables = tableMap.values();
			for(TableAttrib table : tables){
				try{
					table.run();
					if(table.bRemove){
						delIds.add(table.tableId);
					}
				}catch(Exception e){
					this.log("桌子异常!");
				}
			}
			for(Integer tableId : delIds){
				tableMap.remove(tableId);
			}
		}finally{
			this.unlock();
		}
	}
	
	public TableAttrib getTable(int tableId){
		return this.tableMap.get(tableId);
	}
	
	public int createTable(long createPlayer, String tableName, int smallBlind, int bigBlind, 
			int joinChip, long createTime, long vaildTime, int insurance, int straddle, int buyMaxChip){
		
		//检查桌子名称
		Collection<TableAttrib> tables = tableMap.values();
		for(TableAttrib table : tables){
			if(tableName.equalsIgnoreCase(table.tableName)){
				return DzpkerDefine.DZPKER_ERROR_TABLE_NAME;
			}
		}
		
		int tableId = tableIdManager.getTableId();
		if(tableId <= 0){
			return DzpkerDefine.DZPKER_ERROR_EMPTY_TABLENO;
		}
		TableAttrib table = new TableAttrib(tableId, createPlayer, tableName, smallBlind, 
				bigBlind, joinChip, createTime, vaildTime, insurance, straddle, buyMaxChip,
				this.accountManager, this.sessionManager, this, this.recordManager, 
				this.gameDataManager, cfgManager, roleEntityManager, eventTriggerManager);
		
		this.tableMap.put(table.tableId, table);
		
		return table.tableId;
	}
	
	public TableVo table2TableVo(TableAttrib table){
		TableVo retVo = new TableVo();
		retVo.tableId = table.tableId;
		retVo.createPlayer = String.valueOf(table.createPlayer);
		retVo.tableName = table.tableName;
		retVo.smallBlind = table.smallBlind;
		retVo.bigBlind = table.bigBlind;
		retVo.joinChip = table.joinChip;
		retVo.buyMaxChip = table.buyMaxChip;
		retVo.createTime = String.valueOf(table.createTime);
		retVo.vaildTime = String.valueOf(table.vaildTime);
		for(CardAttrib cardAttrib : table.tableHandCards){
			retVo.tableHandCards.add(cardAttrib.cardId);
		}
		Map<Long, StatisticsAttrib> statisticsMap = null;
		DzpkerTableRecordEntity tableRecordEntity = recordManager.loadTableRecord(table.recordId);
		if(null != tableRecordEntity){
			statisticsMap = tableRecordEntity.getStatisticsMap();
		}
		
		for(SeatAttrib seat : table.seats){
			SeatVo seatVo = new SeatVo();
			String starNO = "";
			String nick = "";
			String headImg = "";
			long currMoney = 0;
			long betMoney = 0;
			seatVo.seatIndex = seat.seatIndex;
			seatVo.accountId = String.valueOf(seat.accountId);
			if(seat.accountId > 0){
				RoleEntity roleEntity = roleEntityManager.findOf_accountId(seat.accountId);
				if(null != roleEntity){
					starNO = roleEntity.getStarNO();
					nick = roleEntity.getNick();
					headImg = roleEntity.getHeadImg();
				}
				if(null != statisticsMap){
					StatisticsAttrib statisticsAttrib = statisticsMap.get(seat.accountId);
					if(null != statisticsAttrib){
						currMoney = statisticsAttrib.currMoney;
					}
				}
				betMoney = table.once_betpool.getBetMoneyOfAccountId(seat.accountId);
			}
			
			seatVo.starNO = starNO;
			seatVo.nick = nick;
			seatVo.headImg = headImg;
			for(CardAttrib cardAttrib : seat.handCars){
				seatVo.handCards.add(cardAttrib.cardId);
			}
			
			seatVo.currMoney = String.valueOf(currMoney);
			seatVo.betMoney = String.valueOf(betMoney);
			seatVo.btState = seat.btState;
			seatVo.btResult = seat.btResult;
			seatVo.bGamed = seat.bGamed==true?1:0;
			seatVo.cardType = seat.cardsResult==null?GameDefine.TYPE_CARD_NONE:seat.cardsResult.cardType;
			seatVo.straddle = seat.straddle;
			seatVo.straddleFlag = seat.straddleFlag;
			
			retVo.seats.add(seatVo);
		}
		retVo.prevBtIndex = table.prevBtIndex;
		retVo.maxBetMoney = String.valueOf(table.once_betpool.getMaxBetMoney());
		retVo.gameState = table.gameState;
		retVo.actTotalTime = table.actTotalTime;
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		retVo.svrTime = String.valueOf(currTime);
		retVo.actTime = String.valueOf(table.waitTime);
		retVo.btIndex = table.btIndex;
		retVo.bankerIndex = table.bankerIndex;
		retVo.start = table.start;
		retVo.gameNum = table.gameNum;
		retVo.smallSeatIndex = table.smallSeatIndex;
		retVo.bigSeatIndex = table.bigSeatIndex;
		retVo.showCardSeatIndex = table.showCardSeatIndex;
		retVo.bShowCardFlag = table.bTableShowCardState==true?1:0;
		if(table.bTableShowCardState){
			retVo.showCardSeatIndex = -1;
		}
		
		long tmpMoney = 0;
		long tmpPoolMoneys = 0;
		for(DzpkTableBetPool pool : table.client_all_in_pools){
			tmpMoney = pool.countBetMoney();
			if(tmpMoney > 0){
				retVo.pools.add(String.valueOf(tmpMoney));
			}
		}
		
		for(DzpkTableBetPool pool : table.all_in_pools){
			tmpPoolMoneys += pool.countBetMoney();
		}
		tmpPoolMoneys += table.un_all_in_betpool.countBetMoney();
		retVo.poolMoneys = String.valueOf(tmpPoolMoneys);
				
		retVo.settlementOnceList.addAll(table.settlementOnceList);
		if(table.gameState == GameDefine.STATE_TABLE_BUY_INSURANCE){
			retVo.insuranceStateAttrib = table.insuranceStateAttrib;
		}
		
		return retVo;
	}
	
}
