package com.wangzhixuan.controller;

import javax.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.shiro.ShiroUser;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.Commodity;
import com.wangzhixuan.model.WarehouseOut;
import com.wangzhixuan.model.vo.WarehouseOutVo;
import com.wangzhixuan.service.ICommodityService;
import com.wangzhixuan.service.IWarehouseOutService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 出库单 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-10
 */
@Controller
@RequestMapping("/warehouseOut")
public class WarehouseOutController extends BaseController {

    @Autowired 
    private IWarehouseOutService warehouseOutService;
    @Autowired
    private ICommodityService commodityService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/warehouseOut/warehouseOutList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(WarehouseOutVo vo, Integer page, Integer rows, String sort,String order) {
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(vo.getCommodityName())) {
			condition.put("commodityName", vo.getCommodityName());
		}
		pageInfo.setCondition(condition);
		warehouseOutService.selectDataGrid(pageInfo);
		return pageInfo;
    }
    
    @PostMapping("/getAllWarehouseOut")
    @ResponseBody
    public List<WarehouseOut> getAllWarehouseOut(Integer commodity) {
		return warehouseOutService.getAllWarehouseOut(commodity);
    }
    
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage(Model model) {
    	/**获取所有的商品*/
    	List<Commodity> commodityList = commodityService.getAllCommodity();
    	model.addAttribute("commodityList", commodityList);
    	
        return "admin/warehouseOut/warehouseOutAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid WarehouseOut warehouseOut) {
    	ShiroUser user = getShiroUser();
    	if(user != null) {
    		warehouseOut.setUserId(user.getId().intValue());
    	}
    	warehouseOut.setCreateTime(new Date());
        Result result = warehouseOutService.insertWarehouseOut(warehouseOut);
        if (result != null && result.isSuccess()) {
            return renderSuccess("添加成功！");
        } else {
            return renderError(result.getMsg());
        }
    }
    
}
