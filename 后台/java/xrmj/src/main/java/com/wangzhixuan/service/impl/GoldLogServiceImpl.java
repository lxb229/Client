package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.GoldCommodity;
import com.wangzhixuan.model.GoldLog;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.WarehouseOut;
import com.wangzhixuan.model.vo.GoldLogVo;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.shiro.ShiroUser;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.mapper.GoldLogMapper;
import com.wangzhixuan.service.IGoldCommodityService;
import com.wangzhixuan.service.IGoldLogService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 金币兑换商品记录 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-11
 */
@Service
public class GoldLogServiceImpl extends ServiceImpl<GoldLogMapper, GoldLog> implements IGoldLogService {

	@Autowired
	private IGoldCommodityService goldCommodityService;
	@Autowired
	private GoldLogMapper goldLogMapper;
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<GoldLogVo> list = goldLogMapper.selectGoldLogVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}
	
	@Override
	public GoldLogVo selectGoldLogVoBy(Integer id) {
		return goldLogMapper.selectGoldLogVoBy(id);
	}
	

	@Override
	public Result disposeGoldLog(GoldLog goldLog, ShiroUser user) {
		GoldLog oldGoldLog = this.selectById(goldLog.getId());
		/**如果将状态改为已发货，必填写快递公司和快递单号*/
		if(oldGoldLog.getStatus()==0 && goldLog.getStatus() == 1) {
			goldLog.setDisposeId(user.getId().intValue());
			goldLog.setDisposeTime(new Date());
			if(StringUtils.isBlank(goldLog.getExpress()) || StringUtils.isBlank(goldLog.getExpressCode())) {
				return new Result(false, "未填写快递单号");
			}
		}
		boolean success = this.updateById(goldLog);
		if(success) {
			return new Result("OK");
		} else {
			return new Result(false, "编辑失败");
		}
	}

	
	@Override
	public Result insertGold(String startNo, Integer commodity, WarehouseOut warehouseOut, String name, String phone,
			String address) {
		GoldLog log = new GoldLog();
		GoldCommodity goldCommodity = goldCommodityService.getGoldCommodityBy(commodity);
		if(goldCommodity != null) {
			log.setStartNo(startNo);
			log.setConsume(goldCommodity.getExchangePrice());
			log.setCommodity(commodity);
			log.setStatus(0);
			log.setWarehouseOut(warehouseOut.getId());
			log.setPlayerName(name);
			log.setPlayerPhone(phone);
			log.setPlayerAddress(address);
			log.setCreateTime(new Date());
			boolean success = this.insert(log);
			if(success) {
				return new Result("ok");
			} else {
				return new Result(false, "增加金币兑换商品记录失败");
			}
		} else {
			return null;
		}
	}

}
