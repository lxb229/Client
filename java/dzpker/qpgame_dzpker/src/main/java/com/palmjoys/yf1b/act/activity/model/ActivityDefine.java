package com.palmjoys.yf1b.act.activity.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "活动模块")
public interface ActivityDefine {
	// module
	@SocketModule("活动模块")
	int ACTIVITY = 6;
	
	static final int ACTIVITY_COMMAND_BASE = ACTIVITY*100;
	static final int ACTIVITY_ERROR_BASE = (0-ACTIVITY)*1000;
	static final int ACTIVITY_COMMAND_BASE_NOTIFY = ACTIVITY*10000;
	
	//command id
	//获取活动列表
	int ACTIVITY_COMMAND_GET_ACTIVITY_LIST = ACTIVITY_COMMAND_BASE + 1;
}
