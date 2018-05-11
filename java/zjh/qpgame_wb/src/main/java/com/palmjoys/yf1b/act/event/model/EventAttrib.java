package com.palmjoys.yf1b.act.event.model;

import java.util.ArrayList;
import java.util.List;

import org.treediagram.nina.core.time.DateUtils;

public class EventAttrib {
	//事件Id
	public int eventId;
	//事件参数
	public List<Object> params;
	//事件处理失败后尝试执行次数
	public int fail;
	//事件发生时间
	public long eventTime;
	
	public EventAttrib(int eventId){
		this.eventId = eventId;
		this.params = new ArrayList<Object>();
		this.fail = 1;
		this.eventTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
	}
	
	public void addEventParam(Object param){
		this.params.add(param);
	}
	
	public void addEventParams(List<Object> params){
		this.params.addAll(params);
	}
}

