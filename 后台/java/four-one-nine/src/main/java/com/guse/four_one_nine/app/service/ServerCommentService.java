package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.ServerCommentDao;
import com.guse.four_one_nine.dao.model.ServerComment;

/** 
* @ClassName: ServerCommentService
* @Description: 服务评论管理
* @author: wangkai
* @date: 2018年1月9日 下午3:17:45 
*  
*/
@Service
public class ServerCommentService {

	
	@Autowired
	ServerCommentDao serverCommentDao;

	/**
	 * 新增用户评论
	 * 
	 * @param serverComment
	 */
	public void addServerApply(ServerComment serverComment ) {
		serverCommentDao.addServerComment(serverComment);
	}
}
