package com.palmjoys.yf1b.act.dzpker.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//个人输赢数据
public class TablePlayerWinScoreDetailsVo {
	//当前筹码
	public String currMoney;
	//总购买筹码
	public String buyTotalMoney;
	//输赢列表
	public List<WinScoreDetailsItem> items;
	
	public TablePlayerWinScoreDetailsVo(){
		this.currMoney = "0";
		this.buyTotalMoney = "0";
		this.items = new ArrayList<>();
	}
	
	public void addItem(int gameNum, long winScore){
		WinScoreDetailsItem item = new WinScoreDetailsItem();
		item.gameNum = gameNum;
		item.winScore = String.valueOf(winScore);
		
		this.items.add(item);
	}
	
	public void sort(){
		this.items.sort(new Comparator<WinScoreDetailsItem>(){
			@Override
			public int compare(WinScoreDetailsItem arg0, WinScoreDetailsItem arg1) {
				if(arg0.gameNum > arg1.gameNum){
					return 1;
				}else if(arg0.gameNum > arg1.gameNum){
					return -1;
				}
				return 0;
			}
		});
	}
	
	
	public class WinScoreDetailsItem{
		//局数
		public int gameNum;
		//输赢分数
		public String winScore;
	}
}

