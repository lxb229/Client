package com.palmjoys.yf1b.act.dzpker.model;

public class CardAttrib {
	//牌Id
	public int cardId;
	//花色
	public int suit;
	//点数
	public int point;
	
	public static CardAttrib valueOf(int cardId){
		CardAttrib retCard = new CardAttrib();
		retCard.cardId = cardId;
		retCard.suit = GameDefine.getCardSuit(cardId);
		retCard.point = GameDefine.getCardPoint(cardId);
		
		return retCard;
	}
	
	public String toString(){
		String retStr = "";
		switch(this.suit){
		case GameDefine.TYPE_SUIT_BACK:
			retStr += "黑桃";
			break;
		case GameDefine.TYPE_SUIT_RED:
			retStr += "红桃";
			break;
		case GameDefine.TYPE_SUIT_CLUB:
			retStr += "梅花";
			break;
		case GameDefine.TYPE_SUIT_BLOCK:
			retStr += "方块";
			break;
		default:
			retStr += "未知";
			break;
		}
		switch(this.point){
		case 11:
			retStr += " J";
			break;
		case 12:
			retStr += " Q";
			break;
		case 13:
			retStr += " K";
			break;
		case 14:
			retStr += " A";
			break;
		default:
			retStr += " "+this.point;
			break;
		}
		
		return retStr;
	}
}
