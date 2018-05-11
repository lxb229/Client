package com.palmjoys.yf1b.act.dzpker.model;

public class StatisticsAttrib {
	//参加游戏总局数
	public long gameTotalNum;
	//赢了的局数
	public long winNum;
	//输了的局数
	public long lostNum;
	//最大下注筹码数
	public long maxBetMoney;
	//最大赢取筹码数
	public long maxWinMoney;
	//总购买筹码数
	public long buyTotalMoney;
	//玩家当前筹码数
	public long currMoney;
	//游戏中购买的筹码(下局游戏开始加到当前筹码中)
	public long gameBuyChip;
	//一局游戏总下注(本局结算后清零)
	public long betTotalMoney;
	//保险玩法输赢筹码数
	public long insuranceMoney;
	//总进入桌子次数
	public long joinTableNum;
	//在桌子上坐下次数
	public long seatDownNum;
	//主动亮牌次数
	public long showCardNum;
	//其它玩家弃牌后赢取的次数
	public long dropWinNum;
	//第一轮加注次数
	public long addChipNum;
	//总表态次数
	public long totalBtNum;
	//总弃牌次数
	public long dropCardNum;
	//全下次数
	public long fullBtNum;
}
