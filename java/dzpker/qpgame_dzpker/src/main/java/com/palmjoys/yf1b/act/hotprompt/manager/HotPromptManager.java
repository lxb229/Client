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

import com.palmjoys.yf1b.act.dzpker.manager.DzpkerOrderManager;
import com.palmjoys.yf1b.act.framework.utils.MessagePushQueueUtils;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptAttrib;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptDefine;
import com.palmjoys.yf1b.act.mail.manager.MailManager;

@Component
public class HotPromptManager {
	//KEY=帐号Id,VALUE=热点属性
	private ConcurrentMap<Long, Map<Integer, Long>> hotPromptMap = new ConcurrentHashMap<Long, Map<Integer, Long>>();
	
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private MailManager mailManager;
	@Autowired
	private DzpkerOrderManager dzpkerOrderManager;
	
	//热点索引定义
	//邮件
	public static final int HOT_KEY_MAIL = 1;
	//房主订单
	public static final int HOT_KEY_ORDER = 2;
		
	
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
	public List<HotPromptAttrib> getHotPrompt(long accountId){
		List<HotPromptAttrib> retVo = new ArrayList<HotPromptAttrib>();
		long hotVal = this.getHotPromptAttrib(accountId, HotPromptManager.HOT_KEY_MAIL);
		HotPromptAttrib promptAttrib = new HotPromptAttrib();
		promptAttrib.hotKey = HotPromptManager.HOT_KEY_MAIL;
		promptAttrib.hotVal = (int) hotVal;
		retVo.add(promptAttrib);
		
		hotVal = this.getHotPromptAttrib(accountId, HotPromptManager.HOT_KEY_ORDER);
		promptAttrib = new HotPromptAttrib();
		promptAttrib.hotKey = HotPromptManager.HOT_KEY_ORDER;
		promptAttrib.hotVal = (int) hotVal;
		retVo.add(promptAttrib);
		
		return retVo;
	}
	
	public void updateHotPrompt(long accountId, int hotKey){
		long hotVal = 0;
		switch(hotKey){
		case HotPromptManager.HOT_KEY_MAIL:
			hotVal = mailManager.checkHotPrompt(accountId);
			this.setHotPromptAttrib(accountId, hotKey, hotVal);
			break;
		case HotPromptManager.HOT_KEY_ORDER:
			hotVal = dzpkerOrderManager.checkHotPrompt(accountId);
			this.setHotPromptAttrib(accountId, hotKey, hotVal);
			break;
		}
	}
	
	public void updateHotPromptAll(long accountId){
		this.updateHotPrompt(accountId, HotPromptManager.HOT_KEY_MAIL);
		this.updateHotPrompt(accountId, HotPromptManager.HOT_KEY_ORDER);
	}
	
	//检测推送红点提示
	public void hotPromptNotity(long accountId){
		List<HotPromptAttrib> retVo = this.getHotPrompt(accountId);
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(HotPromptDefine.HOTPROMPT_COMMAND_HOTDATA_NOTIFIY, 
				Result.valueOfSuccess(retVo)); 
		MessagePushQueueUtils.getPushQueue(sessionManager).push2(accountId, pushMsg);
	}
	
	public void clearOfCache(long accountId){
		hotPromptMap.remove(accountId);
	}
	
}
