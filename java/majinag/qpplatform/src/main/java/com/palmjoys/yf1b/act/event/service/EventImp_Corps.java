package com.palmjoys.yf1b.act.event.service;

import org.springframework.stereotype.Service;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventAttrib_Corps;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.event.resource.EventConfig;
import com.palmjoys.yf1b.act.framework.utils.HttpClientUtils;

@Service
public class EventImp_Corps implements EventService{
	@Static
	private Storage<Integer, EventConfig> eventCfgs;

	@Override
	public int getEventId() {
		return EventDefine.EVENT_CORPS;
	}

	@Override
	public boolean execEvent(EventAttrib eventAttrib) {
		boolean bOK = false;
		try{
			
			Object param1 = eventAttrib.params.get(0);
			Integer cmd = (Integer) param1;
			
			EventAttrib_Corps msgAttrib = new EventAttrib_Corps();
			msgAttrib.cmd = cmd;
			if(cmd == 1){
				String mahjong_no = (String) eventAttrib.params.get(1);
				String mahjong_name = (String) eventAttrib.params.get(2);
				String mahjong_wechat = (String) eventAttrib.params.get(3);
				String start_no = (String) eventAttrib.params.get(4);
				String pattern = (String) eventAttrib.params.get(5);
				String usable_amount = (String) eventAttrib.params.get(6);
				String create_time = (String) eventAttrib.params.get(7);
				msgAttrib.addCmd1Data(mahjong_no, mahjong_name, mahjong_wechat,
						start_no, pattern, usable_amount, create_time);				
			}else if(cmd == 2){
				String mahjong_no = (String) eventAttrib.params.get(1);
				String start_no = (String) eventAttrib.params.get(2);
				String create_time = (String) eventAttrib.params.get(3);
				msgAttrib.addCmd2Data(mahjong_no, start_no, create_time);
			}else if(cmd == 3){
				String mahjong_no = (String) eventAttrib.params.get(1);
				String old_start_no = (String) eventAttrib.params.get(2);
				String new_start_no = (String) eventAttrib.params.get(3);
				String create_time = (String) eventAttrib.params.get(4);
				msgAttrib.addCmd3Data(mahjong_no, old_start_no, new_start_no, create_time);
			}else if(cmd == 4){
				String mahjong_no = (String) eventAttrib.params.get(1);
				String wxNO = (String) eventAttrib.params.get(2);
				String create_time = (String) eventAttrib.params.get(3);
				msgAttrib.addCmd4Data(mahjong_no, wxNO, create_time);
			}else{
				return false;
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
