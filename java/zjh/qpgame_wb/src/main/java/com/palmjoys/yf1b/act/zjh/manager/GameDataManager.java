package com.palmjoys.yf1b.act.zjh.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.zjh.model.GameDataAttrib;

@Component
public class GameDataManager {
	//玩家游戏动态数据列表
	private Map<Long, GameDataAttrib> gameDataMap = new HashMap<Long, GameDataAttrib>();
	
	public GameDataAttrib getGameData(long accountId){
		GameDataAttrib retData = gameDataMap.get(accountId);
		if(null == retData){
			retData = new GameDataAttrib(accountId);
		}
		gameDataMap.put(accountId, retData);
		return retData;
	}
	
	public GameDataAttrib getGameDataClone(long accountId){
		GameDataAttrib retVo = new GameDataAttrib(accountId);
		GameDataAttrib orgData = getGameData(accountId);
		retVo.tableId = orgData.tableId;
		retVo.onLine = orgData.onLine;
		return retVo;
	}
	
	public GameDataAttrib getIdleRobot(){
		List<GameDataAttrib> retList = new ArrayList<>();
		Collection<GameDataAttrib> theList = gameDataMap.values();
		for(GameDataAttrib dataAttrib : theList){
			if(dataAttrib.robot == 1 && dataAttrib.tableId==0){
				retList.add(dataAttrib);
			}
		}
		if(retList.isEmpty() == false){
			int index = (int) ((Math.random()*100000)%retList.size());
			return retList.get(index);
		}
		
		return null;
	}
	
	public List<GameDataAttrib> getAllRobot(){
		List<GameDataAttrib> retList = new ArrayList<>();
		Collection<GameDataAttrib> theList = gameDataMap.values();
		for(GameDataAttrib dataAttrib : theList){
			if(dataAttrib.robot == 1){
				retList.add(dataAttrib);
			}
		}
		return retList;
	}
}
