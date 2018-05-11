package com.palmjoys.yf1b.act.majiang.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.majiang.service.MajiangService;

@Component
@ConsoleBean
public class MajiangCommand {
	@Autowired
	private MajiangService majiangService;
	@Autowired
	private AccountManager accountManager;
	
	
	@ConsoleCommand(name = "majiang_command_get_roomcfg", 
			description = "获取房间配置")
	public void majiang_command_get_roomcfg(){
		//majiangService.majiang_get_rulecfg();
	}
	
	@ConsoleCommand(name = "majiang_command_create_table", 
			description = "手动创建房间")
	public Object majiang_command_create_table(String starNO, String corpsId,
			int roomItemId, int []rules, String password){
		
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "未找到指定玩家", null);
		}
		List<Integer> theRules = new ArrayList<>();
		for(int rule : rules){
			theRules.add(rule);
		}
		
		return majiangService.majiang_room_create(accountEntity.getId(), corpsId, roomItemId, theRules, password);
	}
}
