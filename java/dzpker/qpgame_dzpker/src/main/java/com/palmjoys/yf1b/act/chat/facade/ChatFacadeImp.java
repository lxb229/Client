package com.palmjoys.yf1b.act.chat.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.chat.service.ChatService;

@Component
public class ChatFacadeImp implements ChatFacade{
	@Autowired
	private ChatService chatService;

	@Override
	public Object chat_send(Long accountId, int tableId, int type, int content) {
		return chatService.chat_send(accountId, tableId, type, content);
	}

	@Override
	public Object chat_send_notify() {
		return Result.valueOfSuccess();
	}

}
