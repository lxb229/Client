package com.palmjoys.yf1b.act.replay.model;

import java.util.HashMap;
import java.util.Map;

public class RecordScoreAttrib {
	//当前局数
	public int gameNum;
	//记录时间
	public String recordTime;
	//录像数据文件位置
	public String recordFile;
	//座位分数
	public Map<Long, Integer> scoreList = new HashMap<>();
}
