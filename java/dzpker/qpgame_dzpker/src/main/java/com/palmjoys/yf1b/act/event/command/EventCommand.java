package com.palmjoys.yf1b.act.event.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Result;
import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;

@Component
@ConsoleBean
public class EventCommand {
	@Autowired
	private EventTriggerManager eventTriggerManager;
	@Autowired
	private SessionManager sessionManager;
	//日志
	private Logger logger = LoggerFactory.getLogger(EventCommand.class);
	
	/**
	 * 手动上传失败的数据事件 
	 * */
	@ConsoleCommand(name = "gm_event_upload_event", description = "手动上传失败的数据事件 ")
	public Object gm_event_upload_event(){
		eventTriggerManager.tryUpLoadFialEvent();
		return Result.valueOfSuccess();
	}	
	
	/**
	 * 获取事件队列数据数目
	 * */
	@ConsoleCommand(name = "gm_event_count_eventnum", description = "获取事件队列数据数目 ")
	public Object gm_event_count_eventnum(){
		int size = eventTriggerManager.getQueueSize();
		logger.debug("事件队列长度=" + size);		
		return Result.valueOfSuccess();
	}
	
	/**
	 * 获取所有在线连接数
	 * */
	@ConsoleCommand(name = "gm_event_count_online_session", description = "获取所有在线连接数 ")
	public Object gm_event_count_online_session(){
		int onlineNum = sessionManager.count(false);
		String comment = "当前所有在线连接数=" + onlineNum;
		logger.debug(comment);
		return Result.valueOfSuccess(comment);
	}
	
}
