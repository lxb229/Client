package com.palmjoys.yf1b.act.majiang.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.palmjoys.yf1b.act.majiang.manager.CardAnalysisManager;

//游戏全局定义
public class GameDefine {
	//定义桌子游戏状态
	//空闲等待状态
	public static final int STATE_TABLE_IDLE = 0;
	//游戏准备状态
	public static final int STATE_TABLE_READY = 1;
	//发牌状态
	public static final int STATE_TABLE_FAPAI = 2;
	//换牌状态
	public static final int STATE_TABLE_SWAPCARD = 3;
	//定缺状态
	public static final int STATE_TABLE_DINGQUE = 4;
	//摸牌状态
	public static final int STATE_TABLE_MOPAI = 5;
	//出牌状态
	public static final int STATE_TABLE_OUTCARD = 6;
	//杠碰吃胡牌表态状态
	public static final int STATE_TABLE_BREAKCARD = 7;
	//单局结算状态
	public static final int STATE_TABLE_OVER_ONCE = 8;
	//总结算状态
	public static final int STATE_TABLE_OVER_ALL = 9;
	//桌子解散状态
	public static final int STATE_TABLE_DESTORY = 10;
	//报叫状态
	public static final int STATE_TABLE_BAOJIAO = 11;
	//定漂状态
	public static final int STATE_TABLE_PIAOPAI = 12;
	//游戏额外时间等待状态
	public static final int STATE_TABLE_WAIT_EX = 13;
	
	//定义桌子状态时间
	//准备状态时间
	public static final int TIME_TABLE_READY = 2*1000;
	//发牌状态时间
	public static final int TIME_TABLE_FAPAI = 1*1000;
	//换牌等待时间
	public static final int TIME_TABLE_SWAPCARD = 10*1000;
	//定缺状态时间
	public static final int TIME_TABLE_DINGQUE = 15*1000;
	//摸牌状态时间
	public static final int TIME_TABLE_ZHUAPAI = (int) (0*1000);
	//出牌状态时间
	public static final int TIME_TABLE_OUTCARD = 15*1000;
	//杠吃碰胡牌表态时间
	public static final int TIME_TABLE_BREAKCARD = 10*1000;
	//单局结算展示时间
	public static final int TIME_TABLE_OVER_ONCE = 10*1000;
	//桌子解散等待表态时间
	public static final int TIME_TABLE_DESTORY = 5*60*1000;
	//报叫等待时间
	public static final int TIME_TABLE_BAOJIAO = 3*1000;
	//游戏状态额外等待时间
	public static final int TIME_TABLE_WAIT_EX = (int) (0*1000);
	
	//完整牌定义,牌排序按万,筒,条,点数从小到大排
	//座位上配置的牌(KEY=1-4为座位牌,KEY=5桌子牌)
	public static Map<Integer, List<CardAttrib>> tabletPeiPaiMap = new HashMap<>();
	
	public static List<CardAttrib> initCards(int removedSuit){
		List<CardAttrib> cards = new ArrayList<>();
		for(int suit=SUIT_TYPE_WAN; suit<SUIT_TYPE_END; suit++){
			for(int point=1; point<10; point++){
				for(int cardIndex=1; cardIndex<5; cardIndex++){
					CardAttrib cardAttrib = new CardAttrib();
					cardAttrib.suit = suit;
					cardAttrib.point = point;
					cardAttrib.cardId = suit*100 + point*10 + cardIndex;
					cards.add(cardAttrib);
				}
			}
		}
		
		//只打两房牌,去除万字牌
		List<CardAttrib> dels = new ArrayList<>();
		for(CardAttrib card : cards){
			if(card.suit == removedSuit){
				dels.add(card);
			}
		}
		for(CardAttrib card : dels){
			cards.remove(card);
		}
		dels = null;
		
		Collections.shuffle(cards);
		Collections.shuffle(cards);
		Collections.shuffle(cards);
		
		return cards;
	}
	
	//牌总数
	public static final int CARD_MAX_NUM = 108;
	//每个座位初始发牌张数
	public static final int SEAT_CARD_NUM = 13;
	
	//花色定义
	//万
	public static final int SUIT_TYPE_WAN = 1;
	//筒
	public static final int SUIT_TYPE_TONG = 2;
	//条
	public static final int SUIT_TYPE_TIAO = 3;
	//花色
	public static final int SUIT_TYPE_END = SUIT_TYPE_TIAO+1;
	
