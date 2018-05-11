package com.palmjoys.yf1b.act.dzpker.manager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.dzpker.entity.DzpkerPlayerRecordEntity;
import com.palmjoys.yf1b.act.dzpker.entity.DzpkerTableRecordEntity;
import com.palmjoys.yf1b.act.dzpker.model.StatisticsAttrib;

@Component
public class DzpkerRecordManager {
	@Inject
	private EntityMemcache<Long, DzpkerTableRecordEntity> tableRecordCache;
	@Inject
	private EntityMemcache<Long, DzpkerPlayerRecordEntity> playerRecordCache;
	@Autowired
	private Querier querier;
	//数据记录Id
	private AtomicLong atomicLong;
	
	@PostConstruct
	protected void init(){
		Long maxId = null;
		String querySql = "SELECT MAX(A.recordId) FROM DzpkerTableRecordEntity AS A";
		List<Object> retObjects = querier.listBySqlLimit(DzpkerTableRecordEntity.class, Object.class, querySql, 0, 1);
		for(Object obj : retObjects){
			if(obj instanceof Integer){
				Integer val = (Integer)obj;
				maxId = val.longValue();
			}else if(obj instanceof Long){
				maxId = (Long)obj;
			}
		}
		if(null == maxId){
			atomicLong = new AtomicLong(1); 
		}else{
			atomicLong = new AtomicLong(maxId+1);
		}
		
	}
	
	public DzpkerTableRecordEntity createTableRecord(int tableId, String tableName, long createPlayer, long createTime){
		Long id = atomicLong.incrementAndGet();
		return tableRecordCache.loadOrCreate(id, new EntityBuilder<Long, DzpkerTableRecordEntity>(){
			@Override
			public DzpkerTableRecordEntity createInstance(Long pk) {
				return DzpkerTableRecordEntity.valueOf(id, tableId, tableName, createPlayer, createTime);
			}
		});
	}
	
	public DzpkerTableRecordEntity loadTableRecord(long recordId){
		return tableRecordCache.load(recordId);
	}
	
	
	public DzpkerPlayerRecordEntity loadOrCreatePlayerRecord(long accountId){
		return playerRecordCache.loadOrCreate(accountId, new EntityBuilder<Long, DzpkerPlayerRecordEntity>(){
			@Override
			public DzpkerPlayerRecordEntity createInstance(Long pk) {
				return DzpkerPlayerRecordEntity.valueOf(accountId);
			}
		});
	}
	
	public StatisticsAttrib getStatisticsAttrib(long recordId, long accountId){
		DzpkerTableRecordEntity tableRecordEntity = this.loadTableRecord(recordId);
		if(null == tableRecordEntity){
			return null;
		}
		Map<Long, StatisticsAttrib> statisticsMap = tableRecordEntity.getStatisticsMap();
		StatisticsAttrib statisticsAttrib = statisticsMap.get(accountId);
		if(null == statisticsAttrib){
			statisticsAttrib = new StatisticsAttrib();
		}
		
		return statisticsAttrib;
	}
	
	public void setStatisticsAttrib(long recordId, long accountId, StatisticsAttrib statisticsAttrib){
		DzpkerTableRecordEntity tableRecordEntity = this.loadTableRecord(recordId);
		if(null == tableRecordEntity){
			return;
		}
		Map<Long, StatisticsAttrib> statisticsMap = tableRecordEntity.getStatisticsMap();
		statisticsMap.put(accountId, statisticsAttrib);
		tableRecordEntity.setStatisticsMap(statisticsMap);
	}
}
