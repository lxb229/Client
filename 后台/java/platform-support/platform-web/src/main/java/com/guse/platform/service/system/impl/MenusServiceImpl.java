package com.guse.platform.service.system.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.Constant;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.common.config.Config;
import com.guse.platform.common.utils.RightsHelper;
import com.guse.platform.dao.system.MenusMapper;
import com.guse.platform.dao.system.RolesMapper;
import com.guse.platform.entity.system.Menus;
import com.guse.platform.entity.system.Roles;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.system.MenusService;
import com.guse.platform.vo.system.MenuTreeVo;
import com.guse.platform.vo.system.MenusVo;
import com.google.common.collect.Lists;

/**
 * nbin
 * @author Administrator
 * @date 2017年7月18日 下午2:16:12 
 * @version V1.0
 */
@Service("menusService")
public class MenusServiceImpl implements MenusService{
	
	private static Logger logger = LoggerFactory.getLogger(MenusServiceImpl.class);
	
	@Autowired
	private MenusMapper menusMapper;
	@Autowired
	private RolesMapper rolesMapper;
	
	public Result<List<Menus>> getRoleMenus(Roles roles) {
		List<Menus> haveList = new ArrayList<Menus>(); 
		if(roles == null){
			return new Result<List<Menus>>(00000,"获取菜单失败，参数异常！");
		}
		Roles newroles = rolesMapper.selectByPrimaryKey(roles.getRoleId());
		List<Menus> list = this.menusMapper.selectAllMenusList();
		if(CollectionUtils.isEmpty(list)){
			return new Result<List<Menus>>(list);
		}
		for (Menus menus : list) {
			if(newroles.getMenuRights() == null || menus.getMenuId() == null
					|| !RightsHelper.testRights(newroles.getMenuRights().toString(), menus.getMenuId().toString())){
        		continue;
        	} else {
        		haveList.add(menus);
			}
		}
		return new Result<List<Menus>>(haveList);
	}
	
	@Override
	public Result<List<MenusVo>> getUserMenus(Users user) {
		if(user == null){
			return new Result<List<MenusVo>>(00000,"获取菜单失败，参数异常！");
		}
		List<MenusVo> list = Lists.newArrayList();
		if(null == user.getRoleId()){
			return new Result<List<MenusVo>>(list);
		}
		List<Menus> menus = this.menusMapper.selectAllMenusList();
		if(CollectionUtils.isEmpty(menus)){
			return new Result<List<MenusVo>>(list);
		}
		Roles role = this.rolesMapper.selectByPrimaryKey(user.getRoleId());
		if(null == role.getMenuRights()){
			return new Result<List<MenusVo>>(list);
		}
		//排序
		Map<Integer, List<Menus>> menusGroup = menusSortByGroupPid(menus);
		for(Map.Entry<Integer, List<Menus>> entry : menusGroup.entrySet()){
            for(Menus m : entry.getValue() ){
            	if(!user.getLoginName().equals(Config.NO_FILTER)){
            		if(!RightsHelper.testRights(role.getMenuRights().toString(), m.getMenuId().toString())){
                		continue;
                	}
            	}
        		MenusVo mv = new MenusVo();
        		try {
					PropertyUtils.copyProperties(mv, m);
				} catch (Exception e) {
					logger.info("菜单实体copy到Vo异常！",e);
					return new Result<List<MenusVo>>(list);
				} 
        		list.add(mv);
            }
        }
		
		//tree
		List<MenusVo> treeList = buildListToTree(list);
		
		//node
		for(MenusVo one : treeList){
			if(one.getMenuLevel().intValue() == 1){
				one.setMenuNode("m-"+ one.getMenuId());
			}
			List<MenusVo> onechild = one.getChildMenus();
			if(CollectionUtils.isEmpty(onechild)){
				continue;
			}
			for(MenusVo two : onechild){
				Integer twoPid = two.getPid();
				if(two.getMenuLevel().intValue() == 2){
					two.setMenuNode("m-"+ twoPid+"-"+two.getMenuId());
				}
				List<MenusVo> twochild = two.getChildMenus();
				if(CollectionUtils.isEmpty(twochild)){
					continue;
				}
				for(MenusVo three : twochild){
					three.setMenuNode("m-"+ twoPid+"-"+three.getMenuId());
				}
			}
		}
		return new Result<List<MenusVo>>(treeList);
	}	
	
	@Override
	public Result<List<MenuTreeVo>> menusTree() {
		List<MenuTreeVo> list = Lists.newArrayList();
		//设置默认根节点
		MenuTreeVo rootMenu = new MenuTreeVo();
		rootMenu.setId("0");
		rootMenu.setParent("#");
		rootMenu.setText("系统菜单");
		list.add(rootMenu);
		//所有菜单
		List<Menus> menus = this.menusMapper.selectAllMenusList();
		if(CollectionUtils.isEmpty(menus)){
			return new Result<List<MenuTreeVo>>(list);
		}
		Map<Integer, List<Menus>> menusGroup = menusSortByGroupPid(menus);
		for(Map.Entry<Integer, List<Menus>> entry : menusGroup.entrySet()){
            for(Menus m : entry.getValue() ){
            	MenuTreeVo menu = new MenuTreeVo();
            	menu.setId(m.getMenuId().toString());
            	menu.setParent(m.getPid().toString());
            	menu.setText(m.getMenuTitle());
            	menu.setIcon(m.getMenuIcon());
            	menu.setSort(m.getMenuSort()==null?0:m.getMenuSort());
            	menu.setUrl(m.getMenuUrl());
            	list.add(menu);
            }
        }
		return new Result<List<MenuTreeVo>>(list);
	}

