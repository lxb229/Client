package com.palmjoys.yf1b.act.majiang.model.notify.table;

import java.util.ArrayList;
import java.util.List;

public class Notify_Table_GameStateVo_SwapCard{
	//座位列表
	public List<SwapCardInner> seats = new ArrayList<>();

	public void addSeat(int seatIndex, int btState){
		SwapCardInner item = new SwapCardInner();
		item.seatIndex = seatIndex;
		item.btState = btState;
		this.seats.add(item);
	}
	
	public class SwapCardInner{
		//座位下标
		public int seatIndex;
		//表态状态
		public int btState;
	}
}
