package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.WarehouseIn;
import com.wangzhixuan.model.vo.WarehouseINVo;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 入库单 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public interface IWarehouseInService extends IService<WarehouseIn> {
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 新增一个入库单
	 * @param warehouseIn 入库单
	 * @return
	 */
	Result insertWarehouseIn(WarehouseINVo vo);
	
	/**
	 * 获取所有的正常入库的入库单
	 * @return
	 */
	List<WarehouseIn> getAllWarehouseIn(Integer supplier, Integer commodity);
}
