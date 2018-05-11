package com.guse.platform.utils.redis;

/**
 * 统一 redis 缓存keys
 * @author nbin
 * @date 2017年7月24日 下午1:55:38 
 * @version V1.0
 */
public final class RedisKeys {
	
	/** 实时数据 */
	public static final String OPERATION_REALTIME_DAY = "operation_realtime_day";
	
	/** 实时数据 在线用户明细 所有用户keys */
	public static final String OPERATION_BASE_USER_ONLINE = "operation_base_user_online";
	/** 实时数据 在线用户明细  错误记录*/
	public static final String OPERATION_BASE_USER_ONLINE_WRITEERROR = "operation_base_user_online:writeError";
	/** 在线用户，大厅 */
	public static final String OPERATION_BASE_USER_ONLINE_HALL = "operation_base_user_online:hall";
	/** 在线用户，游戏 */
	public static final String OPERATION_BASE_USER_ONLINE_GAME = "operation_base_user_online:game";
	/** 在线用户，新玩家 */
	public static final String OPERATION_BASE_USER_ONLINE_NEW = "operation_base_user_online:new";
	
	
	/** 活动大转盘统计结果 */
	public static final String GAME_ACTIVITY_ROTOR_STATISTICS = "game_activity_rotor_statistics";
	/** 活动大转盘统计，明细写入错误记录 */
	public static final String GAME_ACTIVITY_ROTOR_STATISTICS_WRITEERROR = "game_activity_rotor_statistics:writeError";
	

	/** 玩家注册的数据*/
	public static final String OPERATION_BASE_USER = "operation_base_user";
	/** 玩家key*/
	public static final String OPERATION_BASE_USER_PERMANENT = "operation_base_user_permanent";
	/**玩家注册，明细写入错误记录**/
	public static final String OPERATION_BASE_USER_WRITEERROR = "operation_base_user:writeError";
	/**玩家信息变更，明细写入错误记录**/
	public static final String OPERATION_BASE_USER_CHANGE_WRITEERROR = "operation_base_user_change:writeError";
	/**玩家信息纠正，明细写入错误记录**/
	public static final String OPERATION_BASE_USER_CORRECT_WRITEERROR = "operation_base_user_correct:writeError";
	/**重复注册userId数据**/
	public static final String OPERATION_BASE_USER_USERID_REPEAT = "operation_base_user_userid_repeat";
	
	/** 用户充值明细*/
	public static final String OPERATION_BASE_RECHARGE = "operation_base_recharge";
	/**用户充值，明细写入错误记录**/
	public static final String OPERATION_BASE_RECHARGE_WRITEERROR = "operation_base_recharge:writeError";
	
	/** 用户道具明细*/
	public static final String GAME_PROP_USER_INFO = "game_prop_user_info";
	/**用户道具，明细写入错误记录**/
	public static final String GAME_PROP_USER_INFO_WRITEERROR = "game_prop_user_info:writeError";
	
	/** ATT游戏金币明细 */
	public static final String GAME_ATT_GOLD_DETAIL = "game_att_gold_detail";
	/** ATT游戏金币明细，写入错误记录 */
	public static final String GAME_ATT_GOLD_DETAIL_WRITEERROR = "game_att_gold_detail:writeError";
	
	/** 捕鱼达人金币明细 */
	public static final String GAME_BYDR_GOLD_DETAIL = "game_bydr_gold_detail";
	/** 捕鱼达人金币明细，写入错误 */
	public static final String GAME_BYDR_GOLD_DETAIL_WRITEERROR = "game_bydr_gold_detail:writeError";

	
	/** 捕鱼达人道具明细 */
	public static final String GAME_BYDR_PROP_DETAIL = "game_bydr_prop_detail";
	/** 捕鱼达人金币道具，写入错误 */
	public static final String GAME_BYDR_PROP_DETAIL_WRITEERROR = "game_bydr_prop_detail:writeError";
	
	/** 用户登出明细 */
	public static final String OPERATION_BASE_LOGOUT_DETAIL = "operation_base_logout_detail";
	/** 用户登出明细，写入错误 */
	public static final String OPERATION_BASE_LOGOUT_DETAIL_WRITEERROR = "operation_base_logout_detail:writeError";
	
	/** 用户登录明细 */
	public static final String OPERATION_BASE_LOGIN_DETAIL = "operation_base_login_detail";
	/** 用户登录明细，写入错误 */
	public static final String OPERATION_BASE_LOGIN_DETAIL_WRITEERROR = "operation_base_login_detail:writeError";
	/** 用户登录时段分布统计 */
	public static final String OPERATION_ONLINE_LOGIN_COUNT = "operation_online_login_count";
	
