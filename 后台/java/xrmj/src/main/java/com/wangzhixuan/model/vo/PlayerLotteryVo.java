package com.wangzhixuan.model.vo;

import java.io.Serializable;
import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 红包领取返回对象
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class PlayerLotteryVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 物品顺序号
	 */
	private Integer index;
	
	/**
     * 商品id
     */
	private Integer id;
	
    /**
     * 商品名称
     */
	private String itemName;
	
	/**
	 * 商品等级
	 */
	private Integer commodityLv;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getCommodityLv() {
		return commodityLv;
	}

	public void setCommodityLv(Integer commodityLv) {
		this.commodityLv = commodityLv;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}