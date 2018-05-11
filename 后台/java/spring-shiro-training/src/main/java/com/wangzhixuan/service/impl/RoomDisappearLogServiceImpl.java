package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Room;
import com.wangzhixuan.model.RoomDisappearLog;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.vo.RoomDisappearLogVo;
import com.wangzhixuan.commons.utils.BeanUtils;
import com.wangzhixuan.mapper.RoomDisappearLogMapper;
import com.wangzhixuan.service.IPlayRoomService;
import com.wangzhixuan.service.IRoomDisappearLogService;
import com.wangzhixuan.service.IRoomService;
import com.wangzhixuan.service.ISystemTaskService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 房间销毁日志记录 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-19
 */
@Service
public class RoomDisappearLogServiceImpl extends ServiceImpl<RoomDisappearLogMapper, RoomDisappearLog> implements IRoomDisappearLogService {

	private Gson gson = new Gson();
	@Autowired
	private ISystemTaskService taskService;
	@Autowired
	private IRoomService roomService;
	@Autowired
	private IPlayRoomService playRoomService;
	
	@Override
	public void taskRoomDisappearLog(SystemTask task) {
		String content = "["+task.getJsonContent()+"]";
		List<RoomDisappearLogVo> taskList = gson.fromJson(content, new TypeToken<List<RoomDisappearLogVo>>(){}.getType());
		if(taskList != null && taskList.size() > 0 ) {
			RoomDisappearLog roomDisappearLog = new RoomDisappearLog();
			BeanUtils.copyProperties(taskList.get(0), roomDisappearLog);
			JSONObject json = JSONObject.parseObject(task.getJsonContent());
			roomDisappearLog.setDisappearTime(new Date(new Long(json.getLongValue("disappearTime"))));
			boolean success = this.insertRoomDisappearLog(roomDisappearLog);
			if(success) {
				task.setTaskStatus(1);
			} else {
				task.setTaskStatus(2);
			}
			task.setTaskNum(task.getTaskNum()+1);
			taskService.updateById(task);
		}
		
	}

	@Override
	public boolean insertRoomDisappearLog(RoomDisappearLog disappearLog) {
		boolean success = this.insert(disappearLog);
		if(success) {
			success = this.addRoomDisappear(disappearLog);
		}
		if(success) {
			success = this.addPlayerRoom(disappearLog);
		}
		return success;
	}

	@Override
	public boolean addRoomDisappear(RoomDisappearLog disappearLog) {
		Room room = roomService.getRoomBy(disappearLog.getHouseOwner(), disappearLog.getRoomId(), disappearLog.getDisappearTime());
		if(room != null) {
			room.setDisappearTime(disappearLog.getDisappearTime());
			return roomService.updateById(room);
		} else {
			return true;
		}
		
	}

	@Override
	public boolean addPlayerRoom(RoomDisappearLog disappearLog) {
		/**获取到房间对象*/
		Room room = roomService.getRoomBy(disappearLog.getHouseOwner(), disappearLog.getRoomId(), disappearLog.getDisappearTime());
		if(room != null) {
			int rowNum = playRoomService.updateRoomDisappaearTime(disappearLog.getDisappearTime(), room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			if(rowNum >= 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
}
