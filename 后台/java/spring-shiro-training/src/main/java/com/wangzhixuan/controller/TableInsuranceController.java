package com.wangzhixuan.controller;

import javax.validation.Valid;

import java.util.Map;
import java.io.UnsupportedEncodingException;
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
import com.wangzhixuan.commons.utils.HumpLineUtils;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.TableInsurance;
import com.wangzhixuan.service.ITableInsuranceService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 桌子保险配置 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-23
 */
@Controller
@RequestMapping("/tableInsurance")
public class TableInsuranceController extends BaseController {

    @Autowired private ITableInsuranceService tableInsuranceService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/tableInsurance/tableInsuranceList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(TableInsurance tableInsurance, Integer page, Integer rows, String sort,String order) {
    	if(StringUtils.isNotBlank(sort)) {
        	sort = HumpLineUtils.humpToLine(sort);
        }
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();

        if (tableInsurance.getCardNum() != null) {
            condition.put("cardNum()", tableInsurance.getCardNum());
        }
        pageInfo.setCondition(condition);
        tableInsuranceService.selectDataGrid(pageInfo);
        return pageInfo;
    }
    
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/tableInsurance/tableInsuranceAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid TableInsurance tableInsurance) {
        Result result = null;
        try {
			result = tableInsuranceService.setTableInsurance(tableInsurance, 1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        if(result != null && result.isSuccess()) {
        	return renderSuccess("添加成功！");
        } else if(result != null && !result.isSuccess()) {
        	return renderError(result.getMsg());
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
    public Object delete(int id) {
        
        Result result = null;
        try {
        	TableInsurance tableInsurance = tableInsuranceService.getTableInsuranceById(id);
			result = tableInsuranceService.setTableInsurance(tableInsurance, 3);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        if(result != null && result.isSuccess()) {
        	return renderSuccess("删除成功！");
        } else if(result != null && !result.isSuccess()) {
        	return renderError(result.getMsg());
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
    public String editPage(Model model, int id) {
    	TableInsurance tableInsurance = tableInsuranceService.getTableInsuranceById(id);
        model.addAttribute("tableInsurance", tableInsurance);
        return "admin/tableInsurance/tableInsuranceEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid TableInsurance tableInsurance) {
        
        Result result = null;
        try {
			result = tableInsuranceService.setTableInsurance(tableInsurance, 2);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        if(result != null && result.isSuccess()) {
        	return renderSuccess("编辑成功！");
        } else if(result != null && !result.isSuccess()) {
        	return renderError(result.getMsg());
        } else {
        	return renderError("编辑失败！");
        }
    }
}
