package com.guse.platform.service.doudou;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.doudou.SystemCash;

/**
 * system_cash
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface SystemCashService extends BaseService<SystemCash,java.lang.Integer>{

	
	/**
	 * 新增更新产品
	 * @Title: saveOrUpdateProduct 
	 * @param @param product
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> updateCash(SystemCash cash);
	
	/**
	 * 根据id获取提现对象
	 * @param scId
	 * @return
	 */
	SystemCash getCashById(Integer scId);
}
