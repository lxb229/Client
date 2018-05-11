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
	
	@NetworkApi(value = AccountDefine.ACCOUNT_LOGIN_ACCOUNTID, 
			desc="帐号Id登录")
	Object account_login_accountId(IoSession session,
			@InBody(value = "accountId", desc = "数据库唯 一Id") String accountId);
		
	@NetworkApi(value = AccountDefine.ACCOUNT_HOTPROMPT_NOTIFIY, 
			desc="推送消息(红点提示数据)")
	Object account_notify_hot_prompt();
	
	@NetworkApi(value = AccountDefine.ACCOUNT_WALLET_NOTIFY, 
			desc="推送消息(玩家钱包数据)")
	Object account_notify_wallet();
	
	@NetworkApi(value = AccountDefine.ACCOUNT_KICK_NOTIFY, 
			desc="推送消息(帐号被踢下线通知)")
	Object account_notify_kick_account();
	
}
