package com.palmjoys.yf1b.act.wallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.network.model.Result;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.framework.gameobject.manager.GameObjectManager;
import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptDefine;
import com.palmjoys.yf1b.act.mail.entity.MailEntity;
import com.palmjoys.yf1b.act.mail.manager.MailManager;
import com.palmjoys.yf1b.act.mail.model.MailAttrib;
import com.palmjoys.yf1b.act.wallet.entity.GiveEntity;
import com.palmjoys.yf1b.act.wallet.manager.GiveEntityManager;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;
import com.palmjoys.yf1b.act.wallet.model.RoomCardGiveAttrib;
import com.palmjoys.yf1b.act.wallet.model.WalletGiveVo;

@Service
public class WalletServiceImp implements WalletService{
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private GiveEntityManager giveEntityManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private HotPromptManager hotPromptManager;
	@Autowired
	private MailManager mailManager;
	@Autowired
	private GameObjectManager gameObjectManager;

	@Override
	public Object wallet_roomcard_give(Long accountId, String givePlayer, int giveNum) {
		if(giveNum <= 0){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "赠送房卡数量错误", null);
		}
		AccountEntity orgAccountEntity = accountManager.load(accountId);
		if(null == orgAccountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		
		AccountEntity giveAccountEntity = accountManager.findOf_starNO(givePlayer);
		if(null == giveAccountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接受赠送玩家信息不存在", null);
		}
		long giveAccount = giveAccountEntity.getId();
		if(orgAccountEntity.getId().longValue() == giveAccount){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "不能赠送房卡给自已", null);
		}
		
		GameObject gameObject = new GameObject();
		gameObject.type = "WALLET";
		gameObject.code = 10001;
		gameObject.amount = giveNum;
				
		int ret = gameObjectManager.checkEnough(accountId, gameObject);
		if(ret < 0){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "拥有的房卡数量不足,无法赠送房卡", null);
		}
		
		walletManager.addRoomCard(giveAccount, giveNum);
		walletManager.addRoomCard(accountId, (0-giveNum));
		walletManager.sendWalletNotify(giveAccount);
		walletManager.sendWalletNotify(accountId);
				
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
			mailEntity.addNewMail(mailAttrib, mailManager.getMailMaxCfg());
		}finally{
			mailEntity.unLock();
		}
		//检查推送红点消息
		if(hotPromptManager.updateHotPrompt(giveAccount, HotPromptDefine.HOT_KEY_MAIL) >0 ){
			hotPromptManager.notifyHotPrompt(giveAccount);
		}
		
		return Result.valueOfSuccess();
	}

	@Override
	public Object wallet_roomcard_record(Long accountId) {
		WalletGiveVo retVo = new WalletGiveVo();
		
		GiveEntity giveEntity = giveEntityManager.loadOrCreate(accountId);
		List<RoomCardGiveAttrib> giveList = giveEntity.getGiveList();
		for(RoomCardGiveAttrib item : giveList){
			AccountEntity accountEntity = accountManager.load(item.dst);
			retVo.addItem(item.giveTime, accountEntity.getNick(), accountEntity.getStarNO(), item.giveNum);
		}
		return Result.valueOfSuccess(retVo);
	}
	
}
