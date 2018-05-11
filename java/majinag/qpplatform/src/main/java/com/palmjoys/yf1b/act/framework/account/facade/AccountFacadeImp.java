package com.palmjoys.yf1b.act.framework.account.facade;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.framework.account.service.AccountService;

@Component
public class AccountFacadeImp implements AccountFacade {
	@Autowired
	private AccountService accountService;	
	
	@Override
	public Object account_heart() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object account_login_tourist(IoSession session, String uuid) {
		return accountService.account_login_tourist(session, uuid);
	}

	@Override
	public Object account_login_wx(IoSession session, String uuid,
			String headImg, String nick, int sex) {
		return accountService.account_login_wx(session, uuid, headImg, nick, sex);
	}

	@Override
	public Object account_login_out(String accountId) {
		return accountService.account_login_out(accountId);
	}

	@Override
	public Object account_role_accountId(String accountId) {
		return accountService.account_role_accountId(accountId);
	}

	@Override
	public Object account_role_starNO(String starNO) {
		return accountService.account_role_starNO(starNO);
	}
	
	@Override
	public Object account_ping(Long accountId, int ping) {
		return accountService.account_ping(accountId, ping);
	}
	@Override
	public Object account_login_accountId(IoSession session, String accountId) {
		return accountService.account_login_accountId(session, accountId);
	}
	
	@Override
	public Object account_notify_hot_prompt() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object account_notify_wallet() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object account_notify_kick_account() {
		return Result.valueOfSuccess();
	}
		

}
