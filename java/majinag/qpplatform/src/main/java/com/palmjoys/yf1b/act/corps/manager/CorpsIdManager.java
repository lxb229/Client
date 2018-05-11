package com.palmjoys.yf1b.act.corps.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.corps.entity.CorpsIdEntity;

@Component
public class CorpsIdManager {
	@Inject
	private EntityMemcache<Integer, CorpsIdEntity> corpsIdCache;
	
	public CorpsIdEntity loadOrCreate(){
		int id = 1;
		return corpsIdCache.loadOrCreate(id, new EntityBuilder<Integer, CorpsIdEntity>(){
			@Override
			public CorpsIdEntity createInstance(Integer pk) {
				CorpsIdEntity entity = CorpsIdEntity.valueOf(id);
				initEntity(entity);
				return entity;
			}
		});
		
	}
	
	private void initEntity(CorpsIdEntity entity){
		List<List<String>> idList = entity.getIdList();
		idList.clear();
		//6位帮会Id
		for(int index=0; index<6; index++){
			List<String> tmpList = new ArrayList<>();
			for(int i=0; i<10; i++){
				tmpList.add(String.valueOf(i));
			}
			Collections.shuffle(tmpList);
			Collections.shuffle(tmpList);
			Collections.shuffle(tmpList);
			
			idList.add(tmpList);
		}
		entity.setIdList(idList);
	}
	
	public String createCorpsId(){
		String retId = null;
		
		CorpsIdEntity corpsIdEntity = this.loadOrCreate();
		long idIndex = corpsIdEntity.getIdIndex();
		List<List<String>> idList = corpsIdEntity.getIdList();
		long MAX_CORPS_ID = (long) Math.pow(10, idList.size());
		if(idIndex < MAX_CORPS_ID){
			while(true){
				retId = "";
				int pow = idList.size()-1;
				long used = 0;
				long _idIndex = idIndex;
				
				int nNum = 0;
				List<String> tmpStrList = new ArrayList<>();
				for(List<String> strIds : idList){
					long powVal = (long) Math.pow(10, pow);
					int tmpIndex = (int) (_idIndex/powVal);
					
					String idStr = strIds.get(tmpIndex);
					retId += idStr;
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
			corpsIdEntity.setIdIndex(idIndex);
		}
		if(null == retId || retId.isEmpty()){
			return null;
		}
		
		return retId;
	}
	
	//扩展帮会Id
	public void extendCorpsIdNum(int exNum){
		if(exNum <= 0){
			return;
		}
		CorpsIdEntity entity = this.loadOrCreate();
		List<List<String>> idList = entity.getIdList();
		List<List<String>> copyIdList = new ArrayList<>();
		
		String[] digets = new String[]{"0","1","2","3","4","5","6","7","8","9"};
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
	}
	
}
