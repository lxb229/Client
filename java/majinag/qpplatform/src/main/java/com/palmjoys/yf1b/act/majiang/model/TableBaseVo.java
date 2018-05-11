package com.palmjoys.yf1b.act.majiang.model;

import java.util.ArrayList;
import java.util.List;

public class TableBaseVo {
	//桌子Id
	public int tableId;
	//桌子创建玩家Id
	public String createPlayer;
	//桌子聊天类型(0=一般房,1=聊天房,2=视频房)
	public int tableChatType;
	//桌子所属帮会Id("0"=无帮会)
	public String corpsId;
	//庄家位置
	public int bankerIndex;
	//当前局数
	public int currGameNum;
	//最大游戏局数
	public int maxGameNum;
	//当前桌子牌张数
	public int tableCardNum;
	//服务器当前时间
	public String svrTime;
	//当前动作时间
	public String actTime;
	//桌子游戏状态
	public int gameState;
	//表态座位下标
	public int btIndex;
	//上个表态座位下标
	public int prevBtIndex;
	//桌子规则描述文本
	public String ruleShowDesc;
	//换牌方式(0=[0->1,1->2,2->3,3->0],1=[0->3,3->2,2->1,1->0],2=[0->2,1->3])
	public int swapCardType;
	//解散桌子的申请人
	public String destoryQuestPlayer;
	//是否有下一局
	public int nextGame;
	//桌子配置项Id
	public int cfgId;
	//记录Id
	public String recordId;
	//听牌是否提示(0=不提示,1=提示)
	public int tingTips;
	//是否幺鸡任用(0=不替换,1=任用)
	public int yaojiReplace;
	//手牌张数
	public int handCardNum;
	//最大漂牌数
	public int maxPiaoPaiNum;
	//躺或摆牌可胡的牌
	public List<Integer> tangCanHuList = new ArrayList<>();
		
	public void reset(){
		this.tableCardNum = 0;
	}
}
