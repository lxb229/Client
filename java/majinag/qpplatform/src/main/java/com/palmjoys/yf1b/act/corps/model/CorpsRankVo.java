package com.palmjoys.yf1b.act.corps.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CorpsRankVo {
	//排名列表
	public List<CorpsRankVoItem> ranks = new ArrayList<>();
	
	public void addItem(String nick, String headImg, int gameRoundNum, int score, int activeValue, long giveCardNum, String winMaxRate){
		CorpsRankVoItem item = new CorpsRankVoItem();
		item.nick = nick;
		item.headImg = headImg;
		item.gameRoundNum = gameRoundNum;
		item.score = score;
		item.activeValue = activeValue;
		item.giveCardNum = String.valueOf(giveCardNum);
		item.winMaxRate = winMaxRate;
		
		ranks.add(item);
	}
	
	
	public void sort(int type){
		ranks.sort(new Comparator<CorpsRankVoItem>(){
			@Override
			public int compare(CorpsRankVoItem o1, CorpsRankVoItem o2) {
				if(type == 1){
					//按活跃度排
					if(o1.activeValue > o2.activeValue){
						return -1;
					}else if(o1.activeValue < o2.activeValue){
						return 1;
					}
				}else if(type == 2){
					//按战绩排
					if(o1.score > o2.score){
						return -1;
					}else if(o1.score < o2.score){
						return 1;
					}
				}else{
					//按捐献房卡数排
					long nNum1 = Long.parseLong(o1.giveCardNum);
					long nNum2 = Long.parseLong(o2.giveCardNum);
					if(nNum1 > nNum2){
						return -1;
					}else if(nNum1 < nNum2){
						return 1;
					}
				}				
				return 0;
			}
		});
		
		//设置排名
		int rank = 1;
		for(CorpsRankVoItem rankItem : ranks){
			rankItem.rank = rank;
			rank++;
		}
	}
	
	public class CorpsRankVoItem{
		//排名
		public int rank;
		//呢称
		public String nick;
		//头像
		public String headImg;
		//游戏轮数
		public int gameRoundNum;
		//积分成绩
		public int score;
		//活跃度
		public int activeValue;
		//捐献房卡数
		public String giveCardNum;
		//大赢家率(百分比,直接显示)
		public String winMaxRate;
	}

}
