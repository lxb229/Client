package com.palmjoys.yf1b.act.framework.common.resource;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

@ResourceType("common")
public class ForbiddenWordConfig {
	@ResourceId
	private int id;
	//屏蔽词
	private String name;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
}
