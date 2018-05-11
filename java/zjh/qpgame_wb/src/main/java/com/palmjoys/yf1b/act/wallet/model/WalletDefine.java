package com.palmjoys.yf1b.act.wallet.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketCode;
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
	
	
	//玩家不存在
	@SocketCode("玩家不存在")
	int WALLET_ERROR_UNEXIST = WALLET_ERROR_BASE - 1;
	@SocketCode("您的房卡不足")
	int WALLET_ERROR_ROOMCARD = WALLET_ERROR_BASE - 2;
	@SocketCode("接口参数错误")
	int WALLET_ERROR_CMDPARAM = WALLET_ERROR_BASE - 3;
	@SocketCode("不能赠送房卡给自已")
	int WALLET_ERROR_GIVEROOMCARD = WALLET_ERROR_BASE - 4;
}
