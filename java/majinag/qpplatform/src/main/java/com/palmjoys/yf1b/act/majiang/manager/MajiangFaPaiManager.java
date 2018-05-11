package com.palmjoys.yf1b.act.majiang.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.core.time.DateUtils;

import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;
import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.entity.AccountLuckyEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountLuckyManager;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.majiang.model.CardAttrib;
import com.palmjoys.yf1b.act.majiang.model.GameDefine;
import com.palmjoys.yf1b.act.majiang.model.SeatAttrib;
import com.palmjoys.yf1b.act.majiang.model.TableAttrib;

@Component
public class MajiangFaPaiManager{
	@Autowired
	private AccountLuckyManager accountLuckManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private EventTriggerManager eventTriggerManager;
	//日志
	private static final Logger logger = LoggerFactory.getLogger(MajiangFaPaiManager.class);
	
	public boolean fapai(TableAttrib table){
		int cardTypeNum = table.ruleAttrib.cardTypeNum;
		int handNum = table.ruleAttrib.handCardNum;
		
		int removedSuit = -1;
		if(cardTypeNum == 2){
			removedSuit = GameDefine.SUIT_TYPE_WAN;
		}
		List<CardAttrib> fullCards = GameDefine.initCards(removedSuit);
		//先从所有牌中删除已配置的牌
		for(int key=1; key<6; key++){
			List<CardAttrib> valueCards = GameDefine.tabletPeiPaiMap.get(key);
			if(null == valueCards){
				valueCards = new ArrayList<>();
			}
			//从theInitCards中删除value已存在的,从value中删除theInitCards不存在的
			List<CardAttrib> unDels = new ArrayList<>();
			for(CardAttrib valueCard : valueCards){
				boolean bDel = GameDefine.removeOnceByCardId(fullCards, valueCard.cardId);
				if(bDel == false){
					unDels.add(valueCard);
				}
			}
			for(CardAttrib unDel : unDels){
				valueCards.remove(unDel);
			}
			GameDefine.tabletPeiPaiMap.put(key, valueCards);
		}
		
		//桌子底牌
		List<CardAttrib> tableCards = new ArrayList<>();
		List<CardAttrib> tmpTableCards = GameDefine.tabletPeiPaiMap.get(5);
		if(null != tmpTableCards && tmpTableCards.isEmpty() == false){
			//添加配置的牌
			tableCards.addAll(tmpTableCards);
		}
		//添加余下的底牌
		tableCards.addAll(fullCards);
		
		List<CardAttrib> chkHandCards = new ArrayList<>();
		
		List<List<CardAttrib>> seatCards = new ArrayList<>();
		for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
			int mapKey = seatIndex+1;
			List<CardAttrib> theCards = new ArrayList<>();
			List<CardAttrib> tmpSeatPepaiCards = GameDefine.tabletPeiPaiMap.get(mapKey);
			theCards.addAll(tmpSeatPepaiCards);
			
			if(theCards.size() > handNum){
				//大于手牌多余的加入桌子底牌
				List<CardAttrib> tmpSeatCards1 = new ArrayList<>();
				for(int cardIndex=0; cardIndex<theCards.size(); cardIndex++){
					CardAttrib tmpTheCard = theCards.get(cardIndex);
					if(cardIndex < handNum){
						tmpSeatCards1.add(tmpTheCard);
					}else{
						tableCards.add(tmpTheCard);
					}
				}
				theCards = tmpSeatCards1;
			}else if(theCards.size() < handNum){
				//小于手牌从桌子底牌加入需要的牌张数
				int needNum = handNum - theCards.size();
				int start = tableCards.size() - needNum;
				List<CardAttrib> dels = new ArrayList<>();
				for(int cardIndex=start; cardIndex<tableCards.size(); cardIndex++){
					CardAttrib tmpTheCard = tableCards.get(cardIndex);
					theCards.add(tmpTheCard);
					dels.add(tmpTheCard);
				}
				for(CardAttrib tmpTheCard : dels){
					GameDefine.removeOnceByCardId(tableCards, tmpTheCard.cardId);
				}
			}
			chkHandCards.addAll(theCards);
			seatCards.add(theCards);
		}
		table.tableCards.clear();
		table.tableCards.addAll(tableCards);
		GameDefine.tabletPeiPaiMap.clear();
		
