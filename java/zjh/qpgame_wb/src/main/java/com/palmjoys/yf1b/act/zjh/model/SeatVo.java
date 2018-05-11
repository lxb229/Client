package com.palmjoys.yf1b.act.zjh.model;

import java.util.ArrayList;
import java.util.List;

public class SeatVo {
	//座位下标
	public int seatIndex;
	//玩家Id(0=无人)
	public String accountId;
	//头像
	public String headImg;
	//呢称
	public String nick;
	//明星号
	public String starNO;
	//座位金币
	public int money;
	//座位手牌
	public List<Byte> handCards = new ArrayList<>();
	//表态状态(-1=放弃,0=等待,1=表态)
	public int btState;
	//下注金额
	public int betMoney;
	//表态结果(0=弃牌,1=看牌,2=比牌,3=全押,4=跟注,5=加注)
	public int btVal;
	//看牌标志(0=未看牌,1=已看牌)
	public int looked;
	//是否参加本局游戏(0=未参加,1=参加)
	public int bGamed;
}
