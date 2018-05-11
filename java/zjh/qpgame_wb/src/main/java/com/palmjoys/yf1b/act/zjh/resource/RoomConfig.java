package com.palmjoys.yf1b.act.zjh.resource;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

@ResourceType("zjh")
public class RoomConfig {
	@ResourceId
	private int id;
	//条目名称
	private String itemName;
	//桌子类型
	private int tableType;
	//进入限制
	private int joinLimit;
	//桌子底分
	private int baseScore;
	//单注最大封顶
	private int onceMax;
	//抽水
	private int charge;
	//初始化桌子数
	private int tableNum;
	//场次机器人
	private int robotNum;
	
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
	public int getTableType() {
		return tableType;
	}
	public void setTableType(int tableType) {
		this.tableType = tableType;
	}
	public int getJoinLimit() {
		return joinLimit;
	}
	public void setJoinLimit(int joinLimit) {
		this.joinLimit = joinLimit;
	}
	public int getBaseScore() {
		return baseScore;
	}
	public void setBaseScore(int baseScore) {
		this.baseScore = baseScore;
	}
	public int getOnceMax() {
		return onceMax;
	}
	public void setOnceMax(int onceMax) {
		this.onceMax = onceMax;
	}
	public int getCharge() {
		return charge;
	}
	public void setCharge(int charge) {
		this.charge = charge;
	}
	public int getTableNum() {
		return tableNum;
	}
	public void setTableNum(int tableNum) {
		this.tableNum = tableNum;
	}
	public int getRobotNum() {
		return robotNum;
	}
	public void setRobotNum(int robotNum) {
		this.robotNum = robotNum;
	}
	

}
