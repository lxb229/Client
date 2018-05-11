package com.palmjoys.yf1b.act.majiang.model;

import java.util.ArrayList;
import java.util.List;

//单局结算数据属性
public class SettlementOnceVo {
	//帐号Id
	public String accountId;
	//头像
	public String headImg;
	//呢称
	public String nick;
	//庄家标志(0=非庄家,1=是庄家)
	public int banker;
	//玩家当前手牌
	public List<Integer> handCards = new ArrayList<>();
	//座位暗杠的牌
	public List<List<Integer>> anGangCards = new ArrayList<>();
	//座位巴杠的牌
	public List<List<Integer>> baGangCards = new ArrayList<>();
	//座位点杠的牌
	public List<List<Integer>> dianGangCards = new ArrayList<>();
	//座位碰的牌
	public List<List<Integer>> pengCards = new ArrayList<>();
	//玩家当前已胡的牌
	public List<Integer> huCards = new ArrayList<>();
	//番数
	public int rate;
	//本局分数
	public int score;
	//胡牌顺序(0=未胡牌)
	public int huPaiIndex;
	//胡牌描述
	public String huPaiDesc;
	//座位单局结算分数
	public List<SettementSeatScore> seatScore = new ArrayList<>();
	//我的漂牌数量
	public int myPiaoNum;
}
