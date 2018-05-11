package com.palmjoys.yf1b.act.task.resource;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

import com.palmjoys.yf1b.act.condition.model.ConditionAttrib;
import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;

@ResourceType("task")
public class TaskConfig {
	@ResourceId
	private int id;
	//任务类型
	public int type;
	//完成条件描述
	private String finshDesc;
	//完成奖励描述
	private String rewardDesc;
	//完成条件
	private ConditionAttrib []finshCondition;
	//任务奖励
	private GameObject []rewares;
	//排序
	private int sort;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFinshDesc() {
		return finshDesc;
	}
	public void setFinshDesc(String finshDesc) {
		this.finshDesc = finshDesc;
	}
	public String getRewardDesc() {
		return rewardDesc;
	}
	public void setRewardDesc(String rewardDesc) {
		this.rewardDesc = rewardDesc;
	}
	public ConditionAttrib[] getFinshCondition() {
		return finshCondition;
	}
	public void setFinshCondition(ConditionAttrib[] finshCondition) {
		this.finshCondition = finshCondition;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public GameObject[] getRewares() {
		return rewares;
	}
	public void setRewares(GameObject[] rewares) {
		this.rewares = rewares;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
