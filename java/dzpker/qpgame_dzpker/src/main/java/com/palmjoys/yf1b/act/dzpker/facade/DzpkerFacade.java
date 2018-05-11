package com.palmjoys.yf1b.act.dzpker.facade;

import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;

import com.palmjoys.yf1b.act.dzpker.model.DzpkerDefine;

@NetworkFacade
public interface DzpkerFacade {
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_TABLE_GET_CFG, 
			desc="获取桌子创建配置")
	Object dzpker_table_get_cfg(@InSession Long accountId);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_TABLE_CREATE, 
			desc="创建桌子")
	Object dzpker_table_create(@InSession Long accountId,
			@InBody(value = "tableName", desc = "桌子名称") String tableName,
			@InBody(value = "small", desc = "小盲注") int small,
			@InBody(value = "big", desc = "大盲注") int big,
			@InBody(value = "minJoin", desc = "最小座下") int minJoin,
			@InBody(value = "vaildTime", desc = "游戏时长(单位:分钟)") int vaildTime,
			@InBody(value = "insurance", desc = "是否开启保险(0=未开,1=开启)") int insurance,
			@InBody(value = "straddle", desc = "是否开启闭眼盲注(0=未开,1=开启)") int straddle,
			@InBody(value = "buyMax", desc = "单次筹码购买上限") int buyMax);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_TABLE_RUN, 
			desc="房主点击运行桌子")
	Object dzpker_table_start_run(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_TABLE_JOIN, 
			desc="加入指定桌子")
	Object dzpker_table_join(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_TABLE_LEAVE, 
			desc="退出指定桌子")
	Object dzpker_table_leave(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_SEAT_DOWN, 
			desc="座下指定位置")
	Object dzpker_seat_down(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId,
			@InBody(value = "seatIndex", desc = "座位下标") int seatIndex);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_SEAT_UP, 
			desc="从座位上站起")
	Object dzpker_seat_up(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId,
			@InBody(value = "seatIndex", desc = "座位下标") int seatIndex);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_BUY_CHIP, 
			desc="购买筹码")
	Object dzpker_buy_chip(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId,
			@InBody(value = "chipNum", desc = "筹码数量") int chipNum);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_TABLE_BT, 
			desc="正常游戏表态")
	Object dzpker_table_bt(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId,
			@InBody(value = "bt", desc = "表态(1=弃牌,2=过牌,3=跟注,4=加注,5=全下)") int bt,
			@InBody(value = "btVal", desc = "表态值") int btVal);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_QUERY_BUYCHIP_LIST, 
			desc="房主获取筹码购买申请列表")
	Object dzpker_query_buychip_list(@InSession Long accountId);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_TRANS_BUYCHIP_ITEM, 
			desc="房主处理筹码购买申请条目")
	Object dzpker_trans_buychip_item(@InSession Long accountId,
			@InBody(value = "itemId", desc = "购买申请Id") String itemId,
			@InBody(value = "bt", desc = "处理表态(1=同意,2=拒绝)") int bt);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_TABLE_STRADDL_BT, 
			desc="闭眼盲注表态")
	Object dzpker_straddle_bt(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId,
			@InBody(value = "seatIndex", desc = "座位下标") int seatIndex);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_FIGHTED_TABLE_LIST, 
			desc="获取参与过游戏的未解散的桌子")
	Object dzpker_get_fighted_table_list(@InSession Long accountId);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_TABLE_PREV_FIGHT_RECOED, 
			desc="获取桌子上一局游戏信息")
	Object dzpker_table_prev_fight(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_TABLE_GET_PLAYER_LIST, 
			desc="获取桌子所有玩家数据信息")
	Object dzpker_table_player_list(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_TABLE_ONCE_PLAYER_WINSCORE, 
			desc="获取桌子个人赢亏数据")
	Object dzpker_table_player_once_winscore(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_TABLE_BUY_INSURANCE, 
			desc="购买保险")
	Object dzpker_table_buy_insurance(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId,
			@InBody(value = "bt", desc = "0=不买,1=购买") int bt,
			@InBody(value = "buyCards", desc = "购买的牌数据") int []buyCards,
			@InBody(value = "buyMoney", desc = "购买筹码") int buyMoney,
			@InBody(value = "payMoney", desc = "赔付筹码") int payMoney
			);
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_GET_CAREER_INFO, 
			desc="获取生涯历史数据")
	Object dzpker_table_get_caree_info(@InSession Long accountId);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_SHOW_CARD_BT, 
			desc="结算后亮牌表态")
	Object dzpker_table_show_card_bt(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_TABLE_GET_INFO, 
			desc="获取指定桌子数据")
	Object dzpker_table_get_table_info(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_TABLE_ALL_WIN_SCORE_INFO, 
			desc="获取指定桌子所有参与过游戏的玩家输赢明细")
	Object dzpker_table_get_all_win_score_info(@InSession Long accountId,
			@InBody(value = "recordId", desc = "桌子记录Id") String recordId);
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_TABLE_STATE_NOTIFY, 
			desc="推送消息(游戏桌子状态变化通知)")
	Object dzpker_table_state_notify();
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_SEAT_STATE_NOTIFY, 
			desc="推送消息(游戏座位数据变化通知)")
	Object dzpker_seat_state_notify();
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_BET_STATE_NOTIFY, 
			desc="推送消息(游戏桌子表态通知)")
	Object dzpker_table_bt_notify();
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_OVER_SETTLEMENT_NOTIFY, 
			desc="推送消息(游戏桌子总结算数据通知)")
	Object dzpker_table_settlement_notify();
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_INSURANCE_SETTLEMENT_NOTIFY, 
			desc="推送消息(保险结算结果数据通知)")
	Object dzpker_table_settlement_insurance_notify();
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_BUY_CHIP_SUCESS_NOTIFY, 
			desc="推送消息(筹码购买成功通知)")
	Object dzpker_table_buy_chip_sucess_notify();
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_SHOW_CARD_NOTIFY, 
			desc="推送消息(玩家亮牌通知)")
	Object dzpker_table_show_card_notify();
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_STRADDL_BT_NOTIFY, 
			desc="推送消息(闭眼盲注表态通知)")
	Object dzpker_table_straddl_bt_notify();
	
	@NetworkApi(value = DzpkerDefine.DZPKER_COMMAND_SEAT_KICK_NOTIFY, 
			desc="推送消息(玩家从座位上被踢起通知)")
	Object dzpker_table_seat_kick_notify();
}
