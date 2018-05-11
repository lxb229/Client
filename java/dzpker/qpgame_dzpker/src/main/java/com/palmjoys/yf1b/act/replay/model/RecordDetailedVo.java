package com.palmjoys.yf1b.act.replay.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecordDetailedVo {
	//桌子号
	public int tableId;
	//座位玩家呢称
	public List<String> nicks;
	//列表数据
	public List<RecordDetailedItemVo> items;
	
	public RecordDetailedVo(){
		this.nicks = new ArrayList<>();
		this.items = new ArrayList<>();
	}
	
	public void addItem(int gameNum, String recordTime, 
			List<Integer> scores, String recordFile){
		RecordDetailedItemVo vo = new RecordDetailedItemVo();
		vo.gameNum = gameNum;
		vo.recordTime = recordTime;
		vo.scores.clear();
		vo.scores.addAll(scores);
		vo.recordFile = recordFile;
		this.items.add(vo);
	}
	
	public class RecordDetailedItemVo{
		//局数
		public int gameNum;
		//记录时间
		public String recordTime;
		//分数列表
		public List<Integer> scores = new ArrayList<>();
		//录像文件
		public String recordFile;
	}
	
	public void sort(){
		this.items.sort(new Comparator<RecordDetailedItemVo>(){
			//按局数从小到大排
			@Override
			public int compare(RecordDetailedItemVo o1, RecordDetailedItemVo o2) {
				if(o1.gameNum > o2.gameNum){
					return 1;
				}else if(o1.gameNum < o2.gameNum){
					return -1;
				}
				return 0;
			}
		});
	}
}
