package com.wangzhixuan.mapper;

import com.wangzhixuan.model.SilverCommodity;
import com.wangzhixuan.model.vo.SilverCommodityVo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 银币抽奖商品 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-08
 */
public interface SilverCommodityMapper extends BaseMapper<SilverCommodity> {
	
	List<SilverCommodityVo> selectSilverCommodityVoPage(Pagination page, Map<String, Object> params);
	
	SilverCommodityVo getSilverCommodityBy(@Param("commodityId")Integer commodityId);
}