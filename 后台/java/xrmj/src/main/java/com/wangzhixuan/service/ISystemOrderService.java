package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.SystemOrder;
import com.wangzhixuan.model.SystemTask;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 系统订单 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public interface ISystemOrderService extends IService<SystemOrder> {
	
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 处理任务中的订单数据
	 * @param task
	 */
	void taskOrder(SystemTask task);
	
	/**
	 * 处理订单
	 * @param obj 玩家对象
	 * @return
	 */
	Result processingOrder(JSONObject obj);
	
	/**
	 * 线下新增订单
	 * @param order
	 * @return
	 */
	Result addOrder(SystemOrder order);
	
	/**
	 * 新增一个订单
	 * @param player
	 * @return
	 */
	Result insertOrder(SystemOrder order);
	
	/**
	 * @param type 生成类型(1=订单编号 2=商品编号 )
	 * 生成订单编号
	 * @return
	 */
	String randomOrder(int type);
}
