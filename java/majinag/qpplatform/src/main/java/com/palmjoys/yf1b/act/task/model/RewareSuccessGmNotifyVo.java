package com.palmjoys.yf1b.act.task.model;

import java.util.List;

import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;

public class RewareSuccessGmNotifyVo {
	//抽中物品序号(即第几号牌子)
	public int rewareIndex;
	//是否中奖(0=未中奖,1=中奖)
	public int winning;
	//出库单号
	public String warehouseOut;
	//下发的邮件内容信息
	//邮件标题
	public String title;
	//邮件内容
	public String content;
	//邮件附件
	public List<GameObject> attachment;
	//邮件附件过期时间
	public long attachmentVaildTime;
	//刷新消耗
	public int refshCost;
	//抽奖消耗
	public int drawCost;
	//参与抽奖的奖励列表
	public List<RewareSilverItem> rewareList; 
}
