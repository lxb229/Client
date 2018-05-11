package com.palmjoys.yf1b.act.reward.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.palmjoys.yf1b.act.framework.common.manager.CommonCfgManager;
import com.palmjoys.yf1b.act.framework.common.resource.GameObjectConfig;
import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;
import com.palmjoys.yf1b.act.framework.gameobject.model.ServiceType;
import com.palmjoys.yf1b.act.framework.gameobject.service.GameObjectService;
import com.palmjoys.yf1b.act.wallet.entity.WalletEntity;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

@Service
public class RewardGiveImp_Wallet implements GameObjectService{
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private CommonCfgManager commCfgManager;

	@Override
	public ServiceType serviceType() {
		return ServiceType.WALLET;
	}

	@Override
	public int checkEnough(Long traget, GameObject gameObject) {
		WalletEntity walletEntity = walletManager.loadOrCreate(traget);
		GameObjectConfig objCfg = commCfgManager.getGameObject(gameObject.code);
		if(null == objCfg){
			return 0;
		}
		if(gameObject.type.equals(ServiceType.WALLET.getValue()) == false){
			return 0;
		}
		switch(gameObject.code){
		case 10001://充值房卡
			if(walletEntity.getRoomCard() >= gameObject.amount.intValue()){
				return 1;
			}
			break;
		case 10002://钻石
			if(walletEntity.getDiamond() >= gameObject.amount.intValue()){
				return 1;
			}
			break;
		case 10003://金币
			if(walletEntity.getGoldMoney() >= gameObject.amount.intValue()){
				return 1;
			}
			break;
		case 10004://银币
			if(walletEntity.getSilverMoney() >= gameObject.amount.intValue()){
				return 1;
			}
			break;
		case 10006://体验房卡
			if(walletEntity.getReplaceCard() >= gameObject.amount.intValue()){
				return 1;
			}
			break;
		}
		return 0;
	}

	@Override
	public int increase(Long traget, GameObject gameObject) {
		GameObjectConfig objCfg = commCfgManager.getGameObject(gameObject.code);
		if(null == objCfg){
			return 0;
		}
		if(gameObject.type.equals(ServiceType.WALLET.getValue()) == false){
			return 0;
		}
		switch(gameObject.code){
		case 10001://充值房卡
			walletManager.addRoomCard(traget, gameObject.amount);
			break;
		case 10002://钻石
			walletManager.addRoomCard(traget, gameObject.amount);
			break;
		case 10003://金币
			walletManager.addGoldMoney(traget, gameObject.amount);
			break;
		case 10004://银币
			walletManager.addSilverMoney(traget, gameObject.amount);
			break;
		case 10006://体验房卡
			walletManager.addReplaceCard(traget, gameObject.amount);
			break;
		}
		return 1;
	}

	@Override
	public int decrease(Long traget, GameObject gameObject) {
		GameObjectConfig objCfg = commCfgManager.getGameObject(gameObject.code);
		if(null == objCfg){
			return 0;
		}
		if(gameObject.type.equals(ServiceType.WALLET.getValue()) == false){
			return 0;
		}
		int number = 0-gameObject.amount.intValue();
		int addRet = 0;
		switch(gameObject.code){
		case 10001://充值房卡
			addRet = walletManager.addRoomCard(traget, number);
			if(addRet >= 0){
				return 1;
			}
			break;
		case 10002://钻石
			addRet = walletManager.addRoomCard(traget, number);
			if(addRet >= 0){
				return 1;
			}
			break;
		case 10003://金币
			addRet = walletManager.addGoldMoney(traget, number);
			if(addRet >= 0){
				return 1;
			}
			break;
		case 10004://银币
			addRet = walletManager.addSilverMoney(traget, number);
			if(addRet >= 0){
				return 1;
			}
			break;
		case 10006://体验房卡
			addRet = walletManager.addReplaceCard(traget, number);
			if(addRet >= 0){
				return 1;
			}
			break;
		}
		return 0;
	}

}
