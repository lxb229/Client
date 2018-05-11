package com.wangzhixuan.test.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wangzhixuan.mapper.SystemTaskMapper;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.vo.PlayerVo;
import com.wangzhixuan.test.base.BaseTest;

/**
 * 用户service
 * @author nbin
 * @date 2017年7月18日 上午10:30:06 
 * @version V1.0
 */
public class PlayerServiceTest extends BaseTest {
	
	@Autowired
	private SystemTaskMapper taskMapper;
	private Gson gson = new Gson();
	/*
	 * 分页查询用户
	 */
	@Test
	public void selectPlayerPage(){
		
		List<SystemTask> list = taskMapper.getPendingTask();
		
		for (int i = 0; i < list.size(); i++) {
			String content = "["+list.get(i).getTaskContent()+"]";
			PlayerVo vo = gson.fromJson(content, new TypeToken<PlayerVo>(){}.getType());
			System.out.println(vo.toString());
		}
	}
	
}
