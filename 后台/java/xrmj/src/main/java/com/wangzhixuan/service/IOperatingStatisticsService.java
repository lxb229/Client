package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.Investment;
import com.wangzhixuan.model.OperatingStatistics;
import com.wangzhixuan.model.SystemOrder;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 运营统计 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public interface IOperatingStatisticsService extends IService<OperatingStatistics> {
	/**
	 * 每一笔订单都需要补充到运营统计
	 * @param order
	 * @return
	 */
	Result supplementOperating(SystemOrder order); 
	/**
	 * 每一笔投资都需要扣除房卡毛利润
	 * @param order
	 * @return
	 */
	Result supplementOperating(Investment investment); 
	
	/**
	 * 每一笔房卡使用都需要补充到运营统计
	 * @param amount
	 * @return
	 */
	Result operatingByGame(Integer amount); 
	
	/**
	 * 获取运营统计
	 * @return
	 */
	OperatingStatistics getOperating();
}
