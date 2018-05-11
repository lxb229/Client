package com.guse.platform.service.doudou;

import java.util.List;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.doudou.SystemProduct;
import com.guse.platform.entity.system.Users;
import com.guse.platform.vo.doudou.PayVo;

/**
 * system_product
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface SystemProductService extends BaseService<SystemProduct,java.lang.Integer>{

	/**
	 * 产品列表
	 * @Title: queryListProduct 
	 * @param @param product
	 * @param @return 
	 * @return Result<List<SystemProduct>>
	 */
	Result<List<SystemProduct>> queryListProduct(SystemProduct product);
	
	/**
	 * 商城道具列表
	 * @Title: queryListProduct 
	 * @param @param product
	 * @param @return 
	 * @return Result<List<SystemProduct>>
	 */
	Result<List<SystemProduct>> queryListStore(SystemProduct product);
	
	/**
	 * 新增更新产品
	 * @Title: saveOrUpdateProduct 
	 * @param @param product
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> saveOrUpdateProduct(SystemProduct product);
	
	/**
	 * 删除产品
	 * @Title: deleteProduct 
	 * @param @param spId
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer>  deleteProduct(Integer spId);
	
	/**
	 * 购买产品
	 * @Title: deleteProduct 
	 * @param @param productId
	 * @param @param userId
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<PayVo>  buyProduct(Integer productId, Users user);
	
	/**
	 * 微信支付购买产品
	 * @param productId 商品id
	 * @param user 用户
	 * @return
	 */
	Result<PayVo> buyProductByWX(Integer productId, Users user);
	
	/**
	 * 微信支付购买产品
	 * @param productId 商品id
	 * @param user 用户
	 * @return
	 */
	Result<PayVo> phoneBuyProductByWX(String ip, Integer productId, Users user);
	
	/**
	 * 支付宝购买产品
	 * @param productId 商品id
	 * @param user 用户
	 * @return
	 */
	Result<PayVo> phoneBuyProductByAli(Integer productId, Users user);
	
	/**
	 * 支付回调
	 * @param paramsStr
	 * @return
	 */
	String WeixinNotifyUrl(String paramsStr);
	
	/**
	 * 支付宝购买产品
	 * @param productId 商品id
	 * @param user 用户
	 * @return
	 */
	Result<PayVo> buyProductByAli(Integer productId, Users user);
	
	/**
	 * 支付回调
	 * @param paramsStr
	 * @return
	 */
	String AlipayNotifyUrl(String paramsStr);
	
}