	//胡杠碰吃过等动作表态定义
	//不要
	public static final int ACT_STATE_DROP = -1;
	//等待表态
	public static final int ACT_STATE_WAIT = 0;
	//已表态
	public static final int ACT_STATE_BT = 1;
	
	//胡杠碰吃过动作索引定义
	//胡
	public static final int ACT_INDEX_HU = 0;
	//杠
	public static final int ACT_INDEX_GANG = 1;
	//碰
	public static final int ACT_INDEX_PENG = 2;
	//吃
	public static final int ACT_INDEX_CHI = 3;
	//躺
	public static final int ACT_INDEX_TANG = 4;
	//过
	public static final int ACT_INDEX_DROP = 5;
	//无效动作
	public static final int ACT_INDEX_VAILD = ACT_INDEX_DROP+1;
	
	//胡牌动作方式
	//未胡牌
	public static final int HUPAI_TYPE_NONE = 0;
	//自摸胡
	public static final int HUPAI_TYPE_ZIMO = 1;
	//普通点炮胡
	public static final int HUPAI_TYPE_DIANPAO = 2;
	//抢杠胡
	public static final int HUPAI_TYPE_QIANGGANG = 3;
	//自摸杠上花
	public static final int HUPAI_TYPE_GANGFLOW = 4;
	//点杠上花胡
	public static final int HUPAI_TYPE_DIANGANGFLOW = 5;
	//点杠上炮
	public static final int HUPAI_TYPE_DIANGANGPAO = 6;
	//查叫
	public static final int HUPAI_TYPE_CHAJIAO = 7;
	//听牌有叫
	public static final int HUPAI_TYPE_TINGPAI = 8;
	
	//杠牌类型
	//无杠
	public static final int GANG_TYPE_NONE = 0;
	//自摸巴杠
	public static final int GANG_TYPE_SELF_BAGANG = 1;
	//自摸暗杠
	public static final int GANG_TYPE_SELF_ANGANG = 2;
	//点杠
	public static final int GANG_TYPE_DIANGANG = 3;
	
	//胡牌牌类型定义
	//非胡类型
	public static final int HUPAI_STYLE_NONE = 0;
	//平胡(0番)四坎(除4副刻子)加一对将
	public static final int HUPAI_STYLE_SHUHU = 1;
	//对对胡(1番)4副刻子加一对将
	public static final int HUPAI_STYLE_PENPEN = 2;
	//清一色(2番)全同一种花色
	public static final int HUPAI_STYLE_SAMECOLOR = 3;
	//暗七对(2番)手牌都是对子,没有碰和下雨
	public static final int HUPAI_STYLE_QIDUI = 4;
	//龙七对(3番)手牌都是对子有一根,没有碰和下雨
	public static final int HUPAI_STYLE_QIDUI_DOUBLE = 5;
	//清对(3番)清一色+对对胡
	public static final int HUPAI_STYLE_SAMECOLOR_PENPEN = 6;
	//清七对(4番)清一色+暗七对
	public static final int HUPAI_STYLE_SAMECOLOR_QIDUI = 7;
	//清龙七对(5番)清一色+暗七对+根*1
	public static final int HUPAI_STYLE_SAMECOLOR_QIDUI_DOUBLE = 8;
	//全幺九(3番)所有组成的顺子,刻子,将牌里都是有1或9
	public static final int HUPAI_STYLE_FULL_19 = 9;
	//将对(3番)所有牌中都是258组成的对对胡(特殊牌型)
	public static final int HUPAI_STYLE_PENPEN_258 = 10;
	//将七对(4番)所有牌中都是258组成的龙七对(特殊牌型)
	public static final int HUPAI_STYLE_QIDUI_DOUBLE_258 = 11;
	//门清(1番)胡牌时没有碰过牌,没有明杠
	public static final int HUPAI_STYLE_MENQING = 12;
	//中张(1番)所有组成的顺子,刻子,将牌里不包括1和9
	public static final int HUPAI_STYLE_ZHONGZHANG = 13;
	//金钩钓(1番)手里只有一张牌
	public static final int HUPAI_STYLE_DANDIAO = 14;
	//海底捞(1番)(自摸最后一张胡牌)
	public static final int HUPAI_STYLE_HAIDILAO = 15;
	//海底炮(1番)(最后一张点炮胡牌)
	public static final int HUPAI_STYLE_HAIDILAO_DIANPAO = 16;
	//天胡(3番)(头家起手就胡)
	public static final int HUPAI_STYLE_TIANHU = 17;
	//地胡(2番)(头家起手点炮)
	public static final int HUPAI_STYLE_DIEHU = 18;
	//夹心5胡(1番)(4,6卡5的胡法)
	public static final int HUPAI_STYLE_JIXIN5 = 19;
	//夹心2胡(1番)(1,3卡2的胡法)
	public static final int HUPAI_STYLE_JIXIN2 = 20;
	//无幺鸡(幺鸡任用模式下,1番)
	public static final int HUPAI_STYLE_0YAOJI = 21;
	//四幺鸡(幺鸡任用模式下,2番)
	public static final int HUPAI_STYLE_4YAOJI = 22;
	//摆独张(南充麻将)
	public static final int HUPAI_STYLE_BAIDUZHANG = 23;
	//缺一门(南充麻将)
	public static final int HUPAI_STYLE_QUEYIMENG = 24;
	//一般 高(南充麻将)
	public static final int HUPAI_STYLE_YIBANGAO = 25;
	//暗四对
	public static final int HUPAI_STYLE_AN4Dui = 26;
	//龙四对
	public static final int HUPAI_STYLE_LONG4Dui = 27;
	//清四对
	public static final int HUPAI_STYLE_QING4Dui = 28;
	//清龙四对
	public static final int HUPAI_STYLE_QINGLONG4Dui = 29;
	//将四对
	public static final int HUPAI_STYLE_JIANG4Dui = 30;
	//将龙四对
	public static final int HUPAI_STYLE_JIANGLONG4Dui = 31;
	//清将龙四对
	public static final int HUPAI_STYLE_QINGJIANGLONG4Dui = 32;
	
