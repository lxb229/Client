package com.palmjoys.yf1b.act.wallet.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

@Component
@ConsoleBean
public class WalletCommand {
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private AccountManager accountManager;
	
	@ConsoleCommand(name = "gm_wallet_add_roomcard", 
			description = "增加或减少房卡")
	public Object gm_wallet_add_roomcard(String starNO, int num){
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_ABNORMAL);
		}
		walletManager.addRoomCard(accountEntity.getId(), num, true);
		return Result.valueOfSuccess();
	}
}
