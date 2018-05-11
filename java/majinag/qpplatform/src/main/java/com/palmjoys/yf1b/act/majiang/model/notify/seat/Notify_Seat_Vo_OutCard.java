package com.palmjoys.yf1b.act.majiang.model.notify.seat;

import java.util.List;

public class Notify_Seat_Vo_OutCard {
	//座位下标
	public int seatIndex;
	//表态状态(-1=放弃,0=等待,1=已表态)
	public int btState;
	//摸的牌
	public int moPaiCard;
	//打出的牌
	public int outCard;
	//手牌长度
	public int handCardsLen;
	//玩家当前手牌
	public List<Integer> handCards;
}
