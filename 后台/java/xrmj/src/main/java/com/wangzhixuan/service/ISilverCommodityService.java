package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.model.SilverCommodity;
import com.wangzhixuan.model.vo.SilverCommodityVo;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 银币抽奖商品 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-08
 */
public interface ISilverCommodityService extends IService<SilverCommodity> {
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 根据商品id获取抽奖商品信息
	 * @param commodityId 商品id
	 * @return
	 */
	SilverCommodityVo getSilverCommodityBy(Integer commodityId);
}
