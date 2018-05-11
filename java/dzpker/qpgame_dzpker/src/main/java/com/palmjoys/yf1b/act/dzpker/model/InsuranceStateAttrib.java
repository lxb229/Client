package com.palmjoys.yf1b.act.dzpker.model;
//保险状态数据

import java.util.ArrayList;
import java.util.List;

public class InsuranceStateAttrib {
	//保险的赔率配置
	public List<InsuranceCfgAttrib> insuranceRateList;
	//座位数据列表
	public List<InsuranceSeatAttrib> insuranceSeatList;
	//桌子底牌
	public List<Integer> tableHandCards;
	//可以反超的牌列表
	public List<Integer> winCardList;
	//当前购买保险的玩家
	public String accountId;
	//主池
	public String poolMoney;
	//主池中我下注金额
	public String betMoney;
	//玩家已购买的保险筹码数
	public String buyedNum;	
	//玩家投保数据
	public InsuranceBuyAttrib []buyInsuranceRound;
	//当前保险轮数
	public int round;
	
	public InsuranceStateAttrib(){
		this.insuranceRateList = new ArrayList<>();
		this.insuranceSeatList = new ArrayList<>();
		this.tableHandCards = new ArrayList<>();
		this.winCardList = new ArrayList<>();
		this.accountId = "0";
		this.poolMoney = "0";
		this.betMoney = "0";
		this.buyedNum = "0";
		this.buyInsuranceRound = new InsuranceBuyAttrib[2];
		this.buyInsuranceRound[0] = new InsuranceBuyAttrib();
		this.buyInsuranceRound[1] = new InsuranceBuyAttrib();
		this.round = 0;
	}
	
	public void addSeat(int seatIndex, long accountId, String nick, List<CardAttrib> handCards, int cardNum){
		InsuranceSeatAttrib item = new InsuranceSeatAttrib();
		item.seatIndex = seatIndex;
		item.accountId = String.valueOf(accountId);
		item.nick = nick;
		for(CardAttrib card : handCards){
			item.handCards.add(card.cardId);
		}
		item.cardNum = cardNum;
		
		this.insuranceSeatList.add(item);
	}
	
	public void reset(){
		insuranceRateList.clear();
		insuranceSeatList.clear();
		tableHandCards.clear();
		winCardList.clear();
		this.poolMoney = "0";
		this.betMoney = "0";
	}
	
	public void resetAll(){
		this.reset();
		this.buyInsuranceRound[0].reset();
		this.buyInsuranceRound[1].reset();
	}
	
	public class InsuranceSeatAttrib{
		//座位下标
		public int seatIndex;
		//玩家Id
		public String accountId;
		//呢称
		public String nick;
		//手牌
		public List<Integer> handCards = new ArrayList<>();
		//反超牌个数
		public int cardNum;
	}

	public class InsuranceBuyAttrib{
		//投保玩家
		public long accountId;
		//投保金额
		public long buyMoney;
		//额外附加金额
		public long exBuyMoney;
		//投保收益金额
		public long payMoney;
		//购买的牌
		public List<Integer> buyCards = new ArrayList<>();
		//保险结算金额
		public long settlement;
		
		public void reset(){
			this.accountId = 0;
			this.buyMoney = 0;
			this.exBuyMoney = 0;
			this.payMoney = 0;
			this.buyCards.clear();
			this.settlement = 0;
		}
	}
}
