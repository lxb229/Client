package com.guse.platform.service.doudou;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.doudou.MallProxy;

/**
 * mall_proxy
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface MallProxyService extends BaseService<MallProxy,java.lang.Integer>{
	
	/**
	 * 从游戏服务器获取商城代理集合
	 * @return
	 */
	public List<MallProxy> getMallProxyList();

	/**
	 * 从游戏服务器获取商城代理集合
	 * @return
	 */
	public Result<PageResult<MallProxy>> queryMallProxyList(MallProxy mallProxy, PageBean pageBean);
	
	/**
	 * 新增更新商城代理
	 * @Title: saveOrUpdateExchange 
	 * @param @param product
	 * @param type 1:新增 2：编辑 3：删除
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> setMallProxy(MallProxy mallProxy, int type) throws UnsupportedEncodingException;
	
	/**
	 * 新增更新商城代理
	 * @Title: saveOrUpdateProxy 
	 * @param @param mallProxy
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> saveOrUpdateProxy(MallProxy mallProxy);
	
	/**
	 * 删除产品
	 * @Title: deleteProduct 
	 * @param @param spId
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer>  deleteProxy(MallProxy mallProxy);
}
