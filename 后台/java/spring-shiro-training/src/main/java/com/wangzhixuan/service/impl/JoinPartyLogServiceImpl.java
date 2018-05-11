package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.JoinPartyLog;
import com.wangzhixuan.model.JoinRoomLog;
import com.wangzhixuan.model.PlayRoom;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.Room;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.vo.JoinPartyLogVo;
import com.wangzhixuan.commons.utils.BeanUtils;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.mapper.JoinPartyLogMapper;
import com.wangzhixuan.service.IJoinPartyLogService;
import com.wangzhixuan.service.IJoinRoomLogService;
import com.wangzhixuan.service.IPlayRoomService;
import com.wangzhixuan.service.IPlayerService;
import com.wangzhixuan.service.IRoomService;
import com.wangzhixuan.service.ISystemTaskService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 玩家入局记录 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
@Service
public class JoinPartyLogServiceImpl extends ServiceImpl<JoinPartyLogMapper, JoinPartyLog> implements IJoinPartyLogService {
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
	private JoinPartyLogMapper joinPartyLogMapper;
	@Autowired
	private IPlayRoomService playRoomService;
	@Autowired
	private IJoinRoomLogService joinRoomLogService;
	
	@Override
	public void taskJoinPartyLog(SystemTask task) {
		String content = "["+task.getJsonContent()+"]";
		List<JoinPartyLogVo> taskList = gson.fromJson(content, new TypeToken<List<JoinPartyLogVo>>(){}.getType());
		if(taskList != null && taskList.size() > 0 ) {
			JoinPartyLog joinPartyLog = new JoinPartyLog();
			BeanUtils.copyProperties(taskList.get(0), joinPartyLog);
			JSONObject json = JSONObject.parseObject(task.getJsonContent());
			joinPartyLog.setPartyTime(new Date(new Long(json.getLongValue("partyTime"))));
			boolean success = this.insertJoinPartyLog(joinPartyLog);
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
	public boolean insertJoinPartyLog(JoinPartyLog joinPartyLog) {
		boolean success = this.insert(joinPartyLog);
		if(success) {
			success = this.addPlayerPartyNum(joinPartyLog);
		}
		if(success) {
			success = this.addRoomPartyNum(joinPartyLog);
		}
		if(success) {
			success = this.addPlayerRoom(joinPartyLog);
		}
		return success;
	}

	@Override
	public boolean addPlayerPartyNum(JoinPartyLog joinPartyLog) {
		Player player = playerService.selectPlayerBy(joinPartyLog.getPlayerId());
		if(player != null ) {
			player.setJoinPartyNum(player.getJoinPartyNum()+1);
			return playerService.updateById(player);
		} else { 
			return false;
		}
		
	}

	@Override
	public boolean addRoomPartyNum(JoinPartyLog joinPartyLog) {
		Room room = roomService.getRoomBy(joinPartyLog.getHouseOwner(), joinPartyLog.getRoomId(), joinPartyLog.getPartyTime());
		if(room != null) {
			/**获取房间牌局次数*/
			Map<String, Object> roomPartyNum = this.getRoomPartyNum(null, room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			/**获取房间入局人数*/
			Map<String, Object> roomPeopleNum = this.getRoomPeopleNum(room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			if(roomPartyNum != null && roomPartyNum.get("partyNum") != null && roomPeopleNum != null && roomPeopleNum.get("peopleNum") != null) {
				int partyNum = Integer.parseInt(roomPartyNum.get("partyNum").toString());
				room.setPartyNum(partyNum);
				int peopleNum = Integer.parseInt(roomPeopleNum.get("peopleNum").toString());
				room.setJoinPartyNum(peopleNum);
				return roomService.updateById(room);
			} else {
				return false;
			}
		} else {
			return false;
		}
		
	}
	
	@Override
	public boolean addPlayerRoom(JoinPartyLog joinPartyLog) {
		/**获取到房间对象*/
		Room room = roomService.getRoomBy(joinPartyLog.getHouseOwner(), joinPartyLog.getRoomId(), joinPartyLog.getPartyTime());
		if(room == null) {
			return false;
		}
		/**获取到房主对象*/
		Player houseOwner = playerService.selectPlayerBy(room.getHouseOwner());
		/**获取到玩家对象*/
		Player player = playerService.selectPlayerBy(joinPartyLog.getPlayerId());
		if(room != null && houseOwner != null && player != null) {
			PlayRoom playRoom = playRoomService.getPlayRoomBy(player.getPlayId(), room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			if(playRoom == null) {
				JoinRoomLog joinRoomLog = joinRoomLogService.getJoinRoomLogBy(player.getPlayId(), room.getRoomId(), houseOwner.getPlayId(), room.getCreateTime());
				int id = playRoomService.insertPlayRoom(player, room, houseOwner, joinRoomLog);
				playRoom = playRoomService.selectById(id);
			}
			/**获取玩家参与的牌局次数*/
			Map<String, Object> playPartyNum = this.getRoomPartyNum(player.getPlayId(), room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			if(playPartyNum != null && playPartyNum.get("partyNum") != null) {
				int playParty = Integer.parseInt(playPartyNum.get("partyNum").toString());
				playRoom.setJoinPartyNum(playParty);
				playRoomService.updateById(playRoom);
			}
			
			/**获取房间牌局次数*/
			Map<String, Object> roomPartyNum = this.getRoomPartyNum(null, room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			if(roomPartyNum != null && roomPartyNum.get("partyNum") != null) {
				int roomParty = Integer.parseInt(roomPartyNum.get("partyNum").toString());
				playRoomService.updateRoomPartyNum(roomParty, houseOwner.getPlayId(), room.getRoomId(), room.getCreateTime());
			}
			return true;
		} else {
			return false;
		}
		
	}
	
	@Override
	public Map<String, Object> getRoomPartyNum(String playerId, String houseOwner, int roomId, Date createTime) {
		return joinPartyLogMapper.getRoomPartyNum(playerId, houseOwner, roomId, createTime, configurer.getProperty("roomDays"));
	}

	@Override
	public Map<String, Object> getRoomPeopleNum(String houseOwner, int roomId, Date createTime) {
		return joinPartyLogMapper.getPartyPeopleNum(houseOwner, roomId, createTime, configurer.getProperty("roomDays"));
	}

	@Override
	public Map<String, Object> selectJoinPartyMap() {
		Map<String, Object> joinRoomMap = joinRoomLogService.selectJoinRoomMap();
		BigDecimal allJoinRooms = new BigDecimal(0);
		if(joinRoomMap != null) {
			// 获取加入房间总数
			allJoinRooms = new BigDecimal(joinRoomMap.get("allJoinRooms").toString());
		}
		Map<String, Object> joinPartyMap = joinPartyLogMapper.selectJoinPartyData();
		// 获取入局总数
		BigDecimal allJoinPartys = new BigDecimal(0);
		if(joinPartyMap != null) {
			allJoinPartys = new BigDecimal(joinPartyMap.get("allJoinPartys").toString());
		}
		if(allJoinRooms.compareTo(new BigDecimal(0)) == 0) {
			joinPartyMap.put("partyRate", "0.00");
		} else {
			BigDecimal partyRate = allJoinPartys.multiply(new BigDecimal(100)).divide(allJoinRooms, 2, BigDecimal.ROUND_HALF_UP);
			joinPartyMap.put("partyRate", partyRate);
		}
		return joinPartyMap;
	}

	@Override
	public Map<String, Object> selectTodayPartys() {
		return joinPartyLogMapper.selectTodayPartys();
	}
	
}
