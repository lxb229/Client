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
import com.wangzhixuan.commons.utils.HumpLineUtils;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.service.ITableSeatService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 桌子手牌 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-23
 */
@Controller
@RequestMapping("/tableSeat")
public class TableSeatController extends BaseController {

    @Autowired private ITableSeatService tableSeatService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/tableSeat/tableSeatList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(String roomId, Integer page, Integer rows, String sort,String order) {
        
        if(StringUtils.isNotBlank(sort)) {
        	sort = HumpLineUtils.humpToLine(sort);
        }
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(roomId)) {
            condition.put("roomId", Integer.parseInt(roomId));
        }
        pageInfo.setCondition(condition);
        tableSeatService.selectDataGrid(pageInfo);
        return pageInfo;
    }
    
    
}
