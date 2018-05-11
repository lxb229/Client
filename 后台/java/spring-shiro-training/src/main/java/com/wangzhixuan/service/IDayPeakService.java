package com.wangzhixuan.service;

import com.wangzhixuan.model.DayPeak;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 每日峰值表 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
public interface IDayPeakService extends IService<DayPeak> {
	/**
	 * 获取最高峰值数据
	 * @return
	 */
	DayPeak selectMaxDayPeak();
	
	/**
	 * 获取指定日期的峰值数据
	 * @param createTime 指定日期
	 * @return
	 */
	DayPeak selectDayPeakByTime(Date createTime);
	
	/**
	 * 获取峰值数据
	 * @return
	 */
	Map<String, Object> selectPeakData();
}
