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
			task.setTaskContent(jsonStr);
			task.setTaskStatus(0);
			task.setTaskNum(0);
			task.setTaskLv(1);
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
	public Result insertOrder(String content) {
		return deal(Constant.TASK_ORDER, content);
	}
	@Override
	public Result insertGamePlayer(String content) {
		return deal(Constant.TASK_GAMEPLAYER, content);
	}
	@Override
	public Result insertPlayerWish(String content) {
		return deal(Constant.TASK_PLAYERWISH, content);
	}
	@Override
	public Result insertMahjong(String content) {
		return deal(Constant.TASK_MAHJONG, content);
	}
	@Override
	public Result insertMahjongPlayer(String content) {
		return deal(Constant.TASK_MAHJONGPLAYER, content);
	}
	@Override
	public Result insertMahjongCard(String content) {
		return deal(Constant.TASK_MAHJONGCARD, content);
	}
	@Override
	public Result insertMahjongCombat(String content) {
		return deal(Constant.TASK_MAHJONGCOMBAT, content);
	}
	@Override
	public Result insertUserLuck(String content) {
		return deal(Constant.TASK_USERLUCK, content);
	}
	

}
