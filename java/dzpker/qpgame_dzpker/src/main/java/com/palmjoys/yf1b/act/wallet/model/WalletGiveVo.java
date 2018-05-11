package com.palmjoys.yf1b.act.wallet.model;

import java.util.ArrayList;
import java.util.List;

public class WalletGiveVo {
	public List<WalletGiveInner> items = new ArrayList<>();
		
	public void addItem(long time, String nick, String starNO, int giveNum){
		WalletGiveInner item = new WalletGiveInner();
		item.giveTime = String.valueOf(time);
		item.content = "您向玩家 "+nick+" ("+starNO+"),赠送了"+giveNum+"张房卡";
		this.items.add(item);
	}
	
	public class WalletGiveInner{
		//赠送时间
		public String giveTime;
		//内容
		public String content;
	}

}
