package com.palmjoys.yf1b.act.majiang.resource;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

import com.palmjoys.yf1b.act.majiang.model.config.RuleCfgAttrib;

@ResourceType("majiang")
public class RoomItemConfig {
	@ResourceId
	private int id;
	//条目名称
	private String itemName;
	//配置的规则列表
	private RuleCfgAttrib[] rules;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public RuleCfgAttrib[] getRules() {
		return rules;
	}
	public void setRules(RuleCfgAttrib[] rules) {
		this.rules = rules;
	}
}
