package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.GoldCommodity;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.ValidataBean;
import com.wangzhixuan.model.Warehouse;
import com.wangzhixuan.model.vo.GoldCommodityVo;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.mapper.GoldCommodityMapper;
import com.wangzhixuan.service.IGoldCommodityService;
import com.wangzhixuan.service.IWarehouseService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 金币兑换商品 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-08
 */
@Service
public class GoldCommodityServiceImpl extends ServiceImpl<GoldCommodityMapper, GoldCommodity> implements IGoldCommodityService {

	@Autowired
	private 
	GoldCommodityMapper goldCommodityMapper; 
	@Autowired
	private IWarehouseService warehouseService;
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<GoldCommodityVo> list = goldCommodityMapper.selectGoldCommodityVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}
	@Override
	public GoldCommodity getGoldCommodityBy(Integer commodityId) {
		return goldCommodityMapper.getGoldCommodityBy(commodityId);
	}
	@Override
	public Result insertGoldCommodity(GoldCommodity goldCommodity) {
		ValidataBean validata = goldCommodity.validateModel();
		if (!validata.isFlag()) {
			return new Result(false, validata.getMsg());
		}
		Result result = null;
		/**获取商品库存*/
		Warehouse warehouse = warehouseService.getWarehouseBy(goldCommodity.getCommodity());
		/**判断剩余库存是否满足兑换数量*/
		if(warehouse != null && warehouse.getUsableAmount() >= goldCommodity.getAmount()) {
			/**冻结库存*/
			result = warehouseService.blockWarehouse(goldCommodity.getCommodity(), goldCommodity.getAmount());
			if(result != null && result.isSuccess()) {
				boolean success = this.insert(goldCommodity);
				result = success ? new Result("增加成功"):new Result(false, "增加失败");
			}
		} else {
			result = new Result(false, "库存数量不足");
		}
		return result;
	}
	@Override
	public Result deleteGoldCommodity(Integer goldCommodityId) {
		Result result = null;
		GoldCommodity goldCommodity = this.selectById(goldCommodityId);
		if(goldCommodity != null) {
			/**判断被删除的兑换商品中是否还有剩余数量*/
			int residue = goldCommodity.getResidue();
			if(residue > 0) {
				/**将兑换商品中的剩余返回到可用库存中*/
				warehouseService.blockWarehouse(goldCommodity.getCommodity(), -residue);
			}
			/**删除兑换商品*/
			boolean success = this.deleteById(goldCommodityId);
			if(success) {
				result = new Result("ok");
			} else {
				result = new Result(false, "删除失败");
			}
		}
		return result;
	}
	@Override
	public Result outGoldCommodity(Integer commodity, Integer amount) {
		GoldCommodity goldCommodity = null;
		if(commodity != null) {
			goldCommodity = getGoldCommodityBy(commodity);
			goldCommodity.setResidue(goldCommodity.getResidue()-1);
			boolean success = this.updateById(goldCommodity);
			if(success) {
				return new Result("ok");
			} else {
				return new Result(false, "兑换商品失败");
			}
		}
		return null;
	}
	
}
