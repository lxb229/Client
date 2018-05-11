package com.palmjoys.yf1b.act.hotprompt.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.hotprompt.service.HotpromptService;

@Component
public class HotpromptFacadeImp implements HotpromptFacade{
	@Autowired
	private HotpromptService hotpromptService;

	@Override
	public Object hotprompt_get_hotdata(String accountId) {
		return hotpromptService.hotprompt_get_hotdata(accountId);
	}

	@Override
	public Object hotprompt_hotdata_notify() {
		return Result.valueOfSuccess();
	}

}
