package com.palmjoys.yf1b.act.dzpker.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.dzpker.entity.DzpkerCfgEntity;
import com.palmjoys.yf1b.act.dzpker.entity.DzpkerOrderEntity;
import com.palmjoys.yf1b.act.dzpker.entity.DzpkerPlayerRecordEntity;
import com.palmjoys.yf1b.act.dzpker.entity.DzpkerTableRecordEntity;
import com.palmjoys.yf1b.act.dzpker.manager.DzpkerCfgManager;
import com.palmjoys.yf1b.act.dzpker.manager.DzpkerOrderManager;
import com.palmjoys.yf1b.act.dzpker.manager.DzpkerRecordManager;
import com.palmjoys.yf1b.act.dzpker.manager.GameLogicManager;
import com.palmjoys.yf1b.act.dzpker.model.CareerVo;
import com.palmjoys.yf1b.act.dzpker.model.DzpkerCfgVo;
import com.palmjoys.yf1b.act.dzpker.model.DzpkerDefine;
import com.palmjoys.yf1b.act.dzpker.model.DzpkerOrderVo;
import com.palmjoys.yf1b.act.dzpker.model.GameDefine;
import com.palmjoys.yf1b.act.dzpker.model.InsuranceCfgAttrib;
import com.palmjoys.yf1b.act.dzpker.model.JoinedTableListVo;
import com.palmjoys.yf1b.act.dzpker.model.SeatAttrib;
import com.palmjoys.yf1b.act.dzpker.model.StatisticsAttrib;
import com.palmjoys.yf1b.act.dzpker.model.TableAttrib;
import com.palmjoys.yf1b.act.dzpker.model.TablePlayerListVo;
import com.palmjoys.yf1b.act.dzpker.model.TablePlayerWinScoreDetailsVo;
import com.palmjoys.yf1b.act.dzpker.model.TablePrevFightRecordVo;
import com.palmjoys.yf1b.act.dzpker.model.TableVo;
import com.palmjoys.yf1b.act.dzpker.model.WinScoreAttrib;
import com.palmjoys.yf1b.act.dzpker.model.WinScoreAttribItem;
import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;
import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.account.entity.RoleEntity;
import com.palmjoys.yf1b.act.account.manager.GameDataManager;
import com.palmjoys.yf1b.act.account.manager.RoleEntityManager;
import com.palmjoys.yf1b.act.account.model.GameDataAttrib;
import com.palmjoys.yf1b.act.framework.common.manager.ErrorCodeManager;
import com.palmjoys.yf1b.act.framework.utils.MessagePushQueueUtils;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;

@Service
public class DzpkerServiceImp implements DzpkerService{
	@Autowired
	private DzpkerCfgManager cfgManager;
	@Autowired
	private GameLogicManager logicManager;
	@Autowired
	private ErrorCodeManager errCodeManager;
	@Autowired
	private GameDataManager gameDataManager;
	@Autowired
	private DzpkerRecordManager recordManager;
	@Autowired
	private DzpkerOrderManager orderManager;
	@Autowired
	private HotPromptManager hotPromptManager;
	@Autowired
	private RoleEntityManager roleEntityManager;
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private EventTriggerManager eventTriggerManager;
	
