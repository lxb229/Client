package com.palmjoys.yf1b.act.dzpker.model;

import java.util.ArrayList;
import java.util.List;

public class TableVo {
	//桌子Id
	public int tableId;
	//桌子创建玩家
	public String createPlayer;
	//桌子名称
	public String tableName;
	//小盲注
	public int smallBlind;
	//大盲注
	public int bigBlind;
	//最小加入筹码
	public int joinChip;
	//筹码购入上限
	public int buyMaxChip;
	//桌子底牌
	public List<Integer> tableHandCards = new ArrayList<>();
	//座位列表
	public List<SeatVo> seats = new ArrayList<>();
	//桌子当前游戏状态
	public int gameState;
	//动作总时间(毫秒)
	public int actTotalTime;
	//服务器时间
	public String svrTime;
	//动作到期时间
	public String actTime;
	//当前表态座位
	public int btIndex;
	//本局游戏庄家位置
	public int bankerIndex;
	//桌子是否开始运行
	public int start;
	//桌子游戏局数
	public int gameNum;
	//小盲位置
	public int smallSeatIndex;
	//大盲位置
	public int bigSeatIndex;
	//桌子创建时间
	public String createTime;
	//桌子到期时间
	public String vaildTime;
	//本轮桌子最大下注额
	public String maxBetMoney;
	//上一个表态位置
	public int prevBtIndex;
	//亮牌座位(-1=无亮牌座位)
	public int showCardSeatIndex;
	//所有池子总金额
	public String poolMoneys;
	//池子列表(第一个为主池,其余为边池)
	public List<String> pools = new ArrayList<>();
	//单局结算数据
	public List<SettlementOnceVo> settlementOnceList = new ArrayList<>();
	//购买保险状态数据
	public InsuranceStateAttrib insuranceStateAttrib;
	//秀牌标志(0=未秀过牌,1=已秀牌)
	public int bShowCardFlag;
	
}