	//胡牌牌类型最大定义
	public static final int HUPAI_STYLE_MAX_END = HUPAI_STYLE_QINGJIANGLONG4Dui+1;
	
	public static CardAttrib makeCard(int cardId){
		CardAttrib card = new CardAttrib();
		int suit = cardId/100;
		int point = (cardId-(suit*100))/10;
		card.suit = suit;
		card.point = point;
		card.cardId = cardId;
		return card;
	}
	
	/**
	 * 牌排序
	 * */
	public static void sortCard(List<CardAttrib> cards){
		//按牌花色点数,id从小到大排
		cards.sort(new Comparator<CardAttrib>(){
			@Override
			public int compare(CardAttrib arg0, CardAttrib arg1) {
				if(arg0.suit > arg1.suit){
					return 1;
				}else if(arg0.suit < arg1.suit){
					return -1;
				}else{
					if(arg0.point > arg1.point){
						return 1;
					}else if(arg0.point < arg1.point){
						return -1;
					}else{
						if(arg0.cardId > arg1.cardId){
							return 1;
						}else if(arg0.cardId < arg1.cardId){
							return -1;
						}else{
							return 0;
						}
					}
				}
			}
		});
	}
	//移除牌
	public static boolean removeOnceBySuitPoint(List<CardAttrib> handCards, CardAttrib card){
		CardAttrib findCard = null;
		for(CardAttrib theCard : handCards){
			if(card.suit == theCard.suit
					&& card.point == theCard.point){
				findCard = theCard;
				break;
			}
		}
		if(null != findCard){
			handCards.remove(findCard);
			return true;
		}
		return false;
	}
	
	//移除牌
	public static boolean removeOnceByCardId (List<CardAttrib> handCards, int cardId){
		CardAttrib findCard = null;
		for(CardAttrib theCard : handCards){
			if(cardId == theCard.cardId){
				findCard = theCard;
				break;
			}
		}
		if(null != findCard){
			handCards.remove(findCard);
			return true;
		}
		return false;
	}	
	
	//移除指定花色的牌
	public static void removeAllBySuit(List<CardAttrib> handCards, int suit){
		List<CardAttrib> findCards = new ArrayList<>();
		for(CardAttrib card : handCards){
			if(card.suit != suit){
				findCards.add(card);
			}
		}
		handCards.clear();
		handCards.addAll(findCards);
		findCards = null;
	}
	
