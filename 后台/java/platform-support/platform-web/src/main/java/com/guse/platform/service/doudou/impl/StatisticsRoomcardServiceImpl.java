package com.guse.platform.service.doudou.impl;

//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.dao.doudou.StatisticsRoomcardMapper;
import com.guse.platform.service.doudou.StatisticsRoomcardService;
import com.guse.platform.entity.doudou.StatisticsRoomcard;

/**
 * statistics_roomcard
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class StatisticsRoomcardServiceImpl extends BaseServiceImpl<StatisticsRoomcard, java.lang.Integer> implements StatisticsRoomcardService{

	@Autowired
	private StatisticsRoomcardMapper  statisticsRoomcardMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(statisticsRoomcardMapper);
	}
}
