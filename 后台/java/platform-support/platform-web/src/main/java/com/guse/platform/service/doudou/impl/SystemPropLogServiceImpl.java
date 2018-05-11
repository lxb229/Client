package com.guse.platform.service.doudou.impl;

import java.util.Date;

//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.dao.doudou.SystemPropLogMapper;
import com.guse.platform.service.doudou.SystemPropLogService;
import com.guse.platform.entity.doudou.SystemPropLog;

/**
 * system_prop_log
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class SystemPropLogServiceImpl extends BaseServiceImpl<SystemPropLog, java.lang.Integer> implements SystemPropLogService{

	@Autowired
	private SystemPropLogMapper  systemPropLogMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(systemPropLogMapper);
	}

	@Override
	public Result<Integer> saveOrUpdatePropLog(SystemPropLog propLog) {
		ValidataBean validata = propLog.validateModel();
		if(!validata.isFlag()){
			return new Result<Integer>(00000,validata.getMsg());
		}
		Integer result = null;
		if(null != propLog.getSplId()){
			result = systemPropLogMapper.updateByIdSelective(propLog);
		}else{
			propLog.setCreateTime(new Date());
			result = systemPropLogMapper.insert(propLog);
		}
		return new Result<Integer>(result);
	}
}
