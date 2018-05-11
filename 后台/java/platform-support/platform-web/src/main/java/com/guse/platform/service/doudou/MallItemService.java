package com.guse.platform.service.doudou;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.doudou.MallItem;

/**
 * mall_item
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface MallItemService extends BaseService<MallItem,java.lang.Integer>{

	/**
	 * 从游戏服务器获取商城配置集合
	 * @return
	 */
	public Result<PageResult<MallItem>> queryMallItemList(MallItem mallItem, PageBean pageBean);
	
	/**
	 * 新增更新商城配置
	 * @Title: saveOrUpdateExchange 
	 * @param @param product
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> setMallItem(MallItem mallItem);
}
