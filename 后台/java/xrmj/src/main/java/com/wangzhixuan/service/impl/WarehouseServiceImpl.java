package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Commodity;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.Warehouse;
import com.wangzhixuan.model.vo.WarehouseVo;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.mapper.WarehouseMapper;
import com.wangzhixuan.service.IWarehouseService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品仓库 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements IWarehouseService {

	@Autowired
	private WarehouseMapper warehouseMapper;
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<WarehouseVo> list = warehouseMapper.selectWarehouseVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}

	@Override
	public Result insertWarehouse(Commodity commodity) {
		Result result = null;
		if(commodity != null && commodity.getId() != null) {
			Warehouse warehouse = getWarehouseBy(commodity.getId()); 
			if(warehouse == null) {
				warehouse = new Warehouse();
				warehouse.setCommodity(commodity.getId());
				warehouse.setUsableAmount(0);
				warehouse.setOutboundAmount(0);
				warehouse.setAllAmount(0);
				warehouse.setUseAmount(0);
				boolean success = this.insert(warehouse);
				if(success) {
					return new Result("OK");
				} else {
					return new Result(false, "初始化商品库存失败");
				}
			}
			
		}
		return result;
	}

	@Override
	public Warehouse getWarehouseBy(Integer commodity) {
		Map<String, Object> map = new HashMap<>();
		map.put("commodity", commodity);
		List<Warehouse> warehouseList = this.selectByMap(map);
		if(warehouseList != null && warehouseList.size() > 0) {
			return warehouseList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Result addWarehouse(Integer commodity, Integer amount) {
		Warehouse warehouse = null;
		if(commodity != null) {
			warehouse = getWarehouseBy(commodity);
			warehouse.setUsableAmount(warehouse.getUsableAmount()+amount);
			warehouse.setAllAmount(warehouse.getAllAmount()+amount);
			boolean success = this.updateById(warehouse);
			if(success) {
				return new Result("ok");
			} else {
				return new Result(false, "入库-增减库存失败");
			}
		}
		return null;
	}

	@Override
	public Result blockWarehouse(Integer commodity, Integer amount) {
		Warehouse warehouse = null;
		if(commodity != null) {
			warehouse = getWarehouseBy(commodity);
			warehouse.setUsableAmount(warehouse.getUsableAmount()-amount);
			warehouse.setOutboundAmount(warehouse.getOutboundAmount()+amount);
			boolean success = this.updateById(warehouse);
			if(success) {
				return new Result("ok");
			} else {
				return new Result(false, "冻结库存失败");
			}
		}
		return null;
	}

	@Override
	public Result outWarehouse(Integer commodity, Integer amount) {
		Warehouse warehouse = null;
		if(commodity != null) {
			warehouse = getWarehouseBy(commodity);
			warehouse.setUsableAmount(warehouse.getUsableAmount()-amount);
			warehouse.setUseAmount(warehouse.getUseAmount()+amount);
			boolean success = this.updateById(warehouse);
			if(success) {
				return new Result("ok");
			} else {
				return new Result(false, "出库-增减库存失败");
			}
		}
		return null;
	}

	@Override
	public Result outGoldWarehouse(Integer commodity, Integer amount) {
		Warehouse warehouse = null;
		if(commodity != null) {
			warehouse = getWarehouseBy(commodity);
			warehouse.setOutboundAmount(warehouse.getOutboundAmount()-amount);
			warehouse.setUseAmount(warehouse.getUseAmount()+amount);
			boolean success = this.updateById(warehouse);
			if(success) {
				return new Result("ok");
			} else {
				return new Result(false, "兑换商品失败");
			}
		}
		return null;
	}
	
}
