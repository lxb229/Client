package com.wangzhixuan.mapper;

import com.wangzhixuan.model.CommodityImage;
import com.wangzhixuan.model.vo.CommodityImageVo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 商品图片 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-08
 */
public interface CommodityImageMapper extends BaseMapper<CommodityImage> {
	
	List<CommodityImageVo> selectCommodityImageVoPage(Pagination page, Map<String, Object> params);
	
	List<CommodityImage> getIconList(@Param("commodity")Integer commodity);
	
	List<CommodityImage> getDetailsList(@Param("commodity")Integer commodity);
}