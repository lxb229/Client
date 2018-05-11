package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.CommodityType;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.mapper.CommodityTypeMapper;
import com.wangzhixuan.service.ICommodityTypeService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品类型 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-31
 */
@Service
public class CommodityTypeServiceImpl extends ServiceImpl<CommodityTypeMapper, CommodityType> implements ICommodityTypeService {

	@Autowired
	private CommodityTypeMapper typeMapper;
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<CommodityType> list = typeMapper.selectCommodityTypePage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}

	@Override
	public Result deleteCommodityType(Integer id) {
		CommodityType type = selectById(id);
		boolean success = false;
		if(type != null) {
			type.setStatus(1);
			success = this.updateById(type);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false,"删除失败");
		}
	}

	@Override
	public List<CommodityType> getAllTyep() {
		
		return typeMapper.getAllTyep();
	}
	
}
