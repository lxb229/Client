package com.palmjoys.yf1b.act.corps.service;

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
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.cooltime.manger.CheckResetTimeManager;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeConfigType;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeResult;
import com.palmjoys.yf1b.act.corps.entity.CorpsEntity;
import com.palmjoys.yf1b.act.corps.entity.PlayerCorpsEntity;
import com.palmjoys.yf1b.act.corps.manager.CorpsCfgManager;
import com.palmjoys.yf1b.act.corps.manager.CorpsIdManager;
import com.palmjoys.yf1b.act.corps.manager.CorpsManager;
import com.palmjoys.yf1b.act.corps.manager.PlayerCorpsManager;
import com.palmjoys.yf1b.act.corps.model.CorpsDetailedVo;
import com.palmjoys.yf1b.act.corps.model.CorpsJoinAttrib;
import com.palmjoys.yf1b.act.corps.model.CorpsMemberAttrib;
import com.palmjoys.yf1b.act.corps.model.CorpsMemberNotifyVo;
import com.palmjoys.yf1b.act.corps.model.CorpsMemberVo;
import com.palmjoys.yf1b.act.corps.model.CorpsRankVo;
import com.palmjoys.yf1b.act.corps.model.CorpsSearchVo;
import com.palmjoys.yf1b.act.corps.model.CorpsStateVo;
import com.palmjoys.yf1b.act.corps.model.CorpsTableVo;
import com.palmjoys.yf1b.act.corps.model.CorpsVo;
import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;
import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.manager.SencePlayerManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.framework.common.manager.CommonCfgManager;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptDefine;
import com.palmjoys.yf1b.act.mail.entity.MailEntity;
import com.palmjoys.yf1b.act.mail.manager.MailManager;
import com.palmjoys.yf1b.act.mail.model.MailAttrib;
import com.palmjoys.yf1b.act.majiang.manager.GameLogicManager;
import com.palmjoys.yf1b.act.majiang.manager.MajiangCfgManager;
import com.palmjoys.yf1b.act.majiang.model.TableAttrib;
import com.palmjoys.yf1b.act.majiang.resource.RoomItemConfig;
import com.palmjoys.yf1b.act.wallet.entity.WalletEntity;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

@Service
public class CorpsServiceImp implements CorpsService{
	@Autowired
	private CorpsManager corpsManager;
	@Autowired
	private PlayerCorpsManager playerCorpsManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private CorpsCfgManager corpsCfgManager;
	@Autowired
	private GameLogicManager logicManager;
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private EventTriggerManager eventTriggerManager;
	@Autowired
	private CommonCfgManager commCfgManager;
	@Autowired
	private CorpsIdManager corpsIdManager;
	@Autowired
	private HotPromptManager hotPromptManager;
	@Autowired
	private MailManager mailManager;
	@Autowired
	private MajiangCfgManager majiangCfgManager;
	@Autowired
	private SencePlayerManager sencePlayerManager;
	@Autowired
	private CheckResetTimeManager resetTimeManager;

