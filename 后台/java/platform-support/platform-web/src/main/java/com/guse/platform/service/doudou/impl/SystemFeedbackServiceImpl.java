package com.guse.platform.service.doudou.impl;

import java.util.Date;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.dao.doudou.SystemFeedbackMapper;
import com.guse.platform.service.doudou.SystemFeedbackService;
import com.guse.platform.entity.doudou.SystemFeedback;
import com.guse.platform.entity.system.Users;

/**
 * system_feedback
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class SystemFeedbackServiceImpl extends BaseServiceImpl<SystemFeedback, java.lang.Integer> implements SystemFeedbackService{

	@Autowired
	private SystemFeedbackMapper  systemFeedbackMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(systemFeedbackMapper);
	}

	@Override
	public Result<Integer> saveOrUpdateFeedback(SystemFeedback feedback, Users users) {
		ValidataBean validata = feedback.validateModel();
		if(!validata.isFlag()){
			return new Result<Integer>(00000,validata.getMsg());
		}
		Integer result = null;
		if(null != feedback.getId()){
			result = systemFeedbackMapper.updateByIdSelective(feedback);
		}else{
			feedback.setUserId(users.getUserId());
			feedback.setCreateTime(new Date());
			result = systemFeedbackMapper.insert(feedback);
		}
		return new Result<Integer>(result);
	}
}
