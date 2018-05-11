package com.palmjoys.yf1b.act.dzpker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 桌子上局战斗数据
 * */
public class TablePrevFightRecordVo {
	//桌子底牌
	public List<Integer> tableCards;
	//参与游戏的所有玩家数据
	public List<TablePrevFightRecordItem> items;
	
	public TablePrevFightRecordVo(){
		this.tableCards = new ArrayList<>();
		this.items = new ArrayList<>();
	}
	
	public void addItem(List<Integer> handCards, String starNO, String headImg, String nick, long score, int showCardState){
		TablePrevFightRecordItem item = new TablePrevFightRecordItem();
		if(handCards != null){
			item.handCards.addAll(handCards);
		}
		item.starNO = starNO;
		item.headImg = headImg;
		item.nick = nick;
		item.score = String.valueOf(score);
		item.showCardState = showCardState;
		
		this.items.add(item);
	}
	
	public class TablePrevFightRecordItem{
		//手牌
		public List<Integer> handCards = new ArrayList<>();
		//玩家Id
		public String starNO;
		//头像
		public String headImg;
		//呢称
		public String nick;
		//输赢分数
		public String score;
		//亮牌状态(0=不显示)
		public int showCardState;
	}
}
