package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.TemplateDao;
import com.guse.four_one_nine.dao.model.Template;

/** 
* @ClassName: TemplateService
* @Description: 模板管理
* @author: wangkai
* @date: 2018年1月8日 下午7:40:49 
*  
*/
@Service
public class TemplateAppService {

	@Autowired
	TemplateDao templateDao;
	
	/**
	 * 模板使用次数
	 * @param template
	 */
	public void templateCount(Template template) {
		template = templateDao.getTemplate(template.getId());
		if(template!=null) {
			template.setCount(template.getCount()+1);
			templateDao.templateCount(template);
		}
	}
}
