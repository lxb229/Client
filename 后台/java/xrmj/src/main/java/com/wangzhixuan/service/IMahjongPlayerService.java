package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.MahjongPlayer;
import com.wangzhixuan.model.SystemTask;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 麻将馆玩家 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-17
 */
public interface IMahjongPlayerService extends IService<MahjongPlayer> {
	
	/**
	 * 处理任务中的麻将馆成员数据
	 * @param task
	 */
	void taskMahjongPlayer(SystemTask task);
	
	/**
	 * 处理麻将馆成员
	 * @param obj
	 * @return
	 */
	Result processingMahjongPlayer(JSONObject obj);
	
	/**
	 * 根据麻将馆明星号+玩家明星号获取麻将馆成员
	 * @param mahjongNo 麻将馆明星号
	 * @param startNo 玩家明星号
	 * @return
	 */
	MahjongPlayer selectMahjongPlayerBy(String mahjongNo, String startNo);
}
