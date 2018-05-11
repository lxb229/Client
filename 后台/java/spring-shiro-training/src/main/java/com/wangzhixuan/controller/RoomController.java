package com.wangzhixuan.controller;

import javax.validation.Valid;

import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.utils.HumpLineUtils;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.Room;
import com.wangzhixuan.service.IRoomService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 房间表 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
@Controller
@RequestMapping("/room")
public class RoomController extends BaseController {

    @Autowired private IRoomService roomService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/room/roomList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(Room room, Integer page, Integer rows, String sort,String order) {
    	 if(StringUtils.isNotBlank(sort)) {
         	sort = HumpLineUtils.humpToLine(sort);
         }
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(room.getHouseOwner())) {
            condition.put("houseOwner", room.getHouseOwner());
        }
        if (room.getRoomId()!=null) {
            condition.put("roomId", room.getRoomId());
        }
        pageInfo.setCondition(condition);
        roomService.selectDataGrid(pageInfo);
        return pageInfo;
    }
    
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/room/roomAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid Room room) {
//        room.setCreateTime(new Date());
//        room.setUpdateTime(new Date());
//        room.setDeleteFlag(0);
        boolean b = roomService.insert(room);
        if (b) {
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
    public Object delete(Long id) {
        Room room = new Room();
//        room.setId(id);
//        room.setUpdateTime(new Date());
//        room.setDeleteFlag(1);
        boolean b = roomService.updateById(room);
        if (b) {
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
        Room room = roomService.selectById(id);
        model.addAttribute("room", room);
        return "admin/room/roomEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid Room room) {
//        room.setUpdateTime(new Date());
        boolean b = roomService.updateById(room);
        if (b) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
}
