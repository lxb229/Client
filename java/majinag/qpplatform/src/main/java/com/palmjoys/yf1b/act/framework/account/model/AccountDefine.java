package com.palmjoys.yf1b.act.framework.account.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "账号模块")
public interface AccountDefine {

	// module
	@SocketModule("账号模块")
	int ACCOUNT = 1;

	static final int ACCOUNT_COMMAND_BASE = ACCOUNT*100;
	static final int ACCOUNT_ERROR_BASE = (0-ACCOUNT)*1000;
	static final int ACCOUNT_COMMAND_BASE_NOTIFY = ACCOUNT*10000;

	// command id
	//心跳
	int ACCOUNT_HEART = ACCOUNT_COMMAND_BASE + 1;
	//游客登录
	int ACCOUNT_LOGIN_TOURIST = ACCOUNT_COMMAND_BASE + 2;
	//微信登陆
	int ACCOUNT_LOGIN_WX = ACCOUNT_COMMAND_BASE + 3;
	//登出
	int ACCOUNT_LOGOUT = ACCOUNT_COMMAND_BASE + 4;
	//通过帐号获取角色信息
	int ACCOUNT_ROLEINFO_ACCOUNT = ACCOUNT_COMMAND_BASE + 5;
	//通过明星号获取角色信息	
	int ACCOUNT_ROLEINFO_STARNO = ACCOUNT_COMMAND_BASE + 6;
	//客户端上报与服务器ping值
	int ACCOUNT_PING = ACCOUNT_COMMAND_BASE + 7;
	//通过帐号Id登录(重连用)
	int ACCOUNT_LOGIN_ACCOUNTID = ACCOUNT_COMMAND_BASE + 8;
	
	
	//推送消息(红点提示数据)
	int ACCOUNT_HOTPROMPT_NOTIFIY = ACCOUNT_COMMAND_BASE_NOTIFY + 1;
	//推送消息(玩家钱包数据,暂时只有房卡)
	int ACCOUNT_WALLET_NOTIFY = ACCOUNT_COMMAND_BASE_NOTIFY + 2;
	//推送消息(帐号被踢下线通知)
	int ACCOUNT_KICK_NOTIFY = ACCOUNT_COMMAND_BASE_NOTIFY + 3;
	
	
	//错误Id定义
	int ACCOUNT_ERROR_COMMON_CODE = ACCOUNT_ERROR_BASE - 1;
		
}
