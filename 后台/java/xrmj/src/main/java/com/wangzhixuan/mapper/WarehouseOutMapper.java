package com.wangzhixuan.mapper;

import com.wangzhixuan.model.WarehouseOut;
import com.wangzhixuan.model.vo.WarehouseOutVo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 出库单 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public interface WarehouseOutMapper extends BaseMapper<WarehouseOut> {
	
	List<WarehouseOutVo> selectWarehouseOutVoPage(Pagination page, Map<String, Object> params);
	
	List<WarehouseOut> getAllWarehouseOut(@Param("commodity")Integer commodity);
}