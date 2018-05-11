package com.wangzhixuan.model.vo;

import com.wangzhixuan.commons.utils.JsonUtils;

import java.io.Serializable;

/**
 * <p>
 * 玩家登陆日志
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
public class LoginLogVo  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 玩家ID或者明星号
     */
	private String playerId;
    /**
     * 类型1： 登录 0：登出
     */
	private Integer loginType;
	/**
	 * 创建时间
	 */
	private Long createTime;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