	@Override
	public Object dzpker_table_get_cfg(Long accountId) {
		DzpkerCfgVo retVo = cfgManager.getCfg();
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object dzpker_table_create(Long accountId, String tableName, int small, int big, int minJoin, int vaildTime,
			int insurance, int straddle, int buyMax) {
		TableVo retVo = null;
		int err = DzpkerDefine.DZPKER_ERROR_UNEXIST;
		logicManager.lock();
		try{
			while(true){
				long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
				long endTime = currTime + vaildTime*60*1000;
				int result = logicManager.createTable(accountId, tableName, small, big, 
						minJoin, currTime, endTime, insurance, straddle, buyMax);
				if(result < 0){
					err = result;
					break;
				}
				TableAttrib table = this.logicManager.getTable(result);
				table.tablePlayers.put(accountId, accountId);
				GameDataAttrib gameDataAttrib = gameDataManager.getAccountGameData(accountId);
				gameDataAttrib.tableId = table.tableId;
				gameDataAttrib.joinedTableIdMap.put(table.recordId, table.recordId);
				retVo = this.logicManager.table2TableVo(table);
				err = 0;
				break;
			}
		}finally{
			logicManager.unlock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errCodeManager.Error2Desc(err), null);
		}
		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object dzpker_table_start_run(Long accountId, int tableId) {
		int err = DzpkerDefine.DZPKER_ERROR_UNEXIST;		
		logicManager.lock();
		try{
			while(true){
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					break;
				}
				if(table.createPlayer != accountId.longValue()){
					err = DzpkerDefine.DZPKER_ERROR_TABLE_RUN;
					break;
				}
				int N = table.getSeatPlayerNum();
				if(N < 2){
					err = DzpkerDefine.DZPKER_ERROR_TABLE_RUN_PLAYERNUM;
					break;
				}
				
				table.start = 1;
				
				err = 0;
				break;
			}
		}finally{
			logicManager.unlock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errCodeManager.Error2Desc(err), null);
		}
		
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_table_join(Long accountId, int tableId) {
		int err = DzpkerDefine.DZPKER_ERROR_UNEXIST;		
		TableVo retVo = null;
		logicManager.lock();
		try{
			GameDataAttrib gameDataAttrib = gameDataManager.getAccountGameData(accountId);
			int oldTableId = gameDataAttrib.tableId;
			TableAttrib table = null;
			while(true){
				if(oldTableId > 0 && tableId != oldTableId){
					table = logicManager.getTable(oldTableId);
				}else{
					table = logicManager.getTable(tableId);
				}
				if(null == table){
					gameDataAttrib.tableId = 0;
					break;
				}
				table.tablePlayers.put(accountId, accountId);
				gameDataAttrib.tableId = table.tableId;
				retVo = logicManager.table2TableVo(table);
				
				SeatAttrib seat = table.getPlayerSeat(accountId);
				if(null != seat){
					table.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_SEAT_STATE_NOTIFY);
				}
				
				DzpkerPlayerRecordEntity playerRecordEntity = recordManager.loadOrCreatePlayerRecord(accountId);
				if(playerRecordEntity.getRecordTableMap().containsKey(table.recordId) == false){
					StatisticsAttrib statisticsAttrib = this.recordManager.getStatisticsAttrib(table.recordId, accountId);
					if(null != statisticsAttrib){
						statisticsAttrib.joinTableNum++;
						this.recordManager.setStatisticsAttrib(table.recordId, accountId, statisticsAttrib);
					}
				}
				
				gameDataAttrib.joinedTableIdMap.put(table.recordId, table.recordId);
				
				err = 0;
				break;
			}			
		}finally{
			logicManager.unlock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errCodeManager.Error2Desc(err), null);
		}
		
		RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountId);
		EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_JOIN);
		eventAttrib.addEventParam(roleEntity.getStarNO());
		eventAttrib.addEventParam(retVo.tableId);
		roleEntity = roleEntityManager.findOf_accountId(Long.valueOf(retVo.createPlayer));
		eventAttrib.addEventParam(roleEntity.getStarNO());
		eventAttrib.addEventParam(eventAttrib.eventTime);
		eventTriggerManager.triggerEvent(eventAttrib);
		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object dzpker_table_leave(Long accountId, int tableId) {
		int err = DzpkerDefine.DZPKER_ERROR_UNEXIST;
		logicManager.lock();
		try{
			while(true){
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					break;
				}
				GameDataAttrib gameDataAttrib = gameDataManager.getAccountGameData(accountId);
				gameDataAttrib.tableId = 0;
				
				SeatAttrib seat = table.getPlayerSeat(accountId);
				if(null != seat){
					seat.accountId = 0;
					if(seat.bGamed && seat.btResult != GameDefine.ACT_TYPE_DROP){
						//在游戏中
						seat.reset();
						seat.btState = GameDefine.BT_STATE_BT;
						seat.btResult = GameDefine.ACT_TYPE_DROP;
						table.prevBtIndex = seat.seatIndex;
						table.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_BET_STATE_NOTIFY);
					}else{
						seat.reset();
					}
					table.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_SEAT_STATE_NOTIFY);
				}
				table.tablePlayers.remove(accountId);
				err = 0;
				
				break;
			}
		}finally{
			logicManager.unlock();
		}
		if(err < 0){
			return Result.valueOfError(err, errCodeManager.Error2Desc(err), null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_seat_down(Long accountId, int tableId, int seatIndex) {
		int err = DzpkerDefine.DZPKER_ERROR_UNEXIST;
		logicManager.lock();
		try{
			while(true){
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					break;
				}
				SeatAttrib seat = table.getPlayerSeat(accountId);
				if(null != seat){
					err = DzpkerDefine.DZPKER_ERROR_SEAT_EXIST;
					break;
				}
				seat = table.getSeat(seatIndex);
				if(null == seat){
					break; 
				}
				if(seat.accountId > 0 && seat.accountId != accountId.longValue()){
					err = DzpkerDefine.DZPKER_ERROR_SEAT_UNEMPTY;
					break;
				}
				//检查坐下限制
				DzpkerTableRecordEntity recordEntity = recordManager.loadTableRecord(table.recordId);
				Map<Long, StatisticsAttrib> statisticsMap = recordEntity.getStatisticsMap();
				StatisticsAttrib statisticsAttrib = statisticsMap.get(accountId);
				if(null == statisticsAttrib
						|| statisticsAttrib.currMoney < table.joinChip){
					err = DzpkerDefine.DZPKER_ERROR_SEAT_DOWN;
					break;
				}
				DzpkerPlayerRecordEntity playerRecordEntity = recordManager.loadOrCreatePlayerRecord(accountId);
				if(playerRecordEntity.getRecordTableMap().containsKey(table.recordId) == false){
					statisticsAttrib.seatDownNum++;
					statisticsMap.put(accountId, statisticsAttrib);
					recordEntity.setStatisticsMap(statisticsMap);
				}
								
				seat.reset();
				seat.accountId = accountId;
				
				table.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_SEAT_STATE_NOTIFY);				
				err = 0;
				break;
			}			
		}finally{
			logicManager.unlock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errCodeManager.Error2Desc(err), null);
		}
		
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_seat_up(Long accountId, int tableId, int seatIndex) {
		int err = DzpkerDefine.DZPKER_ERROR_UNEXIST;
		logicManager.lock();
		try{
			while(true){
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					err = 0;
					break;
				}
				SeatAttrib seat = table.getSeat(seatIndex);
				if(null == seat){
					err = 0;
					break; 
				}
				if(seat.accountId != accountId.longValue()){
					err = DzpkerDefine.DZPKER_ERROR_SEAT_EMPTY;
					break;
				}
				
				//增加到玩家筹码中
				DzpkerTableRecordEntity recordEntity = recordManager.loadTableRecord(table.recordId);
				Map<Long, StatisticsAttrib> statisticsMap = recordEntity.getStatisticsMap();
				StatisticsAttrib statisticsAttrib = statisticsMap.get(accountId);
				if(null != statisticsAttrib){
					statisticsAttrib.currMoney += statisticsAttrib.gameBuyChip;
					statisticsAttrib.gameBuyChip = 0;
					statisticsMap.put(accountId, statisticsAttrib);
					recordEntity.setStatisticsMap(statisticsMap);
				}
				
				if(null != seat){
					seat.accountId = 0;
					if(seat.bGamed && seat.btResult != GameDefine.ACT_TYPE_DROP){
						//在游戏中
						seat.reset();
						seat.btState = GameDefine.BT_STATE_BT;
						seat.btResult = GameDefine.ACT_TYPE_DROP;
						table.prevBtIndex = seat.seatIndex;
						table.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_BET_STATE_NOTIFY);
					}else{
						seat.reset();
					}
					table.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_SEAT_STATE_NOTIFY);
				}
				
				err = 0;
				break;
			}			
		}finally{
			logicManager.unlock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errCodeManager.Error2Desc(err), null);
		}
		
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_buy_chip(Long accountId, int tableId, int chipNum) {
		int err = DzpkerDefine.DZPKER_ERROR_UNEXIST;
		logicManager.lock();
		try{
			while(true){
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					break;
				}
				if(chipNum <= 0 || chipNum > table.buyMaxChip){
					err = DzpkerDefine.DZPKER_ERROR_PARAM;
					break;
				}
				if(table.tablePlayers.containsKey(accountId) == false){
					err = DzpkerDefine.DZPKER_ERROR_PARAM;
					break;
				}
				if(table.createPlayer == accountId.longValue()){
					//房主自已购买筹码
					DzpkerOrderEntity orderEntity = orderManager.loadOrCreate(accountId, table.recordId, table.createPlayer, chipNum);
					orderEntity.setState(1);
					//增加到玩家筹码中
					DzpkerTableRecordEntity recordEntity = recordManager.loadTableRecord(table.recordId);
					Map<Long, StatisticsAttrib> statisticsMap = recordEntity.getStatisticsMap();
					StatisticsAttrib statisticsAttrib = statisticsMap.get(orderEntity.getAccountId());
					if(null == statisticsAttrib){
						statisticsAttrib = new StatisticsAttrib();
					}
					statisticsAttrib.buyTotalMoney += orderEntity.getChipNum();
					
					String msgStr = "";
					SeatAttrib seat = table.getPlayerSeat(orderEntity.getAccountId());
					if(null == seat || seat.bGamed == false){
						//不在座位上或未参与本局游戏,直接加到当前筹码中
						statisticsAttrib.currMoney += orderEntity.getChipNum();
						msgStr = "您申请的[" + orderEntity.getChipNum() + "]积分已同意,当前已到帐";
						table.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_SEAT_STATE_NOTIFY);
					}else{
						statisticsAttrib.gameBuyChip += orderEntity.getChipNum();
						msgStr = "您申请的[" + orderEntity.getChipNum() + "]积分已同意,下局游戏开始后到帐";
					}
					
					//通知玩家筹码购买成功
					@SuppressWarnings("rawtypes")
					Request pushMsg = Request.valueOf(DzpkerDefine.DZPKER_COMMAND_BUY_CHIP_SUCESS_NOTIFY,
							Result.valueOfSuccess(msgStr));
					MessagePushQueueUtils.getPushQueue(sessionManager).push2(table.createPlayer, pushMsg);					
					
					statisticsMap.put(orderEntity.getAccountId(), statisticsAttrib);
					recordEntity.setStatisticsMap(statisticsMap);					
					
					RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountId);
					EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_BUY_CHIP);
					eventAttrib.addEventParam(roleEntity.getStarNO());
					eventAttrib.addEventParam(table.tableId);
					roleEntity = roleEntityManager.findOf_accountId(table.createPlayer);
					eventAttrib.addEventParam(roleEntity.getStarNO());
					eventAttrib.addEventParam(chipNum);
					eventAttrib.addEventParam(eventAttrib.eventTime);
					eventTriggerManager.triggerEvent(eventAttrib);
				}else{
					//其它玩家购买,检查是否已有未处理的购买订单
					/*boolean bResult = orderManager.isUnTransBuyRecord(accountId, table.recordId);
					if(bResult == true){
						err = DzpkerDefine.DZPKER_ERROR_ORDER;
						break;
					}*/
					orderManager.loadOrCreate(accountId, table.recordId, table.createPlayer, chipNum);
					
					//设置红点
					hotPromptManager.updateHotPrompt(table.createPlayer, HotPromptManager.HOT_KEY_ORDER);
					hotPromptManager.hotPromptNotity(table.createPlayer);
				}
				
				err = 0;
				break;
			}			
		}finally{
			logicManager.unlock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errCodeManager.Error2Desc(err), null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_table_bt(Long accountId, int tableId, int bt, int btVal) {
		int err = DzpkerDefine.DZPKER_ERROR_UNEXIST;
		logicManager.lock();
		try{
			while(true){
				if(bt < GameDefine.ACT_TYPE_DROP || bt > GameDefine.ACT_TYPE_ALLIN || btVal < 0){
					err = DzpkerDefine.DZPKER_ERROR_PARAM;
					break;
				}
				
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					break;
				}
				
				if(table.gameState != GameDefine.STATE_TABLE_BET_BT_1
						&& table.gameState != GameDefine.STATE_TABLE_BET_BT_2
						&& table.gameState != GameDefine.STATE_TABLE_BET_BT_3
						&& table.gameState != GameDefine.STATE_TABLE_BET_BT_4){
					//游戏状态不是表态状态
					err = DzpkerDefine.DZPKER_ERROR_TABLE_BT;
					break;
				}
				
				SeatAttrib seat = table.getPlayerSeat(accountId);
				if(null == seat){
					err = DzpkerDefine.DZPKER_ERROR_TABLE_BT;
					break;
				}
				if(seat.btState != GameDefine.BT_STATE_WAIT){
					err = DzpkerDefine.DZPKER_ERROR_TABLE_BT;
					break;
				}				
				
				if(table.btIndex != seat.seatIndex){
					err = DzpkerDefine.DZPKER_ERROR_TABLE_BT;
					break;
				}
				DzpkerTableRecordEntity recordEntity = this.recordManager.loadTableRecord(table.recordId);
				Map<Long, StatisticsAttrib> statisticsMap = recordEntity.getStatisticsMap();
				StatisticsAttrib statisticsAttrib = statisticsMap.get(accountId);
				statisticsAttrib.totalBtNum++;
				
				if(bt == GameDefine.ACT_TYPE_DROP){
					seat.btResult = bt;
					statisticsAttrib.dropCardNum++;
				}else{
					long myMoney = table.once_betpool.getBetMoneyOfAccountId(accountId);
					long maxMoney = table.once_betpool.getMaxBetMoney();
					if(bt == GameDefine.ACT_TYPE_PASS){
						if(myMoney != maxMoney){
							err = DzpkerDefine.DZPKER_ERROR_TABLE_BT_MONEY;
							break;
						}
						seat.btResult = bt;
					}else if(bt == GameDefine.ACT_TYPE_SAME){
						if(btVal != (maxMoney-myMoney)){
							err = DzpkerDefine.DZPKER_ERROR_TABLE_BT_MONEY;
							break;
						}
						if(btVal > statisticsAttrib.currMoney){
							err = DzpkerDefine.DZPKER_ERROR_TABLE_BT_MONEY;
							break;
						}
						if(btVal == statisticsAttrib.currMoney){
							statisticsAttrib.fullBtNum++;
							seat.btResult = GameDefine.ACT_TYPE_ALLIN;
						}else{
							seat.btResult = bt;
						}
						table.once_betpool.addBetMoney(accountId, btVal);
						table.un_all_in_betpool.addBetMoney(accountId, btVal);
						statisticsAttrib.betTotalMoney  += btVal;
						statisticsAttrib.currMoney -= btVal;
						if(btVal > statisticsAttrib.maxBetMoney){
							statisticsAttrib.maxBetMoney = btVal;
						}
					}else if(bt == GameDefine.ACT_TYPE_ADD){
						if((btVal+myMoney) < (maxMoney*2)){
							err = DzpkerDefine.DZPKER_ERROR_TABLE_BT_MONEY;
							break;
						}
						if(btVal > statisticsAttrib.currMoney){
							err = DzpkerDefine.DZPKER_ERROR_TABLE_BT_MONEY;
							break;
						}
						if(btVal == statisticsAttrib.currMoney){
							seat.btResult = GameDefine.ACT_TYPE_ALLIN;
							statisticsAttrib.fullBtNum++;
						}else{
							seat.btResult = bt;
							statisticsAttrib.addChipNum++;
						}
						
						table.once_betpool.addBetMoney(accountId, btVal);
						table.un_all_in_betpool.addBetMoney(accountId, btVal);
						statisticsAttrib.betTotalMoney  += btVal;
						statisticsAttrib.currMoney -= btVal;
						if(btVal > statisticsAttrib.maxBetMoney){
							statisticsAttrib.maxBetMoney = btVal;
						}
					}else if(bt == GameDefine.ACT_TYPE_ALLIN){
						btVal = (int) statisticsAttrib.currMoney;
						table.once_betpool.addBetMoney(accountId, statisticsAttrib.currMoney);
						table.un_all_in_betpool.addBetMoney(accountId, statisticsAttrib.currMoney);
						statisticsAttrib.betTotalMoney  += statisticsAttrib.currMoney;
						statisticsAttrib.currMoney = 0;
						if(statisticsAttrib.currMoney > statisticsAttrib.maxBetMoney){
							statisticsAttrib.maxBetMoney = statisticsAttrib.currMoney;
						}
						statisticsAttrib.fullBtNum++;
						seat.btResult = bt;
					}					
					if(btVal > 0){
						RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountId);
						EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_BET);
						eventAttrib.addEventParam(roleEntity.getStarNO());
						eventAttrib.addEventParam(table.tableId);
						roleEntity = roleEntityManager.findOf_accountId(table.createPlayer);
						eventAttrib.addEventParam(roleEntity.getStarNO());
						eventAttrib.addEventParam(table.gameNum);
						eventAttrib.addEventParam(btVal);
						eventAttrib.addEventParam(eventAttrib.eventTime);
						eventTriggerManager.triggerEvent(eventAttrib);
					}
				}
				seat.btState = GameDefine.BT_STATE_BT;
				
				statisticsMap.put(accountId, statisticsAttrib);
				recordEntity.setStatisticsMap(statisticsMap);
				
				GameDataAttrib gameDataAttrib = this.gameDataManager.getAccountGameData(accountId);
				gameDataAttrib.btKickNum = 0;
				err = 0;
				break;
			}
		}finally{
			logicManager.unlock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errCodeManager.Error2Desc(err), null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_query_buychip_list(Long accountId) {
		DzpkerOrderVo retVo = new DzpkerOrderVo();
		logicManager.lock();
		try{
			List<Long> delIds = new ArrayList<>();
			List<Long> orderIds = orderManager.queryQuestBuyOrderList(accountId);
			for(Long orderId : orderIds){
				DzpkerOrderEntity orderEntity = orderManager.load(orderId);
				if(null == orderEntity){
					delIds.add(orderId);
					continue;
				}
				if(orderEntity.getState() != 0){
					continue;
				}
				
				DzpkerTableRecordEntity recordEntity = recordManager.loadTableRecord(orderEntity.getTableRecordId());
				if(null == recordEntity){
					delIds.add(orderId);
					continue;
				}
				TableAttrib table = logicManager.getTable(recordEntity.getTableId());
				if(null == table){
					delIds.add(orderId);
					continue;
				}
				RoleEntity roleEntity = roleEntityManager.findOf_accountId(orderEntity.getAccountId());
				if(null == roleEntity){
					delIds.add(orderId);
					continue;
				}
				retVo.addItem(orderEntity.getRecordId(), roleEntity.getStarNO(),
						roleEntity.getNick(), roleEntity.getHeadImg(), orderEntity.getChipNum());
			}
			
			for(Long orderId : delIds){
				orderManager.remove(orderId);
			}
		}finally{
			logicManager.unlock();
		}		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object dzpker_trans_buychip_item(Long accountId, String itemId, int bt) {
		int err = DzpkerDefine.DZPKER_ERROR_ORDER_ITEM;
		logicManager.lock();
		try{
			while(true){
				if(bt < 1 || bt > 2){
					err = DzpkerDefine.DZPKER_ERROR_PARAM;
					break;
				}
				
				long nId = Long.parseLong(itemId);
				DzpkerOrderEntity orderEntity = orderManager.load(nId);
				if(null == orderEntity){
					break;
				}
				if(orderEntity.getState() != 0){
					break;
				}
				DzpkerTableRecordEntity recordEntity = recordManager.loadTableRecord(orderEntity.getTableRecordId());
				if(null == recordEntity){
					orderManager.remove(nId);
					break;
				}
				TableAttrib table = logicManager.getTable(recordEntity.getTableId());
				if(null == table || table.recordId != orderEntity.getTableRecordId()){
					//桌子已解散
					orderManager.remove(nId);
					break;
				}
				if(bt == 1){
					//同意
					orderEntity.setState(1);
					//增加到玩家筹码中
					Map<Long, StatisticsAttrib> statisticsMap = recordEntity.getStatisticsMap();
					StatisticsAttrib statisticsAttrib = statisticsMap.get(orderEntity.getAccountId());
					if(null == statisticsAttrib){
						statisticsAttrib = new StatisticsAttrib();
					}
					statisticsAttrib.buyTotalMoney += orderEntity.getChipNum();
					

					String msgStr = "";
					SeatAttrib seat = table.getPlayerSeat(orderEntity.getAccountId());
					if(null == seat || seat.bGamed == false){
						//不在座位上或未参与本局游戏,直接加到当前筹码中
						statisticsAttrib.currMoney += orderEntity.getChipNum();
						
						msgStr = "您申请的[" + orderEntity.getChipNum() + "]积分已同意,当前已到帐";
						table.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_SEAT_STATE_NOTIFY);
					}else{
						statisticsAttrib.gameBuyChip += orderEntity.getChipNum();
						msgStr = "您申请的[" + orderEntity.getChipNum() + "]积分已同意,下局游戏开始后到帐";
					}
					//通知玩家筹码购买成功
					@SuppressWarnings("rawtypes")
					Request pushMsg = Request.valueOf(DzpkerDefine.DZPKER_COMMAND_BUY_CHIP_SUCESS_NOTIFY,
							Result.valueOfSuccess(msgStr));
					MessagePushQueueUtils.getPushQueue(sessionManager).push2(orderEntity.getAccountId(), pushMsg);					
					
					statisticsMap.put(orderEntity.getAccountId(), statisticsAttrib);
					recordEntity.setStatisticsMap(statisticsMap);
										
					RoleEntity roleEntity = roleEntityManager.findOf_accountId(orderEntity.getAccountId());
					EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_BUY_CHIP);
					eventAttrib.addEventParam(roleEntity.getStarNO());
					eventAttrib.addEventParam(table.tableId);
					roleEntity = roleEntityManager.findOf_accountId(table.createPlayer);
					eventAttrib.addEventParam(roleEntity.getStarNO());
					int chipNum = (int) orderEntity.getChipNum();
					eventAttrib.addEventParam(chipNum);
					eventAttrib.addEventParam(eventAttrib.eventTime);
					eventTriggerManager.triggerEvent(eventAttrib);
				}else{
					//通知玩家筹码购买成功
					@SuppressWarnings("rawtypes")
					Request pushMsg = Request.valueOf(DzpkerDefine.DZPKER_COMMAND_BUY_CHIP_SUCESS_NOTIFY,
							Result.valueOfSuccess("您申请的[" + orderEntity.getChipNum() + "]积分,房主已拒绝"));
					MessagePushQueueUtils.getPushQueue(sessionManager).push2(orderEntity.getAccountId(), pushMsg);
					
					//拒绝
					orderManager.remove(nId);
				}
				//设置红点
				hotPromptManager.updateHotPrompt(table.createPlayer, HotPromptManager.HOT_KEY_ORDER);
				hotPromptManager.hotPromptNotity(table.createPlayer);
				
				err = 0;
				break;
			}
		}finally{
			logicManager.unlock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errCodeManager.Error2Desc(err), null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_straddle_bt(Long accountId, int tableId, int seatIndex) {
		int err = DzpkerDefine.DZPKER_ERROR_UNEXIST;
		logicManager.lock();
		try{
			while(true){
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					break;
				}
				SeatAttrib mySeat = table.getSeat(seatIndex);
				if(null == mySeat
						|| mySeat.accountId != accountId.longValue()
						|| mySeat.straddle <= 0){
					err = DzpkerDefine.DZPKER_ERROR_TABLE_BT;
					break;
				}
				
				if(table.gameState != GameDefine.STATE_TABLE_BETBLIND){
					err = DzpkerDefine.DZPKER_ERROR_TABLE_BT;
					break;
				}
				
				long maxMoney = table.once_betpool.getMaxBetMoney();
				DzpkerTableRecordEntity recordEntity = this.recordManager.loadTableRecord(table.recordId);
				Map<Long, StatisticsAttrib> statisticsMap = recordEntity.getStatisticsMap();
				StatisticsAttrib statisticsAttrib = statisticsMap.get(mySeat.accountId);
				if(null == statisticsAttrib){
					err = DzpkerDefine.DZPKER_ERROR_TABLE_BT;
					break;
				}
				long tmpBetMoney = maxMoney*2;
				if(statisticsAttrib.currMoney < tmpBetMoney){
					err = DzpkerDefine.DZPKER_ERROR_TABLE_BT;
					break;
				}
				//添加到下注池中
				table.once_betpool.addBetMoney(accountId, tmpBetMoney);
				table.un_all_in_betpool.addBetMoney(accountId, tmpBetMoney);
				//添加到玩家个人下注信息
				statisticsAttrib.betTotalMoney  += tmpBetMoney;
				statisticsAttrib.currMoney -= tmpBetMoney;
				
				if(tmpBetMoney > statisticsAttrib.maxBetMoney){
					statisticsAttrib.maxBetMoney = tmpBetMoney;
				}
				if(statisticsAttrib.currMoney == 0){
					mySeat.btState = GameDefine.BT_STATE_BT;
					mySeat.btResult = GameDefine.ACT_TYPE_ALLIN;
				}
				
				statisticsMap.put(mySeat.accountId, statisticsAttrib);
				
				//设置下了闭眼盲注标志
				mySeat.straddleFlag = 1;
				mySeat.straddle = 0;
				//设置下一位闭眼盲注表态的玩家
				SeatAttrib nextSeat = table.getNextGamedSeat(mySeat.seatIndex);
				if(null != nextSeat && nextSeat.seatIndex != table.bankerIndex){
					statisticsAttrib = statisticsMap.get(nextSeat.accountId);
					if(null != statisticsAttrib){
						maxMoney = table.once_betpool.getMaxBetMoney();
						tmpBetMoney = maxMoney*2;
						if(statisticsAttrib.currMoney >= tmpBetMoney){
							nextSeat.straddle = 1;
						}
					}
					table.btIndex = nextSeat.seatIndex;
				}
				recordEntity.setStatisticsMap(statisticsMap);
				
				//通知客户端闭眼盲注表态通知
				table.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_STRADDL_BT_NOTIFY);
				
				RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountId);
				EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_BET);
				eventAttrib.addEventParam(roleEntity.getStarNO());
				eventAttrib.addEventParam(table.tableId);
				roleEntity = roleEntityManager.findOf_accountId(table.createPlayer);
				eventAttrib.addEventParam(roleEntity.getStarNO());
				eventAttrib.addEventParam(table.gameNum);
				int chipNum = (int)tmpBetMoney;
				eventAttrib.addEventParam(chipNum);
				eventAttrib.addEventParam(eventAttrib.eventTime);
				eventTriggerManager.triggerEvent(eventAttrib);
				
				err = 0;
				break;
			}
		}finally{
			logicManager.unlock();
		}
		if(err < 0){
			return Result.valueOfError(err, errCodeManager.Error2Desc(err), null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_get_fighted_table_list(Long accountId) {
		JoinedTableListVo retVo = new JoinedTableListVo();
		logicManager.lock();
		try{
			GameDataAttrib gameDataAttrib = this.gameDataManager.getAccountGameData(accountId);
			Set<Long> recordIds = gameDataAttrib.joinedTableIdMap.keySet();
			List<Long> delIds = new ArrayList<>();
			for(Long recordId : recordIds){
				DzpkerTableRecordEntity tableRecordEntity = this.recordManager.loadTableRecord(recordId);
				if(null == tableRecordEntity){
					delIds.add(recordId);
					continue;
				}
				
				TableAttrib tableObj = logicManager.getTable(tableRecordEntity.getTableId());
				if(null == tableObj || tableObj.recordId != recordId){
					delIds.add(recordId);
					continue;
				}
				
				long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
				long vaildTime = tableObj.vaildTime - currTime;
				if(vaildTime <= 1*60*1000){
					//小于1分钟的都不算
					delIds.add(recordId);
					continue;
				}
				
				RoleEntity roleEntity = roleEntityManager.findOf_accountId(tableObj.createPlayer);
				if(null == roleEntity){
					delIds.add(recordId);
					continue;
				}
				int currPlayer = tableObj.getSeatPlayerNum();
				retVo.addItem(tableObj.tableId, tableObj.createPlayer, 
						roleEntity.getHeadImg(), roleEntity.getNick(), tableObj.smallBlind, tableObj.bigBlind, 
						tableObj.joinChip, vaildTime, currPlayer, tableObj.seats.size());
			}
			for(Long delId : delIds){
				gameDataAttrib.joinedTableIdMap.remove(delId);
			}
			delIds = null;
		}finally{
			logicManager.unlock();
		}
		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object dzpker_table_prev_fight(Long accountId, int tableId) {
		int err = 0;
		TablePrevFightRecordVo retVo = new TablePrevFightRecordVo();
		logicManager.lock();
		try{
			while(true){
				TableAttrib tableObj = logicManager.getTable(tableId);
				if(null == tableObj){
					break;
				}
				DzpkerTableRecordEntity tableRecordEntity = recordManager.loadTableRecord(tableObj.recordId);
				if(null == tableRecordEntity){
					//没有上局记录
					break;
				}
				Map<Integer, WinScoreAttrib> detailedScoreMap = tableRecordEntity.getDetailedScoreMap();
				int prev = tableObj.gameNum-1;
				WinScoreAttrib winScoreAttrib = detailedScoreMap.get(prev);
				if(null == winScoreAttrib){
					//没有上局记录
					break;
				}
				Set<Long> keys = winScoreAttrib.playerScoresMap.keySet();
				for(Long theAccountId : keys){
					RoleEntity roleEntity = roleEntityManager.findOf_accountId(theAccountId);
					if(null == roleEntity){
						continue;
					}
					WinScoreAttribItem winScoreAttribInner = winScoreAttrib.getPlayerScore(theAccountId);
					retVo.addItem(winScoreAttribInner.playerHandCards, roleEntity.getStarNO(), 
							roleEntity.getHeadImg(), roleEntity.getNick(), winScoreAttribInner.score, winScoreAttribInner.showCardState);
				}
								
				retVo.tableCards.addAll(winScoreAttrib.tableCards);
				
				err = 0;
				break;
			}
		}finally{
			logicManager.unlock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errCodeManager.Error2Desc(err), null);
		}
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object dzpker_table_player_list(Long accountId, int tableId) {
		int err = DzpkerDefine.DZPKER_ERROR_UNEXIST;
		TablePlayerListVo retVo = new TablePlayerListVo(); 
		logicManager.lock();
		try{
			while(true){
				TableAttrib tableObj = logicManager.getTable(tableId);
				if(null == tableObj){
					break;
				}
				Map<Long, StatisticsAttrib> statisticsMap = null; 
				DzpkerTableRecordEntity tableRecordEntity = recordManager.loadTableRecord(tableObj.recordId);
				if(null != tableRecordEntity){
					statisticsMap = tableRecordEntity.getStatisticsMap();
					retVo.insuranceScore = String.valueOf(tableRecordEntity.getInsuranceScore());
				}
				
				Set<Long> playerIds = tableObj.tablePlayers.keySet();
				for(Long playerId : playerIds){
					long gameNum = 0;
					long currMoney = 0;
					long winScore = 0;
					if(null != statisticsMap){
						StatisticsAttrib statisticsAttrib = statisticsMap.get(playerId);
						if(null != statisticsAttrib){
							gameNum = statisticsAttrib.gameTotalNum;
							currMoney = statisticsAttrib.currMoney;
							winScore = (currMoney+statisticsAttrib.gameBuyChip) - statisticsAttrib.buyTotalMoney;
						}
					}
					String starNO = "";
					String headImg = "";
					String nick = "";
					RoleEntity roleEntity = roleEntityManager.findOf_accountId(playerId);
					if(null != roleEntity){
						starNO = roleEntity.getStarNO();
						headImg = roleEntity.getHeadImg();
						nick = roleEntity.getNick();
					}
					retVo.addItem(starNO, headImg, nick, gameNum, currMoney, winScore);
				}
				
				err = 0;
				break;
			}
		}finally{
			logicManager.unlock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errCodeManager.Error2Desc(err), null);
		}
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object dzpker_table_player_once_winscore(Long accountId, int tableId) {
		int err = DzpkerDefine.DZPKER_ERROR_UNEXIST;
		TablePlayerWinScoreDetailsVo retVo = new TablePlayerWinScoreDetailsVo();
		logicManager.lock();
		try{
			while(true){
				TableAttrib tableObj = logicManager.getTable(tableId);
				if(null == tableObj){
					break;
				}
				DzpkerTableRecordEntity tableRecordEntity = recordManager.loadTableRecord(tableObj.recordId);
				if(null == tableRecordEntity){
					break;
				}
				
				Map<Integer, WinScoreAttrib> detailedScoreMap = tableRecordEntity.getDetailedScoreMap();
				Map<Long, StatisticsAttrib> statisticsMap = tableRecordEntity.getStatisticsMap();
				StatisticsAttrib statisticsAttrib = statisticsMap.get(accountId);
				if(null != statisticsAttrib){
					retVo.buyTotalMoney = String.valueOf(statisticsAttrib.buyTotalMoney);
					retVo.currMoney = String.valueOf(statisticsAttrib.currMoney);
				}
				
				Collection<WinScoreAttrib> scoreAttribList = detailedScoreMap.values();
				for(WinScoreAttrib ScoreAttrib : scoreAttribList){
					if(ScoreAttrib.playerScoresMap.containsKey(accountId)){
						WinScoreAttribItem winScoreAttribInner = ScoreAttrib.getPlayerScore(accountId);
						retVo.addItem(ScoreAttrib.gameNum, winScoreAttribInner.score);
					}
				}
				
				err = 0;
				break;
			}
		}finally{
			logicManager.unlock();
		}
		
		if(err < 0){
			return Result.valueOfError(err, errCodeManager.Error2Desc(err), null);
		}
		retVo.sort();
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object dzpker_table_buy_insurance(Long accountId, int tableId, int bt, int[] buyCards, int buyMoney,
			int payMoney) {
		int err = DzpkerDefine.DZPKER_ERROR_UNEXIST;
		logicManager.lock();
		try{
			while(true){
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					break;
				}
				if(table.gameState != GameDefine.STATE_TABLE_BUY_INSURANCE){
					break;
				}
				long n_account = Long.valueOf(table.insuranceStateAttrib.accountId);
				if(n_account != accountId.longValue()){
					break;
				}
				int tmpTotalMoney = 0;
				
				if(bt == 0){
					//不买
					if(table.insuranceStateAttrib.round == 2){
						int roundIndex = table.insuranceStateAttrib.round-1;
						if(table.insuranceStateAttrib.buyInsuranceRound[roundIndex].accountId == accountId.longValue()){
							//第-轮买了第二轮不买需要计算强制保险
							buyMoney = (int) table.insuranceStateAttrib.buyInsuranceRound[roundIndex].buyMoney;
							int outs = table.insuranceStateAttrib.winCardList.size();
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
							if(nRate > 0){
								exMoney = (int) (buyMoney/nRate);
							}
							tmpTotalMoney = exMoney;
							
							table.insuranceStateAttrib.buyInsuranceRound[roundIndex].accountId = accountId;
							table.insuranceStateAttrib.buyInsuranceRound[roundIndex].buyMoney = 0;
							table.insuranceStateAttrib.buyInsuranceRound[roundIndex].payMoney = 0;
							table.insuranceStateAttrib.buyInsuranceRound[roundIndex].buyCards.clear();
							table.insuranceStateAttrib.buyInsuranceRound[roundIndex].exBuyMoney = exMoney;
						}
					}
				}else{
					//选择购买保险,计算额外强制保险
					int exMoney = 0;
					int outs = table.insuranceStateAttrib.winCardList.size() - buyCards.length;
					if(outs > 0){
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
						if(nRate > 0.0f){
							exMoney = (int) (buyMoney/nRate);
						}
					}
					int roundIndex = table.insuranceStateAttrib.round-1;
					table.insuranceStateAttrib.buyInsuranceRound[roundIndex].accountId = accountId;
					table.insuranceStateAttrib.buyInsuranceRound[roundIndex].buyMoney = buyMoney;
					table.insuranceStateAttrib.buyInsuranceRound[roundIndex].payMoney = payMoney;
					for(int buyCard : buyCards){
						table.insuranceStateAttrib.buyInsuranceRound[roundIndex].buyCards.add(buyCard);
					}
					table.insuranceStateAttrib.buyInsuranceRound[roundIndex].exBuyMoney = exMoney;
					
					tmpTotalMoney = exMoney + buyMoney;
				}
				
				if(tmpTotalMoney > 0){
					RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountId);
					EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_TABLE_BUY_INSURANCE);
					eventAttrib.addEventParam(roleEntity.getStarNO());
					eventAttrib.addEventParam(table.tableId);
					roleEntity = roleEntityManager.findOf_accountId(table.createPlayer);
					eventAttrib.addEventParam(roleEntity.getStarNO());
					eventAttrib.addEventParam(table.gameNum);
					eventAttrib.addEventParam(tmpTotalMoney);
					eventAttrib.addEventParam(eventAttrib.eventTime);
					eventTriggerManager.triggerEvent(eventAttrib);
				}
				
				table.gameState = table.nextGameState;
				table.bActExec = false;
				err = 0;
				break;
			}
			
		}finally{
			logicManager.unlock();
		}
		if(err < 0){
			return Result.valueOfError(err, errCodeManager.Error2Desc(err), null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_table_get_caree_info(Long accountId) {
		CareerVo retVo = new CareerVo();
		DzpkerPlayerRecordEntity playerRecordEntity = recordManager.loadOrCreatePlayerRecord(accountId);
		List<Long> recordIds = new ArrayList<>();
		recordIds.addAll(playerRecordEntity.getRecordTableMap().keySet());
		for(Long recordId : recordIds){
			DzpkerTableRecordEntity tableRecordEntity = recordManager.loadTableRecord(recordId);
			Map<Long, StatisticsAttrib> statisticsMap = tableRecordEntity.getStatisticsMap();
			float rate = 0;
			StatisticsAttrib statisticsAttrib = statisticsMap.get(accountId);
			if(null != statisticsAttrib){
				if(statisticsAttrib.gameTotalNum > 0){
					rate = ((statisticsAttrib.winNum*1.0f/statisticsAttrib.gameTotalNum));
					BigDecimal b = new BigDecimal(String.format("%f", rate));
					DecimalFormat d1 = new DecimalFormat("0.00");
					retVo.winRate = d1.format(b);
				}
				if(statisticsAttrib.joinTableNum > 0){
					rate = ((statisticsAttrib.seatDownNum*1.0f/statisticsAttrib.joinTableNum));
					BigDecimal b = new BigDecimal(String.format("%2f", rate));
					DecimalFormat d1 = new DecimalFormat("0.00");
					retVo.seatDown = d1.format(b);
				}
				if(statisticsAttrib.dropWinNum > 0){
					rate = ((statisticsAttrib.showCardNum*1.0f/statisticsAttrib.dropWinNum));
					BigDecimal b = new BigDecimal(String.format("%2f", rate));
					DecimalFormat d1 = new DecimalFormat("0.00");
					retVo.showCard = d1.format(b);
				}
				if(statisticsAttrib.totalBtNum > 0){
					rate = ((statisticsAttrib.addChipNum*1.0f/statisticsAttrib.totalBtNum));
					BigDecimal b = new BigDecimal(String.format("%2f", rate));
					DecimalFormat d1 = new DecimalFormat("0.00");
					retVo.addChip = d1.format(b);
				}
				if(statisticsAttrib.totalBtNum > 0){
					rate = ((statisticsAttrib.dropCardNum*1.0f/statisticsAttrib.totalBtNum));
					BigDecimal b = new BigDecimal(String.format("%2f", rate));
					DecimalFormat d1 = new DecimalFormat("0.00");
					retVo.dropCards = d1.format(b);
				}
				if(statisticsAttrib.totalBtNum > 0){
					rate = ((statisticsAttrib.fullBtNum*1.0f/statisticsAttrib.totalBtNum));
					BigDecimal b = new BigDecimal(String.format("%2f", rate));
					DecimalFormat d1 = new DecimalFormat("0.00");
					retVo.fullBet = d1.format(b);
				}
				long winMoney = statisticsAttrib.currMoney+statisticsAttrib.gameBuyChip - statisticsAttrib.buyTotalMoney;
				String headImg = "";
				String nick = "";
				RoleEntity roleEntity = roleEntityManager.findOf_accountId(tableRecordEntity.getCreatePlayer());
				if(null != roleEntity){
					headImg = roleEntity.getHeadImg();
					nick = roleEntity.getNick();
				}
				
				retVo.addHistoryItem(recordId, accountId, headImg, nick, tableRecordEntity.getTableName(), 
						tableRecordEntity.getCreateTime(), winMoney);
			}
		}
		retVo.sort();
		recordIds = null;
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object dzpker_table_show_card_bt(Long accountId, int tableId) {
		
		logicManager.lock();
		try{
			while(true){
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					break;
				}
				if(table.gameState != GameDefine.STATE_TABLE_OVER_ONCE
						|| table.showCardSeatIndex < 0){
					break;
				}
				SeatAttrib seat = table.getSeat(table.showCardSeatIndex);
				if(null == seat || 
						seat.accountId != accountId.longValue()
						|| seat.bGamed == false){
					break;
				}
								
				StatisticsAttrib statisticsAttrib = recordManager.getStatisticsAttrib(table.recordId, accountId);
				if(null != statisticsAttrib){
					statisticsAttrib.showCardNum++;
					recordManager.setStatisticsAttrib(table.recordId, accountId, statisticsAttrib);
				}
				
				DzpkerTableRecordEntity tableRecordEntity = recordManager.loadTableRecord(table.recordId);
				if(null != tableRecordEntity){
					Map<Integer, WinScoreAttrib> detailedScoreMap = tableRecordEntity.getDetailedScoreMap();
					WinScoreAttrib winScoreAttrib = detailedScoreMap.get(table.gameNum-1);
					if(null != winScoreAttrib){
						if(winScoreAttrib.playerScoresMap.containsKey(accountId)){
							WinScoreAttribItem winScoreAttribInner = winScoreAttrib.getPlayerScore(accountId);
							winScoreAttribInner.showCardState = 1;
							winScoreAttrib.setPlayerScore(accountId, winScoreAttribInner);
							detailedScoreMap.put(table.gameNum-1, winScoreAttrib);
							tableRecordEntity.setDetailedScoreMap(detailedScoreMap);
						}
					}
				}
				
				//通知所有玩家亮牌
				@SuppressWarnings("rawtypes")
				Request pushMsg = Request.valueOf(DzpkerDefine.DZPKER_COMMAND_SHOW_CARD_NOTIFY,
						Result.valueOfSuccess());
				MessagePushQueueUtils.getPushQueue(sessionManager).push(table.tablePlayers.keySet(), pushMsg);
				break;
			}
			
		}finally{
			logicManager.unlock();
		}
		
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_table_get_table_info(Long accountId, int tableId) {
		int err = DzpkerDefine.DZPKER_ERROR_UNEXIST;
		TableVo retVo = null;
		logicManager.lock();
		try{
			while(true){
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					break;
				}
				retVo = logicManager.table2TableVo(table);
				err = 0;
				break;
			}
		}finally{
			logicManager.unlock();
		}
		if(err < 0){
			return Result.valueOfError(err, errCodeManager.Error2Desc(err), null);
		}
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object dzpker_table_get_all_win_score_info(Long accountId, long recordId) {
		TablePlayerListVo retVo = new TablePlayerListVo();
		
		DzpkerTableRecordEntity tableRecordEntity = recordManager.loadTableRecord(recordId);
		if(null != tableRecordEntity){
			Map<Long, StatisticsAttrib> statisticsMap = tableRecordEntity.getStatisticsMap();
			Set<Long> keys = statisticsMap.keySet();
			for(Long theAccountId : keys){
				String starNO = "";
				String headImg = "";
				String nick = "";
				RoleEntity roleEntity = roleEntityManager.findOf_accountId(theAccountId);
				if(null == roleEntity){
					continue;
				}
				
				StatisticsAttrib statisticsAttrib = statisticsMap.get(theAccountId);
				
				starNO = roleEntity.getStarNO();
				headImg = roleEntity.getHeadImg();
				nick = roleEntity.getNick();
				
				long winMoney = statisticsAttrib.currMoney+statisticsAttrib.gameBuyChip - statisticsAttrib.buyTotalMoney;
				retVo.addItem(starNO, headImg, nick, statisticsAttrib.gameTotalNum, statisticsAttrib.currMoney, winMoney);
			}
		}
		
		return Result.valueOfSuccess(retVo);
	}

}
