package com.palmjoys.yf1b.act.wallet.service;

public interface WalletService{
	//赠送房卡
	public Object wallet_roomcard_give(Long accountId, String givePlayer, int giveNum);
	//获取赠送记录
	public Object wallet_roomcard_record(Long accountId);
	
}