	//移除牌
	public static boolean removeAllBySuitPoint(List<CardAttrib> handCards, List<CardAttrib> remove){
		if(remove.isEmpty())
			return true;
		
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(handCards);
		List<CardAttrib> findCards = new ArrayList<CardAttrib>();
		for(CardAttrib card : remove){
			CardAttrib tmpFindCard = null;
			for(CardAttrib findCard : copyCards){
				if(findCard.suit == card.suit
						&& findCard.point == card.point){
					tmpFindCard = findCard;
					break;
				}
			}
			if(null != tmpFindCard){
				copyCards.remove(tmpFindCard);
				findCards.add(tmpFindCard);
			}
		}
		if(findCards.size() != remove.size()){
			return false;
		}
		
		for(CardAttrib findCard : findCards){
			handCards.remove(findCard);
		}
		return true;
	}
	
	//查找指定花色点数的所有牌
	public static List<CardAttrib> findAllBySuitPoint(List<CardAttrib> handCards, int suit, int point){
		List<CardAttrib> retCards = new ArrayList<CardAttrib>();
		for(CardAttrib card : handCards){
			if(card.suit == suit
				&& card.point == point){
				retCards.add(card);
			}
		}
		return retCards;
	}
	
	//查找指定的Id同花色点数的一张牌
	public static CardAttrib findOnceByCardIdOfSuitPoint(List<CardAttrib> handCards, int cardId){
		CardAttrib makeCard = GameDefine.makeCard(cardId);
		for(CardAttrib card : handCards){
			if(card.suit == makeCard.suit
				&& card.point == makeCard.point){
				return card;
			}
		}
		return null;
	}
	
	//查找指定的Id的一张牌
	public static CardAttrib findOnceByCardId(List<CardAttrib> handCards, int cardId){
		for(CardAttrib card : handCards){
			if(card.cardId == cardId){
				return card;
			}
		}
		return null;
	}
	
	
	//查找指定花色点数的一张牌
	public static CardAttrib findOnceBySuitPoint(List<CardAttrib> handCards, int suit, int point){
		for(CardAttrib card : handCards){
			if(card.suit == suit
				&& card.point == point){
				return card;
			}
		}
		return null;
	}
	
	/**
	 * 分析桌子胡杠碰吃
	 * table 桌子
	 * seatIndex 需要分析的座位
	 * prevIndex 上个表态位置
	 * */
	public static int AnalysisBreakCard(TableAttrib table, int seatIndex, int prevIndex, int breakSource){
		switch(table.cfgId){
		case 7://乐山麻将
			return AnalysisBreakCardManager.AnalysisBreakCard_lsmj(table, seatIndex, prevIndex, breakSource);
		case 8://南充麻将
			return AnalysisBreakCardManager.AnalysisBreakCard_ncmj(table, seatIndex, prevIndex, breakSource);
		default:
			return AnalysisBreakCardManager.AnalysisBreakCard_other(table, seatIndex, prevIndex, breakSource);
		}
	}
	
