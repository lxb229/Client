package com.wangzhixuan.mapper;

import com.wangzhixuan.model.Commodity;
import com.wangzhixuan.model.vo.CommodityVo;
import com.wangzhixuan.model.vo.ExchangeDetailsVo;
import com.wangzhixuan.model.vo.GoldExchangeVo;
import com.wangzhixuan.model.vo.SilverCommodityVo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 商品 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public interface CommodityMapper extends BaseMapper<Commodity> {

	List<CommodityVo> selectCommodityVoPage(Pagination page, Map<String, Object> params);
	
	List<Commodity> getAllCommodity();
	
	List<String> getAllSilverCommodity();
	
	List<SilverCommodityVo> getSilverCommodityBy(@Param("lv")Integer lv);
	
	List<GoldExchangeVo> getAllGoldCommodity();
	
	ExchangeDetailsVo exchangeCommodity(@Param("itemId")Integer itemId);
}