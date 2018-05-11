package com.palmjoys.yf1b.act.activity.model;

import java.util.ArrayList;
import java.util.List;

public class ActivityVo {
	//活动列表项
	public List<ActivityItemAttrib> items;
	
	public ActivityVo(){
		this.items = new ArrayList<>();
	}
	
	public void addItem(String currUrl, String openUrl){
		ActivityItemAttrib item = new ActivityItemAttrib();
		item.currUrl = currUrl;
		item.openUrl = openUrl;
		
		this.items.add(item);
	}
}
