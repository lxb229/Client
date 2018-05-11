package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.PlayerLuck;
import com.wangzhixuan.model.SystemTask;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 玩家幸运值 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-18
 */
public interface IPlayerLuckService extends IService<PlayerLuck> {
	
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 获取玩家对应的幸运值
	 * @param startNo
	 * @return
	 */
	PlayerLuck selectPlayerLuckBy(String startNo);
	
	/**
	 * 增加一个玩家幸运值
	 * @param playerLuck
	 * @return
	 */
	Result insertPlayerLuck(PlayerLuck playerLuck);
	
	/**
	 * 编辑一个玩家幸运值
	 * @param playerLuck
	 * @return
	 */
	Result updatePlayerLuck(PlayerLuck playerLuck);
	
	/**
	 * 将一个玩家的幸运值置为无效
	 * @param id
	 * @return
	 */
	Result deletePlayerLuck(Integer id);
	
	/**
	 * 处理任务中的消耗幸运值数据
	 * @param task
	 */
	void taskUseLuck(SystemTask task);
	
	/**
	 * 消耗幸运值业务处理
	 * @param obj
	 * @return
	 */
	Result processingPlayerLuck(JSONObject obj);
}
