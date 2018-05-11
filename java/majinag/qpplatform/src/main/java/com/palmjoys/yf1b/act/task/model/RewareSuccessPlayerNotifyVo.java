package com.palmjoys.yf1b.act.task.model;

import java.util.List;

public class RewareSuccessPlayerNotifyVo {
	//抽中物品序号(即第几号牌子)
	public int rewareIndex;
	//是否中奖(0=未中奖,1=中奖)
	public int winning;
	//刷新消耗
	public int refshCost;
	//抽奖消耗
	public int drawCost;
	//参与抽奖的奖励列表
	public List<RewareSilverItem> rewareList;
}
