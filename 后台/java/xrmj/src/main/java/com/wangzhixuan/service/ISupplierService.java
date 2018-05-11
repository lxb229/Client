package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.model.Supplier;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 供应商 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public interface ISupplierService extends IService<Supplier> {
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 获取所有的供应商
	 * @return
	 */
	List<Supplier> getAllSupplier();
}
