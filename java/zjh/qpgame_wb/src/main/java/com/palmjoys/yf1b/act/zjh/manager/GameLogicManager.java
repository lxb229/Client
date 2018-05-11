package com.palmjoys.yf1b.act.zjh.manager;

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

import com.palmjoys.yf1b.act.cooltime.manger.CheckResetTimeManager;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeConfigType;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeResult;
import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;
import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;
import com.palmjoys.yf1b.act.zjh.model.GameDataAttrib;
import com.palmjoys.yf1b.act.zjh.model.GameDefine;
import com.palmjoys.yf1b.act.zjh.model.SeatAttrib;
import com.palmjoys.yf1b.act.zjh.model.TableAttrib;
import com.palmjoys.yf1b.act.zjh.model.ZJHMessageDefine;
import com.palmjoys.yf1b.act.zjh.resource.RoomConfig;

@Component
public class GameLogicManager {
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private GameCfgManager cfgManager;
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private GameDataManager gameDataManager;
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private TableStateNNManager tableStateNNManager;
	@Autowired
	private TableStateZJHManager tableStateZJHManager;
	@Autowired
	private CheckResetTimeManager checkResetTimeManager;
	@Autowired
	private GameProfitPoolManager profitPoolManager;
	@Autowired
	private EventTriggerManager eventTriggerManager;
	
	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(GameLogicManager.class);
	
	//桌子列表
	private Map<Integer, TableAttrib> tableMap = new HashMap<>();
	//桌子运行线程
	private Thread _thread = null;
	//数据访问操作锁
	private Lock _lock = new ReentrantLock();
	//金花桌子Id下标号
	private int tableId_NN = 1;
	//金花桌子Id下标号
	private int tableId_ZJH = 100001;
		
