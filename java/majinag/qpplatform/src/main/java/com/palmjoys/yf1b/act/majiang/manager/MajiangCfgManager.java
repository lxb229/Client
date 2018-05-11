package com.palmjoys.yf1b.act.majiang.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.majiang.resource.RoomItemConfig;
import com.palmjoys.yf1b.act.majiang.resource.RoomRuleConfig;

@Component
public class MajiangCfgManager {
	@Static
	private Storage<Integer, RoomItemConfig> roomItemCfgs;
	@Static
	private Storage<Integer, RoomRuleConfig> ruleItemCfgs;
	
	public List<RoomItemConfig> getAllRoomCfgs(){
		List<RoomItemConfig> retVo = new ArrayList<RoomItemConfig>();
		retVo.addAll(roomItemCfgs.getAll());
		return retVo;
	}
	
	public RoomItemConfig getRoomItemCfg(int itemId){
		return roomItemCfgs.get(itemId, false);
	}
	
	public RoomRuleConfig getRuleItemCfg(int ruleId){
		return ruleItemCfgs.get(ruleId, false);
	}
	
}
