package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.InsuranceLog;
import com.wangzhixuan.model.JoinRoomLog;
import com.wangzhixuan.model.PlayRoom;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.Room;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.vo.InsuranceLogVo;
import com.wangzhixuan.commons.utils.BeanUtils;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.mapper.InsuranceLogMapper;
import com.wangzhixuan.service.IInsuranceLogService;
import com.wangzhixuan.service.IJoinRoomLogService;
import com.wangzhixuan.service.IPlayRoomService;
import com.wangzhixuan.service.IPlayerService;
import com.wangzhixuan.service.IRoomService;
import com.wangzhixuan.service.ISystemTaskService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 保险记录 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
@Service
public class InsuranceLogServiceImpl extends ServiceImpl<InsuranceLogMapper, InsuranceLog> implements IInsuranceLogService {

	private Gson gson = new Gson();
	@Autowired
	private PropertyConfigurer configurer;
	@Autowired
	private ISystemTaskService taskService;
	@Autowired
	private IPlayerService playerService;
	@Autowired
	private IRoomService roomService;
	@Autowired
	private IJoinRoomLogService joinRoomLogService;
	@Autowired
	private IPlayRoomService playRoomService;
	@Autowired
	private InsuranceLogMapper insuranceLogMapper;
	
	@Override
	public void taskInsuranceLog(SystemTask task) {
		String content = "["+task.getJsonContent()+"]";
		List<InsuranceLogVo> taskList = gson.fromJson(content, new TypeToken<List<InsuranceLogVo>>(){}.getType());
		if(taskList != null && taskList.size() > 0 ) {
			InsuranceLog insuranceLog = new InsuranceLog();
			BeanUtils.copyProperties(taskList.get(0), insuranceLog);
			JSONObject json = JSONObject.parseObject(task.getJsonContent());
			insuranceLog.setInsuranceTime(new Date(new Long(json.getLongValue("insuranceTime"))));
			boolean success = this.insertInsuranceLog(insuranceLog);
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
	public boolean insertInsuranceLog(InsuranceLog insuranceLog) {
		boolean success = this.insert(insuranceLog);
		if(success) {
			success = this.addRoomInsuranceWater(insuranceLog);
		}
		if(success) {
			success = this.addPlayerRoom(insuranceLog);
		}
		return success;
	}

	@Override
	public boolean addRoomInsuranceWater(InsuranceLog insuranceLog) {
		Room room = roomService.getRoomBy(insuranceLog.getHouseOwner(), insuranceLog.getRoomId(), insuranceLog.getInsuranceTime());
		if(room != null) {
			room.setInsuranceWater(room.getInsuranceWater()+insuranceLog.getInsuranceNum());
			return roomService.updateById(room);
		} else {
			return false;
		}
	}

	@Override
	public boolean addPlayerRoom(InsuranceLog insuranceLog) {
		/**获取到房间对象*/
		Room room = roomService.getRoomBy(insuranceLog.getHouseOwner(), insuranceLog.getRoomId(), insuranceLog.getInsuranceTime());
		if(room == null) {
			return false;
		}
		/**获取到房主对象*/
		Player houseOwner = playerService.selectPlayerBy(room.getHouseOwner());
		/**获取到玩家对象*/
		Player player = playerService.selectPlayerBy(insuranceLog.getPlayerId());
		if(room != null && houseOwner != null && player != null) {
			PlayRoom playRoom = playRoomService.getPlayRoomBy(player.getPlayId(), room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			if(playRoom == null) {
				JoinRoomLog joinRoomLog = joinRoomLogService.getJoinRoomLogBy(player.getPlayId(), room.getRoomId(), houseOwner.getPlayId(), room.getCreateTime());
				int id = playRoomService.insertPlayRoom(player, room, houseOwner, joinRoomLog);
				playRoom = playRoomService.selectById(id);
			}
			/**获取玩家的流水*/
			Map<String, Object> playInsuranceWater = this.getRoomInsuranceWater(player.getPlayId(), room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			if(playInsuranceWater != null && playInsuranceWater.get("insuranceWater") != null) {
				int playerInsuranceWater = Integer.parseInt(playInsuranceWater.get("insuranceWater").toString());
				playRoom.setPlayerFinancialWater(playerInsuranceWater);
				playRoomService.updateById(playRoom);
			}
			
			/**获取房间的总流水*/
			Map<String, Object> roomInsuranceWater = this.getRoomInsuranceWater(null, room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			if(roomInsuranceWater != null && roomInsuranceWater.get("insuranceWater") != null) {
				int insuranceWater = Integer.parseInt(roomInsuranceWater.get("insuranceWater").toString());
				playRoomService.updateRoomFinancialWater(insuranceWater, houseOwner.getPlayId(), room.getRoomId(), room.getCreateTime());
			}
			return true;
		} else {
			return  false;
		}
		
	}

	@Override
	public Map<String, Object> getRoomInsuranceWater(String playId, String houseOwner, int roomId, Date createTime) {
		return insuranceLogMapper.getInsuranceWater(playId, houseOwner, roomId, createTime, configurer.getProperty("roomDays"));
	}
	
}
