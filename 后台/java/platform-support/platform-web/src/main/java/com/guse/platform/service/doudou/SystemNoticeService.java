package com.guse.platform.service.doudou;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.doudou.SystemNotice;

/**
 * system_notice
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface SystemNoticeService extends BaseService<SystemNotice,java.lang.Integer>{
	
	/**
	 * 新增更新产品
	 * @Title: saveOrUpdateProduct 
	 * @param @param product
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> saveOrUpdateProduct(SystemNotice notice);
	
	/**
	 * 删除公告
	 * @Title: deleteProduct 
	 * @param @param spId
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer>  deleteNotice(SystemNotice notice);
	
}
