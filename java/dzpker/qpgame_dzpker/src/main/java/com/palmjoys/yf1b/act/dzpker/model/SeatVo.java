package com.palmjoys.yf1b.act.dzpker.model;

import java.util.ArrayList;
import java.util.List;

public class SeatVo {
	//座位下标
	public int seatIndex;
	//帐号Id
	public String accountId;
	//明星号
	public String starNO;
	//呢称
	public String nick;
	//头像
	public String headImg;
	//手牌
	public List<Integer> handCards = new ArrayList<>();
	//当前筹码
	public String currMoney;
	//每轮下注筹码
	public String betMoney;
	//座位表态状态(-1=放弃,0=等待,1=已表态)
	public int btState;
	//座位表态结果(1=弃牌,2=过牌,3=跟注,4=加注,5=全下, btState=-1也表示弃牌)
	public int btResult;
	//是否参加本局游戏(1=参加游戏)
	public int bGamed;
	//当前牌的牌型
	public int cardType;
	//闭眼盲注开启状态(1=可以闭眼盲表态)
	public int straddle;
	//闭眼盲注标志(1=已表态闭眼盲)
	public int straddleFlag;

}
