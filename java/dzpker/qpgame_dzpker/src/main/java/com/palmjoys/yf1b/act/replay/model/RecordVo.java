package com.palmjoys.yf1b.act.replay.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecordVo {
	//列表项数
	public int itemNum;
	//项列表
	public List<RecordItemVo> items;
		
	public RecordVo(){
		this.items = new ArrayList<>();
	}
	
	public void sort(){
		this.items.sort(new Comparator<RecordItemVo>(){
			//按时间从大到小排
			@Override
			public int compare(RecordItemVo o1, RecordItemVo o2) {
				long time1 = Long.parseLong(o1.recordTime);
				long time2 = Long.parseLong(o2.recordTime);
				if(time1 > time2){
					return -1;
				}else if(time1 < time2){
					return 1;
				}
				return 0;
			}
		});
	}
}