	//找所有对子
	public static List<List<CardAttrib>> findDoubleCards(List<CardAttrib> cards){
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(cards);
		GameDefine.sortCard(copyCards);
		
		List<List<CardAttrib>> retList = new ArrayList<>();
		if(cards.size() < 2){
			return retList;
		}
		int N = copyCards.size(); 
		for(int i=0; i<N-1;){
			CardAttrib card1 = copyCards.get(i);
			CardAttrib card2 = copyCards.get(i+1);
			if(card1.suit == card2.suit
					&& card1.point == card2.point){
				//是一对
				i+=2;
				boolean bfind = false;
				for(List<CardAttrib> tmpList : retList){
					CardAttrib tmpCard = tmpList.get(0);
					if(tmpCard.suit == card1.suit
							&& tmpCard.point == card1.point){
						//已经有了
						bfind = true;
						break;
					}
				}
				if(bfind == false){
					List<CardAttrib> tmpList = new ArrayList<>();
					tmpList.add(card1);
					tmpList.add(card2);
					retList.add(tmpList);
				}
			}else{
				i++;
			}
		}
		copyCards = null;
		return retList;
	}
	//找所有三张
	public static List<List<CardAttrib>> findThreeCards(List<CardAttrib> cards){
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(cards);
		GameDefine.sortCard(copyCards);
		List<List<CardAttrib>> retList = new ArrayList<>();
		if(cards.size() < 3){
			return retList;
		}
		
		int N = copyCards.size(); 
		for(int i=0; i<N-2;){
			CardAttrib card1 = copyCards.get(i);
			CardAttrib card2 = copyCards.get(i+1);
			CardAttrib card3 = copyCards.get(i+2);
			if(card1.suit == card2.suit
					&& card1.point == card2.point
					&& card2.suit == card3.suit
					&& card2.point == card3.point){
				//是三张
				i+=3;
				boolean bfind = false;
				for(List<CardAttrib> tmpList : retList){
					CardAttrib tmpCard = tmpList.get(0);
					if(tmpCard.suit == card1.suit
							&& tmpCard.point == card1.point){
						//已经有了
						bfind = true;
						break;
					}
				}
				if(bfind == false){
					List<CardAttrib> tmpList = new ArrayList<>();
					tmpList.add(card1);
					tmpList.add(card2);
					tmpList.add(card3);
					retList.add(tmpList);
				}
			}else{
				i++;
			}
		}
		copyCards = null;
		return retList;
	} 
	//找所有四张
	public static List<List<CardAttrib>> findFourCards(List<CardAttrib> cards){
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(cards);
		GameDefine.sortCard(copyCards);
		List<List<CardAttrib>> retList = new ArrayList<>();
		if(cards.size() < 4){
			return retList;
		}
		int N = copyCards.size(); 
		for(int i=0; i<N-3;){
			CardAttrib card1 = copyCards.get(i);
			CardAttrib card2 = copyCards.get(i+1);
			CardAttrib card3 = copyCards.get(i+2);
			CardAttrib card4 = copyCards.get(i+3);
			if(card1.suit == card2.suit
					&& card1.point == card2.point
					&& card2.suit == card3.suit
					&& card2.point == card3.point
					&& card3.suit == card4.suit
					&& card3.point == card4.point){
				//是三张
				i+=4;
				boolean bfind = false;
				for(List<CardAttrib> tmpList : retList){
					CardAttrib tmpCard = tmpList.get(0);
					if(tmpCard.suit == card1.suit
							&& tmpCard.point == card1.point){
						//已经有了
						bfind = true;
						break;
					}
				}
				if(bfind == false){
					List<CardAttrib> tmpList = new ArrayList<>();
					tmpList.add(card1);
					tmpList.add(card2);
					tmpList.add(card3);
					tmpList.add(card4);
					retList.add(tmpList);
				}
			}else{
				i++;
			}
		}
		copyCards = null;
		return retList;
	}
	
