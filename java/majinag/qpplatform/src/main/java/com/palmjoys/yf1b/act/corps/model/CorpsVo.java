package com.palmjoys.yf1b.act.corps.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CorpsVo {
	public List<CorpsVoItem> items;
	
	public CorpsVo(){
		this.items = new ArrayList<>();
	}
	
	public void addItem(String corpsId, String corpsName, long createPlayer, String wxNO, 
			int memberNum, long activeValue, int joinFlag, int roomCardState, int hidde){
		CorpsVoItem item = new CorpsVoItem();
		item.corpsId = corpsId;
		item.corpsName = corpsName;
		item.createPlayer = String.valueOf(createPlayer);
		item.wxNO = wxNO;
		item.memberNum = memberNum;
		item.activeValue = activeValue;
		item.joinFlag = joinFlag;
		item.roomCardState = roomCardState;
		item.hidde = hidde;
		this.items.add(item);
	}
	
	//按自已创建的和创建时间排序
	public void sort(long createPlayer){
		List<CorpsVoItem> list1 = new ArrayList<>();
		//先找出自已创建的帮会
		boolean bfind = true;
		while(bfind == true){
			bfind = false;
			for(CorpsVoItem corpsVoItem : items){
				long nAccountId = Long.parseLong(corpsVoItem.createPlayer);
				if(nAccountId == createPlayer){
					list1.add(corpsVoItem);
					items.remove(corpsVoItem);
					bfind = true;
					break;
				}
			}
		}
		//按活跃度排
		items.sort(new Comparator<CorpsVoItem>(){
			@Override
			public int compare(CorpsVoItem arg0, CorpsVoItem arg1) {
				if(arg0.activeValue > arg1.activeValue){
					return -1;
				}else if(arg0.activeValue < arg1.activeValue){
					return 1;
				}
				return 0;
			}
		});
		
		list1.addAll(items);
		items = null;
		items = list1;
	}	
	
	public class CorpsVoItem{
		//帮会Id
		public String corpsId;
		//帮会名称
		public String corpsName;
		//馆主Id
		public String createPlayer;
		//帮会微信
		public String wxNO;
		//帮会成员人数
		public int memberNum;
		//加入标志(0=未加入,1=已加入)
		public int joinFlag;
		//房卡可使用状态(0=不可使用,1=可使用)
		public int roomCardState;
		//帮会可见状态(0=可见,1=不可见)
		public int hidde;
		//帮会活跃度(不用传给前方)
		public long activeValue;
	}
}
