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
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.HumpLineUtils;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.service.IPlayerService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 玩家表 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
@Controller
@RequestMapping("/player")
public class PlayerController extends BaseController {

    @Autowired private IPlayerService playerService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/player/playerList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(Player player, Integer page, Integer rows, String sort,String order) {
        if(StringUtils.isNotBlank(sort)) {
        	sort = HumpLineUtils.humpToLine(sort);
        }
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(player.getStartNo())) {
            condition.put("startNo", player.getStartNo());
        }
        pageInfo.setCondition(condition);
        playerService.selectDataGrid(pageInfo);
        return pageInfo;
    }
    
    /**
     * 禁/启用
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(Integer id, Integer status) {
        
        Result b = playerService.banOrEnable(id, status);
        if (b != null && b.isSuccess()) {
            return renderSuccess("操作成功！");
        } else {
            return renderError("操作失败！");
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
        Player player = playerService.selectById(id);
        model.addAttribute("player", player);
        return "admin/player/playerEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid Player player) {
        boolean b = playerService.updateById(player);
        if (b) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
}
