package com.guse.platform.service.doudou.impl;

//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.dao.doudou.SystemRoleMenuMapper;
import com.guse.platform.service.doudou.SystemRoleMenuService;
import com.guse.platform.entity.doudou.SystemRoleMenu;

/**
 * system_role_menu
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class SystemRoleMenuServiceImpl extends BaseServiceImpl<SystemRoleMenu, java.lang.Integer> implements SystemRoleMenuService{

	@Autowired
	private SystemRoleMenuMapper  systemRoleMenuMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(systemRoleMenuMapper);
	}
}
