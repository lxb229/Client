package com.palmjoys.yf1b.act.dzpker.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;

import com.palmjoys.yf1b.act.dzpker.entity.DzpkerOrderEntity;
import com.palmjoys.yf1b.act.dzpker.entity.DzpkerTableRecordEntity;
import com.palmjoys.yf1b.act.dzpker.model.TableAttrib;
import com.palmjoys.yf1b.act.account.entity.RoleEntity;
import com.palmjoys.yf1b.act.account.manager.RoleEntityManager;

@Component
public class DzpkerOrderManager {
	@Inject
	private EntityMemcache<Long, DzpkerOrderEntity> orderCache;
	@Autowired
	private Querier querier;
	@Autowired
	private GameLogicManager logicManager;
	@Autowired
	private DzpkerRecordManager recordManager;
	@Autowired
	private RoleEntityManager roleEntityManager;
	
	//数据记录Id
	private AtomicLong atomicLong;
	
	@PostConstruct
	protected void init(){
		Long maxId = null;
		String querySql = "SELECT MAX(A.recordId) FROM DzpkerOrderEntity AS A";
		List<Object> retObjects = querier.listBySqlLimit(DzpkerOrderEntity.class, Object.class, querySql, 0, 1);
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
	
	public DzpkerOrderEntity loadOrCreate(long accountId, long tableRecordId, long tableCreatePlayer, long chipNum){
		Long recordId = atomicLong.incrementAndGet();
		return orderCache.loadOrCreate(recordId, new EntityBuilder<Long, DzpkerOrderEntity>(){
			@Override
			public DzpkerOrderEntity createInstance(Long pk) {
				return DzpkerOrderEntity.valueOf(recordId, accountId, tableRecordId, tableCreatePlayer, chipNum);
			}
		});
	}
	
	public DzpkerOrderEntity load(long recordId){
		return orderCache.load(recordId);
	}
	
	public void remove(long id){
		orderCache.remove(id);
	}
	
	//查找玩家是否有未处理的购买记录
	public boolean isUnTransBuyRecord(long accountId, long tableRecordId){
		//先查库后查缓存
		String querySql = "SELECT A.recordId FROM DzpkerOrderEntity AS A"
				+ " WHERE A.state=0 AND A.accountId=" + accountId + " AND A.tableRecordId=" + tableRecordId;
		List<Object> retObjects = querier.listBySqlLimit(DzpkerOrderEntity.class, Object.class, querySql, 0, 1);
		if(retObjects.isEmpty() == false){
			return true;
		}
		List<DzpkerOrderEntity> retEntitys = orderCache.getFinder().find(
				DzpkerOrderFilterManager.Instance().createFilter_OrderFilter_state(accountId, tableRecordId, 0));
		if(retEntitys.isEmpty() == false){
			return true;
		}
		
		return false;
	}
	
	//房主获取所有请求购买列表
	public List<Long> queryQuestBuyOrderList(long tableCreatePlayer){
		List<Long> orderIds = new ArrayList<>();
		//先查库后查缓存
		String querySql = "SELECT A.recordId FROM DzpkerOrderEntity AS A"
				+ " WHERE A.state=0 AND A.tableCreatePlayer=" + tableCreatePlayer;
		List<Object> retObjects = querier.listBySqlLimit(DzpkerOrderEntity.class, Object.class, querySql, 0, 100000);
		for(Object obj : retObjects){
			Long id = (Long) obj;
			orderIds.add(id);
		}
		List<DzpkerOrderEntity> retEntitys = orderCache.getFinder().find(
				DzpkerOrderFilterManager.Instance().createFilter_tableCreatePlayer(tableCreatePlayer));
		for(DzpkerOrderEntity entity : retEntitys){
			if(orderIds.contains(entity.getId()) == false){
				orderIds.add(entity.getId());
			}
		}		
		return orderIds;
	}
	
	public long checkHotPrompt(long accountId){
		long retVal = 0;
		List<Long> delIds = new ArrayList<>();
		List<Long> orderIds = this.queryQuestBuyOrderList(accountId);
		for(Long orderId : orderIds){
			DzpkerOrderEntity orderEntity = this.load(orderId);
			if(null == orderEntity){
				delIds.add(orderId);
				continue;
			}
			if(orderEntity.getState() != 0){
				continue;
			}
			DzpkerTableRecordEntity recordEntity = recordManager.loadTableRecord(orderEntity.getTableRecordId());
			if(null == recordEntity){
				delIds.add(orderId);
				continue;
			}
			TableAttrib table = logicManager.getTable(recordEntity.getTableId());
			if(null == table || table.recordId != orderEntity.getTableRecordId()){
				delIds.add(orderId);
				continue;
			}
			RoleEntity roleEntity = roleEntityManager.findOf_accountId(orderEntity.getAccountId());
			if(null == roleEntity){
				delIds.add(orderId);
				continue;
			}
			retVal++;
		}
		
		for(Long orderId : delIds){
			this.remove(orderId);
		}
		 
		return retVal;
	}
	
	
	
	
}
