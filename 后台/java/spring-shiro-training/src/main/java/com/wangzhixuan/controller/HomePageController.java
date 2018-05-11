package com.wangzhixuan.controller;



import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.wangzhixuan.commons.base.BaseController;
import com.wangzhixuan.service.IBetLogService;
import com.wangzhixuan.service.IDayPeakService;
import com.wangzhixuan.service.IJettonLogService;
import com.wangzhixuan.service.IJoinPartyLogService;
import com.wangzhixuan.service.IJoinRoomLogService;
import com.wangzhixuan.service.IPlayerService;
import com.wangzhixuan.service.IRoomService;

/**
 * <p>
 * 玩家在房间中的明细 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-15
 */
@Controller
@RequestMapping("/homepage")
public class HomePageController extends BaseController {

	@Autowired
	private IPlayerService playServie;
	@Autowired
	private IDayPeakService dayPeakService;
	@Autowired
	private IRoomService roomService;
	@Autowired
	private IJoinRoomLogService joinRoomLogService;
	@Autowired
	private IJoinPartyLogService joinPartyLogService;
	@Autowired
	private IBetLogService betLogService;
	@Autowired
	private IJettonLogService jettonLogService;
	
    @GetMapping("/manager")
    public String manager(Model model) {
    	/**获取玩家注册登陆数据*/
    	Map<String, Object> dataMap = playServie.selectPlayer();
    	model.addAttribute("dataMap", dataMap);
    	
    	/**获取玩家峰值数据*/
    	Map<String, Object> peakMap = dayPeakService.selectPeakData();
    	model.addAttribute("peakMap", peakMap);
    	
    	/**获取创建数据*/
    	Map<String, Object> roomMap = roomService.selectRoomMap();
    	model.addAttribute("roomMap", roomMap);
    	
    	/**获取加入房间数据*/
    	Map<String, Object> joinRoomMap = joinRoomLogService.selectJoinRoomMap();
    	model.addAttribute("joinRoomMap", joinRoomMap);
    	
    	/**获取入局数据*/
    	Map<String, Object> joinpartyMap = joinPartyLogService.selectJoinPartyMap();
    	model.addAttribute("joinPartyMap", joinpartyMap);
    	
    	/**获取牌局总数*/
    	Map<String, Object> allPartysMap = roomService.selectAllPartys();
    	model.addAttribute("allPartysMap", allPartysMap);
    	
    	/**获取今日牌局*/
    	Map<String, Object> todayPartysMap = joinPartyLogService.selectTodayPartys();
    	model.addAttribute("todayPartysMap", todayPartysMap);
    	
    	/**获取牌局总数*/
    	Map<String, Object> betMap = betLogService.selectBetMap();
    	model.addAttribute("betMap", betMap);
    	
    	/**获取今日牌局*/
    	Map<String, Object> jettonMap = jettonLogService.selectJettonMap();
    	model.addAttribute("jettonMap", jettonMap);
    	
        return "admin/homepage/dataList";
    }
    
}
