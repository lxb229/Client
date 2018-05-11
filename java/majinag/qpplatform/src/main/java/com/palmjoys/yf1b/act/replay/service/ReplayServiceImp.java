package com.palmjoys.yf1b.act.replay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;
import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.entity.AuthenticationEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.manager.AuthenticationManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.replay.entity.RecordEntity;
import com.palmjoys.yf1b.act.replay.manager.RecordManager;
import com.palmjoys.yf1b.act.replay.model.RecordDetailedVo;
import com.palmjoys.yf1b.act.replay.model.RecordVo;

@Service
public class ReplayServiceImp implements ReplayService{
	@Autowired
	private RecordManager recordManager;
	@Autowired
	private AuthenticationManager authenicationManager;
	@Autowired
	private EventTriggerManager eventTriggerManager;
	@Autowired
	private AccountManager accountManager;

	@Override
	public Object replay_query_record(Long accountId, int type, String query) {
		if(1 != type && 2 != type){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		RecordVo retVo = recordManager.queryRecord(type, query);
		retVo.sort();
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object replay_query_detailed_record(Long accountId, long recordId) {
		RecordDetailedVo retVo = recordManager.queryDetailed(recordId);
		if(null == retVo){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object replay_delete_record(Long accountId, long recordId) {
		recordManager.lock();
		try{
			do{
				RecordEntity recordEntity = recordManager.load(recordId);
				if(null == recordEntity){
					break;
				}
				String corpsId = recordEntity.getCorpsId();
				if(corpsId.equalsIgnoreCase("0")){
					break;
				}
				recordEntity.setCorpsId("0");
			}while(false);
		}finally{
			recordManager.unLock();
		}
		
		return Result.valueOfSuccess();
	}

	@Override
	public Object replay_realname_authentication(Long accountId, String name, String cardId) {
		if(name.length() < 2 ||	cardId.length() != 18){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		AuthenticationEntity entity = authenicationManager.loadOrCreate(accountId);
		entity.setName(name);
		entity.setCardId(cardId);
		
		EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_ACCOUNT);
		Integer cmdId = 4;
		eventAttrib.addEventParam(cmdId);
		eventAttrib.addEventParam(accountId);
		eventTriggerManager.triggerEvent(eventAttrib);
		
		return Result.valueOfSuccess();
	}

	@Override
	public Object replay_phone_get_smsCode(Long accountId, String phone) {
		//检查一个手机只能绑定一个帐号
		AccountEntity accountEntity = accountManager.findOf_phone(phone);
		if(null != accountEntity && accountEntity.getId().longValue() != accountId.longValue()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "此帐号已和其它手机号绑定", null);
		}
		
		String smsCode = authenicationManager.getSMSCode(phone);
		if(null == smsCode){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "验码证获取频繁,稍后重试", null);
		}
		if(smsCode.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "验证码获取失败,稍后重试", null);
		}
		return Result.valueOfSuccess();
	}

	@Override
	public Object replay_phone_bind(Long accountId, String phone, String vaildCode) {
		boolean bOK = authenicationManager.smsCodeVaild(phone, vaildCode);
		if(bOK == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "验证码验证失败", null);
		}
		AuthenticationEntity entity = authenicationManager.loadOrCreate(accountId);
		entity.setPhone(phone);
		String sPhone = authenicationManager.phoneEncryption(phone);
		
		EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_ACCOUNT);
		Integer cmdId = 3;
		eventAttrib.addEventParam(cmdId);
		eventAttrib.addEventParam(accountId);
		eventTriggerManager.triggerEvent(eventAttrib);
		
		return Result.valueOfSuccess(sPhone);
	}

}
