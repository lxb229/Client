package com.palmjoys.yf1b.act.majiang.model;

import java.util.ArrayList;
import java.util.List;
import com.palmjoys.yf1b.act.majiang.manager.CardAnalysisManager;

public class TableOnceOverStateManager {
	public static void run_over_once(long currTime, TableAttrib table){
		switch(table.cfgId){
		case 7://乐山麻将
			TableOnceOverStateManager.run_over_once_lsmj(currTime, table);
			break;
		case 8://南充麻将
			TableOnceOverStateManager.run_over_once_ncmj(currTime, table);
			break;
		default:
			TableOnceOverStateManager.run_over_once_other(currTime, table);
			break;
		}
	}
	
	private static void run_over_once_other(long currTime, TableAttrib table){
		if(table.bActExec == false){
			table.bActExec = true;
			table.waitTime = currTime + GameDefine.TIME_TABLE_OVER_ONCE;
			int nextBanker = -1;
			//没有下叫的座位
			List<SeatAttrib> unTingPaiSeats = new ArrayList<>();
			//下了叫未胡牌的座位
			List<SeatAttrib> tingPaiSeats = new ArrayList<>();
			
			table.logicManager.log("一局游戏结束");
			for(int seatIndex1=0; seatIndex1<table.seats.size(); seatIndex1++){
				SeatAttrib mySeat = table.seats.get(seatIndex1);
				mySeat.statistAttrib.anGangNum = mySeat.anGangCards.size();
				mySeat.statistAttrib.dianGangNum = mySeat.dianGangCards.size();
				mySeat.statistAttrib.baGangNum = mySeat.baGangCards.size();				
				
				if(mySeat.huPaiType == GameDefine.HUPAI_TYPE_NONE){
					//没有胡牌的人
					List<CardAttrib> tingPais = GameDefine.isTingPai(table, mySeat.handCards, mySeat);
					if(null == tingPais || tingPais.isEmpty()){
						//没有叫
						unTingPaiSeats.add(mySeat);
					}else{						
						//有叫
						tingPaiSeats.add(mySeat);
						//找到最大番数可胡牌牌形
						CardAttrib maxRateCard = null;
						int paiStyleRate = 0;
						if(table.ruleAttrib.chaDaJiao > 0){
							//查大叫
							paiStyleRate = -1;
						}else{
							//查小叫
							paiStyleRate = 10000;
						}
						
						int []huPaiStyle = new int[GameDefine.HUPAI_STYLE_MAX_END];
						for(CardAttrib canHu : tingPais){
							CardAnalysisManager.analysisHuPaiStyle(table, mySeat, canHu);
							//查叫不算海底捞和海底炮,天地胡
							mySeat.huPaiStyle[GameDefine.HUPAI_STYLE_HAIDILAO] = 0;
							mySeat.huPaiStyle[GameDefine.HUPAI_STYLE_HAIDILAO_DIANPAO] = 0;
							mySeat.huPaiStyle[GameDefine.HUPAI_STYLE_TIANHU] = 0;
							mySeat.huPaiStyle[GameDefine.HUPAI_STYLE_DIEHU] = 0;
							
							int tmpRate = CardAnalysisManager.getHuPaiStyleRate(table, mySeat.huPaiStyle);
							if(table.ruleAttrib.chaDaJiao > 0){
								//查大叫
								if(tmpRate > paiStyleRate){
									paiStyleRate = tmpRate;
									maxRateCard = canHu;
									System.arraycopy(mySeat.huPaiStyle, 0, huPaiStyle, 0, GameDefine.HUPAI_STYLE_MAX_END);
								}
							}else{
								//查小叫
								if(tmpRate < paiStyleRate){
									paiStyleRate = tmpRate;
									maxRateCard = canHu;
									System.arraycopy(mySeat.huPaiStyle, 0, huPaiStyle, 0, GameDefine.HUPAI_STYLE_MAX_END);
								}
							}
						}
						
						System.arraycopy(huPaiStyle, 0, mySeat.huPaiStyle, 0, GameDefine.HUPAI_STYLE_MAX_END);						
						
						mySeat.huCards.add(maxRateCard);
						List<CardAttrib> tmpCards = new ArrayList<>();
						tmpCards.addAll(mySeat.handCards);
						tmpCards.add(maxRateCard);
						List<List<CardAttrib>> four = GameDefine.findFourCards(tmpCards);						
						mySeat.statistAttrib.daiGengNum = four.size();
						for(List<CardAttrib> cards : mySeat.pengCards){
							for(CardAttrib tmpCard : cards){
								if(tmpCard.suit == maxRateCard.suit
										&& tmpCard.point == maxRateCard.point){
									mySeat.statistAttrib.daiGengNum++;
									break;
								}
							}
						}
						
						tmpCards = null;
						mySeat.huCards.clear();
						mySeat.statistAttrib.tingPaiRate = paiStyleRate;
						four = null;
					}
				}else{
					//胡了牌的座位
					//计算下一局庄家(第一个胡牌的人)
					if(mySeat.huPaiIndex == 1){
						if(nextBanker == -1){
							nextBanker = mySeat.seatIndex;
						}else{
							//点了双炮或三炮的玩家
							nextBanker = mySeat.huPaiWinList.get(0).seatIndex;
						}
					}					
					//计算带根
					List<CardAttrib> tmpCards = new ArrayList<>();
					tmpCards.addAll(mySeat.handCards);
					tmpCards.addAll(mySeat.huCards);
					
					for(List<CardAttrib> pengCardList : mySeat.pengCards){
						tmpCards.addAll(pengCardList);
					}
					
					List<List<CardAttrib>> four = GameDefine.findFourCards(tmpCards);
					mySeat.statistAttrib.daiGengNum = four.size();
					
					mySeat.statistAttrib.huPaiRate = CardAnalysisManager.getHuPaiStyleRate(table, mySeat.huPaiStyle);
					mySeat.statistAttrib.huPaiRate += mySeat.statistAttrib.anGangNum;
					mySeat.statistAttrib.huPaiRate += mySeat.statistAttrib.dianGangNum;
					mySeat.statistAttrib.huPaiRate += mySeat.statistAttrib.baGangNum;
					mySeat.statistAttrib.huPaiRate += mySeat.statistAttrib.daiGengNum;
					int huPaiTypeRate = CardAnalysisManager.getHuPaiTypeRate(table, mySeat.huPaiType);
					mySeat.statistAttrib.huPaiRate += huPaiTypeRate;
					
					int tangNum = 0;
					int baojiaoNum = 0;
					if(mySeat.tangCardState > 0){
						//自已有躺加番
						mySeat.statistAttrib.huPaiRate += 1;
						tangNum = 1;
					}
					if(mySeat.baoJiaoState > 0){
						//自已报了叫加番
						mySeat.statistAttrib.huPaiRate += 1;
						baojiaoNum = 1;
					}
					
					//计算有输赢详细记录数据
					for(SeatHuPaiWinAttrib seatHuPaiWinAttrib : mySeat.huPaiWinList){
						Integer lostSeatIndex = seatHuPaiWinAttrib.seatIndex;
						SeatAttrib lostSeat = table.seats.get(lostSeatIndex);						
						int huPaiFanShu = mySeat.statistAttrib.huPaiRate;
						
						int tmpTangNum = tangNum;
						int tmpBaoJiaoNum = baojiaoNum;
						if(seatHuPaiWinAttrib.tangCardState > 0){
							//被胡座位有躺加番
							huPaiFanShu += 1;
							tmpTangNum += 1;
						}
						if(lostSeat.baoJiaoState > 0){
							//被胡座位也报了叫加番
							huPaiFanShu += 1;
							tmpBaoJiaoNum += 1;
						}
						
						if(huPaiFanShu >= table.ruleAttrib.maxRate){
							huPaiFanShu = table.ruleAttrib.maxRate;
						}
						int rateScore = GameDefine.calculationRateScore(table.ruleAttrib.baseScore, huPaiFanShu);
						if(mySeat.huPaiType == GameDefine.HUPAI_TYPE_ZIMO
								|| mySeat.huPaiType == GameDefine.HUPAI_TYPE_GANGFLOW
								|| mySeat.huPaiType == GameDefine.HUPAI_TYPE_DIANGANGFLOW){
							//自摸类型,检查加底还是加番
							if(table.ruleAttrib.bZiMoAddBase){
								rateScore += table.ruleAttrib.baseScore;
							}
						}
						
						mySeat.statistAttrib.onceScore += rateScore;
						lostSeat.statistAttrib.onceScore -= rateScore;
																		
						//记录赢的玩家
						SettementSeatScore settementSeatScore = mySeat.seatScore.get(lostSeat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score + rateScore;
						settementSeatScore.totalFanNum = huPaiFanShu;
						settementSeatScore.tangNum = tmpTangNum;
						settementSeatScore.baoJiaoNum = tmpBaoJiaoNum;
						mySeat.seatScore.put(lostSeat.accountId, settementSeatScore);
						
						//记录输的玩家
						settementSeatScore = lostSeat.seatScore.get(mySeat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score - rateScore;
						settementSeatScore.totalFanNum = huPaiFanShu;
						settementSeatScore.tangNum = tmpTangNum;
						settementSeatScore.baoJiaoNum = tmpBaoJiaoNum;
						
						lostSeat.seatScore.put(mySeat.accountId, settementSeatScore);						
						
						switch(mySeat.huPaiType){
						case GameDefine.HUPAI_TYPE_DIANPAO:
						case GameDefine.HUPAI_TYPE_QIANGGANG:
						case GameDefine.HUPAI_TYPE_DIANGANGFLOW:
						case GameDefine.HUPAI_TYPE_DIANGANGPAO:
							lostSeat.statistAttrib.outDesc += "点" + mySeat.huPaiIndex + "胡 ";
							break;
						case GameDefine.HUPAI_TYPE_ZIMO:
						case GameDefine.HUPAI_TYPE_GANGFLOW:
							lostSeat.statistAttrib.outDesc += "被" + mySeat.huPaiIndex + "胡自摸 ";
							break;
						}
					}
					
					//计算无输赢关系的座位
					for(int seatIndex2=0; seatIndex2<table.seats.size(); seatIndex2++){
						if(seatIndex2 == seatIndex1){
							//是自已跳过
							continue;
						}
						SeatAttrib lostSeat = table.seats.get(seatIndex2);
						
						boolean bHave = false;
						for(SeatHuPaiWinAttrib seatHuPaiWinAttrib : mySeat.huPaiWinList){
							if(seatIndex2 == seatHuPaiWinAttrib.seatIndex){
								bHave = true;
								break;
							}
						}
						if(bHave){
							//玩家2在玩家1胡牌列表中跳过
							continue;
						}
						
						bHave = false;
						for(SeatHuPaiWinAttrib seatHuPaiWinAttrib : lostSeat.huPaiWinList){
							if(seatIndex1 == seatHuPaiWinAttrib.seatIndex){
								bHave = true;
								break;
							}
						}
						if(bHave){
							//玩家1在玩家2胡牌列表中跳过
							continue;
						}
						
						int tmpTangNum = 0;
						int tmpBaoJiaoNum = 0; 
						if(mySeat.tangCardState > 0){
							tmpTangNum += 1;
						}
						if(mySeat.baoJiaoState > 0){
							tmpBaoJiaoNum += 1;
						}
						
						if(lostSeat.tangCardState > 0){
							tmpTangNum += 1;
						}
						
						if(lostSeat.baoJiaoState > 0){
							tmpBaoJiaoNum += 1;
						}
						
						//记录赢的玩家
						SettementSeatScore settementSeatScore = mySeat.seatScore.get(lostSeat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score + 0;
						settementSeatScore.totalFanNum = 0;
						settementSeatScore.tangNum = tmpTangNum;
						settementSeatScore.baoJiaoNum = tmpBaoJiaoNum;
						settementSeatScore.winPiaoNum = settementSeatScore.winPiaoNum + 0;
						mySeat.seatScore.put(lostSeat.accountId, settementSeatScore);
						
						//记录输的玩家
						settementSeatScore = lostSeat.seatScore.get(mySeat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score - 0;
						settementSeatScore.totalFanNum = 0;
						settementSeatScore.tangNum = tmpTangNum;
						settementSeatScore.baoJiaoNum = tmpBaoJiaoNum;
						settementSeatScore.winPiaoNum = settementSeatScore.winPiaoNum - 0;
						
						lostSeat.seatScore.put(mySeat.accountId, settementSeatScore);
					}
				}
			}
			
			if(table.tableCards.isEmpty() && (unTingPaiSeats.size()+tingPaiSeats.size()) > 1){
				//流局了才检查查叫
				if(unTingPaiSeats.isEmpty() == false && tingPaiSeats.isEmpty() == false){
					//未下叫的人数大于0且下叫的人数大于0,需要查叫
					for(SeatAttrib tingPaiSeat : tingPaiSeats){
						int tangNum = 0;
						int baojiaoNum = 0;
						tingPaiSeat.statistAttrib.totalChaJiao++;
						tingPaiSeat.huPaiType = GameDefine.HUPAI_TYPE_CHAJIAO;
						tingPaiSeat.statistAttrib.huPaiRate += tingPaiSeat.statistAttrib.tingPaiRate;
						tingPaiSeat.statistAttrib.huPaiRate += tingPaiSeat.statistAttrib.anGangNum;
						tingPaiSeat.statistAttrib.huPaiRate += tingPaiSeat.statistAttrib.baGangNum;
						tingPaiSeat.statistAttrib.huPaiRate += tingPaiSeat.statistAttrib.dianGangNum;
						tingPaiSeat.statistAttrib.huPaiRate += tingPaiSeat.statistAttrib.daiGengNum;
						if(tingPaiSeat.tangCardState > 0){
							//自已有躺加番
							tingPaiSeat.statistAttrib.huPaiRate += 1;
							tangNum += 1;
						}
						if(tingPaiSeat.baoJiaoState > 0){
							//报了叫的加番
							tingPaiSeat.statistAttrib.huPaiRate += 1;
							baojiaoNum += 1;
						}
						
						if(tingPaiSeat.statistAttrib.huPaiRate >= table.ruleAttrib.maxRate){
							tingPaiSeat.statistAttrib.huPaiRate = table.ruleAttrib.maxRate;
						}
						int rateScore = GameDefine.calculationRateScore(table.ruleAttrib.baseScore, tingPaiSeat.statistAttrib.huPaiRate);
						for(SeatAttrib unTingSeat : unTingPaiSeats){
							//未下叫的玩家不算杠钱
							unTingSeat.huPaiWinList.clear();
							unTingSeat.anGangWinList.clear();
							unTingSeat.baGangWinList.clear();
							unTingSeat.dianGangWinList.clear();
							unTingSeat.zhuanYuSeats.clear();
							unTingSeat.dianGangJiaJiaYouWinList.clear();
													
							tingPaiSeat.statistAttrib.onceScore += rateScore;
							unTingSeat.statistAttrib.onceScore -= rateScore;
							unTingSeat.statistAttrib.outDesc += "被查叫";
							
							//记录赢的玩家
							SettementSeatScore settementSeatScore = tingPaiSeat.seatScore.get(unTingSeat.accountId);
							if(null == settementSeatScore){
								settementSeatScore = new SettementSeatScore();
							}
							settementSeatScore.score = settementSeatScore.score + rateScore;
							settementSeatScore.totalFanNum = tingPaiSeat.statistAttrib.huPaiRate;
							settementSeatScore.tangNum = tangNum;
							settementSeatScore.baoJiaoNum = baojiaoNum;
							tingPaiSeat.seatScore.put(unTingSeat.accountId, settementSeatScore);
							
							//记录输的玩家
							settementSeatScore = unTingSeat.seatScore.get(tingPaiSeat.accountId);
							if(null == settementSeatScore){
								settementSeatScore = new SettementSeatScore();
							}
							settementSeatScore.score = settementSeatScore.score - rateScore;
							settementSeatScore.totalFanNum = tingPaiSeat.statistAttrib.huPaiRate;
							settementSeatScore.tangNum = tangNum;
							settementSeatScore.baoJiaoNum = baojiaoNum;
							unTingSeat.seatScore.put(tingPaiSeat.accountId, settementSeatScore);
						}
					}
				}else{
					if(unTingPaiSeats.size()>1 && tingPaiSeats.isEmpty()){
						//大家都没叫,未下叫的玩家不算杠钱
						for(SeatAttrib unTingSeat : unTingPaiSeats){
							unTingSeat.huPaiWinList.clear();
							unTingSeat.anGangWinList.clear();
							unTingSeat.baGangWinList.clear();
							unTingSeat.dianGangWinList.clear();
							unTingSeat.zhuanYuSeats.clear();
							unTingSeat.dianGangJiaJiaYouWinList.clear();
						}
					}else{
						if(unTingPaiSeats.isEmpty() && tingPaiSeats.isEmpty() == false){
							//大家都有叫
							for(int tmpSeatIndex2=0; tmpSeatIndex2<tingPaiSeats.size(); tmpSeatIndex2++){
								SeatAttrib tingPaiSeat = tingPaiSeats.get(tmpSeatIndex2);
								tingPaiSeat.huPaiType = GameDefine.HUPAI_TYPE_TINGPAI;
								for(int tmpSeatIndex3=0; tmpSeatIndex3<tingPaiSeats.size(); tmpSeatIndex3++){
									if(tmpSeatIndex2 == tmpSeatIndex3){
										continue;
									}
									SeatAttrib lostSeat = tingPaiSeats.get(tmpSeatIndex3);
									
									int tangNum = 0;
									int baojiaoNum = 0;
									if(tingPaiSeat.tangCardState > 0){
										tangNum += 1;
									}
									if(tingPaiSeat.baoJiaoState > 0){
										baojiaoNum += 1;
									}
									
									if(lostSeat.tangCardState > 0){
										tangNum += 1;
									}
									
									if(lostSeat.baoJiaoState > 0){
										baojiaoNum += 1;
									}
									//记录赢的玩家
									SettementSeatScore settementSeatScore = tingPaiSeat.seatScore.get(lostSeat.accountId);
									if(null == settementSeatScore){
										settementSeatScore = new SettementSeatScore();
									}
									settementSeatScore.score = settementSeatScore.score + 0;
									settementSeatScore.totalFanNum = 0;
									settementSeatScore.tangNum = tangNum;
									settementSeatScore.baoJiaoNum = baojiaoNum;
									tingPaiSeat.seatScore.put(lostSeat.accountId, settementSeatScore);
									
									//记录输的玩家
									settementSeatScore = lostSeat.seatScore.get(tingPaiSeat.accountId);
									if(null == settementSeatScore){
										settementSeatScore = new SettementSeatScore();
									}
									settementSeatScore.score = settementSeatScore.score - 0;
									settementSeatScore.totalFanNum = 0;
									settementSeatScore.tangNum = tangNum;
									settementSeatScore.baoJiaoNum = baojiaoNum;
									
									lostSeat.seatScore.put(tingPaiSeat.accountId, settementSeatScore);
								}
							}
						}
					}
				}
			}
			//计算杠钱
			for(SeatAttrib seat : table.seats){	
				//计算转雨
				if(table.ruleAttrib.bZhuanYu){
					for(SeatHuJiaoZhuYiAttrib seatHuJiaoZhuYiAttrib : seat.zhuanYuSeats){
						SeatAttrib winSeat = table.seats.get(seatHuJiaoZhuYiAttrib.seatIndex);
						
						winSeat.statistAttrib.onceScore += seatHuJiaoZhuYiAttrib.score;
						seat.statistAttrib.onceScore -= seatHuJiaoZhuYiAttrib.score;
						
						//记录赢的玩家
						SettementSeatScore settementSeatScore = winSeat.seatScore.get(seat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score + seatHuJiaoZhuYiAttrib.score;
						seat.seatScore.put(seat.accountId, settementSeatScore);
						
						//记录输的玩家
						settementSeatScore = seat.seatScore.get(winSeat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score - seatHuJiaoZhuYiAttrib.score;
						seat.seatScore.put(winSeat.accountId, settementSeatScore);
					}
				}
				
				//计算巴到烫
				if(table.ruleAttrib.bJiaJiaYou){
					for(List<Integer> jiajiayouList : seat.dianGangJiaJiaYouWinList){
						seat.statistAttrib.jiajiaYouNum += jiajiayouList.size();
						for(int lostSeatIndex : jiajiayouList){
							SeatAttrib lostSeat = table.seats.get(lostSeatIndex);
							seat.statistAttrib.onceScore += table.ruleAttrib.baseScore;
							lostSeat.statistAttrib.onceScore -= table.ruleAttrib.baseScore;
							lostSeat.statistAttrib.outJiajiaYouNum++;
							
							//记录赢的玩家
							SettementSeatScore settementSeatScore = seat.seatScore.get(lostSeat.accountId);
							if(null == settementSeatScore){
								settementSeatScore = new SettementSeatScore();
							}
							settementSeatScore.score = settementSeatScore.score + table.ruleAttrib.baseScore;
							seat.seatScore.put(lostSeat.accountId, settementSeatScore);
							
							//记录输的玩家
							settementSeatScore = lostSeat.seatScore.get(seat.accountId);
							if(null == settementSeatScore){
								settementSeatScore = new SettementSeatScore();
							}
							settementSeatScore.score = settementSeatScore.score - table.ruleAttrib.baseScore;
							lostSeat.seatScore.put(seat.accountId, settementSeatScore);
						}
					}
				}
				
				//暗杠
				for(List<Integer> angangWin : seat.anGangWinList){
					for(int theSeatIndex : angangWin){
						seat.statistAttrib.onceScore += table.ruleAttrib.baseScore*2;
						SeatAttrib lostSeat = table.seats.get(theSeatIndex);
						lostSeat.statistAttrib.onceScore -= table.ruleAttrib.baseScore*2;
						lostSeat.statistAttrib.outAnGang++;
						
						//记录赢的玩家
						SettementSeatScore settementSeatScore = seat.seatScore.get(lostSeat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score + table.ruleAttrib.baseScore*2;
						seat.seatScore.put(lostSeat.accountId, settementSeatScore);
						
						//记录输的玩家
						settementSeatScore = lostSeat.seatScore.get(seat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score - table.ruleAttrib.baseScore*2;
						lostSeat.seatScore.put(seat.accountId, settementSeatScore);
					}
				}
				//巴杠
				for(List<Integer> bagangWin : seat.baGangWinList){
					for(int theSeatIndex : bagangWin){
						seat.statistAttrib.onceScore += table.ruleAttrib.baseScore;
						SeatAttrib lostSeat = table.seats.get(theSeatIndex);
						lostSeat.statistAttrib.onceScore -= table.ruleAttrib.baseScore;
						lostSeat.statistAttrib.outBaGang++;						
						//记录赢的玩家
						SettementSeatScore settementSeatScore = seat.seatScore.get(lostSeat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score + table.ruleAttrib.baseScore;
						seat.seatScore.put(lostSeat.accountId, settementSeatScore);
						
						//记录输的玩家
						settementSeatScore = lostSeat.seatScore.get(seat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score - table.ruleAttrib.baseScore;
						lostSeat.seatScore.put(seat.accountId, settementSeatScore);
					}
				}
				//点杠
				for(int theSeatIndex : seat.dianGangWinList){
					seat.statistAttrib.onceScore += table.ruleAttrib.baseScore*2;
					SeatAttrib lostSeat = table.seats.get(theSeatIndex);
					lostSeat.statistAttrib.onceScore -= table.ruleAttrib.baseScore*2;
					lostSeat.statistAttrib.outDianGang++;
					
					//记录赢的玩家
					SettementSeatScore settementSeatScore = seat.seatScore.get(lostSeat.accountId);
					if(null == settementSeatScore){
						settementSeatScore = new SettementSeatScore();
					}
					settementSeatScore.score = settementSeatScore.score + table.ruleAttrib.baseScore*2;
					seat.seatScore.put(lostSeat.accountId, settementSeatScore);
					
					//记录输的玩家
					settementSeatScore = lostSeat.seatScore.get(seat.accountId);
					if(null == settementSeatScore){
						settementSeatScore = new SettementSeatScore();
					}
					settementSeatScore.score = settementSeatScore.score - table.ruleAttrib.baseScore*2;
					lostSeat.seatScore.put(seat.accountId, settementSeatScore);
				}
			}
			
			//记录单局战绩
			table.recordManager.recordOverOnce(table, currTime);
			//统计总输赢
			String sLog = "统计总输赢,";
			for(SeatAttrib seat : table.seats){
				sLog += "座位="+seat.seatIndex+"分数="+seat.statistAttrib.onceScore;
				seat.score += seat.statistAttrib.onceScore;
			}
			table.logicManager.log(sLog);
			
			table.bGameNext = true;
			if((table.currGameNum+1) > table.ruleAttrib.maxGameNum
					|| table.breakStateSource == GameDefine.STATE_TABLE_DESTORY
					|| currTime >= table.maxVaildTime){
				table.bGameNext = false;
			}
			
			//通知客户端结算
			table.sendGameStateNotify();
			table.currGameNum++;
			if(nextBanker != -1){
				//没有人胡牌,上把庄家继续当庄家
				table.bankerIndex = nextBanker;
			}
		}
		table.bSendAllOverNotify = false;
		table.bActExec = false;
		if(table.bGameNext == false){
			//所有局已打完,或者都同意解散,或者桌子最大有效时间到了强制结束
			table.gameState = GameDefine.STATE_TABLE_OVER_ALL;
		}else{
			table.gameState = GameDefine.STATE_TABLE_IDLE;
			table.tableReset();
		}
	}
	
	private static void run_over_once_ncmj(long currTime, TableAttrib table){
		if(table.bActExec == false){
			table.bActExec = true;
			table.waitTime = currTime + GameDefine.TIME_TABLE_OVER_ONCE;
			int nextBanker = -1;
			//没有下叫的座位
			List<SeatAttrib> unTingPaiSeats = new ArrayList<>();
			//下了叫未胡牌的座位
			List<SeatAttrib> tingPaiSeats = new ArrayList<>();
			
			table.logicManager.log("一局游戏结束");
			for(int seatIndex1=0; seatIndex1<table.seats.size(); seatIndex1++){
				SeatAttrib mySeat = table.seats.get(seatIndex1);
				mySeat.statistAttrib.anGangNum = mySeat.anGangCards.size();
				mySeat.statistAttrib.dianGangNum = mySeat.dianGangCards.size();
				mySeat.statistAttrib.baGangNum = mySeat.baGangCards.size();
				
				if(mySeat.huPaiType == GameDefine.HUPAI_TYPE_NONE){
					//没有胡牌的人
					List<CardAttrib> tingPais = GameDefine.isTingPai(table, mySeat.handCards, mySeat);
					if(null == tingPais || tingPais.isEmpty()){
						//没有叫
						unTingPaiSeats.add(mySeat);
					}else{						
						//有叫
						tingPaiSeats.add(mySeat);
						//找到最大番数可胡牌牌形
						CardAttrib maxRateCard = null;
						int paiStyleRate = 0;
						if(table.ruleAttrib.chaDaJiao > 0){
							//查大叫
							paiStyleRate = -1;
						}else{
							//查小叫
							paiStyleRate = 10000;
						}
						
						int []huPaiStyle = new int[GameDefine.HUPAI_STYLE_MAX_END];
						for(CardAttrib canHu : tingPais){
							CardAnalysisManager.analysisHuPaiStyle(table, mySeat, canHu);
							//查叫不算海底捞和海底炮,天地胡
							mySeat.huPaiStyle[GameDefine.HUPAI_STYLE_HAIDILAO] = 0;
							mySeat.huPaiStyle[GameDefine.HUPAI_STYLE_HAIDILAO_DIANPAO] = 0;
							mySeat.huPaiStyle[GameDefine.HUPAI_STYLE_TIANHU] = 0;
							mySeat.huPaiStyle[GameDefine.HUPAI_STYLE_DIEHU] = 0;
							int tmpRate = CardAnalysisManager.getHuPaiStyleRate(table, mySeat.huPaiStyle);
							if(table.ruleAttrib.chaDaJiao > 0){
								//查大叫
								if(tmpRate > paiStyleRate){
									paiStyleRate = tmpRate;
									maxRateCard = canHu;
									System.arraycopy(mySeat.huPaiStyle, 0, huPaiStyle, 0, GameDefine.HUPAI_STYLE_MAX_END);
								}
							}else{
								//查小叫
								if(tmpRate < paiStyleRate){
									paiStyleRate = tmpRate;
									maxRateCard = canHu;
									System.arraycopy(mySeat.huPaiStyle, 0, huPaiStyle, 0, GameDefine.HUPAI_STYLE_MAX_END);
								}
							}
						}
						System.arraycopy(huPaiStyle, 0, mySeat.huPaiStyle, 0, GameDefine.HUPAI_STYLE_MAX_END);
						
						mySeat.huCards.add(maxRateCard);
						List<CardAttrib> tmpCards = new ArrayList<>();
						tmpCards.addAll(mySeat.handCards);
						tmpCards.add(maxRateCard);
						List<List<CardAttrib>> four = GameDefine.findFourCards(tmpCards);						
						mySeat.statistAttrib.daiGengNum = four.size();
						for(List<CardAttrib> cards : mySeat.pengCards){
							for(CardAttrib tmpCard : cards){
								if(tmpCard.suit == maxRateCard.suit
										&& tmpCard.point == maxRateCard.point){
									mySeat.statistAttrib.daiGengNum++;
									break;
								}
							}
						}
						
						tmpCards = null;
						mySeat.huCards.clear();
						mySeat.statistAttrib.tingPaiRate = paiStyleRate;
						four = null;
					}
				}else{
					//胡了牌的座位
					//计算下一局庄家(第一个胡牌的人)
					if(mySeat.huPaiIndex == 1){
						if(nextBanker == -1){
							nextBanker = mySeat.seatIndex;
						}else{
							//点了双炮或三炮的玩家
							nextBanker = mySeat.huPaiWinList.get(0).seatIndex;
						}
					}					
					//计算带根
					List<CardAttrib> tmpCards = new ArrayList<>();
					tmpCards.addAll(mySeat.handCards);
					tmpCards.addAll(mySeat.huCards);
					
					for(List<CardAttrib> pengCardList : mySeat.pengCards){
						tmpCards.addAll(pengCardList);
					}
					
					List<List<CardAttrib>> four = GameDefine.findFourCards(tmpCards);
					mySeat.statistAttrib.daiGengNum = four.size();
					
					mySeat.statistAttrib.huPaiRate = CardAnalysisManager.getHuPaiStyleRate(table, mySeat.huPaiStyle);
					mySeat.statistAttrib.huPaiRate += mySeat.statistAttrib.anGangNum;
					mySeat.statistAttrib.huPaiRate += mySeat.statistAttrib.dianGangNum;
					mySeat.statistAttrib.huPaiRate += mySeat.statistAttrib.baGangNum;
					mySeat.statistAttrib.huPaiRate += mySeat.statistAttrib.daiGengNum;
					int huPaiTypeRate = CardAnalysisManager.getHuPaiTypeRate(table, mySeat.huPaiType);
					mySeat.statistAttrib.huPaiRate += huPaiTypeRate;
					
					int baiPaiNum = 0;
					if(mySeat.tangCardState > 0){
						baiPaiNum += 1;
						if(mySeat.huPaiStyle[GameDefine.HUPAI_STYLE_BAIDUZHANG]<=0){
							//自已摆了牌,不是摆的独张加2番
							mySeat.statistAttrib.huPaiRate += 2;
						}
					}
					
					//计算有输赢关系的
					for(SeatHuPaiWinAttrib seatHuPaiWinAttrib : mySeat.huPaiWinList){
						Integer lostSeatIndex = seatHuPaiWinAttrib.seatIndex;
						SeatAttrib lostSeat = table.seats.get(lostSeatIndex);						
						int huPaiFanShu = mySeat.statistAttrib.huPaiRate;
						
						int tmpBaiPaiNum = baiPaiNum;
						if(seatHuPaiWinAttrib.tangCardState > 0){
							//被胡座位有摆牌,不管牌形
							huPaiFanShu += 2;
							tmpBaiPaiNum += 1;
						}
						
						int piaoPaiNum = mySeat.piaoNum + lostSeat.piaoNum; 
						
						if(huPaiFanShu >= table.ruleAttrib.maxRate){
							huPaiFanShu = table.ruleAttrib.maxRate;
						}
						int tmpRateScore = table.ruleAttrib.baseScore + table.ruleAttrib.baseScore*huPaiFanShu;
						if(mySeat.huPaiType == GameDefine.HUPAI_TYPE_ZIMO
								|| mySeat.huPaiType == GameDefine.HUPAI_TYPE_GANGFLOW
								|| mySeat.huPaiType == GameDefine.HUPAI_TYPE_DIANGANGFLOW){
							//自摸类型,检查加底还是加番
							if(table.ruleAttrib.bZiMoAddBase){
								tmpRateScore += table.ruleAttrib.baseScore;
							}
						}
						
						int rateScore = tmpRateScore + piaoPaiNum;
						
						mySeat.statistAttrib.onceScore += rateScore;
						lostSeat.statistAttrib.onceScore -= rateScore;
												
						//记录赢的玩家
						SettementSeatScore settementSeatScore = mySeat.seatScore.get(lostSeat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score + tmpRateScore;
						settementSeatScore.totalFanNum = huPaiFanShu;
						settementSeatScore.tangNum = tmpBaiPaiNum;
						settementSeatScore.winPiaoNum = settementSeatScore.winPiaoNum + piaoPaiNum;
						
						mySeat.seatScore.put(lostSeat.accountId, settementSeatScore);
						
						//记录输的玩家
						settementSeatScore = lostSeat.seatScore.get(mySeat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score - tmpRateScore;
						settementSeatScore.totalFanNum = huPaiFanShu;
						settementSeatScore.tangNum = tmpBaiPaiNum;
						settementSeatScore.winPiaoNum = settementSeatScore.winPiaoNum - piaoPaiNum;
						
						lostSeat.seatScore.put(mySeat.accountId, settementSeatScore);						
						
						switch(mySeat.huPaiType){
						case GameDefine.HUPAI_TYPE_DIANPAO:
						case GameDefine.HUPAI_TYPE_QIANGGANG:
						case GameDefine.HUPAI_TYPE_DIANGANGFLOW:
						case GameDefine.HUPAI_TYPE_DIANGANGPAO:
							lostSeat.statistAttrib.outDesc += "点" + mySeat.huPaiIndex + "胡 ";
							break;
						case GameDefine.HUPAI_TYPE_ZIMO:
						case GameDefine.HUPAI_TYPE_GANGFLOW:
							lostSeat.statistAttrib.outDesc += "被" + mySeat.huPaiIndex + "胡自摸 ";
							break;
						}
					}
					
					//计算无输赢关系的座位
					for(int seatIndex2=0; seatIndex2<table.seats.size(); seatIndex2++){
						if(seatIndex2 == seatIndex1){
							//是自已跳过
							continue;
						}
						SeatAttrib lostSeat = table.seats.get(seatIndex2);
						
						boolean bHave = false;
						for(SeatHuPaiWinAttrib seatHuPaiWinAttrib : mySeat.huPaiWinList){
							if(seatIndex2 == seatHuPaiWinAttrib.seatIndex){
								bHave = true;
								break;
							}
						}
						if(bHave){
							//玩家2在玩家1胡牌列表中跳过
							continue;
						}
						
						bHave = false;
						for(SeatHuPaiWinAttrib seatHuPaiWinAttrib : lostSeat.huPaiWinList){
							if(seatIndex1 == seatHuPaiWinAttrib.seatIndex){
								bHave = true;
								break;
							}
						}
						if(bHave){
							//玩家1在玩家2胡牌列表中跳过
							continue;
						}
						
						int tmpTangNum = 0;
						int tmpBaoJiaoNum = 0; 
						if(mySeat.tangCardState > 0){
							tmpTangNum += 1;
						}
						if(mySeat.baoJiaoState > 0){
							tmpBaoJiaoNum += 1;
						}
						
						if(lostSeat.tangCardState > 0){
							tmpTangNum += 1;
						}
						
						if(lostSeat.baoJiaoState > 0){
							tmpBaoJiaoNum += 1;
						}
						
						//记录赢的玩家
						SettementSeatScore settementSeatScore = mySeat.seatScore.get(lostSeat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score + 0;
						settementSeatScore.totalFanNum = 0;
						settementSeatScore.tangNum = tmpTangNum;
						settementSeatScore.baoJiaoNum = tmpBaoJiaoNum;
						settementSeatScore.winPiaoNum = settementSeatScore.winPiaoNum + 0;
						mySeat.seatScore.put(lostSeat.accountId, settementSeatScore);
						
						//记录输的玩家
						settementSeatScore = lostSeat.seatScore.get(mySeat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score - 0;
						settementSeatScore.totalFanNum = 0;
						settementSeatScore.tangNum = tmpTangNum;
						settementSeatScore.baoJiaoNum = tmpBaoJiaoNum;
						settementSeatScore.winPiaoNum = settementSeatScore.winPiaoNum - 0;
						
						lostSeat.seatScore.put(mySeat.accountId, settementSeatScore);
					}
				}
			}
			
			if(table.tableCards.isEmpty() && (unTingPaiSeats.size()+tingPaiSeats.size())==table.seats.size()){
				//流局了才检查查叫
				if(unTingPaiSeats.isEmpty() == false && tingPaiSeats.isEmpty() == false){
					//未下叫的人数大于0且下叫的人数大于0,需要查叫
					for(SeatAttrib tingPaiSeat : tingPaiSeats){
						int tangNum = 0;
						tingPaiSeat.statistAttrib.totalChaJiao++;
						tingPaiSeat.huPaiType = GameDefine.HUPAI_TYPE_CHAJIAO;
						tingPaiSeat.statistAttrib.huPaiRate += tingPaiSeat.statistAttrib.tingPaiRate;
						tingPaiSeat.statistAttrib.huPaiRate += tingPaiSeat.statistAttrib.anGangNum;
						tingPaiSeat.statistAttrib.huPaiRate += tingPaiSeat.statistAttrib.baGangNum;
						tingPaiSeat.statistAttrib.huPaiRate += tingPaiSeat.statistAttrib.dianGangNum;
						tingPaiSeat.statistAttrib.huPaiRate += tingPaiSeat.statistAttrib.daiGengNum;
						
						if(tingPaiSeat.tangCardState > 0){
							tangNum += 1;
							if(tingPaiSeat.huPaiStyle[GameDefine.HUPAI_STYLE_BAIDUZHANG]<=0){
								//自已有躺加番且不是摆的独张
								tingPaiSeat.statistAttrib.huPaiRate += 2;
							}
						}
						
						if(tingPaiSeat.statistAttrib.huPaiRate >= table.ruleAttrib.maxRate){
							tingPaiSeat.statistAttrib.huPaiRate = table.ruleAttrib.maxRate;
						}
						int tmpRateScore = table.ruleAttrib.baseScore + table.ruleAttrib.baseScore*tingPaiSeat.statistAttrib.huPaiRate;
						for(SeatAttrib unTingSeat : unTingPaiSeats){
							//未下叫的玩家不算杠钱
							unTingSeat.huPaiWinList.clear();
							unTingSeat.anGangWinList.clear();
							unTingSeat.baGangWinList.clear();
							unTingSeat.dianGangWinList.clear();
							unTingSeat.zhuanYuSeats.clear();
							unTingSeat.dianGangJiaJiaYouWinList.clear();
													
							int piaoPaiNum = tingPaiSeat.piaoNum + unTingSeat.piaoNum;
							int rateScore = tmpRateScore + piaoPaiNum;
							
							tingPaiSeat.statistAttrib.onceScore += rateScore;
							unTingSeat.statistAttrib.onceScore -= rateScore;
							unTingSeat.statistAttrib.outDesc += "被查叫";
														
							//记录赢的玩家
							SettementSeatScore settementSeatScore = tingPaiSeat.seatScore.get(unTingSeat.accountId);
							if(null == settementSeatScore){
								settementSeatScore = new SettementSeatScore();
							}
							settementSeatScore.score = settementSeatScore.score + tmpRateScore;
							settementSeatScore.totalFanNum = tingPaiSeat.statistAttrib.huPaiRate;
							settementSeatScore.tangNum = tangNum;
							settementSeatScore.winPiaoNum = settementSeatScore.winPiaoNum + piaoPaiNum;
							tingPaiSeat.seatScore.put(unTingSeat.accountId, settementSeatScore);
							
							//记录输的玩家
							settementSeatScore = unTingSeat.seatScore.get(tingPaiSeat.accountId);
							if(null == settementSeatScore){
								settementSeatScore = new SettementSeatScore();
							}
							settementSeatScore.score = settementSeatScore.score - tmpRateScore;
							settementSeatScore.totalFanNum = tingPaiSeat.statistAttrib.huPaiRate;
							settementSeatScore.tangNum = tangNum;
							settementSeatScore.winPiaoNum = settementSeatScore.winPiaoNum - piaoPaiNum;
							unTingSeat.seatScore.put(tingPaiSeat.accountId, settementSeatScore);
						}
					}
				}else{
					if(unTingPaiSeats.size()>1 && tingPaiSeats.isEmpty()){
						//大家都没叫,未下叫的玩家不算杠钱
						for(SeatAttrib unTingSeat : unTingPaiSeats){
							unTingSeat.huPaiWinList.clear();
							unTingSeat.anGangWinList.clear();
							unTingSeat.baGangWinList.clear();
							unTingSeat.dianGangWinList.clear();
							unTingSeat.zhuanYuSeats.clear();
							unTingSeat.dianGangJiaJiaYouWinList.clear();
						}
					}else{
						if(unTingPaiSeats.isEmpty() && tingPaiSeats.isEmpty() == false){
							//大家都有叫
							for(int tmpSeatIndex2=0; tmpSeatIndex2<tingPaiSeats.size(); tmpSeatIndex2++){
								SeatAttrib tingPaiSeat = tingPaiSeats.get(tmpSeatIndex2);
								tingPaiSeat.huPaiType = GameDefine.HUPAI_TYPE_TINGPAI;
								for(int tmpSeatIndex3=0; tmpSeatIndex3<tingPaiSeats.size(); tmpSeatIndex3++){
									if(tmpSeatIndex2 == tmpSeatIndex3){
										continue;
									}
									SeatAttrib lostSeat = tingPaiSeats.get(tmpSeatIndex3);
									
									int tangNum = 0;
									int baojiaoNum = 0;
									if(tingPaiSeat.tangCardState > 0){
										tangNum += 1;
									}
									if(tingPaiSeat.baoJiaoState > 0){
										baojiaoNum += 1;
									}
									
									if(lostSeat.tangCardState > 0){
										tangNum += 1;
									}
									
									if(lostSeat.baoJiaoState > 0){
										baojiaoNum += 1;
									}
									//记录赢的玩家
									SettementSeatScore settementSeatScore = tingPaiSeat.seatScore.get(lostSeat.accountId);
									if(null == settementSeatScore){
										settementSeatScore = new SettementSeatScore();
									}
									settementSeatScore.score = settementSeatScore.score + 0;
									settementSeatScore.totalFanNum = 0;
									settementSeatScore.tangNum = tangNum;
									settementSeatScore.baoJiaoNum = baojiaoNum;
									settementSeatScore.winPiaoNum = settementSeatScore.winPiaoNum + 0;
									tingPaiSeat.seatScore.put(lostSeat.accountId, settementSeatScore);
									
									//记录输的玩家
									settementSeatScore = lostSeat.seatScore.get(tingPaiSeat.accountId);
									if(null == settementSeatScore){
										settementSeatScore = new SettementSeatScore();
									}
									settementSeatScore.score = settementSeatScore.score - 0;
									settementSeatScore.totalFanNum = 0;
									settementSeatScore.tangNum = tangNum;
									settementSeatScore.baoJiaoNum = baojiaoNum;
									settementSeatScore.winPiaoNum = settementSeatScore.winPiaoNum - 0;
									
									lostSeat.seatScore.put(tingPaiSeat.accountId, settementSeatScore);
								}
							}
						}
					}
				}
			}			
			//记录单局战绩
			table.recordManager.recordOverOnce(table, currTime);
			//统计总输赢
			String sLog = "统计总输赢,";
			for(SeatAttrib seat : table.seats){
				sLog += "座位="+seat.seatIndex+"分数="+seat.statistAttrib.onceScore;
				seat.score += seat.statistAttrib.onceScore;
			}
			table.logicManager.log(sLog);
			
			table.bGameNext = true;
			if((table.currGameNum+1) > table.ruleAttrib.maxGameNum
					|| table.breakStateSource == GameDefine.STATE_TABLE_DESTORY
					|| currTime >= table.maxVaildTime){
				table.bGameNext = false;
			}
			
			//通知客户端结算
			table.sendGameStateNotify();
			table.currGameNum++;
			if(nextBanker != -1){
				table.bankerIndex = nextBanker;
			}else{
				//南充麻将没有人胡牌,上把庄家的下一家当庄家
				nextBanker = table.getNextSeatIndex(table.bankerIndex);
				table.bankerIndex = nextBanker;
			}
		}
		table.bSendAllOverNotify = false;
		table.bActExec = false;
		if(table.bGameNext == false){
			//所有局已打完,或者都同意解散,或者桌子最大有效时间到了强制结束
			table.gameState = GameDefine.STATE_TABLE_OVER_ALL;
		}else{
			table.gameState = GameDefine.STATE_TABLE_IDLE;
			table.tableReset();
		}
	}
	
	private static void run_over_once_lsmj(long currTime, TableAttrib table){
		if(table.bActExec == false){
			table.bActExec = true;
			table.waitTime = currTime + GameDefine.TIME_TABLE_OVER_ONCE;
			int nextBanker = -1;
			//没有下叫的座位
			List<SeatAttrib> unTingPaiSeats = new ArrayList<>();
			//下了叫未胡牌的座位
			List<SeatAttrib> tingPaiSeats = new ArrayList<>();
			
			table.logicManager.log("一局游戏结束");
			for(SeatAttrib seat : table.seats){
				seat.statistAttrib.anGangNum = seat.anGangCards.size();
				seat.statistAttrib.dianGangNum = seat.dianGangCards.size();
				seat.statistAttrib.baGangNum = seat.baGangCards.size();
				
				if(seat.huPaiType == GameDefine.HUPAI_TYPE_NONE){
					//没有胡牌的人
					List<CardAttrib> copyHandCards = new ArrayList<>();
					copyHandCards.addAll(seat.handCards);
					//找出手牌中的幺鸡
					List<CardAttrib> yaoJiList_hands = GameDefine.findAllBySuitPoint(copyHandCards, GameDefine.SUIT_TYPE_TIAO, 1);
					
					//保存原始手牌
					List<CardAttrib> savehandCards = new ArrayList<>();
					List<List<CardAttrib>> saveanGangCards = new ArrayList<>();
					List<List<CardAttrib>> savebaGangCards = new ArrayList<>();
					List<List<CardAttrib>> savedianGangCards = new ArrayList<>();
					List<List<CardAttrib>> savepengCards = new ArrayList<>();
					savehandCards.addAll(seat.handCards);
					saveanGangCards.addAll(seat.anGangCards);
					savebaGangCards.addAll(seat.baGangCards);
					savedianGangCards.addAll(seat.dianGangCards);
					savepengCards.addAll(seat.pengCards);					
					
					if(table.ruleAttrib.bYaoJiRenYong){
						//移除手中的幺鸡
						GameDefine.removeAllBySuitPoint(copyHandCards, yaoJiList_hands);
						//替换杠碰牌的幺鸡
						seat.replaceYaoJi();
					}					
					//胡牌组合列表
					List<List<CardAttrib>> huPaiComboList = null;
					int paiStyleRate = 0;
					if(table.ruleAttrib.chaDaJiao > 0){
						//查大叫
						paiStyleRate = -1;
					}else{
						//查小叫
						paiStyleRate = 10000;
					}
					
					//组合幺鸡所有的听牌
					if(table.ruleAttrib.bYaoJiRenYong && yaoJiList_hands.size() > 0){
						List<CardAttrib> allSuitCards = GameDefine.initCards(seat.unSuit);
						int allN = allSuitCards.size();
						int allM = yaoJiList_hands.size();
						int []comboIndex = new int[allM];
						List<List<CardAttrib>> resultCardList = new ArrayList<>();
						GameDefine.combine(allSuitCards, allN, allM, comboIndex, allM, resultCardList);
						
						for(List<CardAttrib> resultCards : resultCardList){
							//重设手牌
							seat.handCards.clear();
							seat.handCards.addAll(copyHandCards);
							seat.handCards.addAll(resultCards);
							List<CardAttrib> tingPaiList = GameDefine.isTingPai(table, seat.handCards, seat);
							if(null != tingPaiList && tingPaiList.isEmpty() == false){
								//听了牌
								for(CardAttrib canHu : tingPaiList){
									List<List<CardAttrib>> tmpHuComboList = GameDefine.checkHuPai(table, seat, canHu);
									int []huPaiStyle = CardAnalysisManager.analysisHuPaiStyle2(table, seat, canHu, tmpHuComboList);
									//查叫不算海底捞,海底炮,天地胡
									huPaiStyle[GameDefine.HUPAI_STYLE_HAIDILAO] = 0;
									huPaiStyle[GameDefine.HUPAI_STYLE_HAIDILAO_DIANPAO] = 0;
									huPaiStyle[GameDefine.HUPAI_STYLE_TIANHU] = 0;
									huPaiStyle[GameDefine.HUPAI_STYLE_DIEHU] = 0;
									int tmpRate = CardAnalysisManager.getHuPaiStyleRate(table, huPaiStyle);
									if(table.ruleAttrib.chaDaJiao > 0){
										//查大叫
										if(tmpRate > paiStyleRate){
											paiStyleRate = tmpRate;
											huPaiComboList = tmpHuComboList;
											System.arraycopy(huPaiStyle, 0, seat.huPaiStyle, 0, GameDefine.HUPAI_STYLE_MAX_END);
										}
									}else{
										//查小叫
										if(tmpRate < paiStyleRate){
											paiStyleRate = tmpRate;
											huPaiComboList = tmpHuComboList;
											System.arraycopy(huPaiStyle, 0, seat.huPaiStyle, 0, GameDefine.HUPAI_STYLE_MAX_END);
										}
									}
								}
							}
						}
					}else{
						//手牌没有幺鸡或未开启幺鸡任用
						List<CardAttrib> tingPais = GameDefine.isTingPai(table, seat.handCards, seat);
						if(null != tingPais && tingPais.isEmpty() == false){
							//有叫,要查叫,计算杠钱
							for(CardAttrib canHu : tingPais){
								List<List<CardAttrib>> tmpHuComboList = GameDefine.checkHuPai(table, seat, canHu);
								int []huPaiStyle = CardAnalysisManager.analysisHuPaiStyle2(table, seat, canHu, tmpHuComboList);
								huPaiStyle[GameDefine.HUPAI_STYLE_HAIDILAO] = 0;
								huPaiStyle[GameDefine.HUPAI_STYLE_HAIDILAO_DIANPAO] = 0;
								huPaiStyle[GameDefine.HUPAI_STYLE_TIANHU] = 0;
								huPaiStyle[GameDefine.HUPAI_STYLE_DIEHU] = 0;
								int tmpRate = CardAnalysisManager.getHuPaiStyleRate(table, huPaiStyle);
								if(table.ruleAttrib.chaDaJiao > 0){
									//查大叫
									if(tmpRate > paiStyleRate){
										paiStyleRate = tmpRate;
										huPaiComboList = tmpHuComboList;
										System.arraycopy(huPaiStyle, 0, seat.huPaiStyle, 0, GameDefine.HUPAI_STYLE_MAX_END);
									}
								}else{
									//查小叫
									if(tmpRate < paiStyleRate){
										paiStyleRate = tmpRate;
										huPaiComboList = tmpHuComboList;
										System.arraycopy(huPaiStyle, 0, seat.huPaiStyle, 0, GameDefine.HUPAI_STYLE_MAX_END);
									}
								}
							}
						}	
					}	
				
					if(null == huPaiComboList){
						unTingPaiSeats.add(seat);
					}else{
						tingPaiSeats.add(seat);
						List<CardAttrib> fours = new ArrayList<>();
						for(List<CardAttrib> huPaiCombo : huPaiComboList){
							fours.addAll(huPaiCombo);
						}
						List<List<CardAttrib>> four = GameDefine.findFourCards(fours);
						seat.statistAttrib.daiGengNum = four.size();
						seat.huCards.clear();
						seat.statistAttrib.tingPaiRate = paiStyleRate;
						four = null;
					}
					//还原手牌
					seat.handCards.clear();
					seat.handCards.addAll(savehandCards);
					seat.anGangCards.clear();
					seat.anGangCards.addAll(saveanGangCards);
					seat.baGangCards.clear();
					seat.baGangCards.addAll(savebaGangCards);
					seat.dianGangCards.clear();
					seat.dianGangCards.addAll(savedianGangCards);
					seat.pengCards.clear();
					seat.pengCards.addAll(savepengCards);
				}else{
					//胡了牌的座位
					//计算下一局庄家(第一个胡牌的人)
					if(seat.huPaiIndex == 1){
						if(nextBanker == -1){
							nextBanker = seat.seatIndex;
						}else{
							//点了双炮或三炮的玩家
							nextBanker = seat.huPaiWinList.get(0).seatIndex;
						}
					}					
					
					if(null != seat.huPaiComboList){
						//计算带根
						List<CardAttrib> tmpCards = new ArrayList<>();
						for(List<CardAttrib> huPaiCombo : seat.huPaiComboList){
							tmpCards.addAll(huPaiCombo);
						}
						List<List<CardAttrib>> four = GameDefine.findFourCards(tmpCards);
						seat.statistAttrib.daiGengNum = four.size();
					}
					
					seat.statistAttrib.huPaiRate = CardAnalysisManager.getHuPaiStyleRate(table, seat.huPaiStyle);
					seat.statistAttrib.huPaiRate += seat.statistAttrib.anGangNum;
					seat.statistAttrib.huPaiRate += seat.statistAttrib.baGangNum;
					seat.statistAttrib.huPaiRate += seat.statistAttrib.dianGangNum;
					seat.statistAttrib.huPaiRate += seat.statistAttrib.daiGengNum;
					int huPaiTypeRate = CardAnalysisManager.getHuPaiTypeRate(table, seat.huPaiType);
					seat.statistAttrib.huPaiRate += huPaiTypeRate;
					
					for(SeatHuPaiWinAttrib seatHuPaiWinAttrib : seat.huPaiWinList){
						Integer lostSeatIndex = seatHuPaiWinAttrib.seatIndex;
						SeatAttrib lostSeat = table.seats.get(lostSeatIndex);						
						int huPaiFanShu = seat.statistAttrib.huPaiRate;
												
						if(huPaiFanShu >= table.ruleAttrib.maxRate){
							huPaiFanShu = table.ruleAttrib.maxRate;
						}
						int rateScore = GameDefine.calculationRateScore(table.ruleAttrib.baseScore, huPaiFanShu);
						if(seat.huPaiType == GameDefine.HUPAI_TYPE_ZIMO
								|| seat.huPaiType == GameDefine.HUPAI_TYPE_GANGFLOW
								|| seat.huPaiType == GameDefine.HUPAI_TYPE_DIANGANGFLOW){
							//自摸类型,检查加底还是加番
							if(table.ruleAttrib.bZiMoAddBase){
								rateScore += table.ruleAttrib.baseScore;
							}
						}
						
						seat.statistAttrib.onceScore += rateScore;
						lostSeat.statistAttrib.onceScore -= rateScore;
												
						//记录赢的玩家
						SettementSeatScore settementSeatScore = seat.seatScore.get(lostSeat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score + rateScore;
						settementSeatScore.totalFanNum = huPaiFanShu;
						seat.seatScore.put(lostSeat.accountId, settementSeatScore);
						
						//记录输的玩家
						settementSeatScore = lostSeat.seatScore.get(seat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score - rateScore;
						settementSeatScore.totalFanNum = huPaiFanShu;
						
						lostSeat.seatScore.put(seat.accountId, settementSeatScore);						
						
						switch(seat.huPaiType){
						case GameDefine.HUPAI_TYPE_DIANPAO:
						case GameDefine.HUPAI_TYPE_QIANGGANG:
						case GameDefine.HUPAI_TYPE_DIANGANGFLOW:
						case GameDefine.HUPAI_TYPE_DIANGANGPAO:
							lostSeat.statistAttrib.outDesc += "点" + seat.huPaiIndex + "胡 ";
							break;
						case GameDefine.HUPAI_TYPE_ZIMO:
						case GameDefine.HUPAI_TYPE_GANGFLOW:
							lostSeat.statistAttrib.outDesc += "被" + seat.huPaiIndex + "胡自摸 ";
							break;
						}
					}
				}
			}
			
			if(table.tableCards.isEmpty() && (unTingPaiSeats.size()+tingPaiSeats.size() > 1)){
				//流局了才需要检查查叫
				if(unTingPaiSeats.isEmpty() == false && tingPaiSeats.isEmpty() == false){
					//需要查叫
					for(SeatAttrib tingPaiSeat : tingPaiSeats){
						tingPaiSeat.statistAttrib.totalChaJiao++;
						tingPaiSeat.huPaiType = GameDefine.HUPAI_TYPE_CHAJIAO;
						tingPaiSeat.statistAttrib.huPaiRate += tingPaiSeat.statistAttrib.tingPaiRate;
						tingPaiSeat.statistAttrib.huPaiRate += tingPaiSeat.statistAttrib.anGangNum;
						tingPaiSeat.statistAttrib.huPaiRate += tingPaiSeat.statistAttrib.baGangNum;
						tingPaiSeat.statistAttrib.huPaiRate += tingPaiSeat.statistAttrib.dianGangNum;
						tingPaiSeat.statistAttrib.huPaiRate += tingPaiSeat.statistAttrib.daiGengNum;
											
						if(tingPaiSeat.statistAttrib.huPaiRate >= table.ruleAttrib.maxRate){
							tingPaiSeat.statistAttrib.huPaiRate = table.ruleAttrib.maxRate;
						}
						int rateScore = GameDefine.calculationRateScore(table.ruleAttrib.baseScore, tingPaiSeat.statistAttrib.huPaiRate);
						for(SeatAttrib unTingSeat : unTingPaiSeats){
							//未下叫的玩家不算杠钱
							unTingSeat.huPaiWinList.clear();
							unTingSeat.anGangWinList.clear();
							unTingSeat.baGangWinList.clear();
							unTingSeat.dianGangWinList.clear();
							unTingSeat.zhuanYuSeats.clear();
							unTingSeat.dianGangJiaJiaYouWinList.clear();
													
							tingPaiSeat.statistAttrib.onceScore += rateScore;
							unTingSeat.statistAttrib.onceScore -= rateScore;
							unTingSeat.statistAttrib.outDesc += "被查叫";
							
							//记录赢的玩家
							SettementSeatScore settementSeatScore = tingPaiSeat.seatScore.get(unTingSeat.accountId);
							if(null == settementSeatScore){
								settementSeatScore = new SettementSeatScore();
							}
							settementSeatScore.score = settementSeatScore.score + rateScore;
							settementSeatScore.totalFanNum = tingPaiSeat.statistAttrib.huPaiRate;
							tingPaiSeat.seatScore.put(unTingSeat.accountId, settementSeatScore);
							
							//记录输的玩家
							settementSeatScore = unTingSeat.seatScore.get(tingPaiSeat.accountId);
							if(null == settementSeatScore){
								settementSeatScore = new SettementSeatScore();
							}
							settementSeatScore.score = settementSeatScore.score - rateScore;
							settementSeatScore.totalFanNum = tingPaiSeat.statistAttrib.huPaiRate;
							unTingSeat.seatScore.put(tingPaiSeat.accountId, settementSeatScore);
						}
					}
				}else{
					if(unTingPaiSeats.size()>1 && tingPaiSeats.isEmpty()){
						//大家都没叫,未下叫的玩家不算杠钱
						for(SeatAttrib unTingSeat : unTingPaiSeats){
							unTingSeat.huPaiWinList.clear();
							unTingSeat.anGangWinList.clear();
							unTingSeat.baGangWinList.clear();
							unTingSeat.dianGangWinList.clear();
							unTingSeat.zhuanYuSeats.clear();
							unTingSeat.dianGangJiaJiaYouWinList.clear();
						}
					}else{
						if(unTingPaiSeats.isEmpty() && tingPaiSeats.isEmpty() == false){
							//大家都有叫
							for(SeatAttrib tingPaiSeat : tingPaiSeats){
								tingPaiSeat.huPaiType = GameDefine.HUPAI_TYPE_TINGPAI;
							}
						}
					}
				}
			}
			
			//计算杠钱
			for(SeatAttrib seat : table.seats){	
				//计算转雨
				if(table.ruleAttrib.bZhuanYu){
					for(SeatHuJiaoZhuYiAttrib seatHuJiaoZhuYiAttrib : seat.zhuanYuSeats){
						SeatAttrib winSeat = table.seats.get(seatHuJiaoZhuYiAttrib.seatIndex);
						
						winSeat.statistAttrib.onceScore += seatHuJiaoZhuYiAttrib.score;
						seat.statistAttrib.onceScore -= seatHuJiaoZhuYiAttrib.score;
						
						//记录赢的玩家
						SettementSeatScore settementSeatScore = winSeat.seatScore.get(seat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score + seatHuJiaoZhuYiAttrib.score;
						seat.seatScore.put(seat.accountId, settementSeatScore);
						
						//记录输的玩家
						settementSeatScore = seat.seatScore.get(winSeat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score - seatHuJiaoZhuYiAttrib.score;
						seat.seatScore.put(winSeat.accountId, settementSeatScore);
					}
				}
				
				//计算巴到烫
				if(table.ruleAttrib.bJiaJiaYou){
					for(List<Integer> jiajiayouList : seat.dianGangJiaJiaYouWinList){
						seat.statistAttrib.jiajiaYouNum += jiajiayouList.size();
						for(int lostSeatIndex : jiajiayouList){
							SeatAttrib lostSeat = table.seats.get(lostSeatIndex);
							seat.statistAttrib.onceScore += table.ruleAttrib.baseScore;
							lostSeat.statistAttrib.onceScore -= table.ruleAttrib.baseScore;
							lostSeat.statistAttrib.outJiajiaYouNum++;
							
							//记录赢的玩家
							SettementSeatScore settementSeatScore = seat.seatScore.get(lostSeat.accountId);
							if(null == settementSeatScore){
								settementSeatScore = new SettementSeatScore();
							}
							settementSeatScore.score = settementSeatScore.score + table.ruleAttrib.baseScore;
							seat.seatScore.put(lostSeat.accountId, settementSeatScore);
							
							//记录输的玩家
							settementSeatScore = lostSeat.seatScore.get(seat.accountId);
							if(null == settementSeatScore){
								settementSeatScore = new SettementSeatScore();
							}
							settementSeatScore.score = settementSeatScore.score - table.ruleAttrib.baseScore;
							lostSeat.seatScore.put(seat.accountId, settementSeatScore);
						}
					}
				}
				
				//暗杠
				for(List<Integer> angangWin : seat.anGangWinList){
					for(int theSeatIndex : angangWin){
						seat.statistAttrib.onceScore += table.ruleAttrib.baseScore*2;
						SeatAttrib lostSeat = table.seats.get(theSeatIndex);
						lostSeat.statistAttrib.onceScore -= table.ruleAttrib.baseScore*2;
						lostSeat.statistAttrib.outAnGang++;
						
						//记录赢的玩家
						SettementSeatScore settementSeatScore = seat.seatScore.get(lostSeat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score + table.ruleAttrib.baseScore*2;
						seat.seatScore.put(lostSeat.accountId, settementSeatScore);
						
						//记录输的玩家
						settementSeatScore = lostSeat.seatScore.get(seat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score - table.ruleAttrib.baseScore*2;
						lostSeat.seatScore.put(seat.accountId, settementSeatScore);
					}
				}
				//巴杠
				for(List<Integer> bagangWin : seat.baGangWinList){
					for(int theSeatIndex : bagangWin){
						seat.statistAttrib.onceScore += table.ruleAttrib.baseScore;
						SeatAttrib lostSeat = table.seats.get(theSeatIndex);
						lostSeat.statistAttrib.onceScore -= table.ruleAttrib.baseScore;
						lostSeat.statistAttrib.outBaGang++;						
						//记录赢的玩家
						SettementSeatScore settementSeatScore = seat.seatScore.get(lostSeat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score + table.ruleAttrib.baseScore;
						seat.seatScore.put(lostSeat.accountId, settementSeatScore);
						
						//记录输的玩家
						settementSeatScore = lostSeat.seatScore.get(seat.accountId);
						if(null == settementSeatScore){
							settementSeatScore = new SettementSeatScore();
						}
						settementSeatScore.score = settementSeatScore.score - table.ruleAttrib.baseScore;
						lostSeat.seatScore.put(seat.accountId, settementSeatScore);
					}
				}
				//点杠
				for(int theSeatIndex : seat.dianGangWinList){
					seat.statistAttrib.onceScore += table.ruleAttrib.baseScore*2;
					SeatAttrib lostSeat = table.seats.get(theSeatIndex);
					lostSeat.statistAttrib.onceScore -= table.ruleAttrib.baseScore*2;
					lostSeat.statistAttrib.outDianGang++;
					
					//记录赢的玩家
					SettementSeatScore settementSeatScore = seat.seatScore.get(lostSeat.accountId);
					if(null == settementSeatScore){
						settementSeatScore = new SettementSeatScore();
					}
					settementSeatScore.score = settementSeatScore.score + table.ruleAttrib.baseScore*2;
					seat.seatScore.put(lostSeat.accountId, settementSeatScore);
					
					//记录输的玩家
					settementSeatScore = lostSeat.seatScore.get(seat.accountId);
					if(null == settementSeatScore){
						settementSeatScore = new SettementSeatScore();
					}
					settementSeatScore.score = settementSeatScore.score - table.ruleAttrib.baseScore*2;
					lostSeat.seatScore.put(seat.accountId, settementSeatScore);
				}
			}			
			
			//记录单局战绩
			table.recordManager.recordOverOnce(table, currTime);
			//统计总输赢
			String sLog = "统计总输赢,";
			for(SeatAttrib seat : table.seats){
				sLog += "座位="+seat.seatIndex+"分数="+seat.statistAttrib.onceScore;
				seat.score += seat.statistAttrib.onceScore;
			}
			table.logicManager.log(sLog);
			
			table.bGameNext = true;
			if((table.currGameNum+1) > table.ruleAttrib.maxGameNum
					|| table.breakStateSource == GameDefine.STATE_TABLE_DESTORY
					|| currTime >= table.maxVaildTime){
				//所有局都打完或者同意解散桌子或者超过了最大时间
				table.bGameNext = false;
			}						
			//通知客户端结算
			table.sendGameStateNotify();
			table.currGameNum++;
			if(nextBanker != -1){
				//没有人胡牌,上把庄家继续当庄家
				table.bankerIndex = nextBanker;
			}
		}
		table.bSendAllOverNotify = false;
		table.bActExec = false;
		if(table.bGameNext == false){
			//所有局已打完,或者都同意解散,或者桌子最大有效时间到了强制结束
			table.gameState = GameDefine.STATE_TABLE_OVER_ALL;
		}else{
			table.gameState = GameDefine.STATE_TABLE_IDLE;
			table.tableReset();
		}
	} 

}
