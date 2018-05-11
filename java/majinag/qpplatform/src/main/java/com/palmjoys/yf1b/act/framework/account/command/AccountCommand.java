package com.palmjoys.yf1b.act.framework.account.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.network.model.Result;
import com.palmjoys.yf1b.act.framework.account.manager.StarNOManager;

@Component
@ConsoleBean
public class AccountCommand {
	@Autowired
	private StarNOManager starNOManager;
	
	/**
	 * 扩展明星号位数
	 * exNum 扩展位数 
	 * */
	@ConsoleCommand(name = "gm_account_extend_starno", description = "扩展明星号位数")
	public Object gm_account_extend_starno(int exNum){
		starNOManager.extendStarNONum(exNum);
		return Result.valueOfSuccess();
	}
}
