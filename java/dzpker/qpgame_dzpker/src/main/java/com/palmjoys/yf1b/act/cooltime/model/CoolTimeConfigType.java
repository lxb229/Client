package com.palmjoys.yf1b.act.cooltime.model;

/**
 * 重置时间功能编号定义,对应PlayerCoolTimeConfig.xls的id和SysCoolTimeConfig.xls的id
 */
public class CoolTimeConfigType {
	/** 系统重置时间点功能编号定义 */

	// 金库每日利息结算时间点
	public static final int SYSTIME_RESET_BANK_ACCURAL = 1;
	// 每周抽奖时间点
	public static final int SYSIME_RESET_LUCKDRAW = 2;

	/** 玩家重置时间点功能编号 */
	// 每日签到重置时间点
	public static final int PLAYERTIME_RESET_DAYSIGN = 1;
	// 累计签 到每月重置时间点
	public static final int PLAYERTIME_RESET_MONTHSIGN = 2;
	// 彩票每日免费次数重置时间点
	public static final int PLAYERTIME_RESET_LOTTERY = 3;
	// 每日福利任务重置时间点
	public static final int PLAYERTIME_RESET_TASK = 4;
	// 小游戏每日免费次数重置时间点
	public static final int PLAYERTIME_LITTLEGAME = 5;
}
