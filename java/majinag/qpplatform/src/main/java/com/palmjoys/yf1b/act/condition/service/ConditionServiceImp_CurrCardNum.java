package com.palmjoys.yf1b.act.condition.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.palmjoys.yf1b.act.condition.model.ConditionAttrib;
import com.palmjoys.yf1b.act.condition.model.ConditionCheckResult;
import com.palmjoys.yf1b.act.condition.model.ConditionDefine;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

@Service
public class ConditionServiceImp_CurrCardNum implements ConditionService{
	@Autowired
	private WalletManager walletManager;

	@Override
	public int getConditionId() {
		return ConditionDefine.CONDITION_CURR_CARDNUM;
	}

	@Override
	public ConditionCheckResult checkCondition(Long playerId, ConditionAttrib condition) {
		ConditionCheckResult ret = new ConditionCheckResult();
		
		ret.total = condition.condition[0];
		ret.complete = walletManager.getRoomCard(playerId);	
		return ret;
	}

}
