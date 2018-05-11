package com.wangzhixuan.controller;


import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangzhixuan.service.ISilverJackpotService;
import com.wangzhixuan.commons.base.BaseController;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.utils.HumpLineUtils;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.SilverJackpot;

/**
 * <p>
 * 银币抽奖奖池 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
@Controller
@RequestMapping("/silverJackpot")
public class SilverJackpotController extends BaseController {

    @Autowired private ISilverJackpotService silverJackpotService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/silverJackpot/silverJackpotList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(SilverJackpot silverJackpot, Integer page, Integer rows, String sort,String order) {
    	if(StringUtils.isNotBlank(sort)) {
        	sort = HumpLineUtils.humpToLine(sort);
        }
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(silverJackpot.getAwardName())) {
			condition.put("awardName", silverJackpot.getAwardName());
		}
		pageInfo.setCondition(condition);
		silverJackpotService.selectDataGrid(pageInfo);
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
        SilverJackpot silverJackpot = silverJackpotService.selectById(id);
        model.addAttribute("silverJackpot", silverJackpot);
        return "admin/silverJackpot/silverJackpotEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid SilverJackpot silverJackpot) {
        boolean b = silverJackpotService.updateById(silverJackpot);
        if (b) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
    
    @GetMapping("/lottery")
    @ResponseBody
    public Object lottery(Integer cmd, String start_no) {
    	
    	return silverJackpotService.lottery(cmd, start_no);
    }
}
