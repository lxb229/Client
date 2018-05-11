
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
import com.guse.platform.dao.doudou.SystemProductMapper;
import com.guse.platform.dao.system.UsersMapper;
import com.guse.platform.entity.doudou.SystemOrder;
import com.guse.platform.entity.doudou.SystemProduct;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.doudou.SystemOrderService;


@Controller
@RequestMapping("/systemOrderController")
public class SystemOrderController extends BaseController {
	
	@Autowired
	private SystemOrderService orderService;
	@Autowired
	private SystemProductMapper productMapper;
	@Autowired
	private UsersMapper usersMapper;
	
	/**
	 * 分页查询
	 * @Title: queryPagelist 
	 * @param @return
	 * @param @throws Exception 
	 * @return Object
	 */
    @RequestMapping(value = "/queryPagelist", method = RequestMethod.POST)
    public @ResponseBody Object queryPagelist(SystemOrder order,PageBean pageBean) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
    	Result<PageResult<SystemOrder>> result = null;
		result = orderService.queryPageList(pageBean, order, "so_id", user);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	for (int i = 0; i < result.getData().getList().size(); i++) {
    		SystemOrder systemOrder = result.getData().getList().get(i);
    		SystemProduct product = productMapper.selectById(systemOrder.getProductId());
    		Users users = usersMapper.selectByPrimaryKey(systemOrder.getUserId());
    		systemOrder.setProduct(product);
    		systemOrder.setUsers(users);
    		result.getData().getList().set(i, systemOrder);
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
    public @ResponseBody Object countAmount(SystemOrder order) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
    	Result<Map<String, Object>> result = null;
    	// 统计订单金额
		result = orderService.countAmount(order, user);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 代理提现
     * @return
     */
    @RequestMapping(value = "/cash", method = RequestMethod.POST)
    @ResponseBody
    public Object cash(SystemOrder order) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
    	Result<Integer> result = orderService.cash(order,user);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    
}
