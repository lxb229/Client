package com.wangzhixuan.controller;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.wangzhixuan.model.Jackpot;
import com.wangzhixuan.model.OperatingStatistics;
import com.wangzhixuan.service.IJackpotService;
import com.wangzhixuan.service.IOperatingStatisticsService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 运营统计 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-29
 */
@Controller
@RequestMapping("/operatingStatistics")
public class OperatingStatisticsController extends BaseController {

	@Autowired
	private IOperatingStatisticsService operatingService;
    @Autowired 
    private IJackpotService jackpotService;
    
    @GetMapping("/manager")
    public String manager(Model model) {
    	/**获取运营统计*/
    	Map<String, Object> operatingMap = new HashMap<>();
    	operatingMap.put("id", 1);
		List<OperatingStatistics> operatingList = operatingService.selectByMap(operatingMap);
		if(operatingList != null && operatingList.size() > 0) {
			model.addAttribute("operating", operatingList.get(0));
		}
		/**获取奖池*/
		Map<String, Object> jackpotMap = new HashMap<>();
		jackpotMap.put("id", 1);
		List<Jackpot> jackpotList = jackpotService.selectByMap(jackpotMap);
		if(jackpotList != null && jackpotList.size() > 0) {
			model.addAttribute("jackpot", jackpotList.get(0));
			BigDecimal minimum = jackpotList.get(0).getMinimum();
			int getNumber = jackpotList.get(0).getGetNumber();
			BigDecimal perCapita= getNumber>0 ? minimum.divide(new BigDecimal(getNumber), 2, BigDecimal.ROUND_HALF_DOWN) : new BigDecimal("0.00"); 
			model.addAttribute("perCapita", perCapita);
		}
        return "admin/homepage/dataList";
    }
}
