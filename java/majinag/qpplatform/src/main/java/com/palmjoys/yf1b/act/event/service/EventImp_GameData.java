package com.palmjoys.yf1b.act.event.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventAttrib_Game;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.event.resource.EventConfig;
import com.palmjoys.yf1b.act.framework.utils.HttpClientUtils;

@Service
public class EventImp_GameData implements EventService{
	@Static
	private Storage<Integer, EventConfig> eventCfgs;

	@Override
	public int getEventId() {
		return EventDefine.EVENT_GAME_DATA;
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
			
			String create_start_no = (String) param1;
			String create_time = (String) param2;
			String consume_start_no = (String) param3;
			String amount = (String) param4;
			@SuppressWarnings("unchecked")
			List<String> playerList = (List<String>) param5;
			
			EventAttrib_Game msgAttrib = new EventAttrib_Game();
			msgAttrib.create_start_no = create_start_no;
			msgAttrib.create_time = create_time;
			msgAttrib.consume_start_no = consume_start_no;
			msgAttrib.amount = amount;
			msgAttrib.playerList = playerList;
						
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
