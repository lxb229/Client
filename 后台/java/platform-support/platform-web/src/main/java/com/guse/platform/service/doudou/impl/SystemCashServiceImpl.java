package com.guse.platform.service.doudou.impl;

//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.dao.doudou.SystemCashMapper;
import com.guse.platform.service.doudou.SystemCashService;
import com.guse.platform.entity.doudou.SystemCash;

/**
 * system_cash
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class SystemCashServiceImpl extends BaseServiceImpl<SystemCash, java.lang.Integer> implements SystemCashService{

	@Autowired
	private SystemCashMapper  systemCashMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(systemCashMapper);
	}

	@Override
	public Result<Integer> updateCash(SystemCash cash) {
		ValidataBean validata = cash.validateModel();
		if(!validata.isFlag()){
			return new Result<Integer>(00000,validata.getMsg());
		}
		Integer result = null;
		if(null != cash.getScId()){
			//更新用户不可以更改登录名
			result = systemCashMapper.updateByIdSelective(cash);
		}else{
			result = systemCashMapper.insert(cash);
		}
		return new Result<Integer>(result);
	}

	@Override
	public SystemCash getCashById(Integer scId) {
		
		return systemCashMapper.selectById(scId);
	}
}
