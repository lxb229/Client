package com.palmjoys.yf1b.act.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;
import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventAttrib_Account;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.event.resource.EventConfig;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.entity.AuthenticationEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.manager.AuthenticationManager;
import com.palmjoys.yf1b.act.framework.utils.HttpClientUtils;

@Service
public class EventImp_Account implements EventService{
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Static
	private Storage<Integer, EventConfig> eventCfgs;

	@Override
	public int getEventId() {
		return EventDefine.EVENT_ACCOUNT;
	}

	@Override
	public boolean execEvent(EventAttrib eventAttrib) {
		boolean bOK = false;
		try{
			if(eventAttrib.params.size() != 2){
				return false;
			}
			
			Object param1 = eventAttrib.params.get(0);
			Object param2 = eventAttrib.params.get(1);
			
			Integer cmdId = (Integer) param1;
			Long accountId = (Long) param2;
			AccountEntity accountEntity = accountManager.load(accountId);
			if(null == accountEntity){
				return false;
			}
			
			EventAttrib_Account msgAttrib = new EventAttrib_Account();
			msgAttrib.cmd = cmdId;
			msgAttrib.start_no = accountEntity.getStarNO();
			msgAttrib.nick = accountEntity.getNick();
			msgAttrib.sex = String.valueOf(accountEntity.getSex());
			msgAttrib.head_img = accountEntity.getHeadImg();
			msgAttrib.status = String.valueOf(accountEntity.getState());
			msgAttrib.create_time = String.valueOf(accountEntity.getCreateTime());
			msgAttrib.create_ip = accountEntity.getCreateIP();
			
			if(msgAttrib.cmd == 3 || msgAttrib.cmd == 4){
				AuthenticationEntity authenticationEntity = authenticationManager.loadOrCreate(accountId);
				msgAttrib.phone = authenticationEntity.getPhone();
				msgAttrib.real_name = authenticationEntity.getName();
				msgAttrib.card_no = authenticationEntity.getCardId();
			}			
			String sendStr = JsonUtils.object2String(msgAttrib);
			String url = eventCfgs.get(getEventId(), false).getPushAddr();
			String retStr = HttpClientUtils.executeByPost(url, sendStr);
			if(null != retStr){
				return true;
			}
		}catch(Exception e){
			bOK = false;
		}
		
		return bOK;
	}

}
