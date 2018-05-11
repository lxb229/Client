package com.guse.platform.dao.doudou;


import java.util.List;

import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.doudou.SystemProduct;


/**
 * system_product
 * @see SystemProductMapper.xml
 * @author nbin
 * @date 2017年7月18日 下午2:02:28 
 * @version V1.0
 */
public interface SystemProductMapper extends  BaseMapper<SystemProduct, java.lang.Integer>{
	
	/**
	 * 根据名称获取产品
	 * @Title: getProductByName 
	 * @param @param productName
	 * @param @return 
	 * @return List<SystemProduct>
	 */
	List<SystemProduct> getProductByName(String productName);
	
	/**
	 * 获取道具商城列表
	 * @Title: selectStore 
	 * @param @param product
	 * @param @return 
	 * @return List<SystemProduct>
	 */
	List<SystemProduct> selectStore(SystemProduct product);
	
}
