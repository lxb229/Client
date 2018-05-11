package com.palmjoys.yf1b.act.event.service;

import org.springframework.stereotype.Service;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventData_Account_Create;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.event.resource.EventConfig;
import com.palmjoys.yf1b.act.framework.utils.HttpClientUtils;

@Service
public class EventServiceImp_Account_Create implements EventService{
	@Static
	private Storage<Integer, EventConfig> eventCfgs;

	@Override
	public int getEventId() {
		return EventDefine.EVENT_ACCOUNT_CREATE;
	}

	@Override
	public boolean execEvent(EventAttrib eventAttrib) {
		try{
			String url = eventCfgs.get(getEventId(), false).getPushAddr();
			
			Object param1 = eventAttrib.params.get(0);
			Object param2 = eventAttrib.params.get(1);
			Object param3 = eventAttrib.params.get(2);
			Object param4 = eventAttrib.params.get(3);
			Object param5 = eventAttrib.params.get(4);
					
			EventData_Account_Create msgVo = new EventData_Account_Create();
			msgVo.playId = (String)param1;
			msgVo.playStatus = (Integer)param2;
			msgVo.createTime = (Long)param3;
			msgVo.playNick = (String)param4;
			msgVo.createType = (Integer)param5;
			
			String sendStr = JsonUtils.object2String(msgVo);
			String retStr = HttpClientUtils.executeByPost(url, sendStr);
			if(null != retStr){
				return true;
			}
		}catch(Exception e){
			return false;
		}		
		return false;
	}

}
