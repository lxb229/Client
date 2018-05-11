package com.guse.four_one_nine.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.four_one_nine.controller.page.RequestPage;
import com.guse.four_one_nine.controller.page.ResponseAjax;
import com.guse.four_one_nine.controller.page.ResponsePage;
import com.guse.four_one_nine.dao.model.Sensitive;
import com.guse.four_one_nine.dao.model.SysUser;
import com.guse.four_one_nine.service.SensitiveService;
import com.guse.four_one_nine.service.util.AbstractSearchService;

/** 
* @ClassName: SensitiveController 
* @Description: 敏感词管理 适配器
* @author Fily GUSE
* @date 2018年1月12日 上午11:40:16 
*  
*/
@Controller
@Scope("prototype")
@RequestMapping("sensitive")
public class SensitiveController extends AbstractSearchService{
	
	@Autowired
	SensitiveService service;
	
	/** 
	* @Title: indexPage 
	* @Description: 敏感词管理首页 
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/")
	public String indexPage() {
		
		return "sensitive";
	}
	
	/** 
	* @Title: search 
	* @Description: 列表搜索 
	* @param @param page
	* @param @return
	* @return ResponsePage 
	* @throws 
	*/
	@RequestMapping("/search")
	@ResponseBody
	public ResponsePage search(RequestPage page) {
		String params = " 1=1";
		if(StringUtils.isNotBlank(page.getText())) {
			params += "and (t.sensitive like '%"+page.getText()+"%' or t.replace like '%"+page.getText()+"%')";
		}
		
		return searchPage("`sensitive` t", params, page);
	}
	@Override
	public String queryField() {
		return "t.id, t.word_group, t.replace_word, t.count_no, t.create_time";
	}
	
	/** 
	* @Title: save 
	* @Description: 保存敏感词信息 
	* @param @param data
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/save")
	@ResponseBody
	public ResponseAjax save(Sensitive data, HttpSession session) {
		ResponseAjax response = new ResponseAjax();
		if(StringUtils.isBlank(data.getWord_group())) {
			response.setFailure("敏感词不能为空");
			return response;
		}
		if(StringUtils.isBlank(data.getReplace_word())) {
			data.setReplace_word("");
		}
		
		if(response.isSuccess()) {
			SysUser user = (SysUser)session.getAttribute(SysUser.SESSION_NAME);
			if(data.getId() == null) {
				data.setCreater(user.getName());
				data.setCreate_time(new Date());
			} else {
				data.setUpdater(user.getName());
				data.setUpdate_time(new Date());
			}
			service.save(data, response);
		}
		return response;
	}
	
	/** 
	* @Description: 根据id获取敏感词信息 
	* @param @param id
	* @param @return
	* @return Sensitive 
	* @throws 
	*/
	@RequestMapping("/info/{id}")
	@ResponseBody
	public Sensitive getById(@PathVariable("id")long id) {
		
		return service.getById(id);
	}

}
