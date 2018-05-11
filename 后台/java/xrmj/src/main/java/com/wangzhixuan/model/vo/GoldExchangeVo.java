package com.wangzhixuan.model.vo;

import java.io.Serializable;
import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 红包领取返回对象
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class GoldExchangeVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**物品Id*/
	public int itemId;
	/**物品名称*/
	public String itemName;
	/**兑换金币价*/
	public int goldMoney;
	/**展示Icon图片*/
	public String icon;

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

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}