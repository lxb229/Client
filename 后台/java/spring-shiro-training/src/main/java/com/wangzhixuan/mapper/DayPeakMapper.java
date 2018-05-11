package com.wangzhixuan.mapper;

import com.wangzhixuan.model.DayPeak;

import java.util.Date;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  * 每日峰值表 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
public interface DayPeakMapper extends BaseMapper<DayPeak> {

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
	DayPeak selectDayPeakByTime(@Param("createTime")Date createTime);
	
	/**
	 * 获取平均峰值
	 * @return
	 */
	Map<String, Object> getMeanPeak();
	
}