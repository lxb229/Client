package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.CommodityLv;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 商品等级 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public interface ICommodityLvService extends IService<CommodityLv> {
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 删除商品等级(假删除，状态置为无效)
	 * @param id
	 * @return
	 */
	Result deleteCommodityLv(Integer id);
	
	/**
	 * 获取所有的有效商品等级
	 * @return
	 */
	List<CommodityLv> getAllLv();
	
	/**
	 * 获取指定等级的商品等级
	 * @param lv
	 * @return
	 */
	CommodityLv getCommodityLvBy(Integer lv);
}
