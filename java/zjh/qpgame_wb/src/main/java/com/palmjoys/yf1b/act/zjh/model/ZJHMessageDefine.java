package com.palmjoys.yf1b.act.zjh.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketCode;
import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "金花牛牛游戏模块")
public interface ZJHMessageDefine {
	@SocketModule("金花牛牛模块")
	int ZJHGAME = 5;
	
	static final int ZJHGAME_COMMAND_BASE = ZJHGAME*100;
	static final int ZJHGAME_ERROR_BASE = (0-ZJHGAME)*1000;
	static final int ZJHGAME_COMMAND_BASE_NOTIFY = ZJHGAME*10000;

	//command id
	//获取游戏场次列表
	int ZJHGAME_COMMAND_GET_ROOM_LIST = ZJHGAME_COMMAND_BASE + 1;
	//获取桌子列表
	int ZJHGAME_COMMAND_GET_TABLE_LIST = ZJHGAME_COMMAND_BASE + 2;
	//快速进入桌子
	int ZJHGAME_COMMAND_QUICK_JOIN_TABLE = ZJHGAME_COMMAND_BASE + 3;
	//加入桌子
	int ZJHGAME_COMMAND_JOIN_TABLE = ZJHGAME_COMMAND_BASE + 4;
	//创建桌子
	int ZJHGAME_COMMAND_CREATE_TABLE = ZJHGAME_COMMAND_BASE + 5;
	//离开桌子
	int ZJHGAME_COMMAND_LEAVE_TABLE = ZJHGAME_COMMAND_BASE + 6;
	//下注
	int ZJHGAME_COMMAND_BET = ZJHGAME_COMMAND_BASE + 7;
	//牛牛叫庄
	int ZJHGAME_COMMAND_NN_CALL_BANKER = ZJHGAME_COMMAND_BASE + 8;
	//游戏点击准备
	int ZJHGAME_COMMAND_GAME_READY = ZJHGAME_COMMAND_BASE + 9;
	
	//推送消息(游戏状态变化)
	int ZJHGAME_COMMAND_STATE_CHANG_NOTIFY = ZJHGAME_COMMAND_BASE_NOTIFY + 1;
	//推送消息(座位数据变化)
	int ZJHGAME_COMMAND_SEAT_NOTIFY = ZJHGAME_COMMAND_BASE_NOTIFY + 2;
	//推送消息(下注数据变化)
	int ZJHGAME_COMMAND_BET_NOTIFY = ZJHGAME_COMMAND_BASE_NOTIFY + 3;
	//推送消息(玩家被踢出座位)
	int ZJHGAME_COMMAND_KICK_NOTIFY = ZJHGAME_COMMAND_BASE_NOTIFY + 4;
	//推送消息(玩家看牌)
	int ZJHGAME_COMMAND_LOOKCARD_NOTIFY = ZJHGAME_COMMAND_BASE_NOTIFY + 5;
	//推送消息(牛牛叫庄数据)
	int ZJHGAME_COMMAND_CALLBANKER_NOTIFY = ZJHGAME_COMMAND_BASE_NOTIFY + 6;
	//推送消息(座位准备数据)
	int ZJHGAME_COMMAND_GAME_READY_NOTIFY = ZJHGAME_COMMAND_BASE_NOTIFY + 7;
	
	
	//error id
	@SocketCode("桌子不存在")
	int ZJHGAME_ERROR_TABLE_UNEXIST = ZJHGAME_ERROR_BASE - 1;
	@SocketCode("没有空桌子了")
	int ZJHGAME_ERROR_TABLE_UNEMPTY = ZJHGAME_ERROR_BASE - 2;
	@SocketCode("进入房间金币不足")
	int ZJHGAME_ERROR_JOIN_LIMIT = ZJHGAME_ERROR_BASE - 3;
	@SocketCode("接口参数错误")
	int ZJHGAME_ERROR_COMMAND_PARAM = ZJHGAME_ERROR_BASE - 4;
	@SocketCode("桌子人数已满")
	int ZJHGAME_ERROR_SEAT_UNEMPTY = ZJHGAME_ERROR_BASE - 5;
	@SocketCode("未轮到你表态")
	int ZJHGAME_ERROR_UNBT_STATE = ZJHGAME_ERROR_BASE - 6;
	@SocketCode("错误的下注金额")
	int ZJHGAME_ERROR_BET_MONEY = ZJHGAME_ERROR_BASE - 7;
	@SocketCode("上家全下只能比牌")
	int ZJHGAME_ERROR_ONLY_COMPARE = ZJHGAME_ERROR_BASE - 8;
	@SocketCode("必须大于2轮以上才能比牌")
	int ZJHGAME_ERROR_ONLY_COMPARE_ROUND = ZJHGAME_ERROR_BASE - 9;
	@SocketCode("非私人房间不能加入")
	int ZJHGAME_ERROR_ONLY_PRIVATE_TABLE = ZJHGAME_ERROR_BASE - 10;
	@SocketCode("只有两个玩家参与当局才能全押")
	int ZJHGAME_ERROR_BETALL_ONLY_DOUBLEPLAYER = ZJHGAME_ERROR_BASE - 11;
	@SocketCode("金币不足下注额,只能比牌")
	int ZJHGAME_ERROR_BET_ONLY_COMPARE = ZJHGAME_ERROR_BASE - 12;
	@SocketCode("必须大于1轮以上才能全押")
	int ZJHGAME_ERROR_ONLY_ONCEROUND_BETALL = ZJHGAME_ERROR_BASE - 13;
	
}
