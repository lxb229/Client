package com.palmjoys.yf1b.act.chat.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "聊天模块")
public interface ChatDefine {
	@SocketModule("聊天模块")
	int CHAT = 6;
	
	static final int CHAT_COMMAND_BASE = CHAT*100;
	static final int CHAT_ERROR_BASE = (0-CHAT)*1000;
	static final int CHAT_COMMAND_BASE_NOTIFY = CHAT*10000;
	
	//command id
	//发送聊天消息
	int CHAT_COMMAND_CHAT_SEND = CHAT_COMMAND_BASE + 1;
	//创建聊天分组
	int CHAT_COMMAND_CREATE_CHAT_GROUP = CHAT_COMMAND_BASE + 2;
	//加入聊天分组
	int CHAT_COMMAND_JOIN_CHAT_GROUP = CHAT_COMMAND_BASE + 3;
	//离开聊天分组
	int CHAT_COMMAND_EXIT_CHAT_GROUP = CHAT_COMMAND_BASE + 4;
	//删除聊天分组
	int CHAT_COMMAND_DEL_CHAT_GROUP = CHAT_COMMAND_BASE + 5;
	//点对点发送聊天信息
	int CHAT_COMMAND_CHAT_SEND2 = CHAT_COMMAND_BASE + 6;
	//推送消息(收到聊天消息)
	int CHAT_COMMAND_CHAT_NOTIFY = CHAT_COMMAND_BASE_NOTIFY + 1;
}
