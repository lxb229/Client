package com.palmjoys.yf1b.act.mall.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.palmjoys.yf1b.act.mall.resource.MallConfig;

public class MallVo {
	//商城物品列表
	public List<MallItemAttrib> items;
	//商城货币交换比例
	public MallRateCfgVo rateCfgVo;

	public MallVo(){
		this.items = new ArrayList<>();
		this.rateCfgVo = new MallRateCfgVo();
	}
	
	public void addItem(MallConfig cfg){
		MallItemAttrib item = new MallItemAttrib();
		item.itemId = cfg.getItemId();
		item.itemDesc = cfg.getItemDesc();
		item.buyPrice = cfg.getBuyPrice();
		item.roomCardNum = cfg.getRoomCardNum();
		item.goldMoneyNum = cfg.getGoldMoneyNum();
		item.diamondNum = cfg.getDiamondNum();
		item.icon = cfg.getIcon();
		
		this.items.add(item);
	}
		
	public void sort(){
		this.items.sort(new Comparator<MallItemAttrib>(){
			@Override
			public int compare(MallItemAttrib arg0, MallItemAttrib arg1) {
				if(arg0.itemId > arg1.itemId){
					return -1;
				}else if(arg0.itemId < arg1.itemId){
					return 1;
				}
				return 0;
			}
		});
	}
}
