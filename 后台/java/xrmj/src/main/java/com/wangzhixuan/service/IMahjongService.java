package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.Mahjong;
import com.wangzhixuan.model.SystemTask;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 麻将馆 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-17
 */
public interface IMahjongService extends IService<Mahjong> {
	/**
	 * 处理任务中的麻将馆数据
	 * @param task
	 */
	void taskMahjong(SystemTask task);
	
	/**
	 * 处理任务中的麻将馆房卡数据
	 * @param task
	 */
	void taskMahjongCard(SystemTask task);
	
	/**
	 * 处理任务中的麻将馆战斗数据
	 * @param task
	 */
	void taskMahjongCombat(SystemTask task);
	
	/**
	 * 处理麻将馆
	 * @param obj
	 * @return
	 */
	Result processingMahjong(JSONObject obj);
	
	/**
	 * 处理麻将馆房卡
	 * @param obj
	 * @return
	 */
	Result processingMahjongCard(JSONObject obj);
	
	/**
	 * 处理麻将馆战斗
	 * @param obj
	 * @return
	 */
	Result processingMahjongCombat(JSONObject obj);
	
	/**
	 * 新增或者更新麻将馆
	 * @param player
	 * @return
	 */
	Result saveOrUpdateMahjong(Mahjong mahjong);
	
	/**
	 * 增加一个麻将馆
	 * @param mahjong
	 * @return
	 */
	Result insertMahjong(Mahjong mahjong);
	
	/**
	 * 根据麻将馆编号获取麻将馆
	 * @param mahjongNo 麻将馆编号
	 * @return
	 */
	Mahjong selectMahjongBy(String mahjongNo);
}
