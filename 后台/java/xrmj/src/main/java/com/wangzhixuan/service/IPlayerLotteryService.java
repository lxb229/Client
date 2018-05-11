package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.PlayerLottery;
import com.wangzhixuan.model.vo.SilverCommodityVo;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public interface IPlayerLotteryService extends IService<PlayerLottery> {
	
	/**
	 * 获取玩家当前参加抽奖奖项
	 * @param startNo
	 * @return
	 */
	PlayerLottery getPlayerLotteryBy(String startNo);
	
	/**
	 * 设置玩家的奖项
	 * @param list
	 * @return
	 */
	String setLotteryJson(List<SilverCommodityVo> list);
	
	/**
	 * 增加一个玩家的抽奖项
	 * @return
	 */
	Result insertPlayerLottery(String startNo);
	
	/**
	 * 刷新一个玩家的奖项
	 * @param startNo
	 * @return
	 */
	Result refreshPlayerLottery(String startNo);
}
