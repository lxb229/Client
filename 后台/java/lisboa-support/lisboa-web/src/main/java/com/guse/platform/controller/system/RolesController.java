package com.guse.platform.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.base.BaseController;
import com.guse.platform.common.base.Constant;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.system.Roles;
import com.guse.platform.service.system.RolesService;

/**
 * 角色
 * @author nbin
 * @version 1.0
 * @CreateDate 2017-7-13 - 上午11:34:26
 */
@Controller
@RequestMapping("/system/roles")
public class RolesController extends BaseController {
	
	@Autowired
	private RolesService rolesService;
	/**
     * 列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Object list(Roles roles) throws Exception {
    	
    	Result<List<Roles>> result = rolesService.queryListRoles(roles);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 详情
     * @return
     */
    @RequestMapping(value = "/detial", method = RequestMethod.POST)
    @ResponseBody
    public Object detial(Integer roleId) throws Exception {
    	Result<Roles> result = rolesService.selectRulesDetial(roleId);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
	
    /**
     * 增加/更新
     * @return
     */
    @RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Object addOrUpdate(Roles roles) throws Exception {
    	Result<Integer> result = rolesService.saveOrUpdateRoles(roles);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    
    /**
     * 删除
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Object delete(Integer roleId) throws Exception {
    	Result<Integer> result = rolesService.deleteRoles(roleId);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    /**
     * 禁用/启用
     * @return
     */
    @RequestMapping(value = "/enableDisableRoles", method = RequestMethod.POST)
    @ResponseBody
    public Object enableDisableRoles(Roles roles) throws Exception {
    	Result<Integer> result = rolesService.enableDisableRoles(roles);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 授权菜单
     * @return Object  
     */
    @RequestMapping(value = "/accreditMenu", method = RequestMethod.POST)
    public @ResponseBody Object accreditMenu(String menusIds,Integer roleId) throws Exception {
    	Result<Integer> result = rolesService.menusRights(menusIds, roleId);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 授权资源
     * @return Object  
     */
    @RequestMapping(value = "/accreditResource", method = RequestMethod.POST)
    public @ResponseBody Object accreditResource(String resouceIds,Integer roleId) throws Exception {
    	Result<Integer> result = rolesService.menusResourceRigths(resouceIds, roleId);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    
}
