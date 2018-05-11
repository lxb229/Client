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
import com.wangzhixuan.model.CommodityLv;
import com.wangzhixuan.service.ICommodityLvService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 商品等级 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
@Controller
@RequestMapping("/commodityLv")
public class CommodityLvController extends BaseController {

    @Autowired private ICommodityLvService commodityLvService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/commodityLv/commodityLvList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(CommodityLv commodityLv, Integer page, Integer rows, String sort,String order) {
    	if(StringUtils.isNotBlank(sort)) {
        	sort = HumpLineUtils.humpToLine(sort);
        }
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(commodityLv.getLvName())) {
			condition.put("lvName", commodityLv.getLvName());
		}
		pageInfo.setCondition(condition);
		commodityLvService.selectDataGrid(pageInfo);
		return pageInfo;
    }
    
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/commodityLv/commodityLvAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid CommodityLv commodityLv) {
    	ShiroUser user = getShiroUser();
    	if(user != null) {
    		commodityLv.setUserId(user.getId().intValue());
    	}
    	commodityLv.setCreateTime(new Date());
        boolean b = commodityLvService.insert(commodityLv);
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
    	Result b = commodityLvService.deleteCommodityLv(id);
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
        CommodityLv commodityLv = commodityLvService.selectById(id);
        model.addAttribute("commodityLv", commodityLv);
        return "admin/commodityLv/commodityLvEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid CommodityLv commodityLv) {
        boolean b = commodityLvService.updateById(commodityLv);
        if (b) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
}
