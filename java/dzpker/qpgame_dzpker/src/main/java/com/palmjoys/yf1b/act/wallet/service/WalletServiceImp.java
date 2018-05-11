package com.palmjoys.yf1b.act.wallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.network.model.Result;
import com.palmjoys.yf1b.act.account.entity.RoleEntity;
import com.palmjoys.yf1b.act.account.manager.RoleEntityManager;
import com.palmjoys.yf1b.act.account.model.AccountDefine;
import com.palmjoys.yf1b.act.framework.common.manager.CommonCfgManager;
import com.palmjoys.yf1b.act.framework.common.manager.ErrorCodeManager;
import com.palmjoys.yf1b.act.framework.common.resource.GameObjectConfig;
import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;
import com.palmjoys.yf1b.act.framework.gameobject.model.ServiceType;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.mail.entity.MailEntity;
import com.palmjoys.yf1b.act.mail.manager.MailManager;
import com.palmjoys.yf1b.act.mail.model.MailAttrib;
import com.palmjoys.yf1b.act.wallet.entity.GiveEntity;
import com.palmjoys.yf1b.act.wallet.entity.WalletEntity;
import com.palmjoys.yf1b.act.wallet.manager.GiveEntityManager;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;
import com.palmjoys.yf1b.act.wallet.model.RoomCardGiveAttrib;
import com.palmjoys.yf1b.act.wallet.model.WalletDefine;
import com.palmjoys.yf1b.act.wallet.model.WalletGiveVo;

@Service
public class WalletServiceImp implements WalletService{
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private CommonCfgManager commCfgManager;
	@Autowired
	private GiveEntityManager giveEntityManager;
	@Autowired
	private ErrorCodeManager errCodeManager;
	@Autowired
	private HotPromptManager hotPromptManager;
	@Autowired
	private MailManager mailManager;
	@Autowired
	private RoleEntityManager roleEntityManager;
	
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
		case 10001://房卡
			if(walletEntity.getRoomCard() >= gameObject.amount.intValue()){
				return 1;
			}
			break;
		case 10002://金币
			if(walletEntity.getGoldMoney() >= gameObject.amount.intValue()){
				return 1;
			}
			break;
		case 10003://钻石
			if(walletEntity.getDiamond() >= gameObject.amount.intValue()){
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
		case 10001://房卡
			walletManager.addRoomCard(traget, gameObject.amount);
			break;
		case 10002://金币
			walletManager.addGoldMoney(traget, gameObject.amount);
			break;	
		case 10003://钻石
			walletManager.addRoomCard(traget, gameObject.amount);
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
		case 10001://房卡
			addRet = walletManager.addRoomCard(traget, number);
			if(addRet >= 0){
				return 1;
			}
			break;
		case 10002://金币
			addRet = walletManager.addGoldMoney(traget, number);
			if(addRet >= 0){
				return 1;
			}
			break;
		case 10003://钻石
			addRet = walletManager.addRoomCard(traget, number);
			if(addRet >= 0){
				return 1;
			}
			break;
		}
		return 0;
	}

	@Override
	public Object wallet_roomcard_give(Long accountId, String givePlayer, int giveNum) {
		if(giveNum <= 0){
			return Result.valueOfError(WalletDefine.WALLET_ERROR_CMDPARAM,
					errCodeManager.Error2Desc(WalletDefine.WALLET_ERROR_CMDPARAM), null);
		}
		RoleEntity orgAccountEntity = roleEntityManager.findOf_accountId(accountId);
		if(null == orgAccountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_ABNORMAL,
					errCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_ABNORMAL), null);
		}
		
		RoleEntity roleEntity = roleEntityManager.findOf_starNO(givePlayer);
		if(null == roleEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_ABNORMAL,
					errCodeManager.Error2Desc(AccountDefine.ACCOUNT_ERROR_ABNORMAL), null);
		}
		long giveAccount = roleEntity.getAccountId();
		if(orgAccountEntity.getAccountId() == giveAccount){
			return Result.valueOfError(WalletDefine.WALLET_ERROR_GIVEROOMCARD,
					errCodeManager.Error2Desc(WalletDefine.WALLET_ERROR_GIVEROOMCARD), null);
		}
		GameObject chkObject = new GameObject();
		chkObject.type = "WALLET";
		chkObject.code = 10001;
		chkObject.amount = giveNum;
		int ret = this.checkEnough(accountId, chkObject);
		if(ret < 0){
			return Result.valueOfError(WalletDefine.WALLET_ERROR_ROOMCARD,
					errCodeManager.Error2Desc(WalletDefine.WALLET_ERROR_ROOMCARD), null);
		}
		chkObject = null;
		walletManager.addRoomCard(giveAccount, giveNum);
		walletManager.addRoomCard(accountId, (0-giveNum));
		
		giveEntityManager.addGiveRecord(accountId, giveAccount, giveNum);
		
		MailEntity mailEntity = mailManager.loadOrCreate(giveAccount);
		mailEntity.lock();
		try{
			String content = "玩家["+orgAccountEntity.getNick()+"]赠送了您" + giveNum + "张房卡";
			MailAttrib mailAttrib = new MailAttrib();
			mailAttrib.content = content;
			mailAttrib.recvTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
			mailAttrib.sender = 0;
			mailAttrib.title = "房卡赠送";
			mailEntity.addNewMail(mailAttrib);
		}finally{
			mailEntity.unLock();
		}
		//检查推送红点消息
		hotPromptManager.updateHotPrompt(giveAccount, HotPromptManager.HOT_KEY_MAIL);
		hotPromptManager.hotPromptNotity(giveAccount);
		return Result.valueOfSuccess();
	}

	@Override
	public Object wallet_roomcard_record(Long accountId) {
		WalletGiveVo retVo = new WalletGiveVo();
		
		GiveEntity giveEntity = giveEntityManager.loadOrCreate(accountId);
		List<RoomCardGiveAttrib> giveList = giveEntity.getGiveList();
		for(RoomCardGiveAttrib item : giveList){
			RoleEntity roleEntity = roleEntityManager.findOf_accountId(item.dst);
			if(null != roleEntity){
				retVo.addItem(item.giveTime, roleEntity.getNick(), roleEntity.getStarNO(), item.giveNum);
			}
		}
		return Result.valueOfSuccess(retVo);
	}
	
}
