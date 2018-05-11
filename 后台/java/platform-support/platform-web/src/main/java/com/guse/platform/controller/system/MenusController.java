
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
import com.guse.platform.entity.system.Menus;
import com.guse.platform.entity.system.Roles;
import com.guse.platform.service.system.MenusService;
import com.guse.platform.vo.system.MenuTreeVo;

/**
 * 菜单
 * @author nbin
 * @date 2017年7月20日 下午5:37:57 
 * @version V1.0
 */
@Controller
@RequestMapping("/system/menus")
public class MenusController extends BaseController {
	
	@Autowired
	private MenusService menusService;
	
	/**
     * 菜单树
     * @return
     */
    @RequestMapping(value = "/loadMenusTree", method = RequestMethod.POST)
    @ResponseBody
    public Object loadMenusTree() throws Exception {
    	Result<List<MenuTreeVo>> result = menusService.menusTree();
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
	
	/**
     * 菜单列表
     * @return
     */
    @RequestMapping(value = "/getChildMenusList", method = RequestMethod.POST)
    @ResponseBody
    public Object getChildMenusList(Integer menusId) throws Exception {
    	Result<List<Menus>> result = menusService.getChildMenusList(menusId);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 菜单详情
     * @return
     */
    @RequestMapping(value = "/detial", method = RequestMethod.POST)
    @ResponseBody
    public Object detial(Integer menusId) throws Exception {
    	Result<Menus> result = menusService.selectMenusDetial(menusId);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
	
    /**
     * 增加/更新
     * @return
     */
    @RequestMapping(value = "/insertUpdateMenus", method = RequestMethod.POST)
    @ResponseBody
    public Object insertUpdateMenus(Menus menus) throws Exception {
    	Result<Integer> result = menusService.insertUpdateMenus(menus);
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
    public Object delete(Integer menusId) throws Exception {
    	Result<Integer> result = menusService.deleteMenus(menusId);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    /**
     * 禁用/启用
     * @return
     */
    @RequestMapping(value = "/enableDisableMenus", method = RequestMethod.POST)
    @ResponseBody
    public Object enableDisableMenus(Menus menus) throws Exception {
    	Result<Integer> result = menusService.enableDisableMenus(menus);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 获取角色已经分配的菜单列表
     * @return
     */
    @RequestMapping(value = "/roleMenus", method = RequestMethod.POST)
    @ResponseBody
    public Object roleMenus(Roles roles) {
    	Result<List<Menus>> result = menusService.getRoleMenus(roles);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
}
