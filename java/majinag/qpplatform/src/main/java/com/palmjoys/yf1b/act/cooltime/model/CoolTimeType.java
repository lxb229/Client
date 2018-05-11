package com.palmjoys.yf1b.act.cooltime.model;

/**时间重置类型,对应于表CoolTimeType.xlsx*/
public class CoolTimeType {

	/**按每月定义时间重置*/
	public static final int COOLTIME_TYPE_MONTH = 1;
	/**按每周定义时间重置*/
	public static final int COOLTIME_TYPE_WEEK = 2;
	/**按每天定义时间重置*/
	public static final int COOLTIME_TYPE_DAY = 3;
	/**按每小定义时间重置*/
	public static final int COOLTIME_TYPE_HOUR = 4;
	/**按自定义间隔时间重置*/
	public static final int COOLTIME_TYPE_CUSTOM = 5;
}
