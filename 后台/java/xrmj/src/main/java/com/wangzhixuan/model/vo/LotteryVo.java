package com.wangzhixuan.model.vo;

import java.io.Serializable;
import java.util.List;

import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 红包领取返回对象
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class LotteryVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**刷新消耗*/
	public int refshCost;
	/**抽奖消耗*/
	public int drawCost;
	/**参与抽奖的奖励列表*/
	public List<RewareSilverItem> rewareList;
	
	public int getRefshCost() {
		return refshCost;
	}

	public void setRefshCost(int refshCost) {
		this.refshCost = refshCost;
	}

	public int getDrawCost() {
		return drawCost;
	}

	public void setDrawCost(int drawCost) {
		this.drawCost = drawCost;
	}

	public List<RewareSilverItem> getRewareList() {
		return rewareList;
	}

	public void setRewareList(List<RewareSilverItem> rewareList) {
		this.rewareList = rewareList;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}