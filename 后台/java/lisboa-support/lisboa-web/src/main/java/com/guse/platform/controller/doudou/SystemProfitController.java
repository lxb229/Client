
package com.guse.platform.controller.doudou;
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
import com.guse.platform.entity.doudou.SystemProfit;
import com.guse.platform.service.doudou.SystemProfitService;


@Controller
@RequestMapping("/systemProfitController")
public class SystemProfitController extends BaseController {
	@Autowired
	private SystemProfitService profitService;
	/**
	 * 分页查询
	 * @Title: queryPagelist 
	 * @param @return
	 * @param @throws Exception 
	 * @return Object
	 */
    @RequestMapping(value = "/queryPagelist", method = RequestMethod.POST)
    public @ResponseBody Object queryPagelist(SystemProfit profit, PageBean pageBean) throws Exception {
    	Result<PageResult<SystemProfit>> result = null;
    	try {
//			result = profitService.queryPageList(pageBean, profit, null);
    		result = profitService.queryProfitList(profit, pageBean);
		} catch (Exception e) {
			return new AjaxResponse(Constant.CODE_ERROR, e.getMessage());
		}
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
    public Object addOrUpdate(SystemProfit profit) throws Exception {
    	Result<Integer> result = profitService.updateProfit(profit);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
}
