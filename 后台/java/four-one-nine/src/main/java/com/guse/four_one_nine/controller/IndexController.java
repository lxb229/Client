package com.guse.four_one_nine.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guse.four_one_nine.common.JSONUtil;
import com.guse.four_one_nine.service.IndexService;

/** 
* @ClassName: IndexController 
* @Description: 首页信息 适配器
* @author Fily GUSE
* @date 2018年1月5日 下午2:56:19 
*  
*/
@Controller
@Scope("prototype")
@RequestMapping("index")
public class IndexController {
	
	@Autowired
	IndexService service;
	
	@RequestMapping("/")
	public String index(ModelMap model) {
		
		return "platform";
	}
	
	/** 
	* @Description: 首页统计信息 
	* @param @param model
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/index_count")
	public String indexCountPage(ModelMap model) {
		// 用户信息统计
		Map<String, Integer> userCount = service.userCount();
		// 交易信息统计
		Map<String, Integer> dealCount = service.dealCount();
		
		/* 页面显示信息 */
		// 平台总人数
		model.addAttribute("userNum", userCount.get("register_num"));
		// 下载量
		model.addAttribute("installerNum", userCount.get("appinstaller_num"));
		// 用户来源统计
		List<Map<String, Integer>> sourceList = service.countSource();
		if(sourceList != null && sourceList.size() > 0) {
			model.addAttribute("source", JSONUtil.toJSONArray(sourceList));
		}
		// 平台流水
		model.addAttribute("platformStatements", dealCount.get("platform_statements_count"));
		// 卖家人数
		model.addAttribute("sellerNum", userCount.get("seller_num"));
		// 服务数量
		model.addAttribute("serverNum", dealCount.get("newly_server_count"));
		// 成交量
		model.addAttribute("orderNum", dealCount.get("order_num"));
		// 单笔最大提现金额
		model.addAttribute("maxCash", dealCount.get("cash_max"));
		// 用户分布统计
		model.addAttribute("city", service.countCity());
		// 用户年龄统计
		String ages[] = {"20", "20-25", "26-35", "35-"};
		model.addAttribute("age", service.countAge(ages));
		// 买家交易排行榜
		model.addAttribute("rankingBuy", service.rankingBuy());
		// 卖家交易排行榜
		model.addAttribute("rankingSell", service.rankingSell());
		
		return "platform-count";
	}
}
