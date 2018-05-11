package com.wangzhixuan.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangzhixuan.service.ISystemTaskService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 系统任务表 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-09
 */
@Controller
@RequestMapping("/gameData")
public class SystemTaskController extends BaseController {

    @Autowired private ISystemTaskService systemTaskService;
    
    
    /**
     * 添加系统任务
     * @param 
     * @return
     */
    @PostMapping("/player")
    @ResponseBody
    public Object player(HttpServletRequest request){
    	
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
	    return systemTaskService.insertPlayer(jsonBuffer.toString());
    }
    /**
     * 添加系统任务
     * @param 
     * @return
     */
    @PostMapping("/room")
    @ResponseBody
    public Object room(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
    	return systemTaskService.insertRoom(jsonBuffer.toString());
    }
    /**
     * 添加系统任务
     * @param 
     * @return
     */
    @PostMapping("/loginLog")
    @ResponseBody
    public Object loginLog(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
	    return systemTaskService.insertLoginLog(jsonBuffer.toString());
    }
    
    /**
     * 添加系统任务
     * @param 
     * @return
     */
    @PostMapping("/joinRoom")
    @ResponseBody
    public Object joinRoom(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
    	return systemTaskService.insertJoinRoom(jsonBuffer.toString());
    }
    /**
     * 添加系统任务
     * @param 
     * @return
     */
    @PostMapping("/joinParty")
    @ResponseBody
    public Object joinParty(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
    	return systemTaskService.insertJoinParty(jsonBuffer.toString());
    }
    /**
     * 添加系统任务
     * @param 
     * @return
     */
    @PostMapping("/jettonLog")
    @ResponseBody
    public Object jettonLog(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
    	return systemTaskService.insertJettonLog(jsonBuffer.toString());
    }
    /**
     * 添加系统任务
     * @param 
     * @return
     */
    @PostMapping("/betLog")
    @ResponseBody
    public Object betLog(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
    	return systemTaskService.insertBetLog(jsonBuffer.toString());
    }
    /**
     * 添加系统任务
     * @param 
     * @return
     */
    @PostMapping("/insuranceLog")
    @ResponseBody
    public Object insuranceLog(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
    	return systemTaskService.insertInsuranceLog(jsonBuffer.toString());
    }
    /**
     * 添加系统任务
     * @param 
     * @return
     */
    @PostMapping("/insuranceProfit")
    @ResponseBody
    public Object insuranceProfit(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
    	return systemTaskService.insertInsuranceProfit(jsonBuffer.toString());
    }
    /**
     * 添加系统任务
     * @param 
     * @return
     */
    @PostMapping("/playerProfit")
    @ResponseBody
    public Object playerProfit(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
    	return systemTaskService.insertPlayerProfit(jsonBuffer.toString());
    }
    /**
     * 添加系统任务
     * @param 
     * @return
     */
    @PostMapping("/roomDisappear")
    @ResponseBody
    public Object roomDisappear(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
    	return systemTaskService.insertRoomDisappear(jsonBuffer.toString());
    }
    
}
