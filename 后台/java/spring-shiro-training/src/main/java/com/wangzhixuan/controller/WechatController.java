package com.wangzhixuan.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.model.Wechat;
import com.wangzhixuan.service.IWechatService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2017-12-18
 */
@Controller
@RequestMapping("/wechat")
public class WechatController extends BaseController {

    @Autowired private IWechatService wechatService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/wechat/wechatList";
    }
    
    @GetMapping("/list")
    public String list() {
        return "admin/wechat/wechatAdd";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(Wechat wechat, Integer page, Integer rows, String sort,String order) {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        wechatService.selectDataGrid(pageInfo);
        return pageInfo;
    }
    
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/wechat/wechatAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid Wechat wechat) {
        boolean b = wechatService.insert(wechat);
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
    public Object delete(Integer id) {
        boolean b = wechatService.deleteById(id);
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
        Wechat wechat = wechatService.selectById(id);
        model.addAttribute("wechat", wechat);
        return "admin/wechat/wechatEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid Wechat wechat) {
        boolean b = wechatService.updateById(wechat);
        if (b) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
}
