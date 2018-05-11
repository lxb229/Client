package com.wangzhixuan.mapper;

import com.wangzhixuan.model.GoldLog;
import com.wangzhixuan.model.vo.GoldLogVo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 金币兑换商品记录 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-11
 */
public interface GoldLogMapper extends BaseMapper<GoldLog> {
	
	List<GoldLogVo> selectGoldLogVoPage(Pagination page, Map<String, Object> params);
	
	GoldLogVo selectGoldLogVoBy(@Param("id")Integer id);

}