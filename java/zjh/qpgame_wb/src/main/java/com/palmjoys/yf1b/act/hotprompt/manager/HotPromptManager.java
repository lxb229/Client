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

import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.framework.utils.MessagePushQueueUtils;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptAttrib;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptDefine;
import com.palmjoys.yf1b.act.mail.manager.MailManager;

@Component
public class HotPromptManager {
	//KEY=帐号Id,VALUE=热点属性
	private ConcurrentMap<Long, Map<Integer, Long>> hotPromptMap = new ConcurrentHashMap<Long, Map<Integer, Long>>();
	
	@Autowired
	private MailManager mailManager;
	@Autowired
	private SessionManager sessionManager;
	
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
		long hotVal = getHotPromptAttrib(accountId, HotPromptDefine.HOT_KEY_MAIL);
		HotPromptAttrib promptAttrib = new HotPromptAttrib();
		promptAttrib.hotKey = HotPromptDefine.HOT_KEY_MAIL;
		promptAttrib.hotVal = (int) hotVal;
		retVo.add(promptAttrib);
		return retVo;
	}
	
	//检测推送红点提示
	public void checkNotifyHotPrompt(long accountId, boolean frist){
		boolean bNotify = false;
		if(frist){
			bNotify = true;
		}else{
			long oldVal = this.getHotPromptAttrib(accountId, HotPromptDefine.HOT_KEY_MAIL);
			int hotVal = mailManager.checkHotPrompt(accountId);
			if(oldVal != hotVal){
				this.setHotPromptAttrib(accountId, HotPromptDefine.HOT_KEY_MAIL, hotVal);
				bNotify = true;
			}
		}
		
		if(bNotify){
			List<HotPromptAttrib> retVo = this.getHotPrompt(accountId);
			@SuppressWarnings("rawtypes")
			Request pushMsg = Request.valueOf(AccountDefine.ACCOUNT_HOTPROMPT_NOTIFIY, 
					Result.valueOfSuccess(retVo).toMap()); 
			MessagePushQueueUtils.getPushQueue(sessionManager).push2(accountId, pushMsg);
		}
	}
	
	public void clearOfCache(long accountId){
		hotPromptMap.remove(accountId);
	}
	
}
