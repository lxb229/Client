package com.palmjoys.yf1b.act.majiang.model.notify.seat;

import java.util.ArrayList;
import java.util.List;

public class Notify_Seat_Vo_Swap {
	//换牌方式(0=[0->1,1->2,2->3,3->0],1=[0->3,3->2,2->1,1->0],2=[0->2,1->3])
	public int swapCardType;
	//座位列表
	public List<SwapInner> seats = new ArrayList<>();
	
	public void addSeat(int seatIndex, int handCardsLen, List<Integer> handCards, List<Integer> swapCards, int btState){
		SwapInner item = new SwapInner();
		item.seatIndex = seatIndex;
		item.handCardsLen = handCardsLen;
		item.handCards = handCards;
		item.swapCards = swapCards;
		item.btState = btState;
		this.seats.add(item);
	}
	
	public class SwapInner{
		//座位
		public int seatIndex;
		//手牌长度
		public int handCardsLen;
		//玩家当前手牌(只能本座位玩家有效)
		public List<Integer> handCards;
		//换到的牌数据(只能本座位玩家有效)
		public List<Integer> swapCards;
		//座位表态状态
		public int btState;
	}
}
