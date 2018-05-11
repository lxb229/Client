package com.palmjoys.yf1b.act.framework.account.service;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Result;
import org.treediagram.nina.network.util.IpUtils;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.entity.AuthenticationEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.manager.AuthenticationManager;
import com.palmjoys.yf1b.act.framework.account.manager.StarNOManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountAttribVo;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.framework.account.model.OtherAccountAttribVo;
import com.palmjoys.yf1b.act.framework.common.manager.ErrorCodeManager;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.zjh.manager.GameDataManager;
import com.palmjoys.yf1b.act.zjh.manager.GameLogicManager;
import com.palmjoys.yf1b.act.zjh.model.GameDataAttrib;
import com.palmjoys.yf1b.act.zjh.model.TableAttrib;
import com.palmjoys.yf1b.act.zjh.model.ZJHMessageDefine;

@Service
public class AccountServiceImp implements AccountService {
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private StarNOManager starNOManager;
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private ErrorCodeManager errorCodeManager;
	@Autowired
	private HotPromptManager hotPromptManager;
	@Autowired
	private GameDataManager gameDataManager;
	@Autowired
	private GameLogicManager logicManager;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public Object account_login_tourist(IoSession session, 
			String uuid) {
		AccountEntity accountEntity = accountManager.findOf_uuid(uuid);
		if(null == accountEntity){
			//注册
			String headImg = "";
			String nick = "游客"+session.getId();
			int sex = (int) ((Math.random()*100)%3);
			String clientIP = IpUtils.getIp(session);
			String starNO = starNOManager.getStarNO();
			accountEntity = accountManager.create(uuid, "", starNO, headImg, sex, nick, clientIP, 0);
		}else{
			//登录
			if(accountEntity.getState() < 0){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_UNUSED,
						errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_UNUSED), null);
			}
		}
		long accountId = accountEntity.getId();
		loginTransSession(accountId, session);
		AccountAttribVo retVo = accountManager.Account2AccountAttrib(accountEntity);
		return Result.valueOfSuccess(retVo);
	}	
	
	@Override
	public Object account_login_wx(IoSession session, String uuid, String password,
			String headImg, String nick, int sex) {
		AccountEntity accountEntity = accountManager.findOf_uuid(uuid);
		if(null == accountEntity){
			//注册
			String clientIP = IpUtils.getIp(session);
			String starNO = starNOManager.getStarNO();
			accountEntity = accountManager.create(uuid, password, starNO, headImg, sex, nick, clientIP, 0);
		}else{
			if(accountEntity.getState() < 0){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_UNUSED,
						errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_UNUSED), null);
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
			}
		}
		long accountId = accountEntity.getId();
		loginTransSession(accountId, session);
		AccountAttribVo retVo = accountManager.Account2AccountAttrib(accountEntity);		
		return Result.valueOfSuccess(retVo);
	}

	private void loginTransSession(long accountId, IoSession session){
		accountManager.accountOnLine(accountId, session);
		if (null != session) {
			IoSession oldSession = sessionManager.getSession(accountId);
			if (oldSession == null) {
				sessionManager.bind(session, accountId);
			} else {
				if (session != oldSession) {
					// 后面的人顶了前面的人,先关闭前面的连接
					sessionManager.unBind(oldSession, accountId);
					oldSession.close(false);
					sessionManager.bind(session, accountId);
				}
			}
			GameDataAttrib gameDataAttrib = gameDataManager.getGameData(accountId);
			gameDataAttrib.onLine = 1;
			if(gameDataAttrib.tableId > 0){
				logicManager.lock();
				try{
					TableAttrib table = logicManager.getTable(gameDataAttrib.tableId);
					if(null != table){
						table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
					}else{
						gameDataAttrib.tableId = 0;
					}
				}finally{
					logicManager.unLock();
				}
			}
			//检测推送红点消息
			hotPromptManager.checkNotifyHotPrompt(accountId, true);
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
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_ABNORMAL, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_ABNORMAL), null);
		}
		OtherAccountAttribVo retVo = accountManager.Account2OtherAttrib(accountEntity);
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object account_role_starNO(String starNO) {
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_ABNORMAL, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_ABNORMAL), null);
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
	public Object account_login_phone(IoSession session, String phone, String code) {
		AccountEntity accountEntity = accountManager.findOf_phone(phone);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_UN_EXIST, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_UN_EXIST), null);
		}
		String password = accountEntity.getPassword();
		if(password.equals(code) == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_PASSWORD, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_PASSWORD), null);
		}
		
		String uuid = "";
		String headImg = ""; 
		String nick = "";
		int sex = 1;
		uuid = accountEntity.getUuid();
		headImg = accountEntity.getHeadImg();
		nick = accountEntity.getNick();
		sex = accountEntity.getSex();
		@SuppressWarnings("rawtypes")
		Result retVo = (Result) this.account_login_wx(session, uuid, code, headImg, nick, sex);
		
		return retVo;
	}

	@Override
	public Object account_get_sms_code(String phone) {
		String code = authenticationManager.getSMSCode(phone, true);
		if(null == code){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_SMSCODE_NUM, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_SMSCODE_NUM), null);
		}
		if(code.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_SMSCODE_FAIL, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_SMSCODE_FAIL), null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object account_register(IoSession session, String phone, String code) {
		if(phone.isEmpty() || phone.length() != 11 || code.length() != 6){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_PARAM, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_PARAM), null);
		}
		
		AccountEntity accountEntity = accountManager.findOf_phone(phone);
		if(null != accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_REGISTER, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_REGISTER), null);
		}
		
		String uuid = phone;
		String headImg = ""; 
		String nick = "";
		int sex = 1;
		int nHeadImg = (int) (Math.random()*1000%15 + 1);
		headImg = String.valueOf(nHeadImg);
		nick = accountManager.getRandomNickName();
		
		@SuppressWarnings("rawtypes")
		Result retVo = (Result) this.account_login_wx(session, uuid, code, headImg, nick, sex);
		AccountAttribVo tmpVo = (AccountAttribVo) retVo.getContent();
		AuthenticationEntity entity = authenticationManager.loadOrCreate(Long.valueOf(tmpVo.accountId));
		entity.setPhone(phone);
		return retVo;
	}

	@Override
	public Object account_customer_service() {
		String customer = starNOManager.getCustomer();
		return Result.valueOfSuccess(customer);
	}	
}
