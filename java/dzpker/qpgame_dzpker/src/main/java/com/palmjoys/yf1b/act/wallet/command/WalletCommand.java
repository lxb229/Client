package com.palmjoys.yf1b.act.wallet.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.network.model.Result;
import com.palmjoys.yf1b.act.account.entity.RoleEntity;
import com.palmjoys.yf1b.act.account.manager.RoleEntityManager;
import com.palmjoys.yf1b.act.account.model.AccountDefine;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

@Component
@ConsoleBean
public class WalletCommand {
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private RoleEntityManager roleEntityManager;
	
	@ConsoleCommand(name = "gm_wallet_add_roomcard", 
			description = "增加或减少房卡")
	public Object gm_wallet_add_roomcard(String starNO, int num){
		RoleEntity roleEntity = roleEntityManager.findOf_starNO(starNO);
		if(null == roleEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_ABNORMAL);
		}
		walletManager.addRoomCard(roleEntity.getAccountId(), num);
		return Result.valueOfSuccess();
	}
	
	@ConsoleCommand(name = "gm_wallet_add_goldmoney", 
			description = "增加或减少金币")
	public Object gm_wallet_add_goldmoney(String starNO, int num){
		RoleEntity roleEntity = roleEntityManager.findOf_starNO(starNO);
		if(null == roleEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_ABNORMAL);
		}
		walletManager.addGoldMoney(roleEntity.getAccountId(), num);
		return Result.valueOfSuccess();
	}
	
	@ConsoleCommand(name = "gm_wallet_add_diamond", 
			description = "增加或减少钻石")
	public Object gm_wallet_add_diamond(String starNO, int num){
		RoleEntity roleEntity = roleEntityManager.findOf_starNO(starNO);
		if(null == roleEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_ABNORMAL);
		}
		walletManager.addDiamond(roleEntity.getAccountId(), num);
		return Result.valueOfSuccess();
	}
	
}
