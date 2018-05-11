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
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.MallProxy;
import com.wangzhixuan.service.IMallProxyService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 商城代理微信 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-19
 */
@Controller
@RequestMapping("/mallProxy")
public class MallProxyController extends BaseController {

    @Autowired private IMallProxyService mallProxyService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/mallProxy/mallProxyList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(MallProxy mallProxy, Integer page, Integer rows, String sort,String order) {
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();
		pageInfo.setCondition(condition);
		mallProxyService.selectDataGrid(pageInfo);
		return pageInfo;
    }
    
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/mallProxy/mallProxyAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid MallProxy mallProxy) {
    	
        Result b = mallProxyService.addMallProxy(mallProxy);
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
        MallProxy mallProxy = new MallProxy();
        mallProxy.setId(id);
        Result b = mallProxyService.deleteMallProxy(mallProxy);
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
        MallProxy mallProxy = mallProxyService.selectById(id);
        model.addAttribute("mallProxy", mallProxy);
        return "admin/mallProxy/mallProxyEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid MallProxy mallProxy) {
    	Result b = mallProxyService.updateMallProxy(mallProxy);
        if (b != null && b.isSuccess()) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
}
