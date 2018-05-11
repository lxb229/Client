
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
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.doudou.OrderService;
import com.guse.platform.vo.doudou.OrderVo;


@Controller
@RequestMapping("/orderController")
public class OrderController extends BaseController {
	@Autowired
	private OrderService orderService;
	/**
     * 购买订单列表
     * @return
     */
    @RequestMapping(value = "/queryBuyOrderList", method = RequestMethod.POST)
    public @ResponseBody Object queryBuyOrderList(OrderVo orderVo,PageBean pageBean) {
    	Result<PageResult<OrderVo>> result = null;
		try {
			String cmd = "gm_order_query_buy_order_list";
			result = orderService.queryOrderList(orderVo, pageBean, cmd);
		} catch (Exception e) {
			return new AjaxResponse(Constant.CODE_ERROR, e.getMessage());
		}
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 提现订单列表
     * @return
     */
    @RequestMapping(value = "/querySellOrderList", method = RequestMethod.POST)
    public @ResponseBody Object querySellOrderList(OrderVo orderVo,PageBean pageBean) {
    	Result<PageResult<OrderVo>> result = null;
		try {
			String cmd = "gm_order_query_sell_order_list";
			result = orderService.queryOrderList(orderVo, pageBean, cmd);
		} catch (Exception e) {
			return new AjaxResponse(Constant.CODE_ERROR, e.getMessage());
		}
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 开始处理订单
     * @return
     */
    @RequestMapping(value = "/orderStart", method = RequestMethod.POST)
    public @ResponseBody Object orderStart(OrderVo orderVo) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	Result<Integer> result = orderService.orderStart(orderVo,user);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
        return new AjaxResponse(result.getData());
    }
    /**
     * 订单处理结果
     * @return
     */
    @RequestMapping(value = "/orderComplete", method = RequestMethod.POST)
    public @ResponseBody Object orderComplete(OrderVo orderVo) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	Result<Integer> result = orderService.orderComplete(orderVo,user);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
        return new AjaxResponse(result.getData());
    }
}
