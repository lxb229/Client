package com.wangzhixuan.mapper;

import com.wangzhixuan.model.CommodityList;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  * 库存商品列表 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-09
 */
public interface CommodityListMapper extends BaseMapper<CommodityList> {
	
	List<CommodityList> getListByWarehouseIn(@Param("warehouseIn")Integer warehouseIn);
	
	List<CommodityList> getListByCommodity(@Param("commodity")Integer commodity);
	
	List<CommodityList> getListByWarehouseOut(@Param("warehouseOut")Integer warehouseOut);
}