
package com.guse.platform.controller.doudou;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.base.BaseController;
import com.guse.platform.common.base.Constant;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.doudou.GoldLog;
import com.guse.platform.service.doudou.GoldLogService;


@Controller
@RequestMapping("/goldLogController")
public class GoldLogController extends BaseController {
	@Autowired
	private GoldLogService goldLogService;
	
	/**
	 * 分页查询
	 * @Title: queryPagelist 
	 * @param @return
	 * @param @throws Exception 
	 * @return Object
	 */
    @RequestMapping(value = "/queryPagelist", method = RequestMethod.POST)
    public @ResponseBody Object queryPagelist(GoldLog goldLog, PageBean pageBean) throws Exception {
    	Result<PageResult<GoldLog>> result = null;
		try {
			result = goldLogService.queryPageList(pageBean, goldLog, null);
		} catch (Exception e) {
			return new AjaxResponse(Constant.CODE_ERROR, e.getMessage());
		}
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    /**
	 * 分页查询
	 * @Title: queryPagelist 
	 * @param @return
	 * @param @throws Exception 
	 * @return Object
	 */
    @RequestMapping(value = "/queryDeductPagelist", method = RequestMethod.POST)
    public @ResponseBody Object queryDeductPagelist(GoldLog goldLog, PageBean pageBean) throws Exception {
    	Result<PageResult<GoldLog>> result = null;
		try {
			result = goldLogService.queryPageList(pageBean, goldLog, null);
		} catch (Exception e) {
			return new AjaxResponse(Constant.CODE_ERROR, e.getMessage());
		}
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    
    /**
	 * 订单金额统计
	 * @Title: queryPagelist 
	 * @param @return
	 * @param @throws Exception 
	 * @return Object
	 */
    @RequestMapping(value = "/countAmount", method = RequestMethod.POST)
    public @ResponseBody Object countAmount(GoldLog goldLog) throws Exception {
    	Result<Map<String, Object>> result = null;
    	// 统计订单金额
		result = goldLogService.countAmount(goldLog);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
}
