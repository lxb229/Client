package com.palmjoys.yf1b.act.zjh.model;

import java.util.ArrayList;
import java.util.List;

import com.palmjoys.yf1b.act.zjh.model.SettlementAttrib.SettlementItemNN;
import com.palmjoys.yf1b.act.zjh.model.SettlementAttrib.SettlementItemZJH;

public class TableVo {
	//桌子Id
	public int tableId;
	//桌子类型(1=牛牛,2=金花)
	public int gameType;
	//游戏状态
	public int gameState;
	//庄家位置
	public String bankerId;
	//当前表态座位下标
	public int btIndex;
	//服务器时间
	public String svrTime;
	//动作到期时间
	public String actTime;
	//结算列表(牛牛)
	public List<SettlementItemNN> NNOverItems = new ArrayList<>();
	//结算列表(ZJH)
	public List<SettlementItemZJH> ZJHOverItems = new ArrayList<>();
	//座位数据
	public List<SeatVo> seats = new ArrayList<>();
	//总压注金额
	public int totalBetMoney;
	//底分
	public int baseScore;
	//单注最大压注上限
	public int onceMax;
	//轮数
	public int roundNum;
	//动作总时间(毫秒)
	public int actTotalTime;
	//比牌发起座位
	public int compareSrcIndex;
	//比牌目标座位
	public int compareDstIndex;
	//比牌结果(-1=源小于目标,0=相同,1=源大于目标)
	public int compareResult;
	//桌子上不看牌需下注金额
	public int unLookBetMoney;
	//桌子上看牌需下注金额
	public int lookBetMoney;
	//上个下注座位下标号
	public int prevSeatIndex;
	
}
