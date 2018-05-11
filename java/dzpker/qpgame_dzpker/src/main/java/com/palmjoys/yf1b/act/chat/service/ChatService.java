package com.palmjoys.yf1b.act.chat.service;

public interface ChatService {
	//发送聊天信息
	public Object chat_send(Long accountId, int tableId, int type, int content);
}
