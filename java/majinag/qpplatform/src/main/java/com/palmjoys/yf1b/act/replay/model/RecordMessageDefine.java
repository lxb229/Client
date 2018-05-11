package com.palmjoys.yf1b.act.replay.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "录像回放模块")
public interface RecordMessageDefine {
	@SocketModule("录像回放模块")
	int REPLAY = 7;
	
	static final int REPLAY_COMMAND_BASE = REPLAY*100;
	static final int REPLAY_ERROR_BASE = (0-REPLAY)*1000;
	static final int REPLAY_COMMAND_BASE_NOTIFY = REPLAY*10000;
	
	//command id
	//查询战绩
	int REPLAY_COMMAND_QUERY_RECORD_LIST = REPLAY_COMMAND_BASE + 1;
	//查询桌子详细战绩数据
	int REPLAY_COMMAND_QUERY_RECORD_DETAILED = REPLAY_COMMAND_BASE + 2;
	//删除战绩记录
	int REPLAY_COMMAND_DELETE_RECORD = REPLAY_COMMAND_BASE + 3;
	//实名认证
	int REPLAY_COMMAND_REALNAME_AUTHENTICATION = REPLAY_COMMAND_BASE + 4;
	//获取手机短信验证码
	int REPLAY_COMMAND_PHONE_VAILDCODE = REPLAY_COMMAND_BASE + 5;
	//手机帮定
	int REPLAY_COMMAND_PHONE_BIND = REPLAY_COMMAND_BASE + 6;
}
