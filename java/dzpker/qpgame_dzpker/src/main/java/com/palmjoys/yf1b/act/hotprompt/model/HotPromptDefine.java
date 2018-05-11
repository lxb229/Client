package com.palmjoys.yf1b.act.hotprompt.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "热点模块")
public interface HotPromptDefine {
	// module
	@SocketModule("热点模块")
	int HOTPROMPT = 7;
	
	static final int HOTPROMPT_COMMAND_BASE = HOTPROMPT*100;
	static final int HOTPROMPT_ERROR_BASE = (0-HOTPROMPT)*1000;
	static final int HOTPROMPT_COMMAND_BASE_NOTIFY = HOTPROMPT*10000;
	
	
	//command id
	//获取红点数据
	int HOTPROMPT_COMMAND_GET_HOTDATA = HOTPROMPT_COMMAND_BASE + 1;
	//推送消息(玩家红点数据)
	int HOTPROMPT_COMMAND_HOTDATA_NOTIFIY = HOTPROMPT_COMMAND_BASE_NOTIFY + 1;
}
