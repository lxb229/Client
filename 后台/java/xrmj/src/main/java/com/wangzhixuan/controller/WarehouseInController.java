package com.wangzhixuan.controller;

import javax.validation.Valid;

import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import com.wangzhixuan.model.Supplier;
import com.wangzhixuan.model.WarehouseIn;
import com.wangzhixuan.model.vo.WarehouseINVo;
import com.wangzhixuan.service.ICommodityService;
import com.wangzhixuan.service.ISupplierService;
import com.wangzhixuan.service.IWarehouseInService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 入库单 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
@Controller
@RequestMapping("/warehouseIn")
public class WarehouseInController extends BaseController {

    @Autowired 
    private IWarehouseInService warehouseInService;
    @Autowired
    private ISupplierService supplierService;
    @Autowired
    private ICommodityService commodityService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/warehouseIn/warehouseInList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(WarehouseINVo vo, Integer page, Integer rows, String sort,String order) {
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(vo.getCommodityName())) {
			condition.put("commodityName", vo.getCommodityName());
		}
		pageInfo.setCondition(condition);
		warehouseInService.selectDataGrid(pageInfo);
		return pageInfo;
    }
    
    @PostMapping("/getAllWarehouseIn")
    @ResponseBody
    public List<WarehouseIn> getAllWarehouseIn(Integer supplier, Integer commodity) {
		return warehouseInService.getAllWarehouseIn(supplier, commodity);
    }
    
    
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage(Model model) {
    	/**获取所有的供应商*/
    	List<Supplier> supplierList = supplierService.getAllSupplier();
    	model.addAttribute("supplierList", supplierList);
    	/**获取所有的商品*/
    	List<Commodity> commodityList = commodityService.getAllCommodity();
    	model.addAttribute("commodityList", commodityList);
    	
        return "admin/warehouseIn/warehouseInAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid WarehouseINVo warehouseINVo) {
    	ShiroUser user = getShiroUser();
    	if(user != null) {
    		warehouseINVo.setCreateId(user.getId().intValue());
    	}
    	warehouseINVo.setCreateTime(new Date());
        Result result = warehouseInService.insertWarehouseIn(warehouseINVo);
        if (result != null && result.isSuccess()) {
            return renderSuccess("添加成功！");
        } else {
            return renderError(result.getMsg());
        }
    }
    
    /**
     * 编辑
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/editPage")
    public String editPage(Model model, Long id) {
    	/**获取所有的供应商*/
    	List<Supplier> supplierList = supplierService.getAllSupplier();
    	model.addAttribute("supplierList", supplierList);
    	/**获取所有的商品*/
    	List<Commodity> commodityList = commodityService.getAllCommodity();
    	model.addAttribute("commodityList", commodityList);
    	
        WarehouseIn warehouseIn = warehouseInService.selectById(id);
        model.addAttribute("warehouseIn", warehouseIn);
        return "admin/warehouseIn/warehouseInEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid WarehouseIn warehouseIn) {
    	ShiroUser user = getShiroUser();
    	if(user != null) {
    		warehouseIn.setUpdateId(user.getId().intValue());
    	}
        warehouseIn.setUpdateTime(new Date());
        boolean b = warehouseInService.updateById(warehouseIn);
        if (b) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
}
