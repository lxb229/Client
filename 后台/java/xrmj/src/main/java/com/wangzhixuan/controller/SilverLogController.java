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
import com.wangzhixuan.model.vo.SilverLogVo;
import com.wangzhixuan.service.ISilverLogService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 银币抽奖记录 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-11
 */
@Controller
@RequestMapping("/silverLog")
public class SilverLogController extends BaseController {

    @Autowired 
    private ISilverLogService silverLogService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/silverLog/silverLogList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(SilverLogVo vo, Integer page, Integer rows, String sort,String order) {
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(vo.getStartNo())) {
            condition.put("startNo", vo.getStartNo());
        }
        pageInfo.setCondition(condition);
        silverLogService.selectDataGrid(pageInfo);
        return pageInfo;
    }
    
}
