package com.palmjoys.yf1b.act.majiang.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatAttrib {
	//座位下标号(从0开始)
	public int seatIndex;
	//座位玩家Id(0=无玩家)
	public long accountId;
	//玩家当前手牌
	public List<CardAttrib> handCards = new ArrayList<>();
	//摸的牌
	public CardAttrib moPaiCard;
	//打出的牌
	public CardAttrib outCard;
	//胡杠碰的牌
	public CardAttrib breakCard;
	//座位暗杠的牌
	public List<List<CardAttrib>> anGangCards = new ArrayList<>();
	//座位巴杠的牌
	public List<List<CardAttrib>> baGangCards = new ArrayList<>();
	//座位点杠的牌
	public List<List<CardAttrib>> dianGangCards = new ArrayList<>();
	//座位碰的牌
	public List<List<CardAttrib>> pengCards = new ArrayList<>();
	//玩家打出别人不要的牌
	public List<CardAttrib> outUnUseCards = new ArrayList<>();
	//玩家当前已胡的牌
	public List<CardAttrib> huCards = new ArrayList<>();
	//出牌手数
	public int outHandNum;
	//摸牌手数
	public int moPaiHandNum;
	//当前分数
	public int score;
	//表态状态(-1=放弃,0=等待,1=已表态)
	public int btState;
	//缺花色(1=万,2=筒,3=条)
	public int unSuit;
	//换过来的牌数据
	public List<CardAttrib> swapCards = new ArrayList<>();
	//自已换牌表态数据
	public List<CardAttrib> swapBtCards = new ArrayList<>();
	//胡牌方式(0=未胡牌,1=自摸 ,2=点炮,3=抢杠胡,4=自摸杠上花,5=点杠上花胡,6=点杠上炮,7=查叫)
	public int huPaiType;	
	//胡牌顺序
	public int huPaiIndex;
	//胡的牌牌型(例清一色,对对胡等)
	public int[] huPaiStyle;
	//胡杠碰吃状态
	public int[] breakCardState;
	//胡杠碰吃过表态结果(0=胡,1=杠,2=碰,3=吃,4=躺,5=过)
	public int breakBtState;
	//胡牌要赢的座位信息
	public List<SeatHuPaiWinAttrib> huPaiWinList = new ArrayList<>();
	//巴杠收取的座位信息
	public List<List<Integer>> baGangWinList = new ArrayList<>();
	//暗杠收取的座位信息
	public List<List<Integer>> anGangWinList = new ArrayList<>();
	//点杠收取的座位信息
	public List<Integer> dianGangWinList = new ArrayList<>();
	//点杠巴到烫收取的座位信息
	public List<List<Integer>> dianGangJiaJiaYouWinList = new ArrayList<>();
	//杠牌类型(0=非杠,1=自摸巴杠,2=自摸暗杠,3=点杠)
	public int gangType;
	//杠牌座位
	public int gangSeatIndex;
	//统计数据
	public GameStatisticsAttrib statistAttrib;
	//桌子解散状态中断保存数据
	public int btStateSave;
	//躺牌状态(0=未躺牌,1=已躺牌)
	public int tangCardState;
	//躺的牌
	public List<CardAttrib> tangCardList = new ArrayList<>();
	//躺了可胡的牌
	public List<CardAttrib> tangCanHuList = new ArrayList<>();
	//躺牌后选择打出的牌
	public int tangOutCard;
	//座位单局结算分数信息(KEY=玩家Id, VALUE=分数)
	public Map<Long, SettementSeatScore> seatScore = new HashMap<>();
	//躺牌来源(1=摸牌,2=碰牌)
	public int tangSource;
	//过手胡牌列表
	public List<CardAttrib> guoShouHuCardList = new ArrayList<>();
	//转雨数据
	public List<SeatHuJiaoZhuYiAttrib> zhuanYuSeats = new ArrayList<>();
	//报叫状态(-1=没有报叫状态,0=可以报叫,1=已报叫)
	public int baoJiaoState;
	//是否点炮没胡
	public boolean bDianPaoNoHu;
	//乐山麻将杠的类型(1=巴杠,2=暗杠)
	public int lsmjGangType;
	//杠牌是否是替换牌
	public boolean bUsedReplaceCard;
	//胡牌组合列表
	public List<List<CardAttrib>> huPaiComboList;	
	//南充麻将漂的个数
	public int piaoNum;	
	//托管状态
	public int trusteeshipState;
	
	public SeatAttrib(int seatIndex){
		this.seatIndex = seatIndex;
		this.accountId = 0;
		this.score = 0;
		this.btState = 0;
		this.unSuit = 0;
		this.huPaiType = GameDefine.HUPAI_TYPE_NONE;
		this.breakCardState = new int[GameDefine.ACT_INDEX_VAILD];
		this.breakBtState = GameDefine.ACT_INDEX_DROP;
		this.statistAttrib = new GameStatisticsAttrib();
		this.huPaiStyle = new int[GameDefine.HUPAI_STYLE_MAX_END];
		this.moPaiCard = null;
		this.outCard = null;
		this.breakCard = null;
		this.tangSource = 0;
		this.baoJiaoState = -1;
		this.bDianPaoNoHu = false;
		this.lsmjGangType = GameDefine.GANG_TYPE_NONE;
		this.huPaiComboList = null;
		this.bUsedReplaceCard = false;
		this.piaoNum = 0;
	}
	
	public void resetAll(){
		this.handCards.clear();
		this.anGangCards.clear();
		this.baGangCards.clear();
		this.dianGangCards.clear();
		this.pengCards.clear();
		this.outUnUseCards.clear();
		this.huCards.clear();
		this.btState = 0;
		this.unSuit = 0;
		this.outHandNum = 0;
		this.moPaiHandNum = 0;
		this.swapCards.clear();
		this.swapBtCards.clear();
		this.huPaiType = GameDefine.HUPAI_TYPE_NONE;
		this.huPaiIndex = 0;
		Arrays.fill(this.huPaiStyle, 0);
		this.breakBtState = GameDefine.ACT_INDEX_DROP;
		this.huPaiWinList.clear();
		this.baGangWinList.clear();
		this.anGangWinList.clear();
		this.dianGangWinList.clear();
		this.dianGangJiaJiaYouWinList.clear();
		this.gangType = GameDefine.GANG_TYPE_NONE;
		this.gangSeatIndex = 0;
		this.resetBreakState();
		this.moPaiCard = null;
		this.outCard = null;
		this.breakCard = null;
		this.statistAttrib.reset();
		this.tangCardState = 0;
		this.tangCardList.clear();
		this.tangCanHuList.clear();
		this.seatScore.clear();
		this.tangSource = 0;
		this.guoShouHuCardList.clear();
		this.zhuanYuSeats.clear();
		this.baoJiaoState = -1;
		this.bDianPaoNoHu = false;
		this.lsmjGangType = GameDefine.GANG_TYPE_NONE;
		this.huPaiComboList = null;
		this.bUsedReplaceCard = false;
		this.piaoNum = 0;
	}
	
	public void resetBreakState(){
		int val = 0;
		Arrays.fill(this.breakCardState, val);
	}
	
	public boolean haveBreakState(){
		int total = 0;
		for(int val : breakCardState){
			total += val;
		}
		if(total > 0){
			return true;
		}
		return false;
	}
	
	//从杠牌,碰牌替换幺鸡牌
	public void replaceYaoJi(){
		List<List<CardAttrib>> copyCardList = new ArrayList<>();		
		copyCardList.clear();
		copyCardList.addAll(this.anGangCards);
		this.anGangCards.clear();
		for(int i=0; i<copyCardList.size(); i++){
			List<CardAttrib> splitCardList = copyCardList.get(i);
			CardAttrib findCard = null;
			for(CardAttrib card : splitCardList){
				if(card.point != 1 || card.suit != GameDefine.SUIT_TYPE_TIAO){
					findCard = card;
					break;
				}
			}
			if(null != findCard){
				List<CardAttrib> newCardList = new ArrayList<>();
				for(int j=0; j<4; j++){
					newCardList.add(GameDefine.makeCard(findCard.cardId));
				}
				this.anGangCards.add(newCardList);
			}
		}
		
		copyCardList.clear();
		copyCardList.addAll(this.baGangCards);
		this.baGangCards.clear();
		for(int i=0; i<copyCardList.size(); i++){
			List<CardAttrib> splitCardList = copyCardList.get(i);
			CardAttrib findCard = null;
			for(CardAttrib card : splitCardList){
				if(card.point != 1 || card.suit != GameDefine.SUIT_TYPE_TIAO){
					findCard = card;
					break;
				}
			}
			if(null != findCard){
				List<CardAttrib> newCardList = new ArrayList<>();
				for(int j=0; j<4; j++){
					newCardList.add(GameDefine.makeCard(findCard.cardId));
				}
				this.baGangCards.add(newCardList);
			}
		}
		
		copyCardList.clear();
		copyCardList.addAll(this.dianGangCards);
		this.dianGangCards.clear();
		for(int i=0; i<copyCardList.size(); i++){
			List<CardAttrib> splitCardList = copyCardList.get(i);
			CardAttrib findCard = null;
			for(CardAttrib card : splitCardList){
				if(card.point != 1 || card.suit != GameDefine.SUIT_TYPE_TIAO){
					findCard = card;
					break;
				}
			}
			if(null != findCard){
				List<CardAttrib> newCardList = new ArrayList<>();
				for(int j=0; j<4; j++){
					newCardList.add(GameDefine.makeCard(findCard.cardId));
				}
				this.dianGangCards.add(newCardList);
			}
		}
		
		copyCardList.clear();
		copyCardList.addAll(this.pengCards);
		this.pengCards.clear();
		for(int i=0; i<copyCardList.size(); i++){
			List<CardAttrib> splitCardList = copyCardList.get(i);
			CardAttrib findCard = null;
			for(CardAttrib card : splitCardList){
				if(card.point != 1 || card.suit != GameDefine.SUIT_TYPE_TIAO){
					findCard = card;
					break;
				}
			}
			if(null != findCard){
				List<CardAttrib> newCardList = new ArrayList<>();
				for(int j=0; j<3; j++){
					newCardList.add(GameDefine.makeCard(findCard.cardId));
				}
				this.pengCards.add(newCardList);
			}
		}
	}
	
	//获取杠牌,碰牌幺鸡数量
	public int getUnHandCardsYaoJiNum(){
		int N = 0;
		for(List<CardAttrib> cards : this.anGangCards){
			for(CardAttrib card : cards){
				if(card.suit == GameDefine.SUIT_TYPE_TIAO
						&& card.point == 1){
					N++;
				}
			}
		}
		for(List<CardAttrib> cards : this.baGangCards){
			for(CardAttrib card : cards){
				if(card.suit == GameDefine.SUIT_TYPE_TIAO
						&& card.point == 1){
					N++;
				}
			}
		}
		for(List<CardAttrib> cards : this.dianGangCards){
			for(CardAttrib card : cards){
				if(card.suit == GameDefine.SUIT_TYPE_TIAO
						&& card.point == 1){
					N++;
				}
			}
		}
		for(List<CardAttrib> cards : this.pengCards){
			for(CardAttrib card : cards){
				if(card.suit == GameDefine.SUIT_TYPE_TIAO
						&& card.point == 1){
					N++;
				}
			}
		}
		
		return N;
	}
	
}
