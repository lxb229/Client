package com.wangzhixuan.mapper;

import com.wangzhixuan.model.SystemTask;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  * 系统任务表 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-09
 */
public interface SystemTaskMapper extends BaseMapper<SystemTask> {
	/**
	 * 获取时间最早的未处理或者处理失败次数3次以内的任务10条
	 * @return
	 */
	List<SystemTask> getPendingTask();
}