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
 * 金币兑换商品记录
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-11
 */
@TableName("gold_log")
public class GoldLog extends Model<GoldLog> {

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
     * 消耗数量
     */
	private Integer consume;
    /**
     * 商品
     */
	private Integer commodity;
    /**
     * 状态(0=待发货 1=已发货)
     */
	private Integer status;
    /**
     * 出库单
     */
	@TableField("warehouse_out")
	private Integer warehouseOut;
    /**
     * 姓名
     */
	@TableField("player_name")
	private String playerName;
    /**
     * 电话
     */
	@TableField("player_phone")
	private String playerPhone;
    /**
     * 地址
     */
	@TableField("player_address")
	private String playerAddress;
    /**
     * 快递公司
     */
	private String express;
    /**
     * 快递单号
     */
	@TableField("express_code")
	private String expressCode;
    /**
     * 兑换时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 发货人
     */
	@TableField("dispose_id")
	private Integer disposeId;
    /**
     * 发货时间
     */
	@TableField("dispose_time")
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

	public Date getDisposeTime() {
		return disposeTime;
	}

	public void setDisposeTime(Date disposeTime) {
		this.disposeTime = disposeTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
