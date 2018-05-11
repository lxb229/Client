package com.palmjoys.yf1b.act.majiang.model;

//游戏统计数据
public class GameStatisticsAttrib {
	//自摸次数
	public int totalZimo=0;
	//别人点我炮次数
	public int totalDianPaoHu=0;
	//我点炮次数
	public int totalMyDianPao=0;
	//暗杠次数
	public int totalAngang=0;
	//明杠次数
	public int totalBagang=0;
	//查叫次数
	public int totalChaJiao=0;
	
	//单局结算统计数据
	//结算总番数
	public int huPaiRate;
	//单局输赢分数
	public int onceScore;
	//我暗杠次数
	public int anGangNum;
	//我巴杠次数
	public int baGangNum;
	//别人点我直杠次数
	public int dianGangNum;
	//被暗杠次数
	public int outAnGang;
	//被巴杠次数
	public int outBaGang;
	//被点杠次数
	public int outDianGang;
	//带根数
	public int daiGengNum;
	//听牌番数
	public int tingPaiRate;
	//单局点炮被自摸描述
	public String outDesc;
	//巴到烫次数
	public int jiajiaYouNum;
	//被巴到烫数
	public int outJiajiaYouNum;
	
	public void reset(){
		this.huPaiRate = 0;
		this.onceScore = 0;
		this.anGangNum = 0;
		this.baGangNum = 0;
		this.dianGangNum = 0;
		this.outAnGang = 0;
		this.outBaGang = 0;
		this.outDianGang = 0;
		this.daiGengNum = 0;
		this.tingPaiRate = 0;
		this.outDesc = "";
		this.jiajiaYouNum = 0;
		this.outJiajiaYouNum = 0;
	}
}
