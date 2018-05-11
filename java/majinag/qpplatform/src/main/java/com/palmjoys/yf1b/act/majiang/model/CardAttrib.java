package com.palmjoys.yf1b.act.majiang.model;

public class CardAttrib {
	//牌的唯一编号
	public int cardId;
	//花色
	public int suit;
	//点数
	public int point;
	
	@Override
	public String toString(){
		String str = "";
		switch(suit){
		case GameDefine.SUIT_TYPE_WAN:
			str = "["+cardId+"="+point + "万]";
			break;
		case GameDefine.SUIT_TYPE_TONG:
			str = "["+cardId+"="+point + "筒]";
			break;
		case GameDefine.SUIT_TYPE_TIAO:
			str = "["+cardId+"="+point + "条]";
			break;
		}
		return str;
	}
}
