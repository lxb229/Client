package com.wangzhixuan.controller;


import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.vo.WarehouseVo;
import com.wangzhixuan.service.IWarehouseService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 商品仓库 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
@Controller
@RequestMapping("/warehouse")
public class WarehouseController extends BaseController {

    @Autowired private IWarehouseService warehouseService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/warehouse/warehouseList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(WarehouseVo vo, Integer page, Integer rows, String sort,String order) {
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(vo.getCommodityName())) {
			condition.put("commodityName", vo.getCommodityName());
		}
		pageInfo.setCondition(condition);
		warehouseService.selectDataGrid(pageInfo);
		return pageInfo;
    }
    
}
