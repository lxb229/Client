package com.palmjoys.yf1b.act.event.model;

import java.util.ArrayList;
import java.util.List;

public class EventAttrib_Corps_GameData {
	//帮会Id
	public String mahjong_no;
	//玩法类型
	public String game_type;
	//消耗房卡数
	public String consume;
	//活跃度
	public String liveness;
	//参与玩家集合
	public List<EventAttrib_Corps_GameData_Score> list = new ArrayList<>();
	//时间
	public String create_time;
	
}
