package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.DayDealCountDao;
import com.guse.four_one_nine.dao.model.CashApply;
import com.guse.four_one_nine.dao.model.DayDealCount;
import com.guse.four_one_nine.dao.model.Server;
import com.guse.four_one_nine.dao.model.ServerOrder;

/** 
* @ClassName: DayDealCountService 
* @Description: 交易信息统计
* @author Fily GUSE
* @date 2018年1月4日 下午6:03:43 
*  
*/
@Service
public class DayDealCountService {
	
	@Autowired
	DayDealCountDao dao;
	
	/** 
	* @Title: countOrder 
	* @Description: 订单信息统计 
	* @param @param order
	* @return void 
	* @throws 
	*/
	public void countOrder(ServerOrder order) {
		DayDealCount count = findToDay();
		// 平台流水
		count.setPlatform_statements_count(count.getPlatform_statements_count() + order.getTotal());
		// 成交量
		count.setOrder_num(count.getOrder_num() + 1);
		// 交易金额
		count.setOrder_count(count.getOrder_count() + order.getTotal());
		// 单笔最大金额
		if(order.getTotal() > count.getOrder_max()) {
			count.setOrder_max(order.getTotal());
		}
		dao.save(count);
	}
	
	/** 
	* @Title: serverCount 
	* @Description: 新增服务统计 
	* @param @param server
	* @return void 
	* @throws 
	*/
	public void serverCount(Server server) {
		DayDealCount count = findToDay();
		count.setNewly_server_count(count.getNewly_server_count() + 1);
		dao.save(count);
	}
	
	/** 
	* @Title: cashCount 
	* @Description: 提现统计 
	* @param @param cash
	* @return void 
	* @throws 
	*/
	public void cashCount(CashApply cash) {
		DayDealCount count = findToDay();
		// 提现次数
		count.setCash_num(count.getCash_num() + 1);
		// 提现总数
		count.setCash_count(count.getCash_count() + cash.getMoney());
		// 最大提现金额
		if(cash.getMoney() > count.getCash_max()) {
			count.setCash_max(cash.getMoney());
		}
		dao.save(count);
	}
	
	/** 
	* @Title: remitCount 
	* @Description: 打款统计 
	* @param @param cash
	* @return void 
	* @throws 
	*/
	public void remitCount(CashApply cash) {
		if(cash.getRemit_time() != null) {
			DayDealCount count = findToDay();
			// 打款次数
			count.setRemit_num(count.getRemit_num() + 1);
			// 打款统计
			count.setRemit_count(count.getRemit_count() + cash.getMoney());
			// 最大打款金额
			if(cash.getMoney() > count.getRemit_max()) {
				count.setRemit_max(cash.getMoney());
			}
			dao.save(count);
		}
	}

	
	/* 获取当前统计信息 */
	private synchronized DayDealCount findToDay() {
		// 获取当天统计信息
		DayDealCount count = dao.getToDay();
		if(count == null) {
			// 创建当天统计信息
			dao.addToDay();
			count = dao.getToDay();
		}
		return count; 
	}
}
