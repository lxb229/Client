package com.guse.platform.service.doudou;


import com.alibaba.fastjson.JSONObject;
import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.doudou.City;

/**
 * city
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface MQService extends BaseService<City,java.lang.Integer>{

	/**
	 * 启动mq消费者
	 * @Title: startConsumer 
	 * @param @return 
	 * @return Result<Integer>
	 */
	void startConsumer();
}
