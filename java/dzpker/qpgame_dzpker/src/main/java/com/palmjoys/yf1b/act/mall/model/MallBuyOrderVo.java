package com.palmjoys.yf1b.act.mall.model;

import java.util.ArrayList;
import java.util.List;

public class MallBuyOrderVo {
	//列表总数
	public int totalNum;
	//列表项
	public List<MallOrderAttrib> items;

	public MallBuyOrderVo(){
		this.totalNum = 0;
		this.items = new ArrayList<>();
	}
	
	public void addItem(long orderId, String starNO, int rmb, 
			long roomCard, long goldMoney, long diamond, long createTime, int state){
		MallOrderAttrib item = new MallOrderAttrib();
		item.orderId = String.valueOf(orderId);
		item.starNO = starNO;
		item.rmb = rmb;
		item.roomCard = String.valueOf(roomCard);
		item.goldMoney = String.valueOf(goldMoney);
		item.diamond = String.valueOf(diamond);
		item.createTime = String.valueOf(createTime);
		item.state = state;
		
		items.add(item);
	}
	
}
