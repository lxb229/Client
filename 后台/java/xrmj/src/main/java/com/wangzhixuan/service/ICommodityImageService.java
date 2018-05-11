package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.model.CommodityImage;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 商品图片 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-08
 */
public interface ICommodityImageService extends IService<CommodityImage> {
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	/**
	 * 获取商品icon图片集合
	 * @param commodity 商品
	 * @return
	 */
	List<CommodityImage> getIconList(Integer commodity);
	/**
	 * 获取商品详情图片集合
	 * @param commodity 商品
	 * @return
	 */
	List<CommodityImage> getDetailsList(Integer commodity);
}
