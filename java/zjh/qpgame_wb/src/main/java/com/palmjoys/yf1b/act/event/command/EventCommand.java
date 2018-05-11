package com.palmjoys.yf1b.act.event.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.event.manager.EventMQManager;
import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;

@Component
@ConsoleBean
public class EventCommand {
	@Autowired
	private EventTriggerManager eventTriggerManager;
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private EventMQManager eventMQManager;
	//日志
	private Logger logger = LoggerFactory.getLogger(EventCommand.class);
	
	/**
	 * 手动上传失败的数据事件 
	 * */
	@ConsoleCommand(name = "gm_Event_TryUploadEvent", description = "手动上传失败的数据事件 ")
	public Object gm_Event_TryUploadEvent(){
		eventTriggerManager.tryUpLoadFialEvent();
		return Result.valueOfSuccess();
	}

	/**
	 * 重新加载MQ消息队列配置(手动改EXECL后执行此命令)
	 * */
	@ConsoleCommand(name = "gm_Event_ReInitMqConfig", description = "重新加载MQ消息队列配置 ")
	public Object gm_Event_ReInitMqConfig(){
		eventMQManager.resetMqInit();
		return Result.valueOfSuccess();
	}
	
	/**
	 * 获取事件队列数据数目
	 * */
	@ConsoleCommand(name = "gm_Event_CountEvtNum", description = "获取事件队列数据数目 ")
	public Object gm_Event_CountEvtNum(){
		int size = eventTriggerManager.getQueueSize();
		logger.debug("事件队列长度=" + size);		
		return Result.valueOfSuccess();
	}
	
	/**
	 * 获取所有在线连接数
	 * */
	@ConsoleCommand(name = "gm_Event_Count_Online_Session", description = "获取所有在线连接数 ")
	public Object gm_Event_Count_Online_Session(){
		int onlineNum = sessionManager.count(false);
		String comment = "所有在线连接数=" + onlineNum;
		logger.debug(comment);
		return Result.valueOfSuccess(comment);
	}
	
}
