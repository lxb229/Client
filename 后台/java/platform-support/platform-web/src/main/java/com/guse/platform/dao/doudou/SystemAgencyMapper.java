package com.guse.platform.dao.doudou;


import java.util.List;

import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.doudou.SystemAgency;


/**
 * system_agency
 * @see SystemAgencyMapper.xml
 * @author nbin
 * @date 2017年7月18日 下午2:02:28 
 * @version V1.0
 */
public interface SystemAgencyMapper extends  BaseMapper<SystemAgency, java.lang.Integer>{
	
	/**
	 * 根据区域id查询所有代理
	 * @Title: selectAgencyListByCityId 
	 * @param @param cityId
	 * @param @return 
	 * @return List<Resource>
	 */
	List<SystemAgency> selectAgencyListByCityId(Integer cityId);
	
	/**
	 * 获取指定区域的代理
	 * @param cityId
	 * @return
	 */
	SystemAgency getAgencyForCityId(Integer cityId);
	
	/**
	 * 查询用户是否还有其他代理区域
	 * @param agency
	 * @return
	 */
	List<SystemAgency> selectAgencyOut(SystemAgency agency);
}
