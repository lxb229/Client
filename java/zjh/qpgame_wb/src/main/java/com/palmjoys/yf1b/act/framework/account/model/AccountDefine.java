package com.palmjoys.yf1b.act.framework.account.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketCode;
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
	//手机登录
	int ACCOUNT_LOGIN_PHONE = ACCOUNT_COMMAND_BASE + 8;
	//获取手机短信验证码
	int ACCOUNT_GET_PHONE_SMSCODE = ACCOUNT_COMMAND_BASE + 9;
	//注册帐号
	int ACCOUNT_REGISTER = ACCOUNT_COMMAND_BASE + 10;
	//获取客户信息
	int ACCOUNT_CUSTOMERSERVICE = ACCOUNT_COMMAND_BASE + 11;
	
	//推送消息(红点提示数据)
	int ACCOUNT_HOTPROMPT_NOTIFIY = ACCOUNT_COMMAND_BASE_NOTIFY + 1;
	//推送消息(玩家钱包数据,暂时只有房卡)
	int ACCOUNT_WALLET_NOTIFY = ACCOUNT_COMMAND_BASE_NOTIFY + 2;
	
	// error code
	@SocketCode("账号异常")
	int ACCOUNT_ERROR_ABNORMAL = ACCOUNT_ERROR_BASE - 1;
	@SocketCode("账号已被冻结")
	int ACCOUNT_ERROR_UNUSED = ACCOUNT_ERROR_BASE - 2;
	@SocketCode("手机短信验证登录失败")
	int ACCOUNT_ERROR_LOGIN_SMS = ACCOUNT_ERROR_BASE - 3;
	@SocketCode("验证码获取频繁,稍后再试")
	int ACCOUNT_ERROR_SMSCODE_NUM = ACCOUNT_ERROR_BASE - 4;
	@SocketCode("验证码获取失败,稍后再试")
	int ACCOUNT_ERROR_SMSCODE_FAIL = ACCOUNT_ERROR_BASE - 5;
	@SocketCode("帐号已注册")
	int ACCOUNT_ERROR_REGISTER = ACCOUNT_ERROR_BASE - 6;
	@SocketCode("命令参数错误")
	int ACCOUNT_ERROR_PARAM = ACCOUNT_ERROR_BASE - 7;
	@SocketCode("帐号不存在")
	int ACCOUNT_ERROR_UN_EXIST = ACCOUNT_ERROR_BASE - 8;
	@SocketCode("帐号或密码错误")
	int ACCOUNT_ERROR_PASSWORD = ACCOUNT_ERROR_BASE - 9;
	
}
