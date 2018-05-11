package com.wangzhixuan.model.vo;

import java.io.Serializable;
import java.util.Date;

import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 红包领取返回对象
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class GoldLogVo implements Serializable {
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
     * 消耗数量
     */
	private Integer consume;
    /**
     * 商品
     */
	private Integer commodity;
	/**
	 * 商品名称
	 */
	private String commodityName;
    /**
     * 状态(0=待发货 1=已发货)
     */
	private Integer status;
    /**
     * 出库单
     */
	private Integer warehouseOut;
	/**
	 * 出库单号
	 */
	private String warehouseOutNo;
    /**
     * 姓名
     */
	private String playerName;
    /**
     * 电话
     */
	private String playerPhone;
    /**
     * 地址
     */
	private String playerAddress;
    /**
     * 快递公司
     */
	private String express;
    /**
     * 快递单号
     */
	private String expressCode;
    /**
     * 兑换时间
     */
	private Date createTime;
    /**
     * 发货人
     */
	private Integer disposeId;
	/**
	 * 发货人名称
	 */
	private String disposeName;
    /**
     * 发货时间
     */
	private Date disposeTime;
	
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

	public Integer getConsume() {
		return consume;
	}

	public void setConsume(Integer consume) {
		this.consume = consume;
	}

	public Integer getCommodity() {
		return commodity;
	}

	public void setCommodity(Integer commodity) {
		this.commodity = commodity;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerPhone() {
		return playerPhone;
	}

	public void setPlayerPhone(String playerPhone) {
		this.playerPhone = playerPhone;
	}

	public String getPlayerAddress() {
		return playerAddress;
	}

	public void setPlayerAddress(String playerAddress) {
		this.playerAddress = playerAddress;
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getDisposeId() {
		return disposeId;
	}

	public void setDisposeId(Integer disposeId) {
		this.disposeId = disposeId;
	}

	public String getDisposeName() {
		return disposeName;
	}

	public void setDisposeName(String disposeName) {
		this.disposeName = disposeName;
	}

	public Date getDisposeTime() {
		return disposeTime;
	}

	public void setDisposeTime(Date disposeTime) {
		this.disposeTime = disposeTime;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}