package com.guse.platform.dao.doudou;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.doudou.SystemOrder;


/**
 * system_order
 * @see SystemOrderMapper.xml
 * @author nbin
 * @date 2017年7月18日 下午2:02:28 
 * @version V1.0
 */
public interface SystemOrderMapper extends  BaseMapper<SystemOrder, java.lang.Integer>{
	
	/**
	 * 统计订单单数和金额
	 * @Title: countOrder 
	 * @param @param order
	 * @param @return 
	 * @return Map<String, Object>
	 */
	Map<String, Object> countOrder(SystemOrder order);
	
	/**
	 * 统计代理单数和金额
	 * @Title: countAgency 
	 * @param @param order
	 * @param @return 
	 * @return Map<String, Object>
	 */
	Map<String, Object> countAgency(SystemOrder order);
	
	/**
	 * 获取代理提现数据
	 * @Title: getAgencyList 
	 * @param @param order
	 * @param @return 
	 * @return Map<String, Object>
	 */
	List<Map<String, Object>> getCashList(SystemOrder order);
	
	/**
	 * 更新订单提现状态
	 * @param mouth 月份
	 * @param cityId 地区
	 * @return
	 */
	Integer updateCashState(@Param("mouth") String mouth,@Param("cityId") int cityId);
	
	/**
	 * 根据订单编号获取订单
	 * @param tradeNo
	 * @return
	 */
	SystemOrder getOrderByNo(@Param("tradeNo")String tradeNo);
	
}
