package com.wangzhixuan.mapper;

import com.wangzhixuan.model.Supplier;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 供应商 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public interface SupplierMapper extends BaseMapper<Supplier> {
	
	List<Supplier> selectSupplierPage(Pagination page, Map<String, Object> params);
	
	List<Supplier> getAllSupplier();
}