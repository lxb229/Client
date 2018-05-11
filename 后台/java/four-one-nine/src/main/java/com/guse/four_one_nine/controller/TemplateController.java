package com.guse.four_one_nine.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.four_one_nine.common.JSONUtil;
import com.guse.four_one_nine.controller.page.ResponseAjax;
import com.guse.four_one_nine.dao.model.SysUser;
import com.guse.four_one_nine.dao.model.Template;
import com.guse.four_one_nine.service.TemplateService;

/** 
* @ClassName: TemplateController 
* @Description: 模版管理 适配器
* @author Fily GUSE
* @date 2018年1月9日 下午6:08:59 
*  
*/
@Controller
@Scope("prototype")
@RequestMapping("/template")
public class TemplateController {
	
	@Autowired
	TemplateService service;
	
	/** 
	* @Title: page 
	* @Description: 模版管理首页 
	* @param @param model
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/")
	public String page(ModelMap model, String text) {
		// 模版列表，包含查询
		model.addAttribute("templates", service.getTemplates(text));
		
		return "template-list";
	}
	
	/** 
	* @Title: publishPage 
	* @Description: 发布模版界面 
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/publish_template")
	public String publishPage() {
		
		return "template-add";
	}
	
	/** 
	* @Title: save 
	* @Description: 保存模版信息 
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/publish")
	@ResponseBody
	public ResponseAjax save(Template template,Long[] userId, HttpSession session) {
		ResponseAjax response = new ResponseAjax();
		if(StringUtils.isBlank(template.getUser_type())) {
			response.setFailure("用户类型必选");
			return response;
		}
		if("部分用户".equals(template.getUser_type())) {
			if(userId == null || userId.length == 0) {
				response.setFailure("请选择用户");
				return response;
			} else {
				String ids = JSONUtil.toJSONArray(userId).toString();
				ids = ids.substring(1, ids.length() - 1);
				template.setUsers(ids);
			}
		}
		if(StringUtils.isBlank(template.getTrigger_condition())) {
			response.setFailure("触发条件必选");
			return response;
		}
		if(StringUtils.isBlank(template.getTemplate_content())) {
			response.setFailure("模版内容必填");
			return response;
		}
		
		if(response.isSuccess()) {
			SysUser user = (SysUser)session.getAttribute(SysUser.SESSION_NAME);
			if(template.getId() == null) {
				template.setCreater(user.getName());
				template.setCreate_time(new Date());
			} else {
				template.setUpdate_time(new Date());
				template.setUpdater(user.getName());
			}
			service.save(template, response);
		}
		return response;
	}
	
	/** 
	* @Title: publishMessage 
	* @Description: 推送消息 页面
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/release_page/")
	public String publishMessage() {
		
		return "template-release";
	}
	
	/** 
	* @Title: sendMessage 
	* @Description: 发送推送消息 
	* @param @param template
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/send_template")
	@ResponseBody
	public ResponseAjax sendMessage(Template template, Long[] userId) {
		ResponseAjax response = new ResponseAjax();
		if(StringUtils.isBlank(template.getUser_type())) {
			response.setFailure("用户类型必选");
			return response;
		}
		if("部分用户".equals(template.getUser_type())) {
			if(userId == null || userId.length == 0) {
				response.setFailure("请选择用户");
				return response;
			} else {
				String ids = JSONUtil.toJSONArray(userId).toString();
				ids = ids.substring(1, ids.length() - 1);
				template.setUsers(ids);
			}
		}
		if(StringUtils.isBlank(template.getTemplate_content())) {
			response.setFailure("模版内容必填");
			return response;
		}
		service.sendMessage(template, response);
		
		return response;
	}

}
