package com.palmjoys.yf1b.act.dzpker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameDefine {
	//游戏状态定义
	//空闲状态
	public static final int STATE_TABLE_IDLE = 0; 
	//准备状态
	public static final int STATE_TABLE_READY = 1;
	//定庄家下大小盲注
	public static final int STATE_TABLE_BETBLIND = 2;
	//第一轮发手牌
	public static final int STATE_TABLE_OUTCARD_1 = 3;
	//第一轮下注表态
	public static final int STATE_TABLE_BET_BT_1 = 4;
	//第二轮发三张公用牌
	public static final int STATE_TABLE_OUTCARD_2 = 5;
	//第二轮下注表态
	public static final int STATE_TABLE_BET_BT_2 = 6;
	//第三轮发一张公用牌
	public static final int STATE_TABLE_OUTCARD_3 = 7;
	//第三轮下注表态
	public static final int STATE_TABLE_BET_BT_3 = 8;
	//第四轮发一张公用牌
	public static final int STATE_TABLE_OUTCARD_4 = 9;
	//第四轮下注表态
	public static final int STATE_TABLE_BET_BT_4 = 10;
	//保险购买状态
	public static final int STATE_TABLE_BUY_INSURANCE = 11;
	//单局结算
	public static final int STATE_TABLE_OVER_ONCE = 12;
	//总结算
	public static final int STATE_TABLE_OVER_ALL = 13;
	//新一轮筹码结算
	public static final int STATE_TABLE_NEW_ROUND_BET = 14;
	//秀牌阶段
	public static final int STATE_TABLE_ALL_IN_SHOW_CARD = 15;
	//游戏额外等待
	public static final int STATE_TABLE_WAIT = 16;
	
	//游戏状态时间定义
	//准备状态时间
	public static final int TIME_TABLE_READY = 0*1000;
	//下大小盲注时间
	public static final int TIME_TABLE_BETBLIND = 1*1000;
	//发一张牌时间
	public static final int TIME_TABLE_OUTCARD = (int) (0.2*1000);
	//表态时间
	public static final int TIME_TABLE_BET_BT = 15*1000;
	//保险购买表态时间
	public static final int TIME_TABLE_BUY_INSURANCE = 2*60*1000;
	//结算时间
	public static final int TIME_TABLE_OVER = 3*1000;
	//新一轮筹码结算时间
	public static final int TIME_TABLE_NEW_ROUND_NET = 1*1000;
	//Straddle表态等待时间
	public static final int TIME_TABLE_STRADDLE_BET = 5*1000;
	//秀牌时间长
	public static final int TIME_TABLE_ALLIN_SHOWCARD = (int) (0.2*1000);
	//游戏额外等待时间
	public static final int TIME_TABLE_WAIT = (int) (0.3*1000);
	
	
	//表态状态定义
	//放弃
	public static final int BT_STATE_DROP = -1;
	//等待
	public static final int BT_STATE_WAIT = 0;
	//表态
	public static final int BT_STATE_BT = 1;
	
	//游戏动作定义
	//无动作
	public static final int ACT_TYPE_NONE = 0;
	//弃牌
	public static final int ACT_TYPE_DROP = 1;
	//过牌
	public static final int ACT_TYPE_PASS = 2;
	//跟注
	public static final int ACT_TYPE_SAME = 3;
	//加注
	public static final int ACT_TYPE_ADD = 4;
	//全押
	public static final int ACT_TYPE_ALLIN = 5;	
	
	//牌型定义
	//单牌
	public static final int TYPE_CARD_NONE = 0;
	//一对
	public static final int TYPE_CARD_ONE_DOUBLE = 1;	
	//两对
	public static final int TYPE_CARD_TWO_DOUBLE = 2;
	//三条
	public static final int TYPE_CARD_SAME_THREE = 3;
	//顺子
	public static final int TYPE_CARD_SHUN = 4;
	//同花
	public static final int TYPE_CARD_SAME_SUIT = 5;
	//葫芦(3条+1对)
	public static final int TYPE_CARD_GOURD = 6;
	//四条
	public static final int TYPE_CARD_SAME_FOUR = 7;
	//同花顺
	public static final int TYPE_CARD_SAME_SUIT_SHUN = 8;
	//皇家同花顺
	public static final int TYPE_CARD_GOLD_SAME_SUIT_SHUN = 9;	
	
	//牌花色定义
	//黑
	public static final int TYPE_SUIT_BACK = 0;
	//红
	public static final int TYPE_SUIT_RED = 1;
	//梅
	public static final int TYPE_SUIT_CLUB = 2;
	//方
	public static final int TYPE_SUIT_BLOCK = 3;
	
	
	//桌子座位数
	public static final int MAX_SEAT_NUM = 9;  
	
	public static final int []g_Cards = new int[]{
		2,3,4,5,6,7,8,9,10,11,12,13,14,				//黑=0
		17,18,19,20,21,22,23,24,25,26,27,28,29,		//红=1
		32,33,34,35,36,37,38,39,40,41,42,43,44,		//梅=2
		47,48,49,50,51,52,53,54,55,56,57,58,59		//方=3
	};
	//初始化一幅牌
	public static List<CardAttrib> initCards(){
		List<CardAttrib> retCards = new ArrayList<>();
		for(int cardId : GameDefine.g_Cards){
			CardAttrib card = new CardAttrib();
			card.cardId = cardId;
			card.suit = cardId/15;
			card.point = cardId%15;
			
			retCards.add(card);
		}
		Collections.shuffle(retCards);
		Collections.shuffle(retCards);
		Collections.shuffle(retCards);
		
		return retCards;
	}
	
	public static int getCardSuit(int cardId){
		return (cardId/15);
	}
	
	public static int getCardPoint(int cardId){
		return (cardId%15);
	}
	
	//是否皇家同花顺
	public static List<CardAttrib> isGoldSameSuitShun(List<CardAttrib> cards){
		List<CardAttrib> suit_back = new ArrayList<>();
		List<CardAttrib> suit_red = new ArrayList<>();
		List<CardAttrib> suit_club = new ArrayList<>();
		List<CardAttrib> suit_block = new ArrayList<>();
		
		for(CardAttrib card : cards){
			switch(card.suit){
			case GameDefine.TYPE_SUIT_BACK:
				suit_back.add(card);
				break;
			case GameDefine.TYPE_SUIT_RED:
				suit_red.add(card);
				break;
			case GameDefine.TYPE_SUIT_CLUB:
				suit_club.add(card);
				break;
			case GameDefine.TYPE_SUIT_BLOCK:
				suit_block.add(card);
				break;
			}
		}
		
		if(suit_back.size() < 5 && suit_red.size() < 5
				&& suit_club.size() < 5 && suit_block.size() < 5)
			return null;
		
		if(suit_back.size()>=5
			&& suit_back.get(0).point == 14
			&& suit_back.get(1).point == 13
			&& suit_back.get(2).point == 12
			&& suit_back.get(3).point == 11
			&& suit_back.get(4).point == 10){
			
			List<CardAttrib> retList = new ArrayList<>();
			retList.addAll(suit_back.subList(0, 5));
			return retList;
		}
		
		if(suit_red.size()>=5
				&& suit_red.get(0).point == 14
				&& suit_red.get(1).point == 13
				&& suit_red.get(2).point == 12
				&& suit_red.get(3).point == 11
				&& suit_red.get(4).point == 10){
				
				List<CardAttrib> retList = new ArrayList<>();
				retList.addAll(suit_red.subList(0, 5));
				return retList;
		}
		
		if(suit_club.size()>=5
				&& suit_club.get(0).point == 14
				&& suit_club.get(1).point == 13
				&& suit_club.get(2).point == 12
				&& suit_club.get(3).point == 11
				&& suit_club.get(4).point == 10){
				
				List<CardAttrib> retList = new ArrayList<>();
				retList.addAll(suit_club.subList(0, 5));
				return retList;
		}
		
		if(suit_block.size()>=5
				&& suit_block.get(0).point == 14
				&& suit_block.get(1).point == 13
				&& suit_block.get(2).point == 12
				&& suit_block.get(3).point == 11
				&& suit_block.get(4).point == 10){
				
				List<CardAttrib> retList = new ArrayList<>();
				retList.addAll(suit_block.subList(0, 5));
				return retList;
		}
		
		return null;
	}
	
	//是否同花顺
	public static List<CardAttrib> isSameSuitShun(List<CardAttrib> cards){
		if(cards.size() < 5)
			return null;
		
		List<CardAttrib> suit_back = new ArrayList<>();
		List<CardAttrib> suit_red = new ArrayList<>();
		List<CardAttrib> suit_club = new ArrayList<>();
		List<CardAttrib> suit_block = new ArrayList<>();
		
		for(CardAttrib card : cards){
			switch(card.suit){
			case GameDefine.TYPE_SUIT_BACK:
				suit_back.add(card);
				break;
			case GameDefine.TYPE_SUIT_RED:
				suit_red.add(card);
				break;
			case GameDefine.TYPE_SUIT_CLUB:
				suit_club.add(card);
				break;
			case GameDefine.TYPE_SUIT_BLOCK:
				suit_block.add(card);
				break;
			}
		}
		List<CardAttrib> retList = null;
		//黑桃
		retList = GameDefine.isShunZhi(suit_back);
		if(null != retList){
			return retList;
		}
		//红心
		retList = GameDefine.isShunZhi(suit_red);
		if(null != retList){
			return retList;
		}
		//梅花
		retList = GameDefine.isShunZhi(suit_club);
		if(null != retList){
			return retList;
		}
		//方块
		retList = GameDefine.isShunZhi(suit_block);
		if(null != retList){
			return retList;
		}
		
		return null;
	}
	
	//是否四条
	public static List<CardAttrib> isFourSame(List<CardAttrib> cards){
		if(cards.size() < 5){
			return null;
		}
		List<CardAttrib> retList = new ArrayList<>();
		for(int i=0; i<cards.size()-1; i++){
			CardAttrib card1 = cards.get(i);
			CardAttrib card2 = cards.get(i+1);
			if(card1.point == card2.point){
				if(retList.contains(card1) == false){
					retList.add(card1);
				}
				if(retList.contains(card2) == false){
					retList.add(card2);
				}
				
				if(retList.size() == 4){
					break;
				}
			}else{
				retList.clear();
			}
		}
		if(retList.size() == 4){
			for(int i=0; i<cards.size(); i++){
				CardAttrib card = cards.get(i);
				if(retList.contains(card) == false){
					retList.add(card);
				}
				if(retList.size() == 5){
					return retList;
				}
			}
		}
		return null;
	}
	
	//是否葫芦(3条+1对)
	public static List<CardAttrib> isFullHourse(List<CardAttrib> cards){
		if(cards.size() < 5){
			return null;
		}
		List<CardAttrib> retList = new ArrayList<>();
		for(int i=0; i<cards.size()-1; i++){
			CardAttrib card1 = cards.get(i);
			CardAttrib card2 = cards.get(i+1);
			if(card1.point == card2.point){
				if(retList.contains(card1) == false){
					retList.add(card1);
				}
				if(retList.contains(card2) == false){
					retList.add(card2);
				}
				
				if(retList.size() == 3){
					break;
				}
			}else{
				retList.clear();
			}
		}
		if(retList.size() == 3){
			for(int i=0; i<cards.size()-1; i++){
				CardAttrib card1 = cards.get(i);
				CardAttrib card2 = cards.get(i+1);
				if(retList.contains(card1) == false
						&& retList.contains(card2) == false){
					if(card1.point == card2.point){
						retList.add(card1);
						retList.add(card2);
						return retList;
					}
				}
			}
		}
		return null;
	}
	
	//是否同花
	public static List<CardAttrib> isSameSuit(List<CardAttrib> cards){
		if(cards.size() < 5)
			return null;
		
		List<CardAttrib> suit_back = new ArrayList<>();
		List<CardAttrib> suit_red = new ArrayList<>();
		List<CardAttrib> suit_club = new ArrayList<>();
		List<CardAttrib> suit_block = new ArrayList<>();
		
		for(CardAttrib card : cards){
			switch(card.suit){
			case GameDefine.TYPE_SUIT_BACK:
				suit_back.add(card);
				break;
			case GameDefine.TYPE_SUIT_RED:
				suit_red.add(card);
				break;
			case GameDefine.TYPE_SUIT_CLUB:
				suit_club.add(card);
				break;
			case GameDefine.TYPE_SUIT_BLOCK:
				suit_block.add(card);
				break;
			}
		}
		List<CardAttrib> retList = new ArrayList<>();
		if(suit_back.size() >= 5){
			retList.addAll(suit_back.subList(0, 5));
			return retList;
		}
		if(suit_red.size() >= 5){
			retList.addAll(suit_red.subList(0, 5));
			return retList;
		}
		if(suit_club.size() >= 5){
			retList.addAll(suit_club.subList(0, 5));
			return retList;
		}
		if(suit_block.size() >= 5){
			retList.addAll(suit_block.subList(0, 5));
			return retList;
		}
		
		return null;
	}
	
	//是否顺子
	public static List<CardAttrib> isShunZhi(List<CardAttrib> cards){
		List<CardAttrib> copyCards = new ArrayList<>();
		for(CardAttrib card : cards){
			boolean bFind = false;
			for(CardAttrib card1 : copyCards){
				if(card1.point == card.point){
					bFind = true;
					break;
				}
			}
			
			if(bFind == false){
				copyCards.add(card);
			}
		}
		
		if(copyCards.size() < 5)
			return null;
		
		List<CardAttrib> shunZi = new ArrayList<>();
		for(int i=0; i<copyCards.size()-1; i++){
			CardAttrib card1 = copyCards.get(i);
			CardAttrib card2 = copyCards.get(i+1);
			if(card1.point-card2.point == 1){
				if(shunZi.contains(card1) == false){
					shunZi.add(card1);	
				}
				if(shunZi.contains(card2) == false){
					shunZi.add(card2);
				}
				
				if(shunZi.size() == 5){
					return shunZi;
				}
			}else{
				shunZi.clear();
			}
		}
		//检查特殊的A,2,3,4,5
		List<CardAttrib> cardA = new ArrayList<>();
		List<CardAttrib> card2 = new ArrayList<>();
		List<CardAttrib> card3 = new ArrayList<>();
		List<CardAttrib> card4 = new ArrayList<>();
		List<CardAttrib> card5 = new ArrayList<>();
		for(int i=0; i<copyCards.size(); i++){
			CardAttrib card = copyCards.get(i);
			switch(card.point){
			case 2:
				card2.add(card);
				break;
			case 3:
				card3.add(card);
				break;
			case 4:
				card4.add(card);
				break;
			case 5:
				card5.add(card);
				break;
			case 14:
				cardA.add(card);
				break;
			}
		}
		
		if(cardA.size()>0 && card2.size()>0
				&& card3.size()>0
				&& card4.size()>0
				&& card5.size()>0){
			shunZi.clear();
			shunZi.add(card5.get(0));
			shunZi.add(card4.get(0));
			shunZi.add(card3.get(0));
			shunZi.add(card2.get(0));
			shunZi.add(cardA.get(0));
			
			return shunZi;
		}
		
		return null;
	}
	
	//是否3条
	public static List<CardAttrib> isThreeSame(List<CardAttrib> cards){
		if(cards.size() < 5){
			return null;
		}
		List<CardAttrib> retList = new ArrayList<>();
		for(int i=0; i<cards.size()-1; i++){
			CardAttrib card1 = cards.get(i);
			CardAttrib card2 = cards.get(i+1);
			if(card1.point == card2.point){
				if(retList.contains(card1) == false){
					retList.add(card1);
				}
				if(retList.contains(card2) == false){
					retList.add(card2);
				}
				
				if(retList.size() == 3){
					break;
				}
			}else{
				retList.clear();
			}
		}
		if(retList.size() == 3){
			for(int i=0; i<cards.size(); i++){
				CardAttrib card = cards.get(i);
				if(retList.contains(card) == false){
					retList.add(card);
				}
				if(retList.size() == 5){
					return retList;
				}
			}
		}
		return null;
	}
	
	//是否2对
	public static List<CardAttrib> isTwoDouble(List<CardAttrib> cards){
		if(cards.size() < 5){
			return null;
		}
		List<CardAttrib> retList = new ArrayList<>();
		for(int i=0; i<cards.size()-1;){
			CardAttrib card1 = cards.get(i);
			CardAttrib card2 = cards.get(i+1);
			if(card1.point == card2.point){
				retList.add(card1);
				retList.add(card2);
				i+=2;
			}else{
				i+=1;
			}
			if(retList.size() == 4){
				break;
			}
		}
		if(retList.size() == 4){
			for(int i=0; i<cards.size(); i++){
				CardAttrib card = cards.get(i);
				if(retList.contains(card) == false){
					retList.add(card);
				}
				if(retList.size() == 5){
					return retList;
				}
			}
		}
		return null;
	}
	
	//是否1对
	public static List<CardAttrib> isOneDouble(List<CardAttrib> cards){
		if(cards.size() < 5){
			CardAttrib card1 = cards.get(0);
			CardAttrib card2 = cards.get(1);
			if(card1.point == card2.point){
				return cards;
			}
		}
		List<CardAttrib> retList = new ArrayList<>();
		for(int i=0; i<cards.size()-1;){
			CardAttrib card1 = cards.get(i);
			CardAttrib card2 = cards.get(i+1);
			if(card1.point == card2.point){
				retList.add(card1);
				retList.add(card2);
				i+=2;
			}else{
				i+=1;
			}
			if(retList.size() == 2){
				break;
			}
		}
		if(retList.size() == 2){
			for(int i=0; i<cards.size(); i++){
				CardAttrib card = cards.get(i);
				if(retList.contains(card) == false){
					retList.add(card);
				}
				if(retList.size() == 5){
					return retList;
				}
			}
		}
		return null;
	}
	
	//分析牌
	public static AnalysisResult getCardsType(List<CardAttrib> cards){
		AnalysisResult retResult = new AnalysisResult();
		//按点数从大到小排序
		cards.sort(new Comparator<CardAttrib>(){
			@Override
			public int compare(CardAttrib arg0, CardAttrib arg1) {
				if(arg0.point > arg1.point){
					return -1;
				}else if(arg0.point < arg1.point){
					return 1;
				}
				return 0;
			}
		});
		//皇家同花顺
		retResult.cardList = GameDefine.isGoldSameSuitShun(cards);
		if(null != retResult.cardList){
			retResult.cardType = GameDefine.TYPE_CARD_GOLD_SAME_SUIT_SHUN;
			return retResult;
		}
		//同花顺
		retResult.cardList = GameDefine.isSameSuitShun(cards);
		if(null != retResult.cardList){
			retResult.cardType = GameDefine.TYPE_CARD_SAME_SUIT_SHUN;
			return retResult;
		}
		//四条
		retResult.cardList = GameDefine.isFourSame(cards);
		if(null != retResult.cardList){
			retResult.cardType = GameDefine.TYPE_CARD_SAME_FOUR;
			return retResult;
		}
		//葫芦
		retResult.cardList = GameDefine.isFullHourse(cards);
		if(null != retResult.cardList){
			retResult.cardType = GameDefine.TYPE_CARD_GOURD;
			return retResult;
		}
		//同花
		retResult.cardList = GameDefine.isSameSuit(cards);
		if(null != retResult.cardList){
			retResult.cardType = GameDefine.TYPE_CARD_SAME_SUIT;
			return retResult;
		}
		//顺子
		retResult.cardList = GameDefine.isShunZhi(cards);
		if(null != retResult.cardList){
			retResult.cardType = GameDefine.TYPE_CARD_SHUN;
			return retResult;
		}
		//三条
		retResult.cardList = GameDefine.isThreeSame(cards);
		if(null != retResult.cardList){
			retResult.cardType = GameDefine.TYPE_CARD_SAME_THREE;
			return retResult;
		}
		//两对
		retResult.cardList = GameDefine.isTwoDouble(cards);
		if(null != retResult.cardList){
			retResult.cardType = GameDefine.TYPE_CARD_TWO_DOUBLE;
			return retResult;
		}
		//一对
		retResult.cardList = GameDefine.isOneDouble(cards);
		if(null != retResult.cardList){
			retResult.cardType = GameDefine.TYPE_CARD_ONE_DOUBLE;
			return retResult;
		}
		//单牌
		if(cards.size() < 5){
			retResult.cardList = cards;
		}else{
			retResult.cardList = new ArrayList<>();
			retResult.cardList.addAll(cards.subList(0, 5)); 
		}
		retResult.cardType = GameDefine.TYPE_CARD_NONE;
		
		return retResult;
	}
	
	//比较牌组,-1=1小于2,0=相同,1=1大于2
	public static int compareCards(AnalysisResult cardsResult1, AnalysisResult cardsResult2){
		if(cardsResult1.cardType < cardsResult2.cardType){
			return -1;
		}else if(cardsResult1.cardType > cardsResult2.cardType){
			return 1;
		}else{
			//牌型一样比较大小
			int min = cardsResult1.cardList.size();
			if(min > cardsResult2.cardList.size()){
				min = cardsResult2.cardList.size();
			}
			for(int index=0; index<min; index++){
				CardAttrib card1 = cardsResult1.cardList.get(index);
				CardAttrib card2 = cardsResult2.cardList.get(index);
				if(card1.point < card2.point){
					return -1;
				}else if(card1.point > card2.point){
					return 1;
				}
			}
		}		
		return 0;
	}
	
}
