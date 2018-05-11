package com.palmjoys.yf1b.act.account.manager;

import org.treediagram.nina.memcache.service.Filter;

import com.palmjoys.yf1b.act.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.account.entity.AuthenticationEntity;

public class AccountFilterManager{
	private static AccountFilterManager _instance = new AccountFilterManager();
	public static AccountFilterManager Instance(){
		return _instance;
	}
	
	public AccountFilter_UUID createFilter_AccountFilter_UUID(String uuid){
		AccountFilter_UUID filter = new AccountFilter_UUID(uuid);
		return filter;
	}
	
	public AuthenticationFilter_Phone createFilter_AuthenticationFilter_Phone(String phone){
		return new AuthenticationFilter_Phone(phone);
	}
	
	//帐号Id过滤器
	public class AccountFilter_UUID implements Filter<AccountEntity>{
		private String uuid;
		
		public AccountFilter_UUID(String uuid){
			this.uuid = uuid;
		}
		
		@Override
		public boolean isExclude(AccountEntity entity) {
			if(uuid.isEmpty() == false){
				if(uuid.equalsIgnoreCase(entity.getUuid()) == false){
					return true;
				}
			}			
			return false;
		}
	}	
	
	//绑定电话过滤器
	public class AuthenticationFilter_Phone implements Filter<AuthenticationEntity>{
		private String phone;
		public AuthenticationFilter_Phone(String phone){
			this.phone = phone;
		}

		@Override
		public boolean isExclude(AuthenticationEntity entity) {
			if(this.phone.equalsIgnoreCase(entity.getPhone())){
				return false;
			}
			return true;
		}
	}
}
