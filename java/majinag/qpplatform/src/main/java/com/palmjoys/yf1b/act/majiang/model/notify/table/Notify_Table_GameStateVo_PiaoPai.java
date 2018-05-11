package com.palmjoys.yf1b.act.majiang.model.notify.table;

import java.util.ArrayList;
import java.util.List;

public class Notify_Table_GameStateVo_PiaoPai{
	
	public List<PiaoPaiInner> seats = new ArrayList<>();
	
	public void addSeat(int seatIndex, int btState){
		PiaoPaiInner item = new PiaoPaiInner();
		item.seatIndex = seatIndex;
		item.btState = btState;
		this.seats.add(item);
	}
	
	public class PiaoPaiInner{
		//座位
		public int seatIndex;
		//表态状态
		public int btState;
	}
}
