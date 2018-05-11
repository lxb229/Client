package com.palmjoys.yf1b.act.wallet.service;

import com.palmjoys.yf1b.act.framework.gameobject.service.GameObjectService;

public interface WalletService extends GameObjectService{
	//赠送房卡
	public Object wallet_roomcard_give(Long accountId, String givePlayer, int giveNum);
	//获取赠送记录
	public Object wallet_roomcard_record(Long accountId);
	
}
