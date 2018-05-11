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
import com.wangzhixuan.model.RedPacketLog;
import com.wangzhixuan.model.vo.RedPacketVo;
import com.wangzhixuan.model.vo.RedVo;
import com.wangzhixuan.service.IRedPacketLogService;
import com.wangzhixuan.commons.base.BaseController;
import com.wangzhixuan.commons.base.Constant;

/**
 * <p>
 * 红包抽奖记录 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-29
 */
@Controller
@RequestMapping("/redPacketLog")
public class RedPacketLogController extends BaseController {

    @Autowired private IRedPacketLogService redPacketLogService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/redPacketLog/redPacketLogList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(RedPacketLog redPacketLog, Integer page, Integer rows, String sort,String order) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(redPacketLog.getStartNo())) {
			condition.put("startNo", redPacketLog.getStartNo());
		}
		pageInfo.setCondition(condition);
		redPacketLogService.selectDataGrid(pageInfo);
		return pageInfo;
    }
    
    @PostMapping("/receiveRedPacket")
    @ResponseBody
    public RedVo receiveRedPacket(String start_no, int redPacketNo, Integer round, Integer receiveNumber) {
    	RedVo redvo = new RedVo();
    	RedPacketVo vo = redPacketLogService.alreadyRedPacket(start_no, redPacketNo, round, receiveNumber);
    	if(vo != null) {
    		if(vo.getMoneyType() == Constant.MONEY_GOLD) {
    			redvo.setGoldMoney(vo.getAmount());
    			redvo.setSilverMoney(0);
    		} else {
    			redvo.setSilverMoney(vo.getAmount());
    			redvo.setGoldMoney(0);
    		}
    		return redvo;
    	} else {
    		vo = redPacketLogService.receiveRedPacket(start_no, redPacketNo, round, receiveNumber);
    		if(vo.getMoneyType() == Constant.MONEY_GOLD) {
    			redvo.setGoldMoney(vo.getAmount());
    			redvo.setSilverMoney(0);
    		} else {
    			redvo.setSilverMoney(vo.getAmount());
    			redvo.setGoldMoney(0);
    		}
    		return redvo;
    	}
    }
    
    
}
