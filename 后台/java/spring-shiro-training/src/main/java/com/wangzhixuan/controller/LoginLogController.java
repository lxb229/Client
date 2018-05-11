package com.wangzhixuan.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 玩家登陆日志 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
@Controller
@RequestMapping("/loginLog")
public class LoginLogController extends BaseController {

    
    @GetMapping("/manager")
    public String manager() {
        return "admin/loginLog/loginLogList";
    }
    
    
}
