package com.palmjoys.yf1b.act.cooltime.model;

public class CoolTimeResult {

	/**
	 * 下次重置时间
	 * */
	public long nextTime;
	
	/**
	 * 自上次冷却以来到现在所经过的时间(单位秒)
	 * */
	public long interval;
	
	/**
	 * 冷却间隔时间(单位秒)
	 * */
	public long cooltime;
	
	/**
	 * 是否重置
	 * */
	public boolean bReset;
	
	public CoolTimeResult(){
		nextTime = 0;
		interval = 0;
		cooltime = 0;
		bReset = false;
	}
}
