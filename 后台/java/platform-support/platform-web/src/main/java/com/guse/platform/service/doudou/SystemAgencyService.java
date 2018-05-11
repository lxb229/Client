package com.guse.platform.service.doudou;

import java.util.List;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.doudou.SystemAgency;

/**
 * system_agency
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface SystemAgencyService extends BaseService<SystemAgency,java.lang.Integer>{

	/**
	 * 新增更新代理
	 * @Title: saveOrUpdateResource 
	 * @param @param resource
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> saveOrUpdateAgency(SystemAgency agency);
	
	/**
	 * 删除代理
	 * @Title: deleteProduct 
	 * @param @param spId
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer>  deleteAgency(Integer saId);
	
	/**
	 * 根据菜单id获得菜单下资源列表不需要分页
	 * @Title: getChildAgencyList 
	 * @param @param cityId
	 * @param @return 
	 * @return Result<List<SystemAgency>>
	 */
	Result<List<SystemAgency>> getAgencyListForCityId(Integer cityId);
	
	/**
	 * 获取指定地区的唯一代理
	 * @param cityId
	 * @return
	 */
	SystemAgency getAgencyForCityId(Integer cityId);
	
	/**
	 * 通知游戏服务器取消代理标志
	 * @Title: deleteProduct 
	 * @param @param spId
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> gmDeleteAgency(SystemAgency agency);
	
}
