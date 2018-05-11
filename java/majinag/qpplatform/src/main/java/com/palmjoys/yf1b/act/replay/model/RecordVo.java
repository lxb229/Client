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
			@Override
			public int compare(RecordItemVo arg0, RecordItemVo arg1) {
				long time1 = Long.parseLong(arg0.recordTime);
				long time2 = Long.parseLong(arg1.recordTime);
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
