package com.palmjoys.yf1b.act.framework.account.service;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.treediagram.nina.network.filter.session.SessionManager;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;

public class AccountSessionCloseFilter extends IoFilterAdapter{
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private AccountManager accountManager;
	
	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
		try{
			Object accountObj = sessionManager.getOnlineIdentity(session);
			if(null != accountObj){
				Long accountId = (Long)accountObj;
				accountManager.accountOffLine(accountId);
			}
		}catch(Exception e){
		}
		super.sessionClosed(nextFilter, session);
	}
}
