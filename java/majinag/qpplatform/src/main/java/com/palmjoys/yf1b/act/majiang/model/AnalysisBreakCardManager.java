package com.palmjoys.yf1b.act.majiang.model;

import java.util.ArrayList;
import java.util.List;

import com.palmjoys.yf1b.act.majiang.manager.CardAnalysisManager;

public class AnalysisBreakCardManager {
	
	public static int AnalysisBreakCard_ncmj(TableAttrib table, int seatIndex, int prevIndex, int breakSource){		
		SeatAttrib seatAttrib = table.seats.get(seatIndex);
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(seatAttrib.handCards);
		SeatAttrib prevSeat = table.seats.get(prevIndex);
		
		CardAttrib breakCard = prevSeat.breakCard;
		if(null != breakCard){
			copyCards.add(breakCard);
		}
		//胡牌组合列表
		List<List<CardAttrib>> huComboList = null;
		seatAttrib.huPaiComboList = null;
		
		huComboList = GameDefine.checkHuPai(table, seatAttrib, breakCard);
		if(huComboList != null){
			//计算胡牌番数>=起始胡牌番数和胡牌分数>=起始胡牌分数才能胡牌
			//分析胡的牌型
			int []huPaiStyle = CardAnalysisManager.analysisHuPaiStyle2(table, seatAttrib, breakCard, huComboList);
			//分析胡牌方式
			int huPaiType = 0;
			if(breakSource == GameDefine.STATE_TABLE_MOPAI){
				//自摸
				if(seatAttrib.gangType == GameDefine.GANG_TYPE_SELF_BAGANG
						|| seatAttrib.gangType == GameDefine.GANG_TYPE_SELF_ANGANG){
					huPaiType = GameDefine.HUPAI_TYPE_GANGFLOW;
				}else if(seatAttrib.gangType == GameDefine.GANG_TYPE_DIANGANG){
					huPaiType = GameDefine.HUPAI_TYPE_DIANGANGFLOW;
				}else{
					huPaiType = GameDefine.HUPAI_TYPE_ZIMO;
				}
			}else if(breakSource == GameDefine.STATE_TABLE_OUTCARD
					|| breakSource == GameDefine.STATE_TABLE_BREAKCARD){
				//别人点炮
				if(breakSource == GameDefine.STATE_TABLE_BREAKCARD){
					//抢杠胡
					huPaiType = GameDefine.HUPAI_TYPE_QIANGGANG;
				}else{
					if(prevSeat.gangType != GameDefine.GANG_TYPE_NONE){
						//杠上炮
						huPaiType = GameDefine.HUPAI_TYPE_DIANGANGPAO;
					}else{
						huPaiType = GameDefine.HUPAI_TYPE_DIANPAO;
					}
				}
			}
			int totalHuPaiRate = 0;
			totalHuPaiRate = CardAnalysisManager.getHuPaiStyleRate(table, huPaiStyle);
			totalHuPaiRate += CardAnalysisManager.getHuPaiTypeRate(table, huPaiType);
			totalHuPaiRate += seatAttrib.anGangCards.size();
			totalHuPaiRate += seatAttrib.baGangCards.size();
			totalHuPaiRate += seatAttrib.dianGangCards.size();
			
			List<CardAttrib> tmpCards = new ArrayList<>();
			tmpCards.addAll(seatAttrib.handCards);
			tmpCards.add(prevSeat.breakCard);
			
			for(List<CardAttrib> pengCardList : seatAttrib.pengCards){
				tmpCards.addAll(pengCardList);
			}
			List<List<CardAttrib>> four = GameDefine.findFourCards(tmpCards);
			totalHuPaiRate += four.size();
			if(table.ruleAttrib.bTangPai && seatAttrib.tangCardState > 0){
				totalHuPaiRate += 1;
			}
			if(table.ruleAttrib.bBaoJia && seatAttrib.baoJiaoState > 0){
				totalHuPaiRate += 1;
			}
			if(breakSource == GameDefine.STATE_TABLE_OUTCARD
					|| breakSource == GameDefine.STATE_TABLE_BREAKCARD){
				if(table.ruleAttrib.bTangPai && prevSeat.tangCardState > 0){
					//别人点炮,点炮人有躺加番
					totalHuPaiRate += 1;
				}
				if(table.ruleAttrib.bBaoJia && prevSeat.baoJiaoState > 0){
					totalHuPaiRate += 1;
				}
			}
			
			if(totalHuPaiRate > table.ruleAttrib.maxRate){
				totalHuPaiRate = table.ruleAttrib.maxRate;
			}
			
			int rateScore = table.ruleAttrib.baseScore + table.ruleAttrib.baseScore*totalHuPaiRate;
			if(huPaiType == GameDefine.HUPAI_TYPE_ZIMO
					|| huPaiType == GameDefine.HUPAI_TYPE_GANGFLOW
					|| huPaiType == GameDefine.HUPAI_TYPE_DIANGANGFLOW){
				//自摸类型
				if(table.ruleAttrib.bZiMoAddBase){
					rateScore += table.ruleAttrib.baseScore;
				}
			}
			
			tmpCards = null;
			if(totalHuPaiRate >= table.ruleAttrib.canHuPaiFanShu && rateScore >= table.ruleAttrib.canHuPaiScore){
				//够起胡番数和起胡分数
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] = 1;
				if(table.ruleAttrib.bTangPai && seatAttrib.tangCardState > 0){
					//躺了或摆了只能胡躺或摆的牌
					boolean bTangHu = false;
					for(CardAttrib card : seatAttrib.tangCanHuList){
						if(card.suit == breakCard.suit
								&& card.point == breakCard.point){
							bTangHu = true;
							break;
						}
					}
					if(bTangHu == false){
						seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] = 0;
					}
				}
				//检查不能胡过手胡的牌
				if(breakSource != GameDefine.STATE_TABLE_MOPAI){
					for(CardAttrib card : seatAttrib.guoShouHuCardList){
						if(card.suit == breakCard.suit
								&& card.point == breakCard.point){
							seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] = 0;
							break;
						}
					}
				}
				if(table.ruleAttrib.bBaoJia && seatAttrib.baoJiaoState > 0){
					//报了叫,当前不是自摸且别人点炮选择了不胡的,只能自摸才能胡了
					if(breakSource != GameDefine.STATE_TABLE_MOPAI){
						if(seatAttrib.bDianPaoNoHu){
							seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] = 0;
						}
					}
				}
			}
			if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
				//可以胡牌
				seatAttrib.huPaiComboList = huComboList;
				System.arraycopy(huPaiStyle, 0, seatAttrib.huPaiStyle, 0, GameDefine.HUPAI_STYLE_MAX_END);
			}
		}
		//检测杠碰需要去除未打缺的花色
		GameDefine.removeAllBySuit(copyCards, seatAttrib.unSuit);
		if(seatIndex == prevIndex){
			//自摸到的牌分析,只能是,胡,杠
			for(List<CardAttrib> pengCardList : seatAttrib.pengCards){
				copyCards.addAll(pengCardList);
			}
			
			List<List<CardAttrib>> findCards = GameDefine.findFourCards(copyCards);
			if(findCards.isEmpty() == false){
				//有暗杠或巴杠
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 1;
				if(table.ruleAttrib.bTangPai && seatAttrib.tangCardState > 0){
					//已躺牌,一定是下了叫的
					if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
						//可以胡牌必胡,不能杠
						seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 0;
					}else{
						//检查是否已躺牌相关的杠
						boolean bCanGang = false;
						for(List<CardAttrib> gangCards : findCards){
							CardAttrib gangCard = gangCards.get(0);
							if(gangCard.suit != seatAttrib.breakCard.suit
									|| gangCard.point != seatAttrib.breakCard.point){
								//杠的牌不是摸到的那张牌,不能杠
								continue;
							}
							
							List<CardAttrib> tmpFindCards = GameDefine.findAllBySuitPoint(seatAttrib.tangCardList, gangCard.suit, gangCard.point);
							if(null == tmpFindCards || tmpFindCards.isEmpty()){
								bCanGang = true;
							}
							if(bCanGang){
								//把杠牌移除后,是否叫牌和躺牌叫一样
								List<CardAttrib> tmpCopyCards = new ArrayList<>();
								tmpCopyCards.addAll(copyCards);
								GameDefine.removeAllBySuitPoint(tmpCopyCards, gangCards);
								List<CardAttrib> tingHuCards = GameDefine.isTingPai(table, tmpCopyCards, seatAttrib);
								
								for(CardAttrib tangCanHuCard : seatAttrib.tangCanHuList){
									List<CardAttrib> tmpFindtangCanHuCards = GameDefine.findAllBySuitPoint(tingHuCards, tangCanHuCard.suit, tangCanHuCard.point);
									if(null == tmpFindtangCanHuCards || tmpFindtangCanHuCards.isEmpty()){
										bCanGang = false;
										break;
									}
								}
							}
							
							if(bCanGang){
								break;
							}
						}
						if(bCanGang == false){
							seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 0;
						}
					}
				}
				
				if(table.ruleAttrib.bBaoJia 
						&& seatAttrib.baoJiaoState > 0 
						&& seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] > 0){
					boolean bCanGang = false;
					//有报叫规则且报了叫,杠了后有叫就可以杠
					for(List<CardAttrib> gangCards : findCards){						
						List<CardAttrib> tmpCopyCards = new ArrayList<>();
						tmpCopyCards.addAll(copyCards);
						GameDefine.removeAllBySuitPoint(tmpCopyCards, gangCards);
						List<CardAttrib> tingHuCards = GameDefine.isTingPai(table, tmpCopyCards, seatAttrib);
						if(tingHuCards != null && tingHuCards.isEmpty() == false){
							bCanGang = true;
							break;
						}
					}
					if(bCanGang == false){
						seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 0;
					}
				}
			}
		}else{
			//别人打的牌分析,胡,杠,碰
			List<CardAttrib> findCards = GameDefine.findAllBySuitPoint(copyCards, breakCard.suit, breakCard.point);
			if(findCards.size() == 3){
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_PENG] = 1;
			}else if(findCards.size() == 4){
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_PENG] = 1;
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 1;
			}
			if(table.ruleAttrib.bTangPai && seatAttrib.tangCardState > 0){
				//躺牌后一定不能碰
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_PENG] = 0;
				
				if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] > 0){
					if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
						seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 0;
					}else{
						//有杠,检查不能是和胡相关的杠
						CardAttrib gangCard = findCards.get(0);
						List<CardAttrib> tmpFindCards = GameDefine.findAllBySuitPoint(seatAttrib.tangCardList, gangCard.suit, gangCard.point);
						if(null == tmpFindCards || tmpFindCards.isEmpty()){
							//是不和躺牌相关的杠牌,把杠牌移除后,是否叫牌和躺牌叫一样
							List<CardAttrib> tmpCopyCards = new ArrayList<>();
							tmpCopyCards.addAll(copyCards);
							GameDefine.removeAllBySuitPoint(tmpCopyCards, findCards);
							List<CardAttrib> tingHuCards = GameDefine.isTingPai(table, tmpCopyCards, seatAttrib);
							
							for(CardAttrib tangCanHuCard : seatAttrib.tangCanHuList){
								List<CardAttrib> tmpFindtangCanHuCards = GameDefine.findAllBySuitPoint(tingHuCards, tangCanHuCard.suit, tangCanHuCard.point);
								if(null == tmpFindtangCanHuCards || tmpFindtangCanHuCards.isEmpty()){
									//叫牌改变了不能杠
									seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 0;
									break;
								}
							}
						}
					}
				}
			}
			if(table.ruleAttrib.bBaoJia && seatAttrib.baoJiaoState > 0){
				//报叫后一定不能碰
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_PENG] = 0;
				
				if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] > 0){
					//有报叫规则且报了叫,杠了后有叫就可以杠
					List<CardAttrib> tmpCopyCards = new ArrayList<>();
					tmpCopyCards.addAll(copyCards);
					GameDefine.removeAllBySuitPoint(tmpCopyCards, findCards);
					List<CardAttrib> tingHuCards = GameDefine.isTingPai(table, tmpCopyCards, seatAttrib);
					if(tingHuCards == null || tingHuCards.isEmpty()){
						seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 0;
					}
				}
			}
		}
		
		int tangChkNum = 8;		
		if(breakSource == GameDefine.STATE_TABLE_MOPAI 
				&& table.ruleAttrib.bTangPai
				&& seatAttrib.tangCardState <= 0 
				&& table.tableCards.size() > tangChkNum){
			//摸牌状态且有躺牌规则,且未躺牌底牌张数大于8张,
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
				
				copyCards.clear();
				copyCards.addAll(seatAttrib.handCards);
				copyCards.add(breakCard);
				for(int index=0; index<copyCards.size(); index++){
					List<CardAttrib> theTmpList = new ArrayList<>();
					theTmpList.addAll(copyCards);
					theTmpList.remove(index);
					List<CardAttrib> tingCards = GameDefine.isTingPai(table, theTmpList, seatAttrib);
					if(null != tingCards && tingCards.isEmpty() == false){
						CardAttrib theCard = copyCards.get(index);
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
					seatAttrib.breakCardState[GameDefine.ACT_INDEX_TANG] = 1;
					seatAttrib.tangSource = 1;
				}
			}
		}		
		
		if(table.tableCards.isEmpty()){
			//桌子上没有牌可摸了不能有杠动作
			seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 0;
		}
				
		boolean bBreak = seatAttrib.haveBreakState();
		if(bBreak){
			seatAttrib.breakCardState[GameDefine.ACT_INDEX_DROP] = 1;
			return 1;
		}
		copyCards = null;
		return 0;
	}
	
	public static int AnalysisBreakCard_other(TableAttrib table, int seatIndex, int prevIndex, int breakSource){		
		SeatAttrib seatAttrib = table.seats.get(seatIndex);
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(seatAttrib.handCards);
		SeatAttrib prevSeat = table.seats.get(prevIndex);
		
		CardAttrib breakCard = prevSeat.breakCard;
		if(null != breakCard){
			copyCards.add(breakCard);
		}
		//胡牌组合列表
		List<List<CardAttrib>> huComboList = null;
		seatAttrib.huPaiComboList = null;
		
		huComboList = GameDefine.checkHuPai(table, seatAttrib, breakCard);
		if(huComboList != null){
			//计算胡牌番数>=起始胡牌番数和胡牌分数>=起始胡牌分数才能胡牌
			//分析胡的牌型
			int []huPaiStyle = CardAnalysisManager.analysisHuPaiStyle2(table, seatAttrib, breakCard, huComboList);
			//分析胡牌方式
			int huPaiType = 0;
			if(breakSource == GameDefine.STATE_TABLE_MOPAI){
				//自摸
				if(seatAttrib.gangType == GameDefine.GANG_TYPE_SELF_BAGANG
						|| seatAttrib.gangType == GameDefine.GANG_TYPE_SELF_ANGANG){
					huPaiType = GameDefine.HUPAI_TYPE_GANGFLOW;
				}else if(seatAttrib.gangType == GameDefine.GANG_TYPE_DIANGANG){
					huPaiType = GameDefine.HUPAI_TYPE_DIANGANGFLOW;
				}else{
					huPaiType = GameDefine.HUPAI_TYPE_ZIMO;
				}
			}else if(breakSource == GameDefine.STATE_TABLE_OUTCARD
					|| breakSource == GameDefine.STATE_TABLE_BREAKCARD){
				//别人点炮
				if(breakSource == GameDefine.STATE_TABLE_BREAKCARD){
					//抢杠胡
					huPaiType = GameDefine.HUPAI_TYPE_QIANGGANG;
				}else{
					if(prevSeat.gangType != GameDefine.GANG_TYPE_NONE){
						//杠上炮
						huPaiType = GameDefine.HUPAI_TYPE_DIANGANGPAO;
					}else{
						huPaiType = GameDefine.HUPAI_TYPE_DIANPAO;
					}
				}
			}
			int totalHuPaiRate = 0;
			totalHuPaiRate = CardAnalysisManager.getHuPaiStyleRate(table, huPaiStyle);
			totalHuPaiRate += CardAnalysisManager.getHuPaiTypeRate(table, huPaiType);
			totalHuPaiRate += seatAttrib.anGangCards.size();
			totalHuPaiRate += seatAttrib.baGangCards.size();
			totalHuPaiRate += seatAttrib.dianGangCards.size();
			
			List<CardAttrib> tmpCards = new ArrayList<>();
			tmpCards.addAll(seatAttrib.handCards);
			tmpCards.add(prevSeat.breakCard);
			
			for(List<CardAttrib> pengCardList : seatAttrib.pengCards){
				tmpCards.addAll(pengCardList);
			}
			List<List<CardAttrib>> four = GameDefine.findFourCards(tmpCards);
			totalHuPaiRate += four.size();
			if(table.ruleAttrib.bTangPai && seatAttrib.tangCardState > 0){
				totalHuPaiRate += 1;
			}
			if(table.ruleAttrib.bBaoJia && seatAttrib.baoJiaoState > 0){
				totalHuPaiRate += 1;
			}
			if(breakSource == GameDefine.STATE_TABLE_OUTCARD
					|| breakSource == GameDefine.STATE_TABLE_BREAKCARD){
				if(table.ruleAttrib.bTangPai && prevSeat.tangCardState > 0){
					//别人点炮,点炮人有躺加番
					totalHuPaiRate += 1;
				}
				if(table.ruleAttrib.bBaoJia && prevSeat.baoJiaoState > 0){
					totalHuPaiRate += 1;
				}
			}
			
			if(totalHuPaiRate > table.ruleAttrib.maxRate){
				totalHuPaiRate = table.ruleAttrib.maxRate;
			}
			
			int rateScore = GameDefine.calculationRateScore(table.ruleAttrib.baseScore, totalHuPaiRate);
			if(huPaiType == GameDefine.HUPAI_TYPE_ZIMO
					|| huPaiType == GameDefine.HUPAI_TYPE_GANGFLOW
					|| huPaiType == GameDefine.HUPAI_TYPE_DIANGANGFLOW){
				//自摸类型
				if(table.ruleAttrib.bZiMoAddBase){
					rateScore += table.ruleAttrib.baseScore;
				}
			}
			
			tmpCards = null;
			if(totalHuPaiRate >= table.ruleAttrib.canHuPaiFanShu && rateScore >= table.ruleAttrib.canHuPaiScore){
				//够起胡番数和起胡分数
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] = 1;
				if(table.ruleAttrib.bTangPai && seatAttrib.tangCardState > 0){
					//躺了或摆了只能胡躺或摆的牌
					boolean bTangHu = false;
					for(CardAttrib card : seatAttrib.tangCanHuList){
						if(card.suit == breakCard.suit
								&& card.point == breakCard.point){
							bTangHu = true;
							break;
						}
					}
					if(bTangHu == false){
						seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] = 0;
					}
				}
				//检查不能胡过手胡的牌
				if(breakSource != GameDefine.STATE_TABLE_MOPAI){
					for(CardAttrib card : seatAttrib.guoShouHuCardList){
						if(card.suit == breakCard.suit
								&& card.point == breakCard.point){
							seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] = 0;
							break;
						}
					}
				}
				if(table.ruleAttrib.bBaoJia && seatAttrib.baoJiaoState > 0){
					//报了叫,当前不是自摸且别人点炮选择了不胡的,只能自摸才能胡了
					if(breakSource != GameDefine.STATE_TABLE_MOPAI){
						if(seatAttrib.bDianPaoNoHu){
							seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] = 0;
						}
					}
				}
			}
			if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
				//可以胡牌
				seatAttrib.huPaiComboList = huComboList;
				System.arraycopy(huPaiStyle, 0, seatAttrib.huPaiStyle, 0, GameDefine.HUPAI_STYLE_MAX_END);
			}
		}
		//检测杠碰需要去除未打缺的花色
		GameDefine.removeAllBySuit(copyCards, seatAttrib.unSuit);
		if(seatIndex == prevIndex){
			//自摸到的牌分析,只能是,胡,杠
			for(List<CardAttrib> pengCardList : seatAttrib.pengCards){
				copyCards.addAll(pengCardList);
			}
			
			List<List<CardAttrib>> findCards = GameDefine.findFourCards(copyCards);
			if(findCards.isEmpty() == false){
				//有暗杠或巴杠
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 1;
				if(table.ruleAttrib.bTangPai && seatAttrib.tangCardState > 0){
					//已躺牌,一定是下了叫的
					if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
						//可以胡牌必胡,不能杠
						seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 0;
					}else{
						//检查是否已躺牌相关的杠
						boolean bCanGang = false;
						for(List<CardAttrib> gangCards : findCards){
							CardAttrib gangCard = gangCards.get(0);
							if(gangCard.suit != seatAttrib.breakCard.suit
									|| gangCard.point != seatAttrib.breakCard.point){
								//杠的牌不是摸到的那张牌,不能杠
								continue;
							}
							
							List<CardAttrib> tmpFindCards = GameDefine.findAllBySuitPoint(seatAttrib.tangCardList, gangCard.suit, gangCard.point);
							if(null == tmpFindCards || tmpFindCards.isEmpty()){
								bCanGang = true;
							}
							if(bCanGang){
								//把杠牌移除后,是否叫牌和躺牌叫一样
								List<CardAttrib> tmpCopyCards = new ArrayList<>();
								tmpCopyCards.addAll(copyCards);
								GameDefine.removeAllBySuitPoint(tmpCopyCards, gangCards);
								List<CardAttrib> tingHuCards = GameDefine.isTingPai(table, tmpCopyCards, seatAttrib);
								
								for(CardAttrib tangCanHuCard : seatAttrib.tangCanHuList){
									List<CardAttrib> tmpFindtangCanHuCards = GameDefine.findAllBySuitPoint(tingHuCards, tangCanHuCard.suit, tangCanHuCard.point);
									if(null == tmpFindtangCanHuCards || tmpFindtangCanHuCards.isEmpty()){
										bCanGang = false;
										break;
									}
								}
							}
							
							if(bCanGang){
								break;
							}
						}
						if(bCanGang == false){
							seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 0;
						}
					}
				}
				
				if(table.ruleAttrib.bBaoJia 
						&& seatAttrib.baoJiaoState > 0 
						&& seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] > 0){
					boolean bCanGang = false;
					//有报叫规则且报了叫,杠了后有叫就可以杠
					for(List<CardAttrib> gangCards : findCards){						
						List<CardAttrib> tmpCopyCards = new ArrayList<>();
						tmpCopyCards.addAll(copyCards);
						GameDefine.removeAllBySuitPoint(tmpCopyCards, gangCards);
						List<CardAttrib> tingHuCards = GameDefine.isTingPai(table, tmpCopyCards, seatAttrib);
						if(tingHuCards != null && tingHuCards.isEmpty() == false){
							bCanGang = true;
							break;
						}
					}
					if(bCanGang == false){
						seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 0;
					}
				}
			}
		}else{
			//别人打的牌分析,胡,杠,碰
			List<CardAttrib> findCards = GameDefine.findAllBySuitPoint(copyCards, breakCard.suit, breakCard.point);
			if(findCards.size() == 3){
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_PENG] = 1;
			}else if(findCards.size() == 4){
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_PENG] = 1;
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 1;
			}
			if(table.ruleAttrib.bTangPai && seatAttrib.tangCardState > 0){
				//躺牌后一定不能碰
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_PENG] = 0;
				
				if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] > 0){
					if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
						seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 0;
					}else{
						//有杠,检查不能是和胡相关的杠
						CardAttrib gangCard = findCards.get(0);
						List<CardAttrib> tmpFindCards = GameDefine.findAllBySuitPoint(seatAttrib.tangCardList, gangCard.suit, gangCard.point);
						if(null == tmpFindCards || tmpFindCards.isEmpty()){
							//是不和躺牌相关的杠牌,把杠牌移除后,是否叫牌和躺牌叫一样
							List<CardAttrib> tmpCopyCards = new ArrayList<>();
							tmpCopyCards.addAll(copyCards);
							GameDefine.removeAllBySuitPoint(tmpCopyCards, findCards);
							List<CardAttrib> tingHuCards = GameDefine.isTingPai(table, tmpCopyCards, seatAttrib);
							
							for(CardAttrib tangCanHuCard : seatAttrib.tangCanHuList){
								List<CardAttrib> tmpFindtangCanHuCards = GameDefine.findAllBySuitPoint(tingHuCards, tangCanHuCard.suit, tangCanHuCard.point);
								if(null == tmpFindtangCanHuCards || tmpFindtangCanHuCards.isEmpty()){
									//叫牌改变了不能杠
									seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 0;
									break;
								}
							}
						}
					}
				}
			}
			if(table.ruleAttrib.bBaoJia 
					&& seatAttrib.baoJiaoState > 0){ 
				//报叫后一定不能碰
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_PENG] = 0;
				
				if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] > 0){
					//有报叫规则且报了叫,杠了后有叫就可以杠
					List<CardAttrib> tmpCopyCards = new ArrayList<>();
					tmpCopyCards.addAll(copyCards);
					GameDefine.removeAllBySuitPoint(tmpCopyCards, findCards);
					List<CardAttrib> tingHuCards = GameDefine.isTingPai(table, tmpCopyCards, seatAttrib);
					if(tingHuCards == null || tingHuCards.isEmpty()){
						seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 0;
					}
				}
			}
		}
		
		int tangChkNum = 8;		
		if(breakSource == GameDefine.STATE_TABLE_MOPAI 
				&& table.ruleAttrib.bTangPai
				&& seatAttrib.tangCardState <= 0 
				&& table.tableCards.size() > tangChkNum){
			//摸牌状态且有躺牌规则,且未躺牌底牌张数大于8张,
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
				if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
					seatAttrib.breakCardState[GameDefine.ACT_INDEX_TANG] = 1;
					seatAttrib.tangSource = 1;
				}else{
					//检查是否可下叫
					copyCards.clear();
					copyCards.addAll(seatAttrib.handCards);
					copyCards.add(breakCard);
					for(int index=0; index<copyCards.size(); index++){
						List<CardAttrib> theTmpList = new ArrayList<>();
						theTmpList.addAll(copyCards);
						theTmpList.remove(index);
						List<CardAttrib> tingCards = GameDefine.isTingPai(table, theTmpList, seatAttrib);
						if(null != tingCards && tingCards.isEmpty() == false){
							seatAttrib.breakCardState[GameDefine.ACT_INDEX_TANG] = 1;
							seatAttrib.tangSource = 1;
							//需要计算胡牌番数问题,暂时不考虑(0番就可以胡)
							break;
						}
					}
				}
			}
		}		
		
		if(table.tableCards.isEmpty()){
			//桌子上没有牌可摸了不能有杠动作
			seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 0;
		}
		boolean bBreak = seatAttrib.haveBreakState();
		if(bBreak){
			seatAttrib.breakCardState[GameDefine.ACT_INDEX_DROP] = 1;
			return 1;
		}
		copyCards = null;
		return 0;
	}

	public static int AnalysisBreakCard_lsmj(TableAttrib table, int seatIndex, int prevIndex, int breakSource){
		SeatAttrib seatAttrib = table.seats.get(seatIndex);
		List<CardAttrib> copyCards = new ArrayList<>();
		copyCards.addAll(seatAttrib.handCards);
		SeatAttrib prevSeat = table.seats.get(prevIndex);
		
		CardAttrib breakCard = prevSeat.breakCard;		
		//胡牌组合列表
		List<List<CardAttrib>> huComboList = null;
		seatAttrib.huPaiComboList = null;
		
		List<CardAttrib> yaoJiList_hands = GameDefine.findAllBySuitPoint(copyCards, GameDefine.SUIT_TYPE_TIAO, 1);
		int N = 0;
		if(table.ruleAttrib.bYaoJiRenYong){
			N = seatAttrib.getUnHandCardsYaoJiNum() + yaoJiList_hands.size();
			if(breakSource == GameDefine.STATE_TABLE_MOPAI
					&& breakCard.suit == GameDefine.SUIT_TYPE_TIAO 
					&& breakCard.point == 1){
				N++;
			}
			if(N == 4){
				//四幺鸡,只能胡,不管缺不缺
				if(table.ruleAttrib.bYaoJiRenYong){
					seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] = 1;
					seatAttrib.huPaiStyle[GameDefine.HUPAI_STYLE_4YAOJI] = 1;
					return 1;
				}			
			}
		}
				
		//保存原始手牌
		List<CardAttrib> savehandCards = new ArrayList<>();
		List<List<CardAttrib>> saveanGangCards = new ArrayList<>();
		List<List<CardAttrib>> savebaGangCards = new ArrayList<>();
		List<List<CardAttrib>> savedianGangCards = new ArrayList<>();
		List<List<CardAttrib>> savepengCards = new ArrayList<>();
		savehandCards.addAll(seatAttrib.handCards);
		saveanGangCards.addAll(seatAttrib.anGangCards);
		savebaGangCards.addAll(seatAttrib.baGangCards);
		savedianGangCards.addAll(seatAttrib.dianGangCards);
		savepengCards.addAll(seatAttrib.pengCards);
		
		
		if(table.ruleAttrib.bYaoJiRenYong){
			//替换杠碰牌中的幺鸡牌
			seatAttrib.replaceYaoJi();
			//移除手牌中的幺鸡牌
			GameDefine.removeAllBySuitPoint(copyCards, yaoJiList_hands);
		}
		
		boolean bDaQue = true;
		if(breakCard.suit == GameDefine.SUIT_TYPE_TIAO 
				&& breakCard.point == 1){
			//是幺鸡
			if(table.ruleAttrib.bYaoJiRenYong == false){
				//非幺鸡任用
				if(breakCard.suit == seatAttrib.unSuit){
					//未打缺
					bDaQue = false;
				}
			}
		}else{
			//不是幺鸡
			if(breakCard.suit == seatAttrib.unSuit){
				//未打缺
				bDaQue = false;
			}
		}
		
		if(bDaQue){
			bDaQue = GameDefine.isDaQue(copyCards, seatAttrib.unSuit);
		}
		if(bDaQue == false){
			//手牌未打缺不能胡
			seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] = 0;
		}else{			
			//组合幺鸡牌检查最大胡牌
			if(table.ruleAttrib.bYaoJiRenYong && yaoJiList_hands.size() > 0){
				List<CardAttrib> allSuitCards = GameDefine.initCards(seatAttrib.unSuit);
				int allN = allSuitCards.size();
				int allM = yaoJiList_hands.size();
				int []comboIndex = new int[allM];
				List<List<CardAttrib>> resultCardList = new ArrayList<>();
				GameDefine.combine(allSuitCards, allN, allM, comboIndex, allM, resultCardList);
				
				int currMaxHuPaiRate = -1;
				for(List<CardAttrib> resultCards : resultCardList){
					seatAttrib.handCards.clear();
					seatAttrib.handCards.addAll(copyCards);
					seatAttrib.handCards.addAll(resultCards);
					
					List<CardAttrib> tmpTingPaiCards = new ArrayList<>();
					List<List<CardAttrib>> tmpHuComboLists = null;
					if(breakCard.suit != GameDefine.SUIT_TYPE_TIAO || breakCard.point != 1){
						//表态牌不是幺鸡牌
						tmpHuComboLists = GameDefine.checkHuPai(table, seatAttrib, breakCard);
						if(null == tmpHuComboLists || tmpHuComboLists.isEmpty()){
							//不能胡牌
							continue;
						}
						tmpTingPaiCards.add(breakCard);
					}else{
						//表态牌是幺鸡牌,检查 是否听牌
						tmpTingPaiCards = GameDefine.isTingPai(table, seatAttrib.handCards, seatAttrib);
						if(null == tmpTingPaiCards || tmpTingPaiCards.isEmpty()){
							//没有下叫
							continue;
						}
					}
					
					for(CardAttrib theTingCard : tmpTingPaiCards){
						if(null == tmpHuComboLists){
							tmpHuComboLists = GameDefine.checkHuPai(table, seatAttrib, theTingCard);
						}
						if(null != tmpHuComboLists && tmpHuComboLists.isEmpty() == false){
							//可以胡
							//计算胡牌番数
							int []huPaiStyle = CardAnalysisManager.analysisHuPaiStyle2(table, seatAttrib, theTingCard, tmpHuComboLists);
							int huPaiType = 0;
							if(breakSource == GameDefine.STATE_TABLE_MOPAI){
								//自摸
								if(seatAttrib.gangType == GameDefine.GANG_TYPE_SELF_BAGANG
										|| seatAttrib.gangType == GameDefine.GANG_TYPE_SELF_ANGANG){
									huPaiType = GameDefine.HUPAI_TYPE_GANGFLOW;
								}else if(seatAttrib.gangType == GameDefine.GANG_TYPE_DIANGANG){
									huPaiType = GameDefine.HUPAI_TYPE_DIANGANGFLOW;
								}else{
									huPaiType = GameDefine.HUPAI_TYPE_ZIMO;
								}
							}else if(breakSource == GameDefine.STATE_TABLE_OUTCARD
									|| breakSource == GameDefine.STATE_TABLE_BREAKCARD){
								//别人点炮
								if(breakSource == GameDefine.STATE_TABLE_BREAKCARD){
									//抢杠胡
									huPaiType = GameDefine.HUPAI_TYPE_QIANGGANG;
								}else{
									if(prevSeat.gangType != GameDefine.GANG_TYPE_NONE){
										//杠上炮
										huPaiType = GameDefine.HUPAI_TYPE_DIANGANGPAO;
									}else{
										huPaiType = GameDefine.HUPAI_TYPE_DIANPAO;
									}
								}
							}
							int totalHuPaiRate = 0;
							totalHuPaiRate = CardAnalysisManager.getHuPaiStyleRate(table, huPaiStyle);
							totalHuPaiRate += CardAnalysisManager.getHuPaiTypeRate(table, huPaiType);
							totalHuPaiRate += seatAttrib.anGangCards.size();
							totalHuPaiRate += seatAttrib.baGangCards.size();
							totalHuPaiRate += seatAttrib.dianGangCards.size();
							
							List<CardAttrib> tmpCards = new ArrayList<>();
							for(List<CardAttrib> tmpHuComboList : tmpHuComboLists){
								tmpCards.addAll(tmpHuComboList);
							}
							for(List<CardAttrib> tmpHuComboList : seatAttrib.pengCards){
								tmpCards.addAll(tmpHuComboList);
							}
							
							List<List<CardAttrib>> four = GameDefine.findFourCards(tmpCards);
							totalHuPaiRate += four.size();
							boolean bTheCanHu = false;
							if(totalHuPaiRate >= table.ruleAttrib.canHuPaiFanShu){
								bTheCanHu = true;
								//检查不能胡过手胡的牌
								if(breakSource != GameDefine.STATE_TABLE_MOPAI){
									for(CardAttrib card : seatAttrib.guoShouHuCardList){
										if(card.suit == breakCard.suit
												&& card.point == breakCard.point){
											bTheCanHu = false;
											break;
										}
									}
								}
							}
							
							if(bTheCanHu && totalHuPaiRate > currMaxHuPaiRate){
								currMaxHuPaiRate = totalHuPaiRate;
								seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] = 1;
								seatAttrib.huPaiComboList = tmpHuComboLists;
								System.arraycopy(huPaiStyle, 0, seatAttrib.huPaiStyle, 0, GameDefine.HUPAI_STYLE_MAX_END);
							}
							tmpHuComboLists = null;
						}
					}					
				}//end of for
			}else{
				//手牌中没有幺鸡或未开启幺鸡任用
				if(table.ruleAttrib.bYaoJiRenYong 
						&& breakCard.suit == GameDefine.SUIT_TYPE_TIAO 
						&& breakCard.point == 1){
					//表态牌是幺鸡且幺鸡任用
					List<CardAttrib> chkCanHuCards = GameDefine.isTingPai(table, seatAttrib.handCards, seatAttrib);
					if(null != chkCanHuCards && chkCanHuCards.isEmpty()==false){
						//手牌是下叫的,找到最大番数胡牌
						int currMaxHuPaiRate = -1;
						for(CardAttrib theTingCard : chkCanHuCards){
							List<List<CardAttrib>> tmpHuComboLists = GameDefine.checkHuPai(table, seatAttrib, theTingCard);
							if(null != tmpHuComboLists && tmpHuComboLists.isEmpty() == false){
								//可以胡
								//计算胡牌番数
								int []huPaiStyle = CardAnalysisManager.analysisHuPaiStyle2(table, seatAttrib, theTingCard, tmpHuComboLists);
								int huPaiType = 0;
								if(breakSource == GameDefine.STATE_TABLE_MOPAI){
									//自摸
									if(seatAttrib.gangType == GameDefine.GANG_TYPE_SELF_BAGANG
											|| seatAttrib.gangType == GameDefine.GANG_TYPE_SELF_ANGANG){
										huPaiType = GameDefine.HUPAI_TYPE_GANGFLOW;
									}else if(seatAttrib.gangType == GameDefine.GANG_TYPE_DIANGANG){
										huPaiType = GameDefine.HUPAI_TYPE_DIANGANGFLOW;
									}else{
										huPaiType = GameDefine.HUPAI_TYPE_ZIMO;
									}
								}else if(breakSource == GameDefine.STATE_TABLE_OUTCARD
										|| breakSource == GameDefine.STATE_TABLE_BREAKCARD){
									//别人点炮
									if(breakSource == GameDefine.STATE_TABLE_BREAKCARD){
										//抢杠胡
										huPaiType = GameDefine.HUPAI_TYPE_QIANGGANG;
									}else{
										if(prevSeat.gangType != GameDefine.GANG_TYPE_NONE){
											//杠上炮
											huPaiType = GameDefine.HUPAI_TYPE_DIANGANGPAO;
										}else{
											huPaiType = GameDefine.HUPAI_TYPE_DIANPAO;
										}
									}
								}
								int totalHuPaiRate = 0;
								totalHuPaiRate = CardAnalysisManager.getHuPaiStyleRate(table, huPaiStyle);
								totalHuPaiRate += CardAnalysisManager.getHuPaiTypeRate(table, huPaiType);
								totalHuPaiRate += seatAttrib.anGangCards.size();
								totalHuPaiRate += seatAttrib.baGangCards.size();
								totalHuPaiRate += seatAttrib.dianGangCards.size();
								
								List<CardAttrib> tmpCards = new ArrayList<>();
								for(List<CardAttrib> tmpHuComboList : tmpHuComboLists){
									tmpCards.addAll(tmpHuComboList);
								}
								for(List<CardAttrib> tmpHuComboList : seatAttrib.pengCards){
									tmpCards.addAll(tmpHuComboList);
								}
								
								List<List<CardAttrib>> four = GameDefine.findFourCards(tmpCards);
								totalHuPaiRate += four.size();
								boolean bTheCanHu = false;
								if(totalHuPaiRate >= table.ruleAttrib.canHuPaiFanShu){
									bTheCanHu = true;
									//检查不能胡过手胡的牌
									if(breakSource != GameDefine.STATE_TABLE_MOPAI){
										for(CardAttrib card : seatAttrib.guoShouHuCardList){
											if(card.suit == breakCard.suit
													&& card.point == breakCard.point){
												bTheCanHu = false;
												break;
											}
										}
									}
								}
								
								if(bTheCanHu && totalHuPaiRate > currMaxHuPaiRate){
									currMaxHuPaiRate = totalHuPaiRate;
									seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] = 1;
									seatAttrib.huPaiComboList = tmpHuComboLists;
									System.arraycopy(huPaiStyle, 0, seatAttrib.huPaiStyle, 0, GameDefine.HUPAI_STYLE_MAX_END);
								}
							}
						}
					}
				}else{
					huComboList = GameDefine.checkHuPai(table, seatAttrib, breakCard);
					if(null != huComboList && huComboList.isEmpty() == false){
						//可以胡牌
						seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] = 1;
						//检查不能胡过手胡的牌
						if(breakSource != GameDefine.STATE_TABLE_MOPAI){
							for(CardAttrib card : seatAttrib.guoShouHuCardList){
								if(card.suit == breakCard.suit
										&& card.point == breakCard.point){
									seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] = 0;
									break;
								}
							}
						}
						
						if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
							int []huPaiStyle = CardAnalysisManager.analysisHuPaiStyle2(table, seatAttrib, breakCard, huComboList);
							System.arraycopy(huPaiStyle, 0, seatAttrib.huPaiStyle, 0, GameDefine.HUPAI_STYLE_MAX_END);
							seatAttrib.huPaiComboList = huComboList;
						}
					}
				}
			}
		}
		
		//检查杠碰牌
		if(breakCard.suit == GameDefine.SUIT_TYPE_TIAO && breakCard.point == 1){
			if(table.ruleAttrib.bYaoJiRenYong){
				yaoJiList_hands.add(breakCard);
			}else{
				copyCards.add(breakCard);
			}
		}else{
			copyCards.add(breakCard);
		}		
		
		//移除未打缺的牌
		GameDefine.removeAllBySuit(copyCards, seatAttrib.unSuit);		
		if(seatIndex == prevIndex){
			//自摸的牌,只能有杠
			List<CardAttrib> copyCards1 = new ArrayList<>();
			copyCards1.addAll(copyCards);
			for(List<CardAttrib> tmpHuComboList : seatAttrib.pengCards){
				copyCards1.addAll(tmpHuComboList);
			}
			List<List<CardAttrib>> gangCardLists = GameDefine.findFourCards(copyCards1);
			if(null != gangCardLists && gangCardLists.isEmpty() == false){
				//有暗杠或巴杠
				seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 1;
			}
			if(table.ruleAttrib.bYaoJiRenYong && yaoJiList_hands.size() == 3){
				//有三个幺鸡一定可杠
				if(copyCards1.size() > 0){
					seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 1;
				}
			}else if(table.ruleAttrib.bYaoJiRenYong && yaoJiList_hands.size() == 2){
				//有二个幺鸡有任意对子就可以杠
				List<List<CardAttrib>> tmpFindCards = GameDefine.findDoubleCards(copyCards);
				if(tmpFindCards.size() > 0 || seatAttrib.pengCards.size() > 0){
					seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 1;
				}
			}else if(table.ruleAttrib.bYaoJiRenYong && yaoJiList_hands.size() == 1){
				//有一个幺鸡有任意三条就可以杠
				List<List<CardAttrib>> tmpFindCards = GameDefine.findThreeCards(copyCards);
				if(tmpFindCards.size() > 0 || seatAttrib.pengCards.size() > 0){
					seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 1;
				}
			}
		}else{
			//别人打的牌,可杠,碰
			if(breakCard.suit != seatAttrib.unSuit){
				List<CardAttrib> gangCardLists = GameDefine.findAllBySuitPoint(copyCards, breakCard.suit, breakCard.point);
				if(gangCardLists.size() == 3){
					//有碰
					seatAttrib.breakCardState[GameDefine.ACT_INDEX_PENG] = 1;
				}else if(gangCardLists.size() == 4){
					//有杠碰
					seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 1;
					seatAttrib.breakCardState[GameDefine.ACT_INDEX_PENG] = 1;
				}
				
				if(table.ruleAttrib.bYaoJiRenYong && yaoJiList_hands.size() == 3){
					//有三个幺鸡一定可杠
					seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 1;
					seatAttrib.breakCardState[GameDefine.ACT_INDEX_PENG] = 1;
				}else if(table.ruleAttrib.bYaoJiRenYong && yaoJiList_hands.size() == 2){
					//有二个幺鸡有指定的对子就可以杠
					seatAttrib.breakCardState[GameDefine.ACT_INDEX_PENG] = 1;
					List<CardAttrib> tmpFindCards = GameDefine.findAllBySuitPoint(copyCards, breakCard.suit, breakCard.point);
					if(tmpFindCards.size() >= 2){
						seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 1;
					}
				}else if(table.ruleAttrib.bYaoJiRenYong && yaoJiList_hands.size() == 1){
					//有一个幺鸡有三条就可以杠
					List<CardAttrib> tmpFindCards = GameDefine.findAllBySuitPoint(copyCards, breakCard.suit, breakCard.point);
					if(tmpFindCards.size() >= 3){
						seatAttrib.breakCardState[GameDefine.ACT_INDEX_PENG] = 1;
						seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 1;
					}else if(tmpFindCards.size() >= 2){
						seatAttrib.breakCardState[GameDefine.ACT_INDEX_PENG] = 1;
					}
				}
			}			
		}
		
		//恢复原手牌
		seatAttrib.handCards.clear();
		seatAttrib.handCards.addAll(savehandCards);
		seatAttrib.anGangCards.clear();
		seatAttrib.anGangCards.addAll(saveanGangCards);
		seatAttrib.baGangCards.clear();
		seatAttrib.baGangCards.addAll(savebaGangCards);
		seatAttrib.dianGangCards.clear();
		seatAttrib.dianGangCards.addAll(savedianGangCards);
		seatAttrib.pengCards.clear();
		seatAttrib.pengCards.addAll(savepengCards);
		
		if(table.ruleAttrib.bYaoJiRenYong && seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
			//检查是否无幺鸡
			N = 0;
			for(CardAttrib card : savehandCards){
				if(card.suit == GameDefine.SUIT_TYPE_TIAO
						&& card.point == 1){
					N++;
				}
			}
			for(List<CardAttrib> cards : saveanGangCards){
				for(CardAttrib card : cards){
					if(card.suit == GameDefine.SUIT_TYPE_TIAO
							&& card.point == 1){
						N++;
					}
				}
			}
			for(List<CardAttrib> cards : savebaGangCards){
				for(CardAttrib card : cards){
					if(card.suit == GameDefine.SUIT_TYPE_TIAO
							&& card.point == 1){
						N++;
					}
				}
			}
			for(List<CardAttrib> cards : savedianGangCards){
				for(CardAttrib card : cards){
					if(card.suit == GameDefine.SUIT_TYPE_TIAO
							&& card.point == 1){
						N++;
					}
				}
			}
			for(List<CardAttrib> cards : savepengCards){
				for(CardAttrib card : cards){
					if(card.suit == GameDefine.SUIT_TYPE_TIAO
							&& card.point == 1){
						N++;
					}
				}
			}
			
			if(N == 0){
				seatAttrib.huPaiStyle[GameDefine.HUPAI_STYLE_0YAOJI] = 1;
			}
		}
		
		
		if(table.tableCards.isEmpty()){
			//桌子上没有牌可摸了不能有杠动作
			seatAttrib.breakCardState[GameDefine.ACT_INDEX_GANG] = 0;
		}
		
		boolean bBreak = seatAttrib.haveBreakState();
		if(bBreak){
			seatAttrib.breakCardState[GameDefine.ACT_INDEX_DROP] = 1;
			return 1;
		}
		
		return 0;
	}
}
