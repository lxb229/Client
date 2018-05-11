package com.wangzhixuan.controller;

import javax.validation.Valid;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.wangzhixuan.model.CommodityLv;
import com.wangzhixuan.model.CommodityType;
import com.wangzhixuan.model.vo.CommodityVo;
import com.wangzhixuan.service.ICommodityLvService;
import com.wangzhixuan.service.ICommodityService;
import com.wangzhixuan.service.ICommodityTypeService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 商品 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
@Controller
@RequestMapping("/commodity")
public class CommodityController extends BaseController {

    @Autowired 
    private ICommodityService commodityService;
    @Autowired
    private ICommodityTypeService typeService;
    @Autowired
    private ICommodityLvService lvService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/commodity/commodityList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(CommodityVo vo, Integer page, Integer rows, String sort,String order) {
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(vo.getCommodityName())) {
			condition.put("commodityName", vo.getCommodityName());
		}
		pageInfo.setCondition(condition);
		commodityService.selectDataGrid(pageInfo);
		return pageInfo;
    }
    
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage(Model model) {
    	/**获取所有的有效商品类型*/
    	List<CommodityType> typeList = typeService.getAllTyep();
    	model.addAttribute("typeList", typeList);
    	/**获取所有的有效商品等级*/
    	List<CommodityLv> lvList = lvService.getAllLv();
    	model.addAttribute("lvList", lvList);
        return "admin/commodity/commodityAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid Commodity commodity) {
    	ShiroUser user = getShiroUser();
    	if(user != null) {
    		commodity.setUserId(user.getId().intValue());
    	}
    	commodity.setCreateTime(new Date());
        Result result = commodityService.insertCommodity(commodity);
        if (result != null && result.isSuccess()) {
            return renderSuccess("添加成功！");
        } else {
            return renderError("添加失败！");
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
    	/**获取所有的有效商品类型*/
    	List<CommodityType> typeList = typeService.getAllTyep();
    	model.addAttribute("typeList", typeList);
    	/**获取所有的有效商品等级*/
    	List<CommodityLv> lvList = lvService.getAllLv();
    	model.addAttribute("lvList", lvList);
    	
        Commodity commodity = commodityService.selectById(id);
        model.addAttribute("commodity", commodity);
        return "admin/commodity/commodityEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid Commodity commodity) {
        boolean b = commodityService.updateById(commodity);
        if (b) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
}
