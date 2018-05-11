package com.palmjoys.yf1b.act.dzpker.manager;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.dzpker.entity.DzpkerTableIdEntity;

@Component
public class TableIdManager {
	@Inject
	private EntityMemcache<Integer, DzpkerTableIdEntity> tableIdCache;
	
	public DzpkerTableIdEntity loadOrCreate(){
		int id = 1;
		return tableIdCache.loadOrCreate(id, new EntityBuilder<Integer, DzpkerTableIdEntity>(){
			@Override
			public DzpkerTableIdEntity createInstance(Integer pk) {
				DzpkerTableIdEntity retEntity = DzpkerTableIdEntity.valueOf(id);
				initEntity(retEntity);
				return retEntity;
			}
		});
	}
	
	private void initEntity(DzpkerTableIdEntity entity){
		List<Integer> idList = entity.getIdList();
		for(int i=100001; i<1000000; i++){
			idList.add(i);
		}
		Collections.shuffle(idList);
		Collections.shuffle(idList);
		Collections.shuffle(idList);		
		entity.setIdList(idList);
	}
	
	public int getTableId(){
		DzpkerTableIdEntity entity = this.loadOrCreate();
		List<Integer> idList = entity.getIdList();
		int idIndex = entity.getIdIndex();
		if(idIndex >= idList.size()){
			idIndex = 0;
		}
		
		Integer tableId = idList.get(idIndex);
		idIndex++;
		entity.setIdIndex(idIndex);
		return tableId;
	}
	
	
	
}
