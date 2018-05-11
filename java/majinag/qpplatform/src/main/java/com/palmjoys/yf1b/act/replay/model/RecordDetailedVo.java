package com.palmjoys.yf1b.act.replay.model;

import java.util.ArrayList;
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
		vo.scores = scores;
		vo.recordFile = recordFile;
		this.items.add(vo);
	}
	
	public class RecordDetailedItemVo{
		//局数
		public int gameNum;
		//记录时间
		public String recordTime;
		//分数列表
		public List<Integer> scores;
		//录像文件
		public String recordFile;
	}
}
