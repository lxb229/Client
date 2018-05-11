package com.palmjoys.yf1b.act.event.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventAttrib_Corps_GameData;
import com.palmjoys.yf1b.act.event.model.EventAttrib_Corps_GameData_Score;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.event.resource.EventConfig;
import com.palmjoys.yf1b.act.framework.utils.HttpClientUtils;

@Service
public class EventImp_Corps_GameData implements EventService{
	@Static
	private Storage<Integer, EventConfig> eventCfgs;
	
	@Override
	public int getEventId() {
		return EventDefine.EVENT_CORPS_GAME_DATA;
	}

	@Override
	public boolean execEvent(EventAttrib eventAttrib) {
		boolean bOK = false;
		try{
			Object param1 = eventAttrib.params.get(0);
			Object param2 = eventAttrib.params.get(1);
			Object param3 = eventAttrib.params.get(2);
			Object param4 = eventAttrib.params.get(3);
			Object param5 = eventAttrib.params.get(4);
			Object param6 = eventAttrib.params.get(5);
			
			String mahjong_no = (String) param1;
			String game_type = (String) param2;
			String consume = (String) param3;
			String liveness = (String) param4;
			@SuppressWarnings("unchecked")
			List<EventAttrib_Corps_GameData_Score> playerList = (List<EventAttrib_Corps_GameData_Score>) param5;
			String create_time = (String) param6;
			
			EventAttrib_Corps_GameData msgAttrib = new EventAttrib_Corps_GameData();
			msgAttrib.mahjong_no = mahjong_no;
			msgAttrib.game_type = game_type;
			msgAttrib.consume = consume;
			msgAttrib.liveness = liveness;
			msgAttrib.list = playerList;
			msgAttrib.create_time = create_time;
						
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
