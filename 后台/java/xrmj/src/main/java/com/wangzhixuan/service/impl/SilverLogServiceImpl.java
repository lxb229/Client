package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Commodity;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.SilverLog;
import com.wangzhixuan.model.WarehouseOut;
import com.wangzhixuan.model.vo.SilverLogVo;
import com.wangzhixuan.commons.base.Constant;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.mapper.SilverLogMapper;
import com.wangzhixuan.service.ICommodityService;
import com.wangzhixuan.service.ISilverLogService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 银币抽奖记录 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-11
 */
@Service
public class SilverLogServiceImpl extends ServiceImpl<SilverLogMapper, SilverLog> implements ISilverLogService {

	@Autowired
	private ICommodityService commodityService;
	@Autowired
	private SilverLogMapper silverLogMapper;

	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<SilverLogVo> list = silverLogMapper.selectSilverLogVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}
	
	@Override
	public Result insertSilverZero(String startNo) {
		SilverLog log = new SilverLog();
		log.setStartNo(startNo);
		log.setCommodity(0);
		log.setConsume(Constant.DRAWCOST);
		log.setAwardLv(0);
		log.setCommodityName("恭喜发财");
		log.setCreateTime(new Date());
		boolean success = this.insert(log);
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "增加银币抽奖记录失败");
		}
	}

	@Override
	public Result insertSilverByLv(String startNo, Integer commodity, Integer awardLv, WarehouseOut warehouseOut) {
		SilverLog log = new SilverLog();
		Commodity comm = commodityService.selectById(commodity);
		if(comm != null) {
			log.setStartNo(startNo);
			log.setCommodity(commodity);
			log.setConsume(Constant.DRAWCOST);
			log.setAwardLv(awardLv);
			log.setCommodityName(comm.getCommodityName());
			log.setWarehouseOut(warehouseOut.getId());
			log.setCreateTime(new Date());
			boolean success = this.insert(log);
			if(success) {
				return new Result("ok");
			} else {
				return new Result(false, "增加银币抽奖记录失败");
			}
		} else {
			return null;
		}
		
	}

}
