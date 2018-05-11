package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.ServerDao;
import com.guse.four_one_nine.dao.model.Server;

/** 
* @ClassName: ServerCommentService
* @Description: 服务管理
* @author: wangkai
* @date: 2018年1月9日 下午3:17:45 
*  
*/
@Service
public class ServerAppService {

	
	@Autowired
	ServerDao serverDao;

	/**
	 * 新增服务
	 * 
	 * @param serverComment
	 */
	public void addServer(Server server ) {
		serverDao.addServer(server);
	}
	
	/**
	 * 更新服务信息
	 * @param server
	 */
	public void updateServer(Server server){
		server = serverDao.getServer(server.getId());
		if(server!=null){
			serverDao.updateServer(server);
		}
	}
	
}
