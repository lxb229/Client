package com.palmjoys.yf1b.act.corps.model;

import java.util.ArrayList;
import java.util.List;

public class CorpsTableItemAttribVo {
	//桌子号
	public int tableId;
	//游戏名称
	public String gameName;
	//桌子规则描述
	public String ruleShowDesc;
	//桌子玩家信息
	public List<CorpsTablePlayerAttribVo> seats;
	//是否有密码(0=无密码,1=有密码)
	public int password;
	
	public CorpsTableItemAttribVo(){
		this.seats = new ArrayList<>();
	}
}
