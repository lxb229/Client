package com.palmjoys.yf1b.act.hotprompt.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.corps.manager.CorpsManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.framework.utils.MessagePushQueueUtils;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptAttrib;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptDefine;
import com.palmjoys.yf1b.act.mail.manager.MailManager;
import com.palmjoys.yf1b.act.task.manager.TaskManager;

@Component
public class HotPromptManager {
	//KEY=帐号Id,VALUE=热点属性
	private ConcurrentMap<Long, Map<Integer, Long>> hotPromptMap = new ConcurrentHashMap<Long, Map<Integer, Long>>();
	
	@Autowired
	private MailManager mailManager;
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private CorpsManager corpsManager;
	@Autowired
	private TaskManager taskManager;
	
	/**
	 * 获取玩家指定索引热点值
	 * accountId 帐号Id
	 * hotKey 热点索引(见HotPromptDefine.java定义)
	 * */
	public long getHotPromptAttrib(long accountId, int hotKey){
		Map<Integer, Long> hotPromptAttrib = hotPromptMap.get(accountId);
		if(null == hotPromptAttrib){
			return 0;
		}
		Long hotVal = hotPromptAttrib.get(hotKey);
		if(hotVal == null)
			return 0;
		
		return hotVal.longValue();
	}
	
	/**
	 * 设置指定索引热点值
	 * accountId 帐号Id
	 * hotKey 热点索引(见HotPromptDefine.java定义)
	 * hotVal 热点值
	 * */
	public void setHotPromptAttrib(long accountId, int hotKey, long hotVal){
		Map<Integer, Long> hotPromptAttrib = hotPromptMap.get(accountId);
		if(null == hotPromptAttrib){
			hotPromptAttrib = new HashMap<Integer, Long>();
		}
		hotPromptAttrib.put(hotKey, hotVal);
		hotPromptMap.put(accountId, hotPromptAttrib);
	}
		
	/**
	 * 获取玩家热点数据
	 * */
	private List<HotPromptAttrib> getHotPrompt(long accountId){
		List<HotPromptAttrib> retVo = new ArrayList<HotPromptAttrib>();
		long hotVal = this.getHotPromptAttrib(accountId, HotPromptDefine.HOT_KEY_MAIL);
		HotPromptAttrib promptAttrib = new HotPromptAttrib();
		promptAttrib.hotKey = HotPromptDefine.HOT_KEY_MAIL;
		promptAttrib.hotVal = (int) hotVal;
		retVo.add(promptAttrib);
		
		hotVal = this.getHotPromptAttrib(accountId, HotPromptDefine.HOT_KEY_CORPS);
		promptAttrib = new HotPromptAttrib();
		promptAttrib.hotKey = HotPromptDefine.HOT_KEY_CORPS;
		promptAttrib.hotVal = (int) hotVal;
		retVo.add(promptAttrib);
		
		hotVal = this.getHotPromptAttrib(accountId, HotPromptDefine.HOT_KEY_DAYTASK);
		promptAttrib = new HotPromptAttrib();
		promptAttrib.hotKey = HotPromptDefine.HOT_KEY_DAYTASK;
		promptAttrib.hotVal = (int) hotVal;
		retVo.add(promptAttrib);
		
		return retVo;
	}
	
	public long updateHotPrompt(long accountId, int hotKey){
		long newVal = 0;
		long oldVal = this.getHotPromptAttrib(accountId, hotKey);
		switch(hotKey){
		case HotPromptDefine.HOT_KEY_MAIL:
			newVal = mailManager.checkHotPrompt(accountId);
			break;
		case HotPromptDefine.HOT_KEY_CORPS:
			newVal = corpsManager.checkHotPromptOf_CreatePlayer(accountId);
			break;
		case HotPromptDefine.HOT_KEY_DAYTASK:
			newVal = taskManager.checkHotPrompt(accountId);
			break;
		}
		if(newVal != oldVal){
			this.setHotPromptAttrib(accountId, hotKey, newVal);
			return 1;
		}
		return 0;
	}
	
	public void updateHotPromptAll(long accountId){
		this.updateHotPrompt(accountId, HotPromptDefine.HOT_KEY_MAIL);
		this.updateHotPrompt(accountId, HotPromptDefine.HOT_KEY_CORPS);
		this.updateHotPrompt(accountId, HotPromptDefine.HOT_KEY_DAYTASK);
	}
	
	//检测推送红点提示
	public void notifyHotPrompt(long accountId){
		List<HotPromptAttrib> retVo = this.getHotPrompt(accountId);
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(AccountDefine.ACCOUNT_HOTPROMPT_NOTIFIY, 
				Result.valueOfSuccess(retVo).toMap()); 
		MessagePushQueueUtils.getPushQueue(sessionManager).push2(accountId, pushMsg);
	}
	
	public void clearOfCache(long accountId){
		hotPromptMap.remove(accountId);
	}
	
}
