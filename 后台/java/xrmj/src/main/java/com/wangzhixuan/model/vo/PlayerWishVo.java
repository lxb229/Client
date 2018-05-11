package com.wangzhixuan.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 玩家统计对象Vo
 * @description：PlayerWishVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class PlayerWishVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 玩家明星号
	 */
	private String startNo;
	/**
	 * 玩家昵称
	 */
	private String nick;
	/**
	 * 玩家祝福值
	 */
	private Integer wish;
	/**
	 * 玩家房卡数量
	 */
	private Integer roomCard;
	/**
	 * 玩家支付总额
	 */
	private BigDecimal payAmount;
	/**
	 * 玩家APP内购
	 */
	private BigDecimal appPay;
	/**
	 * 玩家线下购买
	 */
	private BigDecimal offlinePay;
	/**
	 * 玩家web购买
	 */
	private BigDecimal webPay;
	/**
	 * 玩家公众号购买
	 */
	private BigDecimal wechatPay;
	/**
	 * 玩家消耗房卡
	 */
	private Integer useRoomCard;
	 /**
     * 玩家金币数量
     */
	private Integer goldCoin;
    /**
     * 玩家银币数量
     */
	private Integer silverCoin;
	/**
	 * 总金币数量
	 */
	private Integer allGold;
	/**
	 * 总银币数量
	 */
	private Integer allSilver;

	public String getStartNo() {
		return startNo;
	}

	public void setStartNo(String startNo) {
		this.startNo = startNo;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Integer getWish() {
		return wish;
	}

	public void setWish(Integer wish) {
		this.wish = wish;
	}

	public Integer getRoomCard() {
		return roomCard;
	}

	public void setRoomCard(Integer roomCard) {
		this.roomCard = roomCard;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
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
	public String toString() {
		return JsonUtils.toJson(this);
	}
}