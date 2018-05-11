package com.guse.chessgame.service;

import org.chessgame.dao.bean.Players;

public interface PlayerService {
	
	/**
	 * 保存玩家
	 * @param palyer 玩家对象
	 * @return 玩家在数据库中的ObjectId
	 */
	public String savePalyer(Players palyer);
	
	/**
	 * 修改玩家信息
	 * @param player 玩家最新信息对象
	 * @return 修改数据条数
	 */
	public int updatePlayer(Players player);
	
	/**
	 * 根据用户uid查询玩家对象
	 * @param uid 玩家唯一标识
	 * @return uid对应的玩家对象
	 */
	public Players seachPalyer(String uid);
	
	/**
	 * 获取玩家最新编号
	 * @return 玩家编号
	 */
	public int getPlayerSequence();

}
