package com.palmjoys.yf1b.act.replay.manager;

import org.treediagram.nina.memcache.service.Filter;

import com.palmjoys.yf1b.act.replay.entity.RecordEntity;

public class RecordFilterManager{
	private static RecordFilterManager _instance = new RecordFilterManager();
	public static RecordFilterManager Instance(){
		return _instance;
	}

	public RecordFilter_PlayerOrCorpsId createFilter(String value){
		RecordFilter_PlayerOrCorpsId obj = new RecordFilter_PlayerOrCorpsId(value);
		return obj;
	}
	
	public class RecordFilter_PlayerOrCorpsId implements Filter<RecordEntity>{
		private String value;
		
		public RecordFilter_PlayerOrCorpsId(String value){
			this.value = value;
		}
		
		@Override
		public boolean isExclude(RecordEntity entity) {
			if(this.value.equalsIgnoreCase(entity.getCorpsId())){
				return false;
			}			
			return true;
		}
	}

}
