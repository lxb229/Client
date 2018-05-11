package com.palmjoys.yf1b.act.order.model;

import java.util.ArrayList;
import java.util.List;

public class OrderGmVo {
	//记录总数
	public int totalNum;
	//订单数据列表
	public List<OrderGmItem> items;
	
	public OrderGmVo(){
		this.totalNum = 0;
		this.items = new ArrayList<>();
	}
	
	public void addItem(long orderId, String starNO, long createTime, int rmb, long goldMoney,
			int state, long transTime, String reMarks, int transPlayer, int payType, String payAccount){
		OrderGmItem item = new OrderGmItem();
		item.orderId = String.valueOf(orderId);
		item.starNO = starNO;
		item.createTime = String.valueOf(createTime);
		item.rmb = rmb;
		item.goldMoney = String.valueOf(goldMoney);
		item.state = state;
		item.transTime = String.valueOf(transTime);
		item.reMarks = reMarks;
		item.transPlayer = transPlayer;
		item.payType = payType;
		item.payAccount = payAccount;
		
		items.add(item);
	}
		
	public class OrderGmItem{
		//订单Id
		public String orderId;
		//订单提交人
		public String starNO;
		//订单提交时间
		public String createTime;
		//订单RMB
		public int rmb;
		//订单游戏币
		public String goldMoney;
		//订单状态(0=等待处理,1=正在处理,2=失败,3=成功)
		public int state;
		//订单处理时间
		public String transTime;
		//处理备注
		public String reMarks;
		//订单处理人
		public int transPlayer;
		//订单支付类型(1=WX,2=支付宝)
		public int payType;
		//订单支付帐号
		public String payAccount;
		
	}
}
