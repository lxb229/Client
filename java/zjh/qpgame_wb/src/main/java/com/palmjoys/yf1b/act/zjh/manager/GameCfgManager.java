package com.palmjoys.yf1b.act.zjh.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.zjh.resource.RoomConfig;

@Component
public class GameCfgManager {
	@Static
	private Storage<Integer, RoomConfig> roomCfgs;
	

	public Collection<RoomConfig> getAll(){
		return roomCfgs.getAll();
	}
	
	public RoomConfig getCfg(int cfgId){
		return roomCfgs.get(cfgId, false);
	}
	
	public List<RoomConfig> getAllOfType(int type){
		List<RoomConfig> retList = new ArrayList<>();
		Collection<RoomConfig> cfgs = roomCfgs.getAll();
		for(RoomConfig cfg : cfgs){
			if(cfg.getTableType() == type){
				retList.add(cfg);
			}
		}
		
		return retList;
	}
	
	public int getAllCfgRobotNum(){
		int retNum = 0;
		Collection<RoomConfig> cfgs = roomCfgs.getAll();
		for(RoomConfig cfg : cfgs){
			retNum += cfg.getRobotNum();
		}
		
		return retNum;
	}
}
