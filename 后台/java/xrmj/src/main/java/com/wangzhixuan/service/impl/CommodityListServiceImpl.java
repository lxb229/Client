package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.CommodityList;
import com.wangzhixuan.model.WarehouseIn;
import com.wangzhixuan.model.WarehouseOut;
import com.wangzhixuan.commons.base.Constant;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.mapper.CommodityListMapper;
import com.wangzhixuan.service.ICommodityListService;
import com.wangzhixuan.service.ISystemOrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 库存商品列表 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-09
 */
@Service
public class CommodityListServiceImpl extends ServiceImpl<CommodityListMapper, CommodityList> implements ICommodityListService {

	@Autowired
	private CommodityListMapper commodityListMapper;
	@Autowired
	private ISystemOrderService systemOrderService;
	
	@Override
	public Result insertCommodityList(WarehouseIn warehouseIn, String cardNo, String secretKey) {
		Result result = null;
		boolean success = false;
		if(warehouseIn.getAmount() > 0) {
			for (int i = 0; i < warehouseIn.getAmount(); i++) {
				/***/
				String redeemCode = systemOrderService.randomOrder(Constant.RANDOM_REDEEM_CODE);
				CommodityList commodityList = new CommodityList();
				commodityList.setWarehouseIn(warehouseIn.getId());
				commodityList.setCommodity(warehouseIn.getCommodity());
				commodityList.setSupplier(warehouseIn.getSupplier());
				commodityList.setCardNo(cardNo);
				commodityList.setSecretKey(secretKey);
				commodityList.setStatus(0);
				commodityList.setRedeemCode(redeemCode);
				commodityList.setUseStatus(0);
				commodityList.setCreateTime(warehouseIn.getCreateTime());
				success = this.insert(commodityList);
			}
			if(success) {
				result = new Result("ok");
			} else {
				result = new Result(false, "增加商品库存列表失败");
			}
		}
		return result;
	}

	@Override
	public Result deleteCommodityList(WarehouseIn warehouseIn) {
		Result result = null;
		/**入库冲账数量*/
		Integer amount = Math.abs( warehouseIn.getAmount());
		/**根据入库获取对应可用的商品库存列表*/
		List<CommodityList> list = getListByWarehouseIn(warehouseIn.getWarehouseIn());
		/**游戏库存大于冲账数量*/
		if(list != null && list.size() >= amount) {
			/**将有效商品库存置为无效*/
			result = InvalidCommodityList(list, amount);
		} else {
			result = new Result(false, "有效数量不足以冲账");
		}
		return result;
	}
	
	@Override
	public Result updateByWarehouseOut(WarehouseOut out) {
		Result result = null;
		int amount = Math.abs(out.getAmount());
		/**获取商品库存列表*/
		List<CommodityList> list = getListByWarehouseOut(out.getWarehouseOut());
		if(list == null || list.size() < amount) {
			return new Result(false, "商品库存列表数量错误");
		}
		boolean success = false;
		for (int i = 0; i < amount; i++) {
			CommodityList commodityList = list.get(i);
			commodityList.setWarehouseOut(0);
			/**将商品库存设置为未使用*/
			commodityList.setUseStatus(0);
			success = this.updateById(commodityList);
		}
		if(success) {
			result = new Result("ok");
		} else {
			result = new Result(false, "设置商品库存列表已使用失败");
		}
		return result;
	}

	@Override
	public Result InvalidCommodityList(List<CommodityList> list, Integer amount) {
		Result result = null;
		boolean success = false;
		if(list != null && list.size() >= amount) {
			for (int i = 0; i < amount; i++) {
				CommodityList commodityList = list.get(i);
				/**将商品库存设置为无效*/
				commodityList.setStatus(1);
				success = this.updateById(commodityList);
			}
			if(success) {
				result = new Result("ok");
			} else {
				result = new Result(false, "设置商品库存列表无效失败");
			}
		}
		return result;
	}
	
	@Override
	public Result outCommodityList(WarehouseOut out) {
		Result result = null;
		int amount = out.getAmount();
		/**获取商品库存列表*/
		List<CommodityList> list = getListByCommodity(out.getCommodity());
		if(list == null || list.size() < amount) {
			return new Result(false, "商品库存列表不足");
		}
		boolean success = false;
		for (int i = 0; i < amount; i++) {
			CommodityList commodityList = list.get(i);
			commodityList.setWarehouseOut(out.getId());
			/**将商品库存设置为已使用*/
			commodityList.setUseStatus(2);
			success = this.updateById(commodityList);
		}
		if(success) {
			result = new Result("ok");
		} else {
			result = new Result(false, "设置商品库存列表已使用失败");
		}
		return result;
	}

	@Override
	public Result blockCommodityList(List<CommodityList> list, Integer amount) {
		Result result = null;
		boolean success = false;
		if(list != null && list.size() >= amount) {
			for (int i = 0; i < amount; i++) {
				CommodityList commodityList = list.get(i);
				/**将商品库存设置为冻结*/
				commodityList.setUseStatus(1);
				success = this.updateById(commodityList);
			}
			if(success) {
				result = new Result("ok");
			} else {
				result = new Result(false, "设置商品库存列表冻结失败");
			}
		}
		return result;
	}

	@Override
	public List<CommodityList> getListByWarehouseIn(Integer warehouseIn) {
		return commodityListMapper.getListByWarehouseIn(warehouseIn);
	}
	
	@Override
	public List<CommodityList> getListByCommodity(Integer commodity) {
		return commodityListMapper.getListByCommodity(commodity);
	}

	@Override
	public List<CommodityList> getListByWarehouseOut(Integer warehouseOut) {
		return commodityListMapper.getListByWarehouseOut(warehouseOut);
	}

}
