package com.guse.platform.dao.doudou;


import java.util.List;

import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.doudou.Provinces;


/**
 * provinces
 * @see ProvincesMapper.xml
 * @author nbin
 * @date 2017年7月18日 下午2:02:28 
 * @version V1.0
 */
public interface ProvincesMapper extends  BaseMapper<Provinces, java.lang.Integer>{
	/**
	 * 获得所有省份
	 * @Title: selectAllMenusList 
	 * @param @return 
	 * @return List<Menus>
	 */
	List<Provinces> selectAllProvincesList();
}
