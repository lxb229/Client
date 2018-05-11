package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 玩家账号余额
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
@TableName("player_money")
public class PlayerMoney extends Model<PlayerMoney> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 玩家明星号
     */
	@TableField("start_no")
	private String startNo;
    /**
     * 玩家房卡数量
     */
	@TableField("room_card")
	private Integer roomCard;
    /**
     * 玩家金币数量
     */
	@TableField("gold_coin")
	private Integer goldCoin;
    /**
     * 玩家银币数量
     */
	@TableField("silver_coin")
	private Integer silverCoin;
	/**
     * APP充值金额
     */
	@TableField("app_pay")
	private BigDecimal appPay;
    /**
     * 后台购买金额
     */
	@TableField("offline_pay")
	private BigDecimal offlinePay;
    /**
     * web充值
     */
	@TableField("web_pay")
	private BigDecimal webPay;
    /**
     * 微信公众号充值
     */
	@TableField("wechat_pay")
	private BigDecimal wechatPay;
    /**
     * 消耗房卡数量
     */
	@TableField("use_room_card")
	private Integer useRoomCard;
    /**
     * 玩家历史总金币数量
     */
	@TableField("all_gold")
	private Integer allGold;
    /**
     * 玩家历史总银币数量
     */
	@TableField("all_silver")
	private Integer allSilver;

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

	public Integer getRoomCard() {
		return roomCard;
	}

	public void setRoomCard(Integer roomCard) {
		this.roomCard = roomCard;
	}

	public Integer getGoldCoin() {
		return goldCoin;
	}

	public void setGoldCoin(Integer goldCoin) {
		this.goldCoin = goldCoin;
	}

	public Integer getSilverCoin() {
		return silverCoin;
	}

	public void setSilverCoin(Integer silverCoin) {
		this.silverCoin = silverCoin;
	}

	public BigDecimal getAppPay() {
		return appPay;
	}

	public void setAppPay(BigDecimal appPay) {
		this.appPay = appPay;
	}

	public BigDecimal getOfflinePay() {
		return offlinePay;
	}

	public void setOfflinePay(BigDecimal offlinePay) {
		this.offlinePay = offlinePay;
	}

	public BigDecimal getWebPay() {
		return webPay;
	}

	public void setWebPay(BigDecimal webPay) {
		this.webPay = webPay;
	}

	public BigDecimal getWechatPay() {
		return wechatPay;
	}

	public void setWechatPay(BigDecimal wechatPay) {
		this.wechatPay = wechatPay;
	}

	public Integer getUseRoomCard() {
		return useRoomCard;
	}

	public void setUseRoomCard(Integer useRoomCard) {
		this.useRoomCard = useRoomCard;
	}

	public Integer getAllGold() {
		return allGold;
	}

	public void setAllGold(Integer allGold) {
		this.allGold = allGold;
	}

	public Integer getAllSilver() {
		return allSilver;
	}

	public void setAllSilver(Integer allSilver) {
		this.allSilver = allSilver;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
