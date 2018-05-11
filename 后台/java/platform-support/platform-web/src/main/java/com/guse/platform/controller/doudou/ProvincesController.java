
package com.guse.platform.controller.doudou;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.base.BaseController;


@Controller
@RequestMapping("/provincesController")
public class ProvincesController extends BaseController {
	
	/**
	 * 分页查询
	 * @Title: queryPagelist 
	 * @param @return
	 * @param @throws Exception 
	 * @return Object
	 */
    @RequestMapping(value = "/queryPagelist", method = RequestMethod.POST)
    public @ResponseBody Object queryPagelist() throws Exception {
    	
    	return AjaxResponse.success();
    }
    
    /**
     * 省市树
     * @return
     */
    @RequestMapping(value = "/loadProvinceTree", method = RequestMethod.POST)
    @ResponseBody
    public Object loadProvinceTree() throws Exception {
//    	Result<List<MenuTreeVo>> result = menusService.menusTree();
//    	if (!result.isOk()) {
//    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
//    	}
//    	return new AjaxResponse(result.getData());
    	return null;
    }
}