	/** 德州扑克金币明细 */
	public static final String GAME_DZPK_GOLD_DETAIL = "game_dzpk_gold_detail";
	/** 德州扑克金币明细，写入错误 */
	public static final String GAME_DZPK_GOLD_DETAIL_WRITEERROR = "game_dzpk_gold_detail:writeError";
	
	/** 疯狂宝石押注明细 */
	public static final String GAME_FKBS_BET_DETAIL = "game_fkbs_bet_detail";
	/** 疯狂宝石押注明细，写入错误 */
	public static final String GAME_FKBS_BET_DETAIL_WRITEERROR = "game_fkbs_bet_detail:writeError";
	
	/** 疯狂宝石金币明细 */
	public static final String GAME_FKBS_GOLD_DETAIL = "game_fkbs_gold_detail";
	/** 疯狂宝石金币明细，写入错误 */
	public static final String GAME_FKBS_GOLD_DETAIL_WRITEERROR = "game_fkbs_gold_detail:writeError";
	
	/** 疯狂宝石开奖明细 */
	public static final String GAME_FKBS_PRIZE_DETAIL = "game_fkbs_prize_detail";
	/** 疯狂宝石开奖明细，写入错误 */
	public static final String GAME_FKBS_PRIZE_DETAIL_WRITEERROR = "game_fkbs_prize_detail:writeError";
	
	/** 游戏激活明细 */
	public static final String OPERATION_BASE_ACTIVE = "operation_base_active";
	/** 游戏激活明细，写入错误 */
	public static final String OPERATION_BASE_ACTIVE_WRITEERROR = "operation_base_active:writeError";
	
	/** 游戏加载明细 */
	public static final String OPERATION_BASE_LOAD = "operation_base_load";
	/** 游戏加载明细，写入错误 */
	public static final String OPERATION_BASE_LOAD_WRITEERROR = "operation_base_load:writeError";
	
	/** 游戏下载明细 */
	public static final String OPERATION_BASE_DOWNLOAD = "operation_base_download";
	/** 游戏下载明细，写入错误 */ 
	public static final String OPERATION_BASE_DOWNLOAD_WRITEERROR = "operation_base_download:writeError";
	
	/** 游戏进出明细 */
	public static final String OPERATION_BASE_GAME_INTO = "operation_base_game_into";
	/** 游戏进出明细，写入错误 */
	public static final String OPERATION_BASE_GAME_INTO_WRITEERROR = "operation_base_game_into:writeError";
	
	/** 龙珠探宝金币明细 */
	public static final String GAME_LZTB_GOLD_DETAIL = "game_lztb_gold_detail";
	/** 龙珠探宝金币明细，写入错误 */
	public static final String GAME_LZTB_GOLD_DETAIL_WRITEERROR = "game_lztb_gold_detail:writeError";
	
	/** 其他宝金币明细 */
	public static final String GAME_OTHER_GOLD_DETAIL = "game_other_gold_detail";
	/** 其他金币明细，写入错误 */
	public static final String GAME_OTHER_GOLD_DETAIL_WRITEERROR = "game_other_gold_detail:writeError";

	/** 用户钻石变更明细 */
	public static final String GAME_DIAMOND_DETAIL = "game_diamond_detail";
	/** 用户钻石变更明细，写入错误 */
	public static final String GAME_DIAMOND_DETAIL_WRITEERROR = "game_diamond_detail:writeError";
	
	/** 糖果派对金币明细 */
	public static final String GAME_TGPD_GOLD_DETAIL = "game_tgpd_gold_detail";
	/** 糖果派对金币明细，写入错误 */
	public static final String GAME_TGPD_GOLD_DETAIL_WRITEERROR = "game_tgpd_gold_detail:writeError";
	
	/** 水浒传金币明细 */
	public static final String GAME_SHZ_GOLD_DETAIL = "game_shz_gold_detail";
	/** 水浒传金币明细，写入错误 */
	public static final String GAME_SHZ_GOLD_DETAIL_WRITEERROR = "game_shz_gold_detail:writeError";
	
	
	/** 用户*/
	public static final String USERS = "_users";
	
	/** 设备*/
	public static final String DEVICES = "_devices";
	
	/** 在线玩家趋势 */
	public static final String OPERATION_ONLINE_TREND = "operation_online_trend";
	
	/** 付费统计（按日、下载渠道实时） */
	public static final String OPERATION_MONEY_DAY = "operation_money_day";
	
	
	
	/*................玩家分析相关.....start.........*/
	/** 玩家分析新增数据统计*/
	public static final String OPERATION_PLAYER_NEW = "operation_player_new";
	/** 玩家分析首次在线时间分析统计*/
	public static final String OPERATION_PLAYER_NEW_FIRST_ONLINE = "operation_player_new_first_online";
	
