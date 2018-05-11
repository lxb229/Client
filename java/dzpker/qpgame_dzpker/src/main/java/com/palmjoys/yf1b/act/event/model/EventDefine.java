package com.palmjoys.yf1b.act.event.model;

public interface EventDefine {
	//帐号注册事件
	int EVENT_ACCOUNT_CREATE = 1;
	//桌子创建事件
	int EVENT_TABLE_CREATE = 2;
	//帐号登录事件
	int EVENT_ACCOUNT_LOGIN = 3;
	//桌子加入事件
	int EVENT_TABLE_JOIN = 4;
	//桌子座下事件
	int EVENT_TABLE_SEATDOWN = 5;
	//桌子购买筹码事件
	int EVENT_TABLE_BUY_CHIP = 6;
	//桌子下注事件
	int EVENT_TABLE_BET = 7;
	//桌子购买保险事件
	int EVENT_TABLE_BUY_INSURANCE = 8;
	//保险输赢事件
	int EVENT_TABLE_WIN_INSURANCE = 9;
	//游戏牌局输赢事件
	int EVENT_TABLE_WIN_CARDS = 10;
	//桌子删除事件
	int EVENT_TABLE_REMOVE = 11;
}
