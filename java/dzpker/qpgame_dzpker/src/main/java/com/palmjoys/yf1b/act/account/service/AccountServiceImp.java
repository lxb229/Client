package com.palmjoys.yf1b.act.account.service;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.network.model.Result;
import org.treediagram.nina.network.util.IpUtils;

import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;
import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.account.entity.AuthenticationEntity;
import com.palmjoys.yf1b.act.account.entity.RoleEntity;
import com.palmjoys.yf1b.act.account.manager.AccountManager;
import com.palmjoys.yf1b.act.account.manager.AuthenticationManager;
import com.palmjoys.yf1b.act.account.manager.GameDataManager;
import com.palmjoys.yf1b.act.account.manager.RoleEntityManager;
import com.palmjoys.yf1b.act.account.manager.StarNOManager;
import com.palmjoys.yf1b.act.account.model.AccountAttribVo;
import com.palmjoys.yf1b.act.account.model.AccountDefine;
import com.palmjoys.yf1b.act.account.model.AuthenticationVo;
import com.palmjoys.yf1b.act.account.model.GameDataAttrib;
import com.palmjoys.yf1b.act.account.model.OtherAccountAttribVo;
import com.palmjoys.yf1b.act.framework.common.manager.ErrorCodeManager;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

@Service
public class AccountServiceImp implements AccountService {
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private StarNOManager starNOManager;
	@Autowired
	private ErrorCodeManager errorCodeManager;
	@Autowired
	private RoleEntityManager roleEntityManager;
	@Autowired
	private GameDataManager gameDataManager;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private EventTriggerManager eventTriggerManager;
	
