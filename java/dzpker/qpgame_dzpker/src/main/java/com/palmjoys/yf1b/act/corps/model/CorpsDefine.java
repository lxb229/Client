package com.palmjoys.yf1b.act.corps.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "帮会模块")
public interface CorpsDefine {
	// module
	@SocketModule("帮会模块")
	int CORPS = 9;
	
	static final int CORPS_COMMAND_BASE = CORPS*100;
	static final int CORPS_ERROR_BASE = (0-CORPS)*1000;
	static final int CORPS_COMMAND_BASE_NOTIFY = CORPS*10000;
}
