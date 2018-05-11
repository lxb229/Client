package com.palmjoys.yf1b.act.replay.model;

import java.util.ArrayList;
import java.util.List;

public class RecordItemVo {
	//桌子Id
	public int tableId;
	//记录时间
	public String recordTime;
	//座位数据
	public List<RecordSeatVo> seats;
	
	public RecordItemVo(){
		this.seats = new ArrayList<>();
	}
	
	public void addSeatVo(String nick, int score){
		RecordSeatVo seat = new RecordSeatVo();
		seat.nick = nick;
		seat.score = score;
		this.seats.add(seat);
	}

	public class RecordSeatVo{
		//呢称
		public String nick;
		//总分数
		public int score;
	}
}
