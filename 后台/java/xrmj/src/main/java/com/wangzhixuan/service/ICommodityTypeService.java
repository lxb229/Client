package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.CommodityType;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 商品类型 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-31
 */
public interface ICommodityTypeService extends IService<CommodityType> {
	
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 删除商品类型(假删除，状态置为无效)
	 * @param id
	 * @return
	 */
	Result deleteCommodityType(Integer id);
	
	/**
	 * 获取所有的有效商品类型
	 * @return
	 */
	List<CommodityType> getAllTyep();
	
}
