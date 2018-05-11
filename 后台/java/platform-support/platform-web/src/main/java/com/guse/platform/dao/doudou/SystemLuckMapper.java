package com.guse.platform.dao.doudou;


import java.util.List;

import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.doudou.SystemLuck;


/**
 * system_luck
 * @see SystemLuckMapper.xml
 * @author nbin
 * @date 2017年7月18日 下午2:02:28 
 * @version V1.0
 */
public interface SystemLuckMapper extends  BaseMapper<SystemLuck, java.lang.Integer>{
	
	/**
	 * 根据用户id获取用户幸运值
	 * @Title: getLuckByUserId 
	 * @param @param userId
	 * @param @return 
	 * @return List<SystemProduct>
	 */
	List<SystemLuck> getLuckByUserId(Integer userId);
	
}
