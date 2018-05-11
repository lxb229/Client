package com.guse.platform.service.doudou;

import com.alibaba.fastjson.JSONObject;
import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.doudou.ClubUser;
import com.guse.platform.entity.doudou.SystemTask;

/**
 * club_user
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface ClubUserService extends BaseService<ClubUser,java.lang.Integer>{

	/**
	 * 新增编辑俱乐部玩家
	* @Title: saveOrUpdateClub 
	* @param @param club
	* @param @return 
	* @return Result<Integer>  
	 */
	Result<Integer> saveOrUpdateClubUser(ClubUser clubUser); 
	/**
	 * 获取俱乐部玩家玩家信息
	 * @param clubId 
	 * @param userId
	 * @return
	 */
	ClubUser getClubUserBy(Integer clubId, Integer userId);
	/**
	 * 删除俱乐部玩家玩家
	 * @param cuId
	 * @return
	 */
	Result<Integer>  deleteClubUser(Integer cuId); 
	
	/**
	 * 处理任务中的俱乐部玩家数据
	 * @param task
	 */
	public void taskClubUsers(SystemTask task);
	
	/**
	 * 处理mq中的俱乐部玩家信息
	 * @param obj
	 * @return
	 */
	public Result<Integer> processingClubUsers(JSONObject obj);
}
