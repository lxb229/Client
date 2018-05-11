package com.wangzhixuan.mapper;

import com.wangzhixuan.model.GoldCommodity;
import com.wangzhixuan.model.vo.GoldCommodityVo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 金币兑换商品 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-08
 */
public interface GoldCommodityMapper extends BaseMapper<GoldCommodity> {

	List<GoldCommodityVo> selectGoldCommodityVoPage(Pagination page, Map<String, Object> params);
	
	GoldCommodity getGoldCommodityBy(@Param("commodityId")Integer commodityId);
}