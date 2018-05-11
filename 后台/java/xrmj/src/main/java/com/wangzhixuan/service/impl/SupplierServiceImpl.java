package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.Supplier;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.mapper.SupplierMapper;
import com.wangzhixuan.service.ISupplierService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 供应商 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements ISupplierService {

	@Autowired
	private SupplierMapper supplierMapper;
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<Supplier> list = supplierMapper.selectSupplierPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}
	@Override
	public List<Supplier> getAllSupplier() {
		return supplierMapper.getAllSupplier();
	}
	
}
