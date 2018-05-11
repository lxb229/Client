package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.ServerOrderDao;
import com.guse.four_one_nine.dao.model.ServerOrder;

/** 
* @ClassName: ServerOrderService
* @Description: 服务订单管理
* @author: wangkai
* @date: 2018年1月9日 下午3:17:45 
*  
*/
@Service
public class ServerOrderService {

	
	@Autowired
	ServerOrderDao serverOrderDao;

	/**
	 * 新增用户评论
	 * 
	 * @param serverOrder
	 */
	public void addServerOrder(ServerOrder serverOrder ) {
		serverOrderDao.addServerOrder(serverOrder);
	}
	
	/**
	 * 更新服务订单状态
	 * @param server
	 */
	public void updateServerOrder(ServerOrder serverOrder){
		
		Integer status=serverOrder.getStatus();
		
		serverOrder = serverOrderDao.getServerOrder(serverOrder.getId());
		if(serverOrder!=null){
			serverOrder.setStatus(status);
			serverOrderDao.updateServerOrder(serverOrder);
		}
	}
}
