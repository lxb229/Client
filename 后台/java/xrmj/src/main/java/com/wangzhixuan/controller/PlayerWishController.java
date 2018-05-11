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
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.PlayerWish;
import com.wangzhixuan.service.IPlayerWishService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 玩家祝福值 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-29
 */
@Controller
@RequestMapping("/playerWish")
public class PlayerWishController extends BaseController {

    @Autowired private IPlayerWishService playerWishService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/playerWish/playerWishList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(PlayerWish playerWish, Integer page, Integer rows, String sort,String order) {
    	 PageInfo pageInfo = new PageInfo(page, rows, sort, order);
         Map<String, Object> condition = new HashMap<String, Object>();

         if (StringUtils.isNotBlank(playerWish.getStartNo())) {
             condition.put("startNo", playerWish.getStartNo());
         }
         pageInfo.setCondition(condition);
         playerWishService.selectDataGrid(pageInfo);
         return pageInfo;
    }
    
    /**
	 * 处理玩家祝福值
	 * @param obj 玩家祝福值对象
	 * @return
	 */
    @PostMapping("/processingPlayerWish")
    @ResponseBody
    public Result processingPlayerWish(Integer addWish, String start_no, Integer gold, Integer silver) {
    	return playerWishService.processingPlayerWish(addWish, start_no, gold, silver);
    }
    
}
