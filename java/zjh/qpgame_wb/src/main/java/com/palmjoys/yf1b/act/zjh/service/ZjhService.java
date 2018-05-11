package com.palmjoys.yf1b.act.zjh.service;

public interface ZjhService {
	//获取游戏场列表
	public Object zjh_get_room_list(Long accountId, int gameType);
	//获取游戏桌子列表
	public Object zjh_get_table_list(Long accountId, int cfgId);
	//快速加入游戏桌子
	public Object zjh_quick_join(Long accountId, int cfgId);
	//输入桌子号加入桌子
	public Object zjh_jion_tableId(Long accountId, int tableId, int type);
	//创建桌子
	public Object zjh_table_create(Long accountId, int cfgId);
	//强制离开桌子
	public Object zjh_table_leave(Long accountId, int tableId);
	//座位下注
	public Object zjh_table_bet(Long accountId, int tableId, int seatIndex, int bt, int btVal);
	//牛牛叫庄家
	public Object zjh_table_call_banker(Long accountId, int tableId, int bt);
	//牛牛游戏点准备开始游戏
	public Object zjh_table_nn_game_ready(Long accountId, int tableId);
}
