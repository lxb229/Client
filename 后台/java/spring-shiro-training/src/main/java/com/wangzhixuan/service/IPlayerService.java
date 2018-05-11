package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.SystemTask;

import java.util.Map;

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
	 * 处理任务中的玩家数据
	 * @param task
	 */
	void taskPlayer(SystemTask task);
	/**
	 * 启/禁用玩家
	 * @param player 玩家对象
	 * @return
	 */
	Result deletePlayer(Player player);
	/**
	 * 根据玩家id获取玩家
	 * @param playerId 玩家id
	 * @return
	 */
	Player selectPlayerBy(String playerId);
	
	Map<String, Object> selectPlayer();
	
}
