package com.palmjoys.yf1b.act.task.model;

import java.util.ArrayList;
import java.util.List;

public class RewareSilverVo {
	//刷新消耗
	public int refshCost;
	//抽奖消耗
	public int drawCost;
	//参与抽奖的奖励列表
	public List<RewareSilverItem> rewareList = new ArrayList<>();
}
