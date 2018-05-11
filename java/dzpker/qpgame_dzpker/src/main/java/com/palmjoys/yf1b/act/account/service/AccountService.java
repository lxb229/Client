package com.palmjoys.yf1b.act.account.service;

import org.apache.mina.core.session.IoSession;

public interface AccountService {
	//帐号注册
	public Object account_register(IoSession session, String account, String password,String headImg, String nick, int sex);
	//帐号密码登录
	public Object account_login_password(IoSession session, String account, String password);
	//游客登录
	public Object account_login_tourist(IoSession session, String uuid);
	//微信登录
	public Object account_login_wx(IoSession session, String uuid,
			String headImg, String nick, int sex);
	//通过帐号Id登录
	public Object account_login_accountId(IoSession session, String accountId);
	//登出
	public Object account_login_out(String accountId);
	//通过帐号获取角色信息
	public Object account_role_accountId(String accountId);
	//通过帐号获取角色信息
	public Object account_role_starNO(String starNO);
	//客户端上报与服务器ping值
	public Object account_ping(Long accountId, int ping);
	//实名认证
	public Object account_authentication(Long accountId, String name, String cardId);
	//手机绑定
	public Object account_phone_bind(Long accountId, String phone, String vaildCode);
	//获取短信效验码
	public Object account_phone_get_smsCode(Long accountId, String phone);
	
}
