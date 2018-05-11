package com.wangzhixuan.model.vo;

import java.io.Serializable;
import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 红包领取返回对象
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class RedVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**金币数量*/
	public int goldMoney;
	/**银币数量*/
	public int silverMoney;
	
	public int getGoldMoney() {
		return goldMoney;
	}

	public void setGoldMoney(int goldMoney) {
		this.goldMoney = goldMoney;
	}

	public int getSilverMoney() {
		return silverMoney;
	}

	public void setSilverMoney(int silverMoney) {
		this.silverMoney = silverMoney;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}