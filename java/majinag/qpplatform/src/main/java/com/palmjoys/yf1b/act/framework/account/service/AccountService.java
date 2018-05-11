package com.palmjoys.yf1b.act.framework.account.service;

import org.apache.mina.core.session.IoSession;

public interface AccountService {
	//游客登录
	public Object account_login_tourist(IoSession session, String uuid);
	//微信登录
	public Object account_login_wx(IoSession session, String uuid,
			String headImg, String nick, int sex);
	//登出
	public Object account_login_out(String accountId);
	//通过帐号获取角色信息
	public Object account_role_accountId(String accountId);
	//通过帐号获取角色信息
	public Object account_role_starNO(String starNO);
	//客户端上报与服务器ping值
	public Object account_ping(Long accountId, int ping);
	//通过帐号Id登录
	public Object account_login_accountId(IoSession session, String accountId);
}
