package com.wangzhixuan.service;

import com.wangzhixuan.model.RoomDisappearLog;
import com.wangzhixuan.model.SystemTask;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 房间销毁日志记录 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-19
 */
public interface IRoomDisappearLogService extends IService<RoomDisappearLog> {
	/**
	 * 处理任务中的房间解散数据
	 * @param task
	 */
	void taskRoomDisappearLog(SystemTask task);
	/**
	 * 增加一条房间解散记录
	 * @param joinRoomLog
	 * @return
	 */
	boolean insertRoomDisappearLog(RoomDisappearLog disappearLog);
	
	/**
	 * 增加房间的流水
	 * @param joinRoomLog
	 */
	boolean addRoomDisappear(RoomDisappearLog disappearLog);
	
	/**
	 * 增加玩家在房间中的明细
	 * @param joinRoomLog 
	 */
	boolean addPlayerRoom(RoomDisappearLog disappearLog);
	
}
