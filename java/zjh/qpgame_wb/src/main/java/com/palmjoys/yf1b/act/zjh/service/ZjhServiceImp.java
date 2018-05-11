package com.palmjoys.yf1b.act.zjh.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.framework.common.manager.ErrorCodeManager;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;
import com.palmjoys.yf1b.act.zjh.manager.GameCfgManager;
import com.palmjoys.yf1b.act.zjh.manager.GameDataManager;
import com.palmjoys.yf1b.act.zjh.manager.GameLogicManager;
import com.palmjoys.yf1b.act.zjh.model.GameDataAttrib;
import com.palmjoys.yf1b.act.zjh.model.GameDefine;
import com.palmjoys.yf1b.act.zjh.model.RoomCfgVo;
import com.palmjoys.yf1b.act.zjh.model.SeatAttrib;
import com.palmjoys.yf1b.act.zjh.model.TableAttrib;
import com.palmjoys.yf1b.act.zjh.model.TableCfgVo;
import com.palmjoys.yf1b.act.zjh.model.TableVo;
import com.palmjoys.yf1b.act.zjh.model.ZJHMessageDefine;
import com.palmjoys.yf1b.act.zjh.resource.RoomConfig;

@Service
public class ZjhServiceImp implements ZjhService{
	@Autowired
	private GameCfgManager cfgManager;
	@Autowired
	private GameLogicManager logicManager;
	@Autowired
	private ErrorCodeManager errManager;
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private GameDataManager gameDataManager;

