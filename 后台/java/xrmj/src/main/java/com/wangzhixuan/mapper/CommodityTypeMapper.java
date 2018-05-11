package com.wangzhixuan.mapper;

import com.wangzhixuan.model.CommodityType;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 商品类型 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-31
 */
public interface CommodityTypeMapper extends BaseMapper<CommodityType> {

	List<CommodityType> selectCommodityTypePage(Pagination page, Map<String, Object> params);
	
	List<CommodityType> getAllTyep();
}