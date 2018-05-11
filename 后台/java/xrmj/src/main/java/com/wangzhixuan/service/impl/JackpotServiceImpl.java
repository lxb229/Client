package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Investment;
import com.wangzhixuan.model.Jackpot;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.SystemOrder;
import com.wangzhixuan.model.vo.RedPacketVo;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.mapper.JackpotMapper;
import com.wangzhixuan.service.IJackpotService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 奖池 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
@Service
public class JackpotServiceImpl extends ServiceImpl<JackpotMapper, Jackpot> implements IJackpotService {

	@Autowired
	private PropertyConfigurer configurer;
	@Autowired
	private JackpotMapper jackpotMapper;
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<Jackpot> list = jackpotMapper.selectJackpotPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}
	
	@Override
	public Result supplementJackpot(SystemOrder order) {
		Jackpot jackpot = getJackpot();
		boolean success = false;
		if(order != null && order.getPayPrice() != null && order.getRoomcardAmount() != null
				&& order.getPayPrice().compareTo(new BigDecimal(0)) == 1 && order.getRoomcardAmount() > 0) {
			/**房卡数量*/
			BigDecimal cardAmount = new BigDecimal(order.getRoomcardAmount());
			/**房卡成本*/
			BigDecimal roomcardCost = new BigDecimal(configurer.getProperty("roomcardCost"));
			/**进入众筹奖池比例*/
			BigDecimal crowdfundingRate = new BigDecimal(configurer.getProperty("crowdfundingRate"));
			/**众筹奖池金额*/
			BigDecimal crowdfunding = jackpot.getCrowdfunding();
			
			crowdfunding = crowdfunding.add(cardAmount.multiply(roomcardCost).multiply(crowdfundingRate).setScale(4, BigDecimal.ROUND_HALF_DOWN));
			jackpot.setCrowdfunding(crowdfunding);
			success = this.updateById(jackpot);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "订单-注入众筹奖池失败");
		}
	}
	
	@Override
	public Result supplementJackpot(Investment investment) {
		Jackpot jackpot = getJackpot();
		boolean success = false;
		if(investment != null ) {
			BigDecimal amount = investment.getAmount();
			/**众筹奖池金额*/
			BigDecimal crowdfunding = jackpot.getCrowdfunding();
			jackpot.setCrowdfunding(crowdfunding.add(amount));
			/**已投资金额*/
			jackpot.setInvestmentAmount(jackpot.getInvestmentAmount().add(amount));
			success = this.updateById(jackpot);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "投资-注入众筹奖池失败");
		}
	}

	@Override
	public Result supplementJackpot(Integer roomcardAmount) {
		Jackpot jackpot = getJackpot();
		boolean success = false;
		if(roomcardAmount != null && roomcardAmount > 0) {
			BigDecimal amount = new BigDecimal(roomcardAmount);
			/**保底奖池金额*/
			BigDecimal minimum = jackpot.getMinimum();
			jackpot.setMinimum(minimum.add(amount));
			success = this.updateById(jackpot);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "房卡消耗-注入保底奖池失败");
		}
	}
	
	@Override
	public Result wishJoinParty() {
		Jackpot jackpot = getJackpot();
		jackpot.setGetNumber(jackpot.getGetNumber()+1);
		boolean success = this.updateById(jackpot);
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "参加牌局-增加领取条件总和失败");
		}
	}
	
	@Override
	public Result consumeJackpot(BigDecimal minimum, Integer receiveNumber, BigDecimal crowdfunding, RedPacketVo vo) {
		Jackpot jackpot = getJackpot();
		/**更新保底奖池和已发放的保底金额*/
		jackpot.setMinimum(jackpot.getMinimum().subtract(minimum).setScale(4, BigDecimal.ROUND_HALF_DOWN));
		jackpot.setSendoutMinimum(jackpot.getSendoutMinimum().add(minimum).setScale(4, BigDecimal.ROUND_HALF_DOWN));
		/**更新领取条件总和*/
		jackpot.setGetNumber(jackpot.getGetNumber()-receiveNumber);
		/**更新众筹奖池和已发放的众筹金额*/
		jackpot.setCrowdfunding(jackpot.getCrowdfunding().subtract(crowdfunding).setScale(4, BigDecimal.ROUND_HALF_DOWN));
		jackpot.setSendoutCrowdfunding(jackpot.getSendoutCrowdfunding().add(crowdfunding).setScale(4, BigDecimal.ROUND_HALF_DOWN));
		/**更新已发放的金币和银币总和*/
		switch (vo.getMoneyType()) {
			case 2:
				jackpot.setSendoutGold(jackpot.getSendoutGold()+vo.getAmount());
				break;
			case 3:
				jackpot.setSendoutSilver(jackpot.getSendoutSilver()+vo.getAmount());
				break;
			default:
				break;
		}
		boolean success = this.updateById(jackpot);
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "更新奖池失败");
		}
	}
	
	@Override
	public Jackpot getJackpot() {
		return this.selectById(1);
	}

}
