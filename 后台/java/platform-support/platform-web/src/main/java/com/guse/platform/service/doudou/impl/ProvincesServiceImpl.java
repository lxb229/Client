package com.guse.platform.service.doudou.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.dao.doudou.ProvincesMapper;
import com.guse.platform.service.doudou.ProvincesService;
import com.guse.platform.vo.doudou.ProvincesVo;
import com.guse.platform.vo.system.MenuTreeVo;
import com.guse.platform.entity.doudou.Provinces;
import com.guse.platform.entity.system.Menus;

/**
 * provinces
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class ProvincesServiceImpl extends BaseServiceImpl<Provinces, java.lang.Integer> implements ProvincesService{

	@Autowired
	private ProvincesMapper  provincesMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(provincesMapper);
	}
	
	@Override
	public Result<List<ProvincesVo>> menusTree() {
		List<MenuTreeVo> list = Lists.newArrayList();
		//设置默认根节点
		MenuTreeVo rootMenu = new MenuTreeVo();
		rootMenu.setId("0");
		rootMenu.setParent("#");
		rootMenu.setText("中国省份");
		list.add(rootMenu);
		//所有菜单
//		List<Menus> menus = this.menusMapper.selectAllMenusList();
//		if(CollectionUtils.isEmpty(menus)){
//			return new Result<List<MenuTreeVo>>(list);
//		}
//		Map<Integer, List<Menus>> menusGroup = menusSortByGroupPid(menus);
//		for(Map.Entry<Integer, List<Menus>> entry : menusGroup.entrySet()){
//            for(Menus m : entry.getValue() ){
//            	MenuTreeVo menu = new MenuTreeVo();
//            	menu.setId(m.getMenuId().toString());
//            	menu.setParent(m.getPid().toString());
//            	menu.setText(m.getMenuTitle());
//            	menu.setIcon(m.getMenuIcon());
//            	menu.setSort(m.getMenuSort()==null?0:m.getMenuSort());
//            	menu.setUrl(m.getMenuUrl());
//            	list.add(menu);
//            }
//        }
//		return new Result<List<MenuTreeVo>>(list);
		return null;
	}
}
