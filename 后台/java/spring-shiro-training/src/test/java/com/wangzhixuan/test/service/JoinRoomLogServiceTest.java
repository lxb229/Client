package com.wangzhixuan.test.service;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.wangzhixuan.model.JoinRoomLog;
import com.wangzhixuan.service.IJoinRoomLogService;
import com.wangzhixuan.test.base.BaseTest;

/**
 * 用户service
 * @author nbin
 * @date 2017年7月18日 上午10:30:06 
 * @version V1.0
 */
public class JoinRoomLogServiceTest extends BaseTest {
	
	@Autowired
	private IJoinRoomLogService joinRoomLogService;
	/*
	 * 分页查询用户
	 */
	@Test
	public void getLogId(){
		JoinRoomLog log = new JoinRoomLog();
		log.setHouseOwner("123451");
		log.setJoinTime(new Date());
		log.setPlayerId("123123");
		log.setRoomId(345987);
		boolean success = joinRoomLogService.insert(log);
		if(success) {
			System.out.println(log.getId());
		}
	}
	
}
