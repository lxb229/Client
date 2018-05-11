package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.WarehouseOut;
import com.wangzhixuan.model.vo.ExchangeDetailsVo;
import com.wangzhixuan.model.vo.GoldExchangeVo;
import com.wangzhixuan.model.vo.LuckVo;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 出库单 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public interface IWarehouseOutService extends IService<WarehouseOut> {
	
	/**
	 * 抽奖-出库一个商品
	 * @param startNo
	 * @param lv
	 * @return
	 */
	LuckVo outputCommodity(String startNo, Integer lv, LuckVo luckVo);
	
	/**
	 * 玩家金币兑换
	 * @param cmd 命令码
	 * @param start_no 玩家明星号
	 * @param itemId 玩家兑换商品
	 * @return
	 */
	Object exchange(Integer cmd, String start_no, Integer itemId, String name, String phone, String addr);
	
	/**
	 * 获取所有可以兑换的商品
	 * @return
	 */
	List<GoldExchangeVo> getAllGold();
	
	/**
	 * 兑换商品详情
	 * @param itemId
	 * @return
	 */
	ExchangeDetailsVo detaiCommodity(Integer itemId);
	
	/**
	 * 兑换商品
	 * @param start_no
	 * @param itemId
	 * @param name
	 * @param phone
	 * @param addr
	 * @return
	 */
	LuckVo exchangeCommodity(String start_no ,Integer itemId, String name, String phone, String addr);
	/**
	 * 处理新增出库单对应业务
	 * @param warehouseOut
	 * @return
	 */
	Result insertWarehouseOut(WarehouseOut warehouseOut);
	/**
	 * 处理商品兑换出库单对应业务
	 * @param warehouseOut
	 * @return
	 */
	Result insertGoldOut(WarehouseOut warehouseOut);
	/**
	 * 银币抽奖生成出库单
	 * @param commodity
	 * @return
	 */
	WarehouseOut insertBySilver(Integer commodity);
	/**
	 * 金币兑换生成出库单
	 * @param commodity
	 * @return
	 */
	WarehouseOut insertByGold(Integer commodity);
	/**
	 * 获取所有的正常出库的出库单
	 * @return
	 */
	List<WarehouseOut> getAllWarehouseOut(Integer commodity);
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	/**
	 * 邮件模板转义用于APP展示
	 * @param emailContent 原本邮件模板
	 * @return
	 */
	String appEmailContent(String emailContent);
	
}
