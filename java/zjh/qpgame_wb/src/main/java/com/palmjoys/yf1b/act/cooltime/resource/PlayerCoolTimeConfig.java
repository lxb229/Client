package com.palmjoys.yf1b.act.cooltime.resource;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

import com.palmjoys.yf1b.act.cooltime.model.CoolTimeCondition;

/*角色冷却时间配置类对应PlayerCoolTimeConfig.xls*/
@ResourceType("cooltime")
public class PlayerCoolTimeConfig {

	/**
	 *功能点编号
	 * */
	@ResourceId
	public int id;
	
	/**
	 * 重置条件
	 * */
	public CoolTimeCondition []checkReset;		
}
