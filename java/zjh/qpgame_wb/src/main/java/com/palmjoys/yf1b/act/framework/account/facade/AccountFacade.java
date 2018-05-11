package com.palmjoys.yf1b.act.framework.account.facade;

import org.apache.mina.core.session.IoSession;
import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;

import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;

@NetworkFacade
public interface AccountFacade {
	
	@NetworkApi(value = AccountDefine.ACCOUNT_HEART,
			desc="心跳消息")
	Object account_heart();
	
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
	
	@NetworkApi(value = AccountDefine.ACCOUNT_LOGIN_PHONE, 
			desc="手机登录")
	Object account_login_phone(IoSession session,
			@InBody(value = "phone", desc = "手机号") String phone,
			@InBody(value = "code", desc = "验证码") String code);	
		
	@NetworkApi(value = AccountDefine.ACCOUNT_GET_PHONE_SMSCODE, 
			desc="获取手机短信验证码")
	Object account_get_sms_code(
			@InBody(value = "phone", desc = "手机号码") String phone);
	
	@NetworkApi(value = AccountDefine.ACCOUNT_REGISTER, 
			desc="手机号注册")
	Object account_register(IoSession session,
			@InBody(value = "phone", desc = "手机号") String phone,
			@InBody(value = "code", desc = "密码") String code);
	
	@NetworkApi(value = AccountDefine.ACCOUNT_CUSTOMERSERVICE, 
			desc="客户信息")
	Object account_customer_service();
		
	@NetworkApi(value = AccountDefine.ACCOUNT_HOTPROMPT_NOTIFIY, 
			desc="推送消息(红点提示数据)")
	Object account_notify_hot_prompt();
	
	@NetworkApi(value = AccountDefine.ACCOUNT_WALLET_NOTIFY, 
			desc="推送消息(玩家钱包数据)")
	Object account_notify_wallet();
	
}
