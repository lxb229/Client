package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.model.Room;
import com.wangzhixuan.model.SystemTask;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 房间表 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
public interface IRoomService extends IService<Room> {
	
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	/**
	 * 处理任务中的房间数据
	 * @param task
	 */
	void taskRoom(SystemTask task);
	/**
	 * 增加一个房间记录
	 * @param room
	 * @return
	 */
	boolean insertRoom(Room room);
	/**
	 * 增加玩家的创建房间数量
	 * @param room 房间数量
	 */
	boolean addCreateRoom(Room room);
	/**
	 * 获取房间对象
	 * @param houseOwner 房主
	 * @param roomId 房间id
	 * @param createTime 房间创建时间(时间三天以内)
	 * @return
	 */
	Room getRoomBy(String houseOwner, int roomId, Date createTime );
	/**
	 * 获取房间数据汇总
	 * @return
	 */
	Map<String, Object> selectRoomMap();
	
	/**
	 * 获取牌局总数
	 * @return
	 */
	Map<String, Object> selectAllPartys();
}
