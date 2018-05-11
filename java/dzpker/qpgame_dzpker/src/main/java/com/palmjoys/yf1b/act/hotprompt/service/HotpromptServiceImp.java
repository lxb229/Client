package com.palmjoys.yf1b.act.hotprompt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptAttrib;

@Service
public class HotpromptServiceImp implements HotpromptService{
	@Autowired
	private HotPromptManager hotPromptManager;

	@Override
	public Object hotprompt_get_hotdata(String accountId) {
		List<HotPromptAttrib> retVo = hotPromptManager.getHotPrompt(Long.valueOf(accountId));
		return Result.valueOfSuccess(retVo);
	}

}
