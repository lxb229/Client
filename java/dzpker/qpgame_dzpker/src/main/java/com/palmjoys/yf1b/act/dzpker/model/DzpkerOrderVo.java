package com.palmjoys.yf1b.act.dzpker.model;

import java.util.ArrayList;
import java.util.List;

public class DzpkerOrderVo {
	//列表
	public List<DzpkerOrderItem> items = new ArrayList<>();
	
	public void addItem(long itemId, String starNO, String nick, String headImg, long chipNum){
		DzpkerOrderItem item = new DzpkerOrderItem();
		item.itemId = String.valueOf(itemId);
		item.starNO = starNO;
		item.nick = nick;
		item.headImg = headImg;
		item.chipNum = String.valueOf(chipNum);
		items.add(item);
	}
	
	public class DzpkerOrderItem{
		//记录号
		public String itemId;
		//购买玩家明星号
		public String starNO;
		//购买玩家呢称
		public String nick;
		//购买玩家头像
		public String headImg;
		//购买筹码数
		public String chipNum;
	}
	
}
