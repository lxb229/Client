package com.palmjoys.yf1b.act.majiang.model.notify.table;

import java.util.ArrayList;
import java.util.List;

public class Notify_Table_GameStateVo_BaoJiao{
	
	public List<BaoJiaoInner> seats = new ArrayList<>();
	
	public void addSeat(int seatIndex, int baojiaoState){
		BaoJiaoInner item = new BaoJiaoInner();
		item.seatIndex = seatIndex;
		item.baojiaoState = baojiaoState;
		this.seats.add(item);
	}
	
	public class BaoJiaoInner{
		public int seatIndex;
		//报叫状态(-1=无报叫状态,0=等待报叫,1=已报叫)
		public int baojiaoState;
	}

}
