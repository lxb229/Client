package com.guse.platform.service.doudou;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.doudou.SystemTask;

/**
 * system_task
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface SystemTaskService extends BaseService<SystemTask,java.lang.Integer>{

	/**
	 * 处理服务器发送的定时任务
	 * @return
	 */
	Result<Integer> deal(Integer taskCmd, String jsonStr);
	/**
	 * 处理用户信息	
	 * @param content
	 */
	Result<Integer> operateUser(String content);
	/**
	 * 处理房卡信息
	 * @param content
	 */
	Result<Integer> operateRoomCard(String content);
	/**
	 * 处理俱乐部信息
	 * @param content
	 */
	Result<Integer> operateClub(String content);
	/**
	 * 处理俱乐部玩家信息
	 * @param content
	 */
	Result<Integer> operateClubUser(String content);
	/**
	 * 处理玩家幸运值信息
	 * @param content
	 */
	Result<Integer> operateUserLuck(String content);
}
