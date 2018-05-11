package com.wangzhixuan.model.vo;

import java.io.Serializable;
import java.util.Date;

import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 银币抽奖记录Vo对象
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class SilverLogVo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
     * 表主键id
     */
	private Integer id;
    /**
     * 玩家明星号
     */
	private String startNo;
	/**
     * 玩家昵称
     */
	private String playerNick;
    /**
     * 商品
     */
	private Integer commodity;
    /**
     * 银币消耗数量
     */
	private Integer consume;
    /**
     * 抽奖等级
     */
	private Integer awardLv;
    /**
     * 抽奖等级名称
     */
	private String awardName;
    /**
     * 商品名称
     */
	private String commodityName;
    /**
     * 出库单
     */
	private Integer warehouseOut;
    /**
     * 出库单
     */
	private String warehouseOutNo;
    /**
     * 时间
     */
	private Date createTime;

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

	public String getPlayerNick() {
		return playerNick;
	}

	public void setPlayerNick(String playerNick) {
		this.playerNick = playerNick;
	}

	public Integer getCommodity() {
		return commodity;
	}

	public void setCommodity(Integer commodity) {
		this.commodity = commodity;
	}

	public Integer getConsume() {
		return consume;
	}

	public void setConsume(Integer consume) {
		this.consume = consume;
	}

	public Integer getAwardLv() {
		return awardLv;
	}

	public void setAwardLv(Integer awardLv) {
		this.awardLv = awardLv;
	}

	public String getAwardName() {
		return awardName;
	}

	public void setAwardName(String awardName) {
		this.awardName = awardName;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public Integer getWarehouseOut() {
		return warehouseOut;
	}

	public void setWarehouseOut(Integer warehouseOut) {
		this.warehouseOut = warehouseOut;
	}

	public String getWarehouseOutNo() {
		return warehouseOutNo;
	}

	public void setWarehouseOutNo(String warehouseOutNo) {
		this.warehouseOutNo = warehouseOutNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}