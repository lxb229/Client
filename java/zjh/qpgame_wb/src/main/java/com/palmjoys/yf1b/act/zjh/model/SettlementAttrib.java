package com.palmjoys.yf1b.act.zjh.model;

import java.util.ArrayList;
import java.util.List;

public class SettlementAttrib {
	//牛牛结算列表
	public List<SettlementItemNN> NNItems;
	//金花结算列表
	public List<SettlementItemZJH> ZJHItems;
	
	public void addNNItem(String accountId, int banker, String nick, String nnDesc, long score){
		SettlementItemNN vo = new SettlementItemNN();
		vo.accountId = accountId;
		vo.banker = banker;
		vo.nick = nick;
		vo.nnDesc = nnDesc;
		vo.score = String.valueOf(score);
		NNItems.add(vo);
	}
	
	public void addZJHItem(int seatIndex, long score){
		SettlementItemZJH vo = new SettlementItemZJH();
		vo.seatIndex = seatIndex;
		vo.score = String.valueOf(score);
		ZJHItems.add(vo);
	}
	
	public void reset(){
		this.NNItems.clear();
		this.ZJHItems.clear();
	}
	
	public SettlementAttrib(){
		NNItems = new ArrayList<>();
		ZJHItems = new ArrayList<>();
	}
	
	public class SettlementItemNN{
		//帐号Id
		public String accountId;
		//庄家标识(1=庄家)
		public int banker;
		//呢称
		public String nick;
		//牛点数
		public String nnDesc;
		//输赢分数
		public String score;
	}
	
	public class SettlementItemZJH{
		//座位
		public int seatIndex;
		//输赢分数(正的赢,负的输)
		public String score;
	}
}
