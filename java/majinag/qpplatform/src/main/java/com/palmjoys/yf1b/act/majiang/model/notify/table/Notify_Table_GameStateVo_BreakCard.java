package com.palmjoys.yf1b.act.majiang.model.notify.table;

import java.util.ArrayList;
import java.util.List;

public class Notify_Table_GameStateVo_BreakCard{
	//座位列表
	public List<BreakCardInner> seats = new ArrayList<>();

	
	public void addSeat(int seatIndex, int btState, List<Integer> breakCardState, int breakCard){
		BreakCardInner item = new BreakCardInner();
		item.seatIndex = seatIndex;
		item.breakCardState = breakCardState;
		item.breakCard = breakCard;
		this.seats.add(item);
	}
	
	public class BreakCardInner{
		//座位
		public int seatIndex;
		//表态状态
		public int btState;
		//胡杠碰吃状态
		public List<Integer> breakCardState;
		//胡杠碰的牌
		public int breakCard;
	}
}
