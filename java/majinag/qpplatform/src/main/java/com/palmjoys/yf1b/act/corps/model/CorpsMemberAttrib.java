package com.palmjoys.yf1b.act.corps.model;

//帮会成员属性
public class CorpsMemberAttrib {
	//加入时间
	public long joinTime;
	//游戏活跃度(每游戏一轮加一点,固定时间重置)
	public int activeValue;
	//游戏圈数(固定时间重置)
	public int gameRoundNum;
	//大赢家游戏局数(固定时间重置)
	public int winMaxNum;
	//积累积分(固定时间重置)
	public int score;
	//捐献房卡数(永久)
	public long giveCardNum;
	
	public CorpsMemberAttrib(){
		this.joinTime = 0;
		this.activeValue = 0;
		this.gameRoundNum = 0;
		this.winMaxNum = 0;
		this.score = 0;
		this.giveCardNum = 0;
	}
	
	public void reset(){
		this.activeValue = 0;
		this.gameRoundNum = 0;
		this.winMaxNum = 0;
		this.score = 0;
	}
}
