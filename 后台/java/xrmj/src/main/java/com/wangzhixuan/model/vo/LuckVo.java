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
public class LuckVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**抽中物品序号(即第几号牌子)*/
	public int rewareIndex;
	/**是否中奖 1=中奖 0=未中奖*/
	public int winning;
	/**邮件标题*/
	public String title;
	/**邮件内容*/
	public String content;
	/**邮件附件*/
	public List<Object> attachment;
	/**刷新消耗*/
	public int refshCost;
	/**抽奖消耗*/
	public int drawCost;
	/**参与抽奖的奖励列表*/
	public List<RewareSilverItem> rewareList;

	public int getRewareIndex() {
		return rewareIndex;
	}

	public void setRewareIndex(int rewareIndex) {
		this.rewareIndex = rewareIndex;
	}

	public int getWinning() {
		return winning;
	}

	public void setWinning(int winning) {
		this.winning = winning;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Object> getAttachment() {
		return attachment;
	}

	public void setAttachment(List<Object> attachment) {
		this.attachment = attachment;
	}

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