package com.palmjoys.yf1b.act.zjh.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameDefine {
	//游戏桌子类型定义
	//牛牛
	public static final int GAME_TYPE_NN = 1;
	//金花
	public static final int GAME_TYPE_ZJH = 2;
	
	//游戏动作表态定义
	//放弃
	public static final int ACT_STATE_DROP = -1;
	//等待表态
	public static final int ACT_STATE_WAIT = 0;
	//已表态
	public static final int ACT_STATE_BT = 1;
	
	//座位数定义
	//牛牛座位数
	public static final int SEAT_NUM_NN = 2;
	//金花座位数
	public static final int SEAT_NUM_ZJH = 5;
	
	//游戏空闲状态
	public static final int STATE_TABLE_IDLE = 0;
	//牛牛游戏状态定义
	//牛牛游戏开始
	public static final int STATE_TABLE_NN_START = 1;
	//叫庄状态(牛牛游戏)
	public static final int STATE_TABLE_NN_CALL_BANKER = 2;
	//下注状态
	public static final int STATE_TABLE_NN_BET = 3;
	//发牌结算
	public static final int STATE_TABLE_NN_OVER = 4;
	//游戏额外等待状态
	public static final int STATE_TABLE_NN_WAIT_EX = 6;
		
	//牛牛游戏状态时间定义
	//空闲等待时间
	public static final int TIME_WAIT_NN_IDLE = 0*1000;
	//叫庄状态等待时间
	public static final int TIME_WAIT_NN_START = 1*1000;
	//叫庄状态等待时间
	public static final int TIME_WAIT_NN_CALL_BANKER = 10*1000;
	//下注状态等待时间
	public static final int TIME_WAIT_NN_BET = 15*1000;
	//发牌结算状态等待时间
	public static final int TIME_WAIT_NN_OVER = 1*1000;
	//游戏额外等待状态等待时间
	public static final int TIME_WAIT_NN_WAITEX = (int) (1*1000);
	
	//金花游戏状态定义
	//准备
	public static final int STATE_TABLE_ZJH_READY = 1;
	//扒底
	public static final int STATE_TABLE_ZJH_BASESCORE = 2;
	//发牌
	public static final int STATE_TABLE_ZJH_FAPAI = 3;
	//下注表态
	public static final int STATE_TABLE_ZJH_BET = 4;
	//比牌
	public static final int STATE_TABLE_ZJH_COMPARE = 5;
	//结算
	public static final int STATE_TABLE_ZJH_OVER = 6;
	//游戏额外等待状态
	public static final int STATE_TABLE_ZJH_WAIT_EX = 10;
	
	//金花游戏等待时间定义
	//空闲等待时间
	public static final int TIME_WAIT_ZJH_IDLE = 0*1000;
	//准备等待时间
	public static final int TIME_WAIT_ZJH_READY = 11*1000;
	//扒底等待时间
	public static final int TIME_WAIT_ZJH_BASESCORE = 6*1000;
	//发牌等待时间
	public static final int TIME_WAIT_ZJH_FAPAI = 5*1000;
	//下注表态
	public static final int TIME_WAIT_ZJH_BET = 15*1000;
	//比牌时间
	public static final int TIME_WAIT_ZJH_COMPARE = 8*1000;
	//结算时间
	public static final int TIME_WAIT_ZJH_OVER = 5*1000;
	//游戏额外等待状态等待时间
	public static final int TIME_WAIT_ZJH_WAITEX = (int) (0.5*1000);
	
	//金花牌型定义
	//散牌
	public static final int ZJH_CARD_TYPE_NONE = 0;
	//对子
	public static final int ZJH_CARD_TYPE_DOUBLE = 1;
	//连子
	public static final int ZJH_CARD_TYPE_SHUN = 2;
	//同花
	public static final int ZJH_CARD_TYPE_SAMECOLOE = 3;
	//青同花
	public static final int ZJH_CARD_TYPE_SAMECOLOE_SHUN = 4;
	//飞机
	public static final int ZJH_CARD_TYPE_FIY = 5;
	
	
	//定义表态状态值
	//弃牌
	public static final int BT_VAL_DROP = 0;
	//看牌
	public static final int BT_VAL_LOOCK = 1;
	//比牌
	public static final int BT_VAL_COMPARAE = 2;
	//全下
	public static final int BT_VAL_BETALL = 3;
	//跟注
	public static final int BT_VAL_BETSAME = 4;
	//加注
	public static final int BT_VAL_BETADD = 5;
	
	//定义手牌数
	//牛牛手牌数
	public static final int CARD_HAND_NUM_NN = 5;
	//金花手牌数
	public static final int CARD_HAND_NUM_ZJH = 3;
	
	public static final byte []g_gameCard = new byte[]{
		2,3,4,5,6,7,8,9,10,11,12,13,14,			//黑
		15,16,17,18,19,20,21,22,23,24,25,26,27,	//红
		28,29,30,31,32,33,34,35,36,37,38,39,40,	//梅
		41,42,43,44,45,46,47,48,49,50,51,52,53	//方
	};
	
	public static List<Byte> initCards(){
		List<Byte> retCards = new ArrayList<>();
		for(Byte card : g_gameCard){
			retCards.add(card);
		}
		Collections.shuffle(retCards);
		Collections.shuffle(retCards);
		Collections.shuffle(retCards);
		return retCards;
	}
	//金花散牌
	public static List<List<Byte>> zjhNoneCardTypeList = new ArrayList<>();
	//金花顺子对子
	public static List<List<Byte>> zjhShunZiDoubleList = new ArrayList<>();
	//金花同花以上牌型
	public static List<List<Byte>> zjhSameSuitList = new ArrayList<>();
	
	public static void initZJHCards(){
		List<Byte> cards = GameDefine.initCards();
		for(int i=0; i<cards.size(); i++){
			for(int j=i+1; j<cards.size(); j++){
				for(int k=j+1; k<cards.size(); k++){
					List<Byte> theComboCard = new ArrayList<>();
					theComboCard.add(cards.get(i));
					theComboCard.add(cards.get(j));
					theComboCard.add(cards.get(k));
					AnalysisResult theResult = GameDefine.analysisZJHCards(theComboCard);
					if(theResult.cardType == GameDefine.ZJH_CARD_TYPE_NONE){
						zjhNoneCardTypeList.add(theComboCard);
					}else if(theResult.cardType == GameDefine.ZJH_CARD_TYPE_DOUBLE
							|| theResult.cardType == GameDefine.ZJH_CARD_TYPE_SHUN){
						zjhShunZiDoubleList.add(theComboCard);
					}else{
						zjhSameSuitList.add(theComboCard);
					}
				}
			}
		}
	}
	
	public static AnalysisResult analysisZJHCards(List<Byte> cards){
		AnalysisResult result = new AnalysisResult();
		byte card1 = cards.get(0);
		byte card2 = cards.get(1);
		byte card3 = cards.get(2);
		
		int suit1 = GameDefine.getCardSuit(card1);
		int suit2 = GameDefine.getCardSuit(card2);
		int suit3 = GameDefine.getCardSuit(card3);
		
		int point1 = GameDefine.getCardPoint(card1);
		int point2 = GameDefine.getCardPoint(card2);
		int point3 = GameDefine.getCardPoint(card3);
		
		if((point1==2 && point2==3 && point3==14)
				|| (point1==2 && point2==14 && point3==3)
				|| (point1==3 && point2==14 && point3==2)
				|| (point1==3 && point2==2 && point3==14)
				|| (point1==14 && point2==2 && point3==3)
				|| (point1==14 && point2==3 && point3==2)){
			//A23特殊牌
			if(point1 == 14){
				point1 = 1;
			}
			if(point2 == 14){
				point2 = 1;
			}
			if(point3 == 14){
				point3 = 1;
			}
		}
		
		if(point1==point2 && point1==point3){
			result.cardType = GameDefine.ZJH_CARD_TYPE_FIY;
		}else{
			result.sortedCards.add((byte) point1);
			result.sortedCards.add((byte) point2);
			result.sortedCards.add((byte) point3);
			GameDefine.sortCardsPoint(result.sortedCards);
			
			int tmpPoint1 = result.sortedCards.get(0);
			int tmpPoint2 = result.sortedCards.get(1);
			int tmpPoint3 = result.sortedCards.get(2);
			if(suit1 == suit2 && suit1 == suit3){
				if(tmpPoint1-tmpPoint2 == 1
						&& tmpPoint2-tmpPoint3 == 1){
					result.cardType = GameDefine.ZJH_CARD_TYPE_SAMECOLOE_SHUN;
				}else{
					result.cardType = GameDefine.ZJH_CARD_TYPE_SAMECOLOE;
				}
			}else{
				if(tmpPoint1-tmpPoint2 == 1
						&& tmpPoint2-tmpPoint3 == 1){
					result.cardType = GameDefine.ZJH_CARD_TYPE_SHUN;
				}else{
					if(tmpPoint1 == tmpPoint2 
							|| tmpPoint1==tmpPoint3
							|| tmpPoint2 == tmpPoint3){
						result.cardType = GameDefine.ZJH_CARD_TYPE_DOUBLE;
						if(tmpPoint1==tmpPoint3){
							result.sortedCards.clear();
							result.sortedCards.add((byte) tmpPoint1);
							result.sortedCards.add((byte) tmpPoint3);
							result.sortedCards.add((byte) tmpPoint2);
						}else if(tmpPoint2 == tmpPoint3){
							result.sortedCards.clear();
							result.sortedCards.add((byte) tmpPoint2);
							result.sortedCards.add((byte) tmpPoint3);
							result.sortedCards.add((byte) tmpPoint1);
						}
					}else{
						result.cardType = GameDefine.ZJH_CARD_TYPE_NONE;
					}
				}
			}
		}
		return result;
	}
	
	public static void sortCardsPoint(List<Byte> cards){
		cards.sort(new Comparator<Byte>(){
			@Override
			public int compare(Byte arg0, Byte arg1) {
				if(arg0 > arg1){
					return -1;
				}else if(arg0 < arg1){
					return 1;
				}else{
					return 0;
				}
			}
		});
	}
	
	
	//牛牛无牛牌
	public static List<List<Byte>> noHaveNNList = new ArrayList<>();
	//牛牛有牛牌
	public static List<List<Byte>> haveNNList = new ArrayList<>();
	public static void initNNCard(){
		List<Byte> cards = GameDefine.initCards();
		for(int i=0; i<cards.size(); i++){
			for(int j=i+1; j<cards.size(); j++){
				for(int k=j+1; k<cards.size(); k++){
					for(int x=k+1; x<cards.size(); x++){
						for(int y=x+1; y<cards.size(); y++){
							List<Byte> theComboCard = new ArrayList<>();
							theComboCard.add(cards.get(i));
							theComboCard.add(cards.get(j));
							theComboCard.add(cards.get(k));
							theComboCard.add(cards.get(x));
							theComboCard.add(cards.get(y));
							AnalysisResult theResult = GameDefine.AnalysisNNCards(theComboCard);
							if(theResult.nnNum > 0){
								haveNNList.add(theComboCard);
							}else{
								noHaveNNList.add(theComboCard);
							}
						}
					}
				}
			}
		}
	}
	
	//是否五花牛牛(5张牌都是10点以上)
	public static boolean is5FlowerNN(List<Byte> handCards){
		for(byte card : handCards){
			int point = GameDefine.getCardPoint(card);
			if(point <= 10){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 分析牌
	 * handCards 手牌
	 * return 牛几,0=无牛,10=牛牛
	 * */
	public static AnalysisResult AnalysisNNCards(List<Byte> handCards){
		AnalysisResult retResult = new AnalysisResult();
		List<List<Byte>> retList = new ArrayList<>();
		for(int i=0; i<GameDefine.CARD_HAND_NUM_NN; i++){
			for(int j=i+1; j<GameDefine.CARD_HAND_NUM_NN; j++){
				for(int k=j+1; k<GameDefine.CARD_HAND_NUM_NN; k++){
					byte card1 = handCards.get(i);
					byte card2 = handCards.get(j);
					byte card3 = handCards.get(k);
					
					int point1 = GameDefine.getCardPoint(card1);
					int point2 = GameDefine.getCardPoint(card2);
					int point3 = GameDefine.getCardPoint(card3);
					if(point1 == 14){
						point1 = 1;
					}
					if(point1 == 11 || point1 == 12 || point1==13){
						point1 = 10;
					}
					if(point2 == 14){
						point2 = 1;
					}
					if(point2 == 11 || point2 == 12 || point2==13){
						point2 = 10;
					}
					if(point3 == 14){
						point3 = 1;
					}
					if(point3 == 11 || point3 == 12 || point3==13){
						point3 = 10;
					}
					int N = point1+point2+point3;
					if(N==10 || N==20 || N==30){
						List<Byte> tmpList = new ArrayList<>();
						tmpList.add(card1);
						tmpList.add(card2);
						tmpList.add(card3);
						retList.add(tmpList);
						
						tmpList = new ArrayList<>();
						for(int index=0; index<GameDefine.CARD_HAND_NUM_NN; index++){
							if(index==i || index==j || index==k){
								continue;
							}
							byte theCard = handCards.get(index);
							int theCardPoint = GameDefine.getCardPoint(theCard);
							if(theCardPoint==14){
								theCardPoint = 1;
							}
							if(theCardPoint == 11 || theCardPoint == 12 || theCardPoint==13){
								theCardPoint = 10;
							}
							tmpList.add(theCard);
							retResult.nnNum += theCardPoint;
						}
						retList.add(tmpList);
						retResult.nnNum = retResult.nnNum%10;
						if(retResult.nnNum == 0){
							retResult.nnNum = 10;
						}
						retResult.cards = retList;
						retResult.b5FlowerNN = GameDefine.is5FlowerNN(handCards);
						return retResult;
					}
				}
			}
		}
		retList.add(handCards);
		retResult.cards = retList;
		return retResult;
	}
	
	public static int getCardSuit(byte cardId){
		if(cardId >= 2 && cardId <= 14){
			return 0;
		}
		if(cardId >= 15 && cardId <= 27){
			return 1;
		}
		if(cardId >= 28 && cardId <= 40){
			return 2;
		}
		if(cardId >= 41 && cardId <= 53){
			return 3;
		}
		return 0;
	}
	
	public static int getCardPoint(byte cardId){
		if(cardId >= 2 && cardId <= 14){
			return cardId;
		}
		if(cardId >= 15 && cardId <= 27){
			return cardId-13;
		}
		if(cardId >= 28 && cardId <= 40){
			return cardId-26;
		}
		if(cardId >= 41 && cardId <= 53){
			return cardId-39;
		}
		return 0;
	}
	
	public static String cards2Str(List<Byte> cards){
		String retStr = "";
		for(byte card : cards){
			int suit = GameDefine.getCardSuit(card);
			int point = GameDefine.getCardPoint(card);
			
			switch(suit){
			case 0:
				retStr += "黑桃";
				break;
			case 1:
				retStr += "红桃";
				break;
			case 2:
				retStr += "梅花";
				break;
			case 3:
				retStr += "方块";
				break;
			}
			if(point == 11){
				retStr += "J";
			}else if(point == 12){
				retStr += "Q";
			}else if(point == 13){
				retStr += "K";
			}else if(point == 14){
				retStr += "A";
			}else{
				retStr += ""+point;
			}
			retStr += " ";
		}
		
		return retStr;
	}
}
