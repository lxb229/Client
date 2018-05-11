package com.palmjoys.yf1b.act.notice.facade;

import org.springframework.stereotype.Component;
import org.treediagram.nina.network.model.Result;

@Component
public class NoticeFacadeImp implements NoticeFacade{

	@Override
	public Object message_notice_notify() {
		return Result.valueOfSuccess();
	}
}
