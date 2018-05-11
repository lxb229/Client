package com.guse.platform.service.doudou.impl;

//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.dao.doudou.SystemMessageMapper;
import com.guse.platform.service.doudou.SystemMessageService;
import com.guse.platform.entity.doudou.SystemMessage;

/**
 * system_message
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class SystemMessageServiceImpl extends BaseServiceImpl<SystemMessage, java.lang.Integer> implements SystemMessageService{

	@Autowired
	private SystemMessageMapper  systemMessageMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(systemMessageMapper);
	}
}
