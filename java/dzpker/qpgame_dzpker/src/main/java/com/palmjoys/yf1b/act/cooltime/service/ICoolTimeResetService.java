package com.palmjoys.yf1b.act.cooltime.service;

import com.palmjoys.yf1b.act.cooltime.model.CoolTimeCondition;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeResult;

public interface ICoolTimeResetService {

	/**
	 * prevRestTime:上次重置时间
	 * condition:重置条件
	 * 返回:下次重置时间点,冷却时间(以秒为单位),重置标志(true表示已重置)
	 * */
	public CoolTimeResult resetTime(long nextRestTime, CoolTimeCondition condition);
	
	/**
	 * 获取重置服务id
	 * */
	public int getServiceId();
	
	/**
	 * 获取首次初始化时间
	 * */
	public long getFristResetTime(CoolTimeCondition condition);
}
