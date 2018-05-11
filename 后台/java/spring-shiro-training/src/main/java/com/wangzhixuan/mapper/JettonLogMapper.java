package com.wangzhixuan.mapper;

import com.wangzhixuan.model.JettonLog;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  * 筹码日志表 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
public interface JettonLogMapper extends BaseMapper<JettonLog> {

	/**
	 * 获取筹码数据汇总
	 * @return
	 */
	Map<String, Object> selectJettonData();
	
}