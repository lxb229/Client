package com.wangzhixuan.model.vo;

import com.wangzhixuan.commons.utils.JsonUtils;

import java.io.Serializable;

/**
 * <p>
 * 加入房间日志
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-15
 */
public class JoinRoomLogVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 玩家id
     */
	private String playerId;
    /**
     * 房间id
     */
	private Integer roomId;
    /**
     * 房主
     */
	private String houseOwner;
    /**
     * 加入房间的时间
     */
	private Long joinTime;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
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

	public Long getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Long joinTime) {
		this.joinTime = joinTime;
	}
	
	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
	
}
