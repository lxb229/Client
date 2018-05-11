package com.palmjoys.yf1b.act.majiang.model;

public class SettementSeatScore {
	//分数
	public int score;
	//躺个数(-1=无躺的游戏规则)
	public int tangNum;
	//报叫个数(-1=无报叫的游戏规则)
	public int baoJiaoNum;
	//总番数
	public int totalFanNum;
	//漂牌总输赢数量
	public int winPiaoNum;
	
	public SettementSeatScore(){
		this.score = 0;
		this.tangNum = 0;
		this.baoJiaoNum = 0;
		this.totalFanNum = 0;
		this.winPiaoNum = 0;
	}
}
