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
	
	public int addRoomCard(long accountId, long num, boolean bNotify){
		int ret = -1;
		WalletEntity wallet = loadOrCreate(accountId);
		wallet.lock();
		try{
			long cardNum = wallet.getRoomCard() + num;
			if(cardNum >= 0){
				wallet.setRoomCard(cardNum);
				ret = 0;
			}
		}finally{
			wallet.unLock();
		}
		if(ret >= 0 && bNotify){
			this.sendWalletNotify(accountId);
		}
		return ret;
	}
	
	public int addDiamond(long accountId, long num, boolean bNotify){
		int ret = -1;
		WalletEntity wallet = loadOrCreate(accountId);
		wallet.lock();
		try{
			long diamond = wallet.getDiamond() + num;
			if(diamond >= 0){
				wallet.setDiamond(diamond);
				ret = 0;
			}
		}finally{
			wallet.unLock();
		}
		if(ret >= 0 && bNotify){
			this.sendWalletNotify(accountId);
		}
		return ret;
	}
	
	public int checkEnough(long accountId, int type, long num){
		if(num < 0)
			return -1;
		
		int ret = -1;
		WalletEntity wallet = loadOrCreate(accountId);
		wallet.lock();
		try{
			long chkNum = -10000;
			if(type == 1){
				chkNum = wallet.getRoomCard();
				
			}else if(type == 2){
				chkNum = wallet.getDiamond();
			}
			if(chkNum >= num){
				ret = 0;
			}
		}finally{
			wallet.unLock();
		}
		return ret;
	}
	
	public long getRoomCard(long accountId){
		WalletEntity wallet = loadOrCreate(accountId);
		return wallet.getRoomCard();
	}
	
	public long getDiamond(long accountId){
		WalletEntity wallet = loadOrCreate(accountId);
		return wallet.getDiamond();
	}
	
	public void sendWalletNotify(long accountId){
		WalletVo walletVo = new WalletVo();
		WalletEntity walletEntity = this.loadOrCreate(accountId);
		walletVo.roomCard = String.valueOf(walletEntity.getRoomCard());
		walletVo.diamond = String.valueOf(walletEntity.getDiamond());
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(AccountDefine.ACCOUNT_WALLET_NOTIFY, 
				Result.valueOfSuccess(walletVo).toMap());
		MessagePushQueueUtils.getPushQueue(sessionManager).push2(accountId, pushMsg);
	}
}
