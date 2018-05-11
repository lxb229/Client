package com.wangzhixuan.controller;

import javax.validation.Valid;

import java.util.List;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.model.Notice;
import com.wangzhixuan.service.INoticeService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 游戏公告 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-19
 */
@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController {

    @Autowired private INoticeService noticeService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/notice/noticeList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(Notice notice, Integer page, Integer rows, String sort,String order) {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        return pageInfo;
    }
    
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/notice/noticeAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid Notice notice) {
        notice.setCreateTime(new Date());
        boolean b = noticeService.insert(notice);
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
        Notice notice = new Notice();
        boolean b = noticeService.updateById(notice);
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
        Notice notice = noticeService.selectById(id);
        model.addAttribute("notice", notice);
        return "admin/notice/noticeEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid Notice notice) {
        boolean b = noticeService.updateById(notice);
        if (b) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
}
