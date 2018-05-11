package com.palmjoys.yf1b.act.replay.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.account.entity.RoleEntity;
import com.palmjoys.yf1b.act.account.manager.RoleEntityManager;
import com.palmjoys.yf1b.act.replay.entity.ReplayPlayerEntity;
import com.palmjoys.yf1b.act.replay.entity.ReplayTableEntity;
import com.palmjoys.yf1b.act.replay.model.RecordDetailedVo;
import com.palmjoys.yf1b.act.replay.model.RecordItemVo;
import com.palmjoys.yf1b.act.replay.model.RecordScoreAttrib;
import com.palmjoys.yf1b.act.replay.model.RecordVo;
import com.palmjoys.yf1b.act.replay.resource.RecordConfig;

@Component
public class ReplayTableManager {
	@Inject
	private EntityMemcache<Integer, ReplayTableEntity> replayTableCache;
	@Autowired
	private Querier querier;
	@Autowired
	private RoleEntityManager roleEntityManager;
	@Autowired
	private ReplayPlayerManager replayPlayerManager;
	@Static
	private Storage<Integer, RecordConfig> recordCfgs;
	
	public ReplayTableEntity loadOrCreate(int tableId){
		return replayTableCache.loadOrCreate(tableId, new EntityBuilder<Integer, ReplayTableEntity>(){
			@Override
			public ReplayTableEntity createInstance(Integer pk) {
				return ReplayTableEntity.valueOf(tableId);
			}
		});
	}
	
	public ReplayTableEntity load(int tableId){
		return replayTableCache.load(tableId);
	}
	
	public void remove(int tableId){
		replayTableCache.remove(tableId);
	}
	
	public RecordVo replayQuery(String corpsId){
		RecordVo retVo = new RecordVo();
		List<Integer> ids = new ArrayList<>();
		List<Integer> delIds = new ArrayList<>();
		//全查全库后查缓存
		String querySql = "SELECT A.tableId FROM ReplayTableEntity AS A WHERE A.corpsId='"+corpsId+"' ORDER BY A.recordTime DESC";
		List<Object> retObjects = querier.listBySqlLimit(ReplayTableEntity.class, Object.class, querySql, 0, 10000);
		for(Object obj : retObjects){
			ids.add((Integer)obj);
		}
		List<ReplayTableEntity> retEntitys = null;
		retEntitys = replayTableCache.getFinder().find(ReplayFilterManager.Instance().createFilter_ReplayFilter_CorpsId(corpsId));
		for(ReplayTableEntity entity : retEntitys){
			if(ids.contains(entity.getTableId()) == false){
				ids.add(entity.getTableId());
			}
		}
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		long saveTime = recordCfgs.get(1, false).getRecordSaveTime();
		saveTime = saveTime*60*60*1000;
		for(Integer tableId : ids){
			ReplayTableEntity entity = this.load(tableId);
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
			ReplayTableEntity entity = this.load(tableId);
			if(null == entity){
				continue;
			}
			List<Long> playerIds = entity.getTablePlayerList();
			for(Long playerId : playerIds){
				ReplayPlayerEntity playerEntity = replayPlayerManager.loadOrCreate(playerId);
				List<Integer> gamedTables = playerEntity.getGamedtablesList();
				gamedTables.remove(tableId);
				playerEntity.setGamedtablesList(gamedTables);
			}
			
			this.remove(tableId);
		}
		retVo.sort();
		ids = null;
		delIds = null;
		return retVo;
	}
	
	public RecordDetailedVo queryDetailed(int tableId){
		RecordDetailedVo retVo = new RecordDetailedVo();
		retVo.tableId = tableId;
		
		ReplayTableEntity replayTableEntity = this.load(tableId);
		List<Long> gamedPlayers = replayTableEntity.getTablePlayerList();
		Map<Integer, RecordScoreAttrib> reordMap = replayTableEntity.getRecordMap();
		for(Long accountId : gamedPlayers){
			RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountId);
			retVo.nicks.add(roleEntity.getNick());
		}
		
		List<RecordScoreAttrib> scoreAttribs = new ArrayList<>();
		scoreAttribs.addAll(reordMap.values());
		for(RecordScoreAttrib scoreAttrib : scoreAttribs){
			List<Integer> tmpScores = new ArrayList<>();
			for(Long accountId : gamedPlayers){
				Integer scoreObj = scoreAttrib.scores.get(accountId);
				if(null == scoreObj){
					tmpScores.add(0);
				}else{
					tmpScores.add(scoreObj);
				}
			}
			retVo.addItem(scoreAttrib.gameNum, scoreAttrib.recordTime, tmpScores, scoreAttrib.recordFile);
		}
		retVo.sort();
		return retVo;
	}
	
}
