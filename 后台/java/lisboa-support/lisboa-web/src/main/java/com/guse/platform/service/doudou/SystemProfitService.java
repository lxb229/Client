package com.guse.platform.service.doudou;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.doudou.SystemProfit;

/**
 * system_profit
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface SystemProfitService extends BaseService<SystemProfit,java.lang.Integer>{

	/**
	 * 从游戏服务器获取用户集合
	 * @return
	 */
	public Result<PageResult<SystemProfit>> queryProfitList(SystemProfit profit, PageBean pageBean);
	
	/**
	 * 新增更新产品
	 * @Title: saveOrUpdateProduct 
	 * @param @param product
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> updateProfit(SystemProfit profit);
}
