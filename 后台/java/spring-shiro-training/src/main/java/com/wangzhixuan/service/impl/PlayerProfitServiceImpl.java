package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.JoinRoomLog;
import com.wangzhixuan.model.PlayRoom;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.PlayerProfit;
import com.wangzhixuan.model.Room;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.vo.PlayerProfitVo;
import com.wangzhixuan.commons.utils.BeanUtils;
import com.wangzhixuan.mapper.PlayerProfitMapper;
import com.wangzhixuan.service.IJoinRoomLogService;
import com.wangzhixuan.service.IPlayRoomService;
import com.wangzhixuan.service.IPlayerProfitService;
import com.wangzhixuan.service.IPlayerService;
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
 * 玩家盈亏记录 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-19
 */
@Service
public class PlayerProfitServiceImpl extends ServiceImpl<PlayerProfitMapper, PlayerProfit> implements IPlayerProfitService {

	private Gson gson = new Gson();
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
	
	@Override
	public void taskPlayerProfit(SystemTask task) {
		String content = "["+task.getJsonContent()+"]";
		List<PlayerProfitVo> taskList = gson.fromJson(content, new TypeToken<List<PlayerProfitVo>>(){}.getType());
		if(taskList != null && taskList.size() > 0 ) {
			PlayerProfit playerProfit = new PlayerProfit();
			BeanUtils.copyProperties(taskList.get(0), playerProfit);
			JSONObject json = JSONObject.parseObject(task.getJsonContent());
			playerProfit.setProfitTime(new Date(new Long(json.getLongValue("profitTime"))));
			boolean success = this.insertPlayerProfit(playerProfit);
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
	public boolean insertPlayerProfit(PlayerProfit playerProfit) {
		boolean success = this.insert(playerProfit);
		if(success) {
			success = this.addPlayerProfit(playerProfit);
		}
		if(success) {
			success = this.addPlayerRoom(playerProfit);
		}
		return success;
	}

	@Override
	public boolean addPlayerProfit(PlayerProfit playerProfit) {
		Player player = playerService.selectPlayerBy(playerProfit.getPlayerId());
		if(player != null ) {
			player.setProfitNum(player.getProfitNum()+playerProfit.getPlayerProfit());
			return playerService.updateById(player);
		} else  {
			return false;
		}
		
	}

	@Override
	public boolean addPlayerRoom(PlayerProfit playerProfit) {
		/**获取到房间对象*/
		Room room = roomService.getRoomBy(playerProfit.getHouseOwner(), playerProfit.getRoomId(), playerProfit.getProfitTime());
		if(room == null) {
			return false;
		}
		/**获取到房主对象*/
		Player houseOwner = playerService.selectPlayerBy(room.getHouseOwner());
		/**获取到玩家对象*/
		Player player = playerService.selectPlayerBy(playerProfit.getPlayerId());
		if(room != null && houseOwner != null && player != null) {
			PlayRoom playRoom = playRoomService.getPlayRoomBy(player.getPlayId(), room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			if(playRoom == null) {
				JoinRoomLog joinRoomLog = joinRoomLogService.getJoinRoomLogBy(player.getPlayId(), room.getRoomId(), houseOwner.getPlayId(), room.getCreateTime());
				int id = playRoomService.insertPlayRoom(player, room, houseOwner, joinRoomLog);
				playRoom = playRoomService.selectById(id);
			}
			playRoom.setProfitNum(playRoom.getProfitNum()+playerProfit.getPlayerProfit());
			return playRoomService.updateById(playRoom);
		} else {
			return false;
		}
	}
	
}
