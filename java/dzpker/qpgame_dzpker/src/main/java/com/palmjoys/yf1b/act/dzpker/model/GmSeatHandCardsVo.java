package com.palmjoys.yf1b.act.dzpker.model;

import java.util.ArrayList;
import java.util.List;

public class GmSeatHandCardsVo {
	//座位手牌数据列表
	public List<GmSeatHandCardsInner> items;
	
	public GmSeatHandCardsVo(){
		this.items = new ArrayList<>();
	}
	
	public void addSeat(String nick, String cards){
		GmSeatHandCardsInner item = new GmSeatHandCardsInner();
		item.nick = nick;
		item.cards = cards;
		
		this.items.add(item);
	}
	

	public class GmSeatHandCardsInner{
		//呢称
		public String nick;
		//手牌
		public String cards;
	}
}