	@PostConstruct
	protected void init() {		
		initNNTable();
		this.loadRobot();
		GameDefine.initNNCard();
		GameDefine.initZJHCards();
		_thread = new Thread(){
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
		
		_thread.setDaemon(true);
		_thread.setName("游戏逻辑运行线程");
		_thread.start();
	}
	
	public void lock(){
		_lock.lock();
	}
	
	public void unLock(){
		_lock.unlock();
	}
	
	public void debug(String str){
		logger.debug(str);
	}
	
	private void runTable(){
		this.lock();
		try{
			try{
				this.runRobotAct();
			}catch(Exception e){
				logger.debug("runRobotAct运行异常!!!");
			}
			
			try{
				this.resetRobotWinMoney();
			}catch(Exception e){
				logger.debug("resetRobotWinMoney运行异常!!!");
			}			
			
			List<Integer> delIds = new ArrayList<>();
			Collection<TableAttrib> tables = tableMap.values();
			for(TableAttrib table : tables){
				try{
					table.run();
					if(table.gameType == GameDefine.GAME_TYPE_ZJH){
						if(table.getTablePlayerNum() == 0){
							delIds.add(table.tableId);
						}else{
							int playerNum = 0;
							for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
								SeatAttrib seat = table.getSeat(seatIndex);
								if(seat.accountId > 0){
									GameDataAttrib dataAttrib = table.gameDataManager.getGameData(seat.accountId);
									if(dataAttrib.robot == 0){
										playerNum++;
									}
								}
							}
							if(playerNum == 0){
								//没有真实玩家了解散桌子
								for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
									SeatAttrib seat = table.getSeat(seatIndex);
									if(seat.accountId > 0){
										GameDataAttrib dataAttrib = table.gameDataManager.getGameData(seat.accountId);
										dataAttrib.tableId = 0;
										seat.accountId = 0;
									}
								}
								delIds.add(table.tableId);
							}
						}
					}
				}catch(Exception e){
					logger.debug("桌子运行异常!!!,桌子号="+table.tableId);
				}
			}
			for(Integer tableId : delIds){
				tableMap.remove(tableId);
			}
			delIds = null;
		}catch(Exception e){
			logger.debug("桌子运行异常!!!,信息="+e.getMessage());
		}finally{
			this.unLock();
		}
	}
	
	public TableAttrib getTable(int tableId){
		return tableMap.get(tableId);
	}
	
	private int getTableId(int tableType){
		int tableId = 0;
		if(tableType == 1){
			tableId = tableId_NN;
			tableId_NN++;
		}else{
			if(tableId_ZJH >= 1000000){
				tableId_ZJH = 100000;
			}
			tableId = tableId_ZJH;
			tableId_ZJH++;
		}
		
		return tableId;
	}
	
	//初始化牛牛桌子
	private void initNNTable(){
		Collection<RoomConfig> cfgs = cfgManager.getAll();
		for(RoomConfig cfg : cfgs){
			if(cfg.getTableType() != GameDefine.GAME_TYPE_NN){
				continue;
			}
			int tableNum = cfg.getTableNum();
			int cfgId = cfg.getId();
			int tableType = cfg.getTableType();
			for(int i=0; i<tableNum; i++){
				this.createTable(tableType, cfgId, 0);
			}
		}
	}
	
	public List<TableAttrib> findTables_cfgId(int cfgId){
		List<TableAttrib> retList = new ArrayList<>();
		Collection<TableAttrib> tables = tableMap.values();
		for(TableAttrib table : tables){
			if(table.cfgId == cfgId){
				retList.add(table);
			}
		}
		return retList;
	}
	
	public List<TableAttrib> findTables_gameType(int gameType){
		List<TableAttrib> retList = new ArrayList<>();
		Collection<TableAttrib> tables = tableMap.values();
		for(TableAttrib table : tables){
			if(table.gameType == gameType){
				retList.add(table);
			}
		}
		return retList;
	}
	
	
	public TableAttrib createTable(int gameType, int cfgId, long createPlayer){
		int tableId = getTableId(gameType);
		TableAttrib table = new TableAttrib(tableId, gameType, cfgId, createPlayer, this.tableStateNNManager,
				this.tableStateZJHManager, this.accountManager, this, 
				this.cfgManager, this.walletManager, gameDataManager, sessionManager);
		this.tableMap.put(table.tableId, table);
		return table;
	}
	
	//加载机器人
	public void loadRobot(){
		int N = cfgManager.getAllCfgRobotNum();
		for(int i=0; i<N; i++){
			String accountName = "robot-";
			if(i<10){
				accountName += "00";
			}else if(i<100){
				accountName += "0";
			}
			accountName += ""+i;
			AccountEntity accountEntity = accountManager.findOf_uuid(accountName);
			if(null != accountEntity){
				GameDataAttrib robotAttrib = this.gameDataManager.getGameData(accountEntity.getId());
				robotAttrib.onLine = 1;
				robotAttrib.robot = 1;
			}
		}
	}
	
	private List<Long> getRobotNumOfCfg(int cfgId){
		List<Long> retList = new ArrayList<>();
		List<GameDataAttrib> robotList = this.gameDataManager.getAllRobot();
		for(GameDataAttrib robotAttrib : robotList){
			if(robotAttrib.tableId == 0){
				continue;
			}
			TableAttrib table = this.getTable(robotAttrib.tableId);
			if(null != table && table.cfgId == cfgId){
				retList.add(robotAttrib.accountId);
			}
		}
		return retList;
	}
	
	private void resetRobotWinMoney(){
		CoolTimeResult timeResult = checkResetTimeManager.checkSysResetTime(CoolTimeConfigType.SYSTIME_RESET_ROBOT_WIN, true);
		if(timeResult.bReset){
			long totalWin = profitPoolManager.getRobotWinMoney();
			EventAttrib eventAttrib = null;
			eventAttrib = new EventAttrib(EventDefine.EVENT_GAME);
			eventAttrib.addEventParam("0");
			eventAttrib.addEventParam(String.valueOf(eventAttrib.eventTime));
			eventAttrib.addEventParam(totalWin);
			eventAttrib.addEventParam(0);
			eventTriggerManager.triggerEvent(eventAttrib);
			profitPoolManager.resetCurrWinMoney();
		}
	}
	
	public void joinRobot(TableAttrib table, long currTime){
		int robotNum = 0;
		List<SeatAttrib> emptySeats = new ArrayList<>();
		for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
			SeatAttrib seat = table.getSeat(seatIndex);
			if(seat.accountId <= 0 && currTime >= seat.joinWaitTime){
				emptySeats.add(seat);
			}
		}
		if(emptySeats.isEmpty() || robotNum >= 3){
			return;
		}
		int randVal = (int) (((Math.random()*100)%emptySeats.size())+1);
		int index = 0;
		while(true){
			if(index >= randVal){
				break;
			}
			SeatAttrib emptySeat = emptySeats.get(index);
			if(null == emptySeat){
				break;
			}
			GameDataAttrib robotAttrib = table.gameDataManager.getIdleRobot();
			if(null == robotAttrib){
				break;
			}
			
			emptySeat.accountId = robotAttrib.accountId;
			robotAttrib.tableId = table.tableId;
			
			robotAttrib.swapTable = (int) ((Math.random()*1000)%10 + 1);
			int joinLimit = table.cfgManager.getCfg(table.cfgId).getJoinLimit();
			int max = joinLimit*5;
			int needMoney = (int) ((Math.random()*1000000)%max + joinLimit);
			long robotMoney = this.walletManager.getRoomCard(emptySeat.accountId);
			if(robotMoney < needMoney){
				this.walletManager.addRoomCard(emptySeat.accountId, (needMoney-robotMoney), false);
			}
			
			if(currTime > robotAttrib.updateNickTime){
				long inner = (long) ((Math.random()*100)%16 + 8);
				inner = inner*60*60*1000;
				robotAttrib.updateNickTime = currTime + inner;
				AccountEntity robotEntity = accountManager.load(robotAttrib.accountId);
				if(null != robotEntity){
					robotEntity.setNick(accountManager.getRandomNickName());
				}
			}
			
			index++;
		}
	}
	
	//运行机器人
	private void runRobotAct(){	
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		List<RoomConfig> cfgs = cfgManager.getAllOfType(GameDefine.GAME_TYPE_NN);
		for(RoomConfig cfg : cfgs){
			//此场次的所有桌子
			List<TableAttrib> allTables = this.findTables_cfgId(cfg.getId());
			//一个人在桌子上的玩家列表
			List<Long> tableOncePlayerList = new ArrayList<>();
			//有空座位的桌子列表
			List<TableAttrib> emptyTabls = new ArrayList<>();
			int joinLimit = cfg.getJoinLimit();
			for(TableAttrib table : allTables){
				int playerNum = table.getTablePlayerNum();
				if(playerNum != 1){
					continue;
				}
				for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
					SeatAttrib seat = table.getSeat(seatIndex);
					if(seat.accountId > 0){
						tableOncePlayerList.add(seat.accountId);
						break;
					}
				}
			}
			
			for(long accountId : tableOncePlayerList){
				GameDataAttrib theDataAttrib = this.gameDataManager.getGameData(accountId);
				if(null == theDataAttrib){
					continue;
				}
				if(theDataAttrib.robot == 0){
					//玩家
					if(currTime > theDataAttrib.onceWaitTime){
						TableAttrib table = this.getTable(theDataAttrib.tableId);
						if(null != table){
							SeatAttrib emptySeat = table.getEmptySeat();
							if(null != emptySeat){
								GameDataAttrib robotAttrib = this.gameDataManager.getIdleRobot();
								if(null != robotAttrib){									
									robotAttrib.tableId = table.tableId;
									emptySeat.accountId = robotAttrib.accountId; 
									emptySeat.reset();
									if(currTime > robotAttrib.updateNickTime){
										long inner = (long) ((Math.random()*100)%16 + 8);
										inner = inner*60*60*1000;
										robotAttrib.updateNickTime = currTime + inner;
										AccountEntity robotEntity = accountManager.load(robotAttrib.accountId);
										if(null != robotEntity){
											robotEntity.setNick(accountManager.getRandomNickName());
										}
									}
									
									long playerMoney = this.walletManager.getRoomCard(theDataAttrib.accountId);
									int minMoney = (int) (playerMoney*0.6);
									int maxMoney = (int) (playerMoney*1.5);
									int needMoney = (int) (Math.random()*(maxMoney - minMoney) + minMoney + joinLimit);
									long robotMoney = this.walletManager.getRoomCard(emptySeat.accountId);
									if(needMoney != robotMoney){
										this.walletManager.addRoomCard(emptySeat.accountId, (needMoney-robotMoney), false);
									}
									table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
								}
							}
						}
					}
				}else{
					//机器人
					if(currTime > theDataAttrib.onceWaitTime){
						TableAttrib table = this.getTable(theDataAttrib.tableId);
						if(null != table){
							SeatAttrib seat = table.getPlayerSeat(accountId);
							if(null != seat){
								theDataAttrib.tableId = 0;
								seat.accountId = 0;
								seat.reset();
							}
						}
					}
				}
			}
			int totalRobotNum = this.getRobotNumOfCfg(cfg.getId()).size();
			int totalNeedRobot = cfg.getRobotNum();
			int subNum = totalNeedRobot-totalRobotNum;
			if(subNum > 0){
				for(TableAttrib table : allTables){
					int playerNum = table.getTablePlayerNum();
					if(playerNum == 0 || playerNum == 1){
						emptyTabls.add(table);
					}
				}
				for(int i=0; i<subNum; i++){
					GameDataAttrib robotAttrib = this.gameDataManager.getIdleRobot();
					if(null == robotAttrib){
						break;
					}
					if(emptyTabls.isEmpty()){
						break;
					}
					int tableIndex = (int) ((Math.random()*1000)%emptyTabls.size());
					TableAttrib findTable = emptyTabls.get(tableIndex);
					SeatAttrib emptySeat = findTable.getEmptySeat();
					if(null != emptySeat){
						boolean bAdd = false;
						int thePlayerNum = findTable.getTablePlayerNum();
						if(thePlayerNum == 1){
							SeatAttrib otherSeat = findTable.getNextSeat(emptySeat.seatIndex);
							GameDataAttrib otherGameAttrib = this.gameDataManager.getGameData(otherSeat.accountId);
							if(otherGameAttrib.robot == 0){
								if(currTime > otherGameAttrib.onceWaitTime){
									bAdd = true;
								}
							}else{
								bAdd = true;
							}
							
						}else{
							bAdd = true;
						}
						if(bAdd){
							emptySeat.accountId = robotAttrib.accountId;
							robotAttrib.tableId = findTable.tableId;
							robotAttrib.swapTable = (int) ((Math.random()*1000)%10 + 1);
							robotAttrib.onceWaitTime = (long) (currTime + ((Math.random()*10000)%20+6)*1000);
							
							if(currTime > robotAttrib.updateNickTime){
								long inner = (long) ((Math.random()*100)%16 + 8);
								inner = inner*60*60*1000;
								robotAttrib.updateNickTime = currTime + inner;
								AccountEntity robotEntity = accountManager.load(robotAttrib.accountId);
								if(null != robotEntity){
									robotEntity.setNick(accountManager.getRandomNickName());
								}
							}
							
							int minMoney = joinLimit*2;
							int maxMoney = joinLimit*6;
							SeatAttrib otherSeat = findTable.getNextSeat(emptySeat.seatIndex);
							if(otherSeat.accountId > 0){
								GameDataAttrib otherGameAttrib = this.gameDataManager.getGameData(otherSeat.accountId);
								if(otherGameAttrib.robot == 0){
									long playerMoney = this.walletManager.getRoomCard(otherSeat.accountId);
									minMoney = (int) (playerMoney*0.6);
									maxMoney = (int) (playerMoney*1.5);
								}
							}
							int needMoney = (int) (Math.random()*(maxMoney - minMoney) + minMoney + joinLimit);
							long robotMoney = this.walletManager.getRoomCard(otherSeat.accountId);
							if(needMoney != robotMoney){
								this.walletManager.addRoomCard(otherSeat.accountId, (needMoney-robotMoney), false);
							}
							
							findTable.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
						}
					}else{
						emptyTabls.remove(findTable);
					}
				}				
			}
		}
		
		List<TableAttrib> zjhTables = this.findTables_gameType(GameDefine.GAME_TYPE_ZJH);
		for(TableAttrib table : zjhTables){
			int theGamedPlayerNum = 0;
			for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
				SeatAttrib seat = table.getSeat(seatIndex);
				if(seat.accountId > 0){	
					GameDataAttrib dataAttrib = table.gameDataManager.getGameData(seat.accountId);
					if(dataAttrib.robot == 0){
						theGamedPlayerNum++;
					}
				}
			}
			if(theGamedPlayerNum > 0 && table.createPlayer == 0){
				//有真实玩家且不是私人房间
				this.joinRobot(table, currTime);
			}
		}
	}
	
}
