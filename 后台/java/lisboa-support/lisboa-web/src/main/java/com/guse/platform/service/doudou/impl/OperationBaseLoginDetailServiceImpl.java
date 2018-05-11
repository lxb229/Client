package com.guse.platform.service.doudou.impl;

//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.dao.doudou.OperationBaseLoginDetailMapper;
import com.guse.platform.service.doudou.OperationBaseLoginDetailService;
import com.guse.platform.entity.doudou.OperationBaseLoginDetail;

/**
 * operation_base_login_detail
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class OperationBaseLoginDetailServiceImpl extends BaseServiceImpl<OperationBaseLoginDetail, java.lang.String> implements OperationBaseLoginDetailService{

	@Autowired
	private OperationBaseLoginDetailMapper  operationBaseLoginDetailMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(operationBaseLoginDetailMapper);
	}
}
