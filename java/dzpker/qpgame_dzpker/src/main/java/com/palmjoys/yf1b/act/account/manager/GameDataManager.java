package com.palmjoys.yf1b.act.account.manager;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.account.model.GameDataAttrib;

/**
 * 游戏数据管理类
 * */
@Component
public class GameDataManager {
	private Map<Long, GameDataAttrib> gameDataMap = new HashMap<>();

	public GameDataAttrib getAccountGameData(long accountId){
		if(0 >= accountId){
			return null;
		}
		GameDataAttrib dataAttrib = gameDataMap.get(accountId);
		if(null == dataAttrib){
			dataAttrib = new GameDataAttrib();
			gameDataMap.put(accountId, dataAttrib);
		}		
		return dataAttrib;
	}
	
	public GameDataAttrib getAccountGameDataClone(long accountId){
		GameDataAttrib dataAttrib = new GameDataAttrib();
		GameDataAttrib theAttrib = this.getAccountGameData(accountId);
		dataAttrib.pingVal = theAttrib.pingVal;
		dataAttrib.gameId = theAttrib.gameId;
		dataAttrib.tableId = theAttrib.tableId;
		dataAttrib.onLine = theAttrib.onLine;
		
		return dataAttrib;
	}
	
}
