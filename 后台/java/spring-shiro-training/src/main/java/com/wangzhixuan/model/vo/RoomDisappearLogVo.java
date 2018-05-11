package com.wangzhixuan.model.vo;

import com.wangzhixuan.commons.utils.JsonUtils;
import java.io.Serializable;

/**
 * <p>
 * 房间销毁日志记录
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-19
 */
public class RoomDisappearLogVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 房间id
     */
	private Integer roomId;
    /**
     * 房主id
     */
	private String houseOwner;
    /**
     * 房间销毁时间
     */
	private Long disappearTime;

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

	public Long getDisappearTime() {
		return disappearTime;
	}

	public void setDisappearTime(Long disappearTime) {
		this.disappearTime = disappearTime;
	}
	
	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
	
}
