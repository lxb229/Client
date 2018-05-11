package com.palmjoys.yf1b.act.replay.manager;

import org.treediagram.nina.memcache.service.Filter;

import com.palmjoys.yf1b.act.replay.entity.ReplayTableEntity;

public class ReplayFilterManager {
	private static ReplayFilterManager _instance = new ReplayFilterManager();
	public static ReplayFilterManager Instance(){
		return _instance;
	}
	
	public ReplayFilter_CorpsId createFilter_ReplayFilter_CorpsId(String corpsId){
		return new ReplayFilter_CorpsId(corpsId);
	}
	
	public class ReplayFilter_CorpsId implements Filter<ReplayTableEntity>{
		private String corpsId;
		public ReplayFilter_CorpsId(String corpsId){
			this.corpsId = corpsId;
		}

		@Override
		public boolean isExclude(ReplayTableEntity entity) {
			if(this.corpsId.equalsIgnoreCase(entity.getCorpsId())){
				return false;
			}
			return true;
		}
	}
	

}
