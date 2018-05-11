package com.palmjoys.yf1b.act.mall.model;

public class MallOrderAttrib {
	//订单Id
	public String orderId;
	//订单申请玩家明星号
	public String starNO;
	//订单RMB
	public int rmb;
	//订单获得的房卡
	public String roomCard;
	//订单获得的金币
	public String goldMoney;
	//订单获得的钻石
	public String diamond;
	//订单创建时间
	public String createTime;
	//订单处理状态(0=等待处理,1=正在处理,2=处理成功,3=处理失败)
	public int state;
}
