package com.palmjoys.yf1b.act.dzpker.model;

import java.util.ArrayList;
import java.util.List;

public class WinScoreAttribItem {
	//本局分数
	public long score;
	//亮牌状态(0=不亮牌)
	public int showCardState;
	//手牌
	public List<Integer> playerHandCards;
	
	public WinScoreAttribItem(){
		this.score = 0;
		this.showCardState = 0;
		this.playerHandCards = new ArrayList<>();
	}
}
