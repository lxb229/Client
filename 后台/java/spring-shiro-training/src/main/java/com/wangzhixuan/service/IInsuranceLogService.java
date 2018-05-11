package com.wangzhixuan.service;

import com.wangzhixuan.model.InsuranceLog;
import com.wangzhixuan.model.SystemTask;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 保险记录 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
public interface IInsuranceLogService extends IService<InsuranceLog> {
	/**
	 * 处理任务中的保险数据
	 * @param task
	 */
	void taskInsuranceLog(SystemTask task);
	/**
	 * 增加一条保险记录
	 * @param joinRoomLog
	 * @return
	 */
	boolean insertInsuranceLog(InsuranceLog insuranceLog);
	
	/**
	 * 增加房间的保险
	 * @param joinRoomLog
	 */
	boolean addRoomInsuranceWater(InsuranceLog insuranceLog);
	
	/**
	 * 增加玩家在房间中的明细
	 * @param joinRoomLog 
	 */
	boolean addPlayerRoom(InsuranceLog insuranceLog);
	
	/**
	 * 获取房间内的保险
	 * @param houseOwner 房主id
	 * @param roomId 房间id
	 * @param createTime 房间创建时间
	 * @return
	 */
	Map<String, Object> getRoomInsuranceWater(String playerId, String houseOwner, int roomId, Date createTime);
	
	
}
