package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.AppInstalledDao;
import com.guse.four_one_nine.dao.model.AppInstalled;

/** 
* @ClassName: AppInstalledService
* @Description: 装机管理
* @author: wangkai
* @date: 2018年2月4日 下午3:17:45 
*  
*/
@Service
public class AppInstalledService {

	
	@Autowired
	AppInstalledDao appInstalledDao;

	/**
	 * 新增装机管理
	 * 
	 * @param appInstalled
	 */
	public void addAppInstalled(AppInstalled appInstalled ) {
		appInstalledDao.addAppInstalled(appInstalled);
	}
}
