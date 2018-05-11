package com.wangzhixuan.model.vo;

import com.wangzhixuan.commons.utils.JsonUtils;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 游戏数据Vo
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
public class GamePlayerVo implements Serializable {

    private static final long serialVersionUID = 1L;
	
	 /**
     * 创建房间明星号
     */
	private String create_start_no;
    /**
     * 创建时间
     */
	private String create_time;
    /**
     * 消耗房卡明星号
     */
	private String consume_start_no;
    /**
     * 消耗房卡数量
     */
	private String amount;
    /**
     * 牌局参与者集合
     */
	private List<String> playerList;

	public String getCreate_start_no() {
		return create_start_no;
	}

	public void setCreate_start_no(String create_start_no) {
		this.create_start_no = create_start_no;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getConsume_start_no() {
		return consume_start_no;
	}

	public void setConsume_start_no(String consume_start_no) {
		this.consume_start_no = consume_start_no;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public List<String> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(List<String> playerList) {
		this.playerList = playerList;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
