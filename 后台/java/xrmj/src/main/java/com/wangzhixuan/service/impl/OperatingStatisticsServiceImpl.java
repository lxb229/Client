package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Investment;
import com.wangzhixuan.model.OperatingStatistics;
import com.wangzhixuan.model.SystemOrder;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.mapper.OperatingStatisticsMapper;
import com.wangzhixuan.service.IOperatingStatisticsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 运营统计 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
@Service
public class OperatingStatisticsServiceImpl extends ServiceImpl<OperatingStatisticsMapper, OperatingStatistics> implements IOperatingStatisticsService {

	@Autowired
	private PropertyConfigurer configurer;
	
	@Override
	public Result supplementOperating(SystemOrder order) {
		OperatingStatistics operating = getOperating();
		boolean success = false;
		if(order != null && order.getPayPrice() != null && order.getRoomcardAmount() != null
				&& order.getPayPrice().compareTo(new BigDecimal(0)) == 1 && order.getRoomcardAmount() > 0) {
			/**房卡单价*/
			BigDecimal roomcardPrice = order.getRoomcardPrice();
			/**房卡数量*/
			BigDecimal cardAmount = new BigDecimal(order.getRoomcardAmount());
			operating.setSalesRoomcard(operating.getSalesRoomcard()+order.getRoomcardAmount());
			/**房卡成本*/
			BigDecimal roomcardCost = new BigDecimal(configurer.getProperty("roomcardCost"));
			/**房卡利润*/
			BigDecimal roomcardProfit = operating.getRoomcardProfit();
			/**房卡毛利润*/
			BigDecimal roomcardNetProfit = operating.getRoomcardNetProfit();
			/**该笔订单房卡利润*/
			BigDecimal profit = cardAmount.multiply(roomcardPrice.subtract(roomcardCost)).setScale(4, BigDecimal.ROUND_HALF_DOWN);
			
			operating.setRoomcardProfit(roomcardProfit.add(profit));
			operating.setRoomcardNetProfit(roomcardNetProfit.add(profit));
			success = this.updateById(operating);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "注入众筹奖池失败");
		}
	}
	
	@Override
	public Result operatingByGame(Integer amount) {
		OperatingStatistics operating = getOperating();
		boolean success = false;
		if(amount != null && amount > 0) {
			operating.setUseRoomcard(operating.getUseRoomcard()+amount);
			success = this.updateById(operating);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "房卡消耗-更新运营统计失败");
		}
	}

	@Override
	public Result supplementOperating(Investment investment) {
		OperatingStatistics operating = getOperating();
		boolean success = false;
		if(investment != null) {
			operating.setRoomcardNetProfit(operating.getRoomcardNetProfit().add(investment.getAmount()));
			success = this.updateById(operating);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "投资-更新运营统计失败");
		}
	}
	
	@Override
	public OperatingStatistics getOperating() {
		return this.selectById(1);
	}


	
}
