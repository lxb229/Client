package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.ValidataBean;
import com.wangzhixuan.model.WarehouseIn;
import com.wangzhixuan.model.vo.WarehouseINVo;
import com.wangzhixuan.commons.base.Constant;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.BeanUtils;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.mapper.WarehouseInMapper;
import com.wangzhixuan.service.ICommodityListService;
import com.wangzhixuan.service.ISystemOrderService;
import com.wangzhixuan.service.IWarehouseInService;
import com.wangzhixuan.service.IWarehouseService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 入库单 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
@Service
public class WarehouseInServiceImpl extends ServiceImpl<WarehouseInMapper, WarehouseIn> implements IWarehouseInService {

	@Autowired
	private WarehouseInMapper warehouseInMapper;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private ISystemOrderService systemOrderService;
	@Autowired
	private ICommodityListService commodityListService;
	
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<WarehouseINVo> list = warehouseInMapper.selectWarehouseInVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}
	@Override
	public Result insertWarehouseIn(WarehouseINVo vo) {
		WarehouseIn warehouseIn = new WarehouseIn();
		BeanUtils.copyProperties(vo, warehouseIn);
		/**生成入库单号*/
		String inNo = systemOrderService.randomOrder(Constant.RANDOM_WAREHOUSE_IN);
		if(StringUtils.isNotBlank(inNo)) {
			warehouseIn.setInNo(inNo);
		}
		ValidataBean validata = warehouseIn.validateModel();
		if (!validata.isFlag()) {
			return new Result(false, validata.getMsg());
		}
		boolean success = this.insert(warehouseIn);
		Result result = null;
		/**处理正常入库*/
		if(success && warehouseIn.getType() == 0) {
			/**生成商品库存列表*/
			result = commodityListService.insertCommodityList(warehouseIn, vo.getCardNo(), vo.getSecretKey());
			if(result != null && result.isSuccess()) {
				/**增加商品库存*/
				result = warehouseService.addWarehouse(warehouseIn.getCommodity(), warehouseIn.getAmount());
			}
			/**处理入库冲账*/
		} else if(success && warehouseIn.getType() == 1) {
			/**删除对应商品库存列表*/
			result = commodityListService.deleteCommodityList(warehouseIn);
			/**更新商品库存*/
			if(result != null && result.isSuccess()) {
				/**更新商品库存*/
				result = warehouseService.addWarehouse(warehouseIn.getCommodity(), warehouseIn.getAmount());
			}
		}
		return result;
	}
	@Override
	public List<WarehouseIn> getAllWarehouseIn(Integer supplier, Integer commodity) {
		return warehouseInMapper.getAllWarehouseIn(supplier, commodity);
	}
	
}
