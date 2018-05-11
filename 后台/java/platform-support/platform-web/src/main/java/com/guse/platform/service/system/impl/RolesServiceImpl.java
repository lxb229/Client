package com.guse.platform.service.system.impl;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.Constant;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.common.utils.RightsHelper;
import com.guse.platform.common.utils.Tools;
import com.guse.platform.dao.system.ResourceMapper;
import com.guse.platform.dao.system.RolesMapper;
import com.guse.platform.entity.system.Resource;
import com.guse.platform.entity.system.Roles;
import com.guse.platform.service.system.RolesService;


/**
 * 
 * @author nbin
 * @version 1.0
 * @CreateDate 201-7-26 - 上午11:34:26
 */
@Service("rolesService")
public class RolesServiceImpl implements RolesService{
	
	@Autowired
	private RolesMapper rolesMapper;
	
	@Autowired
	private ResourceMapper resourceMapper;
	
	@Override
	public Result<List<Roles>> queryListRoles(Roles roles) {
		List<Roles> roleList = rolesMapper.select(roles);
		return new Result<List<Roles>>(roleList);
	}
	
	@Override
	public Result<Integer> saveOrUpdateRoles(Roles roles) {
		ValidataBean validata = roles.validateModel();
		if(!validata.isFlag()){
			return new Result<Integer>(00000,validata.getMsg());
		}
		if(null == roles.getRoleId()){
			roles.setCreateTime(new Date());
			roles.setUpdateTime(new Date());
			roles.setRoleStatus(Constant.DISABLE);
		}
		//角色名称不能重复
		List<Roles> existRoles = rolesMapper.getRolesByRoleName(roles.getRoleName());
		if(CollectionUtils.isNotEmpty(existRoles)){
			return new Result<Integer>(00000,"角色名称已存在！");
		}
		Integer result = null;
		if(null != roles.getRoleId()){
			//更新用户不可以更改登录名
			result = rolesMapper.updateByPrimaryKeySelective(roles);
		}else{
			result = rolesMapper.insert(roles);
		}
		return new Result<Integer>(result);
	}

	@Override
	public Result<Integer> deleteRoles(Integer roleId) {
		if(null == roleId){
			return new Result<Integer>(00000,"删除角色失败，参数异常！");
		}
		return new Result<Integer>(rolesMapper.deleteByPrimaryKey(roleId));
	}

	@Override
	public Result<Roles> selectRulesDetial(Integer roleId) {
		if(null == roleId){
			return new Result<Roles>(00000,"获取角色详细信息失败，参数异常！");
		}
		return new Result<Roles>(this.rolesMapper.selectByPrimaryKey(roleId));
	}

	@Override
	public Result<Integer> enableDisableRoles(Roles roles){
		if(null == roles || null == roles.getRoleId() || null == roles.getRoleStatus()) {
			return new Result<Integer>(00000,"启禁用用户失败！");
		}
		roles.setUpdateTime(new Date());
		return new Result<Integer>(rolesMapper.updateByPrimaryKeySelective(roles));
	}

	@Override
	public Result<Integer> menusRights(String menusIds, Integer roleId) {
		if(roleId == null || StringUtils.isBlank(menusIds)){
			return new Result<Integer>(00000,"菜单授权失败，参数异常！");
		}
		//菜单权限
		BigInteger rights = RightsHelper.sumRights(Tools.str2StrArray(menusIds));
		Roles role=new Roles();
		if (rights != null) {
			role.setMenuRights(rights.toString());
		}
		//资源权限
		String[] temps = menusIds.split("\\,");
		List<Long> menusIdList = new ArrayList<Long>();
		for (String long1 : temps) {
			menusIdList.add(Long.parseLong(long1));
		}
		List<Resource> resList = resourceMapper.selectResourceListByMenuIds(menusIdList);
		String resourceIds = "";
		if(resList != null) {
			for (Resource resource : resList) {
				resourceIds += resource.getSrId() + ",";
			}
		}
		if(resourceIds != null && !resourceIds.equals("")) {
			resourceIds = resourceIds.substring(0, resourceIds.length() -1);
		}
		BigInteger resRights = RightsHelper.sumRights(Tools.str2StrArray(resourceIds));
		if (resRights != null) {
			role.setResourceRights(resRights.toString());
		}
		
		role.setRoleId(roleId);
		return new Result<Integer>(rolesMapper.updateByPrimaryKeySelective(role));
	}

	@Override
	public Result<Integer> menusResourceRigths(String resouceIds, Integer roleId) {
		if(roleId == null || StringUtils.isBlank(resouceIds)){
			return new Result<Integer>(00000,"菜单授权失败，参数异常！");
		}
		BigInteger rights = RightsHelper.sumRights(Tools.str2StrArray(resouceIds));
		Roles role=new Roles();
		if (rights != null) {
			role.setResourceRights(rights.toString());
		}
		role.setRoleId(roleId);
		return new Result<Integer>(rolesMapper.updateByPrimaryKeySelective(role));
	}
	
}
