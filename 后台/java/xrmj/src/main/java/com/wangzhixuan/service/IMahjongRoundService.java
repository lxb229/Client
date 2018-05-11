package com.wangzhixuan.service;

import com.wangzhixuan.model.MahjongRound;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 麻将馆轮数 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-17
 */
public interface IMahjongRoundService extends IService<MahjongRound> {
	
	/**
	 * 根据麻将馆编号获取麻将馆轮数
	 * @param mahjongNo 麻将馆编号
	 * @param gameType 玩法类型
	 * @return
	 */
	MahjongRound selectMahjongRoundBy(String mahjongNo, Integer gameType);
}
