package com.palmjoys.yf1b.act.account.facade;

import org.apache.mina.core.session.IoSession;
import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;

import com.palmjoys.yf1b.act.account.model.AccountDefine;

@NetworkFacade
public interface AccountFacade {
	
	@NetworkApi(value = AccountDefine.ACCOUNT_HEART,
			desc="心跳消息")
	Object account_heart();
	
	@NetworkApi(value = AccountDefine.ACCOUNT_REGISTER, 
			desc="帐号注册")
	Object account_register(IoSession session,
			@InBody(value = "account", desc = "帐号名称") String account,
			@InBody(value = "password", desc = "密码") String password,
			@InBody(value = "headImg", desc = "头像") String headImg,
			@InBody(value = "nick", desc = "呢称") String nick,
			@InBody(value = "sex", desc = "性别") int sex);
	
	@NetworkApi(value = AccountDefine.ACCOUNT_LOGIN_PASSWORD, 
			desc="帐号密码方式登录")
	Object account_login_password(IoSession session,
			@InBody(value = "account", desc = "帐号名称") String account,
			@InBody(value = "password", desc = "密码") String password);
	
	@NetworkApi(value = AccountDefine.ACCOUNT_LOGIN_TOURIST, 
			desc="游客登录")
	Object account_login_tourist(IoSession session,
			@InBody(value = "uuid", desc = "微信或游客唯一uuid") String uuid);
	
	@NetworkApi(value = AccountDefine.ACCOUNT_LOGIN_WX, 
			desc="微信登录")
	Object account_login_wx(IoSession session,
			@InBody(value = "uuid", desc = "微信或游客唯一uuid") String uuid,
			@InBody(value = "headImg", desc = "头像") String headImg,
			@InBody(value = "nick", desc = "呢称") String nick,
			@InBody(value = "sex", desc = "性别") int sex);
	
	@NetworkApi(value = AccountDefine.ACCOUNT_LOGIN_ACCOUNTID, 
			desc="帐号Id登录")
	Object account_login_accountId(IoSession session,
			@InBody(value = "accountId", desc = "数据库唯 一Id") String accountId);
	
	@NetworkApi(value = AccountDefine.ACCOUNT_LOGOUT,
			desc="登出")
	Object account_login_out(
			@InBody(value = "accountId", desc = "帐号Id") String accountId);
	
	@NetworkApi(value = AccountDefine.ACCOUNT_ROLEINFO_ACCOUNT, 
			desc="通过帐号获取角色信息")
	Object account_role_accountId(
			@InBody(value = "accountId", desc = "帐号Id") String accountId);
	
	@NetworkApi(value = AccountDefine.ACCOUNT_ROLEINFO_STARNO, 
			desc="通过明星号获取角色信息")
	Object account_role_starNO(
			@InBody(value = "starNO", desc = "明星号") String starNO);
	
	@NetworkApi(value = AccountDefine.ACCOUNT_PING, 
			desc="客户端上报与服务器ping值")
	Object account_ping(@InSession Long accountId,
			@InBody(value = "ping", desc = "ping数值") int ping);
	
	@NetworkApi(value = AccountDefine.ACCOUNT_AUTHENTICATION, 
			desc="帐号实名认证")
	Object account_authentication(@InSession Long accountId,
			@InBody(value = "name", desc = "姓名") String name,
			@InBody(value = "cardId", desc = "身份证") String cardId);
	
	@NetworkApi(value = AccountDefine.ACCOUNT_PHONE_BIND, 
			desc="手机绑定")
	Object account_phone_bind(@InSession Long accountId,
			@InBody(value = "phone", desc = "手机号") String phone,
			@InBody(value = "vaildCode", desc = "验证码") String vaildCode);
	
	@NetworkApi(value = AccountDefine.ACCOUNT_SMSCODE_GET, 
			desc="获取手机短信效验码")
	Object account_phone_get_smsCode(@InSession Long accountId,
			@InBody(value = "phone", desc = "手机号") String phone);
	
	@NetworkApi(value = AccountDefine.ACCOUNT_COMMAND_KICK_NOTIFY, 
			desc="推送消息(玩家被踢下线)")
	Object account_kick_offline_notify();
}
