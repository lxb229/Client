package com.palmjoys.yf1b.act.dzpker.model;

import java.util.ArrayList;
import java.util.List;

public class JoinedTableListVo {
	//参与过游戏未解散的桌子信息列表
	public List<JoinedTableItem> items;

	public JoinedTableListVo(){
		this.items = new ArrayList<>();
	}
	
	public void addItem(int tableId, long createPlayer, String headImg, String nick, int small,
			int big, int minJoin, long vaildTime, int currPlayer, int seatNum){
		JoinedTableItem item = new JoinedTableItem();
		item.tableId = tableId;
		item.createPlayer = String.valueOf(createPlayer);
		item.headImg = headImg;
		item.nick = nick;
		item.small = small;
		item.big = big;
		item.minJoin = minJoin;
		item.vaildTime = String.valueOf(vaildTime);
		item.currPlayer = currPlayer;
		item.seatNum = seatNum;
		
		items.add(item);
	}
		
	public class JoinedTableItem{
		//桌子Id
		public int tableId;
		//桌子创建玩家
		public String createPlayer;
		//头像
		public String headImg;
		//呢称
		public String nick;
		//小盲注
		public int small;
		//大盲注
		public int big;
		//最小座下筹码
		public int minJoin;
		//桌子解散剩余时间(毫秒数)
		public String vaildTime;
		//当前座位人数
		public int currPlayer;
		//总座位数
		public int seatNum;
	}
}
