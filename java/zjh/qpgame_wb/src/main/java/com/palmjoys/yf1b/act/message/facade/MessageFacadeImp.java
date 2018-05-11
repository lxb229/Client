package com.palmjoys.yf1b.act.message.facade;

import org.springframework.stereotype.Component;
import org.treediagram.nina.network.model.Result;

@Component
public class MessageFacadeImp implements MessageFacade{

	@Override
	public Object message_notice_notify() {
		return Result.valueOfSuccess();
	}
}
