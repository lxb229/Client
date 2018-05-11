package com.palmjoys.yf1b.act.majiang.manager;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.majiang.model.GameDataAttrib;

@Component
public class GameDataManager {	
	//玩家游戏动态数据列表
	private ConcurrentHashMap<Long, GameDataAttrib> gameDataMap = new ConcurrentHashMap<Long, GameDataAttrib>();
	
	public GameDataAttrib getGameData(long accountId){
		GameDataAttrib retData = gameDataMap.get(accountId);
		if(null == retData){
			retData = new GameDataAttrib();
		}
		gameDataMap.put(accountId, retData);
		return retData;
	}
	
	public GameDataAttrib getGameDataClone(long accountId){
		GameDataAttrib retVo = new GameDataAttrib();
		GameDataAttrib orgData = getGameData(accountId);
		retVo.tableId = orgData.tableId;
		retVo.onLine = orgData.onLine;
		retVo.robot = orgData.robot;
		return retVo;
	}
	
	public void remove(long accountId){
		gameDataMap.remove(accountId);
	}
}
