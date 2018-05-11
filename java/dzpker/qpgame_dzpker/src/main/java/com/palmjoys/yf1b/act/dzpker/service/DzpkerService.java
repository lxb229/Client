package com.palmjoys.yf1b.act.dzpker.service;

public interface DzpkerService {
	//获取桌子创建配置
	public Object dzpker_table_get_cfg(Long accountId);
	//创建桌子
	public Object dzpker_table_create(Long accountId, String tableName, int small, int big, int minJoin, int vaildTime,
			int insurance, int straddle, int buyMax);
	//房主点击开始运行桌子
	public Object dzpker_table_start_run(Long accountId, int tableId);
	//加入桌子
	public Object dzpker_table_join(Long accountId, int tableId);
	//离开桌子
	public Object dzpker_table_leave(Long accountId, int tableId);
	//座位座下
	public Object dzpker_seat_down(Long accountId, int tableId, int seatIndex);
	//座位站起
	public Object dzpker_seat_up(Long accountId, int tableId, int seatIndex);
	//申请购买筹码
	public Object dzpker_buy_chip(Long accountId, int tableId, int chipNum);
	//游戏表态
	public Object dzpker_table_bt(Long accountId, int tableId, int bt, int btVal);
	//房主获取购买筹码申请列表
	public Object dzpker_query_buychip_list(Long accountId);
	//房主处理购买筹码记录
	public Object dzpker_trans_buychip_item(Long accountId, String itemId, int bt);
	//闭眼盲注表态
	public Object dzpker_straddle_bt(Long accountId, int tableId, int seatIndex);
	//获取参与过游戏的未解散的桌子
	public Object dzpker_get_fighted_table_list(Long accountId);
	//获取桌子上一局游戏信息
	public Object dzpker_table_prev_fight(Long accountId, int tableId);
	//获取桌子所有玩家数据信息
	public Object dzpker_table_player_list(Long accountId, int tableId);
	//获取桌子个人赢亏数据
	public Object dzpker_table_player_once_winscore(Long accountId, int tableId);
	//购买保险
	public Object dzpker_table_buy_insurance(Long accountId, int tableId, int bt, int[] buyCards, int buyMoney,
			int payMoney);
	//获取生涯历史数据
	public Object dzpker_table_get_caree_info(Long accountId);
	//玩家亮牌表态
	public Object dzpker_table_show_card_bt(Long accountId, int tableId);
	//获取指定桌子数据
	public Object dzpker_table_get_table_info(Long accountId, int tableId);
	//获取指定桌子所有参与过游戏的玩家输赢明细
	public Object dzpker_table_get_all_win_score_info(Long accountId, long recordId);

}
