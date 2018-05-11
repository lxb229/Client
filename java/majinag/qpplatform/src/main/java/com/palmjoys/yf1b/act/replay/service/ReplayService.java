package com.palmjoys.yf1b.act.replay.service;

public interface ReplayService {
	//查询战绩
	public Object replay_query_record(Long accountId, int type, String query);
	//查询桌子详细战绩数据
	public Object replay_query_detailed_record(Long accountId, long recordId);
	//删除战绩记录
	public Object replay_delete_record(Long accountId, long recordId);
	//实名认证
	public Object replay_realname_authentication(Long accountId, String name, String cardId);
	//获取手机短信验证码
	public Object replay_phone_get_smsCode(Long accountId, String phone);
	//手机帮定
	public Object replay_phone_bind(Long accountId, String phone, String vaildCode);
}
