package com.palmjoys.yf1b.act.framework.common.resource;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

@ResourceType("common")
public class GameObjectConfig {
	@ResourceId
	private int id;
	//物件类型
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
