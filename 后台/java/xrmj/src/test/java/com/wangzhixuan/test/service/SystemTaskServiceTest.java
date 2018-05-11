package com.wangzhixuan.test.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangzhixuan.mapper.SystemTaskMapper;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.test.base.BaseTest;

/**
 * 用户service
 * @author nbin
 * @date 2017年7月18日 上午10:30:06 
 * @version V1.0
 */
public class SystemTaskServiceTest extends BaseTest {
	
	@Autowired
	private SystemTaskMapper taskMapper;
	/*
	 * 分页查询用户
	 */
	@Test
	public void getPendingTask(){
		
		List<SystemTask> list = taskMapper.getPendingTask();
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getTaskCmd()+"    "+list.get(i).getTaskContent());
		}
		
	}
	
}
