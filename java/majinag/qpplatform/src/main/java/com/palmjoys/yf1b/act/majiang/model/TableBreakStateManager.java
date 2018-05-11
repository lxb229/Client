package com.palmjoys.yf1b.act.majiang.model;

import java.util.ArrayList;
import java.util.List;

public class TableBreakStateManager {
	public static void run_breakcard(long currTime, TableAttrib table){
		switch(table.cfgId){
		case 7://乐山麻将
			TableBreakStateManager.run_breakcard_lsmj(currTime, table);
			break;
		case 8://南充麻将
			TableBreakStateManager.run_breakcard_ncmj(currTime, table);
			break;
		default:
			TableBreakStateManager.run_breakcard_other(currTime, table);
			break;
		}
	}
	
	private static void run_breakcard_ncmj(long currTime, TableAttrib table){		
		int unBt = GameDefine.ACT_INDEX_VAILD;
		int useBt = GameDefine.ACT_INDEX_VAILD;
		int minBt = GameDefine.ACT_INDEX_VAILD;
		
		for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
			SeatAttrib seatAttrib = table.seats.get(seatIndex);
			if(GameDefine.ACT_STATE_WAIT == seatAttrib.btState){
				//此座位还未表态,查找此座位能表态的最高表态行为
				for(int i=0; i<GameDefine.ACT_INDEX_VAILD; i++){
					if(seatAttrib.breakCardState[i] > 0){
						if(i<unBt){
							unBt = i;	
						}
					}
				}
			}else{
				//此座位已表态,查找当前所有表态的最高表态行为
				if(useBt > seatAttrib.breakBtState){
					useBt = seatAttrib.breakBtState;
				}
			}
		}
		if(unBt <= useBt){
			if(unBt < useBt){
				//没有表态的玩家有优先级更高的表态,必须等到此玩家表态
				return;
			}else{
				if(unBt == GameDefine.ACT_INDEX_GANG
						|| unBt == GameDefine.ACT_INDEX_PENG){
					//未表态的玩家和已表态玩家有相同的表态状态,查找座位优先级最高的玩家
					SeatAttrib findSeatAttrib = null;
					int findSeatIndex = table.btIndex;
					while(true){
						SeatAttrib seat = table.seats.get(findSeatIndex);
						if((seat.breakCardState[unBt]>0 && seat.btState != GameDefine.ACT_STATE_DROP)
								|| (seat.breakCardState[unBt]>0 && seat.btState == GameDefine.ACT_STATE_BT
								&& seat.breakBtState < GameDefine.ACT_INDEX_DROP)){
							findSeatAttrib = seat;
							break;
						}
						
						findSeatIndex = table.getNextUnHuSeatIndex(seat.seatIndex);
					}
					if(findSeatAttrib.btState == GameDefine.ACT_STATE_WAIT){
						//优先级高的玩家还没有表态,等待表态
						return;
					}
				}else{
					return;
				}
			}
		}
		SeatAttrib btSeat = table.seats.get(table.btIndex);
		
