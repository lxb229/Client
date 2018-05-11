package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.ServerLikeDao;
import com.guse.four_one_nine.dao.model.ServerLike;

/** 
* @ClassName: ServerLikeService
* @Description: 服务点赞管理
* @author: wangkai
* @date: 2018年1月9日 下午3:17:45 
*  
*/
@Service
public class ServerLikeService {

	
	@Autowired
	ServerLikeDao serverLikeDao;

	/**
	 * 新增用户点赞
	 * 
	 * @param serverLike
	 */
	public void addServerLike(ServerLike serverLike ) {
		serverLikeDao.addServerLike(serverLike);
	}
}
