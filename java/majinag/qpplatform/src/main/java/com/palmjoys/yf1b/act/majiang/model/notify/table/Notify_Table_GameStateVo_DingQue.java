package com.palmjoys.yf1b.act.majiang.model.notify.table;

import java.util.ArrayList;
import java.util.List;

public class Notify_Table_GameStateVo_DingQue{
	
	public List<DingQueInner> seats = new ArrayList<>();

	public void addSeat(int seatIndex, int btState){
		DingQueInner item = new DingQueInner();
		item.seatIndex = seatIndex;
		item.btState = btState;
		this.seats.add(item);
	}
	
	public class DingQueInner{
		//座位下标
		public int seatIndex;
		//表态状态
		public int btState;
	}
}
