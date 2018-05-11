package com.wangzhixuan.controller;

import javax.validation.Valid;

import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.shiro.ShiroUser;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.SystemOrder;
import com.wangzhixuan.model.vo.SystemOrderVo;
import com.wangzhixuan.service.ISystemOrderService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 系统订单 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-29
 */
@Controller
@RequestMapping("/systemOrder")
public class SystemOrderController extends BaseController {

    @Autowired private ISystemOrderService systemOrderService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/systemOrder/systemOrderList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(SystemOrderVo vo, Integer page, Integer rows, String sort,String order) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(vo.getStartNo())) {
			condition.put("startNo", vo.getStartNo());
		}
		if(vo.getPurchaseType() != null && vo.getPurchaseType() != 0) {
			condition.put("purchaseType", vo.getPurchaseType());
		}
		if(vo.getPayType() != null && vo.getPayType() != 0) {
			condition.put("payType", vo.getPayType());
		}
		pageInfo.setCondition(condition);
		systemOrderService.selectDataGrid(pageInfo);
		return pageInfo;
    }
    
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/systemOrder/systemOrderAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid SystemOrder systemOrder) {
    	Result result = null;
    	ShiroUser user = getShiroUser();
    	if(user != null) {
    		systemOrder.setUserId(user.getId().intValue());
    		systemOrder.setCreateTime(new Date());
    		result = systemOrderService.addOrder(systemOrder);
    	}
        if (result != null && result.isSuccess()) {
            return renderSuccess("添加成功！");
        } else {
            return renderError(result.getMsg());
        }
    }
    
}
