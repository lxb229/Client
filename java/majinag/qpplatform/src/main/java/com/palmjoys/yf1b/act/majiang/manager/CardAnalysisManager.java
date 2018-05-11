package com.palmjoys.yf1b.act.majiang.manager;

import java.util.ArrayList;
import java.util.List;

import com.palmjoys.yf1b.act.majiang.model.CardAttrib;
import com.palmjoys.yf1b.act.majiang.model.GameDefine;
import com.palmjoys.yf1b.act.majiang.model.SeatAttrib;
import com.palmjoys.yf1b.act.majiang.model.TableAttrib;
public class CardAnalysisManager {
	
	//对对胡(1番)除巴杠外的牌,形成N副刻子加一对将
	public static boolean isPengPeng(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bDuiDuiHu == false){
			return false;
		}
		
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(handCards);
		if(null != huPaiCard){
			copyCards.add(huPaiCard);
		}
		List<List<CardAttrib>> retList = GameDefine.findThreeCards(copyCards);
		//移除所有刻子
		for(List<CardAttrib> cards : retList){
			GameDefine.removeAllBySuitPoint(copyCards, cards);
		}
		if(copyCards.size() != 2){
			return false;
		}
		CardAttrib card1 = copyCards.get(0);
		CardAttrib card2 = copyCards.get(1);
		if(card1.suit == card2.suit
				&& card1.point == card2.point){
			return true;
		}
		return false;
	}
	
	//清一色(2番)全同一种花色
	public static boolean isQingYiShe(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bQinYiShe == false){
			return false;
		}
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(handCards);
		if(null != huPaiCard){
			copyCards.add(huPaiCard);
		}
		for(List<CardAttrib> anGangCard : anGangCards){
			copyCards.addAll(anGangCard);
		}
		for(List<CardAttrib> baGangCard : baGangCards){
			copyCards.addAll(baGangCard);
		}
		for(List<CardAttrib> dianGangCard : dianGangCards){
			copyCards.addAll(dianGangCard);
		}
		for(List<CardAttrib> pengCard : pengCards){
			copyCards.addAll(pengCard);
		}
				
		List<List<CardAttrib>> retList = GameDefine.spitleSuit(copyCards);
		if(retList.size() == 1){
			return true;
		}
		return false;
	}
	
	//暗七对(2番)手牌都是对子,没有碰和下雨
	public static boolean isAnQiDui(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){		
		if(table.ruleAttrib.bAnQiDui == false){
			return false;
		}
		if(table.ruleAttrib.handCardNum != 13){
			return false;
		}
		if(anGangCards.isEmpty() == false){
			return false;
		}
		if(baGangCards.isEmpty() == false){
			return false;
		}
		if(dianGangCards.isEmpty() == false){
			return false;
		}
		if(pengCards.isEmpty() == false){
			return false;
		}
		
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(handCards);
		
		if(null != huPaiCard){
			copyCards.add(huPaiCard);
		}
		GameDefine.sortCard(copyCards);
		int N = 0;
		for(int i=0; i<copyCards.size()-1;){
			CardAttrib card1 = copyCards.get(i);
			CardAttrib card2 = copyCards.get(i+1);
			if(card1.suit != card2.suit 
					|| card1.point != card2.point){
				return false;
			}
			i += 2;
			N++;
		}
		copyCards = null;
		if(N != 7){
			return false;
		}		
		return true;
	}
	
	//暗四对(2番)手牌都是对子,没有碰和下雨
	public static boolean isAn4Dui(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){		
		if(table.ruleAttrib.bAnQiDui == false){
			return false;
		}
		if(table.ruleAttrib.handCardNum != 7){
			return false;
		}
		
		if(anGangCards.isEmpty() == false){
			return false;
		}
		if(baGangCards.isEmpty() == false){
			return false;
		}
		if(dianGangCards.isEmpty() == false){
			return false;
		}
		if(pengCards.isEmpty() == false){
			return false;
		}
		
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(handCards);
		
		if(null != huPaiCard){
			copyCards.add(huPaiCard);
		}
		GameDefine.sortCard(copyCards);
		int N = 0;
		for(int i=0; i<copyCards.size()-1;){
			CardAttrib card1 = copyCards.get(i);
			CardAttrib card2 = copyCards.get(i+1);
			if(card1.suit != card2.suit 
					|| card1.point != card2.point){
				return false;
			}
			i += 2;
			N++;
		}
		copyCards = null;
		if(N != 4){
			return false;
		}		
		return true;
	}
	
	//龙七对(3番)手牌都是对子有一根,没有碰和下雨
	public static boolean isLongQiDui(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bLongQiDui == false){
			return false;
		}
		if(CardAnalysisManager.isAnQiDui(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard) == false){
			return false;
		}
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(handCards);
		if(null != huPaiCard){
			copyCards.add(huPaiCard);
		}
				
		List<List<CardAttrib>> fourCards = GameDefine.findFourCards(copyCards);
		if(fourCards.isEmpty()){
			return false;
		}
		return true;
	}
	
	//龙4对(3番)手牌都是对子有一根,没有碰和下雨
	public static boolean isLong4Dui(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bLongQiDui == false){
			return false;
		}
		if(table.ruleAttrib.handCardNum != 7){
			return false;
		}
		
		if(CardAnalysisManager.isAn4Dui(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard) == false){
			return false;
		}
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(handCards);
		if(null != huPaiCard){
			copyCards.add(huPaiCard);
		}
				
		List<List<CardAttrib>> fourCards = GameDefine.findFourCards(copyCards);
		if(fourCards.isEmpty()){
			return false;
		}
		return true;
	}
	
	//清对(3番)清一色+对对胡
	public static boolean isQing_PengPeng(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bQinDaDui == false){
			return false;
		}
		boolean bOK = CardAnalysisManager.isPengPeng(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard);
		if(bOK == false){
			return false;
		}
		bOK = CardAnalysisManager.isQingYiShe(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard);
		if(bOK){
			return true;
		}
		return false;
	}
	//清七对(4番)清一色+暗七对
	public static boolean isQing_AnQiDui(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bQinAnQiDui == false){
			return false;
		}
		boolean bOK = CardAnalysisManager.isAnQiDui(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard);
		if(bOK == false){
			return false;
		}
		bOK = CardAnalysisManager.isQingYiShe(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard);
		if(bOK){
			return true;
		}
		return false;
	}
	
	//清四对(4番)清一色+暗四对
	public static boolean isQing_An4Dui(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bQinAnQiDui == false){
			return false;
		}
		if(table.ruleAttrib.handCardNum != 7){
			return false;
		}
		
		boolean bOK = CardAnalysisManager.isAn4Dui(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard);
		if(bOK == false){
			return false;
		}
		bOK = CardAnalysisManager.isQingYiShe(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard);
		if(bOK){
			return true;
		}
		return false;
	}	
	
	//清龙七对(5番)清一色+龙七对
	public static boolean isQing_LongQiDui(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bQinLongQiDui == false){
			return false;
		}
		boolean bOK = CardAnalysisManager.isLongQiDui(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard);
		if(bOK == false){
			return false;
		}
		bOK = CardAnalysisManager.isQingYiShe(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard);
		if(bOK){
			return true;
		}
		return false;
	}
	
	//清龙4对(5番)清一色+龙4对
	public static boolean isQing_Long4Dui(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bQinLongQiDui == false){
			return false;
		}
		if(table.ruleAttrib.handCardNum != 7){
			return false;
		}
		boolean bOK = CardAnalysisManager.isLong4Dui(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard);
		if(bOK == false){
			return false;
		}
		bOK = CardAnalysisManager.isQingYiShe(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard);
		if(bOK){
			return true;
		}
		return false;
	}	
	
	//断幺九(1番)所有组成的顺子,刻子,将牌里不包括1和9
	public static boolean isZhongZhang(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bZhongZhang == false){
			return false;
		}
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(handCards);
		if(null != huPaiCard){
			copyCards.add(huPaiCard);
		}
		for(List<CardAttrib> anGangCard : anGangCards){
			copyCards.addAll(anGangCard);
		}
		for(List<CardAttrib> baGangCard : baGangCards){
			copyCards.addAll(baGangCard);
		}
		for(List<CardAttrib> dianGangCard : dianGangCards){
			copyCards.addAll(dianGangCard);
		}
		for(List<CardAttrib> pengCard : pengCards){
			copyCards.addAll(pengCard);
		}
		for(CardAttrib card : copyCards){
			if(card.point == 1
					|| card.point == 9){
				return false;
			}
		}
		
		return true;
	}
	//全幺九(3番)所有组成的顺子,刻子,将牌里都是有1或9
	public static boolean isQuanYaoJiu(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard, List<List<CardAttrib>> huComboList){
		if(table.ruleAttrib.bYaoJiu == false){
			return false;
		}
		if(anGangCards.isEmpty() == false){
			for(List<CardAttrib> anGangCard : anGangCards){
				for(CardAttrib card : anGangCard){
					if(card.point != 1 && card.point != 9){
						return false;
					}
				}
			}
		}
		if(baGangCards.isEmpty() == false){
			for(List<CardAttrib> baGangCard : baGangCards){
				for(CardAttrib card : baGangCard){
					if(card.point != 1 && card.point != 9){
						return false;
					}
				}
			}
		}
		if(dianGangCards.isEmpty() == false){
			for(List<CardAttrib> dianGangCard : dianGangCards){
				for(CardAttrib card : dianGangCard){
					if(card.point != 1 && card.point != 9){
						return false;
					}
				}
			}
		}
		if(pengCards.isEmpty() == false){
			for(List<CardAttrib> pengCard : pengCards){
				for(CardAttrib card : pengCard){
					if(card.point != 1 && card.point != 9){
						return false;
					}
				}
			}
		}
		
		if(huComboList == null || huComboList.isEmpty()){
			return false;
		}
		
		for(List<CardAttrib> tmpCardList : huComboList){
			boolean bHave19 = false;
			for(CardAttrib card : tmpCardList){
				if(card.point == 1 || card.point == 9){
					bHave19 = true;
					break;
				}
			}
			if(bHave19 == false){
				return false;
			}
		}		
		
		return true;
	}
	//将对(3番)所有牌中都是258组成的对对胡
	public static boolean isJiangDui(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bJiangDui == false){
			return false;
		}
		boolean bOK = CardAnalysisManager.isPengPeng(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard);
		if(bOK == false){
			return false;
		}
		
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(handCards);
		if(null != huPaiCard){
			copyCards.add(huPaiCard);
		}
		for(List<CardAttrib> anGangCard : anGangCards){
			copyCards.addAll(anGangCard);
		}
		for(List<CardAttrib> baGangCard : baGangCards){
			copyCards.addAll(baGangCard);
		}
		for(List<CardAttrib> dianGangCard : dianGangCards){
			copyCards.addAll(dianGangCard);
		}
		for(List<CardAttrib> pengCard : pengCards){
			copyCards.addAll(pengCard);
		}
		for(CardAttrib card : copyCards){
			if(card.point != 2
					&& card.point != 5
					&& card.point != 8){
				return false;
			}
		}
				
		return true;
	}
	
	//将七对(4番)所有牌中都是258组成的龙七对
	public static boolean isJiangLongQiDui(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, 
			List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bJiangDui == false){
			return false;
		}
		boolean bOK = CardAnalysisManager.isLongQiDui(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard);
		if(bOK == false){
			return false;
		}
		
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(handCards);
		if(null != huPaiCard){
			copyCards.add(huPaiCard);
		}
		for(List<CardAttrib> anGangCard : anGangCards){
			copyCards.addAll(anGangCard);
		}
		for(List<CardAttrib> baGangCard : baGangCards){
			copyCards.addAll(baGangCard);
		}
		for(List<CardAttrib> dianGangCard : dianGangCards){
			copyCards.addAll(dianGangCard);
		}
		for(List<CardAttrib> pengCard : pengCards){
			copyCards.addAll(pengCard);
		}
		for(CardAttrib card : copyCards){
			if(card.point != 2
					&& card.point != 5
					&& card.point != 8){
				return false;
			}
		}
		
		return true;
	}
	
	//将4对(4番)所有牌中都是258组成的暗4对
	public static boolean isJiang4Dui(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, 
			List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bJiangDui == false){
			return false;
		}
		if(table.ruleAttrib.handCardNum != 7){
			return false;
		}
		boolean bOK = CardAnalysisManager.isAn4Dui(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard);
		if(bOK == false){
			return false;
		}
		
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(handCards);
		if(null != huPaiCard){
			copyCards.add(huPaiCard);
		}
		for(List<CardAttrib> anGangCard : anGangCards){
			copyCards.addAll(anGangCard);
		}
		for(List<CardAttrib> baGangCard : baGangCards){
			copyCards.addAll(baGangCard);
		}
		for(List<CardAttrib> dianGangCard : dianGangCards){
			copyCards.addAll(dianGangCard);
		}
		for(List<CardAttrib> pengCard : pengCards){
			copyCards.addAll(pengCard);
		}
		for(CardAttrib card : copyCards){
			if(card.point != 2
					&& card.point != 5
					&& card.point != 8){
				return false;
			}
		}
		
		return true;
	}
	
	//将龙4对(4番)所有牌中都是258组成的龙4对
	public static boolean isJiangLong4Dui(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, 
			List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bJiangDui == false){
			return false;
		}
		if(table.ruleAttrib.handCardNum != 7){
			return false;
		}
		boolean bOK = CardAnalysisManager.isLong4Dui(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard);
		if(bOK == false){
			return false;
		}
		
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(handCards);
		if(null != huPaiCard){
			copyCards.add(huPaiCard);
		}
		for(List<CardAttrib> anGangCard : anGangCards){
			copyCards.addAll(anGangCard);
		}
		for(List<CardAttrib> baGangCard : baGangCards){
			copyCards.addAll(baGangCard);
		}
		for(List<CardAttrib> dianGangCard : dianGangCards){
			copyCards.addAll(dianGangCard);
		}
		for(List<CardAttrib> pengCard : pengCards){
			copyCards.addAll(pengCard);
		}
		for(CardAttrib card : copyCards){
			if(card.point != 2
					&& card.point != 5
					&& card.point != 8){
				return false;
			}
		}
		
		return true;
	}
	
	//清将龙4对(4番)所有牌中都是258组成的清龙4对
	public static boolean isQingJiangLong4Dui(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, 
			List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bJiangDui == false){
			return false;
		}
		if(table.ruleAttrib.handCardNum != 7){
			return false;
		}
		boolean bOK = CardAnalysisManager.isLong4Dui(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard);
		if(bOK == false){
			return false;
		}
		
		bOK = CardAnalysisManager.isQingYiShe(table, handCards, anGangCards, baGangCards, dianGangCards, pengCards, huPaiCard);
		if(bOK == false){
			return false;
		}
		
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(handCards);
		if(null != huPaiCard){
			copyCards.add(huPaiCard);
		}
		for(List<CardAttrib> anGangCard : anGangCards){
			copyCards.addAll(anGangCard);
		}
		for(List<CardAttrib> baGangCard : baGangCards){
			copyCards.addAll(baGangCard);
		}
		for(List<CardAttrib> dianGangCard : dianGangCards){
			copyCards.addAll(dianGangCard);
		}
		for(List<CardAttrib> pengCard : pengCards){
			copyCards.addAll(pengCard);
		}
		for(CardAttrib card : copyCards){
			if(card.point != 2
					&& card.point != 5
					&& card.point != 8){
				return false;
			}
		}
		
		return true;
	}	
	
	//门清(1番)胡牌时没有碰过牌,没有明杠
	public static boolean isMengQing(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, 
			List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bMengQin == false){
			return false;
		}
		if(baGangCards.isEmpty()
				&& dianGangCards.isEmpty()
				&& pengCards.isEmpty()){
			return true;
		}
		return false;
	}
	
	//金钩钓(1番)手里只有一张牌
	public static boolean isJinGouDiao(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bJinGouDiao == false){
			return false;
		}
		if(handCards.size() != 1){
			return false;
		}
		
		return true;
	}
	//海底捞(1番)(自摸最后一张胡牌)
	public static boolean isHaiDiLao(TableAttrib table, SeatAttrib seat){
		if(table.ruleAttrib.bHaiDiLao == false){
			return false;
		}
		if(table.btIndex != seat.seatIndex){
			return false;
		}
		if(table.tableCards.isEmpty()){
			return true;
		}
		return false;
	}
	
	//海底炮
	public static boolean isHaiDiPao(TableAttrib table, SeatAttrib seat){
		if(table.ruleAttrib.bHaiDiPao == false){
			return false;
		}
		if(table.btIndex == seat.seatIndex){
			return false;
		}
		if(table.tableCards.isEmpty()){
			return true;
		}
		return false;
	}
	
	//摆独张
	public static boolean isBaiDuZhang(TableAttrib table, SeatAttrib seat){
		if(table.ruleAttrib.bBaiDuZhang == false){
			return false;
		}
		if(seat.tangCanHuList.size() == 1){
			return true;
		}
		return false;
	}
	
	//缺一门
	public static boolean isQueYiMeng(TableAttrib table, SeatAttrib seat){
		if(table.ruleAttrib.bQueYiMeng == false){
			return false;
		}
		List<CardAttrib> handCards = new ArrayList<>();
		handCards.addAll(seat.handCards);
		for(List<CardAttrib> cards : seat.anGangCards){
			handCards.addAll(cards);
		}
		for(List<CardAttrib> cards : seat.dianGangCards){
			handCards.addAll(cards);
		}
		for(List<CardAttrib> cards : seat.baGangCards){
			handCards.addAll(cards);
		}
		for(List<CardAttrib> cards : seat.pengCards){
			handCards.addAll(cards);
		}
		List<List<CardAttrib>> spliteCards = GameDefine.spitleSuit(handCards);
		if(spliteCards.size() == 2){
			return true;
		}
		
		return false;
	}
	
	//一般高
	public static boolean isYiBanGao(TableAttrib table, List<CardAttrib> handCards, List<List<CardAttrib>> anGangCards, List<List<CardAttrib>> baGangCards, 
			List<List<CardAttrib>> dianGangCards, List<List<CardAttrib>> pengCards, CardAttrib huPaiCard){
		if(table.ruleAttrib.bYiBanGao == false){
			return false;
		}
		List<CardAttrib> cards = new ArrayList<>();		
		cards.addAll(handCards);
		if(null != huPaiCard){
			cards.add(huPaiCard);
		}
		List<List<CardAttrib>> spitleSuitList = GameDefine.spitleSuit(cards);
		for(List<CardAttrib> spitleSuits : spitleSuitList){
			List<List<CardAttrib>> doubleLists = GameDefine.findDoubleCards(spitleSuits);
			if(doubleLists.size() < 3){
				continue;
			}
			List<CardAttrib> tmpFinds = new ArrayList<>();
			for(List<CardAttrib> doubles : doubleLists){
				tmpFinds.add(doubles.get(0));
			}
			List<List<CardAttrib>> findShunCardLists = GameDefine.findShunCards(tmpFinds);
			if(findShunCardLists.isEmpty() == false){
				return true;
			}
		}
		
		return false;
	}
	
	//天胡(设定的最大番)(头家起手就胡)
	public static boolean isTianHu(TableAttrib table, SeatAttrib seat){
		if(table.ruleAttrib.bTianDiHu == false){
			return false;
		}
		if(seat.seatIndex != table.bankerIndex){
			return false;
		}
		
		if(seat.outHandNum != 0 || seat.moPaiHandNum !=1){
			return false;
		}
		return true;
	}
	//地胡(设定的最大番)(头家起手点炮)
	public static boolean isDiHu(TableAttrib table, SeatAttrib seat){
		if(table.ruleAttrib.bTianDiHu == false){
			return false;
		}
		if(seat.seatIndex == table.bankerIndex){
			return false;
		} 
		SeatAttrib bankerSeat = table.seats.get(table.bankerIndex);
		if(bankerSeat.outHandNum != 1 || bankerSeat.moPaiHandNum !=1){
			return false;
		}
		
		for(SeatAttrib theSeat : table.seats){
			if(theSeat.seatIndex == table.bankerIndex){
				continue;
			}
			if(theSeat.outHandNum != 0 || theSeat.moPaiHandNum !=0){
				return false;
			}
		}
		
		return true;
	}
	//是否夹心5胡(牌型摆成4和6夹5的胡法)
	public static boolean isJiaXin5(TableAttrib table, SeatAttrib seat, CardAttrib huPaiCard){
		if(table.ruleAttrib.bJiaXin5Hu == false){
			return false;
		}
		if(huPaiCard == null){
			return false;
		}
		if(huPaiCard.point != 5){
			return false;
		}
		boolean bDel4 = false;
		boolean bDel6 = false;
		
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(seat.handCards);
		
		List<CardAttrib> delsCards = new ArrayList<>();
		for(int index=0; index<copyCards.size(); index++){
			CardAttrib theCard = copyCards.get(index);
			if(theCard.suit == huPaiCard.suit
					&& theCard.point == 4 && bDel4==false){
				delsCards.add(theCard);
				bDel4 = true;
			}
			
			if(theCard.suit == huPaiCard.suit
					&& theCard.point == 6 && bDel6==false){
				delsCards.add(theCard);
				bDel6 = true;
			}
			if(bDel4 && bDel6){
				break;
			}
		}
		
		if(bDel4==false || bDel6==false){
			return false;
		}
		copyCards.remove(delsCards.get(0));
		copyCards.remove(delsCards.get(1));
		
		List<List<CardAttrib>> huiList = GameDefine.checkHuPai2(table, copyCards, seat.unSuit);
		if(huiList == null || huiList.isEmpty()){
			return false;
		}
		return true;
	}
	
	//是否卡二条胡(牌型摆成1条和3条夹2条的胡法)
	public static boolean isJiaXin2(TableAttrib table, SeatAttrib seat, CardAttrib huPaiCard){
		if(table.ruleAttrib.bJiaXin2Hu == false){
			return false;
		}
		if(huPaiCard == null){
			return false;
		}
		if(huPaiCard.point != 2 || huPaiCard.suit != GameDefine.SUIT_TYPE_TIAO){
			return false;
		}
		boolean bDel1 = false;
		boolean bDel3 = false;
		
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(seat.handCards);
		
		List<CardAttrib> delsCards = new ArrayList<>();
		for(int index=0; index<copyCards.size(); index++){
			CardAttrib theCard = copyCards.get(index);
			if(theCard.suit == huPaiCard.suit
					&& theCard.point == 1 && bDel1==false){
				delsCards.add(theCard);
				bDel1 = true;
			}
			
			if(theCard.suit == huPaiCard.suit
					&& theCard.point == 3 && bDel3==false){
				delsCards.add(theCard);
				bDel3 = true;
			}
			if(bDel1 && bDel3){
				break;
			}
		}
		
		if(bDel1==false || bDel3==false){
			return false;
		}
		copyCards.remove(delsCards.get(0));
		copyCards.remove(delsCards.get(1));
		
		List<List<CardAttrib>> huList = GameDefine.checkHuPai2(table, copyCards, seat.unSuit);
		if(null == huList || huList.isEmpty()){
			return false;
		}
		return true;
	}
	
	//分析胡牌的牌型
	public static void analysisHuPaiStyle(TableAttrib table, SeatAttrib seat, CardAttrib huPaiCard){
		List<List<CardAttrib>> huComboList = GameDefine.checkHuPai(table, seat, huPaiCard);
		int []huPaiStyle = CardAnalysisManager.analysisHuPaiStyle2(table, seat, huPaiCard, huComboList);
		System.arraycopy(huPaiStyle, 0, seat.huPaiStyle, 0, GameDefine.HUPAI_STYLE_MAX_END);
	}
	
	//分析胡牌的牌型
	public static int[] analysisHuPaiStyle2(TableAttrib table, SeatAttrib seat, CardAttrib huPaiCard, List<List<CardAttrib>> huComboList){
		int []huPaiStyle = new int[GameDefine.HUPAI_STYLE_MAX_END]; 
		
		huPaiStyle[GameDefine.HUPAI_STYLE_SHUHU] = 1;
		if(CardAnalysisManager.isPengPeng(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_PENPEN] = 1;
			if(table.ruleAttrib.bTangPai && seat.tangCardState > 0){
				//如果已躺了牌,不是躺的一张或4张那就不是对对胡
				int N = seat.tangCardList.size();
				if(N != 1 && N != 4){
					huPaiStyle[GameDefine.HUPAI_STYLE_PENPEN] = 0;
				}
			}
		}
		if(CardAnalysisManager.isQingYiShe(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_SAMECOLOR] = 1;
		}
		if(CardAnalysisManager.isAnQiDui(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_QIDUI] = 1;
			if(table.ruleAttrib.bTangPai && seat.tangCardState > 0){
				//如果已躺了牌,不是躺的一张或4张那就不是对对胡
				int N = seat.tangCardList.size();
				if(N != 1){
					huPaiStyle[GameDefine.HUPAI_STYLE_QIDUI] = 0;
				}
			}
		}
		if(CardAnalysisManager.isAn4Dui(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_AN4Dui] = 1;
			if(table.ruleAttrib.bTangPai && seat.tangCardState > 0){
				//如果已躺了牌,不是躺的一张或4张那就不是对对胡
				int N = seat.tangCardList.size();
				if(N != 1){
					huPaiStyle[GameDefine.HUPAI_STYLE_AN4Dui] = 0;
				}
			}
		}
		
		if(CardAnalysisManager.isLongQiDui(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_QIDUI_DOUBLE] = 1;
		}
		if(CardAnalysisManager.isLong4Dui(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_LONG4Dui] = 1;
		}
		
		if(CardAnalysisManager.isQing_PengPeng(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_SAMECOLOR_PENPEN] = 1;
		}
		if(CardAnalysisManager.isQing_AnQiDui(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_SAMECOLOR_QIDUI] = 1;
		}
		if(CardAnalysisManager.isQing_An4Dui(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_QING4Dui] = 1;
		}
		if(CardAnalysisManager.isQing_LongQiDui(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_SAMECOLOR_QIDUI_DOUBLE] = 1;
		}
		if(CardAnalysisManager.isQing_Long4Dui(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_QINGLONG4Dui] = 1;
		}
		if(CardAnalysisManager.isQuanYaoJiu(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard, huComboList)){
			huPaiStyle[GameDefine.HUPAI_STYLE_FULL_19] = 1;
		}
		if(CardAnalysisManager.isJiangDui(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_PENPEN_258] = 1;
		}
		if(CardAnalysisManager.isJiang4Dui(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_JIANG4Dui] = 1;
		}
		if(CardAnalysisManager.isJiangLongQiDui(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_QIDUI_DOUBLE_258] = 1;
		}
		if(CardAnalysisManager.isJiangLong4Dui(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_JIANGLONG4Dui] = 1;
		}
		if(CardAnalysisManager.isQingJiangLong4Dui(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_QINGJIANGLONG4Dui] = 1;
		}
		if(CardAnalysisManager.isMengQing(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_MENQING] = 1;
		}
		if(CardAnalysisManager.isZhongZhang(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_ZHONGZHANG] = 1;
		}
		if(CardAnalysisManager.isJinGouDiao(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_DANDIAO] = 1;
		}
		if(CardAnalysisManager.isHaiDiLao(table, seat)){
			huPaiStyle[GameDefine.HUPAI_STYLE_HAIDILAO] = 1;
		}
		if(CardAnalysisManager.isHaiDiPao(table, seat)){
			huPaiStyle[GameDefine.HUPAI_STYLE_HAIDILAO_DIANPAO] = 1;
		}
		if(CardAnalysisManager.isTianHu(table, seat)){
			huPaiStyle[GameDefine.HUPAI_STYLE_TIANHU] = 1;
		}
		if(CardAnalysisManager.isDiHu(table, seat)){
			huPaiStyle[GameDefine.HUPAI_STYLE_DIEHU] = 1;
		}
		if(CardAnalysisManager.isJiaXin5(table, seat, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_JIXIN5] = 1;
		}
		if(CardAnalysisManager.isJiaXin2(table, seat, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_JIXIN2] = 1;
		}
		if(CardAnalysisManager.isBaiDuZhang(table, seat)){
			huPaiStyle[GameDefine.HUPAI_STYLE_BAIDUZHANG] = 1;
		}
		if(CardAnalysisManager.isQueYiMeng(table, seat)){
			huPaiStyle[GameDefine.HUPAI_STYLE_QUEYIMENG] = 1;
		}
		if(CardAnalysisManager.isYiBanGao(table, seat.handCards, seat.anGangCards, seat.baGangCards, seat.dianGangCards, seat.pengCards, huPaiCard)){
			huPaiStyle[GameDefine.HUPAI_STYLE_YIBANGAO] = 1;
		}
		
		return huPaiStyle;
	}
	
	//胡牌方式转文字描述
	public static String huPaiType2Desc(int huPaiType){
		String retDesc= "";
		switch(huPaiType){
		case GameDefine.HUPAI_TYPE_NONE:
			break;
		case GameDefine.HUPAI_TYPE_ZIMO:
			retDesc= "自摸";
			break;
		case GameDefine.HUPAI_TYPE_DIANPAO:
			retDesc= "接炮";
			break;
		case GameDefine.HUPAI_TYPE_QIANGGANG:
			retDesc= "抢杠胡";
			break;
		case GameDefine.HUPAI_TYPE_GANGFLOW:
			retDesc= "杠上花";
			break;
		case GameDefine.HUPAI_TYPE_DIANGANGFLOW:
			retDesc= "点杠花";
			break;
		case GameDefine.HUPAI_TYPE_DIANGANGPAO:
			retDesc= "点杠炮";
			break;
		case GameDefine.HUPAI_TYPE_CHAJIAO:
			retDesc= "查叫";
			break;
		case GameDefine.HUPAI_TYPE_TINGPAI:
			retDesc= "有叫";
			break;
		}
		return retDesc;
	}
	
	//胡牌牌形转文字描述
	public static String huPaiStyle2Desc(TableAttrib table, SeatAttrib seat){
		if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_4YAOJI] > 0){
			return "四幺鸡";
		}
		
		StringBuilder builder = new StringBuilder();
		
		boolean bcheckQingyishe = true;
		
		if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_PENPEN] > 0){
			bcheckQingyishe = false;
			//是大对胡
			if(builder.length() != 0){
				builder.append(" ");
			}
			if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_SAMECOLOR] > 0){
				//是清一色+大对胡
				if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_PENPEN_258] > 0){
					//是清一色+258的大对胡
					builder.append("清将对");
				}else{
					builder.append("清大对");	
				}
			}else{
				if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_PENPEN_258] > 0){
					builder.append("将对");
				}else{
					builder.append("大对");
				}
			}
		}else{
			if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_QIDUI] > 0){
				bcheckQingyishe = false;
				//是暗七对
				if(builder.length() != 0){
					builder.append(" ");
				}
				if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_QIDUI_DOUBLE] > 0){
					//是龙七对
					if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_QIDUI_DOUBLE_258] > 0){
						//是258的龙七对
						if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_SAMECOLOR] > 0){
							builder.append("清将龙七对");
						}else{
							builder.append("将龙七对");
						}
					}else{
						if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_SAMECOLOR] > 0){
							builder.append("清龙七对");
						}else{
							builder.append("龙七对");
						}
					}
				}else{
					//不是龙七对
					if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_QIDUI_DOUBLE_258] > 0){
						//是258的暗七对
						if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_SAMECOLOR] > 0){
							builder.append("清将七对");
						}else{
							builder.append("将七对");
						}
					}else{
						if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_SAMECOLOR] > 0){
							builder.append("清七对");
						}else{
							builder.append("暗七对");
						}
					}
				}
			}else{
				if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_AN4Dui] > 0){
					bcheckQingyishe = false;
					//是暗四对
					if(builder.length() != 0){
						builder.append(" ");
					}
					if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_LONG4Dui] > 0){
						//是龙四对
						if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_QING4Dui] > 0){
							//清一色
							if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_JIANG4Dui] > 0){
								//258
								builder.append("清将龙四对");
							}else{
								builder.append("清龙四对");
							}
						}else{
							//不是清一色
							if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_JIANG4Dui] > 0){
								//258
								builder.append("将龙四对");
							}else{
								builder.append("龙四对");
							}
						}
					}else{
						//不是龙四对
						if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_QING4Dui] > 0){
							//清一色
							if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_JIANG4Dui] > 0){
								//258
								builder.append("清将四对");
							}else{
								builder.append("清四对");
							}
						}else{
							//不是清一色
							if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_JIANG4Dui] > 0){
								//258
								builder.append("将四对");
							}else{
								builder.append("暗四对");
							}
						}
					}
				}
			}
		}
		
		if(bcheckQingyishe){
			if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_SAMECOLOR] > 0){
				if(builder.length() != 0){
					builder.append(" ");
				}
				builder.append("清一色");
			}
		}
		
		if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_FULL_19] > 0){
			if(builder.length() != 0){
				builder.append(" ");
			}
			builder.append("全幺九");
		}
		
		if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_MENQING] > 0){
			if(builder.length() != 0){
				builder.append(" ");
			}
			builder.append("门清");
		}
		
		if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_ZHONGZHANG] > 0){
			if(builder.length() != 0){
				builder.append(" ");
			}
			builder.append("中张");
		}
		
		if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_DANDIAO] > 0){
			if(builder.length() != 0){
				builder.append(" ");
			}
			builder.append("金钩钓");
		}
		if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_HAIDILAO] > 0){
			if(builder.length() != 0){
				builder.append(" ");
			}
			builder.append("海底捞");
		}
		if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_HAIDILAO_DIANPAO] > 0){
			if(builder.length() != 0){
				builder.append(" ");
			}
			builder.append("海底炮");
		}
		
		if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_TIANHU] > 0){
			if(builder.length() != 0){
				builder.append(" ");
			}
			builder.append("天胡");
		}
		if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_DIEHU] > 0){
			if(builder.length() != 0){
				builder.append(" ");
			}
			builder.append("地胡");
		}
		if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_JIXIN5] > 0){
			if(builder.length() != 0){
				builder.append(" ");
			}
			builder.append("夹心五");
		}
		if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_JIXIN2] > 0){
			if(builder.length() != 0){
				builder.append(" ");
			}
			builder.append("卡二条");
		}
		
		if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_0YAOJI] > 0){
			if(builder.length() != 0){
				builder.append(" ");
			}
			builder.append("无幺鸡");
		}
		
		if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_BAIDUZHANG] > 0){
			if(builder.length() != 0){
				builder.append(" ");
			}
			builder.append("摆独张");
		}
		
		if(seat.huPaiStyle[GameDefine.HUPAI_STYLE_QUEYIMENG] > 0){
			if(builder.length() != 0){
				builder.append(" ");
			}
			builder.append("缺一门");
		}
				
		String retStr = builder.toString();
		if(retStr.isEmpty()){
			retStr = "平胡";
		}
		
		return retStr;
	}
	
	//胡牌牌形总番数
	public static int getHuPaiStyleRate(TableAttrib table, int []huPaiStyle){
		int totalRate = 0;
		
		boolean bcheckQingyishe = true;
		
		if(huPaiStyle[GameDefine.HUPAI_STYLE_PENPEN] > 0){
			//是大对胡
			bcheckQingyishe = false;
			if(table.cfgId == 8){
				//南充麻将
				totalRate += 4;
			}else{
				totalRate += 1;
				if(table.ruleAttrib.bDuiduiHu2Fan){
					totalRate += 1;
				}
			}			
			
			if(huPaiStyle[GameDefine.HUPAI_STYLE_SAMECOLOR] > 0){
				//是清一色+大对胡
				if(table.cfgId == 8){
					//南充麻将
					totalRate += 6;
				}else{
					totalRate += 2;
				}				
				if(huPaiStyle[GameDefine.HUPAI_STYLE_PENPEN_258] > 0){
					//是清一色+258的大对胡
					totalRate += 2;
				}
			}else{
				if(huPaiStyle[GameDefine.HUPAI_STYLE_PENPEN_258] > 0){
					totalRate += 2;
				}
			}
		}else{
			if(huPaiStyle[GameDefine.HUPAI_STYLE_QIDUI] > 0){
				//是暗七对
				bcheckQingyishe = false;
				if(table.cfgId == 8){
					//南充麻将
					totalRate += 6;
				}else{
					totalRate += 2;
				}
				if(huPaiStyle[GameDefine.HUPAI_STYLE_QIDUI_DOUBLE] > 0){
					//是龙七对
					totalRate += 0;
					if(huPaiStyle[GameDefine.HUPAI_STYLE_QIDUI_DOUBLE_258] > 0){
						//是258的龙七对
						totalRate += 2;
						if(huPaiStyle[GameDefine.HUPAI_STYLE_SAMECOLOR] > 0){
							//清一色
							if(table.cfgId == 8){
								//南充麻将
								totalRate += 6;
							}else{
								totalRate += 2;
							}
						}
					}else{
						if(huPaiStyle[GameDefine.HUPAI_STYLE_SAMECOLOR] > 0){
							//清一色
							if(table.cfgId == 8){
								//南充麻将
								totalRate += 6;
							}else{
								totalRate += 2;
							}
						}
					}
				}else{
					//不是龙七对
					if(huPaiStyle[GameDefine.HUPAI_STYLE_QIDUI_DOUBLE_258] > 0){
						//是258的暗七对
						totalRate += 2;
						if(huPaiStyle[GameDefine.HUPAI_STYLE_SAMECOLOR] > 0){
							//清一色
							if(table.cfgId == 8){
								//南充麻将
								totalRate += 6;
							}else{
								totalRate += 2;
							}
						}
					}else{
						if(huPaiStyle[GameDefine.HUPAI_STYLE_SAMECOLOR] > 0){
							//清一色
							if(table.cfgId == 8){
								//南充麻将
								totalRate += 6;
							}else{
								totalRate += 2;
							}
						}
					}
				}
			}else{
				if(huPaiStyle[GameDefine.HUPAI_STYLE_AN4Dui] > 0){
					//是暗四对
					totalRate += 2;
					bcheckQingyishe = false;
					if(huPaiStyle[GameDefine.HUPAI_STYLE_LONG4Dui] > 0){
						//是龙四对
						totalRate += 0;
						if(huPaiStyle[GameDefine.HUPAI_STYLE_QING4Dui] > 0){
							//清一色
							totalRate += 2;
							if(huPaiStyle[GameDefine.HUPAI_STYLE_JIANG4Dui] > 0){
								//258
								totalRate += 2;
							}
						}else{
							//不是清一色
							if(huPaiStyle[GameDefine.HUPAI_STYLE_JIANG4Dui] > 0){
								//258
								totalRate += 2;
							}
						}
					}else{
						//不是龙四对
						if(huPaiStyle[GameDefine.HUPAI_STYLE_QING4Dui] > 0){
							//清一色
							totalRate += 2;
							if(huPaiStyle[GameDefine.HUPAI_STYLE_JIANG4Dui] > 0){
								//258
								totalRate += 2;
							}
						}else{
							//不是清一色
							if(huPaiStyle[GameDefine.HUPAI_STYLE_JIANG4Dui] > 0){
								//258
								totalRate += 2;
							}
						}
					}
				}
			}
		}
		
		if(bcheckQingyishe){
			if(huPaiStyle[GameDefine.HUPAI_STYLE_SAMECOLOR] > 0){
				if(table.cfgId == 8){
					//南充麻将
					totalRate += 6;
				}else{
					totalRate += 2;
				}
			}
		}
		
		if(huPaiStyle[GameDefine.HUPAI_STYLE_FULL_19] > 0){
			totalRate += 3;
		}
		if(huPaiStyle[GameDefine.HUPAI_STYLE_MENQING] > 0){
			totalRate += 1;
		}
		if(huPaiStyle[GameDefine.HUPAI_STYLE_ZHONGZHANG] > 0){
			totalRate += 1;
		}
		if(huPaiStyle[GameDefine.HUPAI_STYLE_DANDIAO] > 0){
			totalRate += 1;
		}
		if(huPaiStyle[GameDefine.HUPAI_STYLE_HAIDILAO] > 0){
			totalRate += 1;
		}
		
		if(huPaiStyle[GameDefine.HUPAI_STYLE_HAIDILAO_DIANPAO] > 0){
			totalRate += 1;
		}
		
		if(huPaiStyle[GameDefine.HUPAI_STYLE_TIANHU] > 0){
			totalRate += 3;
		}
		if(huPaiStyle[GameDefine.HUPAI_STYLE_DIEHU] > 0){
			totalRate += 2;
		}
		if(huPaiStyle[GameDefine.HUPAI_STYLE_JIXIN5] > 0){
			totalRate += 1;
		}
		if(huPaiStyle[GameDefine.HUPAI_STYLE_JIXIN2] > 0){
			totalRate += 1;
		}
		if(huPaiStyle[GameDefine.HUPAI_STYLE_0YAOJI] > 0){
			totalRate += 1;
		}
		
		if(huPaiStyle[GameDefine.HUPAI_STYLE_4YAOJI] > 0){
			totalRate += 16;
		}
		if(huPaiStyle[GameDefine.HUPAI_STYLE_BAIDUZHANG] > 0){
			totalRate += 5;
		}
		if(huPaiStyle[GameDefine.HUPAI_STYLE_QUEYIMENG] > 0){
			totalRate += 1;
		}		
		
		return totalRate;
	}
	
	//胡牌方式番数
	public static int getHuPaiTypeRate(TableAttrib table, int huPaiType){
		int retRate = 0;
		switch(huPaiType){
		case GameDefine.HUPAI_TYPE_NONE:
			break;
		case GameDefine.HUPAI_TYPE_ZIMO:
			if(table.ruleAttrib.bZiMoAddRate){
				retRate = 1;
			}
			break;
		case GameDefine.HUPAI_TYPE_DIANPAO:
			break;
		case GameDefine.HUPAI_TYPE_QIANGGANG:
			retRate = 1;
			break;
		case GameDefine.HUPAI_TYPE_GANGFLOW:
			if(table.ruleAttrib.bZiMoAddRate){
				retRate = 2;
			}else{
				retRate = 1;
			}
			break;
		case GameDefine.HUPAI_TYPE_DIANGANGFLOW:
			if(table.ruleAttrib.bZiMoAddRate){
				retRate = 2;
			}else{
				retRate = 1;
			}
			break;
		case GameDefine.HUPAI_TYPE_DIANGANGPAO:
			retRate = 1;
			break;
		case GameDefine.HUPAI_TYPE_CHAJIAO:
			break;
		case GameDefine.HUPAI_TYPE_TINGPAI:
			break;
		}
		return retRate;
	}
}
