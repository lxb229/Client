package com.palmjoys.yf1b.act.zjh.model;

public class GameDataAttrib {
	//帐号Id
	public long accountId;
	//所在桌子Id
	public int tableId;
	//在线状态(0=不在线,1=在线)
	public int onLine;
	//机器人换桌局数(动态的,每个机器人,每次加入桌子都不同)
	public int swapTable;
	//是否机器人
	public int robot;
	//一个人在桌子上的等待时间
	public long onceWaitTime;
	//连续不准备踢出桌子
	public int unReadyKick;
	//更换名字时间
	public long updateNickTime;
	
	public GameDataAttrib(long accountId){
		this.tableId = 0;
		this.accountId = accountId;
	}
}
