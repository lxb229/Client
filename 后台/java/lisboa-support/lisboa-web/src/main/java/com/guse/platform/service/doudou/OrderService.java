package com.guse.platform.service.doudou;

import java.io.UnsupportedEncodingException;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.entity.doudou.Order;
import com.guse.platform.entity.system.Users;
import com.guse.platform.vo.doudou.OrderVo;

/**
 * order
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface OrderService extends BaseService<Order,java.lang.Integer>{

	/**
	 * 从游戏服务器获取订单集合
	 * @param cmd gm_order_query_buy_order_list：获取购买订单 gm_order_query_sell_order_list：获取提现订单
	 * @return
	 */
	public Result<PageResult<OrderVo>> queryOrderList(OrderVo orderVo, PageBean pageBean, String cmd) throws UnsupportedEncodingException;
	/**
	 * 开始处理订单
	 * @param orderVo 订单对象
	 * @return
	 */
	public Result<Integer>  orderStart(OrderVo orderVo, Users user);
	/**
	 * 订单处理结果
	 * @param orderVo 订单对象
	 * @return
	 */
	public Result<Integer>  orderComplete(OrderVo orderVo, Users user);
	
}
