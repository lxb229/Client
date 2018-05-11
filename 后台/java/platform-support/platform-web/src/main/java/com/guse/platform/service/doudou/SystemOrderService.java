package com.guse.platform.service.doudou;

import java.util.List;
import java.util.Map;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.doudou.SystemAgency;
import com.guse.platform.entity.doudou.SystemOrder;
import com.guse.platform.entity.system.Users;

/**
 * system_order
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface SystemOrderService extends BaseService<SystemOrder,java.lang.Integer>{

	/**
	 * 新增更新订单
	 * @Title: saveOrUpdateProduct 
	 * @param @param product
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> saveOrUpdateOrder(SystemOrder order);
	
	/**
	 * 统计订单数据
	 * @param order
	 * @param user
	 * @return
	 */
	Result<Map<String, Object>> countAmount(SystemOrder order,Users user);
	
	/**
	 * 房卡订单代理提现
	 * @return
	 */
	Result<Integer> cash(SystemOrder order,Users user);
	
	/**
	 * 生成随机订单编号
	 * @return
	 */
	String randomOrder();
	
	/**
	 * 获取代理提成比例
	 * @param amount
	 * @return
	 */
	Double getRadio(SystemAgency agency,Double amount);
	
	/**
	 * 根据每个地区每月的金额获取计算对应的提成
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getAgencyMap(List<Map<String, Object>> map);
	
	/**
	 * 获取提成金额总计
	 * @param map
	 * @return
	 */
	Map<String, Object> getAgencyCount(List<Map<String, Object>> map);
	
	/**
	 * 房卡订单代理提现
	 * @return
	 */
	Result<Integer> updateCashState(String mouth , int cityId);
	
	/**
	 * 查询订单状态
	 * @return
	 */
	Result<Integer> seachStatus(String orderNo);
	
	/**
	 * 支付宝手机网站回调
	 * @return
	 */
	Result<String> phoneNotifyUrl(Map<String, String> paramsStr);
	
	/**
	 * 支付宝手机网站回调
	 * @return
	 */
	Result<String> phoneReturnUrl(Map<String, String> paramsStr);
	
}
