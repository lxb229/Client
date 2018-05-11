package com.palmjoys.yf1b.act.dzpker.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//生涯数据信息
public class CareerVo {
	//获胜率
	public String winRate;
	//入局率
	public String seatDown;
	//亮牌率
	public String showCard;
	//加注率
	public String addChip;
	//弃牌率
	public String dropCards;
	//全下率
	public String fullBet;
	//历史数据
	public List<CareerHistory> historyList;
	
	public CareerVo(){
		this.winRate = "0.0";
		this.seatDown = "0.0";
		this.showCard = "0.0";
		this.addChip = "0.0";
		this.dropCards = "0.0";
		this.fullBet = "0.0";
		this.historyList = new ArrayList<>();
	}
	
	public void addHistoryItem(long recordId, long accountId, String headImg, String nick, String tableName, 
			long recordTime, long winMoney){
		CareerHistory item = new CareerHistory();
		item.recordId = String.valueOf(recordId);
		item.accountId = String.valueOf(accountId);
		item.headImg = headImg;
		item.nick = nick;
		item.tableName = tableName;
		item.recordTime = String.valueOf(recordTime);
		item.winMoney = String.valueOf(winMoney);
		
		this.historyList.add(item);
	}
	
	public void sort(){
		historyList.sort(new Comparator<CareerHistory>(){
			@Override
			public int compare(CareerHistory arg0, CareerHistory arg1) {
				long time1 = Long.parseLong(arg0.recordTime);
				long time2 = Long.parseLong(arg1.recordTime);
				if(time1 > time2){
					return 1;
				}else if(time1 < time2){
					return -1;
				}
				return 0;
			}
		});
	}
	
	public class CareerHistory{
		//桌子记录Id
		public String recordId;
		//房主帐号Id
		public String accountId;
		//头像
		public String headImg;
		//呢称
		public String nick;
		//房间名称
		public String tableName;
		//记录时间
		public String recordTime;
		//总输赢筹码
		public String winMoney;
	}
}