		minBt = useBt;
		List<SeatAttrib> breakBtSeats = new ArrayList<SeatAttrib>();
		for(SeatAttrib seatAttrib : table.seats){
			if(GameDefine.ACT_STATE_BT == seatAttrib.btState){
				if(seatAttrib.breakBtState == minBt){
					breakBtSeats.add(seatAttrib);
				}else if(seatAttrib.breakBtState == GameDefine.ACT_INDEX_DROP){
					if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
						//除自摸外的有胡不胡点过,加入过手牌限制
						if(table.breakStateSource == GameDefine.STATE_TABLE_OUTCARD
								|| table.breakStateSource == GameDefine.STATE_TABLE_BREAKCARD){
							//别人点炮不胡
							seatAttrib.bDianPaoNoHu = true;
							seatAttrib.guoShouHuCardList.add(btSeat.breakCard);
						}
					}
				}
			}else if(GameDefine.ACT_STATE_DROP == seatAttrib.btState){
				if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
					//除自摸外的有胡不胡点过,加入过手牌限制
					if(table.breakStateSource == GameDefine.STATE_TABLE_OUTCARD
							|| table.breakStateSource == GameDefine.STATE_TABLE_BREAKCARD){
						//别人点炮不胡
						seatAttrib.bDianPaoNoHu = true;
						seatAttrib.guoShouHuCardList.add(btSeat.breakCard);
					}
				}
			}
		}
		if(minBt == GameDefine.ACT_INDEX_GANG
						|| minBt == GameDefine.ACT_INDEX_PENG){
			//碰杠只能有一人
			if(breakBtSeats.size() > 1){
				int findSeatIndex = table.btIndex;
				List<Integer> breakSeatIndexs = new ArrayList<>();
				for(SeatAttrib breakBtSeat : breakBtSeats){
					breakSeatIndexs.add(breakBtSeat.seatIndex);
				}
				breakBtSeats.clear();
				while(true){
					if(breakSeatIndexs.contains(findSeatIndex)){
						breakBtSeats.add(table.seats.get(findSeatIndex));
						break;
					}
					
					findSeatIndex = table.getNextUnHuSeatIndex(findSeatIndex);
				}
			}
		}
		
		table.bActExec = false;
		if(breakBtSeats.isEmpty()){
			table.logicManager.log("桌子轮到中断表态,所有人都选择了过");
			btSeat.gangType = GameDefine.GANG_TYPE_NONE;
			btSeat.gangSeatIndex = 0;
			//都选择了过,不中断原来的进程
			if(table.breakStateSource == GameDefine.STATE_TABLE_MOPAI){
				table.gameState = GameDefine.STATE_TABLE_OUTCARD;
			}else if(table.breakStateSource == GameDefine.STATE_TABLE_OUTCARD){
				//打出的牌没人要,下一玩家摸牌
				btSeat.gangSeatIndex = 0;
				table.btIndex = table.getNextUnHuSeatIndex(btSeat.seatIndex);
				table.gameState = GameDefine.STATE_TABLE_MOPAI;
			}else if(table.breakStateSource == GameDefine.STATE_TABLE_BREAKCARD){
				//巴杠,居然不抢杠胡,呵呵不好意思了
				//记录收巴杠钱
				List<Integer> winSeats = new ArrayList<>();
				for(SeatAttrib seat : table.seats){
					if(seat.huPaiType != GameDefine.HUPAI_TYPE_NONE
							|| seat.seatIndex == btSeat.seatIndex){
						continue;
					}
					winSeats.add(seat.seatIndex);
				}
				
				if(table.ruleAttrib.bGuaFengXiaYu){
					btSeat.baGangWinList.add(winSeats);
				}
				btSeat.statistAttrib.totalBagang++;
				btSeat.gangType = GameDefine.GANG_TYPE_SELF_BAGANG;
				
				//杠了继续摸牌
				table.gameState = GameDefine.STATE_TABLE_WAIT_EX;
				table.waitTime = currTime + GameDefine.TIME_TABLE_WAIT_EX;
				table.nextGameState = GameDefine.STATE_TABLE_MOPAI;
			}
			breakBtSeats = null;
			return;
		}
		table.logicManager.log("桌子轮到中断表态,表态有玩家总共数=" + breakBtSeats.size());
		int tmpGameState = table.gameState;
		int tmpBtIndex = table.btIndex;
		if(minBt != GameDefine.ACT_INDEX_HU && minBt != GameDefine.ACT_INDEX_TANG){
			btSeat.gangType = GameDefine.GANG_TYPE_NONE;
		}
		if(minBt == GameDefine.ACT_INDEX_HU){
			//胡了			
			int hupaiIndex = table.getHuPaiIndex() + 1;
			if(table.breakStateSource == GameDefine.STATE_TABLE_MOPAI){
				//自摸
				SeatAttrib winSeat = breakBtSeats.get(0);
				winSeat.huPaiIndex = hupaiIndex;
				winSeat.huCards.add(btSeat.breakCard);
				winSeat.moPaiCard = null;
				if(winSeat.gangType == GameDefine.GANG_TYPE_SELF_BAGANG
						|| winSeat.gangType == GameDefine.GANG_TYPE_SELF_ANGANG){
					//自摸杠上花,要赢所有未胡牌的玩家
					String sLog = "桌子轮到中断表态,表态座位=[" + winSeat.seatIndex + "]自摸杠上花=";
					sLog += GameDefine.cards2String(winSeat.huCards);
					sLog += "赢取座位=";
					
					winSeat.huPaiType = GameDefine.HUPAI_TYPE_GANGFLOW;
					for(SeatAttrib seat : table.seats){
						if(seat.huPaiIndex ==0){
							SeatHuPaiWinAttrib seatHuPaiWinAttrib = new SeatHuPaiWinAttrib();
							seatHuPaiWinAttrib.seatIndex = seat.seatIndex;
							seatHuPaiWinAttrib.tangCardState = seat.tangCardState;
							winSeat.huPaiWinList.add(seatHuPaiWinAttrib);
							sLog += ","+seat.seatIndex;
						}
					}
					winSeat.statistAttrib.totalZimo++;
					sLog += "胡牌顺序" + hupaiIndex;
					table.logicManager.log(sLog);
				}else if(winSeat.gangType == GameDefine.GANG_TYPE_DIANGANG){
					//点杠上花
					winSeat.huPaiType = GameDefine.HUPAI_TYPE_DIANGANGFLOW;
					if(table.ruleAttrib.bDianGangHuaAll){
						String sLog = "桌子轮到中断表态,表态座位=[" + winSeat.seatIndex + "]点炮杠上花算3家=";
						sLog += GameDefine.cards2String(winSeat.huCards);
						sLog += "赢取座位=";
						
						for(SeatAttrib seat : table.seats){
							if(seat.huPaiIndex ==0){
								SeatHuPaiWinAttrib seatHuPaiWinAttrib = new SeatHuPaiWinAttrib();
								seatHuPaiWinAttrib.seatIndex = seat.seatIndex;
								seatHuPaiWinAttrib.tangCardState = seat.tangCardState;
								winSeat.huPaiWinList.add(seatHuPaiWinAttrib);
								sLog += ""+seat.seatIndex;
							}
						}
						sLog += "胡牌顺序" + hupaiIndex;
						table.logicManager.log(sLog);
						winSeat.statistAttrib.totalZimo++;
					}else{
						SeatAttrib lostSeat = table.seats.get(winSeat.gangSeatIndex);
						SeatHuPaiWinAttrib seatHuPaiWinAttrib = new SeatHuPaiWinAttrib();
						seatHuPaiWinAttrib.seatIndex = lostSeat.seatIndex;
						seatHuPaiWinAttrib.tangCardState = lostSeat.tangCardState;
						winSeat.huPaiWinList.add(seatHuPaiWinAttrib);
						
						lostSeat.statistAttrib.totalMyDianPao++;
						winSeat.statistAttrib.totalDianPaoHu++;
						String sLog = "桌子轮到中断表态,表态座位=[" + winSeat.seatIndex + "]点炮杠上花算1家=";
						sLog += GameDefine.cards2String(winSeat.huCards);
						sLog += "赢取座位="+winSeat.gangSeatIndex;
						sLog += "胡牌顺序" + hupaiIndex;
						table.logicManager.log(sLog);
					}
				}else{
					//普通自摸
					winSeat.huPaiType = GameDefine.HUPAI_TYPE_ZIMO;
					String sLog = "桌子轮到中断表态,表态座位=[" + winSeat.seatIndex + "]普通自摸算3家=";
					sLog += GameDefine.cards2String(winSeat.huCards);
					sLog += "赢取座位=";
					
					for(SeatAttrib seat : table.seats){
						if(seat.huPaiType == GameDefine.HUPAI_TYPE_NONE
								&& seat.seatIndex != winSeat.seatIndex){
							SeatHuPaiWinAttrib seatHuPaiWinAttrib = new SeatHuPaiWinAttrib();
							seatHuPaiWinAttrib.seatIndex = seat.seatIndex;
							seatHuPaiWinAttrib.tangCardState = seat.tangCardState;
							
							winSeat.huPaiWinList.add(seatHuPaiWinAttrib);
							sLog += ""+seat.seatIndex;
						}
					}
					winSeat.statistAttrib.totalZimo++;
					sLog += "胡牌顺序" + hupaiIndex;
					table.logicManager.log(sLog);
				}
				//分析胡的牌型
				//CardAnalysisManager.analysisHuPaiStyle(this, winSeat, btSeat.breakCard);
				tmpBtIndex = table.getNextUnHuSeatIndex(tmpBtIndex);
			}else if(table.breakStateSource == GameDefine.STATE_TABLE_OUTCARD
					|| table.breakStateSource == GameDefine.STATE_TABLE_BREAKCARD){
				String sLog = "桌子轮到中断表态玩家,点炮座位="+table.btIndex+"胡牌顺序"+hupaiIndex;
				//点炮
				for(SeatAttrib winSeat : breakBtSeats){
					sLog += "接炮座位=" + winSeat.seatIndex;
					
					winSeat.huPaiIndex = hupaiIndex;
					
					SeatHuPaiWinAttrib seatHuPaiWinAttrib = new SeatHuPaiWinAttrib();
					seatHuPaiWinAttrib.seatIndex = btSeat.seatIndex;
					seatHuPaiWinAttrib.tangCardState = btSeat.tangCardState;
					winSeat.huPaiWinList.add(seatHuPaiWinAttrib);
					if(table.breakStateSource == GameDefine.STATE_TABLE_BREAKCARD){
						//是抢杠胡
						winSeat.huPaiType = GameDefine.HUPAI_TYPE_QIANGGANG;
						winSeat.huCards.add(btSeat.breakCard);
						sLog += "胡牌方式=抢杠胡,胡牌="+btSeat.breakCard.toString();
						//从巴杠中去除被抢杠胡的牌
						int findI = -1;
						for(int tmpI=0; tmpI<btSeat.baGangCards.size(); tmpI++){
							List<CardAttrib> baGangCardList = btSeat.baGangCards.get(tmpI);
							for(CardAttrib card : baGangCardList){
								if(card.suit == btSeat.breakCard.suit
										&& card.point == btSeat.breakCard.point){
									findI = tmpI;
									break;
								}
							}
							if(findI >= 0){
								baGangCardList.remove(0);
								btSeat.pengCards.add(baGangCardList);
								btSeat.baGangCards.remove(findI);
								break;
							}
						}
					}else{
						winSeat.huCards.add(btSeat.breakCard);
						if(btSeat.gangType != GameDefine.GANG_TYPE_NONE){
							//杠上炮
							winSeat.huPaiType = GameDefine.HUPAI_TYPE_DIANGANGPAO;
							sLog += "胡牌方式=杠上炮,胡牌="+btSeat.breakCard.toString();
							
							if(table.ruleAttrib.bGuaFengXiaYu && table.ruleAttrib.bZhuanYu){
								SeatHuJiaoZhuYiAttrib seatHuJiaoZhuYiAttrib = new SeatHuJiaoZhuYiAttrib();
								seatHuJiaoZhuYiAttrib.seatIndex = winSeat.seatIndex;
								//记录转雨数据
								switch(btSeat.gangType){
								case GameDefine.GANG_TYPE_SELF_BAGANG://巴杠
									if(btSeat.baGangWinList.size() > 0){
										int n = btSeat.baGangWinList.get(btSeat.baGangWinList.size()-1).size();
										seatHuJiaoZhuYiAttrib.score = table.ruleAttrib.baseScore*n;
									}
									break;
								case GameDefine.GANG_TYPE_SELF_ANGANG://暗杠
									if(btSeat.anGangWinList.size() > 0){
										int n = btSeat.anGangWinList.get(btSeat.anGangWinList.size()-1).size();
										seatHuJiaoZhuYiAttrib.score = (table.ruleAttrib.baseScore*2)*n;
									}
									break;
								case GameDefine.GANG_TYPE_DIANGANG://点杠
									if(btSeat.dianGangWinList.size() > 0){
										seatHuJiaoZhuYiAttrib.score = (table.ruleAttrib.baseScore*2)*1;
										if(btSeat.dianGangJiaJiaYouWinList.size() > 0){
											int n = btSeat.dianGangJiaJiaYouWinList.get(btSeat.dianGangJiaJiaYouWinList.size()-1).size();
											seatHuJiaoZhuYiAttrib.score += table.ruleAttrib.baseScore*n;
										}
									}
									break;
								}
								btSeat.zhuanYuSeats.add(seatHuJiaoZhuYiAttrib);
							}
						}else{
							//普通点炮
							winSeat.huPaiType = GameDefine.HUPAI_TYPE_DIANPAO;
							sLog += "胡牌方式=普通点炮,胡牌="+btSeat.breakCard.toString();
						}
						GameDefine.removeOnceBySuitPoint(btSeat.outUnUseCards, btSeat.breakCard);
					}
					winSeat.statistAttrib.totalDianPaoHu++;
					//分析胡的牌型
					//CardAnalysisManager.analysisHuPaiStyle(this, winSeat, btSeat.breakCard);
				}
				table.logicManager.log(sLog);
				btSeat.statistAttrib.totalMyDianPao++;
				//计算摸牌玩家,胡了牌的下一个未胡牌座位
				tmpBtIndex = tmpBtIndex - 1;
				if(tmpBtIndex < 0){
					tmpBtIndex = table.seats.size()-1;
				}
				while(true){
					boolean bfind = false;
					for(SeatAttrib seatAttrib : breakBtSeats){
						if(seatAttrib.seatIndex == tmpBtIndex){
							bfind = true;
							break;
						}
					}
					if(bfind){
						break;
					}
					tmpBtIndex = tmpBtIndex - 1;
					if(tmpBtIndex < 0){
						tmpBtIndex = table.seats.size()-1;
					}
				}
				tmpBtIndex = table.getNextUnHuSeatIndex(tmpBtIndex);
			}
			tmpGameState = GameDefine.STATE_TABLE_MOPAI;
		}else if(minBt == GameDefine.ACT_INDEX_GANG){
			//杠,只能有一家
			SeatAttrib winSeat = breakBtSeats.get(0);
			String sLog = "桌子轮到中断表态玩家,开杠座位="+winSeat.seatIndex;
			if(table.breakStateSource == GameDefine.STATE_TABLE_MOPAI){
				//将摸到的牌加到手牌中
				winSeat.handCards.add(winSeat.moPaiCard);
				
				List<CardAttrib> chkList = new ArrayList<>();
				for(List<CardAttrib> tmpTheList : winSeat.pengCards){
					chkList.addAll(tmpTheList);
				}
				chkList.add(btSeat.breakCard);
				List<List<CardAttrib>> findFour = GameDefine.findFourCards(chkList);				
				if(findFour.isEmpty() == false){
					//是巴杠,要检查是否有人抢杠胡
					int breakCardCnt = 0;
					table.resetBreakCardState();
					String breakSeatIndex = "";
					for(SeatAttrib seat : table.seats){
						if(seat.huPaiIndex != 0 || seat.seatIndex == table.btIndex){
							//已经胡了牌的和杠牌的玩家自已排除
							continue;
						}
						GameDefine.AnalysisBreakCard(table, seat.seatIndex, table.btIndex, GameDefine.STATE_TABLE_BREAKCARD);
						int state = seat.breakCardState[GameDefine.ACT_INDEX_HU];
						seat.resetBreakState();
						seat.breakCardState[GameDefine.ACT_INDEX_HU] = state;
						seat.breakCardState[GameDefine.ACT_INDEX_DROP] = state;
						if(state > 0){
							seat.breakCard = btSeat.breakCard;
							breakSeatIndex += ""+seat.seatIndex;
						}
						breakCardCnt += state;
					}
					//从手牌中移除表态牌
					GameDefine.removeOnceBySuitPoint(winSeat.handCards, btSeat.breakCard);
					btSeat.moPaiCard = null;
					//添加到杠牌列表
					winSeat.baGangCards.add(findFour.get(0));
					//删除碰牌列表中碰的牌
					int findI = -1;
					for(int tmpI=0; tmpI<winSeat.pengCards.size(); tmpI++){
						List<CardAttrib> pengCardList = winSeat.pengCards.get(tmpI);
						for(CardAttrib pengCard : pengCardList){
							if(pengCard.suit == btSeat.breakCard.suit
									&& pengCard.point == btSeat.breakCard.point){
								findI = tmpI;
								break;
							}
						}
						if(findI != -1){
							break;
						}
					}
					if(findI >= 0){
						winSeat.pengCards.remove(findI);
					}
					
					if(breakCardCnt > 0){
						//需要抢杠胡表态
						//发送胡杠碰吃通知数据
						List<SeatAttrib> tmpBreakSeats = new ArrayList<SeatAttrib>();
						tmpBreakSeats.add(table.seats.get(table.btIndex));
						int theBt = GameDefine.ACT_INDEX_GANG;
						
						table.sendBreakCardNotify(tmpBreakSeats, theBt);
						table.logicManager.log("桌子轮到中断表态玩家,座位="+winSeat.seatIndex + "中断座位="+breakSeatIndex);
						table.gameState = GameDefine.STATE_TABLE_WAIT_EX;
						table.waitTime = currTime + GameDefine.TIME_TABLE_WAIT_EX;
						table.nextGameState = GameDefine.STATE_TABLE_BREAKCARD;
						table.breakStateSource = GameDefine.STATE_TABLE_BREAKCARD;						
						return;
					}else{
						sLog += "杠牌="+btSeat.breakCard.toString();
						sLog += ",巴杠,赢座位=";
						//没人抢杠
						winSeat.gangType = GameDefine.GANG_TYPE_SELF_BAGANG;
						winSeat.gangSeatIndex = winSeat.seatIndex;
						List<Integer> gangWinList = new ArrayList<>();
						for(SeatAttrib seat : table.seats){
							if(seat.huPaiIndex != 0 || seat.seatIndex == table.btIndex){
								continue;
							}
							gangWinList.add(seat.seatIndex);
							sLog += ""+seat.seatIndex;
						}
						table.logicManager.log(sLog);
						if(table.ruleAttrib.bGuaFengXiaYu){
							winSeat.baGangWinList.add(gangWinList);
						}
						winSeat.statistAttrib.totalBagang++;
					}
				}else{
					//是暗杠
					sLog += "杠牌="+btSeat.breakCard.toString();
					sLog += ",暗杠,赢座位=";
					winSeat.gangType = GameDefine.GANG_TYPE_SELF_ANGANG;
					winSeat.gangSeatIndex = winSeat.seatIndex;
					List<Integer> gangWinList = new ArrayList<>();
					for(SeatAttrib seat : table.seats){
						if(seat.huPaiType != GameDefine.HUPAI_TYPE_NONE
								|| seat.seatIndex == winSeat.seatIndex){
							//自已和胡了牌的不算杠钱
							continue;
						}
						gangWinList.add(seat.seatIndex);
						sLog += ""+seat.seatIndex;
					}
					table.logicManager.log(sLog);
					
					if(table.ruleAttrib.bGuaFengXiaYu){
						winSeat.anGangWinList.add(gangWinList);
					}
					winSeat.statistAttrib.totalAngang++;
										
					List<CardAttrib> findCardList = GameDefine.findAllBySuitPoint(winSeat.handCards, 
							btSeat.breakCard.suit, btSeat.breakCard.point);
					GameDefine.removeAllBySuitPoint(winSeat.handCards, findCardList);
					winSeat.anGangCards.add(findCardList);
				}
				winSeat.moPaiCard = null;
				tmpGameState = GameDefine.STATE_TABLE_MOPAI;
			}else if(table.breakStateSource == GameDefine.STATE_TABLE_OUTCARD){
				//点杠
				winSeat.gangType = GameDefine.GANG_TYPE_DIANGANG;
				winSeat.gangSeatIndex = table.btIndex;
				if(table.ruleAttrib.bGuaFengXiaYu){
					winSeat.dianGangWinList.add(table.btIndex);
					if(table.ruleAttrib.bJiaJiaYou){
						//点杠家家有
						List<Integer> gangWinList = new ArrayList<>();
						for(SeatAttrib lostSeat : table.seats){
							if(lostSeat.huPaiType != GameDefine.HUPAI_TYPE_NONE
									|| lostSeat.seatIndex == winSeat.seatIndex
									|| lostSeat.seatIndex == table.btIndex){
								//自已和胡了牌的,点杠的不算杠钱
								continue;
							}
							gangWinList.add(lostSeat.seatIndex);
						}
						if(gangWinList.isEmpty() == false){
							winSeat.dianGangJiaJiaYouWinList.add(gangWinList);
						}
					}
				}
				winSeat.handCards.add(btSeat.breakCard);
				List<CardAttrib> cardList = GameDefine.findAllBySuitPoint(winSeat.handCards, 
						btSeat.breakCard.suit, btSeat.breakCard.point);
				GameDefine.removeAllBySuitPoint(winSeat.handCards, cardList);
				winSeat.dianGangCards.add(cardList);
				
				sLog += "杠牌="+btSeat.breakCard.toString();
				sLog += ",点杠,赢座位="+btSeat.seatIndex;
				table.logicManager.log(sLog);
				
				winSeat.statistAttrib.totalBagang++;
				tmpBtIndex = winSeat.seatIndex;
				
				GameDefine.removeOnceBySuitPoint(btSeat.outUnUseCards, btSeat.breakCard);
			}			
			tmpGameState = GameDefine.STATE_TABLE_MOPAI;
		}else if(minBt == GameDefine.ACT_INDEX_PENG){
			//碰
			SeatAttrib seatAttrib = breakBtSeats.get(0);
			List<CardAttrib> cardList = GameDefine.findAllBySuitPoint(seatAttrib.handCards, 
					btSeat.breakCard.suit, btSeat.breakCard.point);
			List<CardAttrib> findCards = cardList.subList(0, 2);
			GameDefine.removeAllBySuitPoint(seatAttrib.handCards, findCards);
			List<CardAttrib> pengpengList = new ArrayList<>();
			pengpengList.addAll(findCards);
			pengpengList.add(btSeat.breakCard);
			
			seatAttrib.pengCards.add(pengpengList);
			
			String sLog = "桌子轮到中断表态玩家,碰牌座位="+seatAttrib.seatIndex;
			sLog += "碰的牌="+btSeat.breakCard.toString()+"打牌座位="+btSeat.seatIndex;
			table.logicManager.log(sLog);
			
			GameDefine.removeOnceBySuitPoint(btSeat.outUnUseCards, btSeat.breakCard);
			
			int tangChkNum = 8;
			if(table.cfgId == 8){
				//南充麻将
				tangChkNum = 12;
			}
			boolean bchkTing = false;
			if(table.ruleAttrib.bTangPai
					&& seatAttrib.tangCardState <= 0 
					&& table.tableCards.size() > tangChkNum){
				int unHuiPlayer = 100;
				if(table.ruleAttrib.bLiangJiaBuTang){
					//两家不躺
					unHuiPlayer = 0;
					for(int i=0; i<table.seats.size(); i++){
						SeatAttrib theSeat = table.seats.get(i);
						if(theSeat.huPaiType == GameDefine.HUPAI_TYPE_NONE){
							unHuiPlayer++;
						}
					}
				}
				if(unHuiPlayer > 2){
					List<CardAttrib> canAllOutCards = new ArrayList<>();
					
					for(int tmpIndex=0; tmpIndex<seatAttrib.handCards.size(); tmpIndex++){
						List<CardAttrib> tmpchkList = new ArrayList<>();
						tmpchkList.addAll(seatAttrib.handCards);
						tmpchkList.remove(tmpIndex);
						List<CardAttrib> tingpaiCards = GameDefine.isTingPai(table, tmpchkList, seatAttrib);
						if(null != tingpaiCards && tingpaiCards.isEmpty() == false){
							CardAttrib theCard = seatAttrib.handCards.get(tmpIndex);
							CardAttrib findTheCard = GameDefine.findOnceBySuitPoint(canAllOutCards, theCard.suit, theCard.point);
							if(null == findTheCard){
								canAllOutCards.add(theCard);
							}
						}
					}
					for(SeatAttrib theSeat : table.seats){
						if(seatAttrib.seatIndex == theSeat.seatIndex || theSeat.tangCardState <= 0){
							continue;
						}
						
						for(CardAttrib card : theSeat.tangCanHuList){
							GameDefine.removeOnceBySuitPoint(canAllOutCards, card);
						}
					}
					
					if(canAllOutCards.isEmpty() == false){
						bchkTing = true;
					}
				}
			}
			
			tmpGameState = GameDefine.STATE_TABLE_OUTCARD;
			if(bchkTing){
				seatAttrib.resetBreakState();
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_TANG] = 1;
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_DROP] = 1;
				seatAttrib.tangSource = 2;
				tmpGameState = GameDefine.STATE_TABLE_BREAKCARD;
				table.breakStateSource = GameDefine.STATE_TABLE_MOPAI;
			}
			tmpBtIndex = seatAttrib.seatIndex;
		}else if(minBt == GameDefine.ACT_INDEX_TANG){
			SeatAttrib seatAttrib = breakBtSeats.get(0);
			tmpGameState = GameDefine.STATE_TABLE_OUTCARD;
			tmpBtIndex = seatAttrib.seatIndex;
			
			if(1 == seatAttrib.tangSource){
				if(null != seatAttrib.moPaiCard)
				seatAttrib.handCards.add(seatAttrib.moPaiCard);
				seatAttrib.moPaiCard = null;
			}
		}
		//发送胡杠碰吃结果通知数据
		table.sendBreakCardNotify(breakBtSeats, minBt);
		table.bActExec = false;
		table.gameState = GameDefine.STATE_TABLE_WAIT_EX;
		table.waitTime = currTime + GameDefine.TIME_TABLE_WAIT_EX;
		table.nextGameState = tmpGameState;
		table.btIndex = tmpBtIndex;
		breakBtSeats = null;
	}
	
	private static void run_breakcard_other(long currTime, TableAttrib table){		
		int unBt = GameDefine.ACT_INDEX_VAILD;
		int useBt = GameDefine.ACT_INDEX_VAILD;
		int minBt = GameDefine.ACT_INDEX_VAILD;
		
		for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
			SeatAttrib seatAttrib = table.seats.get(seatIndex);
			if(GameDefine.ACT_STATE_WAIT == seatAttrib.btState){
				//此座位还未表态,查找此座位能表态的最高表态行为
				for(int i=0; i<GameDefine.ACT_INDEX_VAILD; i++){
					if(seatAttrib.breakCardState[i] > 0){
						if(i<unBt){
							unBt = i;	
						}
					}
				}
			}else{
				//此座位已表态,查找当前所有表态的最高表态行为
				if(useBt > seatAttrib.breakBtState){
					useBt = seatAttrib.breakBtState;
				}
			}
		}
		if(unBt <= useBt){
			if(unBt < useBt){
				//没有表态的玩家有优先级更高的表态,必须等到此玩家表态
				return;
			}else{
				if(unBt == GameDefine.ACT_INDEX_GANG
						|| unBt == GameDefine.ACT_INDEX_PENG){
					//未表态的玩家和已表态玩家有相同的表态状态,查找座位优先级最高的玩家
					SeatAttrib findSeatAttrib = null;
					int findSeatIndex = table.btIndex;
					while(true){
						SeatAttrib seat = table.seats.get(findSeatIndex);
						if((seat.breakCardState[unBt]>0 && seat.btState != GameDefine.ACT_STATE_DROP)
								|| (seat.breakCardState[unBt]>0 && seat.btState == GameDefine.ACT_STATE_BT
								&& seat.breakBtState < GameDefine.ACT_INDEX_DROP)){
							findSeatAttrib = seat;
							break;
						}
						
						findSeatIndex = table.getNextUnHuSeatIndex(seat.seatIndex);
					}
					if(findSeatAttrib.btState == GameDefine.ACT_STATE_WAIT){
						//优先级高的玩家还没有表态,等待表态
						return;
					}
				}else{
					return;
				}
			}
		}
		SeatAttrib btSeat = table.seats.get(table.btIndex);
		
		minBt = useBt;
		List<SeatAttrib> breakBtSeats = new ArrayList<SeatAttrib>();
		for(SeatAttrib seatAttrib : table.seats){
			if(GameDefine.ACT_STATE_BT == seatAttrib.btState){
				if(seatAttrib.breakBtState == minBt){
					breakBtSeats.add(seatAttrib);
				}else if(seatAttrib.breakBtState == GameDefine.ACT_INDEX_DROP){
					if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
						//除自摸外的有胡不胡点过,加入过手牌限制
						if(table.breakStateSource == GameDefine.STATE_TABLE_OUTCARD
								|| table.breakStateSource == GameDefine.STATE_TABLE_BREAKCARD){
							//别人点炮不胡
							seatAttrib.bDianPaoNoHu = true;
							seatAttrib.guoShouHuCardList.add(btSeat.breakCard);
						}
					}
				}
			}else if(GameDefine.ACT_STATE_DROP == seatAttrib.btState){
				if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
					//除自摸外的有胡不胡点过,加入过手牌限制
					if(table.breakStateSource == GameDefine.STATE_TABLE_OUTCARD
							|| table.breakStateSource == GameDefine.STATE_TABLE_BREAKCARD){
						//别人点炮不胡
						seatAttrib.bDianPaoNoHu = true;
						seatAttrib.guoShouHuCardList.add(btSeat.breakCard);
					}
				}
			}
		}
		if(minBt == GameDefine.ACT_INDEX_GANG
						|| minBt == GameDefine.ACT_INDEX_PENG){
			//碰杠只能有一人
			if(breakBtSeats.size() > 1){
				int findSeatIndex = table.btIndex;
				List<Integer> breakSeatIndexs = new ArrayList<>();
				for(SeatAttrib breakBtSeat : breakBtSeats){
					breakSeatIndexs.add(breakBtSeat.seatIndex);
				}
				breakBtSeats.clear();
				while(true){
					if(breakSeatIndexs.contains(findSeatIndex)){
						breakBtSeats.add(table.seats.get(findSeatIndex));
						break;
					}
					
					findSeatIndex = table.getNextUnHuSeatIndex(findSeatIndex);
				}
			}
		}
		
		table.bActExec = false;
		if(breakBtSeats.isEmpty()){
			table.logicManager.log("桌子轮到中断表态,所有人都选择了过");
			btSeat.gangType = GameDefine.GANG_TYPE_NONE;
			btSeat.gangSeatIndex = 0;
			//都选择了过,不中断原来的进程
			if(table.breakStateSource == GameDefine.STATE_TABLE_MOPAI){
				table.gameState = GameDefine.STATE_TABLE_OUTCARD;
			}else if(table.breakStateSource == GameDefine.STATE_TABLE_OUTCARD){
				//打出的牌没人要,下一玩家摸牌
				btSeat.gangSeatIndex = 0;
				table.btIndex = table.getNextUnHuSeatIndex(btSeat.seatIndex);
				table.gameState = GameDefine.STATE_TABLE_MOPAI;
			}else if(table.breakStateSource == GameDefine.STATE_TABLE_BREAKCARD){
				//巴杠,居然不抢杠胡,呵呵不好意思了
				//记录收巴杠钱
				List<Integer> winSeats = new ArrayList<>();
				for(SeatAttrib seat : table.seats){
					if(seat.huPaiType != GameDefine.HUPAI_TYPE_NONE
							|| seat.seatIndex == btSeat.seatIndex){
						continue;
					}
					winSeats.add(seat.seatIndex);
				}
				
				if(table.ruleAttrib.bGuaFengXiaYu){
					btSeat.baGangWinList.add(winSeats);
				}
				btSeat.statistAttrib.totalBagang++;
				btSeat.gangType = GameDefine.GANG_TYPE_SELF_BAGANG;
				
				//杠了继续摸牌
				table.gameState = GameDefine.STATE_TABLE_WAIT_EX;
				table.waitTime = currTime + GameDefine.TIME_TABLE_WAIT_EX;
				table.nextGameState = GameDefine.STATE_TABLE_MOPAI;
			}
			breakBtSeats = null;
			return;
		}
		table.logicManager.log("桌子轮到中断表态,表态有玩家总共数=" + breakBtSeats.size());
		int tmpGameState = table.gameState;
		int tmpBtIndex = table.btIndex;
		if(minBt != GameDefine.ACT_INDEX_HU && minBt != GameDefine.ACT_INDEX_TANG){
			btSeat.gangType = GameDefine.GANG_TYPE_NONE;
		}
		if(minBt == GameDefine.ACT_INDEX_HU){
			//胡了			
			int hupaiIndex = table.getHuPaiIndex() + 1;
			if(table.breakStateSource == GameDefine.STATE_TABLE_MOPAI){
				//自摸
				SeatAttrib winSeat = breakBtSeats.get(0);
				winSeat.huPaiIndex = hupaiIndex;
				winSeat.huCards.add(btSeat.breakCard);
				winSeat.moPaiCard = null;
				if(winSeat.gangType == GameDefine.GANG_TYPE_SELF_BAGANG
						|| winSeat.gangType == GameDefine.GANG_TYPE_SELF_ANGANG){
					//自摸杠上花,要赢所有未胡牌的玩家
					String sLog = "桌子轮到中断表态,表态座位=[" + winSeat.seatIndex + "]自摸杠上花=";
					sLog += GameDefine.cards2String(winSeat.huCards);
					sLog += "赢取座位=";
					
					winSeat.huPaiType = GameDefine.HUPAI_TYPE_GANGFLOW;
					for(SeatAttrib seat : table.seats){
						if(seat.huPaiIndex ==0){
							SeatHuPaiWinAttrib seatHuPaiWinAttrib = new SeatHuPaiWinAttrib();
							seatHuPaiWinAttrib.seatIndex = seat.seatIndex;
							seatHuPaiWinAttrib.tangCardState = seat.tangCardState;
							winSeat.huPaiWinList.add(seatHuPaiWinAttrib);
							sLog += ","+seat.seatIndex;
						}
					}
					winSeat.statistAttrib.totalZimo++;
					sLog += "胡牌顺序" + hupaiIndex;
					table.logicManager.log(sLog);
				}else if(winSeat.gangType == GameDefine.GANG_TYPE_DIANGANG){
					//点杠上花
					winSeat.huPaiType = GameDefine.HUPAI_TYPE_DIANGANGFLOW;
					if(table.ruleAttrib.bDianGangHuaAll){
						String sLog = "桌子轮到中断表态,表态座位=[" + winSeat.seatIndex + "]点炮杠上花算3家=";
						sLog += GameDefine.cards2String(winSeat.huCards);
						sLog += "赢取座位=";
						
						for(SeatAttrib seat : table.seats){
							if(seat.huPaiIndex ==0){
								SeatHuPaiWinAttrib seatHuPaiWinAttrib = new SeatHuPaiWinAttrib();
								seatHuPaiWinAttrib.seatIndex = seat.seatIndex;
								seatHuPaiWinAttrib.tangCardState = seat.tangCardState;
								winSeat.huPaiWinList.add(seatHuPaiWinAttrib);
								sLog += ""+seat.seatIndex;
							}
						}
						sLog += "胡牌顺序" + hupaiIndex;
						table.logicManager.log(sLog);
						winSeat.statistAttrib.totalZimo++;
					}else{
						SeatAttrib lostSeat = table.seats.get(winSeat.gangSeatIndex);
						SeatHuPaiWinAttrib seatHuPaiWinAttrib = new SeatHuPaiWinAttrib();
						seatHuPaiWinAttrib.seatIndex = lostSeat.seatIndex;
						seatHuPaiWinAttrib.tangCardState = lostSeat.tangCardState;
						winSeat.huPaiWinList.add(seatHuPaiWinAttrib);
						
						lostSeat.statistAttrib.totalMyDianPao++;
						winSeat.statistAttrib.totalDianPaoHu++;
						String sLog = "桌子轮到中断表态,表态座位=[" + winSeat.seatIndex + "]点炮杠上花算1家=";
						sLog += GameDefine.cards2String(winSeat.huCards);
						sLog += "赢取座位="+winSeat.gangSeatIndex;
						sLog += "胡牌顺序" + hupaiIndex;
						table.logicManager.log(sLog);
					}
				}else{
					//普通自摸
					winSeat.huPaiType = GameDefine.HUPAI_TYPE_ZIMO;
					String sLog = "桌子轮到中断表态,表态座位=[" + winSeat.seatIndex + "]普通自摸算3家=";
					sLog += GameDefine.cards2String(winSeat.huCards);
					sLog += "赢取座位=";
					
					for(SeatAttrib seat : table.seats){
						if(seat.huPaiType == GameDefine.HUPAI_TYPE_NONE
								&& seat.seatIndex != winSeat.seatIndex){
							SeatHuPaiWinAttrib seatHuPaiWinAttrib = new SeatHuPaiWinAttrib();
							seatHuPaiWinAttrib.seatIndex = seat.seatIndex;
							seatHuPaiWinAttrib.tangCardState = seat.tangCardState;
							
							winSeat.huPaiWinList.add(seatHuPaiWinAttrib);
							sLog += ""+seat.seatIndex;
						}
					}
					winSeat.statistAttrib.totalZimo++;
					sLog += "胡牌顺序" + hupaiIndex;
					table.logicManager.log(sLog);
				}
				//分析胡的牌型
				//CardAnalysisManager.analysisHuPaiStyle(this, winSeat, btSeat.breakCard);
				tmpBtIndex = table.getNextUnHuSeatIndex(tmpBtIndex);
			}else if(table.breakStateSource == GameDefine.STATE_TABLE_OUTCARD
					|| table.breakStateSource == GameDefine.STATE_TABLE_BREAKCARD){
				String sLog = "桌子轮到中断表态玩家,点炮座位="+table.btIndex+"胡牌顺序"+hupaiIndex;
				//点炮
				for(SeatAttrib winSeat : breakBtSeats){
					sLog += "接炮座位=" + winSeat.seatIndex;
					
					winSeat.huPaiIndex = hupaiIndex;
					
					SeatHuPaiWinAttrib seatHuPaiWinAttrib = new SeatHuPaiWinAttrib();
					seatHuPaiWinAttrib.seatIndex = btSeat.seatIndex;
					seatHuPaiWinAttrib.tangCardState = btSeat.tangCardState;
					winSeat.huPaiWinList.add(seatHuPaiWinAttrib);
					if(table.breakStateSource == GameDefine.STATE_TABLE_BREAKCARD){
						//是抢杠胡
						winSeat.huPaiType = GameDefine.HUPAI_TYPE_QIANGGANG;
						winSeat.huCards.add(btSeat.breakCard);
						sLog += "胡牌方式=抢杠胡,胡牌="+btSeat.breakCard.toString();
						//从巴杠列表中找到杠牌
						for(int tmpI=0; tmpI<btSeat.baGangCards.size(); tmpI++){
							List<CardAttrib> baGangCardList = btSeat.baGangCards.get(tmpI);
							if(baGangCardList.get(0).suit == btSeat.breakCard.suit
									&& baGangCardList.get(0).point == btSeat.breakCard.point){
								btSeat.baGangCards.remove(tmpI);
								
								baGangCardList.remove(0);
								btSeat.pengCards.add(baGangCardList);
								break;
							}
						}
					}else{
						winSeat.huCards.add(btSeat.breakCard);
						if(btSeat.gangType != GameDefine.GANG_TYPE_NONE){
							//杠上炮
							winSeat.huPaiType = GameDefine.HUPAI_TYPE_DIANGANGPAO;
							sLog += "胡牌方式=杠上炮,胡牌="+btSeat.breakCard.toString();
							
							if(table.ruleAttrib.bGuaFengXiaYu && table.ruleAttrib.bZhuanYu){
								SeatHuJiaoZhuYiAttrib seatHuJiaoZhuYiAttrib = new SeatHuJiaoZhuYiAttrib();
								seatHuJiaoZhuYiAttrib.seatIndex = winSeat.seatIndex;
								//记录转雨数据
								switch(btSeat.gangType){
								case GameDefine.GANG_TYPE_SELF_BAGANG://巴杠
									if(btSeat.baGangWinList.size() > 0){
										int n = btSeat.baGangWinList.get(btSeat.baGangWinList.size()-1).size();
										seatHuJiaoZhuYiAttrib.score = table.ruleAttrib.baseScore*n;
									}
									break;
								case GameDefine.GANG_TYPE_SELF_ANGANG://暗杠
									if(btSeat.anGangWinList.size() > 0){
										int n = btSeat.anGangWinList.get(btSeat.anGangWinList.size()-1).size();
										seatHuJiaoZhuYiAttrib.score = (table.ruleAttrib.baseScore*2)*n;
									}
									break;
								case GameDefine.GANG_TYPE_DIANGANG://点杠
									if(btSeat.dianGangWinList.size() > 0){
										seatHuJiaoZhuYiAttrib.score = (table.ruleAttrib.baseScore*2)*1;
										if(btSeat.dianGangJiaJiaYouWinList.size() > 0){
											int n = btSeat.dianGangJiaJiaYouWinList.get(btSeat.dianGangJiaJiaYouWinList.size()-1).size();
											seatHuJiaoZhuYiAttrib.score += table.ruleAttrib.baseScore*n;
										}
									}
									break;
								}
								btSeat.zhuanYuSeats.add(seatHuJiaoZhuYiAttrib);
							}
						}else{
							//普通点炮
							winSeat.huPaiType = GameDefine.HUPAI_TYPE_DIANPAO;
							sLog += "胡牌方式=普通点炮,胡牌="+btSeat.breakCard.toString();
						}
						GameDefine.removeOnceBySuitPoint(btSeat.outUnUseCards, btSeat.breakCard);
					}
					winSeat.statistAttrib.totalDianPaoHu++;
					//分析胡的牌型
					//CardAnalysisManager.analysisHuPaiStyle(this, winSeat, btSeat.breakCard);
				}
				table.logicManager.log(sLog);
				btSeat.statistAttrib.totalMyDianPao++;
				//计算摸牌玩家,胡了牌的下一个未胡牌座位
				tmpBtIndex = tmpBtIndex - 1;
				if(tmpBtIndex < 0){
					tmpBtIndex = table.seats.size()-1;
				}
				while(true){
					boolean bfind = false;
					for(SeatAttrib seatAttrib : breakBtSeats){
						if(seatAttrib.seatIndex == tmpBtIndex){
							bfind = true;
							break;
						}
					}
					if(bfind){
						break;
					}
					tmpBtIndex = tmpBtIndex - 1;
					if(tmpBtIndex < 0){
						tmpBtIndex = table.seats.size()-1;
					}
				}
				tmpBtIndex = table.getNextUnHuSeatIndex(tmpBtIndex);
			}
			tmpGameState = GameDefine.STATE_TABLE_MOPAI;
		}else if(minBt == GameDefine.ACT_INDEX_GANG){
			//杠,只能有一家
			SeatAttrib winSeat = breakBtSeats.get(0);
			String sLog = "桌子轮到中断表态玩家,开杠座位="+winSeat.seatIndex;
			if(table.breakStateSource == GameDefine.STATE_TABLE_MOPAI){
				//将摸到的牌加到手牌中
				winSeat.handCards.add(winSeat.moPaiCard);
				
				List<CardAttrib> chkList = new ArrayList<>();
				for(List<CardAttrib> tmpTheList : winSeat.pengCards){
					chkList.addAll(tmpTheList);
				}
				chkList.add(btSeat.breakCard);
				List<List<CardAttrib>> findFour = GameDefine.findFourCards(chkList);				
				if(findFour.isEmpty() == false){
					//是巴杠,要检查是否有人抢杠胡
					int breakCardCnt = 0;
					table.resetBreakCardState();
					String breakSeatIndex = "";
					for(SeatAttrib seat : table.seats){
						if(seat.huPaiIndex != 0 || seat.seatIndex == table.btIndex){
							//已经胡了牌的和杠牌的玩家自已排除
							continue;
						}
						GameDefine.AnalysisBreakCard(table, seat.seatIndex, table.btIndex, GameDefine.STATE_TABLE_BREAKCARD);
						int state = seat.breakCardState[GameDefine.ACT_INDEX_HU];
						seat.resetBreakState();
						seat.breakCardState[GameDefine.ACT_INDEX_HU] = state;
						seat.breakCardState[GameDefine.ACT_INDEX_DROP] = state;
						if(state > 0){
							seat.breakCard = btSeat.breakCard;
							breakSeatIndex += ""+seat.seatIndex;
						}
						breakCardCnt += state;
					}
					//从手牌中移除表态牌
					GameDefine.removeOnceBySuitPoint(winSeat.handCards, btSeat.breakCard);
					btSeat.moPaiCard = null;
					//添加到杠牌列表
					winSeat.baGangCards.add(findFour.get(0));
					//删除碰牌列表中碰的牌
					int findI = -1;
					for(int tmpI=0; tmpI<winSeat.pengCards.size(); tmpI++){
						List<CardAttrib> pengCardList = winSeat.pengCards.get(tmpI);
						for(CardAttrib pengCard : pengCardList){
							if(pengCard.suit == btSeat.breakCard.suit
									&& pengCard.point == btSeat.breakCard.point){
								findI = tmpI;
								break;
							}
						}
						if(findI != -1){
							break;
						}
					}
					if(findI >= 0){
						winSeat.pengCards.remove(findI);
					}
					
					if(breakCardCnt > 0){
						//需要抢杠胡表态
						//发送胡杠碰吃通知数据
						List<SeatAttrib> tmpBreakSeats = new ArrayList<SeatAttrib>();
						tmpBreakSeats.add(table.seats.get(table.btIndex));
						int theBt = GameDefine.ACT_INDEX_GANG;
						table.sendBreakCardNotify(tmpBreakSeats, theBt);
						table.logicManager.log("桌子轮到中断表态玩家,座位="+winSeat.seatIndex + "中断座位="+breakSeatIndex);
						table.gameState = GameDefine.STATE_TABLE_WAIT_EX;
						table.waitTime = currTime + GameDefine.TIME_TABLE_WAIT_EX;
						table.nextGameState = GameDefine.STATE_TABLE_BREAKCARD;
						table.breakStateSource = GameDefine.STATE_TABLE_BREAKCARD;						
						return;
					}else{
						sLog += "杠牌="+btSeat.breakCard.toString();
						sLog += ",巴杠,赢座位=";
						//没人抢杠
						winSeat.gangType = GameDefine.GANG_TYPE_SELF_BAGANG;
						winSeat.gangSeatIndex = winSeat.seatIndex;
						List<Integer> gangWinList = new ArrayList<>();
						for(SeatAttrib seat : table.seats){
							if(seat.huPaiIndex != 0 || seat.seatIndex == table.btIndex){
								continue;
							}
							gangWinList.add(seat.seatIndex);
							sLog += ""+seat.seatIndex;
						}
						table.logicManager.log(sLog);
						if(table.ruleAttrib.bGuaFengXiaYu){
							winSeat.baGangWinList.add(gangWinList);
						}
						winSeat.statistAttrib.totalBagang++;
					}
				}else{
					//是暗杠
					sLog += "杠牌="+btSeat.breakCard.toString();
					sLog += ",暗杠,赢座位=";
					winSeat.gangType = GameDefine.GANG_TYPE_SELF_ANGANG;
					winSeat.gangSeatIndex = winSeat.seatIndex;
					List<Integer> gangWinList = new ArrayList<>();
					for(SeatAttrib seat : table.seats){
						if(seat.huPaiType != GameDefine.HUPAI_TYPE_NONE
								|| seat.seatIndex == winSeat.seatIndex){
							//自已和胡了牌的不算杠钱
							continue;
						}
						gangWinList.add(seat.seatIndex);
						sLog += ""+seat.seatIndex;
					}
					table.logicManager.log(sLog);
					
					if(table.ruleAttrib.bGuaFengXiaYu){
						winSeat.anGangWinList.add(gangWinList);
					}
					winSeat.statistAttrib.totalAngang++;
										
					List<CardAttrib> findCardList = GameDefine.findAllBySuitPoint(winSeat.handCards, 
							btSeat.breakCard.suit, btSeat.breakCard.point);
					GameDefine.removeAllBySuitPoint(winSeat.handCards, findCardList);
					winSeat.anGangCards.add(findCardList);
				}
				winSeat.moPaiCard = null;
				tmpGameState = GameDefine.STATE_TABLE_MOPAI;
			}else if(table.breakStateSource == GameDefine.STATE_TABLE_OUTCARD){
				//点杠
				winSeat.gangType = GameDefine.GANG_TYPE_DIANGANG;
				winSeat.gangSeatIndex = table.btIndex;
				if(table.ruleAttrib.bGuaFengXiaYu){
					winSeat.dianGangWinList.add(table.btIndex);
					if(table.ruleAttrib.bJiaJiaYou){
						//点杠家家有
						List<Integer> gangWinList = new ArrayList<>();
						for(SeatAttrib lostSeat : table.seats){
							if(lostSeat.huPaiType != GameDefine.HUPAI_TYPE_NONE
									|| lostSeat.seatIndex == winSeat.seatIndex
									|| lostSeat.seatIndex == table.btIndex){
								//自已和胡了牌的,点杠的不算杠钱
								continue;
							}
							gangWinList.add(lostSeat.seatIndex);
						}
						if(gangWinList.isEmpty() == false){
							winSeat.dianGangJiaJiaYouWinList.add(gangWinList);
						}
					}
				}
				winSeat.handCards.add(btSeat.breakCard);
				List<CardAttrib> cardList = GameDefine.findAllBySuitPoint(winSeat.handCards, 
						btSeat.breakCard.suit, btSeat.breakCard.point);
				GameDefine.removeAllBySuitPoint(winSeat.handCards, cardList);
				winSeat.dianGangCards.add(cardList);
				
				sLog += "杠牌="+btSeat.breakCard.toString();
				sLog += ",点杠,赢座位="+btSeat.seatIndex;
				table.logicManager.log(sLog);
				
				winSeat.statistAttrib.totalBagang++;
				tmpBtIndex = winSeat.seatIndex;
				
				GameDefine.removeOnceBySuitPoint(btSeat.outUnUseCards, btSeat.breakCard);
			}			
			tmpGameState = GameDefine.STATE_TABLE_MOPAI;
		}else if(minBt == GameDefine.ACT_INDEX_PENG){
			//碰
			SeatAttrib seatAttrib = breakBtSeats.get(0);
			List<CardAttrib> cardList = GameDefine.findAllBySuitPoint(seatAttrib.handCards, 
					btSeat.breakCard.suit, btSeat.breakCard.point);
			List<CardAttrib> findCards = cardList.subList(0, 2);
			GameDefine.removeAllBySuitPoint(seatAttrib.handCards, findCards);
			List<CardAttrib> pengpengList = new ArrayList<>();
			pengpengList.addAll(findCards);
			pengpengList.add(btSeat.breakCard);
			
			seatAttrib.pengCards.add(pengpengList);
			
			String sLog = "桌子轮到中断表态玩家,碰牌座位="+seatAttrib.seatIndex;
			sLog += "碰的牌="+btSeat.breakCard.toString()+"打牌座位="+btSeat.seatIndex;
			table.logicManager.log(sLog);
			
			GameDefine.removeOnceBySuitPoint(btSeat.outUnUseCards, btSeat.breakCard);
			
			int tangChkNum = 8;
			if(table.cfgId == 8){
				//南充麻将
				tangChkNum = 12;
			}
			boolean bchkTing = false;
			if(table.ruleAttrib.bTangPai
					&& seatAttrib.tangCardState <= 0 
					&& table.tableCards.size() > tangChkNum){
				int unHuiPlayer = 100;
				if(table.ruleAttrib.bLiangJiaBuTang){
					//两家不躺
					unHuiPlayer = 0;
					for(int i=0; i<table.seats.size(); i++){
						SeatAttrib theSeat = table.seats.get(i);
						if(theSeat.huPaiType == GameDefine.HUPAI_TYPE_NONE){
							unHuiPlayer++;
						}
					}
				}
				if(unHuiPlayer > 2){
					List<CardAttrib> canAllOutCards = new ArrayList<>();
					
					for(int tmpIndex=0; tmpIndex<seatAttrib.handCards.size(); tmpIndex++){
						List<CardAttrib> tmpchkList = new ArrayList<>();
						tmpchkList.addAll(seatAttrib.handCards);
						tmpchkList.remove(tmpIndex);
						List<CardAttrib> tingpaiCards = GameDefine.isTingPai(table, tmpchkList, seatAttrib);
						if(null != tingpaiCards && tingpaiCards.isEmpty() == false){
							CardAttrib theCard = seatAttrib.handCards.get(tmpIndex);
							CardAttrib findTheCard = GameDefine.findOnceBySuitPoint(canAllOutCards, theCard.suit, theCard.point);
							if(null == findTheCard){
								canAllOutCards.add(theCard);
							}
						}
					}
					for(SeatAttrib theSeat : table.seats){
						if(seatAttrib.seatIndex == theSeat.seatIndex || theSeat.tangCardState <= 0){
							continue;
						}
						
						for(CardAttrib card : theSeat.tangCanHuList){
							GameDefine.removeOnceBySuitPoint(canAllOutCards, card);
						}
					}
					
					if(canAllOutCards.isEmpty() == false){
						bchkTing = true;
					}
				}
			}
			
			tmpGameState = GameDefine.STATE_TABLE_OUTCARD;
			if(bchkTing){
				seatAttrib.resetBreakState();
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_TANG] = 1;
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_DROP] = 1;
				seatAttrib.tangSource = 2;
				tmpGameState = GameDefine.STATE_TABLE_BREAKCARD;
				table.breakStateSource = GameDefine.STATE_TABLE_MOPAI;
			}
			tmpBtIndex = seatAttrib.seatIndex;
		}else if(minBt == GameDefine.ACT_INDEX_TANG){
			SeatAttrib seatAttrib = breakBtSeats.get(0);
			tmpGameState = GameDefine.STATE_TABLE_OUTCARD;
			tmpBtIndex = seatAttrib.seatIndex;
			
			if(1 == seatAttrib.tangSource){
				if(null != seatAttrib.moPaiCard)
				seatAttrib.handCards.add(seatAttrib.moPaiCard);
				seatAttrib.moPaiCard = null;
			}
		}
		//发送胡杠碰吃结果通知数据
		table.sendBreakCardNotify(breakBtSeats, minBt);
		table.bActExec = false;
		table.gameState = GameDefine.STATE_TABLE_WAIT_EX;
		table.waitTime = currTime + GameDefine.TIME_TABLE_WAIT_EX;
		table.nextGameState = tmpGameState;
		table.btIndex = tmpBtIndex;
		breakBtSeats = null;
	}
	
	private static void run_breakcard_lsmj(long currTime, TableAttrib table){
		int unBt = GameDefine.ACT_INDEX_VAILD;
		int useBt = GameDefine.ACT_INDEX_VAILD;
		int minBt = GameDefine.ACT_INDEX_VAILD;
		
		for(int seatIndex=0; seatIndex<table.seats.size(); seatIndex++){
			SeatAttrib seatAttrib = table.seats.get(seatIndex);
			if(GameDefine.ACT_STATE_WAIT == seatAttrib.btState){
				//此座位还未表态,查找此座位能表态的最高表态行为
				for(int i=0; i<GameDefine.ACT_INDEX_VAILD; i++){
					if(seatAttrib.breakCardState[i] > 0){
						if(i<unBt){
							unBt = i;	
						}
					}
				}
			}else{
				//此座位已表态,查找当前所有表态的最高表态行为
				if(useBt > seatAttrib.breakBtState){
					useBt = seatAttrib.breakBtState;
				}
			}
		}
		if(unBt <= useBt){
			if(unBt < useBt){
				//没有表态的玩家有优先级更高的表态,必须等到此玩家表态
				return;
			}else{
				if(unBt == GameDefine.ACT_INDEX_GANG
						|| unBt == GameDefine.ACT_INDEX_PENG){
					//未表态的玩家和已表态玩家有相同的表态状态,查找座位优先级最高的玩家
					SeatAttrib findSeatAttrib = null;
					int findSeatIndex = table.btIndex;
					while(true){
						SeatAttrib seat = table.seats.get(findSeatIndex);
						if((seat.breakCardState[unBt]>0 && seat.btState != GameDefine.ACT_STATE_DROP)
								|| (seat.breakCardState[unBt]>0 && seat.btState == GameDefine.ACT_STATE_BT
								&& seat.breakBtState < GameDefine.ACT_INDEX_DROP)){
							findSeatAttrib = seat;
							break;
						}
						
						findSeatIndex = table.getNextUnHuSeatIndex(seat.seatIndex);
					}
					if(findSeatAttrib.btState == GameDefine.ACT_STATE_WAIT){
						//优先级高的玩家还没有表态,等待表态
						return;
					}
				}else{
					return;
				}
			}
		}
		SeatAttrib btSeat = table.seats.get(table.btIndex);
		
		minBt = useBt;
		List<SeatAttrib> breakBtSeats = new ArrayList<SeatAttrib>();
		for(SeatAttrib seatAttrib : table.seats){
			if(GameDefine.ACT_STATE_BT == seatAttrib.btState){
				if(seatAttrib.breakBtState == minBt){
					breakBtSeats.add(seatAttrib);
				}else if(seatAttrib.breakBtState == GameDefine.ACT_INDEX_DROP){
					if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
						//有胡不胡点过,加入过手牌限制
						if(table.breakStateSource == GameDefine.STATE_TABLE_OUTCARD
								|| table.breakStateSource == GameDefine.STATE_TABLE_BREAKCARD){
							//别人点炮不胡
							seatAttrib.bDianPaoNoHu = true;
							seatAttrib.guoShouHuCardList.add(btSeat.breakCard);
						}
					}
				}
			}else if(GameDefine.ACT_STATE_DROP == seatAttrib.btState){
				if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
					//有胡不胡点过,加入过手牌限制
					if(table.breakStateSource == GameDefine.STATE_TABLE_OUTCARD
							|| table.breakStateSource == GameDefine.STATE_TABLE_BREAKCARD){
						//别人点炮不胡
						seatAttrib.bDianPaoNoHu = true;
						seatAttrib.guoShouHuCardList.add(btSeat.breakCard);
					}
				}
			}
		}
		if(minBt == GameDefine.ACT_INDEX_GANG
						|| minBt == GameDefine.ACT_INDEX_PENG){
			//碰杠只能有一人
			if(breakBtSeats.size() > 1){
				int findSeatIndex = table.btIndex;
				List<Integer> breakSeatIndexs = new ArrayList<>();
				for(SeatAttrib breakBtSeat : breakBtSeats){
					breakSeatIndexs.add(breakBtSeat.seatIndex);
				}
				breakBtSeats.clear();
				while(true){
					if(breakSeatIndexs.contains(findSeatIndex)){
						breakBtSeats.add(table.seats.get(findSeatIndex));
						break;
					}
					
					findSeatIndex = table.getNextUnHuSeatIndex(findSeatIndex);
				}
			}
		}
		
		table.bActExec = false;
		if(breakBtSeats.isEmpty()){
			table.logicManager.log("桌子轮到中断表态,所有人都选择了过");
			btSeat.gangType = GameDefine.GANG_TYPE_NONE;
			btSeat.gangSeatIndex = 0;
			//都选择了过,不中断原来的进程
			if(table.breakStateSource == GameDefine.STATE_TABLE_MOPAI){
				table.gameState = GameDefine.STATE_TABLE_OUTCARD;
			}else if(table.breakStateSource == GameDefine.STATE_TABLE_OUTCARD){
				//打出的牌没人要,下一玩家摸牌
				btSeat.gangSeatIndex = 0;
				table.btIndex = table.getNextUnHuSeatIndex(btSeat.seatIndex);
				table.gameState = GameDefine.STATE_TABLE_MOPAI;
			}else if(table.breakStateSource == GameDefine.STATE_TABLE_BREAKCARD){
				//巴杠,居然不抢杠胡,呵呵不好意思了
				//记录收巴杠钱
				List<Integer> winSeats = new ArrayList<>();
				for(SeatAttrib seat : table.seats){
					if(seat.huPaiType != GameDefine.HUPAI_TYPE_NONE
							|| seat.seatIndex == btSeat.seatIndex){
						continue;
					}
					winSeats.add(seat.seatIndex);
				}
				
				if(table.ruleAttrib.bGuaFengXiaYu){
					btSeat.baGangWinList.add(winSeats);
				}
				btSeat.statistAttrib.totalBagang++;
				btSeat.gangType = GameDefine.GANG_TYPE_SELF_BAGANG;
				
				//杠了继续摸牌
				table.gameState = GameDefine.STATE_TABLE_WAIT_EX;
				table.waitTime = currTime + GameDefine.TIME_TABLE_WAIT_EX;
				table.nextGameState = GameDefine.STATE_TABLE_MOPAI;
			}
			breakBtSeats = null;
			return;
		}
		table.logicManager.log("桌子轮到中断表态,表态有玩家总共数=" + breakBtSeats.size());
		int tmpGameState = table.gameState;
		int tmpBtIndex = table.btIndex;
		if(minBt != GameDefine.ACT_INDEX_HU && minBt != GameDefine.ACT_INDEX_TANG){
			btSeat.gangType = GameDefine.GANG_TYPE_NONE;
		}
		if(minBt == GameDefine.ACT_INDEX_HU){
			//胡了			
			int hupaiIndex = table.getHuPaiIndex() + 1;
			if(table.breakStateSource == GameDefine.STATE_TABLE_MOPAI){
				//自摸
				SeatAttrib winSeat = breakBtSeats.get(0);
				winSeat.huPaiIndex = hupaiIndex;
				winSeat.huCards.add(btSeat.breakCard);
				winSeat.moPaiCard = null;
				if(winSeat.gangType == GameDefine.GANG_TYPE_SELF_BAGANG
						|| winSeat.gangType == GameDefine.GANG_TYPE_SELF_ANGANG){
					//自摸杠上花,要赢所有未胡牌的玩家
					String sLog = "桌子轮到中断表态,表态座位=[" + winSeat.seatIndex + "]自摸杠上花=";
					sLog += GameDefine.cards2String(winSeat.huCards);
					sLog += "赢取座位=";
					
					winSeat.huPaiType = GameDefine.HUPAI_TYPE_GANGFLOW;
					for(SeatAttrib seat : table.seats){
						if(seat.huPaiIndex ==0){
							SeatHuPaiWinAttrib seatHuPaiWinAttrib = new SeatHuPaiWinAttrib();
							seatHuPaiWinAttrib.seatIndex = seat.seatIndex;
							seatHuPaiWinAttrib.tangCardState = seat.tangCardState;
							winSeat.huPaiWinList.add(seatHuPaiWinAttrib);
							sLog += ","+seat.seatIndex;
						}
					}
					winSeat.statistAttrib.totalZimo++;
					sLog += "胡牌顺序" + hupaiIndex;
					table.logicManager.log(sLog);
				}else if(winSeat.gangType == GameDefine.GANG_TYPE_DIANGANG){
					//点杠上花
					winSeat.huPaiType = GameDefine.HUPAI_TYPE_DIANGANGFLOW;
					if(table.ruleAttrib.bDianGangHuaAll){
						String sLog = "桌子轮到中断表态,表态座位=[" + winSeat.seatIndex + "]点炮杠上花算3家=";
						sLog += GameDefine.cards2String(winSeat.huCards);
						sLog += "赢取座位=";
						
						for(SeatAttrib seat : table.seats){
							if(seat.huPaiIndex ==0){
								SeatHuPaiWinAttrib seatHuPaiWinAttrib = new SeatHuPaiWinAttrib();
								seatHuPaiWinAttrib.seatIndex = seat.seatIndex;
								seatHuPaiWinAttrib.tangCardState = seat.tangCardState;
								winSeat.huPaiWinList.add(seatHuPaiWinAttrib);
								sLog += ""+seat.seatIndex;
							}
						}
						sLog += "胡牌顺序" + hupaiIndex;
						table.logicManager.log(sLog);
						winSeat.statistAttrib.totalZimo++;
					}else{
						SeatAttrib lostSeat = table.seats.get(winSeat.gangSeatIndex);
						SeatHuPaiWinAttrib seatHuPaiWinAttrib = new SeatHuPaiWinAttrib();
						seatHuPaiWinAttrib.seatIndex = lostSeat.seatIndex;
						seatHuPaiWinAttrib.tangCardState = lostSeat.tangCardState;
						winSeat.huPaiWinList.add(seatHuPaiWinAttrib);
						
						lostSeat.statistAttrib.totalMyDianPao++;
						winSeat.statistAttrib.totalDianPaoHu++;
						String sLog = "桌子轮到中断表态,表态座位=[" + winSeat.seatIndex + "]点炮杠上花算1家=";
						sLog += GameDefine.cards2String(winSeat.huCards);
						sLog += "赢取座位="+winSeat.gangSeatIndex;
						sLog += "胡牌顺序" + hupaiIndex;
						table.logicManager.log(sLog);
					}
				}else{
					//普通自摸
					winSeat.huPaiType = GameDefine.HUPAI_TYPE_ZIMO;
					String sLog = "桌子轮到中断表态,表态座位=[" + winSeat.seatIndex + "]普通自摸算3家=";
					sLog += GameDefine.cards2String(winSeat.huCards);
					sLog += "赢取座位=";
					
					for(SeatAttrib seat : table.seats){
						if(seat.huPaiType == GameDefine.HUPAI_TYPE_NONE
								&& seat.seatIndex != winSeat.seatIndex){
							SeatHuPaiWinAttrib seatHuPaiWinAttrib = new SeatHuPaiWinAttrib();
							seatHuPaiWinAttrib.seatIndex = seat.seatIndex;
							seatHuPaiWinAttrib.tangCardState = seat.tangCardState;
							
							winSeat.huPaiWinList.add(seatHuPaiWinAttrib);
							sLog += ""+seat.seatIndex;
						}
					}
					winSeat.statistAttrib.totalZimo++;
					sLog += "胡牌顺序" + hupaiIndex;
					table.logicManager.log(sLog);
				}
				//分析胡的牌型
				//CardAnalysisManager.analysisHuPaiStyle(this, winSeat, btSeat.breakCard);
				tmpBtIndex = table.getNextUnHuSeatIndex(tmpBtIndex);
			}else if(table.breakStateSource == GameDefine.STATE_TABLE_OUTCARD
					|| table.breakStateSource == GameDefine.STATE_TABLE_BREAKCARD){
				String sLog = "桌子轮到中断表态玩家,点炮座位="+table.btIndex+"胡牌顺序"+hupaiIndex;
				//点炮
				for(SeatAttrib winSeat : breakBtSeats){
					sLog += "接炮座位=" + winSeat.seatIndex;
					
					winSeat.huPaiIndex = hupaiIndex;
					
					SeatHuPaiWinAttrib seatHuPaiWinAttrib = new SeatHuPaiWinAttrib();
					seatHuPaiWinAttrib.seatIndex = btSeat.seatIndex;
					seatHuPaiWinAttrib.tangCardState = btSeat.tangCardState;
					winSeat.huPaiWinList.add(seatHuPaiWinAttrib);
					if(table.breakStateSource == GameDefine.STATE_TABLE_BREAKCARD){
						//是抢杠胡
						winSeat.huPaiType = GameDefine.HUPAI_TYPE_QIANGGANG;
						sLog += "胡牌方式=抢杠胡,胡牌="+btSeat.breakCard.toString();
						
						//从巴杠中移除杠牌,添加到碰牌中
						int findI = -1;
						for(int tmpI=0; tmpI<btSeat.baGangCards.size(); tmpI++){
							List<CardAttrib> baGangCardList = btSeat.baGangCards.get(tmpI);
							for(CardAttrib card : baGangCardList){
								if(card.suit == btSeat.breakCard.suit
										&& card.point == btSeat.breakCard.point){
									findI = tmpI;
									break;
								}
							}
							if(findI >= 0){
								break;
							}
						}
						if(findI >= 0){
							List<CardAttrib> baGangCardList = btSeat.baGangCards.get(findI);
							btSeat.baGangCards.remove(findI);
							
							List<CardAttrib> findList1 = GameDefine.findAllBySuitPoint(baGangCardList, btSeat.breakCard.suit, btSeat.breakCard.point);
							List<CardAttrib> findList2 = GameDefine.findAllBySuitPoint(baGangCardList, GameDefine.SUIT_TYPE_TIAO, 1);
							if(table.ruleAttrib.bYaoJiRenYong){
								if(btSeat.bUsedReplaceCard == false && findList1.size() > 0){
									winSeat.huCards.add(findList1.get(0));
									findList1.remove(0);
								}else if(btSeat.bUsedReplaceCard && findList2.size() > 0){
									winSeat.huCards.add(findList2.get(0));
									findList2.remove(0);
								}
							}else{
								if(findList1.size() > 0){
									winSeat.huCards.add(findList1.get(0));
									findList1.remove(0);
								}
							}
							
							findList1.addAll(findList2);
							if(findList1.size() == 3){
								btSeat.pengCards.add(findList1);
							}
						}
					}else{
						winSeat.huCards.add(btSeat.breakCard);
						if(btSeat.gangType != GameDefine.GANG_TYPE_NONE){
							//杠上炮
							winSeat.huPaiType = GameDefine.HUPAI_TYPE_DIANGANGPAO;
							sLog += "胡牌方式=杠上炮,胡牌="+btSeat.breakCard.toString();
							
							if(table.ruleAttrib.bGuaFengXiaYu && table.ruleAttrib.bZhuanYu){
								SeatHuJiaoZhuYiAttrib seatHuJiaoZhuYiAttrib = new SeatHuJiaoZhuYiAttrib();
								seatHuJiaoZhuYiAttrib.seatIndex = winSeat.seatIndex;
								//记录转雨数据
								switch(btSeat.gangType){
								case GameDefine.GANG_TYPE_SELF_BAGANG://巴杠
									if(btSeat.baGangWinList.size() > 0){
										int n = btSeat.baGangWinList.get(btSeat.baGangWinList.size()-1).size();
										seatHuJiaoZhuYiAttrib.score = table.ruleAttrib.baseScore*n;
									}
									break;
								case GameDefine.GANG_TYPE_SELF_ANGANG://暗杠
									if(btSeat.anGangWinList.size() > 0){
										int n = btSeat.anGangWinList.get(btSeat.anGangWinList.size()-1).size();
										seatHuJiaoZhuYiAttrib.score = (table.ruleAttrib.baseScore*2)*n;
									}
									break;
								case GameDefine.GANG_TYPE_DIANGANG://点杠
									if(btSeat.dianGangWinList.size() > 0){
										seatHuJiaoZhuYiAttrib.score = (table.ruleAttrib.baseScore*2)*1;
										if(btSeat.dianGangJiaJiaYouWinList.size() > 0){
											int n = btSeat.dianGangJiaJiaYouWinList.get(btSeat.dianGangJiaJiaYouWinList.size()-1).size();
											seatHuJiaoZhuYiAttrib.score += table.ruleAttrib.baseScore*n;
										}
									}
									break;
								}
								btSeat.zhuanYuSeats.add(seatHuJiaoZhuYiAttrib);
							}
						}else{
							//普通点炮
							winSeat.huPaiType = GameDefine.HUPAI_TYPE_DIANPAO;
							sLog += "胡牌方式=普通点炮,胡牌="+btSeat.breakCard.toString();
						}
						GameDefine.removeOnceBySuitPoint(btSeat.outUnUseCards, btSeat.breakCard);
					}
					winSeat.statistAttrib.totalDianPaoHu++;
					//分析胡的牌型
					//CardAnalysisManager.analysisHuPaiStyle(this, winSeat, btSeat.breakCard);
				}
				table.logicManager.log(sLog);
				btSeat.statistAttrib.totalMyDianPao++;
				//计算摸牌玩家,胡了牌的下一个未胡牌座位
				tmpBtIndex = tmpBtIndex - 1;
				if(tmpBtIndex < 0){
					tmpBtIndex = table.seats.size()-1;
				}
				while(true){
					boolean bfind = false;
					for(SeatAttrib seatAttrib : breakBtSeats){
						if(seatAttrib.seatIndex == tmpBtIndex){
							bfind = true;
							break;
						}
					}
					if(bfind){
						break;
					}
					tmpBtIndex = tmpBtIndex - 1;
					if(tmpBtIndex < 0){
						tmpBtIndex = table.seats.size()-1;
					}
				}
				tmpBtIndex = table.getNextUnHuSeatIndex(tmpBtIndex);
			}
			tmpGameState = GameDefine.STATE_TABLE_MOPAI;
		}else if(minBt == GameDefine.ACT_INDEX_GANG){
			//杠,只能有一家
			SeatAttrib winSeat = breakBtSeats.get(0);
			String sLog = "桌子轮到中断表态玩家,开杠座位="+winSeat.seatIndex;
			if(table.breakStateSource == GameDefine.STATE_TABLE_MOPAI){	
				//把摸的牌放到手牌中
				winSeat.handCards.add(winSeat.moPaiCard);
				
				if(winSeat.lsmjGangType == GameDefine.GANG_TYPE_SELF_BAGANG){
					//是巴杠,要检查是否有人抢杠胡
					int breakCardCnt = 0;
					table.resetBreakCardState();
					String breakSeatIndex = "";
					for(SeatAttrib seat : table.seats){
						if(seat.huPaiIndex != 0 || seat.seatIndex == table.btIndex){
							//已经胡了牌的和杠牌的玩家自已排除
							continue;
						}
						GameDefine.AnalysisBreakCard(table, seat.seatIndex, table.btIndex, GameDefine.STATE_TABLE_BREAKCARD);
						int state = seat.breakCardState[GameDefine.ACT_INDEX_HU];
						seat.resetBreakState();
						seat.breakCardState[GameDefine.ACT_INDEX_HU] = state;
						seat.breakCardState[GameDefine.ACT_INDEX_DROP] = state;
						if(state > 0){
							seat.breakCard = btSeat.breakCard;
							breakSeatIndex += ""+seat.seatIndex;
						}
						breakCardCnt += state;
					}
					//查找碰牌
					int findI = -1;
					for(int tmpI=0; tmpI<winSeat.pengCards.size(); tmpI++){
						List<CardAttrib> pengCardList = winSeat.pengCards.get(tmpI);
						for(CardAttrib card : pengCardList){
							if(card.point == btSeat.breakCard.point
									&& card.suit == btSeat.breakCard.suit){
								findI = tmpI;
								break;
							}
						}
						if(findI >= 0){
							break;
						}
					}
					List<CardAttrib> findPengCardList = null;
					if(findI >= 0){
						findPengCardList = winSeat.pengCards.get(findI);
						winSeat.pengCards.remove(findI);
					}					
					
					//从手牌中移除表态牌
					if(table.ruleAttrib.bYaoJiRenYong){
						//优先使用原始有的牌,没有再使用替换牌
						CardAttrib findBtCard = GameDefine.findOnceBySuitPoint(winSeat.handCards, btSeat.breakCard.suit, btSeat.breakCard.point);
						if(null != findBtCard){
							GameDefine.removeOnceBySuitPoint(winSeat.handCards, findBtCard);
							findPengCardList.add(findBtCard);
							winSeat.bUsedReplaceCard = false;
						}else{
							//移除一张替换牌
							CardAttrib replaceCard = GameDefine.findOnceBySuitPoint(winSeat.handCards, GameDefine.SUIT_TYPE_TIAO, 1);
							if(null != replaceCard){
								GameDefine.removeOnceBySuitPoint(winSeat.handCards, replaceCard);
								findPengCardList.add(replaceCard);
								winSeat.bUsedReplaceCard = true;
							}
						}
					}else{
						GameDefine.removeOnceBySuitPoint(winSeat.handCards, btSeat.breakCard);
						findPengCardList.add(btSeat.breakCard);
					}
					
					if(null != findPengCardList && findPengCardList.size() == 4){
						winSeat.baGangCards.add(findPengCardList);
					}
					
					btSeat.moPaiCard = null;
					
					if(breakCardCnt > 0){
						//需要抢杠胡表态
						//发送胡杠碰吃通知数据
						List<SeatAttrib> tmpBreakSeats = new ArrayList<SeatAttrib>();
						tmpBreakSeats.add(table.seats.get(table.btIndex));
						int theBt = GameDefine.ACT_INDEX_GANG;
						
						table.sendBreakCardNotify(tmpBreakSeats, theBt);
						table.logicManager.log("桌子轮到中断表态玩家,座位="+winSeat.seatIndex + "中断座位="+breakSeatIndex);
						table.gameState = GameDefine.STATE_TABLE_WAIT_EX;
						table.waitTime = currTime + GameDefine.TIME_TABLE_WAIT_EX;
						table.nextGameState = GameDefine.STATE_TABLE_BREAKCARD;
						table.breakStateSource = GameDefine.STATE_TABLE_BREAKCARD;						
						return;
					}else{
						sLog += "杠牌="+btSeat.breakCard.toString();
						sLog += ",巴杠,赢座位=";
						//没人抢杠
						winSeat.gangType = GameDefine.GANG_TYPE_SELF_BAGANG;
						winSeat.gangSeatIndex = winSeat.seatIndex;
						List<Integer> gangWinList = new ArrayList<>();
						for(SeatAttrib seat : table.seats){
							if(seat.huPaiIndex != 0 || seat.seatIndex == table.btIndex){
								continue;
							}
							gangWinList.add(seat.seatIndex);
							sLog += ""+seat.seatIndex;
						}
						table.logicManager.log(sLog);
						if(table.ruleAttrib.bGuaFengXiaYu){
							winSeat.baGangWinList.add(gangWinList);
						}
						winSeat.statistAttrib.totalBagang++;
					}
				}else{
					//是暗杠
					sLog += "杠牌="+btSeat.breakCard.toString();
					sLog += ",暗杠,赢座位=";
					winSeat.gangType = GameDefine.GANG_TYPE_SELF_ANGANG;
					winSeat.gangSeatIndex = winSeat.seatIndex;
					List<Integer> gangWinList = new ArrayList<>();
					for(SeatAttrib seat : table.seats){
						if(seat.huPaiType != GameDefine.HUPAI_TYPE_NONE
								|| seat.seatIndex == winSeat.seatIndex){
							//自已和胡了牌的不算杠钱
							continue;
						}
						gangWinList.add(seat.seatIndex);
						sLog += ""+seat.seatIndex;
					}
					table.logicManager.log(sLog);
					
					if(table.ruleAttrib.bGuaFengXiaYu){
						winSeat.anGangWinList.add(gangWinList);
					}
					winSeat.statistAttrib.totalAngang++;
										
					List<CardAttrib> findCardList = GameDefine.findAllBySuitPoint(winSeat.handCards, 
							btSeat.breakCard.suit, btSeat.breakCard.point);
					
					List<CardAttrib> findYaojiList = GameDefine.findAllBySuitPoint(winSeat.handCards, 
							GameDefine.SUIT_TYPE_TIAO, 1);
					
					if((findCardList.size()+findYaojiList.size()) >= 4){
						//表态牌不够的用替换牌补上		
						if(table.ruleAttrib.bYaoJiRenYong && findCardList.size() < 4){
							int N = 4-findCardList.size();
							for(int tmpI=0; tmpI<N; tmpI++){
								findCardList.add(findYaojiList.get(tmpI));
							}
						}
						GameDefine.removeAllBySuitPoint(winSeat.handCards, findCardList);
						winSeat.anGangCards.add(findCardList);
					}
				}
				winSeat.moPaiCard = null;
				tmpGameState = GameDefine.STATE_TABLE_MOPAI;
			}else if(table.breakStateSource == GameDefine.STATE_TABLE_OUTCARD){
				//点杠
				winSeat.gangType = GameDefine.GANG_TYPE_DIANGANG;
				winSeat.gangSeatIndex = table.btIndex;
				if(table.ruleAttrib.bGuaFengXiaYu){
					winSeat.dianGangWinList.add(table.btIndex);
					if(table.ruleAttrib.bJiaJiaYou){
						//点杠家家有
						List<Integer> gangWinList = new ArrayList<>();
						for(SeatAttrib lostSeat : table.seats){
							if(lostSeat.huPaiType != GameDefine.HUPAI_TYPE_NONE
									|| lostSeat.seatIndex == winSeat.seatIndex
									|| lostSeat.seatIndex == table.btIndex){
								//自已和胡了牌的,点杠的不算杠钱
								continue;
							}
							gangWinList.add(lostSeat.seatIndex);
						}
						if(gangWinList.isEmpty() == false){
							winSeat.dianGangJiaJiaYouWinList.add(gangWinList);
						}
					}
				}
				//把表态牌加入手牌中
				winSeat.handCards.add(btSeat.breakCard);
				List<CardAttrib> cardList = GameDefine.findAllBySuitPoint(winSeat.handCards, 
						btSeat.breakCard.suit, btSeat.breakCard.point);
				List<CardAttrib> findYaojiList = GameDefine.findAllBySuitPoint(winSeat.handCards, 
						GameDefine.SUIT_TYPE_TIAO, 1);
				
				if((cardList.size()+findYaojiList.size()) >= 4){
					//表态牌不够的用替换牌补上
					if(table.ruleAttrib.bYaoJiRenYong && cardList.size() < 4){
						int N = 4-cardList.size();
						for(int tmpI=0; tmpI<N; tmpI++){
							cardList.add(findYaojiList.get(tmpI));
						}
					}
					GameDefine.removeAllBySuitPoint(winSeat.handCards, cardList);
					winSeat.dianGangCards.add(cardList);
				}
								
				sLog += "杠牌="+btSeat.breakCard.toString();
				sLog += ",点杠,赢座位="+btSeat.seatIndex;
				table.logicManager.log(sLog);
				
				winSeat.statistAttrib.totalBagang++;
				tmpBtIndex = winSeat.seatIndex;
				
				GameDefine.removeOnceBySuitPoint(btSeat.outUnUseCards, btSeat.breakCard);
			}			
			tmpGameState = GameDefine.STATE_TABLE_MOPAI;
		}else if(minBt == GameDefine.ACT_INDEX_PENG){
			//碰
			SeatAttrib seatAttrib = breakBtSeats.get(0);
			List<CardAttrib> cardList = GameDefine.findAllBySuitPoint(seatAttrib.handCards, 
					btSeat.breakCard.suit, btSeat.breakCard.point);
			List<CardAttrib> findYaojiList = GameDefine.findAllBySuitPoint(seatAttrib.handCards, 
					GameDefine.SUIT_TYPE_TIAO, 1);
			
			List<CardAttrib> pengpengList = new ArrayList<>();
			if((cardList.size() + findYaojiList.size()) >= 2){
				if(table.ruleAttrib.bYaoJiRenYong && cardList.size() < 2){
					//表态牌不够的用替换牌补上
					pengpengList.addAll(cardList);
					int N = 2-cardList.size();
					for(int tmpI=0; tmpI<N; tmpI++){
						pengpengList.add(findYaojiList.get(tmpI));
					}
				}else{
					pengpengList.add(cardList.get(0));
					pengpengList.add(cardList.get(1));
				}
			}
			
			GameDefine.removeAllBySuitPoint(seatAttrib.handCards, pengpengList);
			pengpengList.add(btSeat.breakCard);
			seatAttrib.pengCards.add(pengpengList);
			
			String sLog = "桌子轮到中断表态玩家,碰牌座位="+seatAttrib.seatIndex;
			sLog += "碰的牌="+btSeat.breakCard.toString()+"打牌座位="+btSeat.seatIndex;
			table.logicManager.log(sLog);
			
			GameDefine.removeOnceBySuitPoint(btSeat.outUnUseCards, btSeat.breakCard);
			
			boolean bchkTing = false;
			if(table.ruleAttrib.bTangPai
					&& seatAttrib.tangCardState <= 0 
					&& table.tableCards.size() > 8){
				int unHuiPlayer = 100;
				if(table.ruleAttrib.bLiangJiaBuTang){
					//两家不躺
					unHuiPlayer = 0;
					for(int i=0; i<table.seats.size(); i++){
						SeatAttrib theSeat = table.seats.get(i);
						if(theSeat.huPaiType == GameDefine.HUPAI_TYPE_NONE){
							unHuiPlayer++;
						}
					}
				}
				if(unHuiPlayer > 2){
					for(int tmpIndex=0; tmpIndex<seatAttrib.handCards.size(); tmpIndex++){
						List<CardAttrib> tmpchkList = new ArrayList<>();
						tmpchkList.addAll(seatAttrib.handCards);
						tmpchkList.remove(tmpIndex);
						List<CardAttrib> tingpaiCards = GameDefine.isTingPai(table, tmpchkList, seatAttrib);
						if(null != tingpaiCards && tingpaiCards.isEmpty() == false){
							bchkTing = true;
							break;
						}
					}
				}
			}
			
			tmpGameState = GameDefine.STATE_TABLE_OUTCARD;
			if(bchkTing){
				seatAttrib.resetBreakState();
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_TANG] = 1;
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_DROP] = 1;
				seatAttrib.tangSource = 2;
				tmpGameState = GameDefine.STATE_TABLE_BREAKCARD;
				table.breakStateSource = GameDefine.STATE_TABLE_MOPAI;
			}
			tmpBtIndex = seatAttrib.seatIndex;
		}else if(minBt == GameDefine.ACT_INDEX_TANG){
			SeatAttrib seatAttrib = breakBtSeats.get(0);
			tmpGameState = GameDefine.STATE_TABLE_OUTCARD;
			tmpBtIndex = seatAttrib.seatIndex;
			
			if(1 == seatAttrib.tangSource){
				if(null != seatAttrib.moPaiCard)
				seatAttrib.handCards.add(seatAttrib.moPaiCard);
				seatAttrib.moPaiCard = null;
			}
		}
		//发送胡杠碰吃结果通知数据
		table.sendBreakCardNotify(breakBtSeats, minBt);
		table.bActExec = false;
		table.gameState = GameDefine.STATE_TABLE_WAIT_EX;
		table.waitTime = currTime + GameDefine.TIME_TABLE_WAIT_EX;
		table.nextGameState = tmpGameState;
		table.btIndex = tmpBtIndex;
		breakBtSeats = null;
	}
}
