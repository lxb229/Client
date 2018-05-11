package com.palmjoys.yf1b.act.majiang.model.notify.seat;

import java.util.ArrayList;
import java.util.List;

import com.palmjoys.yf1b.act.majiang.model.SeatVo;

public class Notify_Seat_Vo_BreakCard {
	//胡杠碰吃打断处理结果状态(0=胡,1=杠,2=碰,3=吃)
	public int breakState;
	//胡杠碰吃响应座位下标
	public List<Integer> breakSeats;
	//座位数据
	public List<SeatVo> seats = new ArrayList<>();
}
