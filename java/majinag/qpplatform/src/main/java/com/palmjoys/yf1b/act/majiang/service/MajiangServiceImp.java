package com.palmjoys.yf1b.act.majiang.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.corps.entity.CorpsEntity;
import com.palmjoys.yf1b.act.corps.manager.CorpsManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;
import com.palmjoys.yf1b.act.framework.utils.MessagePushQueueUtils;
import com.palmjoys.yf1b.act.majiang.manager.GameDataManager;
import com.palmjoys.yf1b.act.majiang.manager.GameLogicManager;
import com.palmjoys.yf1b.act.majiang.manager.MajiangCfgManager;
import com.palmjoys.yf1b.act.majiang.manager.PlayerGamedRecordManager;
import com.palmjoys.yf1b.act.majiang.model.MajiangDefine;
import com.palmjoys.yf1b.act.majiang.model.PlayerCreatedRuleAttrib;
import com.palmjoys.yf1b.act.majiang.model.CardAttrib;
import com.palmjoys.yf1b.act.majiang.model.GameDataAttrib;
import com.palmjoys.yf1b.act.majiang.model.GameDefine;
import com.palmjoys.yf1b.act.majiang.model.RuleCfgVo;
import com.palmjoys.yf1b.act.majiang.model.RuleCfgVo.RuleContent;
import com.palmjoys.yf1b.act.majiang.model.RuleCfgVo.RuleContentItem;
import com.palmjoys.yf1b.act.majiang.model.RuleCfgVo.RuleContentItemAttrib;
import com.palmjoys.yf1b.act.majiang.model.SeatAttrib;
import com.palmjoys.yf1b.act.majiang.model.SeatVo;
import com.palmjoys.yf1b.act.majiang.model.TableAttrib;
import com.palmjoys.yf1b.act.majiang.model.TableBaseVo;
import com.palmjoys.yf1b.act.majiang.model.TablePasswordStateVo;
import com.palmjoys.yf1b.act.majiang.model.TableVo;
import com.palmjoys.yf1b.act.majiang.model.config.RuleCfgAttrib;
import com.palmjoys.yf1b.act.majiang.model.config.RuleItemContent;
import com.palmjoys.yf1b.act.majiang.model.notify.seat.Notify_Seat_Vo_BaoJiao;
import com.palmjoys.yf1b.act.majiang.model.notify.seat.Notify_Seat_Vo_PiaoPai;
import com.palmjoys.yf1b.act.majiang.resource.RoomItemConfig;
import com.palmjoys.yf1b.act.majiang.resource.RoomRuleConfig;
import com.palmjoys.yf1b.act.wallet.entity.WalletEntity;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

@Service
public class MajiangServiceImp implements MajiangService{
	@Autowired
	private GameLogicManager logicManager;
	@Autowired
	private MajiangCfgManager cfgManager;
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private GameDataManager gameDataManager;
	@Autowired
	private CorpsManager corpsManager;
	@Autowired
	private PlayerGamedRecordManager playerGamedRecordManager;
	@Autowired
	private WalletManager walletManager;
	
