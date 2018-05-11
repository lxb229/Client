package com.palmjoys.yf1b.act.majiang.model.notify.table;

import java.util.ArrayList;
import java.util.List;

public class Notify_Table_GameStateVo_FaiPai{
	//发牌数据
	public List<FaiPaiInner> seats = new ArrayList<>();
	
	
	public void addSeatPai(int seatIndex, int handCardsLen, List<Integer> handCards){
		FaiPaiInner item = new FaiPaiInner();
		item.seatIndex = seatIndex;
		item.handCardsLen = handCardsLen;
		item.handCards = handCards;
		this.seats.add(item);
	}
	
	public class FaiPaiInner{
		//座位下标
		public int seatIndex;
		//手牌长度
		public int handCardsLen;
		//玩家当前手牌(自已座位才有效,其它座位=null)
		public List<Integer> handCards;
	}
}
