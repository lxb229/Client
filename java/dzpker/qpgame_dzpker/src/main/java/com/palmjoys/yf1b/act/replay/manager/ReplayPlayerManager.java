package com.palmjoys.yf1b.act.replay.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.account.entity.RoleEntity;
import com.palmjoys.yf1b.act.account.manager.RoleEntityManager;
import com.palmjoys.yf1b.act.replay.entity.ReplayPlayerEntity;
import com.palmjoys.yf1b.act.replay.entity.ReplayTableEntity;
import com.palmjoys.yf1b.act.replay.model.RecordItemVo;
import com.palmjoys.yf1b.act.replay.model.RecordScoreAttrib;
import com.palmjoys.yf1b.act.replay.model.RecordVo;
import com.palmjoys.yf1b.act.replay.resource.RecordConfig;

@Component
public class ReplayPlayerManager {
	@Inject
	private EntityMemcache<Long, ReplayPlayerEntity> replayPlayerCache;
	@Autowired
	private ReplayTableManager replayTableManager;
	@Autowired
	private RoleEntityManager roleEntityManager;
	@Static
	private Storage<Integer, RecordConfig> recordCfgs;
	
	public ReplayPlayerEntity loadOrCreate(long accountId){
		return replayPlayerCache.loadOrCreate(accountId, new EntityBuilder<Long, ReplayPlayerEntity>(){

			@Override
			public ReplayPlayerEntity createInstance(Long pk) {
				return ReplayPlayerEntity.valueOf(accountId);
			}
		});
	}
	
	public RecordVo replayQuery(long accountId){
		RecordVo retVo = new RecordVo();
		
		ReplayPlayerEntity playerEntity = this.loadOrCreate(accountId);
		List<Integer> gamedTables = playerEntity.getGamedtablesList();
		List<Integer> delIds = new ArrayList<>();
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		long saveTime = recordCfgs.get(1, false).getRecordSaveTime();
		saveTime = saveTime*60*60*1000;
		
		for(Integer tableId : gamedTables){
			ReplayTableEntity entity = replayTableManager.load(tableId);
			if(null == entity){
				delIds.add(tableId);
				continue;
			}
			long endTime = entity.getRecordTime() + saveTime;
			if(currTime >= endTime){
				delIds.add(tableId);
				continue;
			}
			List<Long> playerIds = entity.getTablePlayerList();
			if(playerIds.isEmpty()){
				delIds.add(tableId);
				continue;
			}
			Map<Integer, RecordScoreAttrib> recordMaps = entity.getRecordMap();
			
			RecordItemVo itemVo = new RecordItemVo();
			itemVo.tableId = tableId;
			itemVo.recordTime = String.valueOf(entity.getRecordTime());
			for(Long playerId : playerIds){
				RoleEntity roleEntity = roleEntityManager.findOf_accountId(playerId);
				if(null != roleEntity){
					int totalScore = 0;
					for(Map.Entry<Integer, RecordScoreAttrib> entry : recordMaps.entrySet()){
						RecordScoreAttrib value = entry.getValue();
						Integer scoreObj = value.scores.get(playerId);
						if(null != scoreObj){
							totalScore += scoreObj.intValue();
						}
					}
					
					itemVo.addSeatVo(roleEntity.getNick(), totalScore);
					retVo.items.add(itemVo);
				}
			}
		}
		
		for(Integer tableId : delIds){			
			ReplayTableEntity entity = replayTableManager.load(tableId);
			if(null == entity){
				continue;
			}
			List<Long> playerIds = entity.getTablePlayerList();
			for(Long playerId : playerIds){
				ReplayPlayerEntity tmpEntity = this.loadOrCreate(playerId);
				List<Integer> tmpGamedTables = tmpEntity.getGamedtablesList();
				tmpGamedTables.remove(tableId);
				tmpEntity.setGamedtablesList(tmpGamedTables);
			}
			
			replayTableManager.remove(tableId);
		}
		retVo.sort();
		delIds = null;
		return retVo;
	}
}
