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
import com.wangzhixuan.commons.utils.HumpLineUtils;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.PlayRoom;
import com.wangzhixuan.service.IPlayRoomService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 玩家在房间中的明细 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-15
 */
@Controller
@RequestMapping("/playRoom")
public class PlayRoomController extends BaseController {

    @Autowired private IPlayRoomService playRoomService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/playRoom/playRoomList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(PlayRoom playRoom, Integer page, Integer rows, String sort,String order) {
    	if(StringUtils.isNotBlank(sort)) {
        	sort = HumpLineUtils.humpToLine(sort);
        }
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(playRoom.getPlayId())) {
            condition.put("playId", playRoom.getPlayId());
        }
        if(playRoom.getRoomId() != null ) {
        	condition.put("roomId", playRoom.getRoomId());
        }
        pageInfo.setCondition(condition);
        playRoomService.selectDataGrid(pageInfo);
        return pageInfo;
    }
    
}
