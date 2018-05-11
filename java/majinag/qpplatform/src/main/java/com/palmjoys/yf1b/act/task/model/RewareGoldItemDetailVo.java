package com.palmjoys.yf1b.act.task.model;

import java.util.List;

public class RewareGoldItemDetailVo {
	//物品Id
	public int itemId;
	//物品名称
	public String itemName;
	//兑换金币价
	public int goldMoney;
	//展示Icon图片
	public String icon;
	//市场价
	public int price;
	//库存量
	public int num;
	//请情描述
	public String description;
	//请情图片展示Url列表
	public List<String> descIcon;
	//网店跳转地址
	public String jumpUrl;
	//物品游戏中的类别
	public String type;
}
