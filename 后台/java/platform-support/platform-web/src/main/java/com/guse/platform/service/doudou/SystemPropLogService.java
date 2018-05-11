package com.guse.platform.service.doudou;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.doudou.SystemPropLog;

/**
 * system_prop_log
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface SystemPropLogService extends BaseService<SystemPropLog,java.lang.Integer>{

	Result<Integer> saveOrUpdatePropLog(SystemPropLog propLog);
}
