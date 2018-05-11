package com.wangzhixuan.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangzhixuan.service.ISilverJackpotService;
import com.wangzhixuan.service.ISystemTaskService;
import com.wangzhixuan.service.IWarehouseOutService;
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

    @Autowired 
    private ISystemTaskService systemTaskService;
    @Autowired 
    private ISilverJackpotService silverJackpotService;
    @Autowired
    private IWarehouseOutService warehouseOutService;
    
    /**
     * 添加玩家任务
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
     * 添加订单任务
     * @param 
     * @return
     */
    @PostMapping("/order")
    @ResponseBody
    public Object order(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
    	return systemTaskService.insertOrder(jsonBuffer.toString());
    }
    /**
     * 添加游戏任务
     * @param 
     * @return
     */
    @PostMapping("/gamePlayer")
    @ResponseBody
    public Object gamePlayer(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
	    return systemTaskService.insertGamePlayer(jsonBuffer.toString());
    }
    
    /**
     * 添加玩家祝福值任务
     * @param 
     * @return
     */
    @PostMapping("/wish")
    @ResponseBody
    public Object wish(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
	    return systemTaskService.insertPlayerWish(jsonBuffer.toString());
    }
    

    /**
     * 添加麻将馆任务
     * @param 
     * @return
     */
    @PostMapping("/mahjong")
    @ResponseBody
    public Object mahjong(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
	    return systemTaskService.insertMahjong(jsonBuffer.toString());
    }
    
    /**
     * 添加麻将馆成员任务
     * @param 
     * @return
     */
    @PostMapping("/mahjongPlayer")
    @ResponseBody
    public Object mahjongPlayer(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
	    return systemTaskService.insertMahjongPlayer(jsonBuffer.toString());
    }
    
    /**
     * 添加麻将馆房卡任务
     * @param 
     * @return
     */
    @PostMapping("/mahjongCard")
    @ResponseBody
    public Object mahjongCard(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
	    return systemTaskService.insertMahjongCard(jsonBuffer.toString());
    }
    
    /**
     * 添加麻将馆战斗任务
     * @param 
     * @return
     */
    @PostMapping("/mahjongCombat")
    @ResponseBody
    public Object mahjongCombat(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
	    return systemTaskService.insertMahjongCombat(jsonBuffer.toString());
    }
    
    /**
     * 玩家使用幸运值事件
     * @param 
     * @return
     */
    @PostMapping("/userLuck")
    @ResponseBody
    public Object userLuck(HttpServletRequest request) {
    	StringBuffer jsonBuffer = getRequestJsonBuffer(request);
	    return systemTaskService.insertUserLuck(jsonBuffer.toString());
    }
    
    /**
     * 玩家银币抽奖
     * @param cmd
     * @param start_no
     * @return
     */
    @GetMapping("/lottery")
    @ResponseBody
    public Object lottery(Integer cmd, String start_no) {
    	return silverJackpotService.lottery(cmd, start_no);
    }
    
    /**
     * 玩家金币兑换
     * @param cmd
     * @param start_no
     * @return
     */
    @GetMapping("/exchange")
    @ResponseBody
    public Object exchange(Integer cmd, String start_no, Integer itemId, 
    		String name, String phone, String addr) {
    	return warehouseOutService.exchange(cmd, start_no, itemId, name, phone, addr);
    }
    
    
}
