package com.wangzhixuan.controller;

import javax.validation.Valid;
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
import com.wangzhixuan.commons.utils.HumpLineUtils;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.CommodityType;
import com.wangzhixuan.service.ICommodityTypeService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 商品类型 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-31
 */
@Controller
@RequestMapping("/commodityType")
public class CommodityTypeController extends BaseController {

    @Autowired private ICommodityTypeService commodityTypeService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/commodityType/commodityTypeList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(CommodityType commodityType, Integer page, Integer rows, String sort,String order) {
    	if(StringUtils.isNotBlank(sort)) {
        	sort = HumpLineUtils.humpToLine(sort);
        }
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(commodityType.getTypeName())) {
			condition.put("typeName", commodityType.getTypeName());
		}
		pageInfo.setCondition(condition);
		commodityTypeService.selectDataGrid(pageInfo);
		return pageInfo;
    }
    
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/commodityType/commodityTypeAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid CommodityType commodityType) {
    	ShiroUser user = getShiroUser();
    	if(user != null) {
    		commodityType.setUserId(user.getId().intValue());
    	}
    	commodityType.setCreateTime(new Date());
        boolean b = commodityTypeService.insert(commodityType);
        if (b) {
            return renderSuccess("添加成功！");
        } else {
            return renderError("添加失败！");
        }
    }
    
    /**
     * 删除
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(Integer id) {
        Result b = commodityTypeService.deleteCommodityType(id);
        if (b != null && b.isSuccess()) {
            return renderSuccess("删除成功！");
        } else {
            return renderError("删除失败！");
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
        CommodityType commodityType = commodityTypeService.selectById(id);
        model.addAttribute("commodityType", commodityType);
        return "admin/commodityType/commodityTypeEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid CommodityType commodityType) {
        boolean b = commodityTypeService.updateById(commodityType);
        if (b) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
}
