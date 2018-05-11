package com.guse.platform.common.aop;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.guse.platform.common.base.Constant;
import com.guse.platform.common.utils.AddressUtils;
import com.guse.platform.entity.system.LoginLogs;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.system.LoginLogsService;

/**
 * 登录 log
 * @author nbin
 * @date 2017年8月24日 下午3:19:22 
 * @version V1.0
 */
@Component
@Aspect
public class SystemLoginLogsAspect implements ThrowsAdvice {
	
	private static final Logger logger = LoggerFactory.getLogger(SystemLoginLogsAspect.class);
	@Autowired
	private LoginLogsService loginLogsService;
	
	@AfterReturning(value = "execution(* com.guse.platform.controller.system.LoginController.*doLogin*(..))",argNames = "retVal", returning = "retVal")
	public void doAfterReturning(JoinPoint jp, Object retVal) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		Users user = (Users) session.getAttribute(Constant.SESSION_LOGIN_USER);
		String ip = AddressUtils.getIpAddr(request);
		if (user != null) {
			try {
				LoginLogs loginLogs = new LoginLogs();
		        loginLogs.setHeader(request.getHeader("User-Agent"));
		        getLoginlogs(ip, "utf-8", loginLogs, user.getLoginName());
		        loginLogsService.insert(loginLogs);
			} catch (Exception e) {
				logger.error("登录异常信息:"+ e.getMessage());
			}

		} else {
			logger.info("ip:"+ ip +" 登录错误");
		}
	}
	private static void getLoginlogs(String ip, String encodingString, LoginLogs loginLogs, String username) {
        loginLogs.setLoginName(username);
        loginLogs.setLoginTime(new Date());
        loginLogs.setIp(ip);
        AddressUtils.getAddresses("ip=" + ip, encodingString, loginLogs);
    }
	
}
