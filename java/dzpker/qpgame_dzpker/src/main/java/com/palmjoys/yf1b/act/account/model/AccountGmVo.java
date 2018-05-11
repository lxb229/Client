package com.palmjoys.yf1b.act.account.model;

import java.util.ArrayList;
import java.util.List;

public class AccountGmVo {
	//列表总数
	public int totalNum;
	//列表数据
	public List<AccountAttribVo> items;	
	
	public AccountGmVo(){
		this.items = new ArrayList<>();
	}
}
