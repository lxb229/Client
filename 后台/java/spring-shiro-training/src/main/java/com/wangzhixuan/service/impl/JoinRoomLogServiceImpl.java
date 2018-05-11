package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.JoinRoomLog;
import com.wangzhixuan.model.PlayRoom;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.Room;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.vo.JoinRoomLogVo;
import com.wangzhixuan.commons.utils.BeanUtils;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.mapper.JoinRoomLogMapper;
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
 * 加入房间日志 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-15
 */
@Service
public class JoinRoomLogServiceImpl extends ServiceImpl<JoinRoomLogMapper, JoinRoomLog> implements IJoinRoomLogService {

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
	private IJoinRoomLogService roomLogService;
	@Autowired
	private JoinRoomLogMapper joinRoomLogMapper;
	@Autowired
	private IPlayRoomService playRoomService;
	
	
	@Override
	public void taskJoinRoomLog(SystemTask task) {
		String content = "["+task.getJsonContent()+"]";
		List<JoinRoomLogVo> taskList = gson.fromJson(content, new TypeToken<List<JoinRoomLogVo>>(){}.getType());
		if(taskList != null && taskList.size() > 0 ) {
			JoinRoomLog joinRoomLog = new JoinRoomLog();
			BeanUtils.copyProperties(taskList.get(0), joinRoomLog);
			JSONObject json = JSONObject.parseObject(task.getJsonContent());
			joinRoomLog.setJoinTime(new Date(new Long(json.getLongValue("joinTime"))));
			boolean success = this.insertJoinRoomLog(joinRoomLog);
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
	public boolean insertJoinRoomLog(JoinRoomLog joinRoomLog) {
		boolean success = this.insert(joinRoomLog);
		if(success) {
			// 更新玩家的创建房间数据
			success = this.addPlayerJoinNum(joinRoomLog);
		}
		if(success) {
			// 更新房间的加入人数数据
			success = this.addRoomJoinNum(joinRoomLog);
		}
		if(success) {
			// 生成玩家在房间中的明细
			success = this.addPlayerRoom(joinRoomLog);
		}
		return success;
	}

	@Override
	public boolean addPlayerJoinNum(JoinRoomLog joinRoomLog) {
		Player player = playerService.selectPlayerBy(joinRoomLog.getPlayerId());
		if(player != null ) {
			player.setJoinRoomNum(player.getJoinRoomNum()+1);
			return playerService.updateById(player);
		} else {
			return false;
		}
	}

	@Override
	public boolean addRoomJoinNum(JoinRoomLog joinRoomLog) {
		Room room = roomService.getRoomBy(joinRoomLog.getHouseOwner(), joinRoomLog.getRoomId(), joinRoomLog.getJoinTime());
		if(room != null) {
			/**获取加入房间人数*/
			Map<String, Object> roomJoinNum = roomLogService.getRoomJoinNum(room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			if(roomJoinNum != null && roomJoinNum.get("joinNum") != null) {
				int joinNum = Integer.parseInt(roomJoinNum.get("joinNum").toString());
				room.setJoinRoomNum(joinNum);
				return roomService.updateById(room);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	@Override
	public boolean addPlayerRoom(JoinRoomLog joinRoomLog) {
		/**获取到房间对象*/
		Room room = roomService.getRoomBy(joinRoomLog.getHouseOwner(), joinRoomLog.getRoomId(), joinRoomLog.getJoinTime());
		if(room == null) {
			return false;
		}
		/**获取到房主对象*/
		Player houseOwner = playerService.selectPlayerBy(room.getHouseOwner());
		/**获取到玩家对象*/
		Player player = playerService.selectPlayerBy(joinRoomLog.getPlayerId());
		if(room != null && houseOwner != null && player != null) {
			PlayRoom playRoom = playRoomService.getPlayRoomBy(player.getPlayId(), room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			/**查询是否生成玩家房间明细*/
			if(playRoom == null) {
				playRoomService.insertPlayRoom(player, room, houseOwner, joinRoomLog);
			} 
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Map<String, Object> getRoomJoinNum(String houseOwner, int roomId, Date createTime) {
		return joinRoomLogMapper.getRoomJoinNum(houseOwner, roomId, createTime, configurer.getProperty("roomDays"));
	}

	@Override
	public JoinRoomLog getJoinRoomLogBy(String playerId, int roomId, String houseOwner, Date createTime) {
		return joinRoomLogMapper.getJoinRoomLogBy(playerId, houseOwner, roomId, createTime, configurer.getProperty("roomDays"));
	}

	@Override
	public Map<String, Object> selectJoinRoomMap() {
		return joinRoomLogMapper.selectJoinRoomData();
	}

}
