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
import com.guse.platform.entity.system.Resource;
import com.guse.platform.service.system.ResourceService;

/**
 * 资源
 * @author nbin
 * @version 1.0
 * @CreateDate 2017-7-13 - 上午11:34:26
 */
@Controller
@RequestMapping("/system/resource")
public class ResourceController extends BaseController {
	
	@Autowired
	private ResourceService resourceService;
	/**
     * 列表
     * @return
     */
    @RequestMapping(value = "/getResourceListForMenuId", method = RequestMethod.POST)
    @ResponseBody
    public Object getResourceListForMenuId(Integer menuId) throws Exception {
    	Result<List<Resource>> result = resourceService.getResourceListForMenuId(menuId);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
	
    /**
     * 增加
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateResource", method = RequestMethod.POST)
    @ResponseBody
    public Object saveOrUpdateResource(Resource resource) throws Exception {
    	Result<Integer> result = resourceService.saveOrUpdateResource(resource);
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
    public Object delete(Integer resourceId) throws Exception {
    	Result<Integer> result = resourceService.deleteResource(resourceId);
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
    public Object detial(Integer resourceId) throws Exception {
    	Result<Resource> result = resourceService.selectResourceDetial(resourceId);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
}
