package com.palmjoys.yf1b.act.majiang.model.notify.table;

import java.util.ArrayList;
import java.util.List;

public class Notify_Table_GameStateVo_Destory{
	//解散桌子的申请人
	public String destoryQuestPlayer;
	public List<DestoryInner> seats = new ArrayList<>();
	
	public void addSeat(int seatIndex, int btState){
		DestoryInner item = new DestoryInner();
		item.seatIndex = seatIndex;
		item.btState = btState;
		this.seats.add(item);
	}
	public class DestoryInner{
		//座位
		public int seatIndex;
		//表态状态
		public int btState;
	}

}
