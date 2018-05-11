package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.PlayerMoney;
import com.wangzhixuan.model.SystemOrder;
import com.wangzhixuan.model.vo.RedPacketVo;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 玩家账号余额 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public interface IPlayerMoneyService extends IService<PlayerMoney> {
	
	/**
	 * 新增玩家货币
	 * @param money 玩家货币对象
	 * @return
	 */
	Result insertPlayerMoney(String startNo);
	/**
	 * 更新玩家货币
	 * @param money 玩家货币对象
	 * @return
	 */
	Result updatePlayerMoney(PlayerMoney money);
	
	/**
	 * 根据购买订单更新玩家货币
	 * @param order 购买订单
	 * @return
	 */
	Result updatePlayerMoney(SystemOrder order);
	
	/**
	 * 根据玩家领取红包更新玩家货币
	 * @param order 购买订单
	 * @return
	 */
	Result updatePlayerMoney(RedPacketVo vo);
	
	/**
	 * 根据玩家游戏消耗更新玩家货币
	 * @param startNo 玩家明星号
	 * @param amount 消耗数量
	 * @param moneyType 消耗货币类型
	 * @return
	 */
	Result playerMoneyByGame(String startNo, Integer amount, Integer moneyType);
	/**
	 * 根据玩家明星号获取玩家货币
	 * @param startNo
	 * @return
	 */
	PlayerMoney getPlayerMoneyBy(String startNo);
	/**
	 * 玩家银币消耗
	 * @param startNo 玩家明星号
	 * @param amount 消耗数量
	 * @param type 消耗类型 1=刷新抽奖奖品 2=银币抽奖
	 * @return
	 */
	Result playerUseSilver(String startNo, Integer amount);
	/**
	 * 玩家金币消耗
	 * @param startNo 玩家明星号
	 * @param amount 消耗数量
	 * @return
	 */
	Result playerUserGold(String startNo, Integer amount);
}
