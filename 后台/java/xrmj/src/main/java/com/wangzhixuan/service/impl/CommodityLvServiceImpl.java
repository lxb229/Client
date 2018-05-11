package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.CommodityLv;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.mapper.CommodityLvMapper;
import com.wangzhixuan.service.ICommodityLvService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品等级 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
@Service
public class CommodityLvServiceImpl extends ServiceImpl<CommodityLvMapper, CommodityLv> implements ICommodityLvService {
	@Autowired
	private CommodityLvMapper lvMapper;
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<CommodityLv> list = lvMapper.selectCommodityLvPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}

	@Override
	public Result deleteCommodityLv(Integer id) {
		CommodityLv lv = selectById(id);
		boolean success = false;
		if(lv != null) {
			lv.setStatus(1);
			success = this.updateById(lv);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false,"删除失败");
		}
	}

	@Override
	public List<CommodityLv> getAllLv() {
		return lvMapper.getAllLv();
	}

	@Override
	public CommodityLv getCommodityLvBy(Integer lv) {
		Map<String, Object> map = new HashMap<>();
		map.put("lv", lv);
		List<CommodityLv> lvList = this.selectByMap(map);
		if(lvList != null && lvList.size() > 0) {
			return lvList.get(0);
		} else {
			return null;
		}
	}
}
