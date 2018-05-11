package com.guse.platform.service.doudou.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Constant;
import com.guse.platform.common.base.Result;
import com.guse.platform.dao.doudou.SystemTaskMapper;
import com.guse.platform.service.doudou.SystemTaskService;
import com.guse.platform.entity.doudou.SystemTask;

/**
 * system_task
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class SystemTaskServiceImpl extends BaseServiceImpl<SystemTask, java.lang.Integer> implements SystemTaskService{

	@Autowired
	private SystemTaskMapper  systemTaskMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(systemTaskMapper);
	}

	@Override
	public Result<Integer> deal(Integer taskCmd, String jsonStr) {
		if(StringUtils.isNotBlank(jsonStr)) {
			SystemTask task = new SystemTask();
			task.setTaskCmd(taskCmd);
			task.setJsonContent(jsonStr);
			task.setTaskStatus(0);
			task.setTaskNum(0);
			task.setCreateTime(new Date());
			int success = this.insert(task);
			return new Result<Integer>(success);
		} else {
			return new Result<Integer>(00000,"数据为空！");
		}
	}

	@Override
	public Result<Integer> operateUser(String content) {
		return deal(Constant.TASK_USER, content);
	}

	@Override
	public Result<Integer> operateRoomCard(String content) {
		return deal(Constant.TASK_ROOMCARD, content);
	}

	@Override
	public Result<Integer> operateClub(String content) {
		return deal(Constant.TASK_CLUB, content);
	}

	@Override
	public Result<Integer> operateClubUser(String content) {
		return deal(Constant.TASK_CLUBUSER, content);
	}

	@Override
	public Result<Integer> operateUserLuck(String content) {
		return deal(Constant.TASK_USERLUCK, content);
	}
}
