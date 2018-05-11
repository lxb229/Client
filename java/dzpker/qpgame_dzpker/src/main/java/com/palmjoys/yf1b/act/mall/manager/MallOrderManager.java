package com.palmjoys.yf1b.act.mall.manager;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.mall.entity.MallBuyOrderEntity;

@Component
public class MallOrderManager {
	@Inject
	private EntityMemcache<Long, MallBuyOrderEntity> mallBuyOrderCache;
	@Autowired
	private Querier querier;
	
	//订单状态定义
	//等待处理状态
	public static final int ORDER_STATE_WAIT = 0;
	//正在处理状态
	public static final int ORDER_STATE_TRANSING = 1;
	//处理成功
	public static final int ORDER_STATE_SUCESS = 2;
	//处理失败
	public static final int ORDER_STATE_FAIL = 3;
	
	
	//表Id
	private AtomicLong atomicLong;
	
	@PostConstruct
	protected void init() {
		Long maxId = null;
		String querySql = "SELECT MAX(A.orderId) FROM MallBuyOrderEntity AS A";
		List<Object> retObjects = querier.listBySqlLimit(MallBuyOrderEntity.class, Object.class, querySql, 0, 1);
		for(Object obj : retObjects){
			if(obj instanceof Integer){
				Integer tmpId = (Integer) obj;
				maxId = tmpId.longValue();
			}else if(obj instanceof Long){
				Long tmpId = (Long) obj;
				maxId = tmpId;
			}
			break;
		}
		if(null == maxId){
			atomicLong = new AtomicLong(100000);
		}else{
			atomicLong = new AtomicLong(maxId.longValue()+1);
		}
	}
	
	public MallBuyOrderEntity createBuyOrder(long accountId, String starNO, int rmb, long roomCard, 
			long goldMoney, long diamond){
		
		long orderId = atomicLong.incrementAndGet();
		return mallBuyOrderCache.loadOrCreate(orderId, new EntityBuilder<Long, MallBuyOrderEntity>(){
			@Override
			public MallBuyOrderEntity createInstance(Long pk) {
				return MallBuyOrderEntity.valueOf(orderId, accountId, starNO, rmb, roomCard, goldMoney, diamond);
			}
		});
	}
	
	public MallBuyOrderEntity loadBuyorder(long orderId){
		return mallBuyOrderCache.load(orderId);
	}
	
	

}
