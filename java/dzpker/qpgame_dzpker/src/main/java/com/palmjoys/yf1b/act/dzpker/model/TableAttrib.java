package com.palmjoys.yf1b.act.dzpker.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.dzpker.entity.DzpkerCfgEntity;
import com.palmjoys.yf1b.act.dzpker.entity.DzpkerPlayerRecordEntity;
import com.palmjoys.yf1b.act.dzpker.entity.DzpkerTableRecordEntity;
import com.palmjoys.yf1b.act.dzpker.manager.DzpkerCfgManager;
import com.palmjoys.yf1b.act.dzpker.manager.DzpkerRecordManager;
import com.palmjoys.yf1b.act.dzpker.manager.GameLogicManager;
import com.palmjoys.yf1b.act.dzpker.model.InsuranceStateAttrib.InsuranceBuyAttrib;
import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;
import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.account.entity.RoleEntity;
import com.palmjoys.yf1b.act.account.manager.AccountManager;
import com.palmjoys.yf1b.act.account.manager.GameDataManager;
import com.palmjoys.yf1b.act.account.manager.RoleEntityManager;
import com.palmjoys.yf1b.act.account.model.GameDataAttrib;
import com.palmjoys.yf1b.act.framework.utils.MessagePushQueueUtils;

public class TableAttrib {
	//桌子Id
	public int tableId;
	//桌子创建玩家
	public long createPlayer;
	//桌子名称
	public String tableName;
	//小盲注
	public int smallBlind;
	//大盲注
	public int bigBlind;
	//最小加入筹码
	public int joinChip;
	//桌子创建时间
	public long createTime;
	//桌子到期时间
	public long vaildTime;
	//是否开启保险玩法
	public int insurance;
	//是否开启闭眼盲注玩法
	public int straddle;
	//筹码购入上限
	public int buyMaxChip;	
	//桌子牌
	public List<CardAttrib> tableHandCards;
	//加入此桌子的玩家
	public Map<Long, Long> tablePlayers;
	//座位列表
	public List<SeatAttrib> seats;
	//动作总时间
	public int actTotalTime;
	//桌子当前游戏状态
	public int gameState;
	//桌子下一个游戏状态
	public int nextGameState;
	//当前游戏状态执行状态
	public boolean bActExec;
	//当前游戏状态过期时间
	public long waitTime;
	//当前表态座位
	public int btIndex;
	//是否销毁桌子
	public boolean bRemove;
	//本局游戏庄家位置
	public int bankerIndex;
	//桌子是否开始运行
	public int start;
	//桌子游戏局数
	public int gameNum;
	//小盲位置
	public int smallSeatIndex;
	//大盲位置
	public int bigSeatIndex;
	//all_In边池子列表(每一局开始清空所有边池)
	public List<DzpkTableBetPool> all_in_pools;
	//非all_in池子
	public DzpkTableBetPool un_all_in_betpool;
	// 每轮压注数据(共4轮,每轮表态前清空,分别是手牌,3张底牌,第4张底牌,第5张底牌)
	public DzpkTableBetPool once_betpool;
	//桌子上初始牌
	public List<CardAttrib> tableUsedInitCards;
	//桌子使用的牌
	public List<CardAttrib> tableUsedCards;
	//桌子使用的牌索引下标
	public int tableUsedCardIndex;
	//本桌子在游戏记录表中的唯一Id
	public long recordId;
	//单局结算数据
	public List<SettlementOnceVo> settlementOnceList;
	//购买保险状态数据
	public InsuranceStateAttrib insuranceStateAttrib;
	//亮牌座位(-1=无亮牌座位)
	public int showCardSeatIndex;
	//上一个表态玩家
	public int prevBtIndex;
	//客户端池子
	public List<DzpkTableBetPool> client_all_in_pools;
	//是否已秀过牌了
	public boolean bTableShowCardState;
	//保存的下一游戏状态
	public int saveNextGameState;
	
	//管理 类
	private AccountManager accountManager;
	private SessionManager sessionManager;
	private GameLogicManager gameLogicManager;
	private DzpkerRecordManager recordManager;
	private GameDataManager gameDataManager;
	private DzpkerCfgManager cfgManager;
	private RoleEntityManager roleEntityManager;
	private EventTriggerManager eventTriggerManager;
	
	public TableAttrib(int tableId, long createPlayer, String tableName, int smallBlind, int bigBlind, 
			int joinChip, long createTime, long vaildTime, int insurance, int straddle, int buyMaxChip,
			AccountManager accountManager, SessionManager sessionManager, GameLogicManager gameLogicManager,
			DzpkerRecordManager recordManager, GameDataManager gameDataManager, DzpkerCfgManager cfgManager,
			RoleEntityManager roleEntityManager, EventTriggerManager eventTriggerManager){
		this.tableId = tableId;
		this.createPlayer = createPlayer;
		this.tableName = tableName;
		this.smallBlind = smallBlind;
		this.bigBlind = bigBlind;
		this.joinChip = joinChip;
		this.createTime = createTime;
		this.vaildTime = vaildTime;
		this.insurance = insurance;
		this.straddle = straddle;
		this.buyMaxChip = buyMaxChip;
		this.accountManager = accountManager;
		this.sessionManager = sessionManager;
		this.gameLogicManager = gameLogicManager;
		this.recordManager = recordManager;
		this.gameDataManager = gameDataManager;
		this.cfgManager = cfgManager;
		this.roleEntityManager = roleEntityManager;
		this.eventTriggerManager = eventTriggerManager;
		
		this.tableHandCards = new ArrayList<>();
		this.tablePlayers = new HashMap<>();
		this.seats = new ArrayList<>();
		for(int index=0; index<GameDefine.MAX_SEAT_NUM; index++){
			SeatAttrib seat = new SeatAttrib(index);
			this.seats.add(seat);
		}
		
		this.gameState = GameDefine.STATE_TABLE_IDLE;
		this.nextGameState = GameDefine.STATE_TABLE_IDLE;
		this.bActExec = false;
		this.waitTime = 0;
		this.btIndex = 0;
		this.bRemove = false;
		this.bankerIndex = -1;
		this.showCardSeatIndex = -1;
		this.start = 0;
		this.gameNum = 1;
		this.smallSeatIndex = 0;
		this.bigSeatIndex = 0;
		this.all_in_pools = new ArrayList<>();
		this.un_all_in_betpool = new DzpkTableBetPool();
		this.once_betpool = new DzpkTableBetPool();
		this.client_all_in_pools = new ArrayList<>();
		this.actTotalTime = 0;
		this.prevBtIndex = -1;
		this.tableUsedInitCards = null;
		this.tableUsedCards = new ArrayList<>();
		this.tableUsedCardIndex = 0;
		this.settlementOnceList = new ArrayList<>();
		this.insuranceStateAttrib = new InsuranceStateAttrib();
		this.bTableShowCardState = false;
		this.saveNextGameState = 0;
		
		DzpkerTableRecordEntity recordEntity = this.recordManager.createTableRecord(this.tableId, 
				this.tableName, this.createPlayer, this.createTime);
		this.recordId = recordEntity.getId();
		
		StatisticsAttrib statisticsAttrib = this.recordManager.getStatisticsAttrib(this.recordId, this.createPlayer);
		if(null != statisticsAttrib){
			statisticsAttrib.joinTableNum++;
			statisticsAttrib.currMoney += this.joinChip;
			statisticsAttrib.buyTotalMoney += this.joinChip;
			this.recordManager.setStatisticsAttrib(this.recordId, this.createPlayer, statisticsAttrib);
		}
		
		RoleEntity roleEntity = roleEntityManager.findOf_accountId(this.createPlayer);
		EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_CREATE);
		eventAttrib.addEventParam(this.tableId);
		eventAttrib.addEventParam(this.tableName);
		eventAttrib.addEventParam(roleEntity.getStarNO());
		eventAttrib.addEventParam(roleEntity.getNick());
		eventAttrib.addEventParam(this.createTime);
		int tmpVaildTime = (int) ((this.vaildTime-this.createTime)/60/1000);
		eventAttrib.addEventParam(tmpVaildTime);
		eventAttrib.addEventParam(""+this.smallBlind+"/"+this.bigBlind);
		eventAttrib.addEventParam(this.joinChip);
		eventAttrib.addEventParam(this.insurance);
		eventAttrib.addEventParam(this.straddle);
		eventTriggerManager.triggerEvent(eventAttrib);
		
		eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_BUY_CHIP);
		eventAttrib.addEventParam(roleEntity.getStarNO());
		eventAttrib.addEventParam(this.tableId);
		eventAttrib.addEventParam(roleEntity.getStarNO());
		eventAttrib.addEventParam(this.joinChip);
		eventAttrib.addEventParam(eventAttrib.eventTime);
		eventTriggerManager.triggerEvent(eventAttrib);
	}
	
	public SeatAttrib getEmptySeat(){
		for(int index=0; index<this.seats.size(); index++){
			SeatAttrib seat = this.seats.get(index);
			if(seat.accountId <= 0){
				return seat;
			}
		}
		
		return null;
	}
	
	public SeatAttrib getPlayerSeat(long accountId){
		for(int index=0; index<this.seats.size(); index++){
			SeatAttrib seat = this.seats.get(index);
			if(seat.accountId == accountId){
				return seat;
			}
		}
		
		return null;
	}
	
	public SeatAttrib getSeat(int seatIndex){
		if(seatIndex < 0 || seatIndex >= this.seats.size()){
			return null;
		}
		return this.seats.get(seatIndex);
	}
	
	//获取座位玩家数量
	public int getSeatPlayerNum(){
		int num = 0;
		for(int index=0; index<this.seats.size(); index++){
			SeatAttrib seat = this.seats.get(index);
			if(seat.accountId > 0){
				num++;
			}
		}
		return num;
	}
	
	//获取本局游戏存活的玩家数量
	public int getGamedPlayerNum(){
		int num = 0;
		for(int index=0; index<this.seats.size(); index++){
			SeatAttrib seat = this.seats.get(index);
			if(seat.accountId > 0 && seat.bGamed
					&& seat.btResult != GameDefine.ACT_TYPE_DROP){
				num++;
			}
		}
		return num;
	}
	
	//获取本局游戏还需表态的玩家数量
	public int getGamedNeedBtPlayerNum(){
		int num = 0;
		for(int index=0; index<this.seats.size(); index++){
			SeatAttrib seat = this.seats.get(index);
			if(seat.accountId > 0 && seat.bGamed){
				if(seat.btState == GameDefine.BT_STATE_WAIT){
					num++;
				}else{
					if(seat.btState == GameDefine.BT_STATE_BT){
						if(seat.btResult == GameDefine.ACT_TYPE_PASS
								|| seat.btResult == GameDefine.ACT_TYPE_SAME
								|| seat.btResult == GameDefine.ACT_TYPE_ADD){
							num++;
						}
					}
				}
			}
		}
		return num;
	}
	
	//获取当前座位的下一个座位
	public SeatAttrib getNextSeat(int currIndex){
		int nextIndex = (currIndex+1)%this.seats.size();
		return this.seats.get(nextIndex);
	}
	
	//获取当前参加了游戏的下一个座位
	public SeatAttrib getNextGamedSeat(int currIndex){
		int nextIndex = (currIndex+1)%this.seats.size();
		while(true){
			SeatAttrib seat = this.seats.get(nextIndex);
			if(seat.accountId > 0 && seat.bGamed){
				return seat;
			}
			if(seat.seatIndex == currIndex){
				break;
			}
			nextIndex = (nextIndex+1)%this.seats.size();
		}	
		return null;
	}
	
	//获取下一个表态座位
	public SeatAttrib getNextBtSeat(int currIndex){
		int nextIndex = (currIndex+1)%this.seats.size();
		while(true){
			SeatAttrib seat = this.seats.get(nextIndex);
			if(seat.accountId > 0 && seat.bGamed){
				if(seat.btState == GameDefine.BT_STATE_WAIT){
					return seat;
				}else if(seat.btState == GameDefine.BT_STATE_BT){
					if(seat.btResult == GameDefine.ACT_TYPE_PASS
							|| seat.btResult == GameDefine.ACT_TYPE_SAME
							|| seat.btResult == GameDefine.ACT_TYPE_ADD){
						return seat;
					}
				}
			}
			if(seat.seatIndex == currIndex){
				break;
			}
			nextIndex = (nextIndex+1)%this.seats.size();
		}	
		return null;
	}	
	
	public void reset(){
		for(int index=0; index<this.seats.size(); index++){
			SeatAttrib seat = this.seats.get(index);
			seat.reset();
		}
		this.insuranceStateAttrib.resetAll();
		this.showCardSeatIndex = -1;
		this.tableHandCards.clear();
		this.prevBtIndex = -1;
		this.once_betpool.clear();
		this.all_in_pools.clear();
		this.un_all_in_betpool.clear();
		this.bTableShowCardState = false;
	}
	
	public void sendStateNotify(int notifyMsg){
		TableVo retVo = this.gameLogicManager.table2TableVo(this);
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(notifyMsg,
				Result.valueOfSuccess(retVo));
		MessagePushQueueUtils.getPushQueue(sessionManager).push(this.tablePlayers.keySet(), pushMsg);
	}	
	
	public void run(){
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		
		switch(this.gameState){
		case GameDefine.STATE_TABLE_IDLE:
			run_idle(currTime);
			break;
		case GameDefine.STATE_TABLE_READY:
			run_ready(currTime);
			break;
		case GameDefine.STATE_TABLE_BETBLIND:
			run_betblind(currTime);
			break;
		case GameDefine.STATE_TABLE_OUTCARD_1:
			run_outcard_1(currTime);
			break;
		case GameDefine.STATE_TABLE_BET_BT_1:
			run_bet_bt_1(currTime);
			break;
		case GameDefine.STATE_TABLE_OUTCARD_2:
			run_outcard_2(currTime);
			break;
		case GameDefine.STATE_TABLE_BET_BT_2:
			run_bet_bt_2(currTime);
			break;
		case GameDefine.STATE_TABLE_OUTCARD_3:
			run_outcard_3(currTime);
			break;
		case GameDefine.STATE_TABLE_BET_BT_3:
			run_bet_bt_3(currTime);
			break;
		case GameDefine.STATE_TABLE_OUTCARD_4:
			run_outcard_4(currTime);
			break;
		case GameDefine.STATE_TABLE_BET_BT_4:
			run_bet_bt_4(currTime);
			break;
		case GameDefine.STATE_TABLE_BUY_INSURANCE:
			run_buy_insurance(currTime);
			break;
		case GameDefine.STATE_TABLE_OVER_ONCE:
			run_over_once(currTime);
			break;
		case GameDefine.STATE_TABLE_OVER_ALL:
			run_over_all(currTime);
			break;
		case GameDefine.STATE_TABLE_NEW_ROUND_BET:
			run_new_round_bet(currTime);
			break;
		case GameDefine.STATE_TABLE_ALL_IN_SHOW_CARD:
			run_allin_showcard(currTime);
			break;
		case GameDefine.STATE_TABLE_WAIT:
			run_wait(currTime);
			break;
		}
		
		if(this.bRemove){
			RoleEntity roleEntity = roleEntityManager.findOf_accountId(this.createPlayer);
			EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_REMOVE);
			eventAttrib.addEventParam(this.tableId);
			eventAttrib.addEventParam(roleEntity.getStarNO());
			eventAttrib.addEventParam(eventAttrib.eventTime);
			eventTriggerManager.triggerEvent(eventAttrib);
		}
	}
	
	private void run_idle(long currTime){
		if(currTime > this.vaildTime){
			//桌子游戏时间已到期
			this.bActExec = false;
			this.gameState = GameDefine.STATE_TABLE_OVER_ALL;
			return;
		}else{
			if(this.tablePlayers.isEmpty()){
				//桌子没人了
				this.bActExec = false;
				this.gameState = GameDefine.STATE_TABLE_OVER_ALL;
				return;
			}
		}
		
		int N = this.getSeatPlayerNum();
		if(N < 2){
			this.start = 0;
		}
		if(this.bActExec == false){
			this.reset();
			this.bActExec = true;
			this.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_TABLE_STATE_NOTIFY);
			this.gameLogicManager.log("游戏进入空闲状态");
		}
		if(this.start == 0){
			//桌子游戏未开始
			return;
		}
		
		if(N < 2){
			this.gameState = GameDefine.STATE_TABLE_IDLE;
			this.start = 0;
		}else{
			this.gameState = GameDefine.STATE_TABLE_READY;
			this.bActExec = false;
		}
	}
	
	private void run_ready(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			this.waitTime = currTime + GameDefine.TIME_TABLE_READY;
			this.actTotalTime = GameDefine.TIME_TABLE_READY;
			this.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_TABLE_STATE_NOTIFY);
			this.gameLogicManager.log("游戏进入准备状态");
		}
		
		int N = this.getSeatPlayerNum();
		if(N < 2){
			this.bActExec = false;
			this.gameState = GameDefine.STATE_TABLE_IDLE;
			this.start = 0;
		}else{
			if(currTime > this.waitTime){
				String createPlayerStarNO = "";
				RoleEntity roleEntity = roleEntityManager.findOf_accountId(this.createPlayer);
				createPlayerStarNO = roleEntity.getStarNO();
				
				DzpkerTableRecordEntity recordEntity = this.recordManager.loadTableRecord(this.recordId);
				Map<Long, StatisticsAttrib> statisticsMap = recordEntity.getStatisticsMap();
				Map<Integer, WinScoreAttrib> detailedScoreMap = recordEntity.getDetailedScoreMap();
				WinScoreAttrib winScoreAttrib = detailedScoreMap.get(this.gameNum);
				if(null == winScoreAttrib){
					winScoreAttrib = new WinScoreAttrib();
				}
				winScoreAttrib.gameNum = this.gameNum;
				
				for(int index=0; index<this.seats.size(); index++){
					SeatAttrib seat = this.seats.get(index);
					if(seat.accountId > 0){
						seat.bGamed = true;
						StatisticsAttrib statisticsAttrib = statisticsMap.get(seat.accountId);
						if(null == statisticsAttrib){
							statisticsAttrib = new StatisticsAttrib();
						}
						
						//上局游戏中购买的筹码计入当前筹码
						statisticsAttrib.currMoney += statisticsAttrib.gameBuyChip;
						statisticsAttrib.gameBuyChip = 0;
						statisticsAttrib.betTotalMoney = 0;
						statisticsAttrib.gameTotalNum++;
						statisticsMap.put(seat.accountId, statisticsAttrib);
						
						WinScoreAttribItem winScoreAttribInner = winScoreAttrib.getPlayerScore(seat.accountId);
						winScoreAttrib.setPlayerScore(seat.accountId, winScoreAttribInner);
						
						//记录玩家参与过游戏的桌子
						DzpkerPlayerRecordEntity playerRecordEntity = this.recordManager.loadOrCreatePlayerRecord(seat.accountId);
						Map<Long, Long> recordTableMap = playerRecordEntity.getRecordTableMap();
						if(recordTableMap.containsKey(this.recordId) == false){
							recordTableMap.put(this.recordId, this.recordId);
							playerRecordEntity.setRecordTableMap(recordTableMap);
						}
						
						roleEntity = roleEntityManager.findOf_accountId(seat.accountId);
						EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_SEATDOWN);
						eventAttrib.addEventParam(roleEntity.getStarNO());
						eventAttrib.addEventParam(this.tableId);
						eventAttrib.addEventParam(createPlayerStarNO);
						eventAttrib.addEventParam(this.gameNum);
						eventAttrib.addEventParam(eventAttrib.eventTime);
						eventTriggerManager.triggerEvent(eventAttrib);
					}
				}
				
				this.bActExec = false;
				this.gameState = GameDefine.STATE_TABLE_BETBLIND;
				
				SeatAttrib bankerSeat = null;
				//定庄家
				if(this.gameNum == 1){
					//第一局
					bankerSeat = this.getNextGamedSeat(-1);
				}else{
					bankerSeat = this.getNextGamedSeat(this.bankerIndex);
				}
				this.bankerIndex = bankerSeat.seatIndex;
				//小盲座位
				SeatAttrib smallSeat = this.getNextGamedSeat(this.bankerIndex);
				this.smallSeatIndex = smallSeat.seatIndex;
				//大盲座位
				SeatAttrib bigSeat = this.getNextGamedSeat(this.smallSeatIndex);
				this.bigSeatIndex = bigSeat.seatIndex;
				//第一个表态玩家
				this.btIndex = this.getNextGamedSeat(this.bigSeatIndex).seatIndex;
				//清空每轮下注池
				this.once_betpool.clear();
				//清空非all_in下注池
				this.un_all_in_betpool.clear();
				//清空边池
				this.all_in_pools.clear();
				
				//增加大小盲压注信息到每轮下注池
				this.once_betpool.addBetMoney(smallSeat.accountId, this.smallBlind);
				this.once_betpool.addBetMoney(bigSeat.accountId, this.bigBlind);
				this.un_all_in_betpool.addBetMoney(smallSeat.accountId, this.smallBlind);
				this.un_all_in_betpool.addBetMoney(bigSeat.accountId, this.bigBlind);
				
				//添加到玩家下注信息
				StatisticsAttrib statisticsAttrib = statisticsMap.get(smallSeat.accountId);
				statisticsAttrib.betTotalMoney  += this.smallBlind;
				statisticsAttrib.currMoney -= this.smallBlind;
				if(this.smallBlind > statisticsAttrib.maxBetMoney){
					statisticsAttrib.maxBetMoney = this.smallBlind;
				}
				statisticsMap.put(smallSeat.accountId, statisticsAttrib);
				
				statisticsAttrib = statisticsMap.get(bigSeat.accountId);
				statisticsAttrib.betTotalMoney += this.bigBlind;
				statisticsAttrib.currMoney -= this.bigBlind;
				if(this.bigBlind > statisticsAttrib.maxBetMoney){
					statisticsAttrib.maxBetMoney = this.bigBlind;
				}
				statisticsMap.put(bigSeat.accountId, statisticsAttrib);
				
				recordEntity.setStatisticsMap(statisticsMap);				
				detailedScoreMap.put(this.gameNum, winScoreAttrib);
				recordEntity.setDetailedScoreMap(detailedScoreMap);
				
				//初始化桌子使用的牌
				this.tableUsedCardIndex = 0;
				if(null == this.tableUsedInitCards){
					this.tableUsedInitCards = GameDefine.initCards();
				}
				Collections.shuffle(this.tableUsedInitCards);
				Collections.shuffle(this.tableUsedInitCards);
				Collections.shuffle(this.tableUsedInitCards);
				
				List<CardAttrib> tmpList = new ArrayList<>();
				tmpList.addAll(this.tableUsedInitCards);
				this.tableUsedCards.clear();
				for(int i=0; i<25; i++){
					int cardIndex = (int) ((Math.random()*1000)%tmpList.size());
					this.tableUsedCards.add(tmpList.get(cardIndex));
					tmpList.remove(cardIndex);
				}
				this.tableUsedCards.addAll(tmpList);
				tmpList = null;
								
				roleEntity = roleEntityManager.findOf_accountId(smallSeat.accountId);
				EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_BET);
				eventAttrib.addEventParam(roleEntity.getStarNO());
				eventAttrib.addEventParam(this.tableId);
				eventAttrib.addEventParam(createPlayerStarNO);
				eventAttrib.addEventParam(this.gameNum);
				eventAttrib.addEventParam(this.smallBlind);
				eventAttrib.addEventParam(eventAttrib.eventTime);
				eventTriggerManager.triggerEvent(eventAttrib);
				
				roleEntity = roleEntityManager.findOf_accountId(bigSeat.accountId);
				eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_BET);
				eventAttrib.addEventParam(roleEntity.getStarNO());
				eventAttrib.addEventParam(this.tableId);
				eventAttrib.addEventParam(createPlayerStarNO);
				eventAttrib.addEventParam(this.gameNum);
				eventAttrib.addEventParam(this.bigBlind);
				eventAttrib.addEventParam(eventAttrib.eventTime);
				eventTriggerManager.triggerEvent(eventAttrib);
			}
		}
	}
	
	private void run_betblind(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			this.waitTime = currTime + GameDefine.TIME_TABLE_BETBLIND;
			this.actTotalTime = GameDefine.TIME_TABLE_BETBLIND;
			
			int N = this.getGamedPlayerNum();
			if(N >=4 && this.straddle > 0){
				//游戏人数4人以上且选择开启闭眼盲注玩法
				SeatAttrib tmpSeat = this.getNextGamedSeat(this.bigSeatIndex);
				if(null != tmpSeat){
					long maxMoney = this.once_betpool.getMaxBetMoney();
					DzpkerTableRecordEntity recordEntity = this.recordManager.loadTableRecord(this.recordId);
					Map<Long, StatisticsAttrib> statisticsMap = recordEntity.getStatisticsMap();
					StatisticsAttrib statisticsAttrib = statisticsMap.get(tmpSeat.accountId);
					if(null != statisticsAttrib){
						if(statisticsAttrib.currMoney >= maxMoney*2){
							tmpSeat.straddle = 1;
							this.waitTime = currTime + GameDefine.TIME_TABLE_STRADDLE_BET;
							this.actTotalTime = GameDefine.TIME_TABLE_STRADDLE_BET;
						}
					}
				}
			}
			this.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_TABLE_STATE_NOTIFY);
			this.gameLogicManager.log("游戏进入下注大小盲状态");
		}
		
		int activePlayerNum = this.getGamedPlayerNum();
		if(activePlayerNum == 1){
			//只有1个玩家了,直接结算
			this.gameState = GameDefine.STATE_TABLE_OVER_ONCE;
			this.bActExec = false;
			this.createPools();
			return;
		}
		
		if(currTime > this.waitTime){
			this.bActExec = false;
			this.gameState = GameDefine.STATE_TABLE_OUTCARD_1;
			return;
		}
	}
	
	private void run_outcard_1(long currTime){
		run_outcard_inner(currTime, 1);
	}
	
	private void run_bet_bt_1(long currTime){
		run_bet_bt_inner(currTime, 1);
	}
	
	private void run_outcard_2(long currTime){
		run_outcard_inner(currTime, 2);
	}
	
	private void run_bet_bt_2(long currTime){
		run_bet_bt_inner(currTime, 2);
	}
	
	private void run_outcard_3(long currTime){
		run_outcard_inner(currTime, 3);
	}
	
	private void run_bet_bt_3(long currTime){
		run_bet_bt_inner(currTime, 3);
	}
	
	private void run_outcard_4(long currTime){
		run_outcard_inner(currTime, 4);
	}
	
	private void run_bet_bt_4(long currTime){
		run_bet_bt_inner(currTime, 4);
	}
	
	private void run_buy_insurance(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			
			this.waitTime = currTime + GameDefine.TIME_TABLE_BUY_INSURANCE;
			this.actTotalTime = GameDefine.TIME_TABLE_BUY_INSURANCE;
			this.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_TABLE_STATE_NOTIFY);
			this.gameLogicManager.log("进入保险购买状态");
		}
		
		if(currTime < this.waitTime){
			return;
		}
		this.gameState = this.nextGameState;
		this.bActExec = false;
		//超时了,本轮不够买保险
		int tmpTotalMoney = 0;
		if(this.insuranceStateAttrib.round == 2){
			int roundIndex = this.insuranceStateAttrib.round-1;
			long accountId1 = this.insuranceStateAttrib.buyInsuranceRound[roundIndex].accountId;
			long accountId2 = this.insuranceStateAttrib.buyInsuranceRound[roundIndex+1].accountId;
			if(accountId1 == accountId2 && accountId1 > 0 && accountId2>0){
				//两轮是同一个玩家买保险,第-轮买了第二轮不买需要计算强制保险
				int outs = this.insuranceStateAttrib.winCardList.size();
				long buyMoney = this.insuranceStateAttrib.buyInsuranceRound[roundIndex].buyMoney;
				int exMoney = 0;
				float nRate = 0.0f;
				DzpkerCfgEntity cfgEntity = this.cfgManager.loadOrCreate();
				List<InsuranceCfgAttrib> insuranceList = cfgEntity.getInsuranceList();
				for(InsuranceCfgAttrib cfgAttrib : insuranceList){
					nRate = Float.parseFloat(cfgAttrib.rate);
					if(cfgAttrib.cardNum == outs){
						nRate = Float.parseFloat(cfgAttrib.rate);
						break;
					}
				}
				exMoney = (int) (buyMoney/nRate);
				tmpTotalMoney = exMoney;
				
				this.insuranceStateAttrib.buyInsuranceRound[roundIndex].accountId = accountId2;
				this.insuranceStateAttrib.buyInsuranceRound[roundIndex].buyMoney = 0;
				this.insuranceStateAttrib.buyInsuranceRound[roundIndex].payMoney = 0;
				this.insuranceStateAttrib.buyInsuranceRound[roundIndex].buyCards.clear();
				this.insuranceStateAttrib.buyInsuranceRound[roundIndex].exBuyMoney = exMoney;
				
				if(tmpTotalMoney > 0){
					RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountId2);
					EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_BUY_INSURANCE);
					eventAttrib.addEventParam(roleEntity.getStarNO());
					eventAttrib.addEventParam(this.tableId);
					roleEntity = roleEntityManager.findOf_accountId(this.createPlayer);
					eventAttrib.addEventParam(roleEntity.getStarNO());
					eventAttrib.addEventParam(this.gameNum);
					eventAttrib.addEventParam(tmpTotalMoney);
					eventAttrib.addEventParam(eventAttrib.eventTime);
					eventTriggerManager.triggerEvent(eventAttrib);
				}
			}
		}
	}
	
	private void run_over_once(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			this.gameLogicManager.log("进入单局结算状态");
			this.showCardSeatIndex = -1;
			this.createPools();
			
			if(this.un_all_in_betpool.betInfo.isEmpty() == false){
				this.all_in_pools.add(this.un_all_in_betpool);
			}
			this.waitTime = currTime + GameDefine.TIME_TABLE_OVER;
			this.actTotalTime = GameDefine.TIME_TABLE_OVER;
			List<SeatAttrib> winSeats = new ArrayList<>();
			for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
				SeatAttrib seat = this.getSeat(seatIndex);
				if(seat.accountId > 0 && seat.bGamed && seat.btResult != GameDefine.ACT_TYPE_DROP){
					winSeats.add(seat);
				}
			}
			//参与游戏玩家结算后余下的筹码(此数-总下注=本局总输赢)
			Map<Long, Long> tmpStatisticsMap = new HashMap<>();
			//统计所有参加了本局游戏的玩家
			List<Long> allGamedPlayerList = new ArrayList<>(); 
			for(DzpkTableBetPool pool : this.all_in_pools){
				Set<Long> keys = pool.betInfo.keySet();
				for(Long accountId : keys){
					if(allGamedPlayerList.contains(accountId) == false){
						allGamedPlayerList.add(accountId);
						tmpStatisticsMap.put(accountId, 0L);
					}
				}
			}
			
			DzpkerTableRecordEntity tableRecordEntity = this.recordManager.loadTableRecord(this.recordId);
			Map<Long, StatisticsAttrib> statisticsMap = tableRecordEntity.getStatisticsMap();
			Map<Integer, WinScoreAttrib> detailedScoreMap = tableRecordEntity.getDetailedScoreMap();
			WinScoreAttrib winScoreAttrib = detailedScoreMap.get(this.gameNum);
			winScoreAttrib.tableCards.clear();
			for(CardAttrib card : this.tableHandCards){
				winScoreAttrib.tableCards.add(card.cardId);
			}
									
			long winTotalMoney = 0;
			int activePlayerNum = winSeats.size();
			if(activePlayerNum == 0){
				//结算没有玩家设置所有玩家都输
				this.gameLogicManager.log("座位上无玩家,所有人都算输");
			}else if(activePlayerNum == 1){
				//只有一个玩家赢
				SeatAttrib winSeat = winSeats.get(0);
				for(DzpkTableBetPool pool : this.all_in_pools){
					Set<Long> keys = pool.betInfo.keySet();
					if(keys.contains(winSeat.accountId)){
						//只赢玩家参与的池子
						winTotalMoney += pool.countBetMoney();
					}
				}
				this.showCardSeatIndex = winSeat.seatIndex;
				
				StatisticsAttrib statisticsAttrib = statisticsMap.get(winSeat.accountId);
				statisticsAttrib.dropWinNum++;
								
				if(this.insurance > 0){
					//开启了保险,独赢的玩家扣除保险
					for(int roundIndex=0; roundIndex<2; roundIndex++){
						if(this.insuranceStateAttrib.buyInsuranceRound[roundIndex].accountId > 0
								&& this.insuranceStateAttrib.buyInsuranceRound[roundIndex].accountId == winSeat.accountId
								&& this.insuranceStateAttrib.buyInsuranceRound[roundIndex].settlement < 0){
							winTotalMoney += this.insuranceStateAttrib.buyInsuranceRound[roundIndex].settlement;
							if(winTotalMoney < 0){
								winTotalMoney = 0;
							}							
							statisticsAttrib.insuranceMoney += this.insuranceStateAttrib.buyInsuranceRound[roundIndex].settlement;
							if(statisticsAttrib.currMoney < this.bigBlind){
								this.showCardSeatIndex = -1;
							}
							
							long totalInsuranceScore = tableRecordEntity.getInsuranceScore() + (0-this.insuranceStateAttrib.buyInsuranceRound[roundIndex].settlement);
							tableRecordEntity.setInsuranceScore(totalInsuranceScore);
							
							RoleEntity roleEntity = roleEntityManager.findOf_accountId(winSeat.accountId);
							EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_WIN_INSURANCE);
							eventAttrib.addEventParam(roleEntity.getStarNO());
							eventAttrib.addEventParam(this.tableId);
							roleEntity = roleEntityManager.findOf_accountId(this.createPlayer);
							eventAttrib.addEventParam(roleEntity.getStarNO());
							eventAttrib.addEventParam(this.gameNum);
							eventAttrib.addEventParam((int)this.insuranceStateAttrib.buyInsuranceRound[roundIndex].settlement);
							eventAttrib.addEventParam(eventAttrib.eventTime);
							eventTriggerManager.triggerEvent(eventAttrib);
						}
					} 
				}
				tmpStatisticsMap.put(winSeat.accountId, winTotalMoney);
				statisticsMap.put(winSeat.accountId, statisticsAttrib);
			}else{			
				for(int index=0; index<winSeats.size(); index++){
					SeatAttrib theSeat = winSeats.get(index);
					if(theSeat.accountId <= 0){
						continue;
					}
					WinScoreAttribItem winScoreAttribInner = winScoreAttrib.getPlayerScore(theSeat.accountId);
					winScoreAttribInner.showCardState = 1;
					winScoreAttrib.setPlayerScore(theSeat.accountId, winScoreAttribInner);
				}				
				//多个玩家需要比牌
				List<List<SeatAttrib>> winSeatGroups = new ArrayList<>();
				winSeats.sort(new Comparator<SeatAttrib>(){
					@Override
					public int compare(SeatAttrib o1, SeatAttrib o2) {
						int result = GameDefine.compareCards(o1.cardsResult, o2.cardsResult);
						if(result > 0){
							return -1;
						}else if(result < 0){
							return 1;
						}
						return 0;
					}
				});
				
				int groupIndex = 0;
				SeatAttrib prevSeat = null;
				for(int index=0; index<winSeats.size(); index++){
					SeatAttrib theSeat = winSeats.get(index);
					if(null == prevSeat){
						prevSeat = theSeat;
						List<SeatAttrib> newGroup = new ArrayList<>();
						newGroup.add(theSeat);
						groupIndex = 0;
						winSeatGroups.add(newGroup);
					}else{
						int result = GameDefine.compareCards(prevSeat.cardsResult, theSeat.cardsResult);
						if(result==0){
							//相同放同一组
							List<SeatAttrib> group = winSeatGroups.get(groupIndex);
							group.add(theSeat);
						}else{
							//不相同新生成一组
							prevSeat = theSeat;
							List<SeatAttrib> newGroup = new ArrayList<>();
							newGroup.add(theSeat);
							groupIndex++;
							winSeatGroups.add(newGroup);
						}
					}
				}
				
				//开始分池子结算
				while(true){
					if(this.all_in_pools.isEmpty()){
						break;
					}
					DzpkTableBetPool pool = this.all_in_pools.get(0);
					Set<Long> poolAllBetPlayers = pool.betInfo.keySet();
					for(int winSeatGroupIndex=0; winSeatGroupIndex<winSeatGroups.size(); winSeatGroupIndex++){
						List<SeatAttrib> winSeatGroup = winSeatGroups.get(winSeatGroupIndex);
						List<Long> joinedPlayers = new ArrayList<>();
						for(SeatAttrib seat : winSeatGroup){
							if(poolAllBetPlayers.contains(seat.accountId)){
								joinedPlayers.add(seat.accountId);
							}
						}
						if(joinedPlayers.isEmpty()){
							//本组无玩家参与本池子下注
							continue;
						}
												
						winTotalMoney = pool.countBetMoney();
						if(winTotalMoney <= 0){
							continue;
						}
						//参与了的玩家平分本组总筹码
						long argveMoney = winTotalMoney/joinedPlayers.size();
						if(winSeatGroupIndex==0 && joinedPlayers.size()==1){
							//必须是主池且只有一个玩家独赢
							if(this.insurance > 0){
								//开启了保险,独赢的玩家扣除保险
								long onceWinAccountId = joinedPlayers.get(0).longValue();
								for(int roundIndex=0; roundIndex<2; roundIndex++){
									if(this.insuranceStateAttrib.buyInsuranceRound[roundIndex].accountId > 0
											&& this.insuranceStateAttrib.buyInsuranceRound[roundIndex].accountId == onceWinAccountId
											&& this.insuranceStateAttrib.buyInsuranceRound[roundIndex].settlement < 0){
										argveMoney += this.insuranceStateAttrib.buyInsuranceRound[roundIndex].settlement;
										if(argveMoney < 0){
											argveMoney = 0;
										}
										
										StatisticsAttrib statisticsAttrib = statisticsMap.get(onceWinAccountId);
										statisticsAttrib.insuranceMoney += this.insuranceStateAttrib.buyInsuranceRound[roundIndex].settlement;
										statisticsMap.put(onceWinAccountId, statisticsAttrib);
										long totalInsuranceScore = tableRecordEntity.getInsuranceScore() + (0-this.insuranceStateAttrib.buyInsuranceRound[roundIndex].settlement);
										tableRecordEntity.setInsuranceScore(totalInsuranceScore);
										
										RoleEntity roleEntity = roleEntityManager.findOf_accountId(onceWinAccountId);
										EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_WIN_INSURANCE);
										eventAttrib.addEventParam(roleEntity.getStarNO());
										eventAttrib.addEventParam(this.tableId);
										roleEntity = roleEntityManager.findOf_accountId(this.createPlayer);
										eventAttrib.addEventParam(roleEntity.getStarNO());
										eventAttrib.addEventParam(this.gameNum);
										eventAttrib.addEventParam((int)this.insuranceStateAttrib.buyInsuranceRound[roundIndex].settlement);
										eventAttrib.addEventParam(eventAttrib.eventTime);
										eventTriggerManager.triggerEvent(eventAttrib);
									}
								} 
							}
						}
						
						for(long accountId : joinedPlayers){
							Long winObj = tmpStatisticsMap.get(accountId);
							if(null == winObj){
								winObj = argveMoney;
							}else{
								winObj = winObj.longValue() + argveMoney;
							}
							tmpStatisticsMap.put(accountId, winObj);
						}
						break;
					}
					
					//此池子结算完成删除此池子
					this.all_in_pools.remove(pool);
				}
			}
			//结算完清空池子
			this.once_betpool.clear();
			this.all_in_pools.clear();
			this.un_all_in_betpool.clear();
			this.client_all_in_pools.clear();
			this.settlementOnceList.clear();
			
			//保存单局结算数据			
			RoleEntity roleEntity = roleEntityManager.findOf_accountId(this.createPlayer);
			String createPlayerStarNO = roleEntity.getStarNO();
			String winLogStr = "";
			for(long accountId : allGamedPlayerList){
				StatisticsAttrib statisticsAttrib = statisticsMap.get(accountId);	
				if(null == statisticsAttrib){
					statisticsAttrib = new StatisticsAttrib();
				}
				
				long theMoney = tmpStatisticsMap.get(accountId);
				statisticsAttrib.currMoney += theMoney;
								
				long winMoney = theMoney-statisticsAttrib.betTotalMoney;
				if(winMoney >= 0){
					//赢了
					if(winMoney > statisticsAttrib.maxWinMoney){
						statisticsAttrib.maxWinMoney = winMoney;
					}
					statisticsAttrib.winNum++;
				}else{
					//输了
					statisticsAttrib.lostNum++;
				}
				
				winLogStr += "["+accountId+"="+winMoney+"],";
				if(0 != winMoney){
					SeatAttrib tmpSeat = this.getPlayerSeat(accountId);
					if(null != tmpSeat && tmpSeat.bGamed){
						//在座位上且参与了本局游戏
						SettlementOnceVo tmpItem = new SettlementOnceVo();
						tmpItem.seatIndex = tmpSeat.seatIndex;
						tmpItem.winMoney = String.valueOf(winMoney);
						this.settlementOnceList.add(tmpItem);
					}
					
					EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_WIN_CARDS);
					roleEntity = roleEntityManager.findOf_accountId(accountId);
					eventAttrib.addEventParam(roleEntity.getStarNO());
					eventAttrib.addEventParam(this.tableId);
					eventAttrib.addEventParam(createPlayerStarNO);
					eventAttrib.addEventParam(this.gameNum);
					eventAttrib.addEventParam((int)winMoney);
					eventAttrib.addEventParam(eventAttrib.eventTime);
					eventTriggerManager.triggerEvent(eventAttrib);
				}
								
				statisticsMap.put(accountId, statisticsAttrib);
				
				WinScoreAttribItem winScoreAttribInner = winScoreAttrib.getPlayerScore(accountId);
				winScoreAttribInner.score = winMoney;
				SeatAttrib tmpSeat = this.getPlayerSeat(accountId);
				if(null != tmpSeat){
					for(CardAttrib card : tmpSeat.handCars){
						winScoreAttribInner.playerHandCards.add(card.cardId);
					}
				}
				winScoreAttrib.setPlayerScore(accountId, winScoreAttribInner);
			}
			this.gameLogicManager.log("结算结果:"+winLogStr);
			detailedScoreMap.put(this.gameNum, winScoreAttrib);
			tableRecordEntity.setDetailedScoreMap(detailedScoreMap);
			tableRecordEntity.setStatisticsMap(statisticsMap);
			
			this.gameNum++;
			if(this.bTableShowCardState){
				this.showCardSeatIndex = -1;
			}
			//通知客户端
			this.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_TABLE_STATE_NOTIFY);
		}
		if(currTime > this.waitTime){
			this.bActExec = false;
			this.gameState = GameDefine.STATE_TABLE_IDLE;
			
			boolean bKick = false;
			for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
				SeatAttrib seat = this.getSeat(seatIndex);
				if(seat.accountId <= 0){
					continue;
				}
				GameDataAttrib gameDataAttrib = this.gameDataManager.getAccountGameData(seat.accountId);
				if(gameDataAttrib.onLine == 0){
					//不在线的踢出桌子
					gameDataAttrib.tableId = 0; 
					seat.accountId = 0;
					seat.reset();
					this.tablePlayers.remove(seat.accountId);
					bKick = true;
				}else{
					//没有断线的,两次表态都是由系统表态的,踢出座位
					if(gameDataAttrib.btKickNum >= 2){
						long kickAccountId = seat.accountId;
						gameDataAttrib.btKickNum = 0;
						seat.accountId = 0;
						seat.reset();
						bKick = true;
						
						String msgStr = "您多次未表态已被 请离座位";
						//通知玩家被踢起
						@SuppressWarnings("rawtypes")
						Request pushMsg = Request.valueOf(DzpkerDefine.DZPKER_COMMAND_SEAT_KICK_NOTIFY,
								Result.valueOfSuccess(msgStr));
						MessagePushQueueUtils.getPushQueue(sessionManager).push2(kickAccountId, pushMsg);
					}
				}
				if(seat.accountId > 0){
					StatisticsAttrib statisticsAttrib = this.recordManager.getStatisticsAttrib(this.recordId, seat.accountId);
					if(statisticsAttrib.currMoney < this.bigBlind){
						long kickAccountId = seat.accountId;
						seat.accountId = 0;
						seat.reset();
						bKick = true;
						
						String msgStr = "您的积分不足已被 请离座位";
						//通知玩家被踢起
						@SuppressWarnings("rawtypes")
						Request pushMsg = Request.valueOf(DzpkerDefine.DZPKER_COMMAND_SEAT_KICK_NOTIFY,
								Result.valueOfSuccess(msgStr));
						MessagePushQueueUtils.getPushQueue(sessionManager).push2(kickAccountId, pushMsg);
					}	
				}
			}
			if(bKick){
				this.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_SEAT_STATE_NOTIFY);
			}
		}
	}
	
	private void run_over_all(long currTime){
		this.gameLogicManager.log("游戏进入总结算状态");
		this.bRemove = true;
		DzpkerTableRecordEntity recordEntity = this.recordManager.loadTableRecord(this.recordId);
		Map<Long, StatisticsAttrib> statisticsMap = recordEntity.getStatisticsMap();
		Set<Long> keys = statisticsMap.keySet();
		for(long accountId : keys){			
			StatisticsAttrib statisticsAttrib = statisticsMap.get(accountId);
			if(null == statisticsAttrib){
				continue;
			}
			statisticsAttrib.currMoney += statisticsAttrib.gameBuyChip;
			statisticsAttrib.gameBuyChip = 0;
			
			if(this.tablePlayers.containsKey(accountId) == false){
				//不在桌子上,不通知
				continue;
			}
			
			SettlementVo vo = new SettlementVo();
			vo.gameNum = String.valueOf(statisticsAttrib.gameTotalNum);
			if(statisticsAttrib.gameTotalNum <= 0){
				vo.winRate = "0";
			}else{
				double rate = statisticsAttrib.winNum*1.0/statisticsAttrib.gameTotalNum;
				BigDecimal b = new BigDecimal(String.format("%2f", rate));
				DecimalFormat d1 = new DecimalFormat("0.00");
				vo.winRate = d1.format(b);
			}
			vo.maxBetMoney = String.valueOf(statisticsAttrib.maxBetMoney);
			vo.maxWinMoney = String.valueOf(statisticsAttrib.maxWinMoney);
			vo.buyTotalMoney = String.valueOf(statisticsAttrib.buyTotalMoney);
			vo.winMoney = String.valueOf(statisticsAttrib.currMoney-statisticsAttrib.buyTotalMoney);
			@SuppressWarnings("rawtypes")
			Request pushMsg = Request.valueOf(DzpkerDefine.DZPKER_COMMAND_OVER_SETTLEMENT_NOTIFY, Result.valueOfSuccess(vo));
			MessagePushQueueUtils.getPushQueue(this.sessionManager).push2(accountId, pushMsg);
		}
		keys = this.tablePlayers.keySet();
		for(long accountId : keys){
			GameDataAttrib gameDataAttrib = this.gameDataManager.getAccountGameData(accountId);
			gameDataAttrib.tableId = 0;
		}
		this.tablePlayers.clear();
	}
	
	private void run_new_round_bet(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			this.waitTime = currTime + GameDefine.TIME_TABLE_NEW_ROUND_NET;
			this.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_TABLE_STATE_NOTIFY);
			this.gameLogicManager.log("游戏进入新一轮筹码结算状态");
		}
		if(currTime > this.waitTime){
			this.bActExec = false;
			this.gameState = this.nextGameState;
			this.nextGameState = this.saveNextGameState;
		}
	}
	
	private void run_allin_showcard(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			this.waitTime = currTime + GameDefine.TIME_TABLE_ALLIN_SHOWCARD;
			this.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_TABLE_STATE_NOTIFY);
			this.gameLogicManager.log("游戏进入秀牌状态");
			this.bTableShowCardState = true;
		}
		if(currTime > this.waitTime){
			this.bActExec = false;
			if(this.tableHandCards.size() == 3){
				//发了3张公共牌表态后引发秀牌
				if(this.checkInsurance(1)){
					this.gameState = GameDefine.STATE_TABLE_BUY_INSURANCE;
				}else{
					this.gameState = this.nextGameState;
				}
			}else if(this.tableHandCards.size() == 4){
				//发了第四张公共牌表态后引发秀牌
				if(this.checkInsurance(2)){
					this.gameState = GameDefine.STATE_TABLE_BUY_INSURANCE;
				}else{
					this.gameState = this.nextGameState;
				}
			}else{
				this.gameState = this.nextGameState;
			}
		}
	}
	
	private void run_wait(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			this.waitTime = currTime + GameDefine.TIME_TABLE_WAIT;
		}
		if(currTime > this.waitTime){
			this.bActExec = false;
			this.gameState = this.nextGameState;
		}
	}
		
	private void run_outcard_inner(long currTime, int round){
		int activePlayerNum = this.getGamedPlayerNum();
		if(activePlayerNum == 1){
			//发牌只有一个活着的玩家了,直接结算
			this.gameState = GameDefine.STATE_TABLE_OVER_ONCE;
			this.bActExec = false;
			this.createPools();
			this.gameLogicManager.log("只有一个玩家了单局结算");
			return;
		}
		if(this.bActExec == false){
			this.bActExec = true;
			//清空每轮下注池
			if(round > 1){
				this.once_betpool.clear();
			}
			int N = 0;
			if(round==1){
				//第一轮每个参与了游戏的座位发2张手牌
				for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
					SeatAttrib seat = this.getSeat(seatIndex);
					if(seat.accountId > 0 && seat.bGamed){
						List<Integer> handCards = new ArrayList<>();
						for(int i=0; i<2; i++){
							CardAttrib card = this.tableUsedCards.get(this.tableUsedCardIndex);
							seat.handCars.add(card);
							this.tableUsedCardIndex++;
							handCards.add(card.cardId);
						}						
						N++;
					}
				}				
				this.waitTime = currTime + N*2*GameDefine.TIME_TABLE_OUTCARD;
				this.actTotalTime = N*2*GameDefine.TIME_TABLE_OUTCARD;
			}else if(round==2){				
				//第二轮发3张公共牌
				for(int i=0; i<3; i++){
					CardAttrib card = this.tableUsedCards.get(this.tableUsedCardIndex);
					this.tableUsedCardIndex++;
					this.tableHandCards.add(card);
				}
				
				N = 3;
				this.waitTime = currTime + N*GameDefine.TIME_TABLE_OUTCARD;
				this.actTotalTime = N*GameDefine.TIME_TABLE_OUTCARD;
			}else{				
				//发1张公共牌
				for(int i=0; i<1; i++){
					CardAttrib card = this.tableUsedCards.get(this.tableUsedCardIndex);
					this.tableUsedCardIndex++;
					this.tableHandCards.add(card);
				}
				
				this.waitTime = currTime + GameDefine.TIME_TABLE_OUTCARD;
				this.actTotalTime = GameDefine.TIME_TABLE_OUTCARD;
			}
			
			//计算玩家牌型
			for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
				SeatAttrib seat = this.getSeat(seatIndex);
				if(seat.accountId > 0 && seat.bGamed){
					List<CardAttrib> tmpList = new ArrayList<>();
					tmpList.addAll(seat.handCars);
					tmpList.addAll(this.tableHandCards);
					seat.cardsResult = GameDefine.getCardsType(tmpList);
				}
			}
			this.gameLogicManager.log("第" +round+ "轮发牌");
			this.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_TABLE_STATE_NOTIFY);
		}
		if(currTime > this.waitTime){			
			this.bActExec = false;
			switch(round){
			case 1://第一轮发2张手牌
				this.gameState = GameDefine.STATE_TABLE_BET_BT_1;
				//重设闭眼盲注状态
				for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
					SeatAttrib seat = this.getSeat(seatIndex);
					seat.straddle = 0;
				}
				break;
			case 2://第二轮发3张公共牌,需要检查是否触发保险(活动玩家至少2人且只有一个玩家筹码大于0)
				this.gameLogicManager.log("发了三张公共牌检查保险");
				if(checkInsurance(1)){
					this.gameState = GameDefine.STATE_TABLE_BUY_INSURANCE;
					this.nextGameState = GameDefine.STATE_TABLE_BET_BT_2;
				}else{
					this.gameState = GameDefine.STATE_TABLE_BET_BT_2;
				}
				break;
			case 3://第三轮发1张公共牌,需要检查是否触发保险(活动玩家至少2人且只有一个玩家筹码大于0)
				//先结算上一轮保险
				this.gameLogicManager.log("发了1张公共牌结算第1轮保险");
				this.settlementInsurance(1);
				if(checkInsurance(2)){
					this.gameState = GameDefine.STATE_TABLE_BUY_INSURANCE;
					this.nextGameState = GameDefine.STATE_TABLE_BET_BT_3;
				}else{
					this.gameState = GameDefine.STATE_TABLE_BET_BT_3;
				}
				break;
			case 4://第四轮发1张公共牌
				//先结算上一轮保险
				this.gameLogicManager.log("发了1张公共牌结算第2轮保险");
				this.settlementInsurance(2);
				this.gameState = GameDefine.STATE_TABLE_BET_BT_4;
				break;
			}
		}
	}
	
	private void run_bet_bt_inner(long currTime, int round){
		int tmpGameState = GameDefine.STATE_TABLE_OUTCARD_2;
		switch(round){
		case 1:
			tmpGameState = GameDefine.STATE_TABLE_OUTCARD_2;
			break;
		case 2:
			tmpGameState = GameDefine.STATE_TABLE_OUTCARD_3;
			break;
		case 3:
			tmpGameState = GameDefine.STATE_TABLE_OUTCARD_4;
			break;
		case 4:
			tmpGameState = GameDefine.STATE_TABLE_OVER_ONCE;
			break;
		}
				
		SeatAttrib btSeat = this.getSeat(this.btIndex);
		if(null == btSeat){
			//下一游戏状态
			this.bActExec = false;
			this.gameState = tmpGameState;
			this.createPools();
			return;
		}		
		boolean bChanageState = false;
		if(btSeat.btState == GameDefine.BT_STATE_WAIT){
			if(this.bActExec == false){
				this.bActExec = true;
				this.gameLogicManager.log("第" +round+ "轮表态,当前表态玩家座位="+btSeat.seatIndex);
				btSeat.btState = GameDefine.BT_STATE_WAIT;
				btSeat.btResult = GameDefine.ACT_TYPE_NONE;
				this.waitTime = currTime + GameDefine.TIME_TABLE_BET_BT;
				this.actTotalTime = GameDefine.TIME_TABLE_BET_BT;
				this.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_TABLE_STATE_NOTIFY);
			}
			
			if(currTime > this.waitTime){
				//超时了能够过牌就过,不能过牌设为弃牌
				long maxBetMoney = this.once_betpool.getMaxBetMoney();
				long myBetMoney = this.once_betpool.getBetMoneyOfAccountId(btSeat.accountId);
				if(myBetMoney < maxBetMoney){
					btSeat.btState = GameDefine.BT_STATE_BT;
					btSeat.btResult = GameDefine.ACT_TYPE_DROP;
				}else{
					btSeat.btState = GameDefine.BT_STATE_BT;
					btSeat.btResult = GameDefine.ACT_TYPE_PASS;
				}
				GameDataAttrib gameDataAttrib = this.gameDataManager.getAccountGameData(btSeat.accountId);
				gameDataAttrib.btKickNum++;				
				this.gameLogicManager.log("第" +round+ "轮表态,玩家表态超时,系统自动表态,当前表态玩家座位="+btSeat.seatIndex);
			}else{
				int activityNum = this.getGamedPlayerNum();
				if(activityNum <= 1){
					//轮到我表态,只余下我一个活着的玩家了,直接结算
					this.bActExec = false;
					this.gameState = GameDefine.STATE_TABLE_OVER_ONCE;
					this.btIndex = -1;
					bChanageState = true;
				}else{
					int needBtNum = this.getGamedNeedBtPlayerNum();
					if(1 == needBtNum){
						//轮到我表态,只余下我一个需要全场表态了
						long maxBetMoney = this.once_betpool.getMaxBetMoney();
						long myBetMoney = this.once_betpool.getBetMoneyOfAccountId(btSeat.accountId);
						if(myBetMoney >= maxBetMoney){
							//不需要表态了,进入下一个游戏状态
							this.bActExec = false;
							this.gameState = tmpGameState;
							this.createPools();
							this.btIndex = -1;
							bChanageState = true;
							if(this.bTableShowCardState == false){
								if(round != 4){
									this.gameState = GameDefine.STATE_TABLE_ALL_IN_SHOW_CARD;
									this.nextGameState = tmpGameState;
								}
							}
						}
					}
				}
			}
		}else{
			this.prevBtIndex = btSeat.seatIndex;
			//通知玩家表态消息
			if(btSeat.accountId > 0){
				this.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_BET_STATE_NOTIFY);
			}			
			this.gameLogicManager.log("第" +round+ "轮表态,当前表态玩家座位="+btSeat.seatIndex + ",表态结果="+btSeat.btResult);
			
			int activityNum = this.getGamedPlayerNum();
			if(activityNum <= 1){
				//只余下一个活着的玩家了,直接结算
				this.createPools();
				this.bActExec = false;
				this.gameState = GameDefine.STATE_TABLE_OVER_ONCE;
				this.btIndex = -1;
				bChanageState = true;
			}else{
				//还有多个玩家活着
				//此玩家已表完态,检查所有玩家都表了态,除all_in和弃牌外的所有玩家,如果下注筹码和最大下注都一样,新一轮游戏开始
				boolean bNewRound = this.checkNewRound();
				if(bNewRound){
					bChanageState = true;
					this.gameLogicManager.log("新的一轮表态开始" + round);
					this.bActExec = false;
					this.gameState = tmpGameState;
					this.createPools();
					
					SeatAttrib tmpBtSeat = null;
					for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
						SeatAttrib seat = this.getSeat(seatIndex);
						if(seat.accountId > 0 && seat.bGamed){
							if(seat.btState == GameDefine.BT_STATE_BT){
								if(seat.btResult == GameDefine.ACT_TYPE_PASS
										|| seat.btResult == GameDefine.ACT_TYPE_SAME
										|| seat.btResult == GameDefine.ACT_TYPE_ADD){
									seat.btState = GameDefine.BT_STATE_WAIT;
									seat.btResult = GameDefine.ACT_TYPE_NONE;
									tmpBtSeat = seat;
								}
							}else if(seat.btState == GameDefine.BT_STATE_WAIT){
								seat.btResult = GameDefine.ACT_TYPE_NONE;
								tmpBtSeat = seat;
							}
						}
					}
					int needBtNum = this.getGamedNeedBtPlayerNum();
					if(needBtNum > 1){
						this.btIndex = this.getNextBtSeat(this.bankerIndex).seatIndex;
					}else if(needBtNum == 1){
						long maxBetMoney = this.once_betpool.getMaxBetMoney();
						long myBetMoney = this.once_betpool.getBetMoneyOfAccountId(tmpBtSeat.accountId);
						if(myBetMoney < maxBetMoney){
							//还需要表态
							this.btIndex = tmpBtSeat.seatIndex;
						}else{
							this.btIndex = -1;
							if(this.bTableShowCardState == false){
								if(round != 4){
									this.gameState = GameDefine.STATE_TABLE_ALL_IN_SHOW_CARD;
									this.nextGameState = tmpGameState;
								}
							}
						}
					}else{
						//都ALL_IN无表态了
						this.btIndex = -1;
						if(this.bTableShowCardState == false){
							if(round != 4){
								this.gameState = GameDefine.STATE_TABLE_ALL_IN_SHOW_CARD;
								this.nextGameState = tmpGameState;
							}
						}
					}
				}else{
					int needBtNum = this.getGamedNeedBtPlayerNum();
					if(needBtNum > 1){
						//还有多个玩家可以表态,继续下一位玩家表态
						SeatAttrib nextBtSeat = this.getNextBtSeat(btSeat.seatIndex);
						this.gameLogicManager.log("继续下一位玩家表态座位下标="+nextBtSeat.seatIndex);
						this.btIndex = nextBtSeat.seatIndex;
						nextBtSeat.btState = GameDefine.BT_STATE_WAIT;
						nextBtSeat.btResult = GameDefine.ACT_TYPE_NONE;
						this.bActExec = false;
					}else{
						if(needBtNum == 1){
							//只有一个玩家可以表态了,找出这个可以表态的玩家
							SeatAttrib findBtSeat = null;
							for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
								SeatAttrib seat = this.getSeat(seatIndex);
								if(seat.accountId > 0 && seat.bGamed){
									if(seat.btState == GameDefine.BT_STATE_WAIT){
										findBtSeat = seat;
										break;
									}else if(seat.btState == GameDefine.BT_STATE_BT){
										if(seat.btResult == GameDefine.ACT_TYPE_PASS
												|| seat.btResult == GameDefine.ACT_TYPE_SAME
												|| seat.btResult == GameDefine.ACT_TYPE_ADD){
											findBtSeat = seat;
											break;
										}
									}
								}
							}
							long maxBetMoney = this.once_betpool.getMaxBetMoney();
							long myBetMoney = this.once_betpool.getBetMoneyOfAccountId(findBtSeat.accountId);
							if(myBetMoney >= maxBetMoney){
								//本轮这个玩家是下注是大于最大下注,可以不用表态了
								this.btIndex = -1;
								this.bActExec = false;
								this.gameState = tmpGameState;
								this.createPools();
								bChanageState = true;
								if(this.bTableShowCardState == false){
									if(round != 4){
										this.gameState = GameDefine.STATE_TABLE_ALL_IN_SHOW_CARD;
										this.nextGameState = tmpGameState;
									}
								}
							}else{
								//还需要这个玩家表态
								this.gameLogicManager.log("继续下一位玩家表态座位下标="+findBtSeat.seatIndex);
								this.btIndex = findBtSeat.seatIndex;
								findBtSeat.btState = GameDefine.BT_STATE_WAIT;
								findBtSeat.btResult = GameDefine.ACT_TYPE_NONE;
								this.bActExec = false;
							}
						}else{
							//都ALL_IN了
							this.btIndex = -1;							
							this.bActExec = false;
							this.gameState = tmpGameState;
							this.createPools();
							bChanageState = true;
							if(this.bTableShowCardState == false){
								if(round != 4){
									this.gameState = GameDefine.STATE_TABLE_ALL_IN_SHOW_CARD;
									this.nextGameState = tmpGameState;
								}
							}
						}
					}
				}
			}
		}
		if(bChanageState && this.once_betpool.countBetMoney() > 0){
			this.saveNextGameState = this.nextGameState;
			this.nextGameState = this.gameState;
			this.gameState = GameDefine.STATE_TABLE_NEW_ROUND_BET;
			this.bActExec = false;
			this.client_all_in_pools.clear();
			for(DzpkTableBetPool pool : this.all_in_pools){
				if(pool.countBetMoney() <= 0){
					continue;
				}
				DzpkTableBetPool tmpPool = new DzpkTableBetPool();
				Set<Long> keys = pool.betInfo.keySet();
				for(Long key : keys){
					tmpPool.addBetMoney(key, pool.getBetMoneyOfAccountId(key));
				}
				this.client_all_in_pools.add(tmpPool);
			}
			if(this.un_all_in_betpool.countBetMoney() > 0){
				DzpkTableBetPool tmpPool = new DzpkTableBetPool();
				Set<Long> keys = this.un_all_in_betpool.betInfo.keySet();
				for(Long key : keys){
					tmpPool.addBetMoney(key, this.un_all_in_betpool.getBetMoneyOfAccountId(key));
				}
				this.client_all_in_pools.add(tmpPool);
			}
		}
	}
	
	//创建边池
	private void createPools(){
		List<SeatAttrib> allinSeats = new ArrayList<>();
		for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
			SeatAttrib seat = this.getSeat(seatIndex);
			if(seat.accountId > 0 && seat.bGamed
					&& seat.btState == GameDefine.BT_STATE_BT
					&& seat.btResult == GameDefine.ACT_TYPE_ALLIN){
				long onceBetMoney = this.un_all_in_betpool.getBetMoneyOfAccountId(seat.accountId);
				if(onceBetMoney > 0){
					allinSeats.add(seat);
				}
			}
		}
		
		allinSeats.sort(new Comparator<SeatAttrib>(){
			@Override
			public int compare(SeatAttrib arg0, SeatAttrib arg1) {
				long money1 = un_all_in_betpool.getBetMoneyOfAccountId(arg0.accountId);
				long money2 = un_all_in_betpool.getBetMoneyOfAccountId(arg1.accountId);
				if(money1 > money2){
					return 1;
				}else if(money1 < money2){
					return -1;
				}
				return 0;
			}
		});
		
		Set<Long> keys = this.un_all_in_betpool.betInfo.keySet();
		for(SeatAttrib tmpSeat : allinSeats){
			DzpkTableBetPool oncepool = new DzpkTableBetPool();
			long allinMoney = this.un_all_in_betpool.getBetMoneyOfAccountId(tmpSeat.accountId);
			
			for(Long accountId : keys){
				long tmpBetMoney = this.un_all_in_betpool.getBetMoneyOfAccountId(accountId);
				if(tmpBetMoney <= 0){
					continue;
				}
				long theAllinMoney = allinMoney;
				if(tmpBetMoney < theAllinMoney){
					theAllinMoney = tmpBetMoney;
				}
				oncepool.addBetMoney(accountId, theAllinMoney);
				this.un_all_in_betpool.addBetMoney(accountId, 0-theAllinMoney);
			}
			this.all_in_pools.add(oncepool);
		}
	}
	
	private boolean checkNewRound(){
		//检查如果所有玩家都表了态,除all_in和弃牌外的所有玩家,如果下注筹码都一样,新一轮游戏开始
		long maxBetMoney = this.once_betpool.getMaxBetMoney();
		for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
			SeatAttrib seat = this.getSeat(seatIndex);
			if(seat.accountId > 0
					&& seat.bGamed){
				if(seat.btState == GameDefine.BT_STATE_WAIT){
					return false;
				}else{
					if(seat.btState == GameDefine.BT_STATE_BT){
						if(seat.btResult != GameDefine.ACT_TYPE_DROP
								&& seat.btResult != GameDefine.ACT_TYPE_ALLIN){
							long theMoney = this.once_betpool.getBetMoneyOfAccountId(seat.accountId);
							if(theMoney != maxBetMoney){
								return false;
							}
						}
					}
				}
			}
		}		
		return true;
	}
	
	private boolean checkInsurance(int round){
		//需要检查是否触发保险(活动玩家至少2人且无人可表态了)
		if(this.insurance <= 0){
			this.gameLogicManager.log("未开启保险玩法,不开启保险");
			return false;
		}
		List<SeatAttrib> insuranceSeats = new ArrayList<>();
		int activeNum = 0;
		for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
			SeatAttrib seat = this.getSeat(seatIndex);
			if(seat.accountId > 0 && seat.bGamed 
					&& seat.btResult != GameDefine.ACT_TYPE_DROP){
				activeNum++;
				insuranceSeats.add(seat);
			}
		}
		
		if(activeNum < 2){
			this.gameLogicManager.log("活动人数不够2人,不开启保险");
			return false;
		}
		int needBtNum = this.getGamedNeedBtPlayerNum();
		if(needBtNum > 1){
			this.gameLogicManager.log("可表态人数大于1人,不开启保险");
			return false;
		}
		
		//将座位按牌从大到小排
		insuranceSeats.sort(new Comparator<SeatAttrib>(){
			@Override
			public int compare(SeatAttrib o1, SeatAttrib o2) {
				int result = GameDefine.compareCards(o1.cardsResult, o2.cardsResult);
				if(result > 0){
					return -1;
				}else if(result < 0){
					return 1;
				}
				return 0;
			}
		});
		SeatAttrib buySeat = insuranceSeats.get(0);
		SeatAttrib tmpSeat2 = insuranceSeats.get(1);
		int result = GameDefine.compareCards(buySeat.cardsResult, tmpSeat2.cardsResult);
		if(result == 0){
			//没有最大的牌
			this.gameLogicManager.log("没有最大的牌,不开启保险");
			return false;
		}
		
		int totalCanBuyNum = 0;
		
		this.insuranceStateAttrib.reset();
		DzpkerCfgEntity dzpkerCfgEntity = cfgManager.loadOrCreate();
		this.insuranceStateAttrib.insuranceRateList.addAll(dzpkerCfgEntity.getInsuranceList());
		for(CardAttrib card : this.tableHandCards){
			this.insuranceStateAttrib.tableHandCards.add(card.cardId);
		}
					
		List<DzpkTableBetPool> allPools = new ArrayList<>();
		allPools.addAll(this.all_in_pools);
		allPools.add(this.un_all_in_betpool);
		DzpkTableBetPool mainPool = allPools.get(0);
		this.insuranceStateAttrib.poolMoney = String.valueOf(mainPool.countBetMoney());
		this.insuranceStateAttrib.betMoney = String.valueOf(mainPool.getBetMoneyOfAccountId(buySeat.accountId));
		this.insuranceStateAttrib.accountId = String.valueOf(buySeat.accountId);
		this.insuranceStateAttrib.round = round;
					
		for(int index=0; index<insuranceSeats.size(); index++){
			SeatAttrib theSeat = insuranceSeats.get(index);
			String nick = "";
			RoleEntity roleEntity = roleEntityManager.findOf_accountId(theSeat.accountId);
			if(null != roleEntity){
				nick = roleEntity.getNick();
			}
			int cardNum = 0;
			if(index == 0){
				//是买保险的人不计算
				cardNum = 0;
			}else{
				List<CardAttrib> tmpCardList = new ArrayList<>();
				tmpCardList.addAll(theSeat.handCars);
				tmpCardList.addAll(this.tableHandCards);
				
				List<CardAttrib> buyPlayerCardList = new ArrayList<>();
				buyPlayerCardList.addAll(buySeat.handCars);
				buyPlayerCardList.addAll(this.tableHandCards);
				
				int tmpUsedCardIndex = this.tableUsedCardIndex;
				for(; tmpUsedCardIndex < this.tableUsedCards.size(); tmpUsedCardIndex++){
					CardAttrib theCard = this.tableUsedCards.get(tmpUsedCardIndex);
					tmpCardList.add(theCard);
					buyPlayerCardList.add(theCard);
					
					AnalysisResult analysisResult = GameDefine.getCardsType(tmpCardList);
					AnalysisResult buyPlayerAnalysisResult = GameDefine.getCardsType(buyPlayerCardList);
					
					result = GameDefine.compareCards(buyPlayerAnalysisResult, analysisResult);
					if(result < 0){
						cardNum++;
						totalCanBuyNum++;
						if(this.insuranceStateAttrib.winCardList.contains(theCard.cardId) == false){
							this.insuranceStateAttrib.winCardList.add(theCard.cardId);
						}
					}
					
					tmpCardList.remove(theCard);
					buyPlayerCardList.remove(theCard);
				}
			}
			
			this.insuranceStateAttrib.addSeat(theSeat.seatIndex, theSeat.accountId, nick, theSeat.handCars, cardNum);
		}
		long totalBuyedMoney = 0;
		for(int i=0; i<2; i++){
			if(this.insuranceStateAttrib.buyInsuranceRound[i].accountId == buySeat.accountId){
				totalBuyedMoney += (this.insuranceStateAttrib.buyInsuranceRound[i].buyMoney + this.insuranceStateAttrib.buyInsuranceRound[i].exBuyMoney);
			}
		}
		this.insuranceStateAttrib.buyedNum = String.valueOf(totalBuyedMoney);
		
		if(totalCanBuyNum <= 0){
			this.gameLogicManager.log("反超牌张数为0张,不开启保险");
			return false;
		}
		
		return true;
	}
	
	//保险结算
	private void settlementInsurance(int round){
		if(this.insurance <= 0){
			return;
		}
		if(round == 1){
			if(this.insuranceStateAttrib.buyInsuranceRound[0].accountId <= 0){
				return;
			}
		}else{
			if(this.insuranceStateAttrib.buyInsuranceRound[1].accountId <= 0){
				return;
			}
		}
		
		List<SeatAttrib> insuranceSeats = new ArrayList<>();
		for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
			SeatAttrib seat = this.getSeat(seatIndex);
			if(seat.accountId > 0 && seat.bGamed 
					&& seat.btResult != GameDefine.ACT_TYPE_DROP){
				insuranceSeats.add(seat);
			}
		}
		if(insuranceSeats.size() < 2){
			return;
		}
		
		//将座位按牌从大到小排
		insuranceSeats.sort(new Comparator<SeatAttrib>(){
			@Override
			public int compare(SeatAttrib o1, SeatAttrib o2) {
				int result = GameDefine.compareCards(o1.cardsResult, o2.cardsResult);
				if(result > 0){
					return -1;
				}else if(result < 0){
					return 1;
				}
				return 0;
			}
		});
		SeatAttrib maxCardSeat = insuranceSeats.get(0);
		SeatAttrib tmpSeat2 = insuranceSeats.get(1);
		int result = GameDefine.compareCards(maxCardSeat.cardsResult, tmpSeat2.cardsResult);
		int currTableHandCard = 0;
		InsuranceBuyAttrib buyInsuranceRound = null;
		if(round == 1){
			buyInsuranceRound = this.insuranceStateAttrib.buyInsuranceRound[0];
			currTableHandCard = this.tableHandCards.get(3).cardId;
		}else{
			buyInsuranceRound = this.insuranceStateAttrib.buyInsuranceRound[1];
			currTableHandCard = this.tableHandCards.get(4).cardId;
		}
		InsuranceSettlementVo notifyVo = new InsuranceSettlementVo();
		if(result == 0){
			//买保险的玩家牌不是最大的,赢了保险
			buyInsuranceRound.settlement = buyInsuranceRound.payMoney;
			
			DzpkerTableRecordEntity tableRecordEntity = this.recordManager.loadTableRecord(this.recordId);
			Map<Long, StatisticsAttrib> statisticsMap = tableRecordEntity.getStatisticsMap();
			StatisticsAttrib statisticsAttrib = statisticsMap.get(buyInsuranceRound.accountId);
			statisticsAttrib.currMoney += buyInsuranceRound.payMoney;
			statisticsAttrib.insuranceMoney += buyInsuranceRound.payMoney;
			statisticsMap.put(buyInsuranceRound.accountId, statisticsAttrib);
			tableRecordEntity.setStatisticsMap(statisticsMap);
			
			long totalInsuranceScore = tableRecordEntity.getInsuranceScore() + (0-buyInsuranceRound.payMoney);
			tableRecordEntity.setInsuranceScore(totalInsuranceScore);
			
			notifyVo.result = 1;
			notifyVo.payMoney = String.valueOf(buyInsuranceRound.payMoney);
		}else{			
			if(buyInsuranceRound.accountId == maxCardSeat.accountId){
				//买保险的玩家牌最大,赢了牌,输了保险
				buyInsuranceRound.settlement = (0-(buyInsuranceRound.buyMoney+buyInsuranceRound.exBuyMoney));
				notifyVo.result = 0;
				notifyVo.payMoney = String.valueOf(buyInsuranceRound.settlement);
			}else{
				//买保险的玩家牌不是最大,检查是否买中出了的牌
				boolean bWin = false;
				for(int theCardId : buyInsuranceRound.buyCards){
					if(currTableHandCard == theCardId){
						bWin = true;
						break;
					}
				}
				if(bWin){
					//买中了,输了牌,赢了保险
					buyInsuranceRound.settlement = buyInsuranceRound.payMoney;
					DzpkerTableRecordEntity tableRecordEntity = this.recordManager.loadTableRecord(this.recordId);
					Map<Long, StatisticsAttrib> statisticsMap = tableRecordEntity.getStatisticsMap();
					StatisticsAttrib statisticsAttrib = statisticsMap.get(buyInsuranceRound.accountId);
					statisticsAttrib.currMoney += buyInsuranceRound.payMoney;
					statisticsAttrib.insuranceMoney += buyInsuranceRound.payMoney;
					statisticsMap.put(buyInsuranceRound.accountId, statisticsAttrib);
					tableRecordEntity.setStatisticsMap(statisticsMap);
					
					long totalInsuranceScore = tableRecordEntity.getInsuranceScore() + (0-buyInsuranceRound.payMoney);
					tableRecordEntity.setInsuranceScore(totalInsuranceScore);
					
					notifyVo.result = 1;
					notifyVo.payMoney = String.valueOf(buyInsuranceRound.payMoney);
				}else{
					//没有买中,输了牌,输了保险
					buyInsuranceRound.settlement = (0-(buyInsuranceRound.buyMoney+buyInsuranceRound.exBuyMoney));
					notifyVo.result = 0;
					notifyVo.payMoney = String.valueOf(buyInsuranceRound.settlement);
				}
			}
		}
		
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(DzpkerDefine.DZPKER_COMMAND_INSURANCE_SETTLEMENT_NOTIFY, 
				Result.valueOfSuccess(notifyVo));
		MessagePushQueueUtils.getPushQueue(sessionManager).push2(buyInsuranceRound.accountId, pushMsg);
		
		if(notifyVo.result > 0){
			RoleEntity roleEntity = roleEntityManager.findOf_accountId(buyInsuranceRound.accountId);
			EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_WIN_INSURANCE);
			eventAttrib.addEventParam(roleEntity.getStarNO());
			eventAttrib.addEventParam(this.tableId);
			roleEntity = roleEntityManager.findOf_accountId(this.createPlayer);
			eventAttrib.addEventParam(roleEntity.getStarNO());
			eventAttrib.addEventParam(this.gameNum);
			eventAttrib.addEventParam(Integer.parseInt(notifyVo.payMoney));
			eventAttrib.addEventParam(eventAttrib.eventTime);
			eventTriggerManager.triggerEvent(eventAttrib);
		}
	}
	
}
