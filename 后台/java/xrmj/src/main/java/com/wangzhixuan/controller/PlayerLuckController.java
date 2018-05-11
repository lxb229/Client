package com.wangzhixuan.controller;

import javax.validation.Valid;

import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.shiro.ShiroUser;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.PlayerLuck;
import com.wangzhixuan.model.vo.PlayerLuckVo;
import com.wangzhixuan.service.IPlayerLuckService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 玩家幸运值 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-18
 */
@Controller
@RequestMapping("/playerLuck")
public class PlayerLuckController extends BaseController {

    @Autowired private IPlayerLuckService playerLuckService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/playerLuck/playerLuckList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(PlayerLuckVo vo, Integer page, Integer rows, String sort,String order) {
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(vo.getStartNo())) {
            condition.put("startNo", vo.getStartNo());
        }
        pageInfo.setCondition(condition);
        playerLuckService.selectDataGrid(pageInfo);
        return pageInfo;
    }
    
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/playerLuck/playerLuckAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid PlayerLuck playerLuck) {
    	ShiroUser user = getShiroUser();
    	if(user != null) {
    		playerLuck.setUserId(user.getId().intValue());
    	}
        playerLuck.setCreateTime(new Date());
        Result b = playerLuckService.insertPlayerLuck(playerLuck);
        if (b != null && b.isSuccess()) {
            return renderSuccess("添加成功！");
        } else {
            return renderError("添加失败！");
        }
    }
    
    /**
     * 删除
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(Integer id) {
        Result b = playerLuckService.deletePlayerLuck(id);
        if (b != null && b.isSuccess()) {
            return renderSuccess("删除成功！");
        } else {
            return renderError("删除失败！");
        }
    }
    
    /**
     * 编辑
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/editPage")
    public String editPage(Model model, Long id) {
        PlayerLuck playerLuck = playerLuckService.selectById(id);
        model.addAttribute("playerLuck", playerLuck);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /**幸运开始时间*/
        String luckStart = sdf.format(playerLuck.getLuckStart());
        model.addAttribute("luckStart", luckStart);
        /**幸运结束时间*/
        String luckEnd = sdf.format(playerLuck.getLuckEnd());
        model.addAttribute("luckEnd", luckEnd);
        
        return "admin/playerLuck/playerLuckEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid PlayerLuck playerLuck) {
        Result b = playerLuckService.updatePlayerLuck(playerLuck);
        if (b != null && b.isSuccess()) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
}
