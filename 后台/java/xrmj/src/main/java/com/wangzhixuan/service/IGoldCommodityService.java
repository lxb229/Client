package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.GoldCommodity;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 金币兑换商品 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-08
 */
public interface IGoldCommodityService extends IService<GoldCommodity> {
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 处理增加兑换奖品业务
	 * @param goldCommodity
	 * @return
	 */
	Result insertGoldCommodity(GoldCommodity goldCommodity);
	
	/**
	 * 删除兑换商品
	 * @param goldCommodity 兑换商品
	 * @return
	 */
	Result deleteGoldCommodity(Integer goldCommodityId);
	
	/**
	 * 根据商品id获取兑换商品信息
	 * @param commodityId 商品id
	 * @return
	 */
	GoldCommodity getGoldCommodityBy(Integer commodityId);
	
	/**
	 * 更新兑换商品
	 * @param commodity 商品
	 * @param amount 兑换数量
	 * @return
	 */
	Result outGoldCommodity(Integer commodity, Integer amount);
}
