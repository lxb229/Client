package com.guse.platform.service.doudou;

import com.alibaba.fastjson.JSONObject;
import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.doudou.SystemLuck;
import com.guse.platform.entity.doudou.SystemTask;

/**
 * system_luck
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface SystemLuckService extends BaseService<SystemLuck,java.lang.Integer>{
	/**
	 * 新增更新幸运
	 * @Title: saveOrUpdateProduct 
	 * @param @param product
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer> saveOrUpdateLuck(SystemLuck luck);
	
	/**
	 * 删除幸运值
	 * @Title: deleteLuck 
	 * @param @param luckId
	 * @param @return 
	 * @return Result<Integer>
	 */
	Result<Integer>  deleteLuck(Integer luckId);
	
	/**
	 * 处理任务中的俱乐部玩家数据
	 * @param task
	 */
	public void taskUserLuck(SystemTask task);
	
	/**
	 * 处理mq中的俱乐部玩家信息
	 * @param obj
	 * @return
	 */
	public Result<Integer> processingUserLuck(JSONObject obj);
}
