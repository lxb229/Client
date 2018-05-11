package com.palmjoys.yf1b.act.framework.account.manager;

import org.treediagram.nina.memcache.service.Filter;

import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.entity.AuthenticationEntity;

public class AccountFilterManager{
	private static AccountFilterManager _instance = new AccountFilterManager();
	public static AccountFilterManager Instance(){
		return _instance;
	}
	
	public AccountFilter createAccountFilter(String uuid, String starNO, int accountType){
		AccountFilter filter = new AccountFilter(uuid, starNO, accountType);
		return filter;
	}
	
	public AccountFilter_Phone createAccountFilter_Phone(String phone){
		AccountFilter_Phone filter = new AccountFilter_Phone(phone);
		return filter;
	}
	
	//帐号过滤器
	public class AccountFilter implements Filter<AccountEntity>{
		private String uuid;
		private String starNO;
		private int accountType;
		
		public AccountFilter(String uuid, String starNO, int accountType){
			this.uuid = uuid;
			this.starNO = starNO;
			this.accountType = accountType;
		}
		
		@Override
		public boolean isExclude(AccountEntity entity) {
			if(uuid.isEmpty() == false){
				if(uuid.equalsIgnoreCase(entity.getUuid()) == false){
					return true;
				}
			}
			if(starNO.isEmpty() == false){
				if(starNO.equalsIgnoreCase(entity.getStarNO()) == false){
					return true;
				}
			}
			if(this.accountType != entity.getAccountType()){
				return true;
			}
			
			return false;
		}
	}
	
	public class AccountFilter_Phone implements Filter<AuthenticationEntity>{
		public String phone;
		public AccountFilter_Phone(String phone){
			this.phone = phone;
		}

		@Override
		public boolean isExclude(AuthenticationEntity entity) {
			if(this.phone.equalsIgnoreCase(entity.getPhone()) == false){
				return true;
			}
			return false;
		}
		
	}
	
}
