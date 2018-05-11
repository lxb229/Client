package com.palmjoys.yf1b.act.replay.model;

import java.util.ArrayList;
import java.util.List;

public class RecordItemVo {
	//记录Id
	public String recordId;
	//桌子Id
	public int tableId;
	//记录时间
	public String recordTime;
	//座位数据
	public List<RecordSeatVo> seats;
	
	public RecordItemVo(){
		this.seats = new ArrayList<>();
	}
	
	public void addSeatVo(long accountId, String nick, int score){
		RecordSeatVo seat = new RecordSeatVo();
		seat.accountId = String.valueOf(accountId);
		seat.nick = nick;
		seat.score = score;
		this.seats.add(seat);
	}

	public class RecordSeatVo{
		//帐号Id
		public String accountId;
		//呢称
		public String nick;
		//分数
		public int score;
	}
}
