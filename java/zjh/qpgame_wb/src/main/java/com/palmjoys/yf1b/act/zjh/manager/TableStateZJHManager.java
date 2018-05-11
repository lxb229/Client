package com.palmjoys.yf1b.act.zjh.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.core.time.DateUtils;

import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;
import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.zjh.model.AnalysisResult;
import com.palmjoys.yf1b.act.zjh.model.GameDataAttrib;
import com.palmjoys.yf1b.act.zjh.model.GameDefine;
import com.palmjoys.yf1b.act.zjh.model.SeatAttrib;
import com.palmjoys.yf1b.act.zjh.model.TableAttrib;
import com.palmjoys.yf1b.act.zjh.model.ZJHMessageDefine;
import com.palmjoys.yf1b.act.zjh.resource.RoomConfig;

@Component
public class TableStateZJHManager {
	@Autowired
	private EventTriggerManager eventTriggerManager;
	@Autowired
	private GameProfitPoolManager profitPoolManager;
	
	
	public void state_run(TableAttrib table){
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		switch(table.gameState){
		case GameDefine.STATE_TABLE_IDLE:
			run_idle(table, currTime);
			break;
		case GameDefine.STATE_TABLE_ZJH_READY:
			run_ready(table, currTime);
			break;
		case GameDefine.STATE_TABLE_ZJH_BASESCORE:
			run_baseScore(table, currTime);
			break;
		case GameDefine.STATE_TABLE_ZJH_FAPAI:
			run_fapai(table, currTime);
			break;
		case GameDefine.STATE_TABLE_ZJH_BET:
			run_bet(table, currTime);
			break;
		case GameDefine.STATE_TABLE_ZJH_COMPARE:
			run_compare(table, currTime);
			break;
		case GameDefine.STATE_TABLE_ZJH_OVER:
			run_over(table, currTime);
			break;
		case GameDefine.STATE_TABLE_ZJH_WAIT_EX:
			run_wait_ex(table, currTime);
			break;
		}
	}
	
	private void run_idle(TableAttrib table, long currTime){
		int playerReady = 0;
		for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
			SeatAttrib seat = table.getSeat(seatIndex);
			if(seat.accountId > 0){
				if(seat.btState == GameDefine.ACT_STATE_WAIT){
					GameDataAttrib dataAttrib = table.gameDataManager.getGameData(seat.accountId);
					if(dataAttrib.robot == 1){
						if(seat.btState == GameDefine.ACT_STATE_WAIT){
							if(seat.robotBtTime == 0){
								seat.robotBtTime = (long) (currTime + ((Math.random()*10000)%5 + 1)*1000);
							}
							if(currTime >= seat.robotBtTime){
								seat.btState = GameDefine.ACT_STATE_BT;
								table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
							}
						}					
					}else{
						if(dataAttrib.onLine == 0){
							dataAttrib.tableId = 0;
							seat.accountId = 0;
							seat.reset();
							table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
						}
					}				
				}else{
					playerReady++;
				}
			}
		}
		if(playerReady < 2){
			return;
		}
		
