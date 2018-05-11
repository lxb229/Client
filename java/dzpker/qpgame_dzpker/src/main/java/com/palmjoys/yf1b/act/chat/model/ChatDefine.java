package com.palmjoys.yf1b.act.chat.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketCode;
import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "聊天模块")
public interface ChatDefine {
	@SocketModule("聊天模块")
	int CHAT = 5;
	
	static final int CHAT_COMMAND_BASE = CHAT*100;
	static final int CHAT_ERROR_BASE = (0-CHAT)*1000;
	static final int CHAT_COMMAND_BASE_NOTIFY = CHAT*10000;
	
	//command id
	//发送聊天消息
	int CHAT_COMMAND_CHAT_SEND = CHAT_COMMAND_BASE + 1;
	//推送消息(聊天消息数据)
	int CHAT_COMMAND_CHAT_NOTIFY = CHAT_COMMAND_BASE_NOTIFY + 1;
	
	//error id
	@SocketCode("房间不存在")
	int CHAT_ERROR_UNEXIST = CHAT_ERROR_BASE - 1;
}
