package com.palmjoys.yf1b.act.account.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 角色游戏属性数据
 * */
public class GameDataAttrib {
	//客户端与服务器ping值
	public int pingVal;
	//所在游戏Id(0=无游戏)
	public int gameId;
	//所在桌子Id(0=无桌子)
	public int tableId;
	//在线状态(0=不在线,1=在线)
	public int onLine;
	//加入过的房间记录
	public Map<Long, Long> joinedTableIdMap;
	//几轮不表态结算后踢出房间
	public int btKickNum;
	
	public GameDataAttrib(){
		this.pingVal = 0;
		this.gameId = 0;
		this.tableId = 0;
		this.onLine = 1;
		this.joinedTableIdMap = new HashMap<>();
		this.btKickNum = 0;
	}
}