	//找所有顺子组合
	public static List<List<CardAttrib>> findShunCards(List<CardAttrib> cards){
		List<CardAttrib> copyCards = new ArrayList<>();
		for(CardAttrib card : cards){
			boolean bfind = false;
			for(CardAttrib copyCard : copyCards){
				if(copyCard.suit == card.suit
						&& copyCard.point == card.point){
					bfind = true;
					break;
				}
			}
			if(bfind == false){
				copyCards.add(card);
			}
		}
		
		GameDefine.sortCard(copyCards);
		List<List<CardAttrib>> retList = new ArrayList<>();
		int N = copyCards.size(); 
		for(int i=0; i<N-2;i++){
			CardAttrib card1 = copyCards.get(i);
			CardAttrib card2 = copyCards.get(i+1);
			CardAttrib card3 = copyCards.get(i+2);
			if(card1.suit == card2.suit
					&& card2.suit == card3.suit
					&& ((card3.point-card2.point)==1)
					&& ((card2.point-card1.point)==1)){
				//是三顺
				boolean bfind = false;
				for(List<CardAttrib> tmpList : retList){
					CardAttrib tmpCard1 = tmpList.get(0);
					CardAttrib tmpCard2 = tmpList.get(1);
					CardAttrib tmpCard3 = tmpList.get(2);
					if(tmpCard1.suit == card1.suit
							&& tmpCard1.point == card1.point
							&& tmpCard2.suit == card2.suit
							&& tmpCard2.point == card2.point
							&& tmpCard3.suit == card3.suit
							&& tmpCard3.point == card3.point
							){
						//已经有了
						bfind = true;
						break;
					}
				}
				if(bfind == false){
					List<CardAttrib> tmpList = new ArrayList<>();
					tmpList.add(card1);
					tmpList.add(card2);
					tmpList.add(card3);
					retList.add(tmpList);
				}
			}
		}
		copyCards = null;
		return retList;
	}
	//移除顺牌
	public static void removeShunCards(List<CardAttrib> cards, List<CardAttrib> shunCards){
		List<CardAttrib> cardsA = new ArrayList<>();
		List<CardAttrib> cardsB = new ArrayList<>();
		List<CardAttrib> cardsC = new ArrayList<>();
		
		CardAttrib tmpCardA = shunCards.get(0);
		CardAttrib tmpCardB = shunCards.get(1);
		CardAttrib tmpCardC = shunCards.get(2);
		for(CardAttrib card : cards){
			if(card.suit != tmpCardA.suit){
				continue;
			}
			if(card.point == tmpCardA.point){
				cardsA.add(card);
			}else if(card.point == tmpCardB.point){
				cardsB.add(card);
			}else if(card.point == tmpCardC.point){
				cardsC.add(card);
			}
		}
		int min = Math.min(cardsA.size(), cardsB.size());
		min = Math.min(min, cardsC.size());
		for(int index=0; index<min; index++){
			GameDefine.removeOnceBySuitPoint(cards, cardsA.get(index));
			GameDefine.removeOnceBySuitPoint(cards, cardsB.get(index));
			GameDefine.removeOnceBySuitPoint(cards, cardsC.get(index));
		}		
		cardsA = null;
		cardsB = null;
		cardsC = null;
	}
	//牌去重每个花色点数只保留一张
	public static List<CardAttrib> uniqueCards(List<CardAttrib> cards){
		List<CardAttrib> retList = new ArrayList<>();
		for(CardAttrib card : cards){
			boolean bfind = false;
			for(CardAttrib fCard : retList){
				if(fCard.suit == card.suit
						&& fCard.point == card.point){
					bfind = true;
					break;
				}
			}
			if(bfind == false){
				retList.add(card);
			}
		}		
		return retList;
	}
	//按花色分组牌
	public static List<List<CardAttrib>> spitleSuit(List<CardAttrib> cards){
		List<List<CardAttrib>> retList = new ArrayList<>();
		List<CardAttrib> suitA = new ArrayList<>();
		List<CardAttrib> suitB = new ArrayList<>();
		List<CardAttrib> suitC = new ArrayList<>();
		for(CardAttrib card : cards){
			switch(card.suit){
			case GameDefine.SUIT_TYPE_WAN:
				suitA.add(card);
				break;
			case GameDefine.SUIT_TYPE_TONG:
				suitB.add(card);
				break;
			case GameDefine.SUIT_TYPE_TIAO:
				suitC.add(card);
				break;
			}
		}
		if(suitA.isEmpty() == false){
			retList.add(suitA);
		}
		if(suitB.isEmpty() == false){
			retList.add(suitB);
		}
		if(suitC.isEmpty() == false){
			retList.add(suitC);
		}
		return retList;
	}
	//是否打缺了
	public static boolean isDaQue(List<CardAttrib> copyCards, int unSuit){
		for(CardAttrib card : copyCards){
			if(card.suit == unSuit){
				return false;
			}
		}
		return true;
	}	
	/**
	 * 检查是否可胡
	 * 返回null=不可胡,非空列表=胡牌的组合
	 * */
	public static List<List<CardAttrib>> checkHuPai(TableAttrib table, SeatAttrib seat, CardAttrib huPaiCard){
		List<CardAttrib> cards = new ArrayList<>();
		cards.addAll(seat.handCards);
		if(huPaiCard != null){
			cards.add(huPaiCard);
		}
		
		return GameDefine.checkHuPai2(table, cards, seat.unSuit);
	}
	
