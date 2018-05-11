package com.palmjoys.yf1b.act.wallet.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "钱包模块")
public interface WalletDefine {
	@SocketModule("钱包模块")
	int WALLET = 8;
	
	static final int WALLET_COMMAND_BASE = WALLET*100;
	static final int WALLET_ERROR_BASE = (0-WALLET)*1000;
	static final int WALLET_COMMAND_BASE_NOTIFY = WALLET*10000;
	
	//command id
	//赠送房卡
	int WALLET_COMMAND_ROOMCARD_GIVE = WALLET_COMMAND_BASE + 1;
	//获取房卡赠送记录
	int WALLET_COMMAND_ROOMCARD_RECORD = WALLET_COMMAND_BASE + 2;
	
}
