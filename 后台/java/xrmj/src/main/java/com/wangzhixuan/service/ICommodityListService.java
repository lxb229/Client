package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.CommodityList;
import com.wangzhixuan.model.WarehouseIn;
import com.wangzhixuan.model.WarehouseOut;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 库存商品列表 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-09
 */
public interface ICommodityListService extends IService<CommodityList> {
	
	/**
	 * 根据入库单、卡号、密钥；生成对应的商品库存列表
	 * @param warehouseIn 入库单
	 * @param cardNo 考号
	 * @param secretKey 密钥
	 * @return
	 */
	Result insertCommodityList(WarehouseIn warehouseIn, String cardNo, String secretKey);
	
	/**
	 * 根据入库单删除商品库存列表
	 * @param warehouseIn 入库单
	 * @return
	 */
	Result deleteCommodityList(WarehouseIn warehouseIn);
	/**
	 * 根据出库单更新商品库存列表
	 * @param warehouseOut 出库单
	 * @return
	 */
	Result updateByWarehouseOut(WarehouseOut warehouseOut);
	/**
	 * 获取入库单对应的商品库存列表
	 * @param warehouseIn 入库id
	 * @return
	 */
	List<CommodityList> getListByWarehouseIn(Integer warehouseIn);
	/**
	 * 根据商品获取商品的可用商品库存列表
	 * @param commodity 商品id
	 * @return
	 */
	List<CommodityList> getListByCommodity(Integer commodity);
	/**
	 * 根据出库获取对应的商品库存列表
	 * @param warehouseOut 出库id
	 * @return
	 */
	List<CommodityList> getListByWarehouseOut(Integer warehouseOut);
	
	/**
	 * 将有效商品库存列表中的指定数量设置为无效
	 * @param list 有效商品库存列表
	 * @param amount 无效个数
	 * @return
	 */
	Result InvalidCommodityList(List<CommodityList> list, Integer amount);
	/**
	 * 根据出库单将有效商品库存列表中的指定数量设置为出库
	 * @param out 出库单
	 * @return
	 */
	Result outCommodityList(WarehouseOut out);
	/**
	 * 将有效商品库存列表中的指定数量设置为冻结待出库
	 * @param list 有效商品库存列表
	 * @param amount 冻结数量
	 * @return
	 */
	Result blockCommodityList(List<CommodityList> list, Integer amount);
	
}