	//检查是否平胡
	public static List<List<CardAttrib>> checkHuPai2(TableAttrib table, List<CardAttrib> handCards, int unSuit){
		List<CardAttrib> cards = new ArrayList<>();
		cards.addAll(handCards);
		
		int N = cards.size()%3;
		if(N != 2){
			//手牌张数不满足要求(必须是3*N+2张)
			return null;
		}
		
		if(GameDefine.isDaQue(cards, unSuit) == false){
			//还未打缺
			return null;
		}
		List<List<CardAttrib>> huPaiComboList = new ArrayList<>();
		GameDefine.sortCard(cards);
		//检查特殊牌型(暗七对)
		boolean bHu = CardAnalysisManager.isAnQiDui(table, cards, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
				new ArrayList<>(), null);
		if(bHu){
			List<List<CardAttrib>> fours = GameDefine.findFourCards(cards);
			if(fours.isEmpty() == false){
				huPaiComboList.addAll(fours);
			}
			for(List<CardAttrib> four : fours){
				GameDefine.removeAllBySuitPoint(cards, four);
			}
			List<List<CardAttrib>> doubles = GameDefine.findDoubleCards(cards);
			huPaiComboList.addAll(doubles);
			return huPaiComboList;
		}
		
		bHu = CardAnalysisManager.isAn4Dui(table, cards, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
				new ArrayList<>(), null);
		if(bHu){
			List<List<CardAttrib>> fours = GameDefine.findFourCards(cards);
			if(fours.isEmpty() == false){
				huPaiComboList.addAll(fours);
			}
			for(List<CardAttrib> four : fours){
				GameDefine.removeAllBySuitPoint(cards, four);
			}
			List<List<CardAttrib>> doubles = GameDefine.findDoubleCards(cards);
			huPaiComboList.addAll(doubles);
			return huPaiComboList;
		}
		
		//找到所有对子
		List<List<CardAttrib>> doubleCards = GameDefine.findDoubleCards(cards);
		if(doubleCards.isEmpty()){
			//至少一对将牌才能胡
			return null;
		}
		//保存原始牌
		List<CardAttrib> copyCards = new ArrayList<>();
		for(List<CardAttrib> doubleCard: doubleCards){
			copyCards.clear();
			copyCards.addAll(cards);
			huPaiComboList.clear();
			//检查步骤
			int setp = 1;
			while(setp != 0){
				switch(setp){
				case 1://第一步移除这个对子
					GameDefine.removeAllBySuitPoint(copyCards, doubleCard);
					huPaiComboList.add(doubleCard);
					if(copyCards.isEmpty()){
						//可以胡
						return huPaiComboList;
					}else{
						setp = 2;
					}
					break;
				case 2://第二步检查前三张是否刻子
					{
						CardAttrib card1 = copyCards.get(0);
						CardAttrib card2 = copyCards.get(1);
						CardAttrib card3 = copyCards.get(2);
						if(card1.suit == card2.suit
								&& card1.point == card2.point
								&& card2.suit == card3.suit
								&& card2.point == card3.point){
							GameDefine.removeOnceBySuitPoint(copyCards, card1);
							GameDefine.removeOnceBySuitPoint(copyCards, card2);
							GameDefine.removeOnceBySuitPoint(copyCards, card3);
							List<CardAttrib> tmpShunList = new ArrayList<>();
							tmpShunList.add(card1);
							tmpShunList.add(card2);
							tmpShunList.add(card3);
							huPaiComboList.add(tmpShunList);
							if(copyCards.isEmpty()){
								//可以胡
								return huPaiComboList;
							}else{
								//继续检查三张
								setp = 2;
							}
						}else{
							//不是三张,检查顺子
							setp = 3;
						}
					}
					break;
				case 3://第三步检查当前张是否有顺子
					{
						CardAttrib card1 = copyCards.get(0);
						CardAttrib card2 = null;
						CardAttrib card3 = null;
						for(int index=1; index<copyCards.size(); index++){
							CardAttrib card = copyCards.get(index);
							if(card.suit != card1.suit){
								break;
							}
							if(null == card2){
								//找第二张
								if((card.point-card1.point) == 1){
									card2 = card;
								}
							}else if(null == card3){
								//找第三张
								if((card.point-card2.point) == 1){
									card3 = card;
									break;
								}
							}
						}
						if(null != card1 && null != card2 && null != card3){
							//找到有顺子
							GameDefine.removeOnceBySuitPoint(copyCards, card1);
							GameDefine.removeOnceBySuitPoint(copyCards, card2);
							GameDefine.removeOnceBySuitPoint(copyCards, card3);
							
							List<CardAttrib> tmpShunList = new ArrayList<>();
							tmpShunList.add(card1);
							tmpShunList.add(card2);
							tmpShunList.add(card3);
							huPaiComboList.add(tmpShunList);
							
							if(copyCards.isEmpty()){
								//可以胡
								return huPaiComboList;
							}else{
								//继续检查三张
								setp = 2;
							}
						}else{
							//此一对将牌不能胡牌,继续下一对将牌
							setp = 0;
						}
					}	
					break;
				}
			}
		}		
		return null;
	}
	
