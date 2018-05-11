package com.palmjoys.yf1b.act.majiang.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.orm.Querier;

import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.majiang.model.CardAttrib;
import com.palmjoys.yf1b.act.majiang.model.GameDataAttrib;
import com.palmjoys.yf1b.act.majiang.model.GameDefine;
import com.palmjoys.yf1b.act.majiang.model.SeatAttrib;
import com.palmjoys.yf1b.act.majiang.model.TableAttrib;

@Component
public class RobotManager {
	@Autowired
	private GameDataManager gameDataManager;
	@Autowired
	private Querier querier;
	
	
	//载入机器人帐号
	public void loadRobot(){
		String querySql = "SELECT A.accountId FROM AccountEntity AS A WHERE A.accountType=-1";
		List<Object> retObjects = querier.listBySqlLimit(AccountEntity.class, Object.class, querySql, 0, 1000);
		for(Object obj : retObjects){
			Long accountId = (Long) obj;
			GameDataAttrib gameDataAttrib = gameDataManager.getGameData(accountId);
			gameDataAttrib.onLine = 1;
			gameDataAttrib.robot = 1;
		}
	}

	//托管AI函数组
	//---------------------------------------------------------------------------------------------
	//换牌托管AI
	public void ai_trusteeship_swapcard(TableAttrib table, SeatAttrib seat, long currTime){
		if(GameDefine.ACT_STATE_WAIT != seat.btState || seat.trusteeshipState != 1){
			return;
		}
		
		//未换牌且设置了托管
		GameDataAttrib gameDataAttrib = this.gameDataManager.getGameData(seat.accountId);
		if(0 == gameDataAttrib.autoBtTime){
			int N = (int) ((Math.random()*1000)%5 + 1);
			gameDataAttrib.autoBtTime = currTime + N*1000;
		}
		if(currTime < gameDataAttrib.autoBtTime){
			return;
		}
		gameDataAttrib.autoBtTime = 0;
		
		List<CardAttrib> needSwapCards = null;
		
		int min = 100;
		//按花色分割牌,选取最少的大于2张牌的列表
		List<List<CardAttrib>> spitleCardLists = GameDefine.spitleSuit(seat.handCards);
		for(List<CardAttrib> spitleCardList : spitleCardLists){
			if(spitleCardList.size() < min && spitleCardList.size() >= 3){
				min = spitleCardList.size();
				needSwapCards = spitleCardList;
			}
		}
		Collections.shuffle(needSwapCards);
		Collections.shuffle(needSwapCards);
		Collections.shuffle(needSwapCards);
		
		CardAttrib suitCard1 = needSwapCards.get(0);
		CardAttrib suitCard2 = needSwapCards.get(1);
		CardAttrib suitCard3 = needSwapCards.get(2);
		seat.swapCards.clear();
		seat.swapBtCards.clear();
		seat.swapBtCards.add(suitCard1);
		seat.swapBtCards.add(suitCard2);
		seat.swapBtCards.add(suitCard3);
		seat.swapCards.addAll(seat.swapBtCards);
		GameDefine.removeOnceBySuitPoint(seat.handCards, suitCard1);
		GameDefine.removeOnceBySuitPoint(seat.handCards, suitCard2);
		GameDefine.removeOnceBySuitPoint(seat.handCards, suitCard3);
		seat.btState = GameDefine.ACT_STATE_BT;
		
		//发送换牌表态通知
		table.sendSwapCardNotify();
		
		spitleCardLists = null;
	}
	
	//定缺托管AI
	public void ai_trusteeship_dinque(TableAttrib table, SeatAttrib seat, long currTime){
		if(GameDefine.ACT_STATE_WAIT != seat.btState || seat.trusteeshipState != 1){
			return;
		}
		
		//未定缺且设置了托管
		GameDataAttrib gameDataAttrib = this.gameDataManager.getGameData(seat.accountId);
		if(0 == gameDataAttrib.autoBtTime){
			int N = (int) ((Math.random()*1000)%3 + 1);
			gameDataAttrib.autoBtTime = currTime + N*1000;
		}
		if(currTime < gameDataAttrib.autoBtTime){
			return;
		}
		gameDataAttrib.autoBtTime = 0;
		
		List<CardAttrib> needSwapCards = null;
		int min = 100;
		//按花色分割牌,选取最少牌的列表
		List<List<CardAttrib>> spitleCardLists = GameDefine.spitleSuit(seat.handCards);
		for(List<CardAttrib> spitleCardList : spitleCardLists){
			if(spitleCardList.size() < min){
				min = spitleCardList.size();
				needSwapCards = spitleCardList;
			}
		}
		int btSuit = needSwapCards.get(0).suit;
		seat.btState = GameDefine.ACT_STATE_BT;
		seat.unSuit = btSuit;
		
		//推送定缺座位数据
		table.sendDinQueNotify(seat.seatIndex);
	}
	