	/** 玩家小号分析(设备)*/
	public static final String PLAYER_NEW_SMALL_MARK_DEVICE = "player_new_small_mark_device";
	
	/** 玩家小号分析(ip)*/
	public static final String PLAYER_NEW_SMALL_MARK_IP = "player_new_small_mark_ip";
	
	/** 新增玩家渠道分析*/
	public static final String OPERATION_PLAYER_NEW_CHANNEL = "operation_player_new_channel";
	
	/** 活跃玩家趋势分析*/
	public static final String OPERATION_PLAYER_BRISK_TEND = "operation_player_brisk_tend";
	
	/** 活跃玩家分析*/
	public static final String OPERATION_PLAYER_BRISK = "operation_player_brisk";
	
	/** 活跃玩家渠道分析*/
	public static final String OPERATION_PLAYER_BRISK_CHANNEL = "operation_player_brisk_channel";
	
	/** 活跃玩家结构事件(游戏天数)*/
	public static final String OPERATION_PLAYER_BRISK_GAMEDAYS = "operation_player_brisk_gamedays";
	/** 活跃玩家结构事件(付费贡献)*/
	public static final String OPERATION_PLAYER_BRISK_PAY = "operation_player_brisk_pay";
	/*................玩家分析相关.....end.........*/
	
	/*................留存相关.......start.......*/
	/** 活跃留存新增用户集合*/
	//public static final String OPERATION_REMAIN_NEW_USER = "operation_remain_new_user"; 
	/** 活跃留存新增用户重复集合*/
	//public static final String OPERATION_REMAIN_NEW_USER_REPEAT = "operation_remain_new_user_repeat"; 
	/** 活跃留存新增用户key集合*/
	//public static final String OPERATION_REMAIN_NEW_USER_KEYS = "operation_remain_new_user_keys"; 
	/** 渠道推广*/
	public static final String OPERATION_REMAIN_CHANNEL = "operation_remain_channel"; 
	/** 注册阶段分析*/
	public static final String OPERATION_REMAIN_REGISTER = "operation_remain_register"; 
	
	
	/*................留存相关........end......*/
	
	/*................付费分析.......start.......*/
	/** 付费玩家趋势 */
	public static final String OPERATION_MONEY_TREND = "operation_money_trend"; 
	
	/** 付费玩家排行 */
	public static final String OPERATION_MONEY_RANKING = "operation_money_ranking";
	/*................付费分析.......end.......*/
	
	
	
	/*................游戏-经济概况.......start......*/
	
	/** 概况总览 */
	public static final String GAME_ECONOMY_STATISTICS = "game_economy_statistics";
	
	/** 回收与免充 */
	public static final String GAME_ECONOMY_RECOVERY_NORECHARGE = "game_economy_recovery_norecharge";
	
	/*................游戏-经济概况.......end.......*/
	
	
	/*................游戏-道具概况.......start.......*/
	/** 道具概况 */
	public static final String GAME_PROP_STATISTICS = "game_prop_statistics"; 
	
	/*................游戏-道具概况.......end.......*/
	
	
	/*................游戏-邮件概况.......start.......*/
	/** 用户邮件明细 */
	public static final String GAME_MAIL_DATEIL = "game_mail_dateil";
	/** 用户邮件明细，写入错误 */
	public static final String GAME_MAIL_DATEIL_WRITEERROR = "game_mail_dateil:writeError";
	/** 用户邮件排行统计 */
	public static final String GAME_MAIL_STATISTICS = "game_mail_statistics";
	
	/*................游戏-邮件概况.......end.......*/
	
	
	/*................游戏-游戏概况.......start.......*/
	/** 游戏概况 */
	public static final String GAME_GAME_STATISTICS = "game_game_statistics";
	
	/** 德州扑克房间信息 */
	public static final String GAME_DZPK_ROOM = "game_dzpk_room";
	
	/** 捕鱼达人房间信息 */
	public static final String GAME_BYDR_ROOM = "game_bydr_room";
	
	/** 糖果派对房间信息 */
	public static final String GAME_TGPD_BET = "game_tgpd_bet";
	
	/** 水浒传房间信息 */
	public static final String GAME_SHZ_ROOM = "game_shz_room";
	
	/** att房间信息 */
	public static final String GAME_ATT_ROOM = "game_att_room";
	
	
	/** 实时输赢*/
	public static final String GAME_REALTIME_WINLOSE = "game_realtime_winlose";
	
	
	/*................游戏-邮件概况.......end.......*/
	
}
