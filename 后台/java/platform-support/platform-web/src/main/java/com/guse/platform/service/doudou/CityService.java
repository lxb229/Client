package com.guse.platform.service.doudou;

import java.util.List;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.doudou.City;
import com.guse.platform.vo.doudou.CityTreeVo;

/**
 * city
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface CityService extends BaseService<City,java.lang.Integer>{

	/**
	 * 所有区域地区树
	 * @Title: menusTree 
	 * @param @return 
	 * @return Result<List<Menus>>
	 */
	Result<List<CityTreeVo>> menusTree();
	/**
	 * 根据IP地址获取区域
	 * @param ipAddress IP地址
	 * @return
	 */
	City getCityByIp(String ipAddress);
}
