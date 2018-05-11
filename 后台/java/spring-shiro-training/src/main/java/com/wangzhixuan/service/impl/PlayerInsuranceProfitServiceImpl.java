package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.JoinRoomLog;
import com.wangzhixuan.model.PlayRoom;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.PlayerInsuranceProfit;
import com.wangzhixuan.model.Room;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.vo.PlayerInsuranceProfitVo;
import com.wangzhixuan.commons.utils.BeanUtils;
import com.wangzhixuan.mapper.PlayerInsuranceProfitMapper;
import com.wangzhixuan.service.IJoinRoomLogService;
import com.wangzhixuan.service.IPlayRoomService;
import com.wangzhixuan.service.IPlayerInsuranceProfitService;
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
 * 玩家保险盈亏 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-19
 */
@Service
public class PlayerInsuranceProfitServiceImpl extends ServiceImpl<PlayerInsuranceProfitMapper, PlayerInsuranceProfit> implements IPlayerInsuranceProfitService {

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
	public void taskPlayerInsuranceProfit(SystemTask task) {
		String content = "["+task.getJsonContent()+"]";
		List<PlayerInsuranceProfitVo> taskList = gson.fromJson(content, new TypeToken<List<PlayerInsuranceProfitVo>>(){}.getType());
		if(taskList != null && taskList.size() > 0 ) {
			PlayerInsuranceProfit insuranceProfit = new PlayerInsuranceProfit();
			BeanUtils.copyProperties(taskList.get(0), insuranceProfit);
			JSONObject json = JSONObject.parseObject(task.getJsonContent());
			insuranceProfit.setProfitTime(new Date(new Long(json.getLongValue("profitTime"))));
			boolean success = this.insertPlayerInsuranceProfit(insuranceProfit);
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
	public boolean insertPlayerInsuranceProfit(PlayerInsuranceProfit insuranceProfit) {
		boolean success = this.insert(insuranceProfit);
		if(success) {
			success = this.addPlayerRoom(insuranceProfit);
		}
		return success;
	}

	@Override
	public boolean addPlayerRoom(PlayerInsuranceProfit insuranceProfit) {
		/**获取到房间对象*/
		Room room = roomService.getRoomBy(insuranceProfit.getHouseOwner(), insuranceProfit.getRoomId(), insuranceProfit.getProfitTime());
		if(room == null) {
			return false;
		}
		/**获取到房主对象*/
		Player houseOwner = playerService.selectPlayerBy(room.getHouseOwner());
		/**获取到玩家对象*/
		Player player = playerService.selectPlayerBy(insuranceProfit.getPlayerId());
		if(room != null && houseOwner != null && player != null) {
			PlayRoom playRoom = playRoomService.getPlayRoomBy(player.getPlayId(), room.getHouseOwner(), room.getRoomId(), room.getCreateTime());
			if(playRoom == null) {
				JoinRoomLog joinRoomLog = joinRoomLogService.getJoinRoomLogBy(player.getPlayId(), room.getRoomId(), houseOwner.getPlayId(), room.getCreateTime());
				int id = playRoomService.insertPlayRoom(player, room, houseOwner, joinRoomLog);
				playRoom = playRoomService.selectById(id);
			}
			playRoom.setInsuranceProfitNum(playRoom.getInsuranceProfitNum()+insuranceProfit.getInsuranceProfit());
			return playRoomService.updateById(playRoom);
			
		} else {
			return true;
		}
	}
	
}