		//检查下是否有重复的Id的牌
		for(int cardIndex=0; cardIndex<chkHandCards.size(); cardIndex++){
			CardAttrib tmpTheCard = chkHandCards.get(cardIndex);
			for(CardAttrib chkCard : tableCards){
				if(chkCard.cardId == tmpTheCard.cardId){
					logger.warn("检查到手牌与桌子底牌有重复的牌="+chkCard.toString());
				}
			}
		}
		//发牌到座位上
		this.toCardInSeatHand(table, seatCards, false);
		
		StringBuilder builder = null;
		for(SeatAttrib seatAttrib : table.seats){			
			builder = new StringBuilder();
			builder.append("桌子=["+table.tableId).append("] 座位=[" + seatAttrib.seatIndex + "]");
			builder.append(" 手牌={").append(GameDefine.cards2String(seatAttrib.handCards)).append("}");
			logger.warn(builder.toString());
			builder = null;
		}
		builder = new StringBuilder();
		builder.append("桌子底牌={").append(GameDefine.cards2String(table.tableCards)).append("}");
		logger.warn(builder.toString());
		
		seatCards = null;	
		return true;
	}
	
	//发牌到座位上
	private void toCardInSeatHand(TableAttrib table, List<List<CardAttrib>> seatCards, boolean bOpenXingYun){
		if(bOpenXingYun == false){
			for(int cardsIndex=0; cardsIndex<seatCards.size(); cardsIndex++){
				List<CardAttrib> seatCard = seatCards.get(cardsIndex);
				SeatAttrib theSeat = table.seats.get(cardsIndex);
				theSeat.handCards.clear();
				theSeat.handCards.addAll(seatCard);
			}			
			return;
		}
		
		List<SeatAttrib> tmpSeats = new ArrayList<>();
		tmpSeats.addAll(table.seats);
		
		//座位牌按好牌分值从高到低排
		seatCards.sort(new Comparator<List<CardAttrib>>(){
			@Override
			public int compare(List<CardAttrib> arg0, List<CardAttrib> arg1) {
				int luckVal1 = GameDefine.calculationCardsLuckyValue(arg0);
				int luckVal2 = GameDefine.calculationCardsLuckyValue(arg1);
				if(luckVal1 > luckVal2){
					return -1;
				}else if(luckVal1 < luckVal2){
					return 1;
				}
				return 0;
			}
		});
		
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		//座位按幸运值从大到小排
		tmpSeats.sort(new Comparator<SeatAttrib>(){
			@Override
			public int compare(SeatAttrib o1, SeatAttrib o2) {
				int luckVal1 = 0;
				int luckVal2 = 0;
				if(0!=o1.accountId){
					AccountLuckyEntity luckEntity1 = accountLuckManager.loadOrCreate(o1.accountId);
					int N = luckEntity1.getLuckTotalNum()-luckEntity1.getLuckUsedNum();
					if(currTime > luckEntity1.getLuckStartTime() 
							&& currTime < luckEntity1.getLuckEndTime() && N > 0){
						luckVal1 = luckEntity1.getLuck();
					}
				}
				if(0!=o2.accountId){
					AccountLuckyEntity luckEntity2 = accountLuckManager.loadOrCreate(o2.accountId);
					int N = luckEntity2.getLuckTotalNum()-luckEntity2.getLuckUsedNum();
					if(currTime > luckEntity2.getLuckStartTime()
							&& currTime < luckEntity2.getLuckEndTime() && N > 0){
						luckVal2 = luckEntity2.getLuck();
					}
				}				
				if(luckVal1 > luckVal2){
					return -1;
				}else if(luckVal1 < luckVal2){
					return 1;
				}
				return 0;
			}
		});
		
		//发牌到座位上
		for(int cardsIndex=0; cardsIndex<seatCards.size(); cardsIndex++){
			List<CardAttrib> seatCard = seatCards.get(cardsIndex);
			boolean bUsed = false;
			List<SeatAttrib> unUsedSeats = new ArrayList<>();
			for(SeatAttrib tmpSeat : tmpSeats){
				SeatAttrib handSeat = table.seats.get(tmpSeat.seatIndex);
				if(handSeat.handCards.isEmpty() == false){
					continue;
				}
				
				int luckVal = 0;
				int totalNum = 0;
				int usedNum = 0;
				if(0 != handSeat.accountId){
					AccountLuckyEntity luckEntity = accountLuckManager.loadOrCreate(handSeat.accountId);
					totalNum = luckEntity.getLuckTotalNum();
					usedNum = luckEntity.getLuckUsedNum();
					if(currTime > luckEntity.getLuckStartTime()
							&& currTime < luckEntity.getLuckEndTime() && (totalNum-usedNum) > 0){
						luckVal = luckEntity.getLuck()*100;
					}
				}
				int randVal = (int) ((Math.random()*100000)%10000);
				if(randVal < luckVal){
					//在幸运值内
					bUsed = true;
				}else{
					if(totalNum > 0){
						int currLuckVal = (usedNum*100/totalNum)*100;
						if(currLuckVal < luckVal){
							bUsed = true;
						}
					}
				}
				
				if(bUsed){
					handSeat.handCards.addAll(seatCard);
					if(0 == cardsIndex){
						//是最好的牌
						AccountLuckyEntity luckEntity = accountLuckManager.loadOrCreate(handSeat.accountId);
						int totalNum1 = luckEntity.getLuckTotalNum();
						int usedNum1 = luckEntity.getLuckUsedNum();
						if(usedNum1 < totalNum1 && currTime > luckEntity.getLuckStartTime()
								&& currTime < luckEntity.getLuckEndTime()){
							luckEntity.setLuckUsedNum(usedNum1+1);
							
							AccountEntity accountEntity = accountManager.load(luckEntity.getAccountId());
							if(null != accountEntity){
								EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_GAME_LUCK_USED);
								eventAttrib.addEventParam(accountEntity.getStarNO());
								eventAttrib.addEventParam(eventAttrib.eventTime);
								eventTriggerManager.triggerEvent(eventAttrib);
							}
						}
					}
					break;
				}else{
					unUsedSeats.add(tmpSeat);
				}
			}
			if(bUsed == false){
				//这手牌都没人要,随机到一个空座位上
				int seatIndex = (int) (Math.random()*100%unUsedSeats.size());
				SeatAttrib handSeat = table.seats.get(unUsedSeats.get(seatIndex).seatIndex);
				handSeat.handCards.addAll(seatCard);
				if(0 == cardsIndex){
					//是最好的牌
					AccountLuckyEntity luckEntity = accountLuckManager.loadOrCreate(handSeat.accountId);
					int totalNum1 = luckEntity.getLuckTotalNum();
					int usedNum1 = luckEntity.getLuckUsedNum();
					if(usedNum1 < totalNum1 && currTime > luckEntity.getLuckStartTime()
							&& currTime < luckEntity.getLuckEndTime()){
						luckEntity.setLuckUsedNum(usedNum1+1);
						AccountEntity accountEntity = accountManager.load(luckEntity.getAccountId());
						if(null != accountEntity){
							EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_GAME_LUCK_USED);
							eventAttrib.addEventParam(accountEntity.getStarNO());
							eventAttrib.addEventParam(eventAttrib.eventTime);
							eventTriggerManager.triggerEvent(eventAttrib);
						}
					}
				}
			}
			unUsedSeats = null;
		}
	}
	
}
