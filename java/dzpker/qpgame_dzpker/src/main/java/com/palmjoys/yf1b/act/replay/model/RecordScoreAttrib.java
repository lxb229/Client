package com.palmjoys.yf1b.act.replay.model;

import java.util.HashMap;
import java.util.Map;

public class RecordScoreAttrib {
	//当前局数
	public int gameNum;
	//记录时间
	public String recordTime;
	//录像数据文件名称
	public String recordFile;
	//分数
	public Map<Long, Integer> scores = new HashMap<>();
}
