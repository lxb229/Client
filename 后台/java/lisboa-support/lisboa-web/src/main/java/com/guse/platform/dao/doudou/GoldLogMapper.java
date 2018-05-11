package com.guse.platform.dao.doudou;


import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.doudou.GoldLog;


/**
 * gold_log
 * @see GoldLogMapper.xml
 * @author nbin
 * @date 2017年7月18日 下午2:02:28 
 * @version V1.0
 */
public interface GoldLogMapper extends  BaseMapper<GoldLog, java.lang.Integer>{
	/**
	 * 统计订单单数和金额
	 * @Title: countOrder 
	 * @param @param order
	 * @param @return 
	 * @return Map<String, Object>
	 */
	Map<String, Object> countProfit(@Param("bParam")GoldLog goldLog);
}
