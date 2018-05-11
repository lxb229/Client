package com.palmjoys.yf1b.act.dzpker.model;

import java.util.ArrayList;
import java.util.List;

//桌子所有玩家信息数据
public class TablePlayerListVo {
	//保险分数
	public String insuranceScore;
	//玩家列表
	public List<TablePlayerListItem> items;
	
	public TablePlayerListVo(){
		this.insuranceScore = "0";
		this.items = new ArrayList<>();
	}
	
	public void addItem(String starNO, String headImg, String nick, long gameNum, long currMoney, long winMoney){
		TablePlayerListItem item = new TablePlayerListItem();
		item.starNO = starNO;
		item.headImg = headImg;
		item.nick = nick;
		item.gameNum = String.valueOf(gameNum);
		item.currMoney = String.valueOf(currMoney);
		item.winMoney = String.valueOf(winMoney);
		
		this.items.add(item);
	}
	
	public class TablePlayerListItem {
		//玩家Id
		public String starNO;
		//头像
		public String headImg;
		//呢称
		public String nick;
		//总游戏局数
		public String gameNum;
		//当前筹码数
		public String currMoney;
		//总输赢筹码
		public String winMoney;
	}
}
