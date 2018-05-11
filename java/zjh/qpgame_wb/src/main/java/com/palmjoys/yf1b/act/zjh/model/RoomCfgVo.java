package com.palmjoys.yf1b.act.zjh.model;

import java.util.ArrayList;
import java.util.List;

public class RoomCfgVo {
	public List<RoomCfgItem> items = new ArrayList<>();
	
	public void addItem(int cfgId, int baseScore, int onceMax, int joinLimit){
		RoomCfgItem item = new RoomCfgItem();
		item.cfgId = cfgId;
		item.baseScore = baseScore;
		item.onceMax = onceMax;
		item.joinLimit = joinLimit;
		
		items.add(item);
	}
	
	public class RoomCfgItem{
		//配置Id
		public int cfgId; 
		//底分
		public int baseScore;
		//单注最高封顶
		public int onceMax;
		//进入限制
		public int joinLimit;
	}

}
