package com.guse.platform.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.dao.system.LoginLogMapper;
import com.guse.platform.entity.system.LoginLogs;
import com.guse.platform.service.system.LoginLogsService;

@Service
public class LoginLogsServiceImpl extends BaseServiceImpl<LoginLogs, java.lang.Integer> implements LoginLogsService {
	@Autowired
	private LoginLogMapper  loginLogMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(loginLogMapper);
	}
}
