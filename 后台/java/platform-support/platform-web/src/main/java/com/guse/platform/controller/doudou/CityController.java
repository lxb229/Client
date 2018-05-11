
package com.guse.platform.controller.doudou;
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
import com.guse.platform.service.doudou.CityService;
import com.guse.platform.vo.doudou.CityTreeVo;


@Controller
@RequestMapping("/cityController")
public class CityController extends BaseController {
	@Autowired
	private CityService cityService;
	
	
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
     * 地区区域树
     * @return
     */
    @RequestMapping(value = "/loadCityTree", method = RequestMethod.POST)
    @ResponseBody
    public Object loadCityTree() throws Exception {
    	Result<List<CityTreeVo>> result = cityService.menusTree();
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
}
