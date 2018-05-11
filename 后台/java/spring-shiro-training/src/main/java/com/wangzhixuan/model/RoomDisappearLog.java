package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 房间销毁日志记录
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-19
 */
@TableName("room_disappear_log")
public class RoomDisappearLog extends Model<RoomDisappearLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 房间id
     */
	@TableField("room_id")
	private Integer roomId;
    /**
     * 房主id
     */
	@TableField("house_owner")
	private String houseOwner;
    /**
     * 房间销毁时间
     */
	@TableField("disappear_time")
	private Date disappearTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getHouseOwner() {
		return houseOwner;
	}

	public void setHouseOwner(String houseOwner) {
		this.houseOwner = houseOwner;
	}

	public Date getDisappearTime() {
		return disappearTime;
	}

	public void setDisappearTime(Date disappearTime) {
		this.disappearTime = disappearTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
