package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.Commodity;
import com.wangzhixuan.model.vo.ExchangeDetailsVo;
import com.wangzhixuan.model.vo.GoldExchangeVo;
import com.wangzhixuan.model.vo.SilverCommodityVo;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 商品 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public interface ICommodityService extends IService<Commodity> {
	
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 新增一个商品
	 * @return
	 */
	Result insertCommodity(Commodity commodity);
	
	/**
	 * 生成随机奖池
	 * @return
	 */
	List<SilverCommodityVo> getRandomJackpot();
	
	/**
	 * 获取指定商品等级的一件随机商品
	 * @param lv 指定商品等级
	 * @return
	 */
	SilverCommodityVo randomJackpotLv(Integer lv);
	
	/**
	 * 获取所有的商品
	 * @return
	 */
	List<Commodity> getAllCommodity();
	
	/**
	 * 获取所有可银币抽奖的商品
	 * @return
	 */
	List<String> getAllSilverCommodity();
	
	/**
	 * 获取指定商品等级的可以银币抽奖的商品
	 * @param lv 商品等级
	 * @return
	 */
	List<SilverCommodityVo> getSilverCommodityBy(Integer lv);
	
	/**
	 * 获取所有的可金币兑换的商品
	 * @return
	 */
	List<GoldExchangeVo> getAllGoldCommodity();
	
	/**
	 * 获取商品详情
	 * @param itemId
	 * @return
	 */
	ExchangeDetailsVo exchangeCommodity(Integer itemId);
	
}
