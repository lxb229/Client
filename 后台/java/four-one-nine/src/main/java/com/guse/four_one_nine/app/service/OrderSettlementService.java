package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.OrderSettlementDao;
import com.guse.four_one_nine.dao.model.OrderSettlement;

/** 
* @ClassName: OrderSettlementService
* @Description: 服务结算信息变更管理
* @author: wangkai
* @date: 2018年1月9日 下午3:17:45 
*  
*/
@Service
public class OrderSettlementService {

	
	@Autowired
	OrderSettlementDao orderSettlementDao;

	/**
	 * 新增服务结算信息
	 * 
	 * @param orderSettlement
	 */
	public void addOrderSettlement(OrderSettlement orderSettlement ) {
		orderSettlementDao.addOrderSettlement(orderSettlement);
	}
}
