package com.palmjoys.yf1b.act.wallet.manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.framework.utils.MessagePushQueueUtils;
import com.palmjoys.yf1b.act.wallet.entity.WalletEntity;
import com.palmjoys.yf1b.act.wallet.model.WalletVo;

@Component
public class WalletManager {
	@Inject
	private EntityMemcache<Long, WalletEntity> walletEntityCache;
	@Autowired
	private SessionManager sessionManager;

	public WalletEntity loadOrCreate(long accountId) {
		return walletEntityCache.loadOrCreate(accountId, new EntityBuilder<Long, WalletEntity>() {
			@Override
			public WalletEntity createInstance(Long pk) {
				return WalletEntity.valueOf(accountId);
			}
		});
	}
	
	public void clearOfCache(long accountId){
		walletEntityCache.clear(accountId);
	}
	
	public int addRoomCard(long accountId, int num){
		int ret = -1;
		WalletEntity wallet = loadOrCreate(accountId);
		wallet.lock();
		try{
			int N = wallet.getRoomCard() + num;
			if(N >= 0){
				wallet.setRoomCard(N);
				ret = 0;
			}
		}finally{
			wallet.unLock();
		}
		if(ret >= 0){
			this.sendWalletNotify(accountId);
		}
		return ret;
	}
	
	public int addDiamond(long accountId, int num){
		int ret = -1;
		WalletEntity wallet = loadOrCreate(accountId);
		wallet.lock();
		try{
			int N = wallet.getDiamond() + num;
			if(N >= 0){
				wallet.setDiamond(N);
				ret = 0;
			}
		}finally{
			wallet.unLock();
		}
		if(ret >= 0){
			this.sendWalletNotify(accountId);
		}
		return ret;
	}
	
	public int addReplaceCard(long accountId, int num){
		int ret = -1;
		WalletEntity wallet = loadOrCreate(accountId);
		wallet.lock();
		try{
			int N = wallet.getReplaceCard() + num;
			if(N >= 0){
				wallet.setReplaceCard(N);
				ret = 0;
			}
		}finally{
			wallet.unLock();
		}
		if(ret >= 0){
			this.sendWalletNotify(accountId);
		}
		return ret;
	}
	
	public int addGoldMoney(long accountId, int num){
		int ret = -1;
		WalletEntity wallet = loadOrCreate(accountId);
		wallet.lock();
		try{
			int N = wallet.getGoldMoney() + num;
			if(N >= 0){
				wallet.setGoldMoney(N);
				ret = 0;
			}
		}finally{
			wallet.unLock();
		}
		if(ret >= 0){
			this.sendWalletNotify(accountId);
		}
		return ret;
	}
	
	public int addSilverMoney(long accountId, int num){
		int ret = -1;
		WalletEntity wallet = loadOrCreate(accountId);
		wallet.lock();
		try{
			int N = wallet.getSilverMoney() + num;
			if(N >= 0){
				wallet.setSilverMoney(N);
				ret = 0;
			}
		}finally{
			wallet.unLock();
		}
		if(ret >= 0){
			this.sendWalletNotify(accountId);
		}
		return ret;
	}
		
	public int getRoomCard(long accountId){
		WalletEntity wallet = loadOrCreate(accountId);
		return wallet.getRoomCard();
	}
	
	public int getDiamond(long accountId){
		WalletEntity wallet = loadOrCreate(accountId);
		return wallet.getDiamond();
	}
	
	public int getReplaceCard(long accountId){
		WalletEntity wallet = loadOrCreate(accountId);
		return wallet.getReplaceCard();
	}
	
	public int getGoldMoney(long accountId){
		WalletEntity wallet = loadOrCreate(accountId);
		return wallet.getGoldMoney();
	}
	
	public int getSilverMoney(long accountId){
		WalletEntity wallet = loadOrCreate(accountId);
		return wallet.getSilverMoney();
	}
	
	
	public WalletVo getWallet(long accountId){
		WalletVo walletVo = new WalletVo();
		WalletEntity walletEntity = this.loadOrCreate(accountId);
		walletVo.roomCard = walletEntity.getRoomCard();
		walletVo.replaceCard = walletEntity.getReplaceCard();
		walletVo.diamond = walletEntity.getDiamond();
		walletVo.goldMoney = walletEntity.getGoldMoney();
		walletVo.silverMoney = walletEntity.getSilverMoney();
		
		return walletVo;
	}
	
	public void sendWalletNotify(long accountId){
		WalletVo walletVo = this.getWallet(accountId); 
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(AccountDefine.ACCOUNT_WALLET_NOTIFY, 
				Result.valueOfSuccess(walletVo));
		MessagePushQueueUtils.getPushQueue(sessionManager).push2(accountId, pushMsg);
	}
}
