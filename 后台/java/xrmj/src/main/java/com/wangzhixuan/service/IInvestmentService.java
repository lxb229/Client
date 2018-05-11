package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.Investment;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 投资奖池 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public interface IInvestmentService extends IService<Investment> {
	
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 新增一笔投资
	 * @param player
	 * @return
	 */
	Result insertInvestment(Investment investment);
}
