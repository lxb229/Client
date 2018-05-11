package com.palmjoys.yf1b.act.majiang.model;

import java.util.ArrayList;
import java.util.List;

public class SeatVo {
	//座位下标
	public int seatIndex;
	//座位玩家Id
	public String accountId;
	//头像
	public String headImg;
	//呢称
	public String nick;
	//性别(0=保密,1=男,2=女)
	public int sex;
	//在线状态(0=离线,1=在线)
	public int onLine;
	//手牌长度
	public int handCardsLen;
	//座位暗杠的牌
	public List<List<Integer>> anGangCards = new ArrayList<>();
	//座位巴杠的牌
	public List<List<Integer>> baGangCards = new ArrayList<>();
	//座位点杠的牌
	public List<List<Integer>> dianGangCards = new ArrayList<>();
	//座位碰的牌
	public List<List<Integer>> pengCards = new ArrayList<>();
	//玩家打出别人不要的牌
	public List<Integer> outUnUseCards = new ArrayList<>();
	//玩家当前已胡的牌
	public List<Integer> huCards = new ArrayList<>();
	//当前分数
	public int score;
	//表态状态(-1=放弃,0=等待,1=已表态)
	public int btState;
	//缺花色(1=万,2=筒,3=条)
	public int unSuit;
	//胡牌方式(0=未胡牌,1=自摸 ,2=点炮,3=抢杠胡,4=自摸杠上花,5=点杠上花胡,6=点杠上炮,7=查叫)
	public int huPaiType;
	//胡牌顺序
	public int huPaiIndex;
	//摸的牌
	public int moPaiCard;
	//打出的牌
	public int outCard;
	//杠牌类型(0=非杠,1=自摸巴杠,2=自摸暗杠,3=点杠)
	public int gangType;
	//躺牌状态(0=未躺牌,1=已躺牌)
	public int tangCardState;
	//躺的牌
	public List<Integer> tangCardList = new ArrayList<>();
	//报叫状态(-1=无报叫状态,0=等待报叫,1=已报叫)
	public int baojiaoState;
	//漂牌数量
	public int piaoNum;
	//托管状态(0=未托管,1=托管)
	public int trusteeshipState;
	
	//以下数据只能自已可见,别人不可见
	//玩家当前手牌
	public List<Integer> handCards = new ArrayList<>();
	//换到的牌数据
	public List<Integer> swapCards = new ArrayList<>();
	//胡杠碰的牌
	public int breakCard;
	//胡杠碰吃状态
	public List<Integer> breakCardState = new ArrayList<>();
	
	public SeatVo(){
		this.seatIndex = 0;
		this.accountId = "";
		this.headImg = "";
		this.nick = "";
		this.sex = 0;
		this.onLine = 0;
		this.handCardsLen = 0;
		this.score = 0;
		this.btState = 0;
		this.unSuit = 1;
		this.huPaiType = 0;
		this.huPaiIndex = 0;
		this.moPaiCard = 0;
		this.outCard = 0;
		this.breakCard = 0;
		this.baojiaoState = -1;
		this.piaoNum = 0;
	}
	
	public void reset(){
		this.handCardsLen = 0;
		this.anGangCards.clear();
		this.baGangCards.clear();
		this.dianGangCards.clear();
		this.pengCards.clear();
		this.outUnUseCards.clear();
		this.huCards.clear();
		this.btState = 0;
		this.unSuit = 1;
		this.huPaiType = 0;
		this.huPaiIndex = 0;
		this.handCards.clear();
		this.swapCards.clear();
		this.moPaiCard = 0;
		this.outCard = 0;
		this.breakCard = 0;
		this.breakCardState.clear();
		this.tangCardList.clear();
		this.baojiaoState = -1;
		this.piaoNum = 0;
	}
}