	@Override
	public Object account_register(IoSession session, String account, String password, String headImg, String nick, int sex) {
		if(accountManager.canLogin("") == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_SEVER_CLOSE, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_SEVER_CLOSE), null);
		}
		
		AccountEntity accountEntity = accountManager.findOf_uuid(account);
		if(null != accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_EXIST, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_EXIST), null);
		}
		String starNO = starNOManager.createStarNO();
		if(null == starNO){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_EXIST, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_EXIST), null);
		}
		String createIP = IpUtils.getIp(session);
		accountEntity = accountManager.create(account, password, createIP, 0, AccountManager.ACCOUNT_CREATE_TYPE_PASSWORD);
		roleEntityManager.create(starNO, accountEntity.getAccountId(), headImg, sex, nick);
		walletManager.loadOrCreate(accountEntity.getAccountId());
				
		accountManager.accountOnLine(accountEntity, session);
		AccountAttribVo retVo = accountManager.Account2AccountAttrib(accountEntity);
		
		EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_ACCOUNT_CREATE);
		eventAttrib.addEventParam(retVo.roleAttribVo.starNO);
		eventAttrib.addEventParam(retVo.roleAttribVo.state);
		eventAttrib.addEventParam(Long.parseLong(retVo.roleAttribVo.createTime));
		eventAttrib.addEventParam(retVo.roleAttribVo.nick);
		eventAttrib.addEventParam(accountEntity.getCreateType());
		eventTriggerManager.triggerEvent(eventAttrib);
		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object account_login_password(IoSession session, String account, String password) {
		if(accountManager.canLogin(account) == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_SEVER_CLOSE, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_SEVER_CLOSE), null);
		}
		
		AccountEntity accountEntity = accountManager.findOf_uuid(account);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_PASSWORD, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_PASSWORD), null);
		}
		if(password.equalsIgnoreCase(accountEntity.getPassword()) == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_PASSWORD, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_PASSWORD), null);
		}		
		accountManager.accountOnLine(accountEntity, session);
		AccountAttribVo retVo = accountManager.Account2AccountAttrib(accountEntity);
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object account_login_tourist(IoSession session, 
			String uuid) {
		if(accountManager.canLogin(uuid) == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_SEVER_CLOSE, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_SEVER_CLOSE), null);
		}
		boolean bRegister = false;
		AccountEntity accountEntity = accountManager.findOf_uuid(uuid);
		if(null == accountEntity){
			bRegister = true;
			//注册
			String starNO = starNOManager.createStarNO();
			if(null == starNO){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_EXIST, 
						errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_EXIST), null);
			}
			
			String headImg = "";
			String nick = accountManager.getRandomNickName();
			int sex = (int) ((Math.random()*100)%3);
			String clientIP = IpUtils.getIp(session);
			accountEntity = accountManager.create(uuid, "", clientIP, 0, AccountManager.ACCOUNT_CREATE_TYPE_TOURIST);
			roleEntityManager.create(starNO, accountEntity.getAccountId(), headImg, sex, nick);
			walletManager.loadOrCreate(accountEntity.getAccountId());
		}else{
			//登录
			if(accountEntity.getState() < 0){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_UNUSED,
						errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_UNUSED), null);
			}
		}		
		accountManager.accountOnLine(accountEntity, session);
		AccountAttribVo retVo = accountManager.Account2AccountAttrib(accountEntity);
		
		if(bRegister){
			EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_ACCOUNT_CREATE);
			eventAttrib.addEventParam(retVo.roleAttribVo.starNO);
			eventAttrib.addEventParam(retVo.roleAttribVo.state);
			eventAttrib.addEventParam(Long.parseLong(retVo.roleAttribVo.createTime));
			eventAttrib.addEventParam(retVo.roleAttribVo.nick);
			eventAttrib.addEventParam(accountEntity.getCreateType());
			eventTriggerManager.triggerEvent(eventAttrib);
		}
		
		return Result.valueOfSuccess(retVo);
	}	
	
	@Override
	public Object account_login_wx(IoSession session, String uuid, 
			String headImg, String nick, int sex) {
		if(accountManager.canLogin(uuid) == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_SEVER_CLOSE, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_SEVER_CLOSE), null);
		}
		boolean bRegister = false;
		AccountEntity accountEntity = accountManager.findOf_uuid(uuid);
		if(null == accountEntity){
			bRegister = true;
			//注册
			String starNO = starNOManager.createStarNO();
			if(null == starNO){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_EXIST, 
						errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_EXIST), null);
			}
			String clientIP = IpUtils.getIp(session);
			accountEntity = accountManager.create(uuid, "", clientIP, 0, AccountManager.ACCOUNT_CREATE_TYPE_WX);
			roleEntityManager.create(starNO, accountEntity.getAccountId(), headImg, sex, nick);
			walletManager.loadOrCreate(accountEntity.getAccountId());
		}else{
			if(accountEntity.getState() < 0){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_UNUSED,
						errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_UNUSED), null);
			}
			RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountEntity.getAccountId());
			if(null == roleEntity){
				String starNO = starNOManager.createStarNO();
				if(null == starNO){
					return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_EXIST, 
							errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_EXIST), null);
				}
				roleEntity = roleEntityManager.create(starNO, accountEntity.getAccountId(), headImg, sex, nick);
			}
			
			roleEntity.setHeadImg(headImg);
			roleEntity.setNick(nick);
			roleEntity.setSex(sex);
		}		
		accountManager.accountOnLine(accountEntity, session);
		AccountAttribVo retVo = accountManager.Account2AccountAttrib(accountEntity);
		
		if(bRegister){
			EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_ACCOUNT_CREATE);
			eventAttrib.addEventParam(retVo.roleAttribVo.starNO);
			eventAttrib.addEventParam(retVo.roleAttribVo.state);
			eventAttrib.addEventParam(Long.parseLong(retVo.roleAttribVo.createTime));
			eventAttrib.addEventParam(retVo.roleAttribVo.nick);
			eventAttrib.addEventParam(accountEntity.getCreateType());
			eventTriggerManager.triggerEvent(eventAttrib);
		}
		
		return Result.valueOfSuccess(retVo);
	}
	
	@Override
	public Object account_login_accountId(IoSession session, String accountId) {
		AccountEntity accountEntity = accountManager.findOf_accountId(Long.valueOf(accountId));
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_ABNORMAL, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_ABNORMAL), null);
		}
		return this.account_login_tourist(session, accountEntity.getUuid());
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
		AccountEntity accountEntity = accountManager.findOf_accountId(n_accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_ABNORMAL, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_ABNORMAL), null);
		}
		OtherAccountAttribVo retVo = accountManager.Account2OtherAttrib(accountEntity);
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object account_role_starNO(String starNO) {
		RoleEntity roleEntity = roleEntityManager.findOf_starNO(starNO);
		if(null == roleEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_ABNORMAL, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_ABNORMAL), null);
		}
		AccountEntity accountEntity = accountManager.findOf_accountId(roleEntity.getAccountId());
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_ABNORMAL, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_ABNORMAL), null);
		}
		OtherAccountAttribVo retVo = accountManager.Account2OtherAttrib(accountEntity);
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object account_ping(Long accountId, int ping) {
		GameDataAttrib dataAttrib = gameDataManager.getAccountGameData(accountId);
		dataAttrib.pingVal = ping;
		return Result.valueOfSuccess();
	}

	@Override
	public Object account_authentication(Long accountId, String name, String cardId) {
		AuthenticationEntity entity = authenticationManager.loadOrCreate(accountId);
		if(entity.getName().isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_AUTHICATION, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_AUTHICATION), null);
		}
		entity.setName(name);
		entity.setCardId(cardId);
		AuthenticationVo retVo = authenticationManager.getAuthenticationData(accountId);
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object account_phone_bind(Long accountId, String phone, String vaildCode) {
		AuthenticationEntity entity = authenticationManager.findOf_phone(phone);
		if(entity.getPhone().isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_PHONE_BIND, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_PHONE_BIND), null);
		}
		boolean bVaild = authenticationManager.smsCodeVaild(phone, vaildCode);
		if(bVaild == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_PHONE_SMSCODE, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_PHONE_SMSCODE), null);
		}
		entity.setPhone(phone);
		
		AuthenticationVo retVo = authenticationManager.getAuthenticationData(accountId);
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object account_phone_get_smsCode(Long accountId, String phone) {
		String smsCode = authenticationManager.getSMSCode(phone);
		if(null == smsCode){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_PHONE_SMSCODE_TIME, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_PHONE_SMSCODE_TIME), null);
		}
		if(smsCode.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_PHONE_SMSCODE_FIAL, 
					errorCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_PHONE_SMSCODE_FIAL), null);
		}
		return Result.valueOfSuccess();
	}
	
}
