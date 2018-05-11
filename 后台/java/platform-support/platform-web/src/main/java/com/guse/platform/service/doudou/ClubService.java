package com.guse.platform.service.doudou;

import com.alibaba.fastjson.JSONObject;
import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.doudou.Club;
import com.guse.platform.entity.doudou.SystemTask;
import com.guse.platform.entity.system.Users;

/**
 * club
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface ClubService extends BaseService<Club,java.lang.Integer>{

	/**
	 * 分页获取俱乐部列表
	* @Title: queryPageListByClub 
	* @param @return 
	* @return Result<PageResult<Club>>  
	 */
	public Result<PageResult<Club>> queryPageListByClub(Club club,PageBean pageBean, Users user);
	
	/**
	 * 启用禁用俱乐部
	 * @Title: enableDisableClub 
	 * @param @param club
	 * @param @return 
	 * @return Result<Integer>
	 */
	public Result<Integer> enableDisableClub(Club club);
	
	/**
	 * 处理游戏服务器上的俱乐部
	 * @param club
	 * @return
	 */
	public Result<Integer> manageGameClub(Club club);
	
	/**
	 * 根据编号获取俱乐部
	 * @param accountId
	 * @return
	 */
	public Club getClubByAccount(String accountId);
	
	/**
	 * 新增编辑俱乐部
	* @Title: saveOrUpdateClub 
	* @param @param club
	* @param @return 
	* @return Result<Integer>  
	 */
	public Result<Integer> saveOrUpdateClub(Club club); 
	/**
	 * 获取俱乐部下一个主键id
	 * @return
	 */
	public Integer getNextClubId();
	
	/**
	 * 取消群主标志
	 * @param club
	 * @return
	 */
	public Result<Integer> gmDeleteClub(Club club);
	
	/**
	 * 处理任务中的俱乐部数据
	 * @param task
	 */
	public void taskClubs(SystemTask task);
	
	/**
	 * 处理mq中的俱乐部信息
	 * @param obj
	 * @return
	 */
	public Result<Integer> processingClubs(JSONObject obj);
}
