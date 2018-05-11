package com.wangzhixuan.model.vo;

import java.io.Serializable;
import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 参与抽奖的奖励
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class RewareSilverItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**物品顺序号*/
	private Integer index;
	/**物品名称*/
	private String itemName;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}