	@Override
	public Object zjh_get_room_list(Long accountId, int gameType) {
		RoomCfgVo retVo = new RoomCfgVo();
		List<RoomConfig> cfgs = cfgManager.getAllOfType(gameType);
		for(RoomConfig cfg : cfgs){
			retVo.addItem(cfg.getId(), cfg.getBaseScore(), cfg.getOnceMax(), cfg.getJoinLimit());
		}
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object zjh_get_table_list(Long accountId, int cfgId) {
		long money = walletManager.getRoomCard(accountId);
		RoomConfig cfg = cfgManager.getCfg(cfgId);
		if(null == cfg || money < cfg.getJoinLimit()){
			int err = ZJHMessageDefine.ZJHGAME_ERROR_JOIN_LIMIT;
			return Result.valueOfError(err, errManager.Error2Desc(err), null);
		}
		
		TableCfgVo retVo = new TableCfgVo();
		logicManager.lock();
		try{
			List<TableAttrib> tables = logicManager.findTables_cfgId(cfgId);
			for(TableAttrib table : tables){
				retVo.addItem(table.tableId, table.getTablePlayerNum());
			}
		}finally{
			logicManager.unLock();
		}
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object zjh_quick_join(Long accountId, int cfgId) {
		int err = ZJHMessageDefine.ZJHGAME_ERROR_TABLE_UNEMPTY;
		TableVo retVo = null;
		logicManager.lock();
		try{
			while(true){
				RoomConfig cfg = cfgManager.getCfg(cfgId);
				if(null == cfg){
					err = ZJHMessageDefine.ZJHGAME_ERROR_COMMAND_PARAM;
					break;
				}
				TableAttrib findtable = null;
				
				if(cfg.getTableType() == GameDefine.GAME_TYPE_NN){
					//牛牛
					List<TableAttrib> haveEmptySeatTables = new ArrayList<>();
					List<TableAttrib> tables = logicManager.findTables_cfgId(cfgId);
					for(TableAttrib table : tables){
						SeatAttrib seat = table.getEmptySeat();
						if(null != seat){
							haveEmptySeatTables.add(table);
							break;
						}
					}
					if(haveEmptySeatTables.isEmpty() == false){
						int tmpIndex = (int) ((Math.random()*1000)%haveEmptySeatTables.size());
						findtable = haveEmptySeatTables.get(tmpIndex);
						SeatAttrib seat = findtable.getEmptySeat();
						seat.accountId = accountId;
						seat.reset();
						
						SeatAttrib otherSeat = findtable.getNextSeat(seat.seatIndex);
						if(otherSeat.accountId > 0){
							GameDataAttrib tmpAttrib = findtable.gameDataManager.getGameData(otherSeat.accountId);
							if(tmpAttrib.robot == 1){
								//座位上有人且是机器人
								long playerMoney = this.walletManager.getRoomCard(seat.accountId);
								long minMoney = (long) (playerMoney*0.6);
								long maxMoney = (long) (playerMoney*1.5);
								long needMoney = (long) (Math.random()*(maxMoney - minMoney) + minMoney + cfg.getJoinLimit());
								long robotMoney = this.walletManager.getRoomCard(otherSeat.accountId);
								if(needMoney != robotMoney){
									this.walletManager.addRoomCard(otherSeat.accountId, (needMoney-robotMoney), false);
								}
							}
						}else{
							//座位上没有机器人,加入一个
							GameDataAttrib robotattrib = this.gameDataManager.getIdleRobot();
							if(null != robotattrib){
								long playerMoney = this.walletManager.getRoomCard(seat.accountId);
								long minMoney = (long) (playerMoney*0.6);
								long maxMoney = (long) (playerMoney*1.5);
								long needMoney = (long) (Math.random()*(maxMoney - minMoney) + minMoney + cfg.getJoinLimit());
								long robotMoney = this.walletManager.getRoomCard(otherSeat.accountId);
								if(needMoney != robotMoney){
									this.walletManager.addRoomCard(otherSeat.accountId, (needMoney-robotMoney), false);
								}
								otherSeat.accountId = robotattrib.accountId;
								otherSeat.reset();
								robotattrib.tableId = findtable.tableId;
							}
						}
					}					
					
					if(null == findtable){
						err = ZJHMessageDefine.ZJHGAME_ERROR_TABLE_UNEMPTY;
						break;
					}else{
						findtable.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
					}
				}else{
					//金花
					long money = walletManager.getRoomCard(accountId);
					if(money < cfg.getJoinLimit()){
						err = ZJHMessageDefine.ZJHGAME_ERROR_JOIN_LIMIT;
						break;
					}
					List<TableAttrib> haveEmptySeatTables = new ArrayList<>();
					List<TableAttrib> tables = logicManager.findTables_cfgId(cfgId);
					for(TableAttrib table : tables){
						if(table.createPlayer != 0){
							//快速加入不能进私人房间
							continue;
						}
						SeatAttrib seat = table.getEmptySeat();
						if(null != seat){							
							haveEmptySeatTables.add(table);
							break;
						}
					}
					if(haveEmptySeatTables.isEmpty() == false){
						int tmpIndex = (int) ((Math.random()*1000)%haveEmptySeatTables.size());
						findtable = haveEmptySeatTables.get(tmpIndex);
						SeatAttrib seat = findtable.getEmptySeat();
						seat.reset();
						seat.accountId = accountId;
					}					
					
					if(null == findtable){
						//没有桌子了,新创建一个
						findtable = logicManager.createTable(cfg.getTableType(), cfgId, 0);
						SeatAttrib seat = findtable.getEmptySeat();
						seat.reset();
						seat.accountId = accountId;
					}else{
						findtable.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
					}
				}				
				retVo = findtable.table2TableVo();
				GameDataAttrib gameDateAttrib = gameDataManager.getGameData(accountId);
				gameDateAttrib.tableId = findtable.tableId;
				err = 0;
				break;
			}
			
		}finally{
			logicManager.unLock();
		}
		if(err < 0){
			return Result.valueOfError(err, errManager.Error2Desc(err), null);
		}
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object zjh_jion_tableId(Long accountId, int tableId, int type) {
		int err = ZJHMessageDefine.ZJHGAME_ERROR_TABLE_UNEXIST;
		TableVo retVo = null;
		logicManager.lock();
		try{
			while(true){
				TableAttrib findTable = null;
				GameDataAttrib gameDateAttrib = gameDataManager.getGameData(accountId);
				if(type == 1){
					//加入私人桌子
					findTable = logicManager.getTable(tableId);
					if(null == findTable || findTable.createPlayer <= 0){
						err = ZJHMessageDefine.ZJHGAME_ERROR_ONLY_PRIVATE_TABLE;
						break;
					}
				}else{
					int oldTableId = gameDateAttrib.tableId;
					if(tableId != oldTableId){
						TableAttrib oldTable = logicManager.getTable(oldTableId);
						if(null == oldTable){
							//原来的桌子没有了
							findTable = logicManager.getTable(tableId);
						}else{
							findTable = oldTable;
						}
					}else{
						findTable = logicManager.getTable(tableId); 
					}
				}
				
				if(null == findTable){
					gameDateAttrib.tableId = 0;
					break;
				}
				
				SeatAttrib mySeat = findTable.getPlayerSeat(accountId);
				if(null == mySeat){
					//不在桌子上,检查进入限制
					RoomConfig cfg = cfgManager.getCfg(findTable.cfgId);
					long money = walletManager.getRoomCard(accountId);
					if(money < cfg.getJoinLimit()){
						err = ZJHMessageDefine.ZJHGAME_ERROR_JOIN_LIMIT;
						break;
					}
					mySeat = findTable.getEmptySeat();
					if(null == mySeat){
						//没有空座位了
						err = ZJHMessageDefine.ZJHGAME_ERROR_SEAT_UNEMPTY;
						break;	
					}
					mySeat.accountId = accountId;
					mySeat.reset();
					
					if(findTable.gameType == GameDefine.GAME_TYPE_NN){
						SeatAttrib otherSeat = findTable.getNextSeat(mySeat.seatIndex);
						if(otherSeat.accountId > 0){
							GameDataAttrib tmpAttrib = findTable.gameDataManager.getGameData(otherSeat.accountId);
							if(tmpAttrib.robot == 1){
								//座位上有人且是机器人
								long playerMoney = this.walletManager.getRoomCard(mySeat.accountId);
								long minMoney = (long) (playerMoney*0.6);
								long maxMoney = (long) (playerMoney*1.5);
								long needMoney = (long) (Math.random()*(maxMoney - minMoney) + minMoney + cfg.getJoinLimit());
								long robotMoney = this.walletManager.getRoomCard(otherSeat.accountId);
								if(needMoney != robotMoney){
									this.walletManager.addRoomCard(otherSeat.accountId, (needMoney-robotMoney), false);
								}
							}
						}
					}
					
					findTable.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
				}
				long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
				gameDateAttrib.onceWaitTime = (long) (currTime + ((Math.random()*10000)%20+6)*1000);
				gameDateAttrib.tableId = findTable.tableId;
				retVo = findTable.table2TableVo();
				
				err = 0;
				break;
			}
			
		}finally{
			logicManager.unLock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errManager.Error2Desc(err), null);
		}
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object zjh_table_create(Long accountId, int cfgId) {
		long money = walletManager.getRoomCard(accountId);
		RoomConfig cfg = cfgManager.getCfg(cfgId);
		if(null == cfg || cfg.getTableType() != GameDefine.GAME_TYPE_ZJH){
			int err = ZJHMessageDefine.ZJHGAME_ERROR_COMMAND_PARAM;
			return Result.valueOfError(err, errManager.Error2Desc(err), null);
		}
		if(money < cfg.getJoinLimit()){
			int err = ZJHMessageDefine.ZJHGAME_ERROR_JOIN_LIMIT;
			return Result.valueOfError(err, errManager.Error2Desc(err), null);
		}
		
		int err = ZJHMessageDefine.ZJHGAME_ERROR_COMMAND_PARAM;
		TableVo retVo = null;
		logicManager.lock();
		try{
			TableAttrib table = logicManager.createTable(cfg.getTableType(), cfgId, accountId);
			SeatAttrib mySeat = table.getEmptySeat();
			mySeat.accountId = accountId;
			GameDataAttrib gameDataAttrib = gameDataManager.getGameData(accountId);
			gameDataAttrib.tableId = table.tableId;
			
			retVo = table.table2TableVo();
			err = 0;
		}finally{
			logicManager.unLock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errManager.Error2Desc(err), null);
		}
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object zjh_table_leave(Long accountId, int tableId) {
		//牛斗斗离开桌子,不能直接离开,金花离开桌子,直接弃牌
		int err = ZJHMessageDefine.ZJHGAME_ERROR_COMMAND_PARAM;
		logicManager.lock();
		try{
			while(true){
				GameDataAttrib gameDateAttrib = gameDataManager.getGameData(accountId);
				tableId = gameDateAttrib.tableId;
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					gameDateAttrib.tableId = 0;
					err = 0;
					break;
				}
				RoomConfig cfg = cfgManager.getCfg(table.cfgId);
				SeatAttrib mySeat = table.getPlayerSeat(accountId);
				if(null == mySeat){
					gameDateAttrib.tableId = 0;
					err = 0;
					break;
				}				
				if(cfg.getTableType() == GameDefine.GAME_TYPE_NN){
					if(table.gameState != GameDefine.STATE_TABLE_IDLE){
						//设置为离开标志,游戏结束后踢出
						mySeat.bLeave = true;
					}else{
						//游戏还没开始,可以直接离开
						gameDateAttrib.tableId = 0;
						mySeat.accountId = 0;
						table.sendKickNotify(accountId, 2);
						for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
							SeatAttrib seat = table.seats.get(seatIndex);
							if(seat.accountId > 0){
								GameDataAttrib theAttrib = table.gameDataManager.getGameData(seat.accountId);
								long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
								theAttrib.onceWaitTime = (long) (currTime + ((Math.random()*10000)%20+6)*1000);
								table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
							}
						}
					}
				}else{
					//金花桌子
					if(table.gameState != GameDefine.STATE_TABLE_ZJH_OVER 
							|| (table.gameState==GameDefine.STATE_TABLE_ZJH_OVER && table.bExec)){
						mySeat.btState = GameDefine.ACT_STATE_DROP;
					}else{
						mySeat.btState = GameDefine.ACT_STATE_WAIT;
					}
					mySeat.btVal = GameDefine.BT_VAL_DROP;
					mySeat.bLookCard = true;
					mySeat.bGamed = false;
					mySeat.handCards.clear();
					
					
					gameDateAttrib.tableId = 0;
					mySeat.accountId = 0;
					long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
					mySeat.joinWaitTime = (long) (currTime + ((Math.random()*100)%45+3)*1000);
					for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
						SeatAttrib seat = table.seats.get(seatIndex);
						if(seat.accountId > 0){
							table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
						}
					}
				}				
				err = 0;
				break;
			}
		}finally{
			logicManager.unLock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errManager.Error2Desc(err), null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object zjh_table_bet(Long accountId, int tableId, int seatIndex, int bt, int btVal) {
		int err = ZJHMessageDefine.ZJHGAME_ERROR_COMMAND_PARAM;
		logicManager.lock();
		try{
			while(true){
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					break;
				}
				SeatAttrib mySeat = table.getSeat(seatIndex);
				if(null == mySeat || mySeat.accountId != accountId.longValue()){
					break;
				}
				RoomConfig cfg = cfgManager.getCfg(table.cfgId);
				if(null == cfg){
					break;
				}
				long myMoney = walletManager.getRoomCard(accountId);
				if(cfg.getTableType() == GameDefine.GAME_TYPE_NN){
					//牛牛桌子,只有下注
					if(myMoney < btVal || btVal <= 0){
						err = ZJHMessageDefine.ZJHGAME_ERROR_BET_MONEY;
						break;
					}
					if(mySeat.btState != GameDefine.ACT_STATE_WAIT){
						err = ZJHMessageDefine.ZJHGAME_ERROR_UNBT_STATE;
						break;
					}
					if(table.gameState != GameDefine.STATE_TABLE_NN_BET){
						err = ZJHMessageDefine.ZJHGAME_ERROR_UNBT_STATE;
						break;
					}
					
					SeatAttrib otherSeat = table.getNextSeat(mySeat.seatIndex);
					long otherPlayerMoney = walletManager.getRoomCard(otherSeat.accountId);
					long chkMoney = myMoney;
					if(chkMoney > otherPlayerMoney){
						chkMoney = otherPlayerMoney;
					}
					
					//牛牛下注,最小下1/36,最大1/6
					long minMoney = chkMoney/36;
					long maxMoney = chkMoney/6;
					if(btVal < minMoney || btVal>maxMoney){
						err = ZJHMessageDefine.ZJHGAME_ERROR_BET_MONEY;
						break;
					}					
					mySeat.btState = GameDefine.ACT_STATE_BT;
					mySeat.btVal = GameDefine.BT_VAL_BETSAME;
					walletManager.addRoomCard(accountId, 0-btVal, false);
					table.playerBetMoney(mySeat.accountId, btVal);
					//发送下注通知
					table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, mySeat.seatIndex, btVal);
				}else{
					//金花桌子
					if(bt < GameDefine.BT_VAL_DROP || bt > GameDefine.BT_VAL_BETADD){
						break;
					}
					if(bt == GameDefine.BT_VAL_DROP){
						//弃牌
						if(mySeat.btState == GameDefine.ACT_STATE_DROP){
							err = ZJHMessageDefine.ZJHGAME_ERROR_UNBT_STATE;
							break;
						}
						mySeat.btState = GameDefine.ACT_STATE_DROP;
						mySeat.btVal = bt;
						mySeat.bLookCard = true;
						//发送下注通知
						table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, mySeat.seatIndex, 0);
					}else if(bt == GameDefine.BT_VAL_LOOCK){
						//看牌
						if(mySeat.btState == GameDefine.ACT_STATE_DROP){
							err = ZJHMessageDefine.ZJHGAME_ERROR_UNBT_STATE;
							break;
						}
						if(mySeat.bLookCard == false){
							mySeat.bLookCard = true;
							
							//发送看牌通知
							mySeat.btVal = bt;
							table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_LOOKCARD_NOTIFY, mySeat.seatIndex, 0);
							err = 0;
							break;
						}
					}else if(bt == GameDefine.BT_VAL_COMPARAE){
						//比牌
						if(mySeat.btState != GameDefine.ACT_STATE_WAIT){
							err = ZJHMessageDefine.ZJHGAME_ERROR_UNBT_STATE;
							break;
						}
						SeatAttrib compareSeat = table.getSeat(btVal);
						if(compareSeat.btState == GameDefine.ACT_STATE_DROP){
							err = ZJHMessageDefine.ZJHGAME_ERROR_UNBT_STATE;
							break;
						}
						if(mySeat.seatIndex != table.btIndex){
							err = ZJHMessageDefine.ZJHGAME_ERROR_UNBT_STATE;
							break;
						}
						
						table.compareCardAttrib.compareScr = seatIndex;
						table.compareCardAttrib.compareDst = btVal;
						table.bExec = false;
						table.gameState = GameDefine.STATE_TABLE_ZJH_COMPARE;						
					}else {
						//下注
						if(mySeat.btState != GameDefine.ACT_STATE_WAIT){
							err = ZJHMessageDefine.ZJHGAME_ERROR_UNBT_STATE;
							break;
						}
						if(mySeat.seatIndex != table.btIndex){
							err = ZJHMessageDefine.ZJHGAME_ERROR_UNBT_STATE;
							break;
						}
						
						int tmpChkMoney = table.unLookBetMoney;
						if(mySeat.bLookCard){
							tmpChkMoney = table.lookBetMoney;
						}
						if(myMoney <= tmpChkMoney){
							//只能比牌
							err = ZJHMessageDefine.ZJHGAME_ERROR_BET_ONLY_COMPARE;
							break;
						}
						
						if(table.prevSeatIndex >= 0){
							SeatAttrib prevSeat = table.getSeat(table.prevSeatIndex);
							if(prevSeat.btVal == GameDefine.BT_VAL_BETALL && bt != GameDefine.BT_VAL_BETALL){
								//上家是全下只能比牌
								err = ZJHMessageDefine.ZJHGAME_ERROR_ONLY_COMPARE;
								break;
							}
						}
						
						int onceMax = table.cfgManager.getCfg(table.cfgId).getOnceMax();						
						if(bt == GameDefine.BT_VAL_BETALL){
							//全下,只能有两个人,且全下的钱是其中最少的那个人的钱
							int playerNum = table.getGamedPlayerSeats(0).size();
							playerNum += table.getGamedRobotSeats(0).size();
							if(playerNum != 2){
								err = ZJHMessageDefine.ZJHGAME_ERROR_BETALL_ONLY_DOUBLEPLAYER;
								break;
							}
							if(table.roundNum < 2){
								//至少一轮以后才能全下
								err = ZJHMessageDefine.ZJHGAME_ERROR_ONLY_ONCEROUND_BETALL;
								break;
							}
							
							long mySeatBetMoney = table.getPlayerBetMoney(mySeat.accountId);
							long needbetMoney = btVal-mySeatBetMoney;
							if(needbetMoney <= 0 || myMoney < needbetMoney){
								err = ZJHMessageDefine.ZJHGAME_ERROR_BET_MONEY;
								break;
							}							
							
							table.playerBetMoney(mySeat.accountId, needbetMoney);
							table.walletManager.addRoomCard(accountId, 0-needbetMoney, false);
							table.prevAllBetMoney = needbetMoney;
							table.prevSeatIndex = mySeat.seatIndex;
							table.prevSeatBetLookState = mySeat.bLookCard==true?1:0;
							
							mySeat.btState = GameDefine.ACT_STATE_BT;
							mySeat.btVal = bt;
							
							//发送下注通知
							table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, mySeat.seatIndex, needbetMoney);
							
						}else if(bt == GameDefine.BT_VAL_BETSAME){
							//跟注
							if(mySeat.btState != GameDefine.ACT_STATE_WAIT){
								err = ZJHMessageDefine.ZJHGAME_ERROR_UNBT_STATE;
								break;
							}
							if(mySeat.seatIndex != table.btIndex){
								err = ZJHMessageDefine.ZJHGAME_ERROR_UNBT_STATE;
								break;
							}							
							
							long mySeatBetMoney = table.getPlayerBetMoney(mySeat.accountId);
							long needbetMoney = btVal-mySeatBetMoney;
							if(needbetMoney <= 0 || myMoney < needbetMoney || needbetMoney>onceMax){
								err = ZJHMessageDefine.ZJHGAME_ERROR_BET_MONEY;
								break;
							}
							
							if(mySeat.bLookCard == false){
								//我是没有看牌的,其它看了牌的都要*2
								if(needbetMoney < table.unLookBetMoney){
									err = ZJHMessageDefine.ZJHGAME_ERROR_BET_MONEY;
									break;
								}
								
								if(table.unLookBetMoney < needbetMoney){
									table.unLookBetMoney = (int) needbetMoney;
								}
								
								long tmpMoney = needbetMoney*2;
								if(tmpMoney > onceMax){
									tmpMoney = onceMax;
								}
								if(table.lookBetMoney < tmpMoney){
									table.lookBetMoney = (int) tmpMoney;
								}
							}else{
								if(needbetMoney < table.lookBetMoney){
									err = ZJHMessageDefine.ZJHGAME_ERROR_BET_MONEY;
									break;
								}
								
								if(table.lookBetMoney < needbetMoney){
									table.lookBetMoney = (int) needbetMoney;
								}
								
								long tmpMoney = (needbetMoney+1)/2;
								if(tmpMoney > onceMax){
									tmpMoney = onceMax;
								}
								if(table.unLookBetMoney < tmpMoney){
									table.unLookBetMoney = (int) tmpMoney;
								}
							}
							
							table.playerBetMoney(mySeat.accountId, needbetMoney);
							table.walletManager.addRoomCard(accountId, 0-needbetMoney, false);
							table.prevSeatIndex = mySeat.seatIndex;
							table.prevSeatBetLookState = mySeat.bLookCard==true?1:0;
							
							mySeat.btState = GameDefine.ACT_STATE_BT;
							mySeat.btVal = bt;
							//发送下注通知
							table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, mySeat.seatIndex, needbetMoney);
						}else if(bt == GameDefine.BT_VAL_BETADD){
							//加注
							if(mySeat.btState != GameDefine.ACT_STATE_WAIT){
								err = ZJHMessageDefine.ZJHGAME_ERROR_UNBT_STATE;
								break;
							}
							if(mySeat.seatIndex != table.btIndex){
								err = ZJHMessageDefine.ZJHGAME_ERROR_UNBT_STATE;
								break;
							}
							long mySeatBetMoney = table.getPlayerBetMoney(mySeat.accountId);
							long needbetMoney = btVal-mySeatBetMoney;
							if(needbetMoney <= 0 || myMoney < needbetMoney || needbetMoney>onceMax){
								err = ZJHMessageDefine.ZJHGAME_ERROR_BET_MONEY;
								break;
							}
							if(mySeat.bLookCard == false){
								if(needbetMoney < table.unLookBetMoney){
									err = ZJHMessageDefine.ZJHGAME_ERROR_BET_MONEY;
									break;
								}
								
								if(table.unLookBetMoney < needbetMoney){
									table.unLookBetMoney = (int) needbetMoney;
								}
								
								long tmpMoney = needbetMoney*2;
								if(tmpMoney > onceMax){
									tmpMoney = onceMax;
								}
								if(table.lookBetMoney < tmpMoney){
									table.lookBetMoney = (int) tmpMoney;
								}								
							}else{
								if(needbetMoney < table.lookBetMoney){
									err = ZJHMessageDefine.ZJHGAME_ERROR_BET_MONEY;
									break;
								}
								
								if(table.lookBetMoney < needbetMoney){
									table.lookBetMoney = (int) needbetMoney;
								}
								
								long tmpMoney = (needbetMoney+1)/2;
								if(tmpMoney > onceMax){
									tmpMoney = onceMax;
								}
								if(table.unLookBetMoney < tmpMoney){
									table.unLookBetMoney = (int) tmpMoney;
								}
							}
							
							table.playerBetMoney(mySeat.accountId, needbetMoney);
							table.walletManager.addRoomCard(accountId, 0-needbetMoney, false);
							table.prevSeatIndex = mySeat.seatIndex;
							table.prevSeatBetLookState = mySeat.bLookCard==true?1:0;
							
							mySeat.btState = GameDefine.ACT_STATE_BT;
							mySeat.btVal = bt;
							//发送下注通知
							table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, mySeat.seatIndex, needbetMoney);
						}
					}
				}
				
				err = 0;
				break;
			}
		}finally{
			logicManager.unLock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errManager.Error2Desc(err), null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object zjh_table_call_banker(Long accountId, int tableId, int bt) {
		int err = ZJHMessageDefine.ZJHGAME_ERROR_COMMAND_PARAM;
		logicManager.lock();
		try{
			while(true){
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					err = ZJHMessageDefine.ZJHGAME_ERROR_TABLE_UNEXIST;
					break;
				}
				if(table.gameState != GameDefine.STATE_TABLE_NN_CALL_BANKER){
					err = ZJHMessageDefine.ZJHGAME_ERROR_TABLE_UNEXIST;
					break;
				}
				SeatAttrib seat = table.getPlayerSeat(accountId);
				if(null == seat || seat.seatIndex != table.btIndex){
					err = ZJHMessageDefine.ZJHGAME_ERROR_TABLE_UNEXIST;
					break;
				}
				if(bt == 0){
					seat.btState = GameDefine.ACT_STATE_DROP;
				}else{
					seat.btState = GameDefine.ACT_STATE_BT;
				}
				
				err = 0;
				break;
			}
		}finally{
			logicManager.unLock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errManager.Error2Desc(err), null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object zjh_table_nn_game_ready(Long accountId, int tableId) {
		int err = ZJHMessageDefine.ZJHGAME_ERROR_COMMAND_PARAM;
		logicManager.lock();
		try{
			while(true){
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					err = ZJHMessageDefine.ZJHGAME_ERROR_TABLE_UNEXIST;
					break;
				}
				SeatAttrib mySeat = table.getPlayerSeat(accountId);
				if(null == mySeat){
					err = ZJHMessageDefine.ZJHGAME_ERROR_TABLE_UNEXIST;
					break;
				}
				mySeat.btState = GameDefine.ACT_STATE_BT;
				table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_GAME_READY_NOTIFY);
				err = 0;
				break;
			}
			
		}finally{
			logicManager.unLock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errManager.Error2Desc(err), null);
		}
		return Result.valueOfSuccess();
	}
}
