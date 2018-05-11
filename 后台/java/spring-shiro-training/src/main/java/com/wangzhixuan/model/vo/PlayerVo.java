package com.wangzhixuan.model.vo;

import com.wangzhixuan.commons.utils.JsonUtils;

import java.io.Serializable;

/**
 * <p>
 * 玩家表
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
public class PlayerVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 玩家id/玩家明星号
     */
	private String playId;
    /**
     * 玩家账户状态
     */
	private Integer playStatus;
    /**
     * 注册时间
     */
	private Long createTime;
    /**
     * 玩家昵称
     */
	private String playNick;
    /**
     * 注册类型
     */
	private Integer createType;
   
	public String getPlayId() {
		return playId;
	}

	public void setPlayId(String playId) {
		this.playId = playId;
	}

	public Integer getPlayStatus() {
		return playStatus;
	}

	public void setPlayStatus(Integer playStatus) {
		this.playStatus = playStatus;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getPlayNick() {
		return playNick;
	}

	public void setPlayNick(String playNick) {
		this.playNick = playNick;
	}

	public Integer getCreateType() {
		return createType;
	}

	public void setCreateType(Integer createType) {
		this.createType = createType;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
