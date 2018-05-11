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
import com.wangzhixuan.model.TableVialdtime;
import com.wangzhixuan.service.ITableVialdtimeService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 房间解散时长设置 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-23
 */
@Controller
@RequestMapping("/tableVialdtime")
public class TableVialdtimeController extends BaseController {

    @Autowired private ITableVialdtimeService tableVialdtimeService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/tableVialdtime/tableVialdtimeList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(TableVialdtime tableVialdtime, Integer page, Integer rows, String sort,String order) {
    	if(StringUtils.isNotBlank(sort)) {
        	sort = HumpLineUtils.humpToLine(sort);
        }
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();

        if (tableVialdtime.getVildTimes() != null) {
            condition.put("join", tableVialdtime.getVildTimes());
        }
        pageInfo.setCondition(condition);
        tableVialdtimeService.selectDataGrid(pageInfo);
        return pageInfo;
    }
    
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/tableVialdtime/tableVialdtimeAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid TableVialdtime tableVialdtime) {
        Result result = null;
        try {
			result = tableVialdtimeService.setTableVialdtime(tableVialdtime, 1);
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
        	TableVialdtime tableVialdtime = tableVialdtimeService.getTableVialdtimeById(id);
			result = tableVialdtimeService.setTableVialdtime(tableVialdtime, 3);
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
        TableVialdtime tableVialdtime = tableVialdtimeService.getTableVialdtimeById(id);
        model.addAttribute("tableVialdtime", tableVialdtime);
        return "admin/tableVialdtime/tableVialdtimeEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid TableVialdtime tableVialdtime) {
        
        Result result = null;
        try {
			result = tableVialdtimeService.setTableVialdtime(tableVialdtime, 2);
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
