package com.guse.four_one_nine.service;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.guse.four_one_nine.controller.page.ResponseAjax;
import com.guse.four_one_nine.dao.TemplateDao;
import com.guse.four_one_nine.dao.model.Template;

/** 
* @ClassName: TemplateService 
* @Description: 模版服务类
* @author Fily GUSE
* @date 2018年1月10日 上午11:07:13 
*  
*/
@Service
public class TemplateService {
	public final static Logger logger = LoggerFactory.getLogger(TemplateService.class);
	
	@Autowired
	TemplateDao dao;
	@Autowired
	AppInformService appService;
	
	/** 
	* @Title: getTemplates 
	* @Description: 获取模版信息 
	* @param @param page
	* @param @return
	* @return List<Template> 
	* @throws 
	*/
	public List<Template> getTemplates(String text) {
		String params = "1=1";
		if(StringUtils.isNotBlank(text)) {
			params += " and ("
					+ "template_content like '%"+text+"%'"
					+ " or trigger_condition like '%"+text+"%'";
			params += ")";
		}
		
		return dao.findByParams(params);
	}
	
	/** 
	* @Title: save 
	* @Description: 保存模版信息 
	* @param @param template
	* @param @return
	* @return String 
	* @throws 
	*/
	@Transactional
	public void save(Template template, ResponseAjax response) {
		// 新增模版
		if(template.getId() == null) {
			dao.addTemplate(template);
		} else {
			Template temp = template;
			template = dao.getTemplate(temp.getId());
			template.setUser_type(temp.getUser_type());
			template.setUsers(temp.getUsers());
			template.setTemplate_content(temp.getTemplate_content());
			template.setTrigger_condition(temp.getTrigger_condition());
			template.setUpdate_time(new Date());
			
			dao.save(template);
		}
		
		// 推送信息到App
		try {
			pushTemplatePulish(template);
		} catch (Exception e) {
			response.setFailure(e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
	}
	
	/** 
	* @Title: sendMessage 
	* @Description: 发送消息 
	* @param @param template
	* @param @return
	* @return String 
	* @throws 
	*/
	public void sendMessage(Template template, ResponseAjax response) {
		// 推送信息到App
		try {
			pushTemplateSend(template);
		} catch (Exception e) {
			response.setFailure(e.getMessage());
		}
	}
	
	
	/************************************************
	 ***************** 推送app数据组装 ******************
	 ***********************************************/
	/** 
	* @Description: 发布模版 
	* @param @param template
	* @param @throws Exception
	* @return void 
	* @throws 
	*/
	private void pushTemplatePulish(Template template) throws Exception{
		String business = "template_pulish";
		JSONObject params = new JSONObject();
		params.put("id", template.getId());
		params.put("target", template.getUser_type());
		params.put("users", template.getUsers());
		params.put("triggers", template.getTrigger_condition());
		params.put("timing", template.getSend_time());
		params.put("content", template.getTemplate_content());
		
		params = appService.sendMessage(business, params);
		if(params.getInt("code") != 0) {
			String msg = params.getString("msg");
			logger.error("推送消息到app出现异常,接口：[{}], 异常提示：[{}], 接口参数:[{}]", business, msg, params.toString());
			throw new Exception(msg);
		}
	}
	
	/** 
	* @Description: 直接推送消息 
	* @param @param template
	* @param @throws Exception
	* @return void 
	* @throws 
	*/
	private void pushTemplateSend(Template template) throws Exception{
		String business = "template_send";
		JSONObject params = new JSONObject();
		params.put("users", template.getUsers());
		params.put("content", template.getTemplate_content());
		
		params = appService.sendMessage(business, params);
		if(params.getInt("code") != 0) {
			String msg = params.getString("msg");
			logger.error("推送消息到app出现异常,接口：[{}], 异常提示：[{}], 接口参数:[{}]", business, msg, params.toString());
			throw new Exception(msg);
		}
	}
	
	
	

}
