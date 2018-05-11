
package com.guse.platform.controller.doudou;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.base.BaseController;
import com.guse.platform.common.base.Constant;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.doudou.SystemProduct;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.doudou.SystemProductService;
import com.guse.platform.vo.doudou.PayVo;


@Controller
@RequestMapping("/systemProductController")
public class SystemProductController extends BaseController {
	
	@Autowired
	private SystemProductService productService;
	
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
     * 列表
     * @return
     */
    @RequestMapping(value = "/productList", method = RequestMethod.POST)
    @ResponseBody
    public Object productList(SystemProduct product) throws Exception {
    	
    	Result<List<SystemProduct>> result = productService.queryListProduct(product);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/storeList", method = RequestMethod.POST)
    @ResponseBody
    public Object storeList(SystemProduct product) throws Exception {
    	
    	Result<List<SystemProduct>> result = productService.queryListStore(product);
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
    public Object addOrUpdate(SystemProduct product) throws Exception {
    	Result<Integer> result = productService.saveOrUpdateProduct(product);
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
    public Object delete(Integer spId) throws Exception {
    	Result<Integer> result = productService.deleteProduct(spId);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 购买产品
     * @return
     */
    @RequestMapping(value = "/buylink", method = RequestMethod.POST)
    @ResponseBody
    public Object buylink(Integer spId, Model model) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
    	Result<PayVo> result = productService.buyProduct(spId, user);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
}
