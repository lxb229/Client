package com.palmjoys.yf1b.act.majiang.model;

//玩家游戏保存数据
public class GameDataAttrib {
	//所在桌子Id
	public int tableId;
	//在线状态(0=不在线,1=在线)
	public int onLine;
	//总结算桌子数据
	public TableVo allOverVo;
	//掉线计时
	public long offlineTime;
	//系统自动代打计时时间
	public long autoBtTime;
	//聊天间隔时间
	public long chatInnerTime;
	//帐号类型(0=玩家,1=机器人)
	public int robot;
	
	public GameDataAttrib(){
		this.tableId = 0;
		this.offlineTime = 0;
		this.autoBtTime = 0;
		this.chatInnerTime = 0;
		this.robot = 0;
	}
}