	//出牌托管AI
	public void ai_trusteeship_outcard(TableAttrib table, SeatAttrib seat, long currTime){
		if(GameDefine.ACT_STATE_WAIT != seat.btState || seat.trusteeshipState != 1){
			return;
		}
		
		//未出牌且设置了托管
		GameDataAttrib gameDataAttrib = this.gameDataManager.getGameData(seat.accountId);
		if(0 == gameDataAttrib.autoBtTime){
			int N = (int) ((Math.random()*1000)%3 + 1);
			gameDataAttrib.autoBtTime = currTime + N*1000;
		}
		if(currTime < gameDataAttrib.autoBtTime){
			return;
		}
		gameDataAttrib.autoBtTime = 0;
		
		//未打缺的打缺房牌,已打缺的,如果是摸牌打牌就打摸到的牌,其它随意打一张
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(seat.handCards);
		if(null != seat.moPaiCard){
			copyCards.add(seat.moPaiCard);
		}
		if(table.ruleAttrib.bYaoJiRenYong){
			//幺鸡任用,去除幺鸡牌
			List<CardAttrib> findYaoJicards = GameDefine.findAllBySuitPoint(copyCards, GameDefine.SUIT_TYPE_TIAO, 1);
			GameDefine.removeAllBySuitPoint(copyCards, findYaoJicards);
		}
		CardAttrib findCanOutCard = null;
		boolean bQue = GameDefine.isDaQue(copyCards, seat.unSuit);
		if(bQue == false){
			//没有打缺,打一张缺房牌
			List<CardAttrib> unQueCards = new ArrayList<>();
			for(CardAttrib card : copyCards){
				if(card.suit == seat.unSuit){
					unQueCards.add(card);
				}
			}
			//这样能保证摸牌打牌
			if(unQueCards.isEmpty() == false){
				findCanOutCard = unQueCards.get(unQueCards.size()-1);
			}
		}else{
			//已打缺
			if(table.cfgId == 8){
				//南充麻将,不能打摆了的牌
				List<CardAttrib> unCanOutCards = new ArrayList<>();
				for(SeatAttrib theSeat : table.seats){
					if(seat.seatIndex == theSeat.seatIndex || theSeat.tangCardState <= 0){
						//是自已或没有摆牌的座位跳过
						continue;
					}
					unCanOutCards.addAll(theSeat.tangCanHuList);
				}
				List<CardAttrib> copyCards2 = new ArrayList<>();
				copyCards2.addAll(copyCards);
				GameDefine.removeAllBySuitPoint(copyCards2, unCanOutCards);
				if(copyCards2.isEmpty()){
					//满手都是不能打的牌,打最后一张
					findCanOutCard = copyCards.get(copyCards.size()-1);
				}else{
					//还有可以打的牌从可打的牌中选一张
					findCanOutCard = copyCards2.get(copyCards2.size()-1);
				}
			}else{
				//不是南充麻将特殊打法,从牌尾中选一张打牌
				findCanOutCard = copyCards.get(copyCards.size()-1);
			}
		}
		
		if(null != findCanOutCard){
			if(null != seat.moPaiCard){
				seat.handCards.add(seat.moPaiCard);
			}
			seat.moPaiCard = null;
			
			seat.outUnUseCards.add(findCanOutCard);
			seat.outCard = findCanOutCard;
			GameDefine.removeOnceByCardId(seat.handCards, findCanOutCard.cardId);
			seat.btState = GameDefine.ACT_STATE_BT;
			seat.outHandNum++;
		}		
	}
	
	//中断表态托管AI
	public void ai_trusteeship_breakcard(TableAttrib table, SeatAttrib seat, long currTime){
		if(GameDefine.ACT_STATE_WAIT != seat.btState || seat.trusteeshipState != 1){
			return;
		}
		
		//未表态且设置了托管
		GameDataAttrib gameDataAttrib = this.gameDataManager.getGameData(seat.accountId);
		if(0 == gameDataAttrib.autoBtTime){
			int N = (int) ((Math.random()*1000)%3 + 1);
			gameDataAttrib.autoBtTime = currTime + N*1000;
		}
		if(currTime < gameDataAttrib.autoBtTime){
			return;
		}
		gameDataAttrib.autoBtTime = 0;
		
		SeatAttrib btSeat = table.seats.get(table.btIndex);
		
		if(seat.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
			//是可以胡牌的,直接表态胡牌
			seat.breakBtState = GameDefine.ACT_INDEX_HU;
			seat.btState = GameDefine.ACT_STATE_BT;
			seat.breakCard = btSeat.breakCard;
		}else{
			//不能胡牌的直接表态过
			seat.breakBtState = GameDefine.ACT_INDEX_DROP;
			seat.btState = GameDefine.ACT_STATE_DROP;
		}
	}
	
}
