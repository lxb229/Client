package com.palmjoys.yf1b.act.event.service;

import org.springframework.stereotype.Service;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventData_Table_Remove;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.event.resource.EventConfig;
import com.palmjoys.yf1b.act.framework.utils.HttpClientUtils;

@Service
public class EventServiceImp_Table_Remove implements EventService{
	@Static
	private Storage<Integer, EventConfig> eventCfgs;

	@Override
	public int getEventId() {
		return EventDefine.EVENT_TABLE_REMOVE;
	}

	@Override
	public boolean execEvent(EventAttrib eventAttrib) {
		try{
			String url = eventCfgs.get(getEventId(), false).getPushAddr();
			
			Object param1 = eventAttrib.params.get(0);
			Object param2 = eventAttrib.params.get(1);
			Object param3 = eventAttrib.params.get(2);
					
			EventData_Table_Remove msgVo = new EventData_Table_Remove();
			msgVo.roomId = (Integer)param1;
			msgVo.houseOwner = (String)param2;
			msgVo.disappearTime = (Long)param3;
			
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
