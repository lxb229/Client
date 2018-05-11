package com.palmjoys.yf1b.act.event.service;

import org.springframework.stereotype.Service;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;
import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventAttrib_CorpsMember;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.event.resource.EventConfig;
import com.palmjoys.yf1b.act.framework.utils.HttpClientUtils;

@Service
public class EventImp_CorpsMember implements EventService{
	@Static
	private Storage<Integer, EventConfig> eventCfgs;

	@Override
	public int getEventId() {
		return EventDefine.EVENT_CORPS_MEMBER;
	}

	@Override
	public boolean execEvent(EventAttrib eventAttrib) {
		boolean bOK = false;
		try{
			Object param1 = eventAttrib.params.get(0);
			Object param2 = eventAttrib.params.get(1);
			Object param3 = eventAttrib.params.get(2);
			Object param4 = eventAttrib.params.get(3);
			
			Integer cmdId = (Integer) param1;
			String mahjong_no = (String) param2;
			String start_no = (String) param3;
			String create_time = (String) param4;
			
			EventAttrib_CorpsMember msgAttrib = new EventAttrib_CorpsMember();
			msgAttrib.cmd = cmdId;
			msgAttrib.mahjong_no = mahjong_no;
			msgAttrib.start_no = start_no;
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
