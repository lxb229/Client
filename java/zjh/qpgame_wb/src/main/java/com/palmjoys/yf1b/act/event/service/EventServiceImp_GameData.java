package com.palmjoys.yf1b.act.event.service;

import org.springframework.stereotype.Service;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventData_Game;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.event.resource.EventConfig;
import com.palmjoys.yf1b.act.framework.utils.HttpClientUtils;

@Service
public class EventServiceImp_GameData implements EventService{
	@Static
	private Storage<Integer, EventConfig> eventNotifyCfgs;

	@Override
	public int getEventId() {
		return EventDefine.EVENT_GAME;
	}

	@Override
	public boolean execEvent(EventAttrib eventAttrib) {
		if(eventAttrib.params.size() != 4){
			return false;
		}
		try{
			Object param1 = eventAttrib.params.get(0);
			Object param2 = eventAttrib.params.get(1);
			Object param3 = eventAttrib.params.get(2);
			Object param4 = eventAttrib.params.get(3);
			
			EventData_Game dataAttrib = new EventData_Game();
			dataAttrib.starNO = (String) param1;
			dataAttrib.eventTime = (String) param2;
			dataAttrib.score = (Integer) param3;
			dataAttrib.gameType = (Integer)param4;
			
			String EVENT_NOTFIY_ADDR = eventNotifyCfgs.get(getEventId(), false).getPushAddr();
			String jsonStr = JsonUtils.object2String(dataAttrib);
			String retStr = HttpClientUtils.executeByPost(EVENT_NOTFIY_ADDR, jsonStr);
			if(null == retStr)
				return false;
		}catch(Exception e){
			return false;
		}		
		return true;
	}

}
