package com.palmjoys.yf1b.act.majiang.resource;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;

@ResourceType("majiang")
public class RoomRuleConfig {
	@ResourceId
	private int id;
	//条目名称
	private String itemName;
	//前置消耗
	private GameObject[] cost;
	//规则描述
	private String desc;
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public GameObject[] getCost() {
		return cost;
	}
	public void setCost(GameObject[] cost) {
		this.cost = cost;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	
}
