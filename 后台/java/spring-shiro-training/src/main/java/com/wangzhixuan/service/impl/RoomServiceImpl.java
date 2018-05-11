package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.JoinRoomLog;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.Room;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.vo.RoomVo;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.utils.BeanUtils;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.mapper.RoomMapper;
import com.wangzhixuan.service.IJoinRoomLogService;
import com.wangzhixuan.service.IPlayerService;
import com.wangzhixuan.service.IRoomService;
import com.wangzhixuan.service.ISystemTaskService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
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
 * 房间表 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements IRoomService {

	@Autowired
	private RoomMapper roommapper;
	@Autowired
	private ISystemTaskService taskService;
	@Autowired
	private IPlayerService playerService;
	@Autowired
	private IJoinRoomLogService joinRoomLogService;
	@Autowired
	private PropertyConfigurer configurer;
	
	private Gson gson = new Gson();
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
        Page<Room> page = new Page<Room>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<Room> list = roommapper.selectRoomPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
	}

	@Override
	public void taskRoom(SystemTask task) {
		String content = "["+task.getJsonContent()+"]";
		List<RoomVo> taskList = gson.fromJson(content, new TypeToken<List<RoomVo>>(){}.getType());
		if(taskList != null && taskList.size() > 0 ) {
			Room room = new Room();
			BeanUtils.copyProperties(taskList.get(0), room);
			JSONObject json = JSONObject.parseObject(task.getJsonContent());
			room.setCreateTime(new Date(new Long(json.getLongValue("createTime"))));
			boolean success = this.insertRoom(room);
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
	public boolean insertRoom(Room room) {
		Boolean success = this.insert(room);
		if(success) {
			// 更新玩家的创建房间数据
			success = this.addCreateRoom(room);
		}
		return success;
	}
	
	@Override
	public boolean addCreateRoom(Room room) {
		Player player = playerService.selectPlayerBy(room.getHouseOwner());
		if(player != null) {
			player.setCreateRoomNum(player.getCreateRoomNum()+1);
			playerService.updateById(player);
			/**创建房间同时生成一条房主进入房间的数据*/
			JoinRoomLog joinRoomLog = new JoinRoomLog();
			joinRoomLog.setPlayerId(room.getHouseOwner());
			joinRoomLog.setRoomId(room.getRoomId());
			joinRoomLog.setHouseOwner(room.getHouseOwner());
			joinRoomLog.setJoinTime(new Date());
			return joinRoomLogService.insertJoinRoomLog(joinRoomLog);
		} else {
			return false;
		}
	}

	@Override
	public Room getRoomBy(String houseOwner, int roomId, Date createTime) {
		return roommapper.getRoomBy(houseOwner, roomId, createTime, configurer.getProperty("roomDays"));
	}

	@Override
	public Map<String, Object> selectRoomMap() {
		return roommapper.selectRoomData();
	}

	@Override
	public Map<String, Object> selectAllPartys() {
		return roommapper.selectAllPartys();
	}
	
}
