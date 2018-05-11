package com.guse.platform.service.doudou;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.doudou.Exchange;

/**
 * exchange
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface ExchangeService extends BaseService<Exchange,java.lang.Integer>{

	/**
	 * 从游戏服务器获取兑换比例集合
	 * @return
	 */
	public Result<PageResult<Exchange>> queryExchangeList(Exchange exchange, PageBean pageBean);
	
	/**
	 * 新增更新兑换比例
	 * @Title: saveOrUpdateExchange 
	 * @param @param product
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> updateExchange(Exchange exchange);
}