	//获取指定花色的所有原始牌
	public static List<CardAttrib> getSuitCards(int suit){
		List<CardAttrib> retList = new ArrayList<>();
		for(int point=1; point<10; point++){
			CardAttrib card = new CardAttrib();
			card.suit = suit;
			card.point = point;
			retList.add(card);
		}
		return retList;
	}
	
	//检查下叫牌
	public static List<CardAttrib> isTingPai(TableAttrib table, List<CardAttrib> handCards, SeatAttrib seat){
		List<CardAttrib> retList = new ArrayList<>();
		if(GameDefine.isDaQue(handCards, seat.unSuit) == false){
			return retList;
		}
		for(int suit=SUIT_TYPE_WAN; suit<SUIT_TYPE_END; suit++){
			if(suit == seat.unSuit){
				continue;
			}
			List<CardAttrib> chkCards = GameDefine.getSuitCards(suit);
			for(CardAttrib card : chkCards){
				List<CardAttrib> tmpList = new ArrayList<>();
				tmpList.addAll(handCards);
				tmpList.add(card);
				List<List<CardAttrib>> huList = GameDefine.checkHuPai2(table, tmpList, seat.unSuit);
				if(null != huList && huList.isEmpty() == false){
					retList.add(card);
				}
				tmpList = null;
			}
		}
		
		return retList;
	}
	
	
	//计算倍率分数
	public static int calculationRateScore(int baseScore, int rate){
		int retScore = baseScore;
		for(int i=0; i<rate; i++){
			retScore = retScore*2;
		}
		return retScore;
	}
	//计算牌的好坏分值
	public static int calculationCardsLuckyValue(List<CardAttrib> cards){
		int retN = 0; 
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(cards);
		
		boolean bSuit1 = GameDefine.isDaQue(copyCards, GameDefine.SUIT_TYPE_WAN);
		boolean bSuit2 = GameDefine.isDaQue(copyCards, GameDefine.SUIT_TYPE_TONG);
		boolean bSuit3 = GameDefine.isDaQue(copyCards, GameDefine.SUIT_TYPE_TIAO);
		if(bSuit1 || bSuit2 || bSuit3){
			retN += 15;
		}
		
		List<List<CardAttrib>> fourCards = GameDefine.findFourCards(copyCards);
		retN += fourCards.size()*8;
		for(List<CardAttrib> cardList : fourCards){
			GameDefine.removeAllBySuitPoint(copyCards, cardList);
		}
		
		List<List<CardAttrib>> threeCards = GameDefine.findFourCards(copyCards);
		retN += threeCards.size()*6;
		for(List<CardAttrib> cardList : threeCards){
			GameDefine.removeAllBySuitPoint(copyCards, cardList);
		}
		
		List<List<CardAttrib>> shunCards = GameDefine.findShunCards(copyCards);
		while(shunCards.isEmpty() == false){
			for(List<CardAttrib> cardList : shunCards){
				boolean bOK = GameDefine.removeAllBySuitPoint(copyCards, cardList);
				if(bOK){
					retN += 3;
				}
			}
			shunCards = GameDefine.findShunCards(copyCards);
		}
		
		List<List<CardAttrib>> doubleCards = GameDefine.findDoubleCards(copyCards);
		retN += doubleCards.size()*2;		
		return retN;
	}
	
	public static String cards2String(List<CardAttrib> cards){
		String str = "";
		for(CardAttrib card : cards){
			str += card.toString();
			str += " ";
		}
		return str;
	}
	
	public static void combine( List<CardAttrib> allSuitCards, int n, int m, int []b, int M, List<List<CardAttrib>> resultCards){
		for(int i=n; i>=m; i--){
			//注意这里的循环范围
		    b[m-1] = i - 1;
		    if (m > 1){
		      combine(allSuitCards, i-1, m-1, b, M, resultCards);
		    }else{
		    	//m == 1, 输出一个组合
		    	List<CardAttrib> tmpList = new ArrayList<>();
		    	for(int j=M-1; j>=0; j--){
		    		tmpList.add(allSuitCards.get(b[j]));
		    	}
		    	resultCards.add(tmpList);
		    }
		}
	}
}
