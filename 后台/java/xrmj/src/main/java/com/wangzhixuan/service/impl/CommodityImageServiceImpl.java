package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.CommodityImage;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.vo.CommodityImageVo;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.mapper.CommodityImageMapper;
import com.wangzhixuan.service.ICommodityImageService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品图片 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-08
 */
@Service
public class CommodityImageServiceImpl extends ServiceImpl<CommodityImageMapper, CommodityImage> implements ICommodityImageService {

	@Autowired
	private CommodityImageMapper commodityImageMapper;
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<CommodityImageVo> list = commodityImageMapper.selectCommodityImageVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}
	@Override
	public List<CommodityImage> getIconList(Integer commodity) {
		return commodityImageMapper.getIconList(commodity);
	}
	@Override
	public List<CommodityImage> getDetailsList(Integer commodity) {
		return commodityImageMapper.getDetailsList(commodity);
	}
	
}
