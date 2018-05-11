package com.palmjoys.yf1b.act.majiang.model.notify.table;

import java.util.ArrayList;
import java.util.List;

//桌子游戏状态消息数据
public class Notify_Table_GameStateVo {
	//游戏状态值
	public int state;
	//当前局数
	public int currGameNum;
	//当前桌子牌张数
	public int tableCardNum;
	//表态座位下标
	public int btIndex;
	//上个表态座位下标
	public int prevBtIndex;
	//躺或摆牌可胡的牌
	public List<Integer> tangCanHuList = new ArrayList<>();
	//服务器当前时间(毫秒时间)
	public String svrTime;
	//状态有效到期时间(毫秒时间)
	public String stateVaildTime;
	//状态总时长(毫秒时间)
	public String stateTime;
	//游戏状态相对应的游戏数据
	public Object stateData;
}
