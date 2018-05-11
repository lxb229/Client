package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.SystemTask;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 玩家表 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
public interface IPlayerService extends IService<Player> {
	
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 玩家的禁/启用
	 * @param id 玩家数据库主键id
	 * @param status 状态
	 * @return
	 */
	Result banOrEnable(Integer id, Integer status);
	
	/**
	 * 修改玩家电话
	 * @param player 玩家对象
	 * @return
	 */
	Result updatePlayer(Player player);
	/**
	 * 处理任务中的玩家数据
	 * @param task
	 */
	void taskPlayer(SystemTask task);
	
	/**
	 * 处理玩家
	 * @param obj 玩家对象
	 * @return
	 */
	Result processingPlayer(JSONObject obj);
	
	/**
	 * 根据玩家id获取玩家
	 * @param playerId 玩家id
	 * @return
	 */
	Player selectPlayerBy(String start_no);
	
	/**
	 * 新增或者更新玩家
	 * @param player
	 * @return
	 */
	Result saveOrUpdatePlayer(Player player);
	
	/**
	 * 新增一个玩家
	 * @param player
	 * @return
	 */
	Result insertPlayer(Player player);
}
