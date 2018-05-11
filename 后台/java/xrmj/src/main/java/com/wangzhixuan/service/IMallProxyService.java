package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.MallProxy;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 商城代理微信 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-19
 */
public interface IMallProxyService extends IService<MallProxy> {
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 从游戏服务器获取商城代理集合
	 * @return
	 */
	List<MallProxy> getMallProxyList();
	
	/**
	 * 新增更新商城代理
	 * @Title: saveOrUpdateExchange 
	 * @param @param product
	 * @param type 1:新增 2：编辑 3：删除
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result setMallProxy(MallProxy mallProxy, Integer type) throws UnsupportedEncodingException;
	
	/**
	 * 添加商城代理
	 * @param mallProxy
	 * @return
	 */
	Result addMallProxy(MallProxy mallProxy);
	
	/**
	 * 编辑商城代理
	 * @param mallProxy
	 * @return
	 */
	Result updateMallProxy(MallProxy mallProxy);
	
	/**
	 * 删除商城代理
	 * @param mallProxy
	 * @return
	 */
	Result deleteMallProxy(MallProxy mallProxy);
}
