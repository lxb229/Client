package com.wangzhixuan.controller;

import javax.validation.Valid;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.shiro.ShiroUser;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.Investment;
import com.wangzhixuan.model.vo.InvestmentVo;
import com.wangzhixuan.service.IInvestmentService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 投资奖池 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-29
 */
@Controller
@RequestMapping("/investment")
public class InvestmentController extends BaseController {

    @Autowired private IInvestmentService investmentService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/investment/investmentList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(InvestmentVo vo, Integer page, Integer rows, String sort,String order) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(vo.getName())) {
			condition.put("name", vo.getName());
		}
		pageInfo.setCondition(condition);
		investmentService.selectDataGrid(pageInfo);
		return pageInfo;
    }
    
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/investment/investmentAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid Investment investment) {
    	Result result = null;
    	ShiroUser user = getShiroUser();
    	if(user != null) {
    		investment.setUserId(user.getId().intValue());
    		investment.setCreateTime(new Date());
    		result = investmentService.insertInvestment(investment);
    	}
        if (result != null && result.isSuccess()) {
            return renderSuccess("添加成功！");
        } else {
            return renderError("添加失败！");
        }
    }
    
}