	@Override
	public Object corps_get_corps_list(Long accountId, int type) {
		if(type < 1 || type > 3){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		CorpsVo retVo = new CorpsVo();
		corpsManager.lock();
		try{
			switch(type){
			case 1://已加入的
				{
					PlayerCorpsEntity playerCorpsEntity = playerCorpsManager.loadOrCreate(accountId);
					Set<String> corpsIds = playerCorpsEntity.getJoinedCorpsList().keySet();
					for(String corpsId : corpsIds){
						CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
						if(null == corpsEntity){
							continue;
						}
						//计算活跃度
						int totalActiveVal = 0;
						Set<Long> memberIds = corpsEntity.getMemberList().keySet();
						for(long memberId : memberIds){
							PlayerCorpsEntity thePlayerCorpsEntity = playerCorpsManager.loadOrCreate(memberId);
							CorpsMemberAttrib corpsMemberAttrib = thePlayerCorpsEntity.getCorpsDataList().get(corpsId);
							if(null != corpsMemberAttrib){
								totalActiveVal += corpsMemberAttrib.activeValue;
							}
						}
						
						retVo.addItem(corpsId, corpsEntity.getCorpsName(), corpsEntity.getCreatePlayer(),
								corpsEntity.getWxNO(), corpsEntity.getMemberList().size(), 
								totalActiveVal, 1, corpsEntity.getRoomCardState(), corpsEntity.getHidde());
					}
				}	
				break;
			case 2://推荐的
				{
					PlayerCorpsEntity playerCorpsEntity = playerCorpsManager.loadOrCreate(accountId);
					Map<String, String> corpsList = playerCorpsEntity.getJoinedCorpsList();
					
					List<String> corpsIds = corpsCfgManager.getRecommendCorpsList();
					for(String corpsId : corpsIds){
						CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
						if(null == corpsEntity){
							continue;
						}
						if(corpsEntity.getHidde() == 1){
							//隐藏的不显示
							continue;
						}
						
						int join = 0;
						if(corpsList.containsKey(corpsId)){
							join = 1;
						}
						
						//计算活跃度
						int totalActiveVal = 0;
						Set<Long> memberIds = corpsEntity.getMemberList().keySet();
						for(long memberId : memberIds){
							PlayerCorpsEntity thePlayerCorpsEntity = playerCorpsManager.loadOrCreate(memberId);
							CorpsMemberAttrib corpsMemberAttrib = thePlayerCorpsEntity.getCorpsDataList().get(corpsId);
							if(null != corpsMemberAttrib){
								totalActiveVal += corpsMemberAttrib.activeValue;
							}
						}
						
						retVo.addItem(corpsId, corpsEntity.getCorpsName(), corpsEntity.getCreatePlayer(),
								corpsEntity.getWxNO(), corpsEntity.getMemberList().size(), 
								totalActiveVal, join, corpsEntity.getRoomCardState(), corpsEntity.getHidde());
					}
				}
				break;
			case 3://所有公开的
				{
					PlayerCorpsEntity playerCorpsEntity = playerCorpsManager.loadOrCreate(accountId);
					Map<String, String> corpsList = playerCorpsEntity.getJoinedCorpsList();
					
					List<String> corpsIds = corpsManager.findOf_Hidde(0);
					for(String corpsId : corpsIds){
						CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
						if(null == corpsEntity){
							continue;
						}
						if(corpsEntity.getHidde() == 1){
							//隐藏的不显示
							continue;
						}
						int join = 0;
						if(corpsList.containsKey(corpsId)){
							join = 1;
						}
						
						//计算活跃度
						int totalActiveVal = 0;
						Set<Long> memberIds = corpsEntity.getMemberList().keySet();
						for(long memberId : memberIds){
							PlayerCorpsEntity thePlayerCorpsEntity = playerCorpsManager.loadOrCreate(memberId);
							CorpsMemberAttrib corpsMemberAttrib = thePlayerCorpsEntity.getCorpsDataList().get(corpsId);
							if(null != corpsMemberAttrib){
								totalActiveVal += corpsMemberAttrib.activeValue;
							}
						}
						
						retVo.addItem(corpsId, corpsEntity.getCorpsName(), corpsEntity.getCreatePlayer(),
								corpsEntity.getWxNO(), corpsEntity.getMemberList().size(), 
								totalActiveVal, join, corpsEntity.getRoomCardState(), corpsEntity.getHidde());
					}
				}
				break;
			}		
		}finally{
			corpsManager.unLock();
		}
		retVo.sort(accountId);
		
		//更新红点
		int hotVal = corpsManager.checkHotPromptOf_CreatePlayer(accountId);
		hotPromptManager.setHotPromptAttrib(accountId, HotPromptDefine.HOT_KEY_CORPS, hotVal);
		hotPromptManager.notifyHotPrompt(accountId);
		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object corps_create(Long accountId, String corpsName, String wxNO) {
		//检查帮会名称是否合法
		if(null == corpsName || corpsName.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆名称包含非法字符", null);
		}
		
		String retStr = commCfgManager.checkForbiddenWord(corpsName, false);
		if(null == retStr){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆名称包含非法字符", null);
		}
		
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		
		int maxNum = corpsCfgManager.getMaxCreateNum();
		String err = "接口参数错误";
		corpsManager.lock();
		try{
			do{				
				//检查是否已存在的帮会名称
				CorpsEntity findCorpsEntity = corpsManager.findOf_CorpsName(corpsName);
				if(null != findCorpsEntity){
					err = "已存在的麻将馆名称";
					break;
				}
				
				//检查总创建数量
				List<String> myCreates = corpsManager.findOf_CreatePlayer(accountId);
				if(myCreates.size() >= maxNum){
					err = "创建的麻将馆数量已达上限";
					break;
				}				
				String corpsId = corpsIdManager.createCorpsId();
				if(null == corpsId){
					corpsIdManager.extendCorpsIdNum(1);
					corpsId = corpsIdManager.createCorpsId();
					if(null == corpsId){
						err = "系统错误,创建麻将馆失败";
						break;
					}
				}
				
				boolean bOK = false;
				int limitCardNum = corpsCfgManager.getCreateNeedCard();
				//检查并将创建者房卡转移到帮会中
				WalletEntity walletEntity = walletManager.loadOrCreate(accountId);
				walletEntity.lock();
				try{
					int roomCard = walletEntity.getRoomCard();
					if(roomCard >= limitCardNum){
						walletEntity.setRoomCard(roomCard-limitCardNum);
						bOK = true;
					}
				}finally{
					walletEntity.unLock();
				}
				
				if(bOK == false){
					err = "创建麻将馆需要房卡" +limitCardNum + "张";
					break;
				}
				//通知房卡数量变化
				walletManager.sendWalletNotify(accountId);
				
				//创建帮会实体,设置帮会房卡数
				CorpsEntity createCorpsEntity = corpsManager.loadOrCreate(corpsId, corpsName, accountId);
				createCorpsEntity.setRoomCard(limitCardNum);
				createCorpsEntity.setWxNO(wxNO);
				
				//设置玩家加入帮会列表
				CorpsMemberAttrib corpsMemberAttrib = new CorpsMemberAttrib();
				corpsMemberAttrib.joinTime = createCorpsEntity.getCreateTime();
				
				PlayerCorpsEntity playerCorpsEntity = playerCorpsManager.loadOrCreate(accountId);
				Map<String, CorpsMemberAttrib> corpsList = playerCorpsEntity.getCorpsDataList();
				corpsList.put(corpsId, corpsMemberAttrib);
				playerCorpsEntity.setCorpsDataList(corpsList);
				
				Map<String, String> joinedCorpsList = playerCorpsEntity.getJoinedCorpsList();
				joinedCorpsList.put(corpsId, corpsId);
				playerCorpsEntity.setJoinedCorpsList(joinedCorpsList);
				
				//上报帮会创建事件
				EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_CORPS);
				Integer cmd = 1;
				eventAttrib.addEventParam(cmd);
				eventAttrib.addEventParam(corpsId);
				eventAttrib.addEventParam(corpsName);
				eventAttrib.addEventParam(createCorpsEntity.getWxNO());
				eventAttrib.addEventParam(accountEntity.getStarNO());
				eventAttrib.addEventParam("1");
				eventAttrib.addEventParam(String.valueOf(createCorpsEntity.getRoomCard()));
				eventAttrib.addEventParam(String.valueOf(createCorpsEntity.getCreateTime()));
				
				eventTriggerManager.triggerEvent(eventAttrib);
				
				err = "";
			}while(false);
		}finally{
			corpsManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}
	
	@Override
	public Object corps_corps_search(Long accountId, String query){
		if(null == query || query.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		CorpsSearchVo retVo = new CorpsSearchVo(); 
		String err = "接口参数错误";
		corpsManager.lock();
		try{
			do{
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsName(query);
				if(null == corpsEntity){
					err = "未找到指定麻将馆信息";
					break;
				}
				retVo.corpsId = corpsEntity.getCorpsId(); 
				retVo.corpsName = corpsEntity.getCorpsName();
				retVo.memberNum = corpsEntity.getMemberList().size();
				
				err = "";
			}while(false);
			
		}finally{
			corpsManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess(retVo);
	}
	
	@Override
	public Object corps_quest_join(Long accountId, String corpsId, String reson) {
		if(null == corpsId || corpsId.isEmpty() || null == reson || reson.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		String err = "接口参数错误";
		corpsManager.lock();
		try{
			do{				
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
				if(null == corpsEntity){
					err = "不存在的麻将馆";
					break;
				}
				
				if(corpsEntity.getCorpsState() < 0){
					err = "麻将馆已被禁用,无法申请加入";
					break;
				}
				
				//检查黑名单
				Map<Long, Long> blackList = corpsEntity.getBlackList();
				if(blackList.containsKey(accountId)){
					//在黑名单中,直接返回成功
					err = "";
					break;
				}
				
				Map<Long, Long> memberList = corpsEntity.getMemberList();
				//检查是否已加入帮会
				if(memberList.containsKey(accountId)){
					err = "这是您自己的麻将馆,不用加入";
					break;
				}
				//检查成员数量上限
				if(memberList.size() >= corpsCfgManager.getMaxMemberNum()){
					err = "麻将馆成员数量已达上限,无法申请加入";
					break;
				}
				
				//检查加入申请数量上限
				Map<Long, CorpsJoinAttrib> joinList = corpsEntity.getJoinList();
				if(joinList.size() >= corpsCfgManager.getMaxQuestJionNum()){
					err = "麻将馆申请加入数量已达上限,无法申请加入";
					break;
				}
				
				long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
				CorpsJoinAttrib joinAttrib = joinList.get(accountId);
				if(null == joinAttrib){
					joinAttrib = new CorpsJoinAttrib();
				}
				joinAttrib.accountId = accountId;
				joinAttrib.joinTime = currTime;
				joinAttrib.joinReason = reson;
				joinList.put(accountId, joinAttrib);
				corpsEntity.setJoinList(joinList);
				
				if(hotPromptManager.updateHotPrompt(corpsEntity.getCreatePlayer(), HotPromptDefine.HOT_KEY_CORPS) > 0){
					hotPromptManager.notifyHotPrompt(corpsEntity.getCreatePlayer());
				}
				err = "";
			}while(false);
		}finally{
			corpsManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}
	
	@Override
	public Object corps_corps_givecard(Long accountId, String corpsId, int cardNum){
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		if(cardNum <= 0){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "捐赠房卡数量错误", null);
		}
		
		String err = "接口参数错误";
		corpsManager.lock();
		try{
			do{
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
				if(null == corpsEntity){
					err = "麻将馆不存在";
					break;
				}
				
				if(corpsEntity.getMemberList().containsKey(accountId) == false){
					err = "非麻将馆成员无法捐赠房卡";
					break;
				}
				
				boolean bOK = false;
				WalletEntity walletEntity = walletManager.loadOrCreate(accountId);
				walletEntity.lock();
				try{
					int num = walletEntity.getRoomCard();
					if(num >= cardNum){
						walletEntity.setRoomCard(num - cardNum);
						bOK = true;
					}
				}finally{
					walletEntity.unLock();
				}
				if(bOK == false){
					err = "拥有的房卡数量不足,无法捐赠房卡";
					break;
				}
				
				long roomCard = corpsEntity.getRoomCard() + cardNum;
				corpsEntity.setRoomCard(roomCard);
				walletManager.sendWalletNotify(accountId);
				
				PlayerCorpsEntity playerCorpsEntity = playerCorpsManager.loadOrCreate(accountId);
				Map<String, CorpsMemberAttrib> corpsList = playerCorpsEntity.getCorpsDataList();
				CorpsMemberAttrib corpsMemberAttrib = corpsList.get(corpsId);
				if(null != corpsMemberAttrib){
					corpsMemberAttrib.giveCardNum += cardNum;
					corpsList.put(corpsId, corpsMemberAttrib);
					playerCorpsEntity.setCorpsDataList(corpsList);
				}
				
				//上报房卡事件
				EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_CORPS_CARD);
				Integer cmd = 2;
				eventAttrib.addEventParam(cmd);
				eventAttrib.addEventParam(corpsId);
				eventAttrib.addEventParam(accountEntity.getStarNO());
				eventAttrib.addEventParam(String.valueOf(cardNum));
				eventAttrib.addEventParam(String.valueOf(eventAttrib.eventTime));
				
				eventTriggerManager.triggerEvent(eventAttrib);
				
				//发送帮会房卡变化通知
				corpsManager.sendCorpsWalletNotify(corpsId, roomCard);
				
				err = "";
			}while(false);
		}finally{
			corpsManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		
		return Result.valueOfSuccess();
	}

	@Override
	public Object corps_table_list(Long accountId, String corpsId) {
		CorpsTableVo retVo = new CorpsTableVo();
		String err = "接口参数错误";
		corpsManager.lock();
		try{
			do{
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
				if(null == corpsEntity){
					err = "麻将馆不存在";
					break;
				}
				retVo.corpsNotice = corpsEntity.getCorpsNotice();
				retVo.cardNum = String.valueOf(corpsEntity.getRoomCard());
				retVo.corpsId = corpsId;
				err = "";
			}while(false);
			
		}finally{
			corpsManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		
		logicManager.lock();
		try{
			List<TableAttrib> tables = logicManager.findCorpsTables(corpsId);
			for(TableAttrib table : tables){
				String gameName = "unkwon";
				RoomItemConfig itemCfg = majiangCfgManager.getRoomItemCfg(table.cfgId);
				if(null != itemCfg){
					gameName = itemCfg.getItemName();
				}
				retVo.addTable(table, gameName);
			}
		}finally{
			logicManager.unLock();
		}
		sencePlayerManager.joinSence(SencePlayerManager.SENCE_CORPS, corpsId, accountId);
		
		return Result.valueOfSuccess(retVo);
	}		
	
	@Override
	public Object corps_member_list(Long accountId, String corpsId, int type) {
		if(type < 1 || type > 3){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}		
		
		String err = "接口参数错误";
		CorpsMemberVo retVo = new CorpsMemberVo();
		
		corpsManager.lock();
		try{
			do{
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
				if(null == corpsEntity){
					err = "麻将馆不存在";
					break;
				}
				
				switch(type){
				case 1://成员
					{
						Set<Long> memberList = corpsEntity.getMemberList().keySet();
						for(long memberId : memberList){
							AccountEntity accountEntity = accountManager.load(memberId);
							if(null == accountEntity){
								continue;
							}
							int isOwner = 0;
							if(corpsEntity.getCreatePlayer() == memberId){
								isOwner = 1;
							}
							String reson = "";
							retVo.addItem(memberId, accountEntity.getNick(), accountEntity.getHeadImg(), reson, isOwner);
						}
					}
					break;
				case 2://申请列表
					{
						//只有馆主才能操作
						if(corpsEntity.getCreatePlayer() != accountId.longValue()){
							err = "无麻将馆操作权限";
							break;
						}
						
						Collection<CorpsJoinAttrib> joinList = corpsEntity.getJoinList().values();
						for(CorpsJoinAttrib corpsJoinAttrib : joinList){
							AccountEntity accountEntity = accountManager.load(corpsJoinAttrib.accountId);
							if(null == accountEntity){
								continue;
							}
							int isOwner = 0;
							String reson = corpsJoinAttrib.joinReason;
							retVo.addItem(corpsJoinAttrib.accountId, accountEntity.getNick(), accountEntity.getHeadImg(), reson, isOwner);
						}
					}
					break;
				case 3://黑名单
					{
						//只有馆主才能操作
						if(corpsEntity.getCreatePlayer() != accountId.longValue()){
							err = "无麻将馆操作权限";
							break;
						}
						
						Set<Long> blackList = corpsEntity.getBlackList().keySet();
						for(long memberId : blackList){
							AccountEntity accountEntity = accountManager.load(memberId);
							if(null == accountEntity){
								continue;
							}
							int isOwner = 0;
							String reson = "";
							retVo.addItem(memberId, accountEntity.getNick(), accountEntity.getHeadImg(), reson, isOwner);
						}
					}
					break;
				}			
								
				err = "";
			}while(false);
		}finally{
			corpsManager.unLock();
		}
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object corps_kick_member(Long accountId, String corpsId, String kickAccountId, int type) {
		if(null == corpsId || corpsId.isEmpty() 
				|| null == kickAccountId || kickAccountId.isEmpty()
				|| type < 0 || type > 1){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		String err = "接口参数错误";
		corpsManager.lock();
		try{
			do{				
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
				if(null == corpsEntity){
					err = "麻将馆不存在";
					break;
				}
				//馆主才能操作
				if(corpsEntity.getCreatePlayer() != accountId.longValue()){
					err = "无麻将馆操作权限";
					break;
				}
				long nKickId = Long.parseLong(kickAccountId);
				if(nKickId == accountId.longValue()){
					err = "不能踢出麻将馆馆主";
					break;
				}
				
				AccountEntity kickAccountEntity = accountManager.load(nKickId);
				if(null == kickAccountEntity){
					err = "未到指定玩家信息";
					break;
				}
				
				//成员列表删除玩家
				Map<Long, Long> memberList = corpsEntity.getMemberList();
				memberList.remove(nKickId);
				corpsEntity.setMemberList(memberList);
				
				//从玩家已加入的帮会列表中删除帮会
				PlayerCorpsEntity playerCorps = playerCorpsManager.loadOrCreate(nKickId);
				Map<String, CorpsMemberAttrib> corpsDataList = playerCorps.getCorpsDataList();
				corpsDataList.remove(corpsId);
				playerCorps.setCorpsDataList(corpsDataList);
				
				Map<String, String> joinedCorpsList = playerCorps.getJoinedCorpsList();
				joinedCorpsList.remove(corpsId);
				playerCorps.setJoinedCorpsList(joinedCorpsList);
				
				
				if(type == 1){
					//加入帮会黑名单
					Map<Long, Long> blackList = corpsEntity.getBlackList();
					blackList.put(nKickId, nKickId);
					corpsEntity.setBlackList(blackList);
				}
				
				//推送成员退群通知
				corpsManager.sendCorpsKickNotify(nKickId, corpsId, corpsEntity.getCreatePlayer(), corpsEntity.getCorpsName());
				
				//上报帮会成员事件
				EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_CORPS_MEMBER);
				Integer cmd = 2;
				eventAttrib.addEventParam(cmd);
				eventAttrib.addEventParam(corpsId);
				eventAttrib.addEventParam(kickAccountEntity.getStarNO());
				eventAttrib.addEventParam(String.valueOf(eventAttrib.eventTime));
				eventTriggerManager.triggerEvent(eventAttrib);
				
				sencePlayerManager.leaveSence(SencePlayerManager.SENCE_CORPS, corpsId, accountId);
				err = "";
			}while(false);
		}finally{
			corpsManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return corps_member_list(accountId, corpsId, 1);
	}
	
	@Override
	public Object corps_quest_join_bt(Long accountId, String corpsId, String btAccountId, int bt, String reson) {
		if(bt<0 || bt>1 || null==corpsId || corpsId.isEmpty()
				|| null==btAccountId || btAccountId.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		String err = "接口参数错误";
		corpsManager.lock();
		try{
			do{
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
				if(null == corpsEntity){
					err = "麻将馆不存在";
					break;
				}
				//检查馆主才能操作
				if(corpsEntity.getCreatePlayer() != accountId.longValue()){
					err = "无麻将馆操作权限";
					break;
				}
				
				long nBtAccountId = Long.parseLong(btAccountId);
				Map<Long, CorpsJoinAttrib> joinList = corpsEntity.getJoinList();
				if(bt == 0){
					List<Long> needJoinedPlayers = new ArrayList<>();
					//拒绝加入
					if(nBtAccountId == 0){
						//全部拒绝
						needJoinedPlayers.addAll(joinList.keySet());
						joinList.clear();
						corpsEntity.setJoinList(joinList);
					}else{
						//单个拒绝
						needJoinedPlayers.add(nBtAccountId);
						joinList.remove(nBtAccountId);
						corpsEntity.setJoinList(joinList);
					}
					//发送邮件
					for(long notifyAccountId : needJoinedPlayers){
						MailEntity entry = mailManager.loadOrCreate(notifyAccountId);
						entry.lock();
						try{
							MailAttrib mailAttrib = new MailAttrib();
							mailAttrib.sender = 0;
							mailAttrib.title = "申请拒绝";
							mailAttrib.content = "您加入 " + corpsEntity.getCorpsName() + " 麻将馆的申请被拒绝,拒绝理由:" + reson;
							mailAttrib.recvTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
							mailAttrib.read = 0;
							
							entry.addNewMail(mailAttrib, mailManager.getMailMaxCfg());
						}finally{
							entry.unLock();
						}
						//检查推送红点消息
						if(hotPromptManager.updateHotPrompt(notifyAccountId, HotPromptDefine.HOT_KEY_MAIL) > 0){
							hotPromptManager.notifyHotPrompt(notifyAccountId);
						}
					}
				}else{
					//同意加入
					Map<Long, Long> memberList = corpsEntity.getMemberList();
					if(memberList.size() >= corpsCfgManager.getMaxMemberNum()){
						err = "麻将馆成员已达上限";
						break;
					}
					
					List<Long> needJoinedPlayers = new ArrayList<>(); 
					if(nBtAccountId == 0){
						//全部同意
						needJoinedPlayers.addAll(joinList.keySet());
						joinList.clear();
					}else{
						//单个同意
						needJoinedPlayers.add(nBtAccountId);
						joinList.remove(nBtAccountId);
					}
					
					corpsEntity.setJoinList(joinList);
					for(Long joinPlayer : needJoinedPlayers){
						memberList.put(joinPlayer, joinPlayer);
						
						PlayerCorpsEntity playerCorps = playerCorpsManager.loadOrCreate(joinPlayer);
						Map<String, CorpsMemberAttrib> corpsDataList = playerCorps.getCorpsDataList();
						CorpsMemberAttrib corpsMemberAttrib = corpsDataList.get(corpsId);
						if(null == corpsMemberAttrib){
							corpsMemberAttrib = new CorpsMemberAttrib();
						}
						corpsMemberAttrib.joinTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
						corpsDataList.put(corpsId, corpsMemberAttrib);
						playerCorps.setCorpsDataList(corpsDataList);
						
						Map<String, String> joinedCorpsList = playerCorps.getJoinedCorpsList();
						joinedCorpsList.put(corpsId, corpsId);
						playerCorps.setJoinedCorpsList(joinedCorpsList);
						
						//推送成员添加通知
						if(sessionManager.isOnline(joinPlayer)){
							corpsManager.sendCorpsAddNotify(joinPlayer, corpsId, corpsEntity.getCreatePlayer(), corpsEntity.getCorpsName(), 1);
						}
						
						String starNO = "";
						AccountEntity accountEntity = accountManager.load(joinPlayer);
						if(null != accountEntity){
							starNO = accountEntity.getStarNO();
						}
						
						//上报帮会成员事件
						EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_CORPS_MEMBER);
						Integer cmd = 1;
						eventAttrib.addEventParam(cmd);
						eventAttrib.addEventParam(corpsId);
						eventAttrib.addEventParam(starNO);
						eventAttrib.addEventParam(String.valueOf(eventAttrib.eventTime));
						eventTriggerManager.triggerEvent(eventAttrib);
					}
					needJoinedPlayers = null;
					corpsEntity.setMemberList(memberList);
				}
				//处理红点
				long hotVal = joinList.size();
				hotPromptManager.setHotPromptAttrib(corpsEntity.getCreatePlayer(), HotPromptDefine.HOT_KEY_CORPS, hotVal);
				hotPromptManager.notifyHotPrompt(corpsEntity.getCreatePlayer());
				
				err = "";
			}while(false);
		}finally{
			corpsManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return corps_member_list(accountId, corpsId, 2);
	}
	
	@Override
	public Object corps_member_blacklist_unlock(Long accountId, String corpsId, String btAccountId) {
		if(null == corpsId || corpsId.isEmpty() || null == btAccountId || btAccountId.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		String err = "接口参数错误";
		corpsManager.lock();
		try{
			do{
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
				if(null == corpsEntity){
					err = "麻将馆不存在";
					break;
				}
				
				long nBtAccountId = Long.parseLong(btAccountId);
				Map<Long, Long> blackList = corpsEntity.getBlackList();
				blackList.remove(nBtAccountId);
				corpsEntity.setBlackList(blackList);
				
				err = "";
			}while(false);
		}finally{
			corpsManager.unLock();
		}
		
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return corps_member_list(accountId, corpsId, 3);
	}

	@Override
	public Object corps_set_roomcard_state(Long accountId, String corpsId, int state) {
		if(state != 0 && state != 1 || null==corpsId || corpsId.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		CorpsStateVo retVo = new CorpsStateVo();
		String err = "接口参数错误";
		corpsManager.lock();
		try{
			do{
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
				if(null == corpsEntity){
					err = "麻将馆不存在";
					break;
				}
				
				//检查馆主才能操作
				if(corpsEntity.getCreatePlayer() != accountId.longValue()){
					err = "无麻将馆操作权限";
					break;
				}
				corpsEntity.setRoomCardState(state);
				
				retVo.corpsId = corpsId;
				retVo.roomCardState = state;
				retVo.hidde = corpsEntity.getHidde();
				
				err = "";
			}while(false);
		}finally{
			corpsManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		
		return Result.valueOfSuccess(retVo);
	}
	
	@Override
	public Object corps_zhuanrang_player(Long accountId, String corpsId, String btStarNO){
		if(null == corpsId || corpsId.isEmpty() || null==btStarNO || btStarNO.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		CorpsMemberNotifyVo retVo = new CorpsMemberNotifyVo();
		String err = "接口参数错误";
		corpsManager.lock();
		try{
			do{
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
				if(null == corpsEntity){
					err = "麻将馆不存在";
					break;
				}
				
				//检查馆主才能操作
				if(corpsEntity.getCreatePlayer() != accountId.longValue()){
					err = "无麻将馆操作权限";
					break;
				}
				
				AccountEntity newAccountEntity = accountManager.findOf_starNO(btStarNO);
				if(null == newAccountEntity){
					err = "转让玩家不存在";
					break;
				}
				
				AccountEntity oldAccountEntity = accountManager.load(accountId);
				if(null == oldAccountEntity){
					err = "转让玩家不存在";
					break;
				}
				
				//检查不能转让给自已
				if(newAccountEntity.getId().longValue() == accountId.longValue()){
					err = "麻将馆不能转让给自已";
					break;
				}
								
				//检查只能转让给帮会的成员,且转让后玩家创建的帮会不能大于数量限制
				if(corpsEntity.getMemberList().containsKey(newAccountEntity.getId()) == false){
					err = "只能转让给麻将馆成员";
					break;
				}
				
				List<String> corpsIdList = corpsManager.findOf_CreatePlayer(newAccountEntity.getId());
				if((corpsIdList.size() + 1) > corpsCfgManager.getMaxCreateNum()){
					err = "被转让的玩家已拥有麻将馆已达上限,无法再转让";
					break;
				}
				
				//检查帮会排行榜数据重置
				CoolTimeResult coolTimeResult = resetTimeManager.checkPlayerResetTime(corpsEntity.getCreatePlayer(), 
						CoolTimeConfigType.PLAYERTIME_RESET_RANK, true);
				if(coolTimeResult.bReset){
					//需要重置排行榜数据
					Set<Long> memberList = corpsEntity.getMemberList().keySet();
					for(Long memberId : memberList){
						PlayerCorpsEntity playerCorpsEntity = playerCorpsManager.loadOrCreate(memberId);
						Map<String, CorpsMemberAttrib> corpsDataList = playerCorpsEntity.getCorpsDataList();
						CorpsMemberAttrib corpsMemberAttrib = corpsDataList.get(corpsId);
						if(null != corpsMemberAttrib){
							corpsMemberAttrib.reset();
							corpsDataList.put(corpsId, corpsMemberAttrib);
							playerCorpsEntity.setCorpsDataList(corpsDataList);
						}
					}
				}
				resetTimeManager.checkPlayerResetTime(newAccountEntity.getId(), 
						CoolTimeConfigType.PLAYERTIME_RESET_RANK, true);
				
				corpsEntity.setCreatePlayer(newAccountEntity.getId());
				
				retVo.corpsId = corpsId;
				retVo.createPlayer = String.valueOf(newAccountEntity.getId());
								
				//上报帮会事件
				String oldCreateStarNO = oldAccountEntity.getStarNO();
				EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_CORPS);
				Integer cmd = 3;
				eventAttrib.addEventParam(cmd);
				eventAttrib.addEventParam(corpsId);
				eventAttrib.addEventParam(oldCreateStarNO);
				eventAttrib.addEventParam(btStarNO);
				eventAttrib.addEventParam(String.valueOf(eventAttrib.eventTime));
				eventTriggerManager.triggerEvent(eventAttrib);				
				
				//发送转让通知
				corpsManager.sendCorpsZhuanRangNotify(newAccountEntity.getId(), corpsId, corpsEntity.getCorpsName());
				
				err = "";
			}while(false);
		}finally{
			corpsManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		
		return Result.valueOfSuccess(retVo);
	}
	
	@Override
	public Object corps_destory(Long accountId, String corpsId) {
		if(null == corpsId || corpsId.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆不存在", null);
		}
		
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		
		String err = "接口参数错误";
		corpsManager.lock();
		try{
			do{
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
				if(null == corpsEntity){
					err = "麻将馆不存在";
					break;
				}
				
				//检查只有馆主才能操作
				if(corpsEntity.getCreatePlayer() != accountId.longValue()){
					err = "无麻将馆操作权限";
					break;
				}
				
				//删除所有群成员
				Set<Long> memberList = corpsEntity.getMemberList().keySet();
				for(long memberId : memberList){
					PlayerCorpsEntity playerCorps = playerCorpsManager.loadOrCreate(memberId);
					Map<String, CorpsMemberAttrib> corpsDataList = playerCorps.getCorpsDataList();
					corpsDataList.remove(corpsId);
					playerCorps.setCorpsDataList(corpsDataList);
					
					Map<String, String> joinedCorpsList = playerCorps.getJoinedCorpsList();
					joinedCorpsList.remove(corpsId);
					playerCorps.setJoinedCorpsList(joinedCorpsList);
				}
				//发送群解散消息
				corpsManager.sendCorpsDestoryNotify(memberList, corpsId, corpsEntity.getCorpsName());
				//清除成员
				memberList.clear();
				
				//上报帮会操作事件
				EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_CORPS);
				Integer cmd = 2;
				eventAttrib.addEventParam(cmd);
				eventAttrib.addEventParam(corpsId);
				eventAttrib.addEventParam(accountEntity.getStarNO());
				eventAttrib.addEventParam(String.valueOf(eventAttrib.eventTime));
				eventTriggerManager.triggerEvent(eventAttrib);
				
				//删除帮会
				corpsManager.removeCorps(corpsId);
				
				sencePlayerManager.delMainSence(SencePlayerManager.SENCE_CORPS);
				err = "";
			}while(false);
		}finally{
			corpsManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object corps_exit(Long accountId, String corpsId) {
		if(null == corpsId || corpsId.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆不存在", null);
		}
		
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		
		String err = "接口参数错误";
		corpsManager.lock();
		try{
			do{
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
				if(null == corpsEntity){
					err = "麻将馆不存在";
					break;
				}
				Map<Long, Long> memberList = corpsEntity.getMemberList();
				if(corpsEntity.getCreatePlayer() == accountId.longValue()){
					//馆主退出了,解散麻将馆
					Set<Long> memberIds = memberList.keySet();
					for(long memberId : memberIds){
						PlayerCorpsEntity playerCorps = playerCorpsManager.loadOrCreate(memberId);
						Map<String, CorpsMemberAttrib> corpsDataList = playerCorps.getCorpsDataList();
						corpsDataList.remove(corpsId);
						playerCorps.setCorpsDataList(corpsDataList);
						
						Map<String, String> joinedCorpsList = playerCorps.getJoinedCorpsList();
						joinedCorpsList.remove(corpsId);
						playerCorps.setJoinedCorpsList(joinedCorpsList);
					}
					//发送麻将馆解散消息
					corpsManager.sendCorpsDestoryNotify(memberIds, corpsId, corpsEntity.getCorpsName());
					memberList.clear();
					
					//上报帮会操作事件
					EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_CORPS);
					Integer cmd = 2;
					eventAttrib.addEventParam(cmd);
					eventAttrib.addEventParam(corpsId);
					eventAttrib.addEventParam(accountEntity.getStarNO());
					eventAttrib.addEventParam(String.valueOf(eventAttrib.eventTime));
					eventTriggerManager.triggerEvent(eventAttrib);
					
					//删除帮会
					corpsManager.removeCorps(corpsId);
					
					sencePlayerManager.delMainSence(SencePlayerManager.SENCE_CORPS);
				}else{
					//单人退出
					PlayerCorpsEntity playerCorps = playerCorpsManager.loadOrCreate(accountId);
					Map<String, CorpsMemberAttrib> corpsDataList = playerCorps.getCorpsDataList();
					corpsDataList.remove(corpsId);
					playerCorps.setCorpsDataList(corpsDataList);
					
					Map<String, String> joinedCorpsList = playerCorps.getJoinedCorpsList();
					joinedCorpsList.remove(corpsId);
					playerCorps.setJoinedCorpsList(joinedCorpsList);
					
					memberList.remove(accountId);
					corpsEntity.setMemberList(memberList);
					
					sencePlayerManager.leaveSence(SencePlayerManager.SENCE_CORPS, corpsId, accountId);
					//上报帮会成员事件
					EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_CORPS_MEMBER);
					Integer cmd = 3;
					eventAttrib.addEventParam(cmd);
					eventAttrib.addEventParam(corpsId);
					eventAttrib.addEventParam(accountEntity.getStarNO());
					eventAttrib.addEventParam(String.valueOf(eventAttrib.eventTime));
					eventTriggerManager.triggerEvent(eventAttrib);
				}
				
				err = "";
			}while(false);
		}finally{
			corpsManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}
	
	@Override
	public Object corps_modfiy_notice(Long accountId, String corpsId, String notice){
		if(null == corpsId || corpsId.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆不存在", null);
		}
		
		String err = "接口参数错误";
		corpsManager.lock();
		try{
			do{
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
				if(null == corpsEntity){
					err = "麻将馆不存在";
					break;
				}
				
				//检查只有馆主才能操作
				if(corpsEntity.getCreatePlayer() != accountId.longValue()){
					err = "无麻将馆操作权限";
					break;
				}
				
				if(null == notice){
					notice = "";
				}
				if(notice.isEmpty() == false){
					String retStr = commCfgManager.checkForbiddenWord(notice, false);
					if(null == retStr){
						//有敏感字符
						err = "麻将馆公告内容包含非法字符";
						break;
					}
				}				
				corpsEntity.setCorpsNotice(notice);
				
				corpsManager.sendCorpsModfiyNotify(corpsId, corpsEntity.getCorpsNotice(), corpsEntity.getWxNO());
				err = "";
			}while(false);
		}finally{
			corpsManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}
	
	@Override
	public Object corps_modfiy_wxno(Long accountId, String corpsId, String wxno){
		if(null == corpsId || corpsId.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆不存在", null);
		}
		
		String err = "接口参数错误";
		corpsManager.lock();
		try{
			do{
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
				if(null == corpsEntity){
					err = "麻将馆不存在";
					break;
				}
				
				//检查只有馆主才能操作
				if(corpsEntity.getCreatePlayer() != accountId.longValue()){
					err = "无麻将馆操作权限";
					break;
				}
				
				if(null == wxno){
					wxno = "";
				}
				corpsEntity.setWxNO(wxno);
				
				corpsManager.sendCorpsModfiyNotify(corpsId, corpsEntity.getCorpsNotice(), corpsEntity.getWxNO());
				
				//提交帮会微信号修改事件
				EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_CORPS);
				int cmd = 4;
				eventAttrib.addEventParam(cmd);
				eventAttrib.addEventParam(corpsId);
				eventAttrib.addEventParam(wxno);
				eventAttrib.addEventParam(String.valueOf(eventAttrib.eventTime));
				eventTriggerManager.triggerEvent(eventAttrib);
				
				err = "";
			}while(false);
		}finally{
			corpsManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess();
	}
	
	@Override
	public Object corps_modfiy_hidde(Long accountId, String corpsId, int hidde){
		if(null==corpsId || corpsId.isEmpty() || hidde < 0 || hidde > 1){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		String err = "接口参数错误";
		CorpsStateVo retVo = new CorpsStateVo();
		corpsManager.lock();
		try{
			do{
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
				if(null == corpsEntity){
					err = "麻将馆不存在";
					break;
				}
				
				//检查只有馆主才能操作
				if(corpsEntity.getCreatePlayer() != accountId.longValue()){
					err = "无麻将馆操作权限";
					break;
				}
				
				corpsEntity.setHidde(hidde);
				
				retVo.corpsId = corpsId;
				retVo.roomCardState = corpsEntity.getRoomCardState();
				retVo.hidde = hidde;
				
				err = "";
			}while(false);
		}finally{
			corpsManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		return Result.valueOfSuccess(retVo);
	}
	
	@Override
	public Object corps_member_rankinfo(Long accountId, String corpsId, int type){
		if(null==corpsId || corpsId.isEmpty() || type<1 || type>3){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		CorpsRankVo retVo = new CorpsRankVo();
		String err = "接口参数错误";
		corpsManager.lock();
		try{
			do{
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
				if(null == corpsEntity){
					err = "麻将馆不存在";
					break;
				}
				
				//检查排行榜重置数据
				CoolTimeResult coolTimeResult = resetTimeManager.checkPlayerResetTime(corpsEntity.getCreatePlayer(), 
						CoolTimeConfigType.PLAYERTIME_RESET_RANK, true);
				
				Map<Long, Long> memberList = corpsEntity.getMemberList();
				Set<Long> memberIds = memberList.keySet();
				for(Long memberId : memberIds){
					PlayerCorpsEntity playerCorpsEntity = playerCorpsManager.loadOrCreate(memberId);
					Map<String, CorpsMemberAttrib> corpsDataList = playerCorpsEntity.getCorpsDataList();
					CorpsMemberAttrib corpsMemberAttrib = corpsDataList.get(corpsId);
					if(null == corpsMemberAttrib){
						continue;
					}
					if(coolTimeResult.bReset){
						corpsMemberAttrib.reset();
						corpsDataList.put(corpsId, corpsMemberAttrib);
						playerCorpsEntity.setCorpsDataList(corpsDataList);
					}
					
					AccountEntity accountEntity = accountManager.load(memberId);
					if(null == accountEntity){
						continue;
					}					
					
					String winMaxRate = "0.00";
					
					if(corpsMemberAttrib.activeValue > 0){
						float rate = corpsMemberAttrib.winMaxNum*100.0f;
						rate = rate/corpsMemberAttrib.activeValue;
						BigDecimal b = new BigDecimal(String.format("%f", rate));
						DecimalFormat d1 = new DecimalFormat("0.00");
						winMaxRate = d1.format(b);
					}					
					
					retVo.addItem(accountEntity.getNick(), accountEntity.getHeadImg(), 
							corpsMemberAttrib.gameRoundNum, corpsMemberAttrib.score, corpsMemberAttrib.activeValue, 
							corpsMemberAttrib.giveCardNum, winMaxRate);
				}
				
				err = "";
			}while(false);
		}finally{
			corpsManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		
		//排名
		retVo.sort(type);
		
		return Result.valueOfSuccess(retVo);
	}
	
	@Override
	public Object corps_get_corps_detailed(Long accountId, String corpsId) {
		if(null == corpsId || corpsId.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
		if(null == corpsEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆不存在", null);
		}
		CorpsDetailedVo retVo = new CorpsDetailedVo();
		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object corps_leave_sence(Long accountId, String corpsId) {
		sencePlayerManager.leaveSence(SencePlayerManager.SENCE_CORPS, corpsId, accountId);
		return Result.valueOfSuccess();
	}

	@Override
	public Object corps_yaoqing_join(Long accountId, String corpsId, String btStarNO) {
		if(null == corpsId || corpsId.isEmpty()
				|| null == btStarNO || btStarNO.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		AccountEntity accountEntity = accountManager.findOf_starNO(btStarNO);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "未找到指定邀请的玩家信息", null);
		}
		String err = "接口参数错误";
		corpsManager.lock();
		try{
			do{
				CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
				if(null == corpsEntity){
					err = "麻将馆不存在";
					break;
				}
				//检查是否帮主
				if(corpsEntity.getCreatePlayer() != accountId.longValue()){
					err = "无麻将馆操作权限";
					break;
				}
				//检查是否帮会成员
				Map<Long, Long> memberList = corpsEntity.getMemberList();
				if(memberList.containsKey(accountEntity.getId())){
					err = "玩家已经是麻将馆成员";
					break;
				}
				//检查帮会成员数量上限
				if(memberList.size() >= corpsCfgManager.getMaxMemberNum()){
					err = "麻将馆成员数量已达上限,无法邀请加入";
					break;
				}
				//检查黑名单
				Map<Long, Long> blackList = corpsEntity.getBlackList();
				if(blackList.containsKey(accountEntity.getId())){
					err = "玩家已加入麻将馆黑名单,无法邀请加入";
					break;
				}
				//添加到成员列表中
				memberList.put(accountEntity.getId(), accountEntity.getId());
				corpsEntity.setMemberList(memberList);
				
				//删除申请记录
				Map<Long, CorpsJoinAttrib> joinList = corpsEntity.getJoinList();
				joinList.remove(accountEntity.getId());
				corpsEntity.setJoinList(joinList);
				
				//添加帮会加入记录
				PlayerCorpsEntity playerCorpsEntity = playerCorpsManager.loadOrCreate(accountEntity.getId());
				Map<String, String> joinedCorpsList = playerCorpsEntity.getJoinedCorpsList();
				joinedCorpsList.put(corpsId, corpsId);
				playerCorpsEntity.setJoinedCorpsList(joinedCorpsList);
				
				Map<String, CorpsMemberAttrib> corpsDataList = playerCorpsEntity.getCorpsDataList();
				CorpsMemberAttrib corpsMemberAttrib = corpsDataList.get(corpsId);
				if(null == corpsMemberAttrib){
					corpsMemberAttrib = new CorpsMemberAttrib();
				}
				corpsMemberAttrib.reset();
				corpsDataList.put(corpsId, corpsMemberAttrib);
				playerCorpsEntity.setCorpsDataList(corpsDataList);
			
				//发送成员添加通知
				corpsManager.sendCorpsAddNotify(accountEntity.getId(), corpsId, accountId, corpsEntity.getCorpsName(), 2);				
				err = "";
			}while(false);
		}finally{
			corpsManager.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		
		return Result.valueOfSuccess();
	}	
}
