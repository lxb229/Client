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
import com.wangzhixuan.commons.utils.HumpLineUtils;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.Jackpot;
import com.wangzhixuan.service.IJackpotService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 奖池 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-29
 */
@Controller
@RequestMapping("/jackpot")
public class JackpotController extends BaseController {

    @Autowired private IJackpotService jackpotService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/jackpot/jackpotList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(Jackpot jackpot, Integer page, Integer rows, String sort,String order) {
    	if(StringUtils.isNotBlank(sort)) {
        	sort = HumpLineUtils.humpToLine(sort);
        }
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();
        pageInfo.setCondition(condition);
        jackpotService.selectDataGrid(pageInfo);
        return pageInfo;
    }
    
    /**
     * 编辑
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/editPage")
    public String editPage(Model model, Long id) {
        Jackpot jackpot = jackpotService.selectById(id);
        model.addAttribute("jackpot", jackpot);
        return "admin/jackpot/jackpotEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid Jackpot jackpot) {
    	if(jackpot.getBonus() < 0) {
    		return renderError("金额设置错误");
    	}
        boolean b = jackpotService.updateById(jackpot);
        if (b) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
}
