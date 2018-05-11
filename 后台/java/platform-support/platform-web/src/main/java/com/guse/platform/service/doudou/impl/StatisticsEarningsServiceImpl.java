package com.guse.platform.service.doudou.impl;

//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.dao.doudou.StatisticsEarningsMapper;
import com.guse.platform.service.doudou.StatisticsEarningsService;
import com.guse.platform.entity.doudou.StatisticsEarnings;

/**
 * statistics_earnings
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class StatisticsEarningsServiceImpl extends BaseServiceImpl<StatisticsEarnings, java.lang.Integer> implements StatisticsEarningsService{

	@Autowired
	private StatisticsEarningsMapper  statisticsEarningsMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(statisticsEarningsMapper);
	}
}
