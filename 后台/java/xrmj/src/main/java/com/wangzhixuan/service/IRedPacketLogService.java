package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.model.PlayerWish;
import com.wangzhixuan.model.RedPacketLog;
import com.wangzhixuan.model.vo.RedPacketVo;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 红包抽奖记录 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-27
 */
public interface IRedPacketLogService extends IService<RedPacketLog> {
	
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 领取红包-未生成的红包
	 * @param startNo 玩家明星号
	 * @param redPacketNo 红包号数
	 * @param round 轮数
	 * @return 领取的红包
	 */
	RedPacketVo receiveRedPacket(String startNo, int redPacketNo, Integer round, Integer receiveNumber);
	
	/**
	 * 领取红包-已生成的红包
	 * @param startNo 玩家明星号
	 * @param redPacketNo 红包号数
	 * @param round 轮数
	 * @return 领取的红包
	 */
	RedPacketVo alreadyRedPacket(String startNo, int redPacketNo, Integer round, Integer receiveNumber);
	
	/**
	 * 保底奖池领取金额
	 * @param receiveNumber 抽奖次数
	 * @return
	 */
	BigDecimal getMinimum(int receiveNumber);
	
	/**
	 * 众筹奖池领取金额
	 * @param wish 玩家当时的祝福值
	 * @param receiveNumber 抽奖次数
	 * @return
	 */
	BigDecimal getCrowdfunding(PlayerWish wish, int receiveNumber);
	
	/**
	 * 获取玩家指定轮数的红包
	 * @param startNo 玩家明星号
	 * @param round 轮数
	 * @return
	 */
	List<RedPacketLog> getRedByRound(String startNo, Integer round);

}
