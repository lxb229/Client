package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.SilverCommodity;
import com.wangzhixuan.model.vo.SilverCommodityVo;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.mapper.SilverCommodityMapper;
import com.wangzhixuan.service.ISilverCommodityService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 银币抽奖商品 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-08
 */
@Service
public class SilverCommodityServiceImpl extends ServiceImpl<SilverCommodityMapper, SilverCommodity> implements ISilverCommodityService {

	@Autowired
	private SilverCommodityMapper silverCommodityMapper;
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<SilverCommodityVo> list = silverCommodityMapper.selectSilverCommodityVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}
	@Override
	public SilverCommodityVo getSilverCommodityBy(Integer commodityId) {
		return silverCommodityMapper.getSilverCommodityBy(commodityId);
	}
	
}
