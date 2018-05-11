package com.palmjoys.yf1b.act.zjh.facade;

import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;

import com.palmjoys.yf1b.act.zjh.model.ZJHMessageDefine;

@NetworkFacade
public interface ZjhFacade {
	
	@NetworkApi(value = ZJHMessageDefine.ZJHGAME_COMMAND_GET_ROOM_LIST,
			desc="获取游戏场列表")
	Object zjh_get_room_list(@InSession Long accountId,
			@InBody(value = "gameType", desc = "游戏类型,1=牛牛,2=金花") int gameType);
	
	@NetworkApi(value = ZJHMessageDefine.ZJHGAME_COMMAND_GET_TABLE_LIST,
			desc="获取桌子列表")
	Object zjh_get_table_list(@InSession Long accountId,
			@InBody(value = "cfgId", desc = "游戏场Id") int cfgId);
	
	@NetworkApi(value = ZJHMessageDefine.ZJHGAME_COMMAND_QUICK_JOIN_TABLE,
			desc="快速加入游戏桌子")
	Object zjh_quick_join(@InSession Long accountId,
			@InBody(value = "cfgId", desc = "游戏场Id") int cfgId);

	@NetworkApi(value = ZJHMessageDefine.ZJHGAME_COMMAND_JOIN_TABLE,
			desc="输入桌子号加入桌子")
	Object zjh_jion_tableId(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId,
			@InBody(value = "type", desc = "1=私人桌子") int type);
	
	@NetworkApi(value = ZJHMessageDefine.ZJHGAME_COMMAND_CREATE_TABLE,
			desc="创建桌子")
	Object zjh_table_create(@InSession Long accountId,
			@InBody(value = "cfgId", desc = "游戏场Id") int cfgId);
	
	@NetworkApi(value = ZJHMessageDefine.ZJHGAME_COMMAND_LEAVE_TABLE,
			desc="强制离开桌子")
	Object zjh_table_leave(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId);
	
	@NetworkApi(value = ZJHMessageDefine.ZJHGAME_COMMAND_BET,
			desc="座位表态")
	Object zjh_table_bet(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId,
			@InBody(value = "seatIndex", desc = "座位下标(0-N)") int seatIndex,
			@InBody(value = "bt", desc = "表态状态(0=放弃,1=看牌,2=比牌,3=全押,4=跟注,5=加注)") int bt,
			@InBody(value = "btVal", desc = "表态结果值") int btVal);
	
	@NetworkApi(value = ZJHMessageDefine.ZJHGAME_COMMAND_NN_CALL_BANKER,
			desc="牛牛游戏叫庄家")
	Object zjh_table_call_banker(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId,
			@InBody(value = "bt", desc = "表态结果(0=不叫,1=叫庄家)") int bt);
	
	@NetworkApi(value = ZJHMessageDefine.ZJHGAME_COMMAND_GAME_READY,
			desc="游戏点准备开始游戏")
	Object zjh_table_nn_game_ready(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId);
	
	@NetworkApi(value = ZJHMessageDefine.ZJHGAME_COMMAND_STATE_CHANG_NOTIFY,
			desc="推送消息(游戏状态变化)")
	Object zjh_gamestate_chanage_notify();
	
	@NetworkApi(value = ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY,
			desc="推送消息(座位数据变化)")
	Object zjh_seat_chanage_notify();
	
	@NetworkApi(value = ZJHMessageDefine.ZJHGAME_COMMAND_BET_NOTIFY,
			desc="推送消息(下注数据变化)")
	Object zjh_table_bet_notify();
	
	@NetworkApi(value = ZJHMessageDefine.ZJHGAME_COMMAND_KICK_NOTIFY,
			desc="推送消息(玩家被踢出座位)")
	Object zjh_table_kick_player_notify();
	
	@NetworkApi(value = ZJHMessageDefine.ZJHGAME_COMMAND_LOOKCARD_NOTIFY,
			desc="推送消息(玩家看牌数据)")
	Object zjh_table_lookcard_notify();
	
	@NetworkApi(value = ZJHMessageDefine.ZJHGAME_COMMAND_CALLBANKER_NOTIFY,
			desc="推送消息(牛牛叫庄数据)")
	Object zjh_table_nn_callbanker_notify();
	
	@NetworkApi(value = ZJHMessageDefine.ZJHGAME_COMMAND_GAME_READY_NOTIFY,
			desc="推送消息(牛牛游戏准备数据)")
	Object zjh_table_nn_gameready_notify();
}
