package com.palmjoys.yf1b.act.condition.service;

import com.palmjoys.yf1b.act.condition.model.ConditionAttrib;
import com.palmjoys.yf1b.act.condition.model.ConditionCheckResult;

public interface ConditionService {
	//获取条件Id
	public int getConditionId();
	//检查条件完成
	public ConditionCheckResult checkCondition(Long playerId, ConditionAttrib condition);
}
