package com.wangzhixuan.mapper;

import com.wangzhixuan.model.CommodityLv;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 商品等级 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public interface CommodityLvMapper extends BaseMapper<CommodityLv> {

	List<CommodityLv> selectCommodityLvPage(Pagination page, Map<String, Object> params);
	
	List<CommodityLv> getAllLv();
}