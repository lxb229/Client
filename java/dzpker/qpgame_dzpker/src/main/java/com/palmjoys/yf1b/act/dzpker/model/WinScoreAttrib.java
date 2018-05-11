package com.palmjoys.yf1b.act.dzpker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//桌子每局输
public class WinScoreAttrib {
	//局数
	public int gameNum;
	//桌子底牌
	public List<Integer> tableCards;
	//本局参与了游戏的玩家输赢数据
	public Map<Long, WinScoreAttribItem> playerScoresMap;
	
	public WinScoreAttrib(){
		this.tableCards = new ArrayList<>();
		this.playerScoresMap = new HashMap<>();
	}
	
	public WinScoreAttribItem getPlayerScore(long accountId){
		WinScoreAttribItem obj = playerScoresMap.get(accountId);
		if(null == obj){
			obj = new WinScoreAttribItem();
		}
		return obj;
	}
	
	public void setPlayerScore(long accountId, WinScoreAttribItem item){
		playerScoresMap.put(accountId, item);
	}
}
