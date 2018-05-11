package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.commons.base.Constant;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.mapper.SystemTaskMapper;
import com.wangzhixuan.service.ISystemTaskService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.Date;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统任务表 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-09
 */
@Service
public class SystemTaskServiceImpl extends ServiceImpl<SystemTaskMapper, SystemTask> implements ISystemTaskService {
	@Override
	public Result deal(Integer taskCmd, String jsonStr) {
		Result result = null;
		if(StringUtils.isNotBlank(jsonStr)) {
			result = new Result();
			SystemTask task = new SystemTask();
			task.setTaskCmd(taskCmd);
			task.setJsonContent(jsonStr);
			task.setTaskStatus(0);
			task.setTaskNum(0);
			task.setCreateTime(new Date());
			boolean success = this.insert(task);
			result.setSuccess(success);
			result.setMsg("成功");
		}
        return result;
	}
	@Override
	public Result insertPlayer(String content) {
		
		return deal(Constant.TASK_PLAYER, content);
	}
	@Override
	public Result insertRoom(String content) {
		return deal(Constant.TASK_ROOM, content);
	}
	@Override
	public Result insertLoginLog(String content) {
		return deal(Constant.TASK_LOGINLOG, content);
	}
	@Override
	public Result insertJoinRoom(String content) {
		return deal(Constant.TASK_JOINROOM, content);
	}
	@Override
	public Result insertJoinParty(String content) {
		return deal(Constant.TASK_JOINPARTY, content);
	}
	@Override
	public Result insertJettonLog(String content) {
		return deal(Constant.TASK_JETTONLOG, content);
	}
	@Override
	public Result insertBetLog(String content) {
		return deal(Constant.TASK_BETLOG, content);
	}
	@Override
	public Result insertInsuranceLog(String content) {
		return deal(Constant.TASK_INSURANCELOG, content);
	}
	@Override
	public Result insertInsuranceProfit(String content) {
		return deal(Constant.TASK_INSURANCEPROFIT, content);
	}
	@Override
	public Result insertPlayerProfit(String content) {
		return deal(Constant.TASK_PLAYERPROFIT, content);
	}
	@Override
	public Result insertRoomDisappear(String content) {
		return deal(Constant.TASK_ROOMDISAPPEAR, content);
	}

}
