package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Commodity;
import com.wangzhixuan.model.CommodityLv;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.vo.CommodityVo;
import com.wangzhixuan.model.vo.ExchangeDetailsVo;
import com.wangzhixuan.model.vo.GoldExchangeVo;
import com.wangzhixuan.model.vo.SilverCommodityVo;
import com.wangzhixuan.commons.base.Constant;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.mapper.CommodityMapper;
import com.wangzhixuan.service.ICommodityLvService;
import com.wangzhixuan.service.ICommodityService;
import com.wangzhixuan.service.ISystemOrderService;
import com.wangzhixuan.service.IWarehouseService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements ICommodityService {

	@Autowired
	private CommodityMapper commodityMapper;
	@Autowired
	private ISystemOrderService orderService;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private ICommodityLvService commodityLvService;
	
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<CommodityVo> list = commodityMapper.selectCommodityVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}
	@Override
	public Result insertCommodity(Commodity commodity) {
		Result result = null;
		if(commodity != null) {
			/**生成商品编号*/
			String commodityNo = orderService.randomOrder(Constant.RANDOM_COMMODITY);
			commodity.setCommodityNo(commodityNo);
			boolean success = this.insert(commodity);
			if(success) {
				/**初始化商品库存*/
				result = warehouseService.insertWarehouse(commodity);
			}
		}
		return result;
	}
	@Override
	public List<Commodity> getAllCommodity() {
		return commodityMapper.getAllCommodity();
	}
	@Override
	public List<String> getAllSilverCommodity() {
		return commodityMapper.getAllSilverCommodity();
	}
	@Override
	public List<SilverCommodityVo> getSilverCommodityBy(Integer lv) {
		return commodityMapper.getSilverCommodityBy(lv);
	}
	@Override
	public List<GoldExchangeVo> getAllGoldCommodity() {
		return commodityMapper.getAllGoldCommodity();
	}
	
	
	@Override
	public List<SilverCommodityVo> getRandomJackpot() {
		List<SilverCommodityVo> list  = new ArrayList<>();
		/**获取一个一等奖*/
		CommodityLv lv = commodityLvService.getCommodityLvBy(1);
		SilverCommodityVo commodity = randomJackpotLv(lv.getLv());
		list.add(commodity);
		/**获取一个二等奖*/
		lv = commodityLvService.getCommodityLvBy(2);
		commodity = randomJackpotLv(lv.getLv());
		list.add(commodity);
		/**获取一个三等奖*/
		lv = commodityLvService.getCommodityLvBy(3);
		commodity = randomJackpotLv(lv.getLv());
		list.add(commodity);
		/**获取一个四等奖*/
		lv = commodityLvService.getCommodityLvBy(4);
		commodity = randomJackpotLv(lv.getLv());
		list.add(commodity);
		/**获取一个五等奖*/
		lv = commodityLvService.getCommodityLvBy(5);
		commodity = randomJackpotLv(lv.getLv());
		list.add(commodity);
		
		/**设置2个零等级的奖项*/
		commodity = new SilverCommodityVo();
		commodity.setId(0);
		commodity.setCommodityName("恭喜发财");
		commodity.setAwardLv(0);
		list.add(commodity);
		list.add(commodity);
		
		return list;
	}
	
	@Override
	public SilverCommodityVo randomJackpotLv(Integer lv) {
		List<SilverCommodityVo> list = getSilverCommodityBy(lv);
		if(list != null && list.size() > 0) {
			Random random = new Random();
			Integer randomNo = list.size();
			Integer randomNumber = random.nextInt(randomNo);
			SilverCommodityVo vo = list.get(randomNumber);
			return vo;
		}
		return null;
	}
	
	@Override
	public ExchangeDetailsVo exchangeCommodity(Integer itemId) {
		return commodityMapper.exchangeCommodity(itemId);
	}
	
}
