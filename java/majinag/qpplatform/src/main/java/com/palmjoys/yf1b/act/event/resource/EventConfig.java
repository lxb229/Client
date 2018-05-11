package com.palmjoys.yf1b.act.event.resource;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

@ResourceType("event")
public class EventConfig {
	//事件Id
	@ResourceId
	private int eventId;
	//事件上报地址
	private String pushAddr;
	
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public String getPushAddr() {
		return pushAddr;
	}
	public void setPushAddr(String pushAddr) {
		this.pushAddr = pushAddr;
	}
	
}
