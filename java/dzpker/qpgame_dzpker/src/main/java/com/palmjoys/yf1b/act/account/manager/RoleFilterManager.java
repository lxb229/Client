package com.palmjoys.yf1b.act.account.manager;

import org.treediagram.nina.memcache.service.Filter;

import com.palmjoys.yf1b.act.account.entity.RoleEntity;

public class RoleFilterManager {
	private static RoleFilterManager _instance = new RoleFilterManager();
	public static RoleFilterManager Instance(){
		return _instance;
	}
	
	public  RoleFilter_AccountId create_RoleFilter_AccountId(long accountId){
		return new RoleFilter_AccountId(accountId);
	}
	
	public class RoleFilter_AccountId implements Filter<RoleEntity>{
		private long accountId;
		public RoleFilter_AccountId(long accountId){
			this.accountId = accountId;
		}
		
		@Override
		public boolean isExclude(RoleEntity entity) {
			if(accountId == entity.getAccountId()){
				return false;
			}
			return true;
		}
	}

}
