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
import com.wangzhixuan.model.PropLog;
import com.wangzhixuan.service.IPropLogService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-11
 */
@Controller
@RequestMapping("/propLog")
public class PropLogController extends BaseController {

    @Autowired private IPropLogService propLogService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/propLog/propLogList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(PropLog propLog, Integer page, Integer rows, String sort,String order) {
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(propLog.getStartNo())) {
            condition.put("startNo", propLog.getStartNo());
        }
        pageInfo.setCondition(condition);
        propLogService.selectDataGrid(pageInfo);
        return pageInfo;
    }
    
}
