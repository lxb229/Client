package com.palmjoys.yf1b.act.wallet.facade;

import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;

import com.palmjoys.yf1b.act.wallet.model.WalletDefine;

@NetworkFacade
public interface WalletFacade {
	
	@NetworkApi(value = WalletDefine.WALLET_COMMAND_ROOMCARD_GIVE, desc="赠送房卡")
	Object wallet_roomcard_give(@InSession Long accountId,
			@InBody(value = "givePlayer", desc = "赠送玩家StarNO") String givePlayer,
			@InBody(value = "giveNum", desc = "赠送数量") int giveNum);
	
	@NetworkApi(value = WalletDefine.WALLET_COMMAND_ROOMCARD_RECORD, desc="获取房卡赠送记录")
	Object wallet_roomcard_record(@InSession Long accountId);
}
