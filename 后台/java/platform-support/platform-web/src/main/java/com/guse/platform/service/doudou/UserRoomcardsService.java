package com.guse.platform.service.doudou;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.guse.platform.common.base.BaseService;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.doudou.SystemTask;
import com.guse.platform.entity.doudou.UserRoomcards;

/**
 * user_roomcards
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface UserRoomcardsService extends BaseService<UserRoomcards,java.lang.Integer>{

	Result<Integer> saveOrUpdateRoomcard(UserRoomcards roomcards);
	
	List<UserRoomcards> getRoomcardByUser(Integer userId);
	
	/**
	 * 处理任务中的房卡数据
	 * @param task
	 */
	public void taskRoomCards(SystemTask task);
	
	/**
	 * 处理mq中的房卡信息
	 * @param obj
	 * @return
	 */
	public Result<Integer> processingRoomCards(JSONObject obj);
}