	@Override
	public Result<List<Menus>> getChildMenusList(Integer menusId) {
		if(menusId == null){
			return new Result<List<Menus>>(00000,"获取子菜单列表失败，参数异常！");
		}
		List<Menus> list = this.menusMapper.selectChildMenusList(menusId);
		if(CollectionUtils.isEmpty(list)){
			return new Result<List<Menus>>(new ArrayList<Menus>());
		}
		return new Result<List<Menus>>(list);
	}

	@Override
	public Result<Integer> insertUpdateMenus(Menus menus){
		if(menus==null){
			return new Result<Integer>(00000,"创建菜单失败，参数为空！");
		}
		
		if(null != menus.getMenuId()){
			menus.setPid(null);
			menus.setMenuLevel(null);
			menus.setMenuStatus(null);
			return new Result<Integer>(menusMapper.updateByPrimaryKeySelective(menus));
		}
		//层级
		int level = 1;
		if(menus.getPid().intValue() == 0){//一级
			menus.setMenuLevel(level);
		}else{
			Menus parentMenus = menusMapper.selectByPrimaryKey(menus.getPid());
			if(parentMenus == null ){
				return new Result<Integer>(00000,"创建菜单失败，数据异常！");
			}
			menus.setMenuLevel(parentMenus.getMenuLevel() + 1);
		}
		ValidataBean validata = menus.validateModel();
		if(!validata.isFlag()){
			return new Result<Integer>(00000,validata.getMsg());
		}
		menus.setCreateTime(new Date());
		menus.setUpdateTime(new Date());
		menus.setMenuStatus(Constant.DISABLE);
		//success
		return new Result<Integer>(menusMapper.insert(menus));
	}

	@Override
	public Result<Integer> deleteMenus(Integer menusId) {
		if(menusId == null){
			return new Result<Integer>(00000,"删除失败，参数异常！");
		}
		return new Result<Integer>(menusMapper.deleteByPrimaryKey(menusId));
	}

	@Override
	public Result<Integer> enableDisableMenus(Menus menus){
		if(null == menus || null == menus.getMenuId() || null == menus.getMenuStatus()) {
			return new Result<Integer>(00000,"启禁用菜单失败，参数异常！");
		}
		menus.setUpdateTime(new Date());
		return new Result<Integer>(menusMapper.updateByPrimaryKeySelective(menus));
	}

	@Override
	public Result<Menus> selectMenusDetial(Integer menusId) {
		if(null == menusId){
			return new Result<Menus>(00000,"获取菜单详细信息失败，参数异常！");
		}
		return new Result<Menus>(this.menusMapper.selectByPrimaryKey(menusId));
	}
	
	
	/**
	  * 分组排序根据父id
	  * @Title: menusSortByGroup 
	  * @param @param list
	  * @param @return 
	  * @return Map<Integer,List<Menus>>
	  */
	 public static Map<Integer, List<Menus>> menusSortByGroupPid(List<Menus> list){
       Map<Integer, List<Menus>> map = new HashMap<Integer, List<Menus>>();
       for(Menus menus : list) {
           List<Menus> staList = map.get(menus.getPid());
           if(staList==null){
               staList = new ArrayList<Menus>();
           }
           staList.add(menus);
           Collections.sort(staList, new Comparator<Menus>() {
               @Override
               public int compare(Menus o1, Menus o2) {
                   return o1.getMenuSort().compareTo(o2.getMenuSort());
               }
           });
           
           map.put(menus.getPid(), staList);
       }
       return map;
   }
	
	/*
	 * 菜单根据树结构查询 
	 */
 	@SuppressWarnings("unchecked")
    private List<MenusVo> buildListToTree(List<MenusVo> allMenus) {
        List<MenusVo> roots = findRoots(allMenus);
        List<MenusVo> notRoots = (List<MenusVo>) CollectionUtils.subtract(allMenus, roots);
        for (MenusVo root : roots) {
        	root.setMenuNode("m-"+ root.getMenuId());
            root.setChildMenus(findChildren(root, notRoots));
        }
        return roots;
    }

    public List<MenusVo> findRoots(List<MenusVo> allMenus) {
        List<MenusVo> results = new ArrayList<MenusVo>();
        for (MenusVo MenusVo : allMenus) {
            boolean isRoot = true;
            for (MenusVo comparedOne : allMenus) {
                if (MenusVo.getPid() == comparedOne.getMenuId()) {
                    isRoot = false;
                    break;
                }
            }
            if (isRoot) {
                results.add(MenusVo);
            }
        }
        return results;
    }
    
    @SuppressWarnings("unchecked")
    private List<MenusVo> findChildren(MenusVo root, List<MenusVo> notRoots) {
        List<MenusVo> children = new ArrayList<MenusVo>();
        for (MenusVo comparedOne : notRoots) {
            if (comparedOne.getPid() == root.getMenuId()) {
                children.add(comparedOne);
            }
        }
        List<MenusVo> notChildren = (List<MenusVo>) CollectionUtils.subtract(notRoots, children);
        for (MenusVo child : children) {
            List<MenusVo> tmpChildren = findChildren(child, notChildren);
            child.setChildMenus(tmpChildren);
        }
        return children;
    }
  
}
	