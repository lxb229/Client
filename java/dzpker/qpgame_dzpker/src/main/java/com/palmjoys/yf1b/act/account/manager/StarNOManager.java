package com.palmjoys.yf1b.act.account.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.account.entity.StarNOEntity;

/**
 * 玩家展示id号生成规则
 * 6位数字	  
 * */
@Component
public class StarNOManager {
	@Inject
	private EntityMemcache<Integer, StarNOEntity> starNOCache;
	//id下标同步锁
	private Lock _lock = new ReentrantLock();
	private int MAX_STARNO_NUM = 1000000;
	
	private StarNOEntity loadOrCreate(){
		int id = 1;
		return starNOCache.loadOrCreate(id, new EntityBuilder<Integer, StarNOEntity>(){

			@Override
			public StarNOEntity createInstance(Integer pk) {
				StarNOEntity entity = StarNOEntity.valueOf(id);
				initEntity(entity);
				return entity;
			}
		});
	}
	
	private void initEntity(StarNOEntity entity){
		String[] digets = new String[]{"0","1","2","3","4","5","6","7","8","9"};
		List<List<String>> idList = entity.getIdList();
		
		for(int i=0; i<6; i++){
			List<String> tmpList = new ArrayList<String>();
			for(String diget : digets){
				tmpList.add(diget);
			}
			Collections.shuffle(tmpList);
			Collections.shuffle(tmpList);
			Collections.shuffle(tmpList);
			idList.add(tmpList);
		}
		entity.setIdList(idList);
	}
	
	public String createStarNO(){
		String retStarNO = null;
		_lock.lock();
		try{
			StarNOEntity entity = this.loadOrCreate();
			long idIndex = entity.getIdIndex();
			if(idIndex < MAX_STARNO_NUM){
				List<List<String>> idList = entity.getIdList();			
				while(true){
					retStarNO = "";
					int pow = idList.size()-1;
					long used = 0;
					long _idIndex = idIndex;
					for(List<String> strIds : idList){
						long powVal = (long) Math.pow(10, pow);
						int tmpIndex = (int) (_idIndex/powVal);
						
						String idStr = strIds.get(tmpIndex);
						retStarNO += idStr;
						
						used = (powVal*tmpIndex);
						_idIndex -= used;
						pow = pow - 1;
					}
					idIndex = idIndex + 1;
					//检查是否靓号
					break;
				}
				entity.setIdIndex(idIndex);
			}
			
		}finally{
			_lock.unlock();
		}
		
		return retStarNO;
	}
	
}
