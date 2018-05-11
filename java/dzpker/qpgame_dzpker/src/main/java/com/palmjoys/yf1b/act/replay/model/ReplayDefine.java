package com.palmjoys.yf1b.act.replay.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketCode;
import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "录像回放模块")
public interface ReplayDefine {
	@SocketModule("录像回放模块")
	int REPLAY = 10;
	
	static final int REPLAY_COMMAND_BASE = REPLAY*100;
	static final int REPLAY_ERROR_BASE = (0-REPLAY)*1000;
	static final int REPLAY_COMMAND_BASE_NOTIFY = REPLAY*10000;
	
	//command id
	//查询战绩
	int REPLAY_COMMAND_QUERY_RECORD_LIST = REPLAY_COMMAND_BASE + 1;
	//查询桌子详细战绩数据
	int REPLAY_COMMAND_QUERY_RECORD_DETAILED = REPLAY_COMMAND_BASE + 2;
	
	
	//error id
	@SocketCode("玩家不存在")
	int REPLAY_ERROR_PLAYER_UNEXIST = REPLAY_ERROR_BASE - 1;
	@SocketCode("接口参数错误")
	int REPLAY_ERROR_COMMAND_PARAM = REPLAY_ERROR_BASE - 2;
	@SocketCode("验码证获取频繁,稍后重试")
	int REPLAY_ERROR_SMS_NUM = REPLAY_ERROR_BASE - 3;
	@SocketCode("验证码获取失败,稍后重试")
	int REPLAY_ERROR_SMS_GET = REPLAY_ERROR_BASE - 4;
	@SocketCode("验证码错误")
	int REPLAY_ERROR_SMS_CODE = REPLAY_ERROR_BASE - 5;
	@SocketCode("此手机已和其它帐号绑定")
	int REPLAY_ERROR_SMS_PHONE = REPLAY_ERROR_BASE - 6;
}
