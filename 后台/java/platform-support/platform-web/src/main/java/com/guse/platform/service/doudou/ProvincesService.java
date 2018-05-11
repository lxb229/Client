package com.guse.platform.service.doudou;

import java.util.List;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.doudou.Provinces;
import com.guse.platform.vo.doudou.ProvincesVo;

/**
 * provinces
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface ProvincesService extends BaseService<Provinces,java.lang.Integer>{

	/**
	 * 所有省市树
	 * @Title: menusTree 
	 * @param @return 
	 * @return Result<List<Menus>>
	 */
	Result<List<ProvincesVo>> menusTree();
}
