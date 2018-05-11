package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.JettonLog;
import com.wangzhixuan.model.JoinRoomLog;
import com.wangzhixuan.model.PlayRoom;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.Room;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.vo.JettonLogVo;
import com.wangzhixuan.commons.utils.BeanUtils;
import com.wangzhixuan.mapper.JettonLogMapper;
import com.wangzhixuan.service.IJettonLogService;
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
 * 筹码日志表 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
@Service
public class JettonLogServiceImpl extends ServiceImpl<JettonLogMapper, JettonLog> implements IJettonLogService {

	private Gson gson = new Gson();
	@Autowired
	private ISystemTaskService taskService;
	@Autowired
	private JettonLogMapper jettonLogMapper;
	@Autowired
	private IPlayerService playerService;
	@Autowired
	private IRoomService roomService;
	@Autowired
	private IPlayRoomService playRoomService;
	@Autowired
	private IJoinRoomLogService joinRoomLogService;
	
	
	@Override
	public void taskJettonLog(SystemTask task) {
		String content = "["+task.getJsonContent()+"]";
		List<JettonLogVo> taskList = gson.fromJson(content, new TypeToken<List<JettonLogVo>>(){}.getType());
		if(taskList != null && taskList.size() > 0 ) {
			JettonLog jettonLog = new JettonLog();
			BeanUtils.copyProperties(taskList.get(0), jettonLog);
			JSONObject json = JSONObject.parseObject(task.getJsonContent());
			jettonLog.setJettonTime(new Date(new Long(json.getLongValue("jettonTime"))));
			boolean success = this.insertJettonLog(jettonLog);
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
	public boolean insertJettonLog(JettonLog jettonLog) {
		boolean success = this.insert(jettonLog);
		if(success) {
			// 更新玩家的创建房间数据
			success = this.addPlayerJettonNum(jettonLog);
		}
		if(success) {
			// 更新房间的加入人数数据
			success = this.addRoomJettonNum(jettonLog);
		}
		if(success) {
			// 生成玩家在房间中的明细
			success = this.addPlayerRoom(jettonLog);
			
		}
		return success;
	}

	@Override
	public boolean addPlayerJettonNum(JettonLog jettonLog) {
		Player player = playerService.selectPlayerBy(jettonLog.getPlayerId());
		if(player != null ) {
			player.setJettonNum(player.getJettonNum()+jettonLog.getJettonNum());
			return playerService.updateById(player);
		} else {
			return false;
		}
	}

	@Override
	public boolean addRoomJettonNum(JettonLog jettonLog) {
		Room room = roomService.getRoomBy(jettonLog.getHouseOwner(), jettonLog.getRoomId(), jettonLog.getJettonTime());
		if(room != null) {
			room.setJettonNum(room.getJettonNum()+jettonLog.getJettonNum());
			return roomService.updateById(room);
		} else {
			return false;
		}
		
	}

	@Override
	public boolean addPlayerRoom(JettonLog jettonLog) {
		/**获取到房间对象*/
		Room room = roomService.getRoomBy(jettonLog.getHouseOwner(), jettonLog.getRoomId(), jettonLog.getJettonTime());
		if(room == null) {
			return false;
		}
		/**获取到房主对象*/
		Player houseOwner = playerService.selectPlayerBy(room.getHouseOwner());
		/**获取到玩家对象*/
		Player player = playerService.selectPlayerBy(jettonLog.getPlayerId());
		if(room != null && houseOwner != null && player != null) {
			PlayRoom playRoom = playRoomService.getPlayRoomBy(player.getPlayId(), room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			if(playRoom == null) {
				JoinRoomLog joinRoomLog = joinRoomLogService.getJoinRoomLogBy(player.getPlayId(), room.getRoomId(), houseOwner.getPlayId(), room.getCreateTime());
				int id = playRoomService.insertPlayRoom(player, room, houseOwner, joinRoomLog);
				playRoom = playRoomService.selectById(id);
			}
			playRoom.setJettonNum(playRoom.getJettonNum()+jettonLog.getJettonNum());
			return playRoomService.updateById(playRoom);
		} else {
			return false;
		}
	}

	@Override
	public Map<String, Object> selectJettonMap() {
		return jettonLogMapper.selectJettonData();
	}
	
}
