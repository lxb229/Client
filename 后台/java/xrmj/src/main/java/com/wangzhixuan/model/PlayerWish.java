package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 玩家祝福值
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-29
 */
@TableName("player_wish")
public class PlayerWish extends Model<PlayerWish> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 玩家明星号
     */
	@TableField("start_no")
	private String startNo;
    /**
     * 祝福值
     */
	private Integer wish;
    /**
     * 祝福值等级
     */
	@TableField("wish_lv")
	private Integer wishLv;
    /**
     * 今日创建牌局次数
     */
	@TableField("create_party_number")
	private Integer createPartyNumber;
    /**
     * 今日购买房卡数量
     */
	@TableField("buy_number")
	private Integer buyNumber;
    /**
     * 红包领取有效参加牌局次数
     */
	@TableField("join_number")
	private Integer joinNumber;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStartNo() {
		return startNo;
	}

	public void setStartNo(String startNo) {
		this.startNo = startNo;
	}

	public Integer getWish() {
		return wish;
	}

	public void setWish(Integer wish) {
		this.wish = wish;
	}

	public Integer getWishLv() {
		return wishLv;
	}

	public void setWishLv(Integer wishLv) {
		this.wishLv = wishLv;
	}

	public Integer getCreatePartyNumber() {
		return createPartyNumber;
	}

	public void setCreatePartyNumber(Integer createPartyNumber) {
		this.createPartyNumber = createPartyNumber;
	}

	public Integer getBuyNumber() {
		return buyNumber;
	}

	public void setBuyNumber(Integer buyNumber) {
		this.buyNumber = buyNumber;
	}

	public Integer getJoinNumber() {
		return joinNumber;
	}

	public void setJoinNumber(Integer joinNumber) {
		this.joinNumber = joinNumber;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