		table.bExec = false;
		table.gameState = GameDefine.STATE_TABLE_ZJH_READY;
	}
	private void run_ready(TableAttrib table, long currTime){
		if(table.bExec == false){
			table.bExec = true;
			table.waitTime = currTime + GameDefine.TIME_WAIT_ZJH_READY;
			table.actTotalTime = GameDefine.TIME_WAIT_ZJH_READY-1000;
			table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_STATE_CHANG_NOTIFY);
		}
		
		int playerReady = 0;
		for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
			SeatAttrib seat = table.getSeat(seatIndex);
			if(seat.accountId > 0){
				if(seat.btState == GameDefine.ACT_STATE_WAIT){
					GameDataAttrib dataAttrib = table.gameDataManager.getGameData(seat.accountId);
					if(dataAttrib.robot == 1){
						if(seat.btState == GameDefine.ACT_STATE_WAIT){
							if(seat.robotBtTime == 0){
								seat.robotBtTime = (long) (currTime + ((Math.random()*10000)%5 + 1)*1000);
							}
							if(currTime >= seat.robotBtTime){
								seat.btState = GameDefine.ACT_STATE_BT;
								table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
							}
						}					
					}else{
						if(dataAttrib.onLine == 0){
							dataAttrib.tableId = 0;
							seat.accountId = 0;
							seat.reset();
							table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
						}
					}				
				}else{
					playerReady++;
				}
			}
		}
		
		if(currTime < table.waitTime){
			return;
		}
		
		if(playerReady >= 2){
			//计算庄家
			RoomConfig cfg = table.cfgManager.getCfg(table.cfgId);
			int baseScore = cfg.getBaseScore();
			for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
				SeatAttrib seat = table.getSeat(seatIndex);
				if(seat.accountId <= 0){
					continue;
				}
				GameDataAttrib dataAttrib = table.gameDataManager.getGameData(seat.accountId);
				if(seat.btState == GameDefine.ACT_STATE_BT){
					table.walletManager.addRoomCard(seat.accountId, 0-baseScore, false);
					table.playerBetMoney(seat.accountId, baseScore);
					seat.bGamed = true;
					dataAttrib.unReadyKick = 0;
				}else{
					dataAttrib.unReadyKick++;
					if(dataAttrib.unReadyKick > 1){
						dataAttrib.tableId = 0;
						seat.accountId = 0;
						dataAttrib.unReadyKick = 0;
						table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
						table.sendKickNotify(dataAttrib.accountId, 3);
					}
				}
			}
			
			table.bankerIndex = this.findBanker(table);
			table.btIndex = table.bankerIndex;
			table.bExec = false;
			table.gameState = GameDefine.STATE_TABLE_ZJH_BASESCORE;
			
			table.lookBetMoney = baseScore;
			table.unLookBetMoney = baseScore;
			table.resetBtState();
			table.btIndex = findNextBtSeat(table);
		}else{
			table.gameState = GameDefine.STATE_TABLE_IDLE;
			table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_STATE_CHANG_NOTIFY);
		}
	}
	private void run_baseScore(TableAttrib table, long currTime){
		if(table.bExec == false){
			table.bExec = true;
			table.waitTime = currTime + GameDefine.TIME_WAIT_ZJH_BASESCORE;
			table.actTotalTime = GameDefine.TIME_WAIT_ZJH_BASESCORE;
			table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_STATE_CHANG_NOTIFY);
		}
		if(currTime < table.waitTime){
			return;
		}
		table.bExec = false;
		table.gameState = GameDefine.STATE_TABLE_ZJH_FAPAI;
	}
	private void run_fapai(TableAttrib table, long currTime){
		if(table.bExec == false){
			table.bExec = true;
			table.waitTime = currTime + GameDefine.TIME_WAIT_ZJH_FAPAI;
			table.actTotalTime = GameDefine.TIME_WAIT_ZJH_FAPAI;
			
			//金花散牌
			List<List<Byte>> tmpZjhNoneCardTypeList = new ArrayList<>();
			//金花顺子对子
			List<List<Byte>> tmpZjhShunZiDoubleList = new ArrayList<>();
			//金花同花以上牌型
			List<List<Byte>> tmpZjhSameSuitList = new ArrayList<>();
			
			tmpZjhNoneCardTypeList.addAll(GameDefine.zjhNoneCardTypeList);
			tmpZjhShunZiDoubleList.addAll(GameDefine.zjhShunZiDoubleList);
			tmpZjhSameSuitList.addAll(GameDefine.zjhSameSuitList);			
			int gamedPlayerNum = 0;
			int cardIndex = 0;
			List<SeatAttrib> robotSeats = new ArrayList<>();
			List<SeatAttrib> playerSeats = new ArrayList<>();
			List<List<Byte>> tableCards = new ArrayList<>();
			for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
				SeatAttrib seat = table.getSeat(seatIndex);
				seat.handCards.clear();
				if(seat.bGamed == false){
					continue;
				}
				GameDataAttrib dataAttrib = table.gameDataManager.getGameData(seat.accountId);
				if(dataAttrib.robot == 1){
					robotSeats.add(seat);
				}else{
					playerSeats.add(seat);
				}
				List<Byte> tmpCards = null; 
				int tmpRandVal = (int) (Math.random()*100);
				if(tmpRandVal >=0 && tmpRandVal<20){
					tmpRandVal = (int) ((Math.random()*100000000)%tmpZjhSameSuitList.size());
					tmpCards = tmpZjhSameSuitList.get(tmpRandVal);
					tmpZjhSameSuitList.remove(tmpRandVal);
				}else if(tmpRandVal >=20 && tmpRandVal<50){
					tmpRandVal = (int) ((Math.random()*100000000)%tmpZjhShunZiDoubleList.size());
					tmpCards = tmpZjhShunZiDoubleList.get(tmpRandVal);
					tmpZjhShunZiDoubleList.remove(tmpRandVal);
				}else{
					tmpRandVal = (int) ((Math.random()*100000000)%tmpZjhNoneCardTypeList.size());
					tmpCards = tmpZjhNoneCardTypeList.get(tmpRandVal);
					tmpZjhNoneCardTypeList.remove(tmpRandVal);
				}
				tableCards.add(tmpCards);
			}
			int maxCardIndex = -1;
			//找出最大的一副牌
			for(int tmpIndex=0; tmpIndex<tableCards.size(); tmpIndex++){
				List<Byte> tmpCards = tableCards.get(tmpIndex);
				if(maxCardIndex == -1){
					maxCardIndex = 0;
				}else{
					List<Byte> tmpMax = tableCards.get(maxCardIndex);
					AnalysisResult resultTmpMax = GameDefine.analysisZJHCards(tmpMax);
					AnalysisResult resultTmpCards = GameDefine.analysisZJHCards(tmpCards);
					int rs = this.compareCard(resultTmpMax, resultTmpCards);
					if(rs <= 0){
						maxCardIndex = tmpIndex;
					}
				}
			}
			gamedPlayerNum = robotSeats.size() + playerSeats.size();
			if(gamedPlayerNum <= 0){
				gamedPlayerNum = 5;
			}
			int robotWinRte = profitPoolManager.getWinRate(1, 1);
			int tmpRobotWinRate = (robotSeats.size()*100)/gamedPlayerNum;
			if(tmpRobotWinRate > robotWinRte){
				robotWinRte = tmpRobotWinRate;
			}
			robotWinRte = robotWinRte * 100;
			int randVal = (int) ((Math.random()*100000)%10000);
			table.bRobotWin = false;
			if(randVal < robotWinRte){
				//机器人赢
				table.bRobotWin = true;
			}
			
			if(table.bRobotWin && robotSeats.isEmpty() == false){
				//机器人要赢,且本局中有机器人,随机一个机器人拿最大的牌
				randVal = (int) ((Math.random()*100)%robotSeats.size());
				SeatAttrib seat = robotSeats.get(randVal);
				seat.handCards.addAll(tableCards.get(maxCardIndex));
			}else{
				//随机一个玩家拿最大的牌
				randVal = (int) ((Math.random()*100)%playerSeats.size());
				SeatAttrib seat = playerSeats.get(randVal);
				seat.handCards.addAll(tableCards.get(maxCardIndex));
			}
			tableCards.remove(maxCardIndex);
			
			cardIndex = 0;
			//开始发牌
			for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
				SeatAttrib seat = table.getSeat(seatIndex);
				if(seat.bGamed == false){
					continue;
				}
				if(seat.handCards.isEmpty()){
					seat.handCards.addAll(tableCards.get(cardIndex));
					cardIndex++;
				}
				GameDataAttrib dataAttrib = table.gameDataManager.getGameData(seat.accountId);
				if(dataAttrib.robot == 0){
					continue;
				}
				
				AnalysisResult seatCardResult = GameDefine.analysisZJHCards(seat.handCards);
				switch(seatCardResult.cardType){
				case GameDefine.ZJH_CARD_TYPE_NONE:
					{//散牌
						byte point = seatCardResult.sortedCards.get(0);
						if(point == 14 || point==13){
							//A大或K大
							if(gamedPlayerNum <= 3){
								seat.betMaxRound = 3;
							}else{
								seat.betMaxRound = 2;
							}
						}else{
							//其它牌不跟注
							seat.betMaxRound = 0;
							randVal = (int) (Math.random()*100);
							if(point == 12 || point == 11){
								if(randVal > 75){
									seat.betMaxRound = 1;
								}
							}else{
								if(point == 10 && randVal > 90){
									seat.betMaxRound = 1;
								}
							}
						}
					}
					break;
				case GameDefine.ZJH_CARD_TYPE_DOUBLE:
					if(gamedPlayerNum <= 3){
						seat.betMaxRound = 4;
					}else{
						seat.betMaxRound = 3;
					}
					break;
				case GameDefine.ZJH_CARD_TYPE_SHUN:
					seat.betMaxRound = 5;
					break;
				case GameDefine.ZJH_CARD_TYPE_SAMECOLOE:
					seat.betMaxRound = 7;
					break;
				case GameDefine.ZJH_CARD_TYPE_SAMECOLOE_SHUN:
					seat.betMaxRound = 10;
					break;
				case GameDefine.ZJH_CARD_TYPE_FIY:
					seat.betMaxRound = 15;
					break;
				}
			}
			table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_STATE_CHANG_NOTIFY);
		}
		if(currTime < table.waitTime){
			return;
		}
		table.bExec = false;
		table.gameState = GameDefine.STATE_TABLE_ZJH_BET;
	}
	private void run_bet(TableAttrib table, long currTime){
		SeatAttrib btSeat = table.getSeat(table.btIndex);
		if(table.bExec == false){
			table.bExec = true;
			table.waitTime = currTime + GameDefine.TIME_WAIT_ZJH_BET;
			table.actTotalTime = GameDefine.TIME_WAIT_ZJH_BET;
			table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_STATE_CHANG_NOTIFY);
			
			btSeat.robotBtTime = 0;
		}
		int gamedPlayerNum = 0;
		for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
			SeatAttrib seat = table.getSeat(seatIndex);
			if(seat.accountId > 0 && seat.bGamed 
					&& seat.btState != GameDefine.ACT_STATE_DROP){
				gamedPlayerNum++;
			}
		}
		if(gamedPlayerNum <= 1){
			table.bExec = false;
			table.gameState = GameDefine.STATE_TABLE_ZJH_OVER;
			return;
		}
		
		if(btSeat.btState != GameDefine.ACT_STATE_WAIT){
			//已经表态
			table.btIndex = this.findNextBtSeat(table);
			table.bExec = false;
			table.gameState = GameDefine.STATE_TABLE_ZJH_WAIT_EX;
			table.nextState = GameDefine.STATE_TABLE_ZJH_BET;
			return;
		}
		if(currTime > table.waitTime){
			//超时了
			btSeat.btState = GameDefine.ACT_STATE_DROP;
			btSeat.btVal = GameDefine.BT_VAL_DROP;
			btSeat.bLookCard = true;
			table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, 0);
			return;
		}
		
		GameDataAttrib dataAttrib = table.gameDataManager.getGameData(btSeat.accountId);
		if(dataAttrib.robot == 0){
			return;
		}
		if(btSeat.robotBtTime == 0){
			btSeat.robotBtTime = (long) (currTime + ((Math.random()*1000)%2 + 1)*1000);
		}
		if(currTime < btSeat.robotBtTime){
			return;
		}
		
		int minJoinLimit = table.cfgManager.getCfg(table.cfgId).getJoinLimit();
		int myNeedBetMoney = table.lookBetMoney;
		int randVal = 0;
		if(btSeat.bLookCard == false){
			myNeedBetMoney = table.unLookBetMoney;
			int lookRate = 35 + table.roundNum*5;			
			if(table.roundNum > btSeat.betMaxRound){
				//当前轮数大于最大下注轮数一定会看牌
				lookRate = 1000;
			}
			randVal = (int) ((Math.random()*1000)%100);
			if(randVal < lookRate){
				btSeat.bLookCard = true;
				btSeat.btVal = GameDefine.BT_VAL_LOOCK;
				table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_LOOKCARD_NOTIFY, btSeat.seatIndex, 0);
				btSeat.robotBtTime = 0;
				return;
			}
		}
		if(btSeat.betMaxRound == 0){
			//看牌后弃牌
			btSeat.btState = GameDefine.ACT_STATE_DROP;
			btSeat.btVal = GameDefine.BT_VAL_DROP;
			btSeat.bLookCard = true;
			//发送下注通知
			table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, 0);
			return;
		}
		
		boolean bIsMax = true;
		AnalysisResult btSeatResult = GameDefine.analysisZJHCards(btSeat.handCards);
		for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
			if(seatIndex == btSeat.seatIndex){
				continue;
			}
			
			SeatAttrib theSeat = table.getSeat(seatIndex);
			if(theSeat.accountId <= 0 || theSeat.bGamed == false 
					|| theSeat.btState == GameDefine.ACT_STATE_DROP){
				continue;
			}
			AnalysisResult dstSeatResult = GameDefine.analysisZJHCards(theSeat.handCards);
			int res = this.compareCard(btSeatResult, dstSeatResult);
			if(res < 0){
				bIsMax = false;
				break;
			}
		}
		int rate = 0;
		int onceMax = table.cfgManager.getCfg(table.cfgId).getOnceMax();
		long myMoney = table.walletManager.getRoomCard(btSeat.accountId);
		if(btSeat.bLookCard == false){
			//没看牌下注
			if(bIsMax){
				//我的牌最大
				if(table.prevSeatIndex >= 0){
					SeatAttrib prevBetSeat = table.getSeat(table.prevSeatIndex);
					if(prevBetSeat.btVal == GameDefine.BT_VAL_BETALL){
						//上家是全压的只能比牌
						btSeat.bLookCard = true;
						btSeat.btVal = GameDefine.BT_VAL_LOOCK;
						table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_LOOKCARD_NOTIFY, btSeat.seatIndex, 0);
						btSeat.robotBtTime = 0;
						return;
					}
				}
				
				if(myMoney <= table.unLookBetMoney){
					//钱不够只能与下家比牌
					btSeat.bLookCard = true;
					btSeat.btVal = GameDefine.BT_VAL_LOOCK;
					table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_LOOKCARD_NOTIFY, btSeat.seatIndex, 0);
					btSeat.robotBtTime = 0;
					return;
				}				
				//加注
				rate = table.roundNum*5;
				randVal = (int) (Math.random()*100);
				if(randVal <= rate && myNeedBetMoney < onceMax){
					int needbetMoney = (int) (myNeedBetMoney + ((Math.random()*1000)%(onceMax-myNeedBetMoney))+1);
					table.playerBetMoney(btSeat.accountId, needbetMoney);
					table.walletManager.addRoomCard(btSeat.accountId, 0-needbetMoney, false);
					table.prevSeatIndex = btSeat.seatIndex;
					table.unLookBetMoney = needbetMoney;
					table.prevSeatBetLookState = btSeat.bLookCard==true?1:0;
					
					int lookBetMoney = needbetMoney*2;
					if(lookBetMoney > onceMax){
						lookBetMoney = onceMax;
					}
					if(lookBetMoney > table.lookBetMoney){
						table.lookBetMoney = lookBetMoney; 
					}
										
					btSeat.btState = GameDefine.ACT_STATE_BT;
					btSeat.btVal = GameDefine.BT_VAL_BETADD;
					//发送下注通知
					table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, needbetMoney);
					return;
				}
				
				if(gamedPlayerNum == 2 && table.roundNum > 1){
					//身上的钱全下
					randVal = (int) (Math.random()*100);
					rate = table.roundNum*3;
					if(randVal < rate){
						long minMoney = table.getGamedPlayerMinMoney();
						table.playerBetMoney(btSeat.accountId, minMoney);
						table.walletManager.addRoomCard(btSeat.accountId, 0-minMoney, false);
						table.prevAllBetMoney = minMoney;
						table.prevSeatIndex = btSeat.seatIndex;
						table.prevSeatBetLookState = btSeat.bLookCard==true?1:0;
						btSeat.btState = GameDefine.ACT_STATE_BT;
						btSeat.btVal = GameDefine.BT_VAL_BETALL;
						//发送下注通知
						table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, minMoney);
						return;
					}
				}
				
				//跟注
				table.playerBetMoney(btSeat.accountId, myNeedBetMoney);
				table.walletManager.addRoomCard(btSeat.accountId, 0-myNeedBetMoney, false);
				table.prevSeatIndex = btSeat.seatIndex;
				table.unLookBetMoney = myNeedBetMoney;
				table.prevSeatBetLookState = btSeat.bLookCard==true?1:0;
				int lookBetMoney = myNeedBetMoney*2;
				if(lookBetMoney > onceMax){
					lookBetMoney = onceMax;
				}
				if(lookBetMoney > table.lookBetMoney){
					table.lookBetMoney = lookBetMoney; 
				}
				btSeat.btState = GameDefine.ACT_STATE_BT;
				btSeat.btVal = GameDefine.BT_VAL_BETSAME;
				//发送下注通知
				table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, myNeedBetMoney);
			}else{
				//未看牌我的牌不是最大的
				if(table.prevSeatIndex >= 0){
					SeatAttrib prevBetSeat = table.getSeat(table.prevSeatIndex);
					if(prevBetSeat.btVal == GameDefine.BT_VAL_BETALL){
						//上家是全压的,先看牌
						btSeat.bLookCard = true;
						btSeat.btVal = GameDefine.BT_VAL_LOOCK;
						table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_LOOKCARD_NOTIFY, btSeat.seatIndex, 0);
						btSeat.robotBtTime = 0;
						return;
					}
				}
				
				if(table.bRobotWin){
					//是我们机器人要赢,检查加注
					rate = table.roundNum*15;
					randVal = (int) (Math.random()*100);
					if(randVal <= rate && myNeedBetMoney < onceMax){
						int needbetMoney = (int) (myNeedBetMoney + ((Math.random()*1000)%(onceMax-myNeedBetMoney))+1);
						table.playerBetMoney(btSeat.accountId, needbetMoney);
						table.walletManager.addRoomCard(btSeat.accountId, 0-needbetMoney, false);
						table.prevSeatIndex = btSeat.seatIndex;
						table.unLookBetMoney = needbetMoney;
						table.prevSeatBetLookState = btSeat.bLookCard==true?1:0;
						
						int lookBetMoney = needbetMoney*2;
						if(lookBetMoney > onceMax){
							lookBetMoney = onceMax;
						}
						if(lookBetMoney > table.lookBetMoney){
							table.lookBetMoney = lookBetMoney; 
						}
											
						btSeat.btState = GameDefine.ACT_STATE_BT;
						btSeat.btVal = GameDefine.BT_VAL_BETADD;
						//发送下注通知
						table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, needbetMoney);
						return;
					}
					
					if(gamedPlayerNum == 2 && table.roundNum > 1){
						//身上的钱全下
						randVal = (int) (Math.random()*100);
						rate = table.roundNum*5;
						if(randVal < rate){
							long minMoney = table.getGamedPlayerMinMoney();
							table.playerBetMoney(btSeat.accountId, minMoney);
							table.walletManager.addRoomCard(btSeat.accountId, 0-minMoney, false);
							table.prevAllBetMoney = minMoney;
							table.prevSeatIndex = btSeat.seatIndex;
							table.prevSeatBetLookState = btSeat.bLookCard==true?1:0;
							btSeat.btState = GameDefine.ACT_STATE_BT;
							btSeat.btVal = GameDefine.BT_VAL_BETALL;
							//发送下注通知
							table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, minMoney);
							return;
						}
					}
				}
				
				//跟注
				table.playerBetMoney(btSeat.accountId, myNeedBetMoney);
				table.walletManager.addRoomCard(btSeat.accountId, 0-myNeedBetMoney, false);
				table.prevSeatIndex = btSeat.seatIndex;
				table.unLookBetMoney = myNeedBetMoney;
				table.prevSeatBetLookState = btSeat.bLookCard==true?1:0;
				int lookBetMoney = myNeedBetMoney*2;
				if(lookBetMoney > onceMax){
					lookBetMoney = onceMax;
				}
				if(lookBetMoney > table.lookBetMoney){
					table.lookBetMoney = lookBetMoney; 
				}
				btSeat.btState = GameDefine.ACT_STATE_BT;
				btSeat.btVal = GameDefine.BT_VAL_BETSAME;
				//发送下注通知
				table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, myNeedBetMoney);
			}
		}else{
			//看了牌下注
			if(bIsMax){
				//看了牌后,我的牌是最大的
				if(table.prevSeatIndex >= 0){
					SeatAttrib prevBetSeat = table.getSeat(table.prevSeatIndex);
					if(prevBetSeat.btVal == GameDefine.BT_VAL_BETALL){
						//上家是全压的只能比牌
						if(btSeatResult.cardType > GameDefine.ZJH_CARD_TYPE_DOUBLE
								|| (btSeatResult.cardType <= GameDefine.ZJH_CARD_TYPE_DOUBLE) && myMoney <= 3*minJoinLimit){
							table.compareCardAttrib.compareScr = btSeat.seatIndex;
							table.compareCardAttrib.compareDst = table.prevSeatIndex;
							table.bExec = false;
							table.gameState = GameDefine.STATE_TABLE_ZJH_COMPARE;
							return;
						}
						if(btSeatResult.cardType == GameDefine.ZJH_CARD_TYPE_DOUBLE){
							rate = 80;
						}else{
							rate = 60;
						}
						randVal = (int) (Math.random()*100);
						if(randVal < rate){
							table.compareCardAttrib.compareScr = btSeat.seatIndex;
							table.compareCardAttrib.compareDst = table.prevSeatIndex;
							table.bExec = false;
							table.gameState = GameDefine.STATE_TABLE_ZJH_COMPARE;
							return;
						}
						
						btSeat.btState = GameDefine.ACT_STATE_DROP;
						btSeat.btVal = GameDefine.BT_VAL_DROP;
						btSeat.bLookCard = true;
						//发送下注通知
						table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, 0);
					}
				}
				if(myMoney <= table.lookBetMoney){
					//钱不够只能与下家比牌
					SeatAttrib nextBtSeat = table.getNextSeat(btSeat.seatIndex);
					table.compareCardAttrib.compareScr = btSeat.seatIndex;
					table.compareCardAttrib.compareDst = nextBtSeat.seatIndex;
					table.bExec = false;
					table.gameState = GameDefine.STATE_TABLE_ZJH_COMPARE;
					return;
				}
				
				//加注
				randVal = (int) (Math.random()*100);
				rate = table.roundNum*8;
				if(randVal <= rate && myNeedBetMoney < onceMax){
					int needbetMoney = (int) (myNeedBetMoney + ((Math.random()*1000)%(onceMax-myNeedBetMoney))+1);
					table.playerBetMoney(btSeat.accountId, needbetMoney);
					table.walletManager.addRoomCard(btSeat.accountId, 0-needbetMoney, false);
					table.prevSeatIndex = btSeat.seatIndex;
					table.lookBetMoney = needbetMoney;
					table.prevSeatBetLookState = btSeat.bLookCard==true?1:0;
					
					int unLookBetMoney = (needbetMoney+1)/2;
					if(unLookBetMoney > onceMax){
						unLookBetMoney = onceMax;
					}
					if(unLookBetMoney > table.unLookBetMoney){
						table.unLookBetMoney = unLookBetMoney; 
					}
										
					btSeat.btState = GameDefine.ACT_STATE_BT;
					btSeat.btVal = GameDefine.BT_VAL_BETADD;
					//发送下注通知
					table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, needbetMoney);
					return;
				}
				
				if(gamedPlayerNum == 2 && table.roundNum > 1){
					//身上的钱全下
					randVal = (int) (Math.random()*100);
					if(btSeatResult.cardType > GameDefine.ZJH_CARD_TYPE_SHUN){
						rate = table.roundNum*5;
					}else if(btSeatResult.cardType == GameDefine.ZJH_CARD_TYPE_SHUN){
						rate = table.roundNum*3;
					}else if(btSeatResult.cardType == GameDefine.ZJH_CARD_TYPE_DOUBLE){
						rate = table.roundNum*1;
					}else{
						rate = 0;
					}
					
					if(randVal < rate){
						long minMoney = table.getGamedPlayerMinMoney();
						table.playerBetMoney(btSeat.accountId, minMoney);
						table.walletManager.addRoomCard(btSeat.accountId, 0-minMoney, false);
						table.prevAllBetMoney = minMoney;
						table.prevSeatIndex = btSeat.seatIndex;
						table.prevSeatBetLookState = btSeat.bLookCard==true?1:0;
						btSeat.btState = GameDefine.ACT_STATE_BT;
						btSeat.btVal = GameDefine.BT_VAL_BETALL;
						//发送下注通知
						table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, minMoney);
						return;
					}
				}
				
				//比牌
				if(table.roundNum > 2){
					if(table.roundNum > btSeat.betMaxRound){
						//大于了最大下注轮数一定比牌
						rate = 10000;
					}else{
						int tmpOnce = 100/btSeat.betMaxRound;
						rate = (table.roundNum-2)*tmpOnce;
					}
					randVal = (int) (Math.random()*100);
					if(randVal <= rate){
						SeatAttrib theSeat = table.getGamedRobotPlayer(btSeat.accountId);
						if(null == theSeat){
							theSeat = table.getGamedPlayer(btSeat.accountId);
						}
						table.compareCardAttrib.compareScr = btSeat.seatIndex;
						table.compareCardAttrib.compareDst = theSeat.seatIndex;
						table.bExec = false;
						table.gameState = GameDefine.STATE_TABLE_ZJH_COMPARE;
						return;
					}
				}
				
				//跟注
				table.playerBetMoney(btSeat.accountId, myNeedBetMoney);
				table.walletManager.addRoomCard(btSeat.accountId, 0-myNeedBetMoney, false);
				table.prevSeatIndex = btSeat.seatIndex;
				table.lookBetMoney = myNeedBetMoney;
				table.prevSeatBetLookState = btSeat.bLookCard==true?1:0;
				int unLookBetMoney = (myNeedBetMoney+1)/2;
				if(unLookBetMoney > onceMax){
					unLookBetMoney = onceMax;
				}
				if(unLookBetMoney > table.unLookBetMoney){
					table.unLookBetMoney = unLookBetMoney; 
				}
				btSeat.btState = GameDefine.ACT_STATE_BT;
				btSeat.btVal = GameDefine.BT_VAL_BETSAME;
				//发送下注通知
				table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, myNeedBetMoney);
				
			}else{
				//看了牌后,我的牌不是最大的
				if(table.prevSeatIndex >= 0){
					SeatAttrib prevBetSeat = table.getSeat(table.prevSeatIndex);
					if(prevBetSeat.btVal == GameDefine.BT_VAL_BETALL){
						//上家是全压的只能比牌
						if(gamedPlayerNum == 2){
							if(btSeatResult.cardType > GameDefine.ZJH_CARD_TYPE_SHUN){
								//如果只两家了,去比牌
								table.compareCardAttrib.compareScr = btSeat.seatIndex;
								table.compareCardAttrib.compareDst = table.prevSeatIndex;
								table.bExec = false;
								table.gameState = GameDefine.STATE_TABLE_ZJH_COMPARE;
							}else{
								rate = 0;
								if(btSeatResult.cardType == GameDefine.ZJH_CARD_TYPE_SHUN){
									rate = 80;
								}else if(btSeatResult.cardType == GameDefine.ZJH_CARD_TYPE_DOUBLE){
									rate = 60;
								}else if(btSeatResult.cardType == GameDefine.ZJH_CARD_TYPE_NONE){
									int point = btSeatResult.sortedCards.get(0);
									if((point == 13 || point == 14) && table.roundNum < 3){
										rate = 30;
									}
								}
								
								randVal = (int) (Math.random()*100);
								if(randVal < rate){
									table.compareCardAttrib.compareScr = btSeat.seatIndex;
									table.compareCardAttrib.compareDst = table.prevSeatIndex;
									table.bExec = false;
									table.gameState = GameDefine.STATE_TABLE_ZJH_COMPARE;
								}else{
									btSeat.btState = GameDefine.ACT_STATE_DROP;
									btSeat.btVal = GameDefine.BT_VAL_DROP;
									btSeat.bLookCard = true;
									//发送下注通知
									table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, 0);
								}
							}
						}else{
							//大于两家就弃牌
							btSeat.btState = GameDefine.ACT_STATE_DROP;
							btSeat.btVal = GameDefine.BT_VAL_DROP;
							btSeat.bLookCard = true;
							//发送下注通知
							table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, 0);
						}
						return;
					}
				}
				
				if(table.bRobotWin){
					//是我们机器人要赢,检查加注
					rate = table.roundNum*8;
					randVal = (int) (Math.random()*100);
					if(randVal < rate && myNeedBetMoney < onceMax){
						int needbetMoney = (int) (myNeedBetMoney + ((Math.random()*1000)%(onceMax-myNeedBetMoney))+1);
						table.playerBetMoney(btSeat.accountId, needbetMoney);
						table.walletManager.addRoomCard(btSeat.accountId, 0-needbetMoney, false);
						table.prevSeatIndex = btSeat.seatIndex;
						table.lookBetMoney = needbetMoney;
						table.prevSeatBetLookState = btSeat.bLookCard==true?1:0;
						
						int unLookBetMoney = (needbetMoney+1)/2;
						if(unLookBetMoney > onceMax){
							unLookBetMoney = onceMax;
						}
						if(unLookBetMoney > table.unLookBetMoney){
							table.unLookBetMoney = unLookBetMoney; 
						}
											
						btSeat.btState = GameDefine.ACT_STATE_BT;
						btSeat.btVal = GameDefine.BT_VAL_BETADD;
						//发送下注通知
						table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, needbetMoney);
						return;
					}
					
					if(gamedPlayerNum == 2 && table.roundNum > 1){
						//身上的钱全下
						randVal = (int) (Math.random()*100);
						rate = table.roundNum*5;
						if(randVal < rate){
							long minMoney = table.getGamedPlayerMinMoney();
							table.playerBetMoney(btSeat.accountId, minMoney);
							table.walletManager.addRoomCard(btSeat.accountId, 0-minMoney, false);
							table.prevAllBetMoney = minMoney;
							table.prevSeatIndex = btSeat.seatIndex;
							table.prevSeatBetLookState = btSeat.bLookCard==true?1:0;
							btSeat.btState = GameDefine.ACT_STATE_BT;
							btSeat.btVal = GameDefine.BT_VAL_BETALL;
							//发送下注通知
							table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, minMoney);
							return;
						}
					}
				}
						
				if(table.roundNum > 2){
					//比牌
					if(table.roundNum > btSeat.betMaxRound 
							|| (gamedPlayerNum == 2 && btSeatResult.cardType > GameDefine.ZJH_CARD_TYPE_NONE)){
						//一定比牌
						rate = 10000;
					}else{
						rate = table.roundNum*25;
					}
					randVal = (int) (Math.random()*100);
					if(randVal < rate){
						SeatAttrib theSeat = table.getGamedRobotPlayer(btSeat.accountId);
						if(null == theSeat){
							theSeat = table.getGamedPlayer(btSeat.accountId);
						}
						table.compareCardAttrib.compareScr = btSeat.seatIndex;
						table.compareCardAttrib.compareDst = theSeat.seatIndex;
						table.bExec = false;
						table.gameState = GameDefine.STATE_TABLE_ZJH_COMPARE;
						return;
					}
				}				
				
				if(table.roundNum > btSeat.betMaxRound){
					if(btSeatResult.cardType < GameDefine.ZJH_CARD_TYPE_SHUN){
						rate = gamedPlayerNum*15;
						randVal = (int) (Math.random()*100);
						if(randVal < rate){
							btSeat.btState = GameDefine.ACT_STATE_DROP;
							btSeat.btVal = GameDefine.BT_VAL_DROP;
							btSeat.bLookCard = true;
							//发送下注通知
							table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, 0);
							return;
						}
					}
				}				
				
				if(myNeedBetMoney >= onceMax/2){
					if(btSeatResult.cardType < GameDefine.ZJH_CARD_TYPE_SHUN){
						btSeat.btState = GameDefine.ACT_STATE_DROP;
						btSeat.btVal = GameDefine.BT_VAL_DROP;
						btSeat.bLookCard = true;
						//发送下注通知
						table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, 0);
						return;
					}
				}
				
				//加注
				rate = 20/table.roundNum;
				if(rate > 10){
					rate = 10;
				}
				if(table.bRobotWin == false){
					rate = 0;
				}
				randVal = (int) (Math.random()*100);
				if(randVal < rate && myNeedBetMoney < onceMax){
					int needbetMoney = (int) (myNeedBetMoney + ((Math.random()*1000)%(onceMax-myNeedBetMoney))+1);
					table.playerBetMoney(btSeat.accountId, needbetMoney);
					table.walletManager.addRoomCard(btSeat.accountId, 0-needbetMoney, false);
					table.prevSeatIndex = btSeat.seatIndex;
					table.lookBetMoney = needbetMoney;
					table.prevSeatBetLookState = btSeat.bLookCard==true?1:0;
					
					int unLookBetMoney = (needbetMoney+1)/2;
					if(unLookBetMoney > onceMax){
						unLookBetMoney = onceMax;
					}
					if(unLookBetMoney > table.unLookBetMoney){
						table.unLookBetMoney = unLookBetMoney; 
					}
										
					btSeat.btState = GameDefine.ACT_STATE_BT;
					btSeat.btVal = GameDefine.BT_VAL_BETADD;
					//发送下注通知
					table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, needbetMoney);
					return;
				}
				
				//跟注
				table.playerBetMoney(btSeat.accountId, myNeedBetMoney);
				table.walletManager.addRoomCard(btSeat.accountId, 0-myNeedBetMoney, false);
				table.prevSeatIndex = btSeat.seatIndex;
				table.lookBetMoney = myNeedBetMoney;
				table.prevSeatBetLookState = btSeat.bLookCard==true?1:0;
				int unLookBetMoney = (myNeedBetMoney+1)/2;
				if(unLookBetMoney > onceMax){
					unLookBetMoney = onceMax;
				}
				if(unLookBetMoney > table.unLookBetMoney){
					table.unLookBetMoney = unLookBetMoney; 
				}
				btSeat.btState = GameDefine.ACT_STATE_BT;
				btSeat.btVal = GameDefine.BT_VAL_BETSAME;
				//发送下注通知
				table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, btSeat.seatIndex, myNeedBetMoney);
			}
		}
	}
	private void run_compare(TableAttrib table, long currTime){
		if(table.bExec == false){
			table.bExec = true;
			table.waitTime = currTime + GameDefine.TIME_WAIT_ZJH_COMPARE;
			table.actTotalTime = GameDefine.TIME_WAIT_ZJH_COMPARE;
			SeatAttrib srcSeat = table.getSeat(table.compareCardAttrib.compareScr);
			SeatAttrib dstSeat = table.getSeat(table.compareCardAttrib.compareDst);
			AnalysisResult srcResult = GameDefine.analysisZJHCards(srcSeat.handCards);
			AnalysisResult dstRsult = GameDefine.analysisZJHCards(dstSeat.handCards);
			int result = compareCard(srcResult, dstRsult);
			table.compareCardAttrib.compareResult = result;
			long myMoney = table.walletManager.getRoomCard(srcSeat.accountId);
			
			long needBetMoney = 0;
			if(dstSeat.btVal == GameDefine.BT_VAL_BETALL){
				//和全下的人比牌
				needBetMoney = table.prevAllBetMoney;
				if(table.prevSeatBetLookState == 0){
					needBetMoney = (table.prevAllBetMoney+1)/2;
				}
			}else{
				if(srcSeat.bLookCard){
					needBetMoney = table.lookBetMoney;
				}else{
					needBetMoney = table.unLookBetMoney;
				}
			}
			long canBetMoney = needBetMoney;
			if(myMoney <= canBetMoney){
				canBetMoney = myMoney;
			}
			table.playerBetMoney(srcSeat.accountId, canBetMoney);
			table.walletManager.addRoomCard(srcSeat.accountId, 0-canBetMoney, false);
			if(result > 0){
				//请求比牌的玩家赢了
				srcSeat.btState = GameDefine.ACT_STATE_BT;
				dstSeat.btState = GameDefine.ACT_STATE_DROP;
				dstSeat.btVal = GameDefine.BT_VAL_DROP;
				dstSeat.bLookCard = true;
				if(myMoney-canBetMoney <= 0){
					table.prevAllBetMoney = canBetMoney;
					srcSeat.btVal = GameDefine.BT_VAL_BETALL;
				}
				table.prevSeatIndex = srcSeat.seatIndex;
			}else{
				//请求比牌的玩家输了,目标座位表态状态保持原样
				srcSeat.btState = GameDefine.ACT_STATE_DROP;
				srcSeat.btVal = GameDefine.BT_VAL_DROP;
				srcSeat.bLookCard = true;
			}			
			table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_STATE_CHANG_NOTIFY);
		}
		if(currTime < table.waitTime){
			return;
		}
		//如果只有一家了结束游戏
		int unBtNum = 0;
		for(SeatAttrib seat : table.seats){
			if(seat.accountId > 0 && seat.bGamed 
					&& seat.btState != GameDefine.ACT_STATE_DROP){
				unBtNum++;
			}
		}
		if(unBtNum == 1){
			table.bExec = false;
			table.gameState = GameDefine.STATE_TABLE_ZJH_OVER;
		}else{
			table.btIndex = this.findNextBtSeat(table);
			table.bExec = false;
			table.gameState = GameDefine.STATE_TABLE_ZJH_BET;
		}
	}
	private void run_over(TableAttrib table, long currTime){
		if(table.bExec == false){
			table.bExec = true;
			table.waitTime = currTime + GameDefine.TIME_WAIT_ZJH_OVER;
			table.actTotalTime = GameDefine.TIME_WAIT_ZJH_OVER;
			
			SeatAttrib winSeat = null;
			for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
				SeatAttrib seat = table.getSeat(seatIndex);
				if(seat.accountId > 0 && seat.bGamed 
						&& seat.btState != GameDefine.ACT_STATE_DROP){
					if(winSeat == null){
						winSeat = seat;
					}
				}
				seat.bLookCard = true;
			}
			int charge = table.cfgManager.getCfg(table.cfgId).getCharge();
			if(table.createPlayer == 0){
				charge = 0;
			}
			int totalWin = 0;
			int myTotalBet = 0;
			int playerBetTotalMoney = 0;
			int robotTotalBet = 0;
			for(Entry<Long, Long> entry: table.betMap.entrySet()){
				long accountId = entry.getKey();
				long money = entry.getValue();
				if(accountId != winSeat.accountId){
					totalWin += money;
					SeatAttrib tmpSeat = table.getPlayerSeat(accountId);
					if(null != tmpSeat && tmpSeat.bGamed){
						table.settlement.addZJHItem(tmpSeat.seatIndex, 0-money);
					}
					
					AccountEntity theAccountEntity = table.accountManager.load(accountId);
					if(null != theAccountEntity){
						if(theAccountEntity.getRobot() == 0){
							EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_GAME);
							eventAttrib.addEventParam(theAccountEntity.getStarNO());
							eventAttrib.addEventParam(String.valueOf(currTime));
							eventAttrib.addEventParam(0-money);
							eventAttrib.addEventParam(table.gameType);
							this.eventTriggerManager.triggerEvent(eventAttrib);
							playerBetTotalMoney += money;
						}else{
							robotTotalBet += money;
						}
					}
				}else{
					myTotalBet += money;
				}
			}
			int chargeWin = (totalWin*charge/100);
			totalWin = totalWin - chargeWin;
			table.settlement.addZJHItem(winSeat.seatIndex, totalWin);
			table.walletManager.addRoomCard(winSeat.accountId, totalWin+myTotalBet, true);			
			table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_STATE_CHANG_NOTIFY);
			
			boolean bRecordWinMoney = false;
			table.bankerIndex = winSeat.seatIndex;
			AccountEntity winAccountEntity = table.accountManager.load(winSeat.accountId);
			if(null != winAccountEntity ){
				if(winAccountEntity.getRobot() == 0){
					EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_GAME);
					eventAttrib.addEventParam(winAccountEntity.getStarNO());
					eventAttrib.addEventParam(String.valueOf(currTime));
					eventAttrib.addEventParam(totalWin);
					eventAttrib.addEventParam(table.gameType);
					this.eventTriggerManager.triggerEvent(eventAttrib);
					bRecordWinMoney = true;
					
					profitPoolManager.saveRobotWinMoney(0-robotTotalBet);
				}else{
					//机器人赢了,记录
					if(playerBetTotalMoney > 0){
						bRecordWinMoney = true;
						profitPoolManager.saveRobotWinMoney(playerBetTotalMoney);
					}
				}
			}
			if(chargeWin > 0 && bRecordWinMoney){
				//上报抽水
				EventAttrib eventAttrib = null;
				eventAttrib = new EventAttrib(EventDefine.EVENT_GAME);
				eventAttrib.addEventParam("0");
				eventAttrib.addEventParam(String.valueOf(currTime));
				eventAttrib.addEventParam(chargeWin);
				eventAttrib.addEventParam(3);
				eventTriggerManager.triggerEvent(eventAttrib);
			}
			
			table.reset();
		}
		if(currTime < table.waitTime){
			return;
		}
		int joinLimit = table.cfgManager.getCfg(table.cfgId).getJoinLimit();
		for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
			SeatAttrib seat = table.getSeat(seatIndex);
			if(seat.accountId <= 0){
				continue;
			}
			GameDataAttrib gameDateAttrib = table.gameDataManager.getGameData(seat.accountId);
			if(gameDateAttrib.robot == 1){
				gameDateAttrib.swapTable = gameDateAttrib.swapTable-1; 
			}
			
			long money = table.walletManager.getRoomCard(seat.accountId);
			if(money < joinLimit || seat.bLeave || (gameDateAttrib.robot==1 && gameDateAttrib.swapTable<=0)
					|| (gameDateAttrib.robot==0&&gameDateAttrib.onLine==0)){
				//推送被踢出座位消息
				seat.joinWaitTime = (long) (currTime + ((Math.random()*100)%45+3)*1000);
				long notifyId = seat.accountId;
				gameDateAttrib.tableId = 0;
				seat.accountId = 0;
				if(seat.bLeave){
					table.sendKickNotify(notifyId, 2);
				}else{
					table.sendKickNotify(notifyId, 1);
				}
				for(int seatIndex1=0; seatIndex1<table.seats.size(); seatIndex1++){
					SeatAttrib seat1 = table.getSeat(seatIndex1);
					if(seat1.accountId > 0){
						table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
					}
				}
			}
		}
		
		table.bExec = false;
		table.gameState = GameDefine.STATE_TABLE_IDLE;
		table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_STATE_CHANG_NOTIFY);
	}
	
	private void run_wait_ex(TableAttrib table, long currTime){
		if(table.bExec == false){
			table.bExec = true;
			table.waitTime = currTime + GameDefine.TIME_WAIT_ZJH_WAITEX;
		}
		if(currTime < table.waitTime){
			return;
		}
		table.gameState = table.nextState;
		table.bExec = false;
	}
		
	private int findBanker(TableAttrib table){
		int banker = 0;
		if(table.bankerIndex < 0){
			//第一局随机庄家
			List<Integer> seatIndexs = new ArrayList<>();
			for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
				SeatAttrib seat = table.getSeat(seatIndex);
				if(seat.accountId > 0 && seat.bGamed){
					seatIndexs.add(seat.seatIndex);
				}
			}
			if(seatIndexs.isEmpty() == false){
				int randVal = (int) ((Math.random()*100)%seatIndexs.size());
				banker = seatIndexs.get(randVal);
			}
			
		}else{
			SeatAttrib bankerSeat = table.getSeat(table.bankerIndex);
			if(bankerSeat.accountId <= 0 || bankerSeat.bGamed == false){
				//庄家不在了,或者庄家本局未参加游戏
				int tmpIndex = table.bankerIndex;
				while(true){
					SeatAttrib nextSeat = table.getNextSeat(tmpIndex);
					if(nextSeat.accountId > 0 && nextSeat.bGamed){
						banker = nextSeat.seatIndex;
						break;
					}
					tmpIndex = nextSeat.seatIndex;
				}
			}else{
				banker = table.bankerIndex;
			}
		}		
		return banker;
	}
	
	private int findNextBtSeat(TableAttrib table){
		int nextIndex = table.btIndex;
		//庄家下一位置
		SeatAttrib bankerNextSeat = table.getNextSeat(table.bankerIndex);
		while(true){
			SeatAttrib nextSeat = table.getNextSeat(nextIndex);
			if(nextSeat.seatIndex == table.bankerIndex){
				for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
					SeatAttrib tmpSeat = table.getSeat(seatIndex);
					if(tmpSeat.accountId > 0 && tmpSeat.bGamed 
							&& tmpSeat.btState != GameDefine.ACT_STATE_DROP){
						tmpSeat.btState = GameDefine.ACT_STATE_WAIT;
					}
				}
			}
			
			if(nextSeat.seatIndex == bankerNextSeat.seatIndex){
				table.roundNum++;
			}
			
			if(nextSeat.accountId > 0 && nextSeat.bGamed 
					&& nextSeat.btState == GameDefine.ACT_STATE_WAIT){
				nextIndex = nextSeat.seatIndex;
				break;
			}
			nextIndex = nextSeat.seatIndex;
		}
		return nextIndex;
	}
	//比较牌-1=src<dst,0=相等,1=src>dst
	private int compareCard(AnalysisResult src, AnalysisResult dst){
		if(src.cardType > dst.cardType){
			return 1;
		}else if(src.cardType < dst.cardType){
			return -1;
		}else{
			//牌型相同比较点数
			for(int i=0; i<GameDefine.CARD_HAND_NUM_ZJH; i++){
				int point1 = src.sortedCards.get(i);
				int point2 = dst.sortedCards.get(i);
				if(point1 > point2){
					return 1;
				}else if(point1 < point2){
					return -1;
				}
			}
		}
		return -1;
	}
	
}
