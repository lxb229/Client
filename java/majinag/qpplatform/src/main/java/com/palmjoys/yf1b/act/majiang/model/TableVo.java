package com.palmjoys.yf1b.act.majiang.model;

import java.util.ArrayList;
import java.util.List;

public class TableVo {
	//桌子基础数据,所有人都可见
	public TableBaseVo tableBaseVo;
	//桌子座位数据
	public List<SeatVo> seats;
	//单局结算数据
	public List<SettlementOnceVo> settlementOnce;
	//总结算数据
	public List<SettlementAllVo> settlementAll;
	//胡杠碰吃打断处理结果状态(0=胡,1=杠,2=碰,3=吃)
	public int breakState;
	//胡杠碰吃响应座位下标
	public List<Integer> breakSeats;
	
		
	public TableVo(){
		this.seats = new ArrayList<SeatVo>();
		this.settlementOnce = new ArrayList<>();
		this.settlementAll = new ArrayList<>();
		this.breakSeats = new ArrayList<>();
	}
}
