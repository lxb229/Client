package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.BetLog;
import com.wangzhixuan.model.JoinRoomLog;
import com.wangzhixuan.model.PlayRoom;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.Room;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.vo.BetLogVo;
import com.wangzhixuan.commons.utils.BeanUtils;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.mapper.BetLogMapper;
import com.wangzhixuan.service.IBetLogService;
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
 * 下注记录 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
@Service
public class BetLogServiceImpl extends ServiceImpl<BetLogMapper, BetLog> implements IBetLogService {

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
	private BetLogMapper betLogMapper;
	
	
	@Override
	public void taskBetLog(SystemTask task) {
		String content = "["+task.getJsonContent()+"]";
		List<BetLogVo> taskList = gson.fromJson(content, new TypeToken<List<BetLogVo>>(){}.getType());
		if(taskList != null && taskList.size() > 0 ) {
			BetLog betLog = new BetLog();
			BeanUtils.copyProperties(taskList.get(0), betLog);
			JSONObject json = JSONObject.parseObject(task.getJsonContent());
			betLog.setBetTime(new Date(new Long(json.getLongValue("betTime"))));
			boolean success = this.insertBetLog(betLog);
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
	public boolean insertBetLog(BetLog betLog) {
		boolean success = this.insert(betLog);
		if(success) {
			success = this.addRoomFinancialWater(betLog);
		}
		if(success) {
			success = this.addPlayerRoom(betLog);
			
		}
		return success;
	}

	@Override
	public boolean addRoomFinancialWater(BetLog betLog) {
		Room room = roomService.getRoomBy(betLog.getHouseOwner(), betLog.getRoomId(), betLog.getBetTime());
		if(room != null) {
			room.setFinancialWater(room.getFinancialWater()+betLog.getBetNum());
			return roomService.updateById(room);
		} else {
			return false;
		}
	}
	
	@Override
	public boolean addPlayerRoom(BetLog betLog) {
		/**获取到房间对象*/
		Room room = roomService.getRoomBy(betLog.getHouseOwner(), betLog.getRoomId(), betLog.getBetTime());
		if(room == null) {
			return false;
		}
		/**获取到房主对象*/
		Player houseOwner = playerService.selectPlayerBy(room.getHouseOwner());
		/**获取到玩家对象*/
		Player player = playerService.selectPlayerBy(betLog.getPlayerId());
		if(room != null && houseOwner != null && player != null) {
			PlayRoom playRoom = playRoomService.getPlayRoomBy(player.getPlayId(), room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			if(playRoom == null) {
				JoinRoomLog joinRoomLog = joinRoomLogService.getJoinRoomLogBy(player.getPlayId(), room.getRoomId(), houseOwner.getPlayId(), room.getCreateTime());
				int id = playRoomService.insertPlayRoom(player, room, houseOwner, joinRoomLog);
				playRoom = playRoomService.selectById(id);
			}
			/**获取玩家的流水*/
			Map<String, Object> playFinancialWater = this.getRoomFinancialWater(player.getPlayId(), room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			if(playFinancialWater != null && playFinancialWater.get("financialWater") != null) {
				int playerFinancialWater = Integer.parseInt(playFinancialWater.get("financialWater").toString());
				playRoom.setPlayerFinancialWater(playerFinancialWater);
				playRoomService.updateById(playRoom);
			}
			
			/**获取房间的总流水*/
			Map<String, Object> roomFinancialWater = this.getRoomFinancialWater(null, room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			if(roomFinancialWater != null && roomFinancialWater.get("financialWater") != null) {
				int financialWater = Integer.parseInt(roomFinancialWater.get("financialWater").toString());
				playRoomService.updateRoomFinancialWater(financialWater, houseOwner.getPlayId(), room.getRoomId(), room.getCreateTime());
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Map<String, Object> getRoomFinancialWater(String playerId, String houseOwner, int roomId, Date createTime) {
		return betLogMapper.getFinancialWater(playerId, houseOwner, roomId, createTime, configurer.getProperty("roomDays"));
	}

	@Override
	public Map<String, Object> selectBetMap() {
		return betLogMapper.selectBetData();
	}
	
}
