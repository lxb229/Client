package com.palmjoys.yf1b.act.replay.service;

public interface ReplayService {
	//查询战绩
	public Object replay_query_record(Long accountId, int type, String query);
	//查询桌子详细战绩数据
	public Object replay_query_detailed_record(Long accountId, int tableId);
}
