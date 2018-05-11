package com.palmjoys.yf1b.act.event.service;

import com.palmjoys.yf1b.act.event.model.EventAttrib;

public interface EventService {
	//获取事件Id
	int getEventId();
	//执行事件,true=成功,false=失败
	boolean execEvent(EventAttrib eventAttrib);
}
