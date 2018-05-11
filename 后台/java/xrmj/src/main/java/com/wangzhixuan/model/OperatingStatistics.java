package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 运营统计
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
@TableName("operating_statistics")
public class OperatingStatistics extends Model<OperatingStatistics> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 玩家总量
     */
	@TableField("player_amount")
	private Integer playerAmount;
    /**
     * 销售房卡总量
     */
	@TableField("sales_roomcard")
	private Integer salesRoomcard;
    /**
     * 已使用房卡总量
     */
	@TableField("use_roomcard")
	private Integer useRoomcard;
    /**
     * 房卡利润
     */
	@TableField("roomcard_profit")
	private BigDecimal roomcardProfit;
    /**
     * 房卡毛利润
     */
	@TableField("roomcard_net_profit")
	private BigDecimal roomcardNetProfit;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPlayerAmount() {
		return playerAmount;
	}

	public void setPlayerAmount(Integer playerAmount) {
		this.playerAmount = playerAmount;
	}

	public Integer getSalesRoomcard() {
		return salesRoomcard;
	}

	public void setSalesRoomcard(Integer salesRoomcard) {
		this.salesRoomcard = salesRoomcard;
	}

	public Integer getUseRoomcard() {
		return useRoomcard;
	}

	public void setUseRoomcard(Integer useRoomcard) {
		this.useRoomcard = useRoomcard;
	}

	public BigDecimal getRoomcardProfit() {
		return roomcardProfit;
	}

	public void setRoomcardProfit(BigDecimal roomcardProfit) {
		this.roomcardProfit = roomcardProfit;
	}

	public BigDecimal getRoomcardNetProfit() {
		return roomcardNetProfit;
	}

	public void setRoomcardNetProfit(BigDecimal roomcardNetProfit) {
		this.roomcardNetProfit = roomcardNetProfit;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
