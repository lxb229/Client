package com.palmjoys.yf1b.act.wallet.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.wallet.service.WalletService;

@Component
public class WalletFacadeImp implements WalletFacade{
	@Autowired
	private WalletService walletService;
	
	@Override
	public Object wallet_roomcard_give(Long accountId, String givePlayer, int giveNum) {
		return walletService.wallet_roomcard_give(accountId, givePlayer, giveNum);
	}

	@Override
	public Object wallet_roomcard_record(Long accountId) {
		return walletService.wallet_roomcard_record(accountId);
	}

	@Override
	public Object wallet_wallet_notify() {
		return Result.valueOfSuccess();
	}
	
}
