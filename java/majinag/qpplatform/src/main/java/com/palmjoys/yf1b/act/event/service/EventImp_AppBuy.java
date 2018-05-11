package com.palmjoys.yf1b.act.event.service;

import org.springframework.stereotype.Service;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventAttrib_AppBuy;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.event.resource.EventConfig;
import com.palmjoys.yf1b.act.framework.utils.HttpClientUtils;

@Service
public class EventImp_AppBuy implements EventService{
	@Static
	private Storage<Integer, EventConfig> eventCfgs;

	@Override
	public int getEventId() {
		return EventDefine.EVENT_APP_BUY;
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
			
			String order_no = (String) param1;
			String start_no = (String) param2;
			String pay_price = (String) param3;
			String pay_time = (String) param4;
			int roomcard_amount = (Integer) param5;
			
			EventAttrib_AppBuy msgAttrib = new EventAttrib_AppBuy();
			msgAttrib.order_no = order_no;
			msgAttrib.start_no = start_no;
			msgAttrib.pay_price = pay_price;
			msgAttrib.pay_price = pay_price;
			msgAttrib.pay_time = pay_time;
			msgAttrib.roomcard_amount = roomcard_amount;
						
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
