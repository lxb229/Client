package com.palmjoys.yf1b.act.hotprompt.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;

import com.palmjoys.yf1b.act.hotprompt.service.HotpromptService;

@Component
@ConsoleBean
public class HotpromptCommand {
	@Autowired
	private HotpromptService hotpromptService;
	
	/**
	 * 获取指定玩家红点数据信息
	 * */
	@ConsoleCommand(name = "gm_hotprompt_get_hotprompt_data", description = "获取指定玩家红点数据信息")
	public Object gm_hotprompt_get_hotprompt_data(String accountId){
		return hotpromptService.hotprompt_get_hotdata(accountId);
	}
	

}
