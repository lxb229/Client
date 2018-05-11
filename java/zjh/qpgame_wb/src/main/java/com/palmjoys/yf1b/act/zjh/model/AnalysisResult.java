package com.palmjoys.yf1b.act.zjh.model;

import java.util.ArrayList;
import java.util.List;

public class AnalysisResult {
	//牌拆分结果
	public List<List<Byte>> cards;
	//牛点数
	public int nnNum = 0;
	//五花牛牛
	public boolean b5FlowerNN = false;
	
	//金花牌型分析(0=散牌,1=对子,2=连子,3=同花,4=青同花,5=飞机)
	public int cardType;
	//从大到小排序后的牌
	public List<Byte> sortedCards = new ArrayList<>();
}
