package com.guse.platform.dao.doudou;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.doudou.City;


/**
 * city
 * @see CityMapper.xml
 * @author nbin
 * @date 2017年7月18日 下午2:02:28 
 * @version V1.0
 */
public interface CityMapper extends  BaseMapper<City, Integer>{
	
	
	/**
	 * 获得所有城市
	 * @Title: selectAllCityList 
	 * @param @return 
	 * @return List<Menus>
	 */
	List<City> selectAllCityList();
	
	/**
	 * 根据区域名称获取区域
	 * @param cityName
	 * @return
	 */
	City getCityByName(@Param("cityName") String cityName);
}
