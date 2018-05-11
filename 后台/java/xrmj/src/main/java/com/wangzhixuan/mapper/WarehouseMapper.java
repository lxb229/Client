package com.wangzhixuan.mapper;

import com.wangzhixuan.model.Warehouse;
import com.wangzhixuan.model.vo.WarehouseVo;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 商品仓库 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public interface WarehouseMapper extends BaseMapper<Warehouse> {
	
	List<WarehouseVo> selectWarehouseVoPage(Pagination page, Map<String, Object> params);
}