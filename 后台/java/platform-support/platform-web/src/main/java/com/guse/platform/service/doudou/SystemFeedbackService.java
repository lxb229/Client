package com.guse.platform.service.doudou;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.doudou.SystemFeedback;
import com.guse.platform.entity.system.Users;

/**
 * system_feedback
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface SystemFeedbackService extends BaseService<SystemFeedback,java.lang.Integer>{

	/**
	 * 新增更新用户反馈
	 * @Title: saveOrUpdateFeedback 
	 * @param @param feedback
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> saveOrUpdateFeedback(SystemFeedback feedback ,Users users);
}
