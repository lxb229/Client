package com.palmjoys.yf1b.act.lobby.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "大厅模块")
public interface LobbyDefine {
	// module
	@SocketModule("大厅模块")
	int LOBBY = 11;
	
	static final int LOBBY_COMMAND_BASE = LOBBY*100;
	static final int LOBBY_ERROR_BASE = (0-LOBBY)*1000;
	static final int LOBBY_COMMAND_BASE_NOTIFY = LOBBY*10000;
}
