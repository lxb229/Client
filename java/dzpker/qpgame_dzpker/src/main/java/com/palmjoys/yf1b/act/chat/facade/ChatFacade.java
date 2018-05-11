package com.palmjoys.yf1b.act.chat.facade;

import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;

import com.palmjoys.yf1b.act.chat.model.ChatDefine;

@NetworkFacade
public interface ChatFacade {
	@NetworkApi(value = ChatDefine.CHAT_COMMAND_CHAT_SEND,
			desc="发送聊天信息")
	Object chat_send(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId,
			@InBody(value = "type", desc = "聊天内容类型") int type,
			@InBody(value = "content", desc = "聊天内容Id") int content);
	
	@NetworkApi(value = ChatDefine.CHAT_COMMAND_CHAT_NOTIFY,
			desc="推送消息(聊天信息通知)")
	Object chat_send_notify();
}