	@Override
	public Object majiang_get_rulecfg(Long accountId, String corpsId) {
		if(null == corpsId || corpsId.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		Map<Integer, PlayerCreatedRuleAttrib> createRuleMap = playerGamedRecordManager.getPlayerCreateRuleList(accountId);
		List<RuleCfgVo> retVo = new ArrayList<RuleCfgVo>();
		boolean bChanage = false;
		
		List<RoomItemConfig> roomItemCfgs = cfgManager.getAllRoomCfgs();
		for(RoomItemConfig roomItemCfg : roomItemCfgs){
			RuleCfgVo vo = new RuleCfgVo();
			vo.itemId = roomItemCfg.getId();
			vo.itemName = roomItemCfg.getItemName();
			
			PlayerCreatedRuleAttrib createRuleAttrib = createRuleMap.get(vo.itemId);
			if(null == createRuleAttrib){
				createRuleAttrib = new PlayerCreatedRuleAttrib();
				createRuleAttrib.ruleItem = vo.itemId;
				createRuleMap.put(vo.itemId, createRuleAttrib);
				bChanage = true;
			}
			
			RuleCfgAttrib[] ruleCfgAttribs = roomItemCfg.getRules();
			for(RuleCfgAttrib ruleCfgAttrib : ruleCfgAttribs){
				if(ruleCfgAttrib.show == 0){
					//不显示的不传
					continue;
				}
				RuleContent ruleContent = vo.newRuleContent();
				ruleContent.ruleName = ruleCfgAttrib.name;
				ruleContent.show = ruleCfgAttrib.show;
				
				boolean bAdd = true;
				for(RuleItemContent ruleItemContent : ruleCfgAttrib.content){
					if(null != corpsId && corpsId.isEmpty() == false
							&& corpsId.equalsIgnoreCase("0") == false){
						//是帮会桌子,去除房卡支付选项
						for(int ruleId : ruleItemContent.item){
							if(ruleId == 7201){
								bAdd = false;
								break;
							}
						}
						if(bAdd == false){
							break;
						}
					}						
						
					RuleContentItem ruleContentItem = vo.newRuleContentItem();
					ruleContentItem.ridio = ruleItemContent.ridio;
					for(int ruleId : ruleItemContent.item){
						RuleContentItemAttrib ruleContentItemAttrib = vo.newRuleContentItemAttrib();
						ruleContentItemAttrib.ruleId = ruleId;
						RoomRuleConfig roomRuleConfig = cfgManager.getRuleItemCfg(ruleId);
						if(null != roomRuleConfig){
							ruleContentItemAttrib.ruleName = roomRuleConfig.getItemName();
						}
						ruleContentItemAttrib.state = 0;
						if(null != createRuleAttrib && createRuleAttrib.ruleList.contains(ruleId)){
							ruleContentItemAttrib.state = 1;
						}						
						ruleContentItem.ruleContentItemAttribs.add(ruleContentItemAttrib);
					}
					ruleContent.ruleContentItems.add(ruleContentItem);
				}
				if(bAdd){
					vo.ruleContents.add(ruleContent);
				}
			}
			retVo.add(vo);
		}
		if(bChanage){
			playerGamedRecordManager.saveCreateRuleList(accountId, createRuleMap);
		}
		
		roomItemCfgs = null;
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object majiang_room_create(Long accountId, String corpsId,
			int roomItemId, List<Integer> rules, String password) {
		
		RoomItemConfig roomItemCfg = cfgManager.getRoomItemCfg(roomItemId);
		if(null == roomItemCfg){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		if(null == rules || rules.isEmpty() || null == corpsId || corpsId.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		GameDataAttrib gameDataAttrib = gameDataManager.getGameData(accountId);
		if(gameDataAttrib.tableId > 0){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家游戏中,不能创建房间", null);
		}
		
		//获取需要的房卡数
		int needCardNum = 0;
		for(Integer ruleId : rules){
			RoomRuleConfig ruleCfg = cfgManager.getRuleItemCfg(ruleId);
			if(null == ruleCfg){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
			}
			GameObject[] tmpObjects = ruleCfg.getCost();
			for(GameObject tmpObject : tmpObjects){
				needCardNum += tmpObject.amount;
			}
		}
		
		String err = "接口参数错误";
		
		if(null != corpsId && corpsId.equalsIgnoreCase("0") == false ){
			//创建帮会桌子,检查帮会是否存在,帮会是否可创建房间,找到帮会创建人
			corpsManager.lock();
			try{
				do{
					CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
					if(null == corpsEntity){
						err = "麻将馆不存在";
						break;
					}
					if(corpsEntity.getCorpsState() < 0){
						//帮会已冻结
						err = "麻将馆已被关闭使用,无法创建房间";
						break;
					}
					int roomCardState = corpsEntity.getRoomCardState();
					if(roomCardState <= 0){
						err = "麻将馆已被关闭使用,无法创建房间";
						break;
					}
					
					long corpsRoomCard = corpsEntity.getRoomCard();
					if(corpsRoomCard < needCardNum){
						err = "创建房间需要的房卡不足";
						break;
					}
					
					if(corpsEntity.getMemberList().containsKey(accountId) == false){
						err = "麻将馆成员才能创建麻将馆房间";
						break;
					}
					//扣除帮会房卡
					corpsRoomCard = corpsRoomCard - needCardNum;
					corpsEntity.setRoomCard(corpsRoomCard);
					
					//发送帮会钱包变化通知
					corpsManager.sendCorpsWalletNotify(corpsId, corpsRoomCard);
					
					err = "";
				}while(false);
			}finally{
				corpsManager.unLock();
			}
			if(err.isEmpty() == false){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
			}
		}else{
			corpsId = "0";
			
			//个人创建桌子,只检查房卡,先不扣除
			WalletEntity wallet = walletManager.loadOrCreate(accountId);
			wallet.lock();
			try{
				int roomCard = wallet.getRoomCard() + wallet.getReplaceCard();
				if(roomCard >= needCardNum){
					err = "";
				}else{
					err = "创建房间需要的房卡不足";
				}
			}finally{
				wallet.unLock();
			}
			
			if(err.isEmpty() == false){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
			}
		}		
		
		List<Integer> allRules = new ArrayList<>();
		RuleCfgAttrib []ruleCfgAttribs = roomItemCfg.getRules();
		//添加默认配置
		for(RuleCfgAttrib ruleCfgAttrib : ruleCfgAttribs){
			if(ruleCfgAttrib.show == 0){
				for(int defValue : ruleCfgAttrib.value){
					if(rules.contains(defValue) == false){
						allRules.add(defValue);
					}
				}
			}
		}
		//添加选择配置
		allRules.addAll(rules);
		
		TableVo retVo = new TableVo();
		logicManager.lock();
		try{
			if(null == password || password.isEmpty() || password.equalsIgnoreCase("0")){
				password = "";
			}
			TableAttrib tableObj = logicManager.createTable(accountId, roomItemId, needCardNum, 
					corpsId, allRules, password, sessionManager, cfgManager);
			gameDataAttrib = gameDataManager.getGameData(accountId);
			gameDataAttrib.tableId = tableObj.tableId;
						
			SeatAttrib mySeat = tableObj.getPlayerSeatIndex(accountId);
			TableBaseVo tableBaseVo = logicManager.Table2TableBaseVo(tableObj);
			retVo.tableBaseVo = tableBaseVo;
			for(SeatAttrib seat : tableObj.seats){
				SeatVo seatVo = logicManager.Seat2SeatVo(tableObj, seat, mySeat.seatIndex);
				retVo.seats.add(seatVo);
			}
			mySeat.btState = GameDefine.ACT_STATE_BT;
			
			
			tableObj.gameName = roomItemCfg.getItemName();
			//如果是帮会桌子推送帮会桌子创建消息
			if(null != corpsId && corpsId.equalsIgnoreCase("0") == false ){
				corpsManager.sendCorpsCreateTableNotify(accountId, corpsId, 
						retVo.tableBaseVo.tableId, tableObj.gameName, password);
				
				corpsManager.sendCorpsTableDataChanageNotify(tableObj, tableObj.gameName, CorpsManager.CORPS_TABLE_CHANAGE_TYPE_ADD);
			}
		}finally{
			logicManager.unLock();
		}
		
		Map<Integer, PlayerCreatedRuleAttrib> createRuleMap = playerGamedRecordManager.getPlayerCreateRuleList(accountId);
		PlayerCreatedRuleAttrib createdAttrib = createRuleMap.get(roomItemId);
		if(null == createdAttrib){
			createdAttrib = new PlayerCreatedRuleAttrib();
			createdAttrib.ruleItem = roomItemId;
		}
		createdAttrib.ruleList.clear();
		createdAttrib.ruleList.addAll(rules);
		createRuleMap.put(roomItemId, createdAttrib);
		
		playerGamedRecordManager.saveCreateRuleList(accountId, createRuleMap);
				
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object majiang_room_join(Long accountId, int tableId, String password) {
		if(null == password || password.isEmpty() || tableId <= 0){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		String err = "接口参数错误";
		TableVo retVo = new TableVo();
		logicManager.lock();
		try{
			do{
				GameDataAttrib gameData = gameDataManager.getGameData(accountId);
				int oldTableId = gameData.tableId;
				TableAttrib findTable = null;
				if(tableId != oldTableId){
					TableAttrib oldTable = logicManager.getTable(oldTableId);
					if(null == oldTable){
						//原来的桌子没有了
						findTable = logicManager.getTable(tableId);
					}else{
						//原来的房间还在进原桌子
						findTable = oldTable;
					}
				}else{
					findTable = logicManager.getTable(tableId);
				}
				
				if(null == findTable){
					err = "房间未找到";
					break;
				}
				if(findTable.ruleAttrib.roomCardPayType > 1){
					//桌子房卡支付方式 为2=随机付,3=赢家付,4=输家付,检查房卡数量
					WalletEntity walletEntity = walletManager.loadOrCreate(accountId);
					walletEntity.lock();
					try{
						int roomCard = walletEntity.getRoomCard() + walletEntity.getReplaceCard();
						if(roomCard >= findTable.createCost){
							err = "";
						}else{
							err = "加入房间房卡不足";
						}
					}finally{
						walletEntity.unLock();
					}
					if(err.isEmpty() == false){
						break;
					}
				}
				
				err = "接口参数错误";
				SeatAttrib oldSeat = findTable.getPlayerSeatIndex(accountId);
				if(null == oldSeat){
					//不在座位上
					
					//检查加入密码
					if(findTable.password.isEmpty() == false){
						if(findTable.password.equalsIgnoreCase(password) == false){
							err = "加入房间密码错误";
							break;
						}
					}
					
					if(findTable.corpsId.isEmpty()==false &&
							findTable.corpsId.equalsIgnoreCase("0")==false ){
						//是帮会房间,必须是帮会成员才能加入
						corpsManager.lock();
						try{
							CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(findTable.corpsId);
							if(null != corpsEntity){
								if(corpsEntity.getMemberList().containsKey(accountId) == false){
									err = "麻将馆成员才能加入麻将馆房间";
									break;
								}
							}
						}finally{
							corpsManager.unLock();
						}
					}
					
					SeatAttrib emptySeat = findTable.getEmptySeat();
					if(null == emptySeat){
						err = "房间已经满员";
						break;
					}
					emptySeat.accountId = accountId;
					
					gameData.tableId = findTable.tableId;
					//推送桌子座位变化数据
					findTable.sendSeatNotify(emptySeat.seatIndex);
					
					//如果是帮会桌子推送帮会桌子消息
					if(null != findTable.corpsId && findTable.corpsId.equalsIgnoreCase("0") == false ){						
						corpsManager.sendCorpsTableDataChanageNotify(findTable, findTable.gameName, CorpsManager.CORPS_TABLE_CHANAGE_TYPE_MODFIY);
					}
				}
				
				SeatAttrib mySeat = findTable.getPlayerSeatIndex(accountId);
				if(findTable.gameState == GameDefine.STATE_TABLE_IDLE
						|| findTable.gameState == GameDefine.STATE_TABLE_READY){
					mySeat.btState = GameDefine.ACT_STATE_BT;
				}
				
				TableBaseVo tableBaseVo = logicManager.Table2TableBaseVo(findTable);
				retVo.tableBaseVo = tableBaseVo;
				for(SeatAttrib seat : findTable.seats){
					SeatVo seatVo = logicManager.Seat2SeatVo(findTable, seat, mySeat.seatIndex);
					retVo.seats.add(seatVo);
				}
								
				err = "";
			}while(false);
		}finally{
			logicManager.unLock();
		}
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object majiang_room_leav(Long accountId, int tableId) {
		logicManager.lock();
		try{
			do{
				TableAttrib tableObj = logicManager.getTable(tableId);
				if(null == tableObj){
					break;
				}				
				SeatAttrib seat = tableObj.getPlayerSeatIndex(accountId);
				if(null == seat){
					break;
				}
				if(GameDefine.STATE_TABLE_IDLE != tableObj.gameState
						|| (GameDefine.STATE_TABLE_IDLE == tableObj.gameState 
						&& tableObj.currGameNum != 1)){
					//游戏已开始或游戏进行了多局,玩家无法解散桌子
					break;
				}
				if(seat.accountId == tableObj.createPlayer){
					tableObj.bRemove = true;
					//房主离开房间,踢出其它人
					List<Long> notifyIds = new ArrayList<>();
					for(SeatAttrib theSeat : tableObj.seats){
						if(0 == theSeat.accountId){
							continue;
						}
						GameDataAttrib gameData = gameDataManager.getGameData(theSeat.accountId);
						gameData.tableId = 0;
						notifyIds.add(theSeat.accountId);
						theSeat.accountId = 0;
					}
					//未开始游戏返回房卡
					if(tableObj.corpsId.equalsIgnoreCase("0") == false){
						//是帮会房间
						corpsManager.lock();
						try{
							CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(tableObj.corpsId);
							if(null != corpsEntity){
								long roomCard = corpsEntity.getRoomCard() + tableObj.createCost;
								corpsEntity.setRoomCard(roomCard);
							}
						}finally{
							corpsManager.unLock();
						}
					}
					
					//推送房间解散消息
					tableObj.sendTableDestoryNotify(notifyIds);
				}else{
					//从座位上删除玩家
					seat.accountId = 0;
					seat.resetAll();
					//推送桌子座位变化数据
					tableObj.sendSeatNotify(seat.seatIndex);
					GameDataAttrib gameData = gameDataManager.getGameData(accountId);
					gameData.tableId = 0;
					
					//如果是帮会桌子推送帮会桌子消息
					if(null != tableObj.corpsId && tableObj.corpsId.equalsIgnoreCase("0") == false ){						
						corpsManager.sendCorpsTableDataChanageNotify(tableObj, tableObj.gameName, CorpsManager.CORPS_TABLE_CHANAGE_TYPE_MODFIY);
					}
				}
			}while(false);
		}finally{
			logicManager.unLock();
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_get_roominfo(Long accountId, int tableId) {
		String err = "接口参数错误";
		TableVo retVo = new TableVo();
		logicManager.lock();
		try{
			do{
				TableAttrib tableObj = logicManager.getTable(tableId);
				if(null == tableObj){
					err = "房间未找到";
					break;
				}
				SeatAttrib mySeat = tableObj.getPlayerSeatIndex(accountId);
				if(null == mySeat){
					err = "房间未找到";
					break;
				}
				TableBaseVo tableBaseVo = logicManager.Table2TableBaseVo(tableObj);
				retVo.tableBaseVo = tableBaseVo;
				for(SeatAttrib seat : tableObj.seats){
					SeatVo seatVo = logicManager.Seat2SeatVo(tableObj, seat, mySeat.seatIndex);
					retVo.seats.add(seatVo);
				}
				err = "";
			}while(false);
		}finally{
			logicManager.unLock();
		}
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object majiang_room_swap_card(Long accountId, int tableId, int[] cardIds) {
		if(null == cardIds || cardIds.length != 3){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}	
		
		String err = "接口参数错误";
		logicManager.lock();
		try{
			do{
				TableAttrib tableObj = logicManager.getTable(tableId);
				if(null == tableObj){
					err = "房间未找到";
					break;
				}
				if(tableObj.gameState != GameDefine.STATE_TABLE_SWAPCARD){
					err = "还未轮到表态时间";
					break;
				}
				SeatAttrib mySeat = tableObj.getPlayerSeatIndex(accountId);
				if(null == mySeat){
					err = "还未轮到表态时间";
					break;
				}
				
				if(mySeat.btState != GameDefine.ACT_STATE_WAIT){
					err = "还未轮到表态时间";
					break;
				}
				
				CardAttrib suitCard1 = GameDefine.findOnceByCardId(mySeat.handCards, cardIds[0]);
				CardAttrib suitCard2 = GameDefine.findOnceByCardId(mySeat.handCards, cardIds[1]);
				CardAttrib suitCard3 = GameDefine.findOnceByCardId(mySeat.handCards, cardIds[2]);
				if(null == suitCard1 || null == suitCard2 || null == suitCard3
						|| suitCard1.suit != suitCard2.suit
						|| suitCard1.suit != suitCard3.suit
						|| suitCard2.suit != suitCard3.suit){
					err = "换牌数据错误";
					break;
				}
				mySeat.swapCards.clear();
				mySeat.swapBtCards.clear();
				mySeat.swapBtCards.add(suitCard1);
				mySeat.swapBtCards.add(suitCard2);
				mySeat.swapBtCards.add(suitCard3);
				mySeat.swapCards.addAll(mySeat.swapBtCards);
				GameDefine.removeOnceBySuitPoint(mySeat.handCards, suitCard1);
				GameDefine.removeOnceBySuitPoint(mySeat.handCards, suitCard2);
				GameDefine.removeOnceBySuitPoint(mySeat.handCards, suitCard3);
				mySeat.btState = GameDefine.ACT_STATE_BT;
				tableObj.sendSwapCardNotify();
				
				err = "";
			}while(false);
		}finally{
			logicManager.unLock();
		}
				
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_dinque(Long accountId, int tableId, int bt) {
		if(bt < GameDefine.SUIT_TYPE_WAN  || bt > GameDefine.SUIT_TYPE_TIAO){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		String err = "接口参数错误";
		logicManager.lock();
		try{
			do{
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					err = "房间未找到";
					break;
				}
				if(table.gameState != GameDefine.STATE_TABLE_DINGQUE){
					err = "还未轮到表态时间";
					break;
				}
				SeatAttrib seat = table.getPlayerSeatIndex(accountId);
				if(null == seat){
					err = "还未轮到表态时间";
					break;
				}
				if(seat.btState != GameDefine.ACT_STATE_WAIT){
					err = "还未轮到表态时间";
					break;
				}
				seat.btState = GameDefine.ACT_STATE_BT;
				seat.unSuit = bt;
				
				//推送定缺座位数据
				table.sendDinQueNotify(seat.seatIndex);				
				err = "";
			}while(false);
		}finally{
			logicManager.unLock();
		}
				
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_out_card(Long accountId, int tableId, int cardId) {
		String err = "接口参数错误";
		logicManager.lock();
		try{
			do{
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					err = "房间未找到";
					break;
				}
				if(table.gameState != GameDefine.STATE_TABLE_OUTCARD){
					err = "还未轮到表态时间";
					break;
				}
				SeatAttrib seat = table.getPlayerSeatIndex(accountId);
				if(null == seat){
					err = "还未轮到表态时间";
					break;
				}
				if(table.btIndex != seat.seatIndex){
					err = "还未轮到表态时间";
					break;
				}
				if(seat.btState != GameDefine.ACT_STATE_WAIT){
					err = "还未轮到表态时间";
					break;
				}
				
				CardAttrib makeCard = GameDefine.makeCard(cardId);
				if(table.ruleAttrib.bYaoJiRenYong){
					//幺鸡任用不能打
					if(makeCard.suit == GameDefine.SUIT_TYPE_TIAO
							&& makeCard.point == 1){
						err = "幺鸡任用规则中,幺鸡不能打出";
						break;
					}
				}				
				
				List<CardAttrib> copyHandCards = new ArrayList<>();
				copyHandCards.addAll(seat.handCards);
				if(null != seat.moPaiCard){
					copyHandCards.add(seat.moPaiCard);
				}
				
				CardAttrib findCard = GameDefine.findOnceByCardId(copyHandCards, cardId);
				if(null == findCard){
					//打的牌未找到
					err = "打出的牌数据错误";
					break;
				}
				
				if(table.ruleAttrib.bYaoJiRenYong){
					List<CardAttrib> yaojiCards = GameDefine.findAllBySuitPoint(copyHandCards, GameDefine.SUIT_TYPE_TIAO, 1);
					GameDefine.removeAllBySuitPoint(copyHandCards, yaojiCards);
				}
				//未打缺前不能打其它花色的牌
				boolean bQue = GameDefine.isDaQue(copyHandCards, seat.unSuit);
				if(bQue == false){
					if(seat.unSuit != findCard.suit){
						err = "未打缺不能打出其它花色的牌";
						break;
					}
				}
				if(table.cfgId == 8){
					//南充麻将
					List<CardAttrib> unCanOutCards = new ArrayList<>();
					for(SeatAttrib theSeat : table.seats){
						if(seat.seatIndex == theSeat.seatIndex || theSeat.tangCardState <= 0){
							continue;
						}
						unCanOutCards.addAll(theSeat.tangCanHuList);
					}
					
					CardAttrib findUnOutCard = GameDefine.findOnceBySuitPoint(unCanOutCards, findCard.suit, findCard.point);
					if(null != findUnOutCard){
						//是不能打出的牌,检查手里的牌全是不能打出的牌才能打
						if(null != seat.moPaiCard){
							copyHandCards.add(seat.moPaiCard);
						}
						boolean bCanOut = true;
						for(CardAttrib theCard : copyHandCards){
							findUnOutCard = GameDefine.findOnceBySuitPoint(unCanOutCards, theCard.suit, theCard.point);
							if(null == findUnOutCard){
								//还有其它牌可以打,不能打出这张牌
								bCanOut = false;
								break;
							}
						}
						if(bCanOut == false){
							err = "南充麻将不能打出其它玩家摆了要胡的牌";
							break;
						}
					}
				}
				
				if(null != seat.moPaiCard){
					seat.handCards.add(seat.moPaiCard);
				}
				seat.moPaiCard = null;
				
				seat.outUnUseCards.add(findCard);
				seat.outCard = findCard;
				GameDefine.removeOnceByCardId(seat.handCards, findCard.cardId);
				seat.btState = GameDefine.ACT_STATE_BT;
				seat.outHandNum++;
				
				err = "";
			}while(false);
		}finally{
			logicManager.unLock();
		}
				
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_otherbreak_card(Long accountId, int tableId, int bt, int cardId) {
		String err = "接口参数错误";
		logicManager.lock();
		try{
			do{
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					err = "房间未找到";
					break;
				}
				if(table.gameState != GameDefine.STATE_TABLE_BREAKCARD){
					err = "还未轮到表态时间";
					break;
				}
				SeatAttrib mySeat = table.getPlayerSeatIndex(accountId);
				if(null == mySeat){
					err = "还未轮到表态时间";
					break;
				}
				if(bt < GameDefine.ACT_INDEX_HU ||
						bt > GameDefine.ACT_INDEX_DROP){
					err = "接口参数错误";
					break;
				}
				if(mySeat.breakCardState[bt] <= 0){
					err = "接口参数错误";
					break;
				}
				if(bt == GameDefine.ACT_INDEX_TANG){
					err = "接口参数错误";
					break;
				}
				
				if(mySeat.btState != GameDefine.ACT_STATE_WAIT){
					err = "还未轮到表态时间";
					break;
				}
				
				CardAttrib btCard = GameDefine.makeCard(cardId);
				SeatAttrib btSeat = table.seats.get(table.btIndex);				
				if(bt == GameDefine.ACT_INDEX_HU){
					if(btSeat.breakCard.cardId != cardId){
						err = "接口参数错误";
						break;
					}
					
					if(mySeat.breakCardState[GameDefine.ACT_INDEX_HU] <= 0){
						err = "还未轮到表态时间";
						break;
					}
				}else if(bt == GameDefine.ACT_INDEX_GANG){
					if(btSeat.seatIndex == mySeat.seatIndex){
						//自已摸牌后表态杠牌
						if(table.ruleAttrib.bTangPai && mySeat.tangCardState > 0){
							//躺了牌检查杠牌是否是躺牌相关
							List<CardAttrib> tangFindCards = GameDefine.findAllBySuitPoint(mySeat.tangCardList, btCard.suit, btCard.point);
							if(null != tangFindCards && tangFindCards.isEmpty() == false){
								//是躺了的牌不能杠
								err = "躺了或摆了的牌不能杠";
								break;
							}
							
							List<CardAttrib> tmpCopyCards = new ArrayList<>();
							tmpCopyCards.addAll(mySeat.handCards);
							for(List<CardAttrib> pengCardList : mySeat.pengCards){
								tmpCopyCards.addAll(pengCardList);
							}
							tmpCopyCards.add(btCard);
							List<CardAttrib> gangCards = GameDefine.findAllBySuitPoint(tmpCopyCards, btCard.suit, btCard.point);
							GameDefine.removeAllBySuitPoint(tmpCopyCards, gangCards);
							List<CardAttrib> tingHuCards = GameDefine.isTingPai(table, tmpCopyCards, mySeat);
							
							boolean bGangOK = true;
							for(CardAttrib tangCanHuCard : mySeat.tangCanHuList){
								List<CardAttrib> tmpFindtangCanHuCards = GameDefine.findAllBySuitPoint(tingHuCards, tangCanHuCard.suit, tangCanHuCard.point);
								if(null == tmpFindtangCanHuCards || tmpFindtangCanHuCards.isEmpty()){
									//杠了后的牌叫牌和躺胡的牌不一样
									bGangOK = false;
									break;
								}
							}
							if(bGangOK == false){
								err = "杠了后叫牌和躺胡或摆胡的牌不一样,不能杠牌";
								break;
							}
						}
						
						if(table.ruleAttrib.bBaoJia && mySeat.baoJiaoState > 0){
							//报了叫的,杠牌后必须要有叫
							List<CardAttrib> tmpCopyCards = new ArrayList<>();
							tmpCopyCards.addAll(mySeat.handCards);
							for(List<CardAttrib> pengCardList : mySeat.pengCards){
								tmpCopyCards.addAll(pengCardList);
							}
							tmpCopyCards.add(btCard);
							List<CardAttrib> gangCards = GameDefine.findAllBySuitPoint(tmpCopyCards, btCard.suit, btCard.point);
							GameDefine.removeAllBySuitPoint(tmpCopyCards, gangCards);
							List<CardAttrib> tingHuCards = GameDefine.isTingPai(table, tmpCopyCards, mySeat);
							if(null == tingHuCards || tingHuCards.isEmpty()){
								err = "报了叫必须杠了牌后有叫才能杠";
								break;
							}
						}
						
						if(btCard.suit == btSeat.breakCard.suit
								&& btCard.point == btSeat.breakCard.point){
							//是杠摸起来的牌
							List<CardAttrib> bagangs = new ArrayList<>();
							List<CardAttrib> angangs = new ArrayList<>();
							for(int tmpI=0; tmpI<mySeat.pengCards.size(); tmpI++){
								List<CardAttrib> gangCards = mySeat.pengCards.get(tmpI);
								for(CardAttrib card : gangCards){
									if(card.suit == btCard.suit 
											&& card.point == btCard.point){
										bagangs = gangCards;
										break;
									}
								}
							}
							angangs = GameDefine.findAllBySuitPoint(mySeat.handCards, btSeat.breakCard.suit, btSeat.breakCard.point);
							if(angangs.size() != 3 && bagangs.size() != 3){
								err = "接口参数错误";
								break;
							}
						}else{
							//是杠手里的牌
							List<CardAttrib> bagangs = new ArrayList<>();
							for(int tmpI=0; tmpI<mySeat.pengCards.size(); tmpI++){
								List<CardAttrib> gangCards = mySeat.pengCards.get(tmpI);
								for(CardAttrib card : gangCards){
									if(card.suit == btCard.suit 
											&& card.point == btCard.point){
										bagangs = gangCards;
										break;
									}
								}
							}
							List<CardAttrib> angangs = GameDefine.findAllBySuitPoint(mySeat.handCards, btCard.suit, btCard.point);
							if(angangs.size() != 4 && bagangs.size() != 3){
								err = "接口参数错误";
								break;
							}
						}
					}else{
						//别人打牌杠牌
						if(btSeat.breakCard.cardId != cardId){
							err = "接口参数错误";
							break;
						}
						
						List<CardAttrib> angangs = GameDefine.findAllBySuitPoint(mySeat.handCards, btSeat.breakCard.suit, btSeat.breakCard.point);
						if(angangs.size() != 3){
							err = "接口参数错误";
							break;
						}
						if(table.ruleAttrib.bTangPai && mySeat.tangCardState > 0){
							//躺了牌,不能是胡牌相关的杠							
							List<CardAttrib> tmpFindCards = GameDefine.findAllBySuitPoint(mySeat.tangCardList, btSeat.breakCard.suit, btSeat.breakCard.point);
							boolean bCanHu = true;
							if(null == tmpFindCards || tmpFindCards.isEmpty()){
								//是不和躺牌相关的杠牌,把杠牌移除后,是否叫牌和躺牌叫一样
								List<CardAttrib> tmpCopyCards = new ArrayList<>();
								tmpCopyCards.addAll(mySeat.handCards);
								GameDefine.removeAllBySuitPoint(tmpCopyCards, tmpFindCards);
								List<CardAttrib> tingHuCards = GameDefine.isTingPai(table, tmpCopyCards, mySeat);
								
								for(CardAttrib tangCanHuCard : mySeat.tangCanHuList){
									List<CardAttrib> tmpFindtangCanHuCards = GameDefine.findAllBySuitPoint(tingHuCards, tangCanHuCard.suit, tangCanHuCard.point);
									if(null == tmpFindtangCanHuCards || tmpFindtangCanHuCards.isEmpty()){
										//叫牌改变了不能杠
										bCanHu = false;
										break;
									}
								}
							}
							
							if(bCanHu == false){
								err = "接口参数错误";
								break;
							}
						}
						if(table.ruleAttrib.bBaoJia && mySeat.baoJiaoState > 0){
							//报了叫的,杠牌后必须要有叫
							List<CardAttrib> tmpCopyCards = new ArrayList<>();
							tmpCopyCards.addAll(mySeat.handCards);
							GameDefine.removeAllBySuitPoint(tmpCopyCards, angangs);
							List<CardAttrib> tingHuCards = GameDefine.isTingPai(table, tmpCopyCards, mySeat);
							if(null == tingHuCards || tingHuCards.isEmpty()){
								err = "接口参数错误";
								break;
							}
						}
					}
				}else if(bt == GameDefine.ACT_INDEX_PENG){
					if(btSeat.breakCard.cardId != cardId){
						err = "接口参数错误";
						break;
					}
					if(table.ruleAttrib.bTangPai && mySeat.tangCardState > 0){
						//躺了的牌不能碰
						err = "躺了牌不能碰牌";
						break;
					}
					if(table.ruleAttrib.bBaoJia && mySeat.baoJiaoState > 0){
						//报了叫的牌不能碰
						err = "报了叫不能碰牌";
						break;
					}
					
					List<CardAttrib> f1 = GameDefine.findAllBySuitPoint(mySeat.handCards, btCard.suit, btCard.point);
					if(f1.size() != 2 && f1.size() != 3){
						err = "接口参数错误";
						break;
					}
				}
				
				mySeat.breakBtState = bt;
				mySeat.btState = GameDefine.ACT_STATE_BT;
				if(mySeat.breakBtState == GameDefine.ACT_INDEX_DROP){
					mySeat.btState = GameDefine.ACT_STATE_DROP;
				}else{
					mySeat.breakCard = btCard;	
				}
				err = "";
			}while(false);
		}finally{
			logicManager.unLock();
		}
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_quest_delete(Long accountId, int tableId) {
		String err = "接口参数错误";
		logicManager.lock();
		try{
			do{
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					err = "房间未找到";
					break;
				}
				SeatAttrib seat = table.getPlayerSeatIndex(accountId);
				if(null == seat){
					err = "房间未找到";
					break;
				}
				if(table.gameState == GameDefine.STATE_TABLE_DESTORY){
					err = "接口参数错误";
					break;
				}
				if(GameDefine.STATE_TABLE_IDLE == table.gameState 
						&& table.currGameNum == 1){
					//桌子还未开始过游戏
					this.majiang_room_leav(accountId, tableId);
					err = "";
					break;
				}else{
					//投票,等待其余玩家表态
					long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
					table.gameStateSave = table.gameState;
					table.bActExecSave = table.bActExec;
					table.btIndexSave = table.btIndex;
					table.waitTimeSave = table.waitTime-currTime;
					if(table.waitTimeSave < 0){
						table.waitTimeSave = 0;
					}
					
					table.gameState = GameDefine.STATE_TABLE_DESTORY;
					table.bActExec = false;
					for(SeatAttrib theSeat : table.seats){
						theSeat.btStateSave = theSeat.btState;
						theSeat.btState = GameDefine.ACT_STATE_WAIT;
					}
					seat.btState = GameDefine.ACT_STATE_BT;
					table.destoryQuestPlayer = accountId;
				}				
				err = "";
			}while(false);
		}finally{
			logicManager.unLock();
		}
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_delete_bt(Long accountId, int tableId, int bt) {
		if(bt != 1 && bt != 2){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		String err = "接口参数错误";
		logicManager.lock();
		try{
			do{				
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					err = "房间未找到";
					break;
				}
				SeatAttrib seat = table.getPlayerSeatIndex(accountId);
				if(null == seat){
					err = "房间未找到";
					break;
				}
				if(table.gameState != GameDefine.STATE_TABLE_DESTORY){
					err = "还未轮到表态时间";
					break;
				}
				if(seat.btState != GameDefine.ACT_STATE_WAIT){
					err = "还未轮到表态时间";
					break;
				}
				if(bt == 1){
					//拒绝
					seat.btState = GameDefine.ACT_STATE_DROP;
				}else{
					//同意
					seat.btState = GameDefine.ACT_STATE_BT;
				}
				table.sendDestoryBtNotify(seat.seatIndex);
				err = "";
			}while(false);
		}finally{
			logicManager.unLock();
		}
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_next_game(Long accountId) {
		TableVo tableVo = null;
		logicManager.lock();
		try{
			GameDataAttrib dataAttrib = gameDataManager.getGameData(accountId);
			if(dataAttrib.tableId > 0){
				TableAttrib table = logicManager.getTable(dataAttrib.tableId);
				if(null != table){
					SeatAttrib mySeat = table.getPlayerSeatIndex(accountId);
					if(null != mySeat){
						mySeat.btState = GameDefine.ACT_STATE_BT;
					}
					tableVo = logicManager.Table2TableVo(table);
					table.sendSeatNotify(mySeat.seatIndex);
				}else{
					dataAttrib.tableId = 0;
					tableVo = dataAttrib.allOverVo;
				}
			}else{
				tableVo = dataAttrib.allOverVo;
			}
		}finally{
			logicManager.unLock();
		}
		if(null == tableVo){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "", null);
		}
		return Result.valueOfSuccess(tableVo);
	}

	@Override
	public Object majiang_room_tang_card_bt(Long accountId, int tableId, int[] btcards, int[] hucards, int outcard) {
		if(tableId<=0 || null == btcards || btcards.length==0
				|| null== hucards || hucards.length==0 || outcard<=0){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		String err = "接口参数错误";
		logicManager.lock();
		try{
			do{
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					err = "房间未找到";
					break;
				}
				if(table.ruleAttrib.bTangPai == false){
					err = "房间未找到";
					break;
				}
				
				SeatAttrib mySeat = table.getPlayerSeatIndex(accountId);
				if(null == mySeat){
					err = "房间未找到";
					break;
				}
				
				if(mySeat.btState != GameDefine.ACT_STATE_WAIT){
					err = "还未轮到表态时间";
					break;
				}
				
				if(mySeat.breakCardState[GameDefine.ACT_INDEX_TANG] <= 0){
					err = "接口参数错误";
					break;
				}
				if(mySeat.tangCardState > 0){
					err = "还未轮到表态时间";
					break;
				}
				
				List<CardAttrib> copyHandCards = new ArrayList<>();
				copyHandCards.addAll(mySeat.handCards);
				if(null != mySeat.moPaiCard){
					copyHandCards.add(mySeat.moPaiCard);
				}
				CardAttrib findOutCard = GameDefine.findOnceByCardId(copyHandCards, outcard);
				if(null == findOutCard){
					err = "未找到要打出的牌";
					break;
				}
				
				boolean bBtCardErr = false;
				List<CardAttrib> tangCards = new ArrayList<>();
				for(int btcard : btcards){
					findOutCard = GameDefine.findOnceByCardId(copyHandCards, btcard);
					if(null == findOutCard){
						bBtCardErr = true;
						break;
					}
				}
				if(bBtCardErr){
					err = "要躺的牌数据错误";
					break;
				}
				for(int btcard : btcards){
					tangCards.add(GameDefine.makeCard(btcard));
				}
				
				List<CardAttrib> tangHuCards = new ArrayList<>();
				for(int hucard : hucards){
					CardAttrib makeCard = GameDefine.makeCard(hucard);
					tangHuCards.add(makeCard);
				}
				boolean bFullHu = true;
				List<CardAttrib> chkList = new ArrayList<>();
				for(CardAttrib cardAttrib : tangHuCards){
					chkList.clear();
					chkList.addAll(mySeat.handCards);
					if(1 == mySeat.tangSource){
						//摸牌后躺牌,需要加上摸的牌,碰牌不用加
						chkList.add(mySeat.breakCard);
					}
					chkList.add(cardAttrib);
					//添加手牌,摸的牌,和要胡的牌
					boolean bCanHu = false;
										
					for(int cardIndex=0; cardIndex<chkList.size(); cardIndex++){
						List<CardAttrib> chkList1 = new ArrayList<>();
						chkList1.addAll(chkList);
						
						//打出一张后是否能胡牌
						chkList1.remove(cardIndex);
						List<List<CardAttrib>> huList = GameDefine.checkHuPai2(table, chkList1, mySeat.unSuit);
						if(null != huList && huList.isEmpty() == false){
							bCanHu = true;
							break;
						}
					}
					if(bCanHu == false){
						bFullHu = false;
						break;
					}
				}
				if(bFullHu == false){
					err = "接口参数错误";
					break;
				}
				
				mySeat.tangCardList.clear();
				mySeat.tangCardList.addAll(tangCards);
				mySeat.tangCanHuList.clear();
				mySeat.tangCanHuList.addAll(tangHuCards);
				mySeat.tangCardState = 1;
				mySeat.breakBtState = GameDefine.ACT_INDEX_TANG;
				mySeat.btState = GameDefine.ACT_STATE_BT;
				mySeat.tangOutCard = outcard;
				
				err = "";
			}while(false);
		}finally{
			logicManager.unLock();
		}		
	
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_baojiao_bt(Long accountId, int tableId, int btVal) {
		if(btVal<0 || btVal>1){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		String err = "接口参数错误";
		logicManager.lock();
		try{
			do{
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					err = "房间未找到";
					break;
				}
				SeatAttrib mySeat = table.getPlayerSeatIndex(accountId);
				if(null == mySeat){
					err = "房间未找到";
					break;
				}
				if(mySeat.baoJiaoState != 0){
					err = "还未轮到表态时间";
					break;
				}
				
				if(btVal == 0){
					//不报叫
					mySeat.baoJiaoState = -1;
				}else{
					//报叫
					mySeat.baoJiaoState = 1;
				}
				
				Notify_Seat_Vo_BaoJiao notifyVo = new Notify_Seat_Vo_BaoJiao();
				notifyVo.seatIndex = mySeat.seatIndex;
				notifyVo.baojiaoState = mySeat.baoJiaoState;
				
				List<Long> ids = new ArrayList<>();
				for(SeatAttrib seat : table.seats){
					if(seat.accountId > 0){
						ids.add(seat.accountId);
					}
				}
				
				@SuppressWarnings("rawtypes")
				Request pushMsg = Request.valueOf(MajiangDefine.MAJIANGGAME_COMMAND_TABLE_BAOJIAO_BT_NOTIFY, 
						Result.valueOfSuccess(notifyVo));			
				MessagePushQueueUtils.getPushQueue(sessionManager).push(ids, pushMsg);
				
				err = "";
			}while(false);
		}finally{
			logicManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_otherbreak_card_lsmj_bt(Long accountId, int tableId, int bt, int cardId, int gangType,
			int replace) {
		String err = "接口参数错误";
		logicManager.lock();
		try{
			do{
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					err = "房间未找到";
					break;
				}
				if(table.gameState != GameDefine.STATE_TABLE_BREAKCARD){
					err = "还未轮到表态时间";
					break;
				}
				SeatAttrib mySeat = table.getPlayerSeatIndex(accountId);
				if(null == mySeat){
					err = "还未轮到表态时间";
					break;
				}
				if(bt < GameDefine.ACT_INDEX_HU ||
						bt > GameDefine.ACT_INDEX_DROP){
					err = "接口参数错误";
					break;
				}
				if(mySeat.breakCardState[bt] <= 0){
					err = "接口参数错误";
					break;
				}
				if(bt == GameDefine.ACT_INDEX_TANG){
					err = "接口参数错误";
					break;
				}
				
				if(mySeat.btState != GameDefine.ACT_STATE_WAIT){
					err = "还未轮到表态时间";
					break;
				}				
				
				CardAttrib btCard = GameDefine.makeCard(cardId);
				SeatAttrib btSeat = table.seats.get(table.btIndex);				
				if(bt == GameDefine.ACT_INDEX_HU){
					if(btSeat.breakCard.cardId != cardId){
						err = "接口参数错误";
						break;
					}
					
					if(mySeat.breakCardState[GameDefine.ACT_INDEX_HU] <= 0){
						err = "接口参数错误";
						break;
					}
				}else if(bt == GameDefine.ACT_INDEX_GANG){
					if(btSeat.seatIndex == mySeat.seatIndex){
						//自已摸牌后表态杠牌
						if(gangType == GameDefine.GANG_TYPE_SELF_BAGANG){
							//巴杠
							boolean bFind = false;
							for(int tmpI=0; tmpI<btSeat.pengCards.size(); tmpI++){
								List<CardAttrib> pengCards = btSeat.pengCards.get(tmpI);
								for(CardAttrib card : pengCards){
									if(card.suit == btCard.suit
											&& card.point == btCard.point){
										bFind = true;
										break;
									}
								}
								if(bFind){
									break;
								}
							}
							if(bFind == false){
								err = "接口参数错误";
								break;
							}
						}else if(gangType == GameDefine.GANG_TYPE_SELF_ANGANG){
							//暗杠
							List<CardAttrib> copyCards = new ArrayList<>();
							copyCards.addAll(mySeat.handCards);
							copyCards.add(btCard);
							if(table.ruleAttrib.bYaoJiRenYong){
								List<CardAttrib> yaoJiList_hands = GameDefine.findAllBySuitPoint(copyCards, GameDefine.SUIT_TYPE_TIAO, 1);
								GameDefine.removeAllBySuitPoint(copyCards, yaoJiList_hands);
								List<CardAttrib> findCards = GameDefine.findAllBySuitPoint(copyCards, btCard.suit, btCard.point);
								
								if((yaoJiList_hands.size() + findCards.size()) < 4){
									err = "接口参数错误";
									break;
								}
							}else{
								List<CardAttrib> findCards = GameDefine.findAllBySuitPoint(copyCards, btCard.suit, btCard.point);
								if(findCards.size() < 4){
									err = "接口参数错误";
									break;
								}
							}
						}else{
							err = "接口参数错误";
							break;
						}
					}else{
						//别人打牌杠牌			
						if(gangType == GameDefine.GANG_TYPE_SELF_ANGANG){
							err = "接口参数错误";
							break;
						}
						List<CardAttrib> copyCards = new ArrayList<>();
						copyCards.addAll(mySeat.handCards);
						copyCards.add(btCard);
						if(table.ruleAttrib.bYaoJiRenYong){
							List<CardAttrib> yaoJiList_hands = GameDefine.findAllBySuitPoint(copyCards, GameDefine.SUIT_TYPE_TIAO, 1);
							GameDefine.removeAllBySuitPoint(copyCards, yaoJiList_hands);
							List<CardAttrib> findCards = GameDefine.findAllBySuitPoint(copyCards, btCard.suit, btCard.point);
							
							if((yaoJiList_hands.size() + findCards.size()) < 4){
								err = "接口参数错误";
								break;
							}
						}else{
							List<CardAttrib> findCards = GameDefine.findAllBySuitPoint(copyCards, btCard.suit, btCard.point);
							if(findCards.size() < 4){
								err = "接口参数错误";
								break;
							}
						}
					}
				}else if(bt == GameDefine.ACT_INDEX_PENG){
					List<CardAttrib> copyCards = new ArrayList<>();
					copyCards.addAll(mySeat.handCards);
					copyCards.add(btCard);
					
					if(table.ruleAttrib.bYaoJiRenYong){
						List<CardAttrib> yaoJiList_hands = GameDefine.findAllBySuitPoint(copyCards, GameDefine.SUIT_TYPE_TIAO, 1);
						GameDefine.removeAllBySuitPoint(copyCards, yaoJiList_hands);
						List<CardAttrib> findCards = GameDefine.findAllBySuitPoint(copyCards, btCard.suit, btCard.point);
						
						if((yaoJiList_hands.size() + findCards.size()) < 3){
							err = "接口参数错误";
							break;
						}
					}else{
						List<CardAttrib> findCards = GameDefine.findAllBySuitPoint(copyCards, btCard.suit, btCard.point);
						if(findCards.size() < 3){
							err = "接口参数错误";
							break;
						}
					}
				}
				
				mySeat.breakBtState = bt;
				mySeat.btState = GameDefine.ACT_STATE_BT;
				if(mySeat.breakBtState == GameDefine.ACT_INDEX_DROP){
					mySeat.btState = GameDefine.ACT_STATE_DROP;
				}else{
					mySeat.breakCard = btCard;	
				}
				mySeat.lsmjGangType = gangType;
				mySeat.bUsedReplaceCard = replace==1?true:false;
				
				err = "";
			}while(false);
		}finally{
			logicManager.unLock();
		}
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_ncmj_piaopai_bt(Long accountId, int tableId, int btVal) {
		String err = "接口参数错误";
		logicManager.lock();
		try{
			do{
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					err = "房间未找到";
					break;
				}
				if(btVal<0 || btVal>table.ruleAttrib.maxPiaoNum){
					err = "接口参数错误";
					break;
				}
				
				SeatAttrib mySeat = table.getPlayerSeatIndex(accountId);
				if(null == mySeat){
					err = "还未轮到表态时间";
					break;
				}
				
				if(mySeat.btState != GameDefine.ACT_STATE_WAIT){
					err = "还未轮到表态时间";
					break;
				}
				
				mySeat.piaoNum = btVal;
				mySeat.btState = GameDefine.ACT_STATE_BT;
				
				Notify_Seat_Vo_PiaoPai notifyVo = new Notify_Seat_Vo_PiaoPai();
				notifyVo.seatIndex = mySeat.seatIndex;
				notifyVo.btState = mySeat.btState;
				notifyVo.piaoNum = btVal;
				
				List<Long> ids = new ArrayList<>();
				for(SeatAttrib seat : table.seats){
					if(seat.accountId > 0){
						ids.add(seat.accountId);
					}
				}
				
				@SuppressWarnings("rawtypes")
				Request pushMsg = Request.valueOf(MajiangDefine.MAJIANGGAME_COMMAND_TABLE_PIAOPAI_BT_NOTIFY, 
						Result.valueOfSuccess(notifyVo));			
				MessagePushQueueUtils.getPushQueue(sessionManager).push(ids, pushMsg);
				
				err = "";
			}while(false);
		}finally{
			logicManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}
	
	@Override
	public Object majiang_room_password_state(Long accountId, int tableId){
		String err = "接口参数错误";
		TablePasswordStateVo retVo = new TablePasswordStateVo();
		logicManager.lock();
		try{
			do{
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					err = "房间未找到";
					break;
				}
				
				retVo.tableId = tableId;
				if(table.password.isEmpty() == false){
					retVo.password = 1;
				}
				
				err = "";
			}while(false);
		}finally{
			logicManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object majiang_room_trusteeship(Long accountId, int tableId, int bt) {
		if(bt<0 || bt>1){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		String err = "接口参数错误";
		logicManager.lock();
		try{
			do{				
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					err = "房间未找到";
					break;
				}
				
				SeatAttrib mySeat = table.getPlayerSeatIndex(accountId);
				if(null == mySeat){
					err = "房间未找到";
					break;
				}
				mySeat.trusteeshipState = bt;
				
				table.sendSeatNotify(mySeat.seatIndex);
				
				GameDataAttrib gameDataAttrib = gameDataManager.getGameData(accountId);
				gameDataAttrib.autoBtTime = 0;
				
				err = "";
			}while(false);
		}finally{
			logicManager.unLock();	
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}

}
