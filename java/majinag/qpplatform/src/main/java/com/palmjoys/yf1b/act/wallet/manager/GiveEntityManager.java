package com.palmjoys.yf1b.act.wallet.manager;

import java.util.List;
import org.springframework.stereotype.Component;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.wallet.entity.GiveEntity;
import com.palmjoys.yf1b.act.wallet.model.RoomCardGiveAttrib;

@Component
public class GiveEntityManager {
	@Inject
	private EntityMemcache<Long, GiveEntity> giveCache;
	
	public GiveEntity loadOrCreate(long accountId){
		return giveCache.loadOrCreate(accountId, new EntityBuilder<Long, GiveEntity>(){
			@Override
			public GiveEntity createInstance(Long pk) {
				return GiveEntity.valueOf(accountId);
			}
		});
	}
	
	public void addGiveRecord(long src, long dst, int giveNum){
		GiveEntity giveEntity = this.loadOrCreate(src); 
		List<RoomCardGiveAttrib> giveList = giveEntity.getGiveList();
		if(giveList.size() >= 50){
			giveList.remove(0);
		}
		RoomCardGiveAttrib item = new RoomCardGiveAttrib();
		item.src = src;
		item.dst = dst;
		item.giveTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		item.giveNum = giveNum;
		giveList.add(item);
		
		giveEntity.setGiveList(giveList);
	}
}
