package com.palmjoys.yf1b.act.dzpker.manager;

import org.treediagram.nina.memcache.service.Filter;

import com.palmjoys.yf1b.act.dzpker.entity.DzpkerOrderEntity;

public class DzpkerOrderFilterManager {
	private static DzpkerOrderFilterManager _instance = new DzpkerOrderFilterManager();
	
	public static DzpkerOrderFilterManager Instance(){
		return _instance;
	}

	public DzpkerOrderFilter_State createFilter_OrderFilter_state(long accountId, long tableRecordId, int state){
		return new DzpkerOrderFilter_State(accountId, tableRecordId, state);
	}
	
	public DzpkerOrderFilter_tableCreatePlayer createFilter_tableCreatePlayer(long tableCreatePlayer){
		return new DzpkerOrderFilter_tableCreatePlayer(tableCreatePlayer);
	}
	
	
	public class DzpkerOrderFilter_State implements Filter<DzpkerOrderEntity>{
		private long accountId;
		private long tableRecordId;
		private int state;
		
		public DzpkerOrderFilter_State(long accountId, long tableRecordId, int state){
			this.accountId = accountId;
			this.tableRecordId = tableRecordId;
			this.state = state;
		}

		@Override
		public boolean isExclude(DzpkerOrderEntity entity) {
			if(this.accountId != entity.getAccountId()){
				return true;
			}
			if(this.tableRecordId != entity.getTableRecordId()){
				return true;
			}
			if(this.state != entity.getState()){
				return true;
			}
			
			return false;
		}
		
	}
	
	public class DzpkerOrderFilter_tableCreatePlayer implements Filter<DzpkerOrderEntity>{
		private long tableCreatePlayer;
		
		public DzpkerOrderFilter_tableCreatePlayer(long tableCreatePlayer){
			this.tableCreatePlayer = tableCreatePlayer;
		}

		@Override
		public boolean isExclude(DzpkerOrderEntity entity) {
			if(this.tableCreatePlayer != entity.getTableCreatePlayer()){
				return true;
			}
			if(entity.getState() != 0){
				return true;
			}
			
			return false;
		}
		
	}
	
	
}
