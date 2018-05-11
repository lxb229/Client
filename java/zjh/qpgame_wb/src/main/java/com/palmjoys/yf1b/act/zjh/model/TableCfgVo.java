package com.palmjoys.yf1b.act.zjh.model;

import java.util.ArrayList;
import java.util.List;

public class TableCfgVo {
	public List<TableCfgItem> items = new ArrayList<>();
	
	public void addItem(int tableId, int playerNum){
		TableCfgItem vo = new TableCfgItem();
		vo.tableId = tableId;
		vo.playerNum = playerNum;
		items.add(vo);
	}
	public class TableCfgItem{
		//桌子号
		public int tableId;
		//桌子人数
		public int playerNum;
	}
}
