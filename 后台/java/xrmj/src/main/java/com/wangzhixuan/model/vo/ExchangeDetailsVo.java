package com.wangzhixuan.model.vo;

import java.io.Serializable;
import java.util.List;

import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 兑换商品详情Vo
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class ExchangeDetailsVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**物品Id*/
	public int itemId;
	/**物品名称*/
	public String itemName;
	/**兑换金币价*/
	public int goldMoney;
	/**展示Icon图片*/
	public String icon;
	/**市场价*/
	public int price;
	/**库存量*/
	public int num;
	/**请情描述*/
	public String description;
	/**请情图片展示Url列表*/
	public List<String> descIcon;
	/**网店跳转地址*/
	public String jumpUrl;

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getGoldMoney() {
		return goldMoney;
	}

	public void setGoldMoney(int goldMoney) {
		this.goldMoney = goldMoney;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getDescIcon() {
		return descIcon;
	}

	public void setDescIcon(List<String> descIcon) {
		this.descIcon = descIcon;
	}

	public String getJumpUrl() {
		return jumpUrl;
	}

	public void setJumpUrl(String jumpUrl) {
		this.jumpUrl = jumpUrl;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}