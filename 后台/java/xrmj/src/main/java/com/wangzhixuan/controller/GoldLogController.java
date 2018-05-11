package com.wangzhixuan.controller;

import javax.validation.Valid;

import java.util.Map;
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
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.GoldLog;
import com.wangzhixuan.model.vo.GoldLogVo;
import com.wangzhixuan.service.IGoldLogService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 金币兑换商品记录 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-11
 */
@Controller
@RequestMapping("/goldLog")
public class GoldLogController extends BaseController {

    @Autowired 
    private IGoldLogService goldLogService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/goldLog/goldLogList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(GoldLogVo vo, Integer page, Integer rows, String sort,String order) {
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(vo.getStartNo())) {
            condition.put("startNo", vo.getStartNo());
        }
        pageInfo.setCondition(condition);
        goldLogService.selectDataGrid(pageInfo);
        return pageInfo;
    }
    
    
    /**
     * 编辑
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/editPage")
    public String editPage(Model model, Integer id) {
        GoldLogVo goldLog = goldLogService.selectGoldLogVoBy(id);
        model.addAttribute("goldLog", goldLog);
        return "admin/goldLog/goldLogEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid GoldLog goldLog) {
    	ShiroUser user = getShiroUser();
        Result b = goldLogService.disposeGoldLog(goldLog, user);
        if (b != null && b.isSuccess()) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError(b.getMsg());
        }
    }
}
