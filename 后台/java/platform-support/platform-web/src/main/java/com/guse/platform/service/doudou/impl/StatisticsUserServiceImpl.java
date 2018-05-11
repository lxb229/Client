package com.guse.platform.service.doudou.impl;

//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.dao.doudou.StatisticsUserMapper;
import com.guse.platform.service.doudou.StatisticsUserService;
import com.guse.platform.entity.doudou.StatisticsUser;

/**
 * statistics_user
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class StatisticsUserServiceImpl extends BaseServiceImpl<StatisticsUser, java.lang.Integer> implements StatisticsUserService{

	@Autowired
	private StatisticsUserMapper  statisticsUserMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(statisticsUserMapper);
	}
}
