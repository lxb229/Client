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
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		walletManager.addRoomCard(accountEntity.getId(), num);
		return Result.valueOfSuccess();
	}
	
	@ConsoleCommand(name = "gm_wallet_add_replacecard", 
			description = "增加或减少体验房卡")
	public Object gm_wallet_add_replacecard(String starNO, int num){
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		walletManager.addReplaceCard(accountEntity.getId(), num);
		return Result.valueOfSuccess();
	}
	
	@ConsoleCommand(name = "gm_wallet_add_goldmoney", 
			description = "增加或减少金币")
	public Object gm_wallet_add_goldmoney(String starNO, int num){
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		walletManager.addGoldMoney(accountEntity.getId(), num);
		return Result.valueOfSuccess();
	}
	
	@ConsoleCommand(name = "gm_wallet_add_silvermoney", 
			description = "增加或减少银币")
	public Object gm_wallet_add_silvermoney(String starNO, int num){
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		walletManager.addSilverMoney(accountEntity.getId(), num);
		return Result.valueOfSuccess();
	}
}
