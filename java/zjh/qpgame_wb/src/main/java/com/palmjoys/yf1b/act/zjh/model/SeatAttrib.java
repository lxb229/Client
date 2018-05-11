package com.palmjoys.yf1b.act.zjh.model;

import java.util.ArrayList;
import java.util.List;

public class SeatAttrib {
	//座位下标
	public int seatIndex;
	//玩家Id(0=无人)
	public long accountId;
	//座位手牌
	public List<Byte> handCards;
	//表态状态(-1=放弃,0=等待,1=表态)
	public int btState;
	//游戏结束后是否离开桌子
	public boolean bLeave;
	//是否参加本轮游戏
	public boolean bGamed;
	//是否看牌
	public boolean bLookCard;
	//表态结果(0=弃牌,1=看牌,2=比牌,3=全押,4=跟注,5=加注)
	public int btVal;
	//机器人表态等待时间
	public long robotBtTime;
	//机器人最大下注轮数
	public int betMaxRound;
	//金花桌子座位加入等待时间
	public long joinWaitTime;
	
	public SeatAttrib(int seatIndex){
		this.seatIndex = seatIndex;
		this.accountId = 0;
		this.handCards = new ArrayList<>();
		this.btState = GameDefine.ACT_STATE_WAIT;
		this.bLeave = false;
		this.bGamed = false;
		this.bLookCard = false;
		this.btVal = -1;
		this.robotBtTime = 0;
		this.betMaxRound = 1;
	}
	
	public void reset(){
		this.handCards.clear();
		this.btState = GameDefine.ACT_STATE_WAIT;
		this.bLeave = false;
		this.bGamed = false;
		this.bLookCard = false;
		this.btVal = -1;
		this.robotBtTime = 0;
		this.betMaxRound = 1;
	}
}
