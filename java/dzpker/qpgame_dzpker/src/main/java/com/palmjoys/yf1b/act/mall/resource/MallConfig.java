package com.palmjoys.yf1b.act.mall.resource;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

@ResourceType("mall")
public class MallConfig {
	@ResourceId
	private int itemId;
	//物品描述
	private String itemDesc;
	//购买RMB
	private int buyPrice;
	//购买成功后获取的房卡数
	private int roomCardNum;
	//购买成功后获取的金币数量
	private int goldMoneyNum;
	//购买成功后获取的钻石数
	private int diamondNum;
	//物品展示icon
	private String icon;
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public int getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(int buyPrice) {
		this.buyPrice = buyPrice;
	}
	public int getRoomCardNum() {
		return roomCardNum;
	}
	public void setRoomCardNum(int roomCardNum) {
		this.roomCardNum = roomCardNum;
	}
	public int getGoldMoneyNum() {
		return goldMoneyNum;
	}
	public void setGoldMoneyNum(int goldMoneyNum) {
		this.goldMoneyNum = goldMoneyNum;
	}
	public int getDiamondNum() {
		return diamondNum;
	}
	public void setDiamondNum(int diamondNum) {
		this.diamondNum = diamondNum;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
}
