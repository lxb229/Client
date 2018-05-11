package com.palmjoys.yf1b.act.reward.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.core.time.DateUtils;

import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;
import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.common.manager.CommonCfgManager;
import com.palmjoys.yf1b.act.framework.common.resource.GameObjectConfig;
import com.palmjoys.yf1b.act.framework.gameobject.manager.GameObjectManager;
import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptDefine;
import com.palmjoys.yf1b.act.mail.entity.MailEntity;
import com.palmjoys.yf1b.act.mail.manager.MailManager;
import com.palmjoys.yf1b.act.mail.model.MailAttrib;
import com.palmjoys.yf1b.act.reward.model.RewardVo;
import com.palmjoys.yf1b.act.reward.model.RewareItemVo;
import com.palmjoys.yf1b.act.task.model.RewareSuccessGmNotifyVo;

@Component
public class RewardManager {
	@Autowired
	private RewardRandManager rewardRandManager;
	@Autowired
	private GameObjectManager gameObjectManager;
	@Autowired
	private CommonCfgManager commonCfgManager;
	@Autowired
	private MailManager mailManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private EventTriggerManager eventTriggerManager;
	@Autowired
	private HotPromptManager hotPromptManager;

	//发放奖励
	public RewardVo rewareGive(long traget, List<GameObject> items, Object reason){
		RewardVo rewardVo = new RewardVo();
		if(null == items || items.isEmpty() || traget <= 0){
			return rewardVo;
		}
		boolean bMailNotify = false;
		List<GameObject> needMailList = new ArrayList<>();
		for(GameObject item : items){
			GameObjectConfig cfg = commonCfgManager.getGameObject(item.code);
			if(null == cfg){
				continue;
			}
			
			String name = cfg.getName();
			int rewardGiveType = cfg.getRewardGiveType();
			if(rewardGiveType == 0){
				//邮件通知，线下领取
				bMailNotify = true;
			}else if(rewardGiveType == 1){
				//游戏内直接发放领取
				if(gameObjectManager.increase(traget, item) >= 0){
					//发放成功
					RewareItemVo itemVo = new RewareItemVo();
					itemVo.code = item.code;
					itemVo.type = item.type;
					itemVo.name = name;
					itemVo.amount = item.amount;
					rewardVo.rewardItems.add(itemVo);
				}
			}else if(rewardGiveType == 2){
				//通过邮件发放,邮件附件领取
				RewareItemVo itemVo = new RewareItemVo();
				itemVo.code = item.code;
				itemVo.type = item.type;
				itemVo.name = name;
				itemVo.amount = item.amount;
				rewardVo.rewardItems.add(itemVo);
				
				needMailList.add(item);
			}
		}
		
		if(null != reason && (needMailList.isEmpty() == false || bMailNotify)){
			MailEntity mailEntity = mailManager.loadOrCreate(traget);
			mailEntity.lock();
			try{
				RewareSuccessGmNotifyVo rewareSuccessGmNotifyVo = (RewareSuccessGmNotifyVo) reason;
				MailAttrib mailAttrib = new MailAttrib();
				mailAttrib.sender = 0;
				mailAttrib.title = rewareSuccessGmNotifyVo.title;
				mailAttrib.content = rewareSuccessGmNotifyVo.content;
				mailAttrib.recvTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
				mailAttrib.read = 0;
				if(needMailList.isEmpty() == false){
					mailAttrib.attachment.addAll(needMailList);
				}
				mailAttrib.attachmentData = rewareSuccessGmNotifyVo.warehouseOut;
				mailAttrib.attachmentVaildTime = rewareSuccessGmNotifyVo.attachmentVaildTime;
				mailEntity.addNewMail(mailAttrib, mailManager.getMailMaxCfg());
			}finally{
				mailEntity.unLock();
			}
			//检查邮件红点
			if(hotPromptManager.updateHotPrompt(traget, HotPromptDefine.HOT_KEY_MAIL) > 0){
				hotPromptManager.notifyHotPrompt(traget);
			}
		}
						
		int roomCard = rewardVo.getRewardNum("WALLET", 10001);
		int goldMoney = rewardVo.getRewardNum("WALLET", 10003);
		int silver = rewardVo.getRewardNum("WALLET", 10004);
		int wish = rewardVo.getRewardNum("WALLET", 10005);
		if(roomCard>0 || goldMoney>0 || silver>0 || wish>0){
			//上报钱包数据变化
			AccountEntity accountEntity = accountManager.load(traget);
			if(null != accountEntity){
				EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_WALLET);
				eventAttrib.addEventParam(accountEntity.getStarNO());
				eventAttrib.addEventParam(roomCard);
				eventAttrib.addEventParam(wish);
				eventAttrib.addEventParam(goldMoney);
				eventAttrib.addEventParam(silver);
				eventTriggerManager.triggerEvent(eventAttrib);
			}
		}
		
		return rewardVo;
	}
	
	//发放奖励
	public RewardVo rewareGive(long traget, String rewareId, Object reason){
		List<GameObject> items = this.getRewares(rewareId);
		return this.rewareGive(traget, items, reason);
	}
	
	//获取奖励列表
	public List<GameObject> getRewares(String ...rewareIds){
		List<GameObject> retObjs = new ArrayList<>();
		if(null == rewareIds){
			return retObjs;
		}
		for(String rewareId : rewareIds){
			List<GameObject> items = rewardRandManager.getRewareItems(rewareId);
			for(GameObject obj : items){
				boolean bfind = false; 
				for(GameObject retObj : retObjs){
					if(retObj.type.equalsIgnoreCase(obj.type)
							&& retObj.code == obj.code){
						//同一个物品
						retObj.amount += obj.amount;
						bfind = true;
						break;
					}
				}
				
				if(bfind == false){
					//新物品
					retObjs.add(obj);
				}
			}
		}
		
		return retObjs;
	}
	
	//转换奖励列表为客户端数据
	public List<RewareItemVo> toRewareItemVoList(List<GameObject> gameObjectList){
		if(null == gameObjectList || gameObjectList.isEmpty()){
			return null;
		}
		List<RewareItemVo> retList = new ArrayList<>();
		for(GameObject gameObject : gameObjectList){
			GameObjectConfig cfg = commonCfgManager.getGameObject(gameObject.code);
			if(null != cfg){
				RewareItemVo itemVo = new RewareItemVo();
				itemVo.amount = gameObject.amount;
				itemVo.name = cfg.getName();
				itemVo.code = gameObject.code;
				itemVo.type = gameObject.type;
				retList.add(itemVo);
			}
		}
		
		return retList;
	}
}
