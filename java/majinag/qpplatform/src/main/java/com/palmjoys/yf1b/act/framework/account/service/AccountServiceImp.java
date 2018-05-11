package com.palmjoys.yf1b.act.framework.account.service;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Result;
import org.treediagram.nina.network.util.IpUtils;

import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;
import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.manager.StarNOManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountAttribVo;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.framework.account.model.OtherAccountAttribVo;

@Service
public class AccountServiceImp implements AccountService {
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private StarNOManager starNOManager;
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private EventTriggerManager eventTriggerManager;
	
	@Override
	public Object account_login_tourist(IoSession session, 
			String uuid) {
		AccountEntity accountEntity = accountManager.findOf_uuid(uuid);
		if(null == accountEntity){
			//注册
			String starNO = starNOManager.createStarNO();
			if(null == starNO){
				starNOManager.extendStarNONum(1);
				starNO = starNOManager.createStarNO();
			}
			
			if(null == starNO){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "注册失败,服务器帐号已达限制", null);
			}
			String headImg = "";
			String nick = "游客"+session.getId();
			int sex = (int) ((Math.random()*100)%3);
			String clientIP = IpUtils.getIp(session);
			accountEntity = accountManager.create(uuid, starNO, headImg, sex, nick, clientIP);
			EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_ACCOUNT);
			Integer cmdId = 1;
			eventAttrib.addEventParam(cmdId);
			eventAttrib.addEventParam(accountEntity.getId());
			eventTriggerManager.triggerEvent(eventAttrib);
		}else{
			//登录
			if(accountEntity.getState() < 0){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号已被禁用,无法登录", null);
			}
		}
		long accountId = accountEntity.getId();
		loginTransSession(accountId, session);
		AccountAttribVo retVo = accountManager.Account2AccountAttrib(accountEntity);
		return Result.valueOfSuccess(retVo);
	}	
	
	@Override
	public Object account_login_wx(IoSession session, String uuid, 
			String headImg, String nick, int sex) {
		AccountEntity accountEntity = accountManager.findOf_uuid(uuid);
		if(null == accountEntity){
			//注册
			String starNO = starNOManager.createStarNO();
			if(null == starNO){
				starNOManager.extendStarNONum(1);
				starNO = starNOManager.createStarNO();
			}			
			if(null == starNO){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "注册失败,服务器帐号已达限制", null);
			}
			
			String clientIP = IpUtils.getIp(session);
			accountEntity = accountManager.create(uuid, starNO, headImg, sex, nick, clientIP);
			//上报事件
			EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_ACCOUNT);
			Integer cmdId = 1;
			eventAttrib.addEventParam(cmdId);
			eventAttrib.addEventParam(accountEntity.getId());
			eventTriggerManager.triggerEvent(eventAttrib);
		}else{
			if(accountEntity.getState() < 0){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号已被禁用,无法登录", null);
			}
			
			String theHeadImg = accountEntity.getHeadImg();
			String theNick = accountEntity.getNick();
			int theSex = accountEntity.getSex();
			if((headImg.equalsIgnoreCase(theHeadImg) == false)
					|| (theNick.equalsIgnoreCase(nick) == false)
					|| (sex!=theSex)){
				//数据信息改动过了
				accountEntity.setHeadImg(headImg);
				accountEntity.setNick(nick);
				accountEntity.setSex(sex);
				//上报事件
				EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_ACCOUNT);
				Integer cmdId = 2;
				eventAttrib.addEventParam(cmdId);
				eventAttrib.addEventParam(accountEntity.getId());
				eventTriggerManager.triggerEvent(eventAttrib);
			}
		}
		long accountId = accountEntity.getId();
		loginTransSession(accountId, session);
		AccountAttribVo retVo = accountManager.Account2AccountAttrib(accountEntity);		
		return Result.valueOfSuccess(retVo);
	}

	private void loginTransSession(long accountId, IoSession session){
		if (null != session) {
			IoSession oldSession = sessionManager.getSession(accountId);
			if (oldSession == null) {
				sessionManager.bind(session, accountId);
			} else {
				if (session != oldSession) {
					// 后面的人顶了前面的人,先关闭前面的连接
					@SuppressWarnings("rawtypes")
					Request pushMsg = Request.valueOf(AccountDefine.ACCOUNT_KICK_NOTIFY, 
							Result.valueOfSuccess());
					sessionManager.send(pushMsg, oldSession);
					
					sessionManager.unBind(oldSession, accountId);
					oldSession.close(false);
					sessionManager.bind(session, accountId);
				}
			}
		
			accountManager.accountOnLine(accountId, session);
		}
	}
	
	@Override
	public Object account_login_out(String accountId) {
		//long n_accountId = Long.valueOf(accountId);
		//accountManager.accountOffLine(n_accountId);
		return Result.valueOfSuccess();
	}

	@Override
	public Object account_role_accountId(String accountId) {
		long n_accountId = Long.valueOf(accountId);
		AccountEntity accountEntity = accountManager.load(n_accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		OtherAccountAttribVo retVo = accountManager.Account2OtherAttrib(accountEntity);
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object account_role_starNO(String starNO) {
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		OtherAccountAttribVo retVo = accountManager.Account2OtherAttrib(accountEntity);
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object account_ping(Long accountId, int ping) {
		accountManager.updatPing(accountId, ping);
		return Result.valueOfSuccess();
	}

	@Override
	public Object account_login_accountId(IoSession session, String accountId) {
		AccountEntity accountEntity = accountManager.load(Long.valueOf(accountId));
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		return this.account_login_wx(session, accountEntity.getUuid(), accountEntity.getHeadImg(), 
				accountEntity.getNick(), accountEntity.getSex());
	}	
}
