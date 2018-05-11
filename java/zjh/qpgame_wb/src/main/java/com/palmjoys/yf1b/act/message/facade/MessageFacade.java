package com.palmjoys.yf1b.act.message.facade;

import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;

import com.palmjoys.yf1b.act.message.model.MessageDefine;

@NetworkFacade
public interface MessageFacade {	
	
	@NetworkApi(value = MessageDefine.MESSAGE_COMMAND_NOTIFY,
			desc="公告推送消息")
	Object message_notice_notify();
}
