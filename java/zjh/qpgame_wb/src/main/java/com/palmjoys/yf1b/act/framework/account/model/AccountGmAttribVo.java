package com.palmjoys.yf1b.act.framework.account.model;

import java.util.ArrayList;
import java.util.List;

public class AccountGmAttribVo {
	//总记录数
	public int totalNum;
	//记录列表
	public List<AccountGmItem> items = new ArrayList<>();
	
	public void addItem(String starNO, String nick, int state, String createTime, 
			long roomCard, String phone, String lastTime){
		AccountGmItem item = new AccountGmItem();
		item.starNO = starNO;
		item.nick = nick;
		item.state = state;
		item.createTime = createTime;
		item.roomCard = String.valueOf(roomCard);
		item.phone = phone;
		item.lastTime = lastTime;
		items.add(item);
	}
	
	public class AccountGmItem{
		//明星号
		public String starNO;
		//角色呢称
		public String nick;
		//状态(0=正常,-1=冻结)
		public int state;
		//创建时间
		public String createTime;
		//角色房卡
		public String roomCard;
		//帮定的手机号
		public String phone;
		//最后在线时间
		public String lastTime;
	}
}
