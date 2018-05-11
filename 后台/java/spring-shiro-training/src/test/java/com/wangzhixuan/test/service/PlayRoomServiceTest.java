package com.wangzhixuan.test.service;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.wangzhixuan.mapper.PlayRoomMapper;
import com.wangzhixuan.model.PlayRoom;
import com.wangzhixuan.service.IPlayRoomService;
import com.wangzhixuan.test.base.BaseTest;

/**
 * 用户service
 * @author nbin
 * @date 2017年7月18日 上午10:30:06 
 * @version V1.0
 */
public class PlayRoomServiceTest extends BaseTest {
	
	@Autowired
	private PlayRoomMapper playRoomMapper;
	@Autowired
	private IPlayRoomService playRoomService;
	/*
	 * 分页查询用户
	 */
	@Test
	public void getPlayRoomBy(){
		PlayRoom playRoom = playRoomMapper.getPlayRoomBy(null, null, 456323, new Date(), "30");
		System.out.println(playRoom);
	}
	@Test
	public void update() {
		int updateNum = playRoomService.updateRoomPartyNum(2, "123123", 345345, new Date());
		System.out.println(updateNum);
	}
	
}
