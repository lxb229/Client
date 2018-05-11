package com.palmjoys.yf1b.act.zjh.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

@Component
public class TableStateNNManager {
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
		case GameDefine.STATE_TABLE_NN_START:
			run_start(table, currTime);
			break;
		case GameDefine.STATE_TABLE_NN_CALL_BANKER:
			run_callBanker(table, currTime);
			break;
		case GameDefine.STATE_TABLE_NN_BET:
			run_bet(table, currTime);
			break;
		case GameDefine.STATE_TABLE_NN_OVER:
			run_over(table, currTime);
			break;
		case GameDefine.STATE_TABLE_NN_WAIT_EX:
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
							table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
						}
					}				
				}else{
					playerReady++;
				}
			}
		}
		if(playerReady != 2){
			return;
		}
		
		//所有人都点击开始,进入开始状态
		table.bExec = false;
		table.gameState = GameDefine.STATE_TABLE_NN_START;
	}
	
	private void run_start(TableAttrib table, long currTime){
		if(table.bExec == false){
			table.bExec = true;
			table.waitTime = currTime + GameDefine.TIME_WAIT_NN_START;
			
			table.resetBtState();
			if(-1 == table.bankerIndex){
				//第一局,由最先进来的玩家先叫庄家
				table.bankerIndex = 0;
			}
			table.btIndex = table.bankerIndex;
			table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_STATE_CHANG_NOTIFY);
			for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
				SeatAttrib seat = table.getSeat(seatIndex);
				if(seat.accountId > 0){
					seat.bGamed = true;
				}
			}
		}
		
		table.bExec = false;
		table.gameState = GameDefine.STATE_TABLE_NN_CALL_BANKER;
	}	
	
	private void run_callBanker(TableAttrib table, long currTime){
		if(table.bExec == false){
			table.bExec = true;
			table.waitTime = currTime + GameDefine.TIME_WAIT_NN_CALL_BANKER;			
			table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_STATE_CHANG_NOTIFY);
		}
		SeatAttrib btSeat = table.getSeat(table.btIndex);
		
		if(currTime > table.waitTime){
			btSeat.btState = GameDefine.ACT_STATE_DROP;
		}else{
			if(btSeat.btState == GameDefine.ACT_STATE_WAIT){
				if(table.gameDataManager.getGameData(btSeat.accountId).robot == 1){
					if(btSeat.robotBtTime == 0){
						btSeat.robotBtTime = (long) (currTime + ((Math.random()*1000)%2+1)*1000);
					}
					if(currTime > btSeat.robotBtTime){
						int randVal = (int) ((Math.random()*100000)%10000);
						if(randVal > 5000){
							btSeat.btState = GameDefine.ACT_STATE_BT;
						}else{
							btSeat.btState = GameDefine.ACT_STATE_DROP;
						}
					}
				}
				return;
			}
		}		
		
		table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_CALLBANKER_NOTIFY);
		
		table.bExec = false;
		if(btSeat.btState == GameDefine.ACT_STATE_DROP){
			SeatAttrib nextSeat = table.getNextSeat(table.btIndex);
			if(nextSeat.seatIndex == table.bankerIndex){
				//轮了一圈了,确定庄家,进入下注状态
				table.gameState = GameDefine.STATE_TABLE_NN_WAIT_EX;
				table.nextState = GameDefine.STATE_TABLE_NN_BET;
			}else{
				//继续叫庄家
				table.btIndex = nextSeat.seatIndex;
				table.gameState = GameDefine.STATE_TABLE_NN_WAIT_EX;
				table.nextState = GameDefine.STATE_TABLE_NN_CALL_BANKER;				
			}
		}else if(btSeat.btState == GameDefine.ACT_STATE_BT){
			//确定庄家,进入下注状态
			table.bankerIndex = table.btIndex;
			SeatAttrib nextSeat = table.getNextSeat(table.bankerIndex);
			table.btIndex = nextSeat.seatIndex;
			table.gameState = GameDefine.STATE_TABLE_NN_WAIT_EX;
			table.nextState = GameDefine.STATE_TABLE_NN_BET;
		}
	}
	
	private void run_bet(TableAttrib table, long currTime){
		if(table.bExec == false){
			table.bExec = true;
			table.waitTime = currTime + GameDefine.TIME_WAIT_NN_BET;
			table.resetBtState();
			table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_STATE_CHANG_NOTIFY);
		}
		SeatAttrib btSeat = table.getSeat(table.btIndex);
		if(btSeat.btState == GameDefine.ACT_STATE_BT){
			//已下注进入发牌结算
			table.bExec = false;
			table.gameState = GameDefine.STATE_TABLE_NN_WAIT_EX;
			table.nextState = GameDefine.STATE_TABLE_NN_OVER;
		}else{
			if(table.gameDataManager.getGameData(btSeat.accountId).robot == 1){
				if(btSeat.robotBtTime == 0){
					btSeat.robotBtTime = (long) (currTime + ((Math.random()*1000)%2+1)*1000);
				}
				if(currTime > btSeat.robotBtTime){
					int randVal = (int) ((Math.random()*1000)%6 + 1);
					long myMoney = table.walletManager.getRoomCard(btSeat.accountId);
					SeatAttrib otherSeat = table.getNextSeat(btSeat.seatIndex);
					long otherMoney = table.walletManager.getRoomCard(otherSeat.accountId);
					long myBetMoney = myMoney;
					if(otherMoney < myBetMoney){
						myBetMoney = otherMoney;
					}
					myBetMoney = (randVal*myBetMoney)/36;
					if(myBetMoney < 1){
						myBetMoney = 1;
					}
					btSeat.btState = GameDefine.ACT_STATE_BT;
					table.walletManager.addRoomCard(btSeat.accountId, (0-myBetMoney), false);
					table.playerBetMoney(btSeat.accountId, (int) myBetMoney);
					//发送下注通知
					table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, table.btIndex, (int) myBetMoney);
				}
			}else{
				if(currTime > table.waitTime){
					//超时了,自动下最小的注
					long myMoney = table.walletManager.getRoomCard(btSeat.accountId);
					SeatAttrib otherSeat = table.getNextSeat(btSeat.seatIndex);
					long otherMoney = table.walletManager.getRoomCard(otherSeat.accountId);
					long myBetMoney = myMoney;
					if(otherMoney < myBetMoney){
						myBetMoney = otherMoney;
					}
					myBetMoney = myBetMoney/36;
					if(myBetMoney < 1){
						myBetMoney = 1;
					}
					btSeat.btState = GameDefine.ACT_STATE_BT;
					table.walletManager.addRoomCard(btSeat.accountId, (0-myBetMoney), false);
					table.playerBetMoney(btSeat.accountId, (int) myBetMoney);
					//发送下注通知
					table.sendBtNotify(ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY, table.btIndex, (int) myBetMoney);
				}
			}
		}
	}
	
	private void run_over(TableAttrib table, long currTime){
		if(table.bExec == false){
			int nextBanker = table.bankerIndex;
			table.bExec = true;
			table.waitTime = currTime + GameDefine.TIME_WAIT_NN_OVER;
			//牛牛无牛牌
			List<List<Byte>> tmpNoHaveNNList = new ArrayList<>();
			//牛牛有牛牌
			List<List<Byte>> tmpHaveNNList = new ArrayList<>();
			tmpHaveNNList.addAll(GameDefine.haveNNList);
			tmpNoHaveNNList.addAll(GameDefine.noHaveNNList);
			
			List<Byte> handCard1 = new ArrayList<>();
			List<Byte> handCard2 = new ArrayList<>();
			//有牛的概率85%,无牛概率15%
			int rate = 850;
			int ranVal = (int) ((Math.random()*1000)%1000);
			if(ranVal < rate){
				ranVal = (int) ((Math.random()*10000000)%tmpHaveNNList.size());
				handCard1.addAll(tmpHaveNNList.get(ranVal));
				tmpHaveNNList.remove(ranVal);
			}else{
				ranVal = (int) ((Math.random()*10000000)%tmpNoHaveNNList.size());
				handCard1.addAll(tmpNoHaveNNList.get(ranVal));
				tmpNoHaveNNList.remove(ranVal);
			}
			ranVal = (int) ((Math.random()*1000)%1000);
			if(ranVal < rate){
				ranVal = (int) ((Math.random()*10000000)%tmpHaveNNList.size());
				handCard2.addAll(tmpHaveNNList.get(ranVal));
				tmpHaveNNList.remove(ranVal);
			}else{
				ranVal = (int) ((Math.random()*10000000)%tmpNoHaveNNList.size());
				handCard2.addAll(tmpNoHaveNNList.get(ranVal));
				tmpNoHaveNNList.remove(ranVal);
			}
			
			SeatAttrib bankerSeat = table.getSeat(table.bankerIndex);
			SeatAttrib unBankerSeat = table.getNextSeat(table.bankerIndex);
			GameDataAttrib bankerSeatDataAttrib = table.gameDataManager.getGameData(bankerSeat.accountId);
			GameDataAttrib unBankerSeatDataAttrib = table.gameDataManager.getGameData(unBankerSeat.accountId);
			boolean bRecordWinMoney = false;
			if((bankerSeatDataAttrib.robot==1 && unBankerSeatDataAttrib.robot == 0)
					 || (bankerSeatDataAttrib.robot == 0 && unBankerSeatDataAttrib.robot == 1)){
				AnalysisResult bankerResult = GameDefine.AnalysisNNCards(handCard1);
				AnalysisResult unBankerResult = GameDefine.AnalysisNNCards(handCard2);
				
				long bankerMoney = table.walletManager.getRoomCard(bankerSeat.accountId);
				long unBankerMoney = table.walletManager.getRoomCard(unBankerSeat.accountId);
				long minMoney = bankerMoney;
				if(minMoney > unBankerMoney){
					minMoney = unBankerMoney;
				}
				long unBankerBetMoney = table.getPlayerBetMoney(unBankerSeat.accountId);
				int X = (int) (unBankerBetMoney/minMoney);
				int Y = 0;
				int result = this.compareCardsPoint(bankerResult, unBankerResult);
				if(result >= 0){
					Y = this.getNNRate(bankerResult);
				}else{
					Y = this.getNNRate(unBankerResult);
				}
				//一个机器人一个真实玩家				
				bRecordWinMoney = true;
				int robotWinRate = profitPoolManager.getWinRate(X, Y);
				int randVal = (int) (Math.random()*100);
				if(randVal <= robotWinRate){
					//机器人要赢
					if(result >= 0){
						//第一副牌比第二副牌大
						if(bankerSeatDataAttrib.robot == 1){
							bankerSeat.handCards.addAll(handCard1);
							unBankerSeat.handCards.addAll(handCard2);
						}else{
							bankerSeat.handCards.addAll(handCard2);
							unBankerSeat.handCards.addAll(handCard1);
						}
					}else{
						//第一副牌比第二副牌小
						if(bankerSeatDataAttrib.robot == 1){
							bankerSeat.handCards.addAll(handCard2);
							unBankerSeat.handCards.addAll(handCard1);
						}else{
							bankerSeat.handCards.addAll(handCard1);
							unBankerSeat.handCards.addAll(handCard2);
						}
					}
				}else{
					//机器人要输
					if(result >= 0){
						//第一副牌比第二副牌大
						if(bankerSeatDataAttrib.robot == 1){
							bankerSeat.handCards.addAll(handCard2);
							unBankerSeat.handCards.addAll(handCard1);
						}else{
							bankerSeat.handCards.addAll(handCard1);
							unBankerSeat.handCards.addAll(handCard2);
						}
					}else{
						//第一副牌比第二副牌小
						if(bankerSeatDataAttrib.robot == 1){
							bankerSeat.handCards.addAll(handCard1);
							unBankerSeat.handCards.addAll(handCard2);
						}else{
							bankerSeat.handCards.addAll(handCard2);
							unBankerSeat.handCards.addAll(handCard1);
						}
					}
				}
			}else{
				if(bankerSeatDataAttrib.robot == 0 && unBankerSeatDataAttrib.robot == 0){
					//两个真实玩家
					bRecordWinMoney = true;
				}
				bankerSeat.handCards.addAll(handCard1);
				unBankerSeat.handCards.addAll(handCard2);
			}
			
			int charge = table.cfgManager.getCfg(table.cfgId).getCharge();
			AnalysisResult bankerResult = GameDefine.AnalysisNNCards(bankerSeat.handCards);
			AnalysisResult unBankerResult = GameDefine.AnalysisNNCards(unBankerSeat.handCards);
			int result = this.compareCardsPoint(bankerResult, unBankerResult);
			if(result >= 0){
				//庄家赢,闲家输
				nextBanker = bankerSeat.seatIndex;
				rate = this.getNNRate(bankerResult);
				long betMoney = table.getPlayerBetMoney(unBankerSeat.accountId);
				long totalLost = betMoney*rate;
				long needLost = totalLost - betMoney;
				if(needLost > 0){
					table.walletManager.addRoomCard(unBankerSeat.accountId, 0-needLost, true);
				}
				if(bRecordWinMoney && unBankerSeatDataAttrib.robot==1){
					profitPoolManager.saveRobotWinMoney(0-totalLost);
					table.logicManager.debug("机器人是闲家本局总输="+(0-totalLost) + ",倍率="+rate);
				}
				long chargeWin = ((totalLost*charge)/100);
				long win = totalLost-chargeWin;
				if(win > 0){
					table.walletManager.addRoomCard(bankerSeat.accountId, win, true);
				}
				if(bRecordWinMoney && bankerSeatDataAttrib.robot==1){
					profitPoolManager.saveRobotWinMoney(win);
					table.logicManager.debug("机器人是庄家本局总赢="+win + ",倍率="+rate);
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
				
				String nick = "";
				String nnDesc = "";
				String starNO = "";
				AccountEntity accountEntity = table.accountManager.load(bankerSeat.accountId);
				if(null != accountEntity){
					nick = accountEntity.getNick();
					starNO = accountEntity.getStarNO();
				}
				nnDesc = this.getNNDesc(bankerResult);			
				table.settlement.addNNItem(String.valueOf(bankerSeat.accountId), 1, nick, nnDesc, win);
				EventAttrib eventAttrib = null;
				if(bankerSeatDataAttrib.robot == 0){
					eventAttrib = new EventAttrib(EventDefine.EVENT_GAME);
					eventAttrib.addEventParam(starNO);
					eventAttrib.addEventParam(String.valueOf(currTime));
					eventAttrib.addEventParam(win);
					eventAttrib.addEventParam(table.gameType);
					this.eventTriggerManager.triggerEvent(eventAttrib);
				}
				
				accountEntity = table.accountManager.load(unBankerSeat.accountId);
				if(null != accountEntity){
					nick = accountEntity.getNick();
					starNO = accountEntity.getStarNO();
				}
				
				nnDesc = this.getNNDesc(unBankerResult);
				table.settlement.addNNItem(String.valueOf(unBankerSeat.accountId), 0, nick, nnDesc, 0-totalLost);
				if(unBankerSeatDataAttrib.robot == 0){
					eventAttrib = new EventAttrib(EventDefine.EVENT_GAME);
					eventAttrib.addEventParam(starNO);
					eventAttrib.addEventParam(String.valueOf(currTime));
					eventAttrib.addEventParam(0-totalLost);
					eventAttrib.addEventParam(table.gameType);
					this.eventTriggerManager.triggerEvent(eventAttrib);
				}
			}else{
				//闲家赢,庄家输
				nextBanker = unBankerSeat.seatIndex;
				rate = this.getNNRate(unBankerResult);
				long betMoney = table.getPlayerBetMoney(unBankerSeat.accountId);
				long totalWin = betMoney*rate;
				long bankerMoney = table.walletManager.getRoomCard(bankerSeat.accountId);
				if(bankerMoney < totalWin){
					totalWin = bankerMoney;
				}
				table.walletManager.addRoomCard(bankerSeat.accountId, 0-totalWin, true);
				if(bRecordWinMoney && bankerSeatDataAttrib.robot==1){
					profitPoolManager.saveRobotWinMoney(0-totalWin);
					table.logicManager.debug("机器人是庄家本局总输="+(0-totalWin) + ",倍率="+rate);
				}
				long chargeWin = ((totalWin*charge)/100);
				long win = totalWin-chargeWin;
				if(win > 0){
					table.walletManager.addRoomCard(unBankerSeat.accountId, win+betMoney, true);
				}
				if(bRecordWinMoney && unBankerSeatDataAttrib.robot==1){
					profitPoolManager.saveRobotWinMoney(totalWin);
					table.logicManager.debug("机器人是闲家本局总赢="+(totalWin) + ",倍率="+rate);
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
				
				String nick = "";
				String nnDesc = "";
				String starNO = "";
				AccountEntity accountEntity = table.accountManager.load(bankerSeat.accountId);
				if(null != accountEntity){
					nick = accountEntity.getNick();
					starNO = accountEntity.getStarNO();
				}
				nnDesc = this.getNNDesc(bankerResult);
				table.settlement.addNNItem(String.valueOf(bankerSeat.accountId), 1, nick, nnDesc, 0-totalWin);
				EventAttrib eventAttrib = null;
				if(bankerSeatDataAttrib.robot == 0){
					eventAttrib = new EventAttrib(EventDefine.EVENT_GAME);
					eventAttrib.addEventParam(starNO);
					eventAttrib.addEventParam(String.valueOf(currTime));
					eventAttrib.addEventParam(0-totalWin);
					eventAttrib.addEventParam(table.gameType);
					this.eventTriggerManager.triggerEvent(eventAttrib);
				}
				
				nick = "";
				nnDesc = "";
				starNO = "";
				accountEntity = table.accountManager.load(unBankerSeat.accountId);
				if(null != accountEntity){
					nick = accountEntity.getNick();
					starNO = accountEntity.getStarNO();
				}
				nnDesc = this.getNNDesc(unBankerResult);			
				table.settlement.addNNItem(String.valueOf(unBankerSeat.accountId), 0, nick, nnDesc, win);
				if(unBankerSeatDataAttrib.robot == 0){
					eventAttrib = new EventAttrib(EventDefine.EVENT_GAME);
					eventAttrib.addEventParam(starNO);
					eventAttrib.addEventParam(String.valueOf(currTime));
					eventAttrib.addEventParam(win);
					eventAttrib.addEventParam(table.gameType);
					this.eventTriggerManager.triggerEvent(eventAttrib);
				}
			}
			table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_STATE_CHANG_NOTIFY);
			table.reset();
			table.bankerIndex = nextBanker;
		}
		
		if(currTime < table.waitTime){
			return;
		}
		
		//等客户端结算展示完再踢人
		int joinLimit = table.cfgManager.getCfg(table.cfgId).getJoinLimit();
		for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
			SeatAttrib seat = table.getSeat(seatIndex);
			if(seat.accountId <= 0){
				continue;
			}
			GameDataAttrib gameDateAttrib = table.gameDataManager.getGameData(seat.accountId);
			gameDateAttrib.onceWaitTime = (long) (currTime + ((Math.random()*10000)%20+10)*1000);
			if(gameDateAttrib.robot == 1){
				gameDateAttrib.swapTable = gameDateAttrib.swapTable-1;
			}
			
			long money = table.walletManager.getRoomCard(seat.accountId);
			if(money < joinLimit || seat.bLeave || (gameDateAttrib.robot==1 && gameDateAttrib.swapTable<=0)
					|| (gameDateAttrib.robot==0 && gameDateAttrib.onLine==0)){
				//推送被踢出座位消息
				long notifyId = seat.accountId;
				gameDateAttrib.tableId = 0;
				seat.accountId = 0;
				seat.bGamed = false;
				if(seat.bLeave){
					table.sendKickNotify(notifyId, 2);
				}else{
					table.sendKickNotify(notifyId, 1);
				}
				for(int seatIndex1=0; seatIndex1<table.seats.size(); seatIndex1++){
					SeatAttrib seat1 = table.getSeat(seatIndex1);
					if(seat1.accountId > 0){
						table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
						table.bankerIndex = seat1.seatIndex;
					}
				}
			}
		}
		table.gameState = GameDefine.STATE_TABLE_IDLE;
	}
	
	private void run_wait_ex(TableAttrib table, long currTime){
		if(table.bExec == false){
			table.bExec = true;
			table.waitTime = currTime + GameDefine.TIME_WAIT_NN_WAITEX;
		}
		if(currTime < table.waitTime){
			return;
		}
		table.bExec = false;
		table.gameState = table.nextState;
	}
		
	/**
	 * 比较两副牌牛点数相同,比较点数大小(先比组成整10数的三张,再比后两张)
	 * -1=1比2小,0=完全相等,1=1比2大
	 * */
	private int compareCardsPoint(AnalysisResult result1, AnalysisResult result2){		
		if(result1.nnNum > result2.nnNum){
			//手牌1牛大于手牌2牛
			return 1;
		}else if(result1.nnNum < result2.nnNum){
			//手牌1牛小于手牌2牛
			return -1;
		}else{
			//牛点数相同,需要比较牌点数大小
			List<Byte> tmpList1 = result1.cards.get(0);
			List<Byte> tmpList2 = result2.cards.get(0);
			this.sortCard(tmpList1);
			this.sortCard(tmpList2);				
			for(int cardIndex=0; cardIndex<tmpList2.size(); cardIndex++){
				int point1 = GameDefine.getCardPoint(tmpList1.get(cardIndex));
				int point2 = GameDefine.getCardPoint(tmpList2.get(cardIndex));
				if(point1 > point2){
					return 1;
				}else if(point1 < point2){
					return -1;
				}
			}
			//前面3张整数牌都相同,比较牛点数牌
			tmpList1 = result1.cards.get(1);
			tmpList2 = result2.cards.get(1);
			this.sortCard(tmpList1);
			this.sortCard(tmpList2);
			for(int cardIndex=0; cardIndex<tmpList1.size(); cardIndex++){
				int point1 = GameDefine.getCardPoint(tmpList1.get(cardIndex));
				int point2 = GameDefine.getCardPoint(tmpList2.get(cardIndex));
				if(point1 > point2){
					return 1;
				}else if(point1 < point2){
					return -1;
				}
			}
		}		
		return 0;
	}
	
	//牌按点数大小排序
	private void sortCard(List<Byte> cards){
		cards.sort(new Comparator<Byte>(){
			@Override
			public int compare(Byte arg0, Byte arg1) {
				int point1 = GameDefine.getCardPoint(arg0);
				if(point1 == 14){
					point1 = 1;
				}
				int point2 = GameDefine.getCardPoint(arg1);
				if(point2 == 14){
					point2 = 1;
				}
				if(point1 > point2){
					return -1;
				}else if(point1 < point2){
					return 1;
				}else{
					return 0;
				}
			}
		});
	}
	
	//获取牛牛点数倍率
	private int getNNRate(AnalysisResult result){
		if(result.b5FlowerNN){
			return 6;
		}
		int rate = 1;
		switch(result.nnNum){
		case 7:
			rate = 2;
			break;
		case 8:
			rate = 3;
			break;
		case 9:
			rate = 4;
			break;
		case 10:
			rate = 5;
			break;
		}
		
		return rate;
	}
	
	private String getNNDesc(AnalysisResult analysisResult){
		if(analysisResult.b5FlowerNN){
			return "五花牛牛";
		}
		String retStr = "";
		switch(analysisResult.nnNum){
		case 0:
			retStr = "没牛";
			break;
		case 1:
			retStr = "牛一";
			break;
		case 2:
			retStr = "牛二";
			break;
		case 3:
			retStr = "牛三";
			break;
		case 4:
			retStr = "牛四";
			break;
		case 5:
			retStr = "牛五";
			break;
		case 6:
			retStr = "牛六";
			break;
		case 7:
			retStr = "牛七";
			break;
		case 8:
			retStr = "牛八";
			break;
		case 9:
			retStr = "牛九";
			break;
		case 10:
			retStr = "牛牛";
			break;
		default:
			retStr = "没牛";
			break;
		}

		return retStr;
	}
}
