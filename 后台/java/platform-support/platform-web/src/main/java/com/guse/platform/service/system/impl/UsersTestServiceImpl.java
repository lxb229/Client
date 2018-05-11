package com.guse.platform.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.dao.system.UsersMapper;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.system.UsersTestService;

@Service
public class UsersTestServiceImpl extends BaseServiceImpl<Users, Integer> implements UsersTestService{
	
	@Autowired
	private UsersMapper usersMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(usersMapper);
	}
}
