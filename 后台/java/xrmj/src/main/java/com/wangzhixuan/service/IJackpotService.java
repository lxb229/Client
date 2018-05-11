package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.Investment;
import com.wangzhixuan.model.Jackpot;
import com.wangzhixuan.model.SystemOrder;
import com.wangzhixuan.model.vo.RedPacketVo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 奖池 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public interface IJackpotService extends IService<Jackpot> {
	
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 每一笔订单都需要补充到奖池
	 * @param order
	 * @return
	 */
	Result supplementJackpot(SystemOrder order); 
	
	/**
	 * 每一笔投资都需要补充到奖池
	 * @param order
	 * @return
	 */
	Result supplementJackpot(Investment investment); 
	
	/**
	 * 每一笔房卡消耗都需要补充到奖池
	 * @param amount 房卡数量
	 * @return
	 */
	Result supplementJackpot(Integer roomcardAmount);
	
	/**
	 * 更新奖池
	 * @param minimum 抽奖-保底金额
	 * @param receiveNumber 抽奖次数
	 * @param crowdfunding 抽奖-众筹金额
	 * @param vo 红包领取的货币，计入奖池统计
	 * @return
	 */
	Result consumeJackpot(BigDecimal minimum, Integer receiveNumber, BigDecimal crowdfunding, RedPacketVo vo);
	
	/**
	 * 每一次有效的牌局数，都需要增加奖池的领取条件总和
	 * @return
	 */
	Result wishJoinParty();
	
	/**
	 * 获取奖池
	 * @return
	 */
	Jackpot getJackpot();
	
}
