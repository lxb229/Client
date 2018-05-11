package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.Commodity;
import com.wangzhixuan.model.Warehouse;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 商品仓库 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public interface IWarehouseService extends IService<Warehouse> {
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 初始化一个商品库存
	 * @return
	 */
	Result insertWarehouse(Commodity commodity);
	
	/**
	 * 获取商品对应库存
	 * @param commodity 商品id
	 * @return
	 */
	Warehouse getWarehouseBy(Integer commodity);
	
	/**
	 * 入库-增加或者减少商品可用库存和总库存
	 * @param commodity 商品id
	 * @param amount 更新数量
	 * @return
	 */
	Result addWarehouse(Integer commodity, Integer amount);
	
	/**
	 * 冻结库存，减少可用库存
	 * @param commodity 商品id
	 * @param amount 冻结数量
	 * @return
	 */
	Result blockWarehouse(Integer commodity, Integer amount);
	/**
	 * 出库-增加或者减少可用库存、总数量
	 * @param commodity 商品
	 * @param amount 增减数量
	 * @return
	 */
	Result outWarehouse(Integer commodity, Integer amount);
	/**
	 * 兑换商品出库
	 * @param commodity 商品
	 * @param amount 兑换数量
	 * @return
	 */
	Result outGoldWarehouse(Integer commodity, Integer amount);
	
}
