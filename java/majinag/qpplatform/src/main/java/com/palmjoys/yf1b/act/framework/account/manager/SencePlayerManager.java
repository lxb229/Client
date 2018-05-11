package com.palmjoys.yf1b.act.framework.account.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;

@Component
public class SencePlayerManager {
	//场景玩家列表,KEY=场景Id,VALUE=在场景的玩家Id
	private Map<Integer, Map<String, Map<Long, Long>>> sencePlayerMap = new HashMap<>();
	// 数据操作锁
	private Lock _lock = new ReentrantLock();
	
	//场景Id定义
	//帮会场景
	public final static int SENCE_CORPS = 1;
	
	//子场景定义
	public final static String SENCE_SUB_NONE = "sence_sub_none"; 
	
	
	//加入场景
	public void joinSence(int senceId, String senceSubKey, long accountId){
		if(senceId <= 0 || null == senceSubKey || senceSubKey.isEmpty() || accountId<=0){
			return;
		}
		
		this._lock.lock();
		try{
			Map<String, Map<Long, Long>> senceData = sencePlayerMap.get(senceId);
			if(null == senceData){
				senceData = new HashMap<>();
			}
			Map<Long, Long> senceSubData = senceData.get(senceSubKey);
			if(null == senceSubData){
				senceSubData = new HashMap<>();
			}
			senceSubData.put(accountId, accountId);
			senceData.put(senceSubKey, senceSubData);
			sencePlayerMap.put(senceId, senceData);
		}finally{
			this._lock.unlock();
		}
	}
	
	//离开场景
	public void leaveSence(int senceId, String senceSubKey, long accountId){
		if(senceId <= 0 || null == senceSubKey || senceSubKey.isEmpty() || accountId<=0){
			return;
		}
		
		this._lock.lock();
		try{
			Map<String, Map<Long, Long>> senceData = sencePlayerMap.get(senceId);
			if(null != senceData){
				Map<Long, Long> senceSubData = senceData.get(senceSubKey);
				if(null != senceSubData){
					senceSubData.remove(accountId);
					senceData.put(senceSubKey, senceSubData);
					sencePlayerMap.put(senceId, senceData);
				}
			}
		}finally{
			this._lock.unlock();
		}
	}
	
	//删除子场景
	public void delSubSence(int senceId, String senceSubKey){
		if(senceId <= 0 || null == senceSubKey || senceSubKey.isEmpty()){
			return;
		}
		this._lock.lock();
		try{
			Map<String, Map<Long, Long>> senceData = sencePlayerMap.get(senceId);
			if(null != senceData){
				senceData.remove(senceSubKey);
				sencePlayerMap.put(senceId, senceData);
			}
		}finally{
			this._lock.unlock();
		}
	}
	
	//删除 主场景
	public void delMainSence(int senceId){
		if(senceId <= 0){
			return;
		}
		this._lock.lock();
		try{
			sencePlayerMap.remove(senceId);
		}finally{
			this._lock.unlock();
		}
	}
	
	//获取指定场景玩家列表
	public List<Long> getSencePlayers(int senceId, String senceSubKey){
		List<Long> retList = new ArrayList<>();
		
		this._lock.lock();
		try{
			Map<String, Map<Long, Long>> senceData = sencePlayerMap.get(senceId);
			if(null != senceData){
				Map<Long, Long> senecSubData = senceData.get(senceSubKey);
				if(null != senecSubData){
					retList.addAll(senecSubData.keySet());
				}
			}
		}finally{
			this._lock.unlock();
		}
		
		return retList;
	}
	
}
