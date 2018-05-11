package com.palmjoys.yf1b.act.event.manager;

import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;
import com.palmjoys.yf1b.act.event.entity.EventEntity;

@Component
public class EventEntityManager {
	@Inject
	private EntityMemcache<Long, EventEntity> eventEntityCache;
	@Autowired
	private Querier querier;
	private AtomicLong  atomicLong;
	
	@PostConstruct
	protected void init(){
		Long maxId = querier.unique(EventEntity.class, Long.class, EventEntity.NQ_EVENTENTITY_MAXUID);
		if(maxId == null){
			atomicLong = new AtomicLong(1);
		}else{
			atomicLong = new AtomicLong(maxId+1);
		}
	}
	
	public EventEntity loadOrCreate(){
		Long id = atomicLong.incrementAndGet();
		return eventEntityCache.loadOrCreate(id, new EntityBuilder<Long, EventEntity>(){
			@Override
			public EventEntity createInstance(Long pk) {
				return EventEntity.valueOf(pk);
			}
		});
	}
	
	public EventEntity load(Long id){
		return eventEntityCache.load(id);
	}
	
	public void remove(Long id){
		eventEntityCache.remove(id);
	}
}
