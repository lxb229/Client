package com.palmjoys.yf1b.act.event.model;

public class EventData_Table_Create {
	//桌子Id
	public int roomId;
	//桌子名称
	public String roomName;
	//房主明星号
	public String houseOwner;
	//房主呢称
	public String ownerName;
	//创建时间
	public long createTime;
	//房间设置游戏时长
	public int timeSet;
	//大小盲注
	public String sizeBlind;
	//最小座下筹码
	public int jettonSet;
	//是否开启保险
	public int insurance;
	//是否开启闭眼盲
	public int straddle;
}
