package com.guse.platform.service.doudou;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.doudou.GoldLog;

/**
 * gold_log
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface GoldLogService extends BaseService<GoldLog,java.lang.Integer>{

	/**
	 * 增加/减少金币
	 * @param goldLogVo
	 * @return
	 */
	public Result<Integer> saveOrUpdateGoldLog(String goldJsonStr);
	
	/**
	 * 角色列表
	 * @Title: queryListRoles 
	 * @param @param roles
	 * @param @return 
	 * @return Result<List<Roles>>
	 */
	public Result<List<GoldLog>> queryList(GoldLog goldLog);
	
	/**
	 * 统计盈利数据
	 * @param goldLog
	 * @return
	 */
	Result<Map<String, Object>> countAmount(GoldLog goldLog);
	
	/**
	 * 判断日期是否是今天
	 * @param date
	 * @return
	 */
	public boolean isNow(Date date);
	
}
