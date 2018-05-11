package com.palmjoys.yf1b.act.framework.account.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.framework.account.entity.StarNOEntity;

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
			List<List<String>> idList = entity.getIdList();
			long MAX_PLAYER_NUM = (long) Math.pow(10, idList.size());
			long idIndex = entity.getIdIndex();			
			if(idIndex < MAX_PLAYER_NUM){
				while(true){
					retStarNO = "";
					int pow = idList.size()-1;
					long used = 0;
					long _idIndex = idIndex;
					
					int nNum = 0;
					List<String> tmpStrList = new ArrayList<>();
					for(List<String> strIds : idList){
						long powVal = (long) Math.pow(10, pow);
						int tmpIndex = (int) (_idIndex/powVal);
						
						String idStr = strIds.get(tmpIndex);
						retStarNO += idStr;
						boolean bFind = false;
						for(String tmpStr : tmpStrList){
							if(tmpStr.equalsIgnoreCase(idStr)){
								bFind = true;
								break;
							}
						}
						if(bFind == false){
							nNum++;
						}
						
						tmpStrList.add(idStr);
						
						used = (powVal*tmpIndex);
						_idIndex -= used;
						pow = pow - 1;
					}
					idIndex = idIndex + 1;
					//检查是否符合规则,不能有连续三个及以上号码相同,至少三个或以上不相同的号码组成
					if(nNum < 3){
						tmpStrList = null;
						continue;
					}
					boolean bSame = false;
					int nSize = tmpStrList.size();
					for(int tmpIndex=0; tmpIndex<nSize-2; tmpIndex++){
						String tmpNO1 = tmpStrList.get(tmpIndex);
						String tmpNO2 = tmpStrList.get(tmpIndex+1);
						String tmpNO3 = tmpStrList.get(tmpIndex+2);
						if(tmpNO1.equalsIgnoreCase(tmpNO2) && tmpNO1.equalsIgnoreCase(tmpNO3)){
							bSame = true;
							break;
						}
					}
					if(bSame){
						continue;
					}
					
					break;
				}
				entity.setIdIndex(idIndex);
			}
		}finally{
			_lock.unlock();
		}
		
		if(null == retStarNO || retStarNO.isEmpty()){
			return null;
		}
		return retStarNO;
	}
	
	/**
	 * 扩展明星号位数
	 * exNum 扩展位数
	 * */
	public void extendStarNONum(int exNum){
		if(exNum <= 0){
			return;
		}
		
		_lock.lock();
		try{
			String[] digets = new String[]{"0","1","2","3","4","5","6","7","8","9"};
			StarNOEntity entity = this.loadOrCreate();
			List<List<String>> idList = entity.getIdList();
			List<List<String>> copyIdList = new ArrayList<>();
			
			for(int i=0; i<exNum; i++){
				List<String> tmpList = new ArrayList<String>();
				for(String diget : digets){
					tmpList.add(diget);
				}
				Collections.shuffle(tmpList);
				Collections.shuffle(tmpList);
				Collections.shuffle(tmpList);
				
				copyIdList.add(tmpList);
			}
			copyIdList.addAll(idList);
			idList.clear();
			idList.addAll(copyIdList);
			copyIdList = null;
			entity.setIdList(idList);
		}finally{
			_lock.unlock();	
		}
	}
	
}
