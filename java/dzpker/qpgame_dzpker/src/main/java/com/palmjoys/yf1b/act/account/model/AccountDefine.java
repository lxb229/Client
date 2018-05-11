package com.palmjoys.yf1b.act.account.model;

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
	//帐号注册
	int ACCOUNT_REGISTER = ACCOUNT_COMMAND_BASE + 2;
	//用户名密码方式登录
	int ACCOUNT_LOGIN_PASSWORD = ACCOUNT_COMMAND_BASE + 3;
	//游客方式登录
	int ACCOUNT_LOGIN_TOURIST = ACCOUNT_COMMAND_BASE + 4;
	//微信方式登陆
	int ACCOUNT_LOGIN_WX = ACCOUNT_COMMAND_BASE + 5;
	//通过帐号Id登录(重连用)
	int ACCOUNT_LOGIN_ACCOUNTID = ACCOUNT_COMMAND_BASE + 6;	
	//帐号登出
	int ACCOUNT_LOGOUT = ACCOUNT_COMMAND_BASE + 7;
	//通过帐号Id获取角色信息
	int ACCOUNT_ROLEINFO_ACCOUNT = ACCOUNT_COMMAND_BASE + 8;
	//通过玩家Id获取角色信息	
	int ACCOUNT_ROLEINFO_STARNO = ACCOUNT_COMMAND_BASE + 9;
	//客户端上报与服务器ping值
	int ACCOUNT_PING = ACCOUNT_COMMAND_BASE + 10;
	//帐号实名认证
	int ACCOUNT_AUTHENTICATION = ACCOUNT_COMMAND_BASE + 11;
	//手机帮定
	int ACCOUNT_PHONE_BIND = ACCOUNT_COMMAND_BASE + 12;
	//获取短信效验码
	int ACCOUNT_SMSCODE_GET = ACCOUNT_COMMAND_BASE + 13;
	
	//推送消息(玩家被 踢下线)
	int ACCOUNT_COMMAND_KICK_NOTIFY = ACCOUNT_COMMAND_BASE_NOTIFY + 1;
	
	// error code
	@SocketCode("账号异常")
	int ACCOUNT_ERROR_ABNORMAL = ACCOUNT_ERROR_BASE - 1;
	@SocketCode("账号已被冻结")
	int ACCOUNT_ERROR_UNUSED = ACCOUNT_ERROR_BASE - 2;
	@SocketCode("账号或密码错误")
	int ACCOUNT_ERROR_PASSWORD = ACCOUNT_ERROR_BASE - 3;
	@SocketCode("账号已存在")
	int ACCOUNT_ERROR_EXIST = ACCOUNT_ERROR_BASE - 4;
	@SocketCode("账号已认证")
	int ACCOUNT_ERROR_AUTHICATION = ACCOUNT_ERROR_BASE - 5;
	@SocketCode("账号已绑定手机号")
	int ACCOUNT_ERROR_PHONE_BIND = ACCOUNT_ERROR_BASE - 6;
	@SocketCode("短信效验码错误")
	int ACCOUNT_ERROR_PHONE_SMSCODE = ACCOUNT_ERROR_BASE - 7;
	@SocketCode("短信效验码获取太频繁,诮后再试")
	int ACCOUNT_ERROR_PHONE_SMSCODE_TIME = ACCOUNT_ERROR_BASE - 8;
	@SocketCode("短信效验码获取失败,诮后再试")
	int ACCOUNT_ERROR_PHONE_SMSCODE_FIAL = ACCOUNT_ERROR_BASE - 9;
	@SocketCode("服务器维护中,请稍后再试")
	int ACCOUNT_ERROR_SEVER_CLOSE = ACCOUNT_ERROR_BASE - 10;
}
