package com.guse.platform.dao.doudou;


import java.util.List;

import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.doudou.SystemTask;


/**
 * system_task
 * @see SystemTaskMapper.xml
 * @author nbin
 * @date 2017年7月18日 下午2:02:28 
 * @version V1.0
 */
public interface SystemTaskMapper extends  BaseMapper<SystemTask, java.lang.Integer>{
	
	/**
	 * 获取时间最早的未处理或者处理失败次数3次以内的任务10条
	 * @return
	 */
	List<SystemTask> getPendingTask();
	
}
