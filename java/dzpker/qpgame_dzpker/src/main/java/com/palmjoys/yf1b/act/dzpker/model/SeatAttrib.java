package com.palmjoys.yf1b.act.dzpker.model;

import java.util.ArrayList;
import java.util.List;

public class SeatAttrib {
	//座位下标
	public int seatIndex;
	//座位玩家
	public long accountId;
	//座位牌
	public List<CardAttrib> handCars;
	//座位表态状态
	public int btState;
	//座位表态结果
	public int btResult;
	//是否参加本局游戏
	public boolean bGamed;
	//发牌以后最大的牌
	public AnalysisResult cardsResult;
	//闭眼盲注开启状态
	public int straddle;
	//闭眼盲注标志
	public int straddleFlag;
	
	public SeatAttrib(int seatIndex){
		this.seatIndex = seatIndex;
		this.accountId = 0;
		this.handCars = new ArrayList<>();
		this.btState = GameDefine.BT_STATE_WAIT;
		this.btResult = GameDefine.ACT_TYPE_NONE;
		this.bGamed = false;;
		this.cardsResult = null;
		this.straddle = 0;
		this.straddleFlag = 0;
	}
	
	public void reset(){
		this.handCars.clear();
		this.btState = GameDefine.BT_STATE_WAIT;
		this.btResult = GameDefine.ACT_TYPE_NONE;
		this.bGamed = false;
		this.cardsResult = null;
		this.straddle = 0;
		this.straddleFlag = 0;
	}
}
