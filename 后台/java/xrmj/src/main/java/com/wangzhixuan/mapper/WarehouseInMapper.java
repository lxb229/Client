package com.wangzhixuan.mapper;

import com.wangzhixuan.model.WarehouseIn;
import com.wangzhixuan.model.vo.WarehouseINVo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 入库单 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public interface WarehouseInMapper extends BaseMapper<WarehouseIn> {
	
	List<WarehouseINVo> selectWarehouseInVoPage(Pagination page, Map<String, Object> params);
	
	List<WarehouseIn> getAllWarehouseIn(@Param("supplier")Integer supplier, @Param("commodity")Integer commodity);
}