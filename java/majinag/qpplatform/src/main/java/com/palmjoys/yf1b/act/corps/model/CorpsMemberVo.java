package com.palmjoys.yf1b.act.corps.model;

import java.util.ArrayList;
import java.util.List;

public class CorpsMemberVo {
	//成员列表
	public List<CorpsMemberVoItem> members;
	
	public CorpsMemberVo(){
		this.members = new ArrayList<>();
	}
	
	public void addItem(long accountId, String nick, String headImg, String reson, int isOwner){
		CorpsMemberVoItem item = new CorpsMemberVoItem();
		item.accountId = String.valueOf(accountId);
		item.nick = nick;
		item.headImg = headImg;
		item.reson = reson;
		item.isOwner = isOwner;		
		this.members.add(item);
	}
	
	public class CorpsMemberVoItem{
		//帐号唯一Id
		public String accountId;
		//呢称
		public String nick;
		//头像
		public String headImg;
		//原因
		public String reson;
		//馆主标识(0=成员,1=馆主)
		public int isOwner;
	}

}
