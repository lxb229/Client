package com.palmjoys.yf1b.act.task.model;

//任务条件统计数据
public class TaskStatisticsAttrib {
	//每日充值房卡数
	public int dayChargeCard;
	//每日创建并游戏的轮数
	public int dayCreateTable;
	//每日大赢家次数
	public int dayWinMaxNum;
	//每日自摸次数
	public int dayZiMoNum;
	//每日接炮次数
	public int dayJiePaoNum;
	//每日分享战绩次数
	public int daySharScoreNum;
	//每日暗杠牌次数
	public int dayAnGangNum;
	//每日巴杠牌次数
	public int dayBaGangNum;
	//每日银币抽奖次数
	public int dayLuckRewardNum;
	//红包领取条件次数(创建并游戏的轮数)
	public int totalCreateTable;
	
	
	public TaskStatisticsAttrib(){
		this.reset();
		this.totalCreateTable = 0;
	}
	
	public void reset(){
		this.dayChargeCard = 0;
		this.dayCreateTable = 0;
		this.dayWinMaxNum = 0;
		this.dayZiMoNum = 0;
		this.dayJiePaoNum = 0;
		this.daySharScoreNum = 0;
		this.dayAnGangNum = 0;
		this.dayBaGangNum = 0;
		this.dayLuckRewardNum = 0;
	}
}
