package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.SensitiveDao;
import com.guse.four_one_nine.dao.model.Sensitive;

/** 
* @ClassName: SensitiveService
* @Description: 敏感字管理
* @author: wangkai
* @date: 2018年1月8日 下午6:42:02 
*  
*/
@Service
public class SensitiveAppService {
	@Autowired
	SensitiveDao sensitiveDao;
	
	/**
	 * 敏感字使用叠加
	 * @param sensitive
	 */
	public void SensitiveCount(Sensitive sensitive) {
		sensitive = sensitiveDao.getSensitive(sensitive.getId());
		if(sensitive!=null) {
			sensitive.setCount_no(sensitive.getCount_no()+1);
			sensitiveDao.save(sensitive);
		}
	}
	
}
