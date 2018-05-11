package com.palmjoys.yf1b.act.activity.facade;

import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;

import com.palmjoys.yf1b.act.activity.model.ActivityDefine;

@NetworkFacade
public interface ActivityFacade {
	
	@NetworkApi(value = ActivityDefine.ACTIVITY_COMMAND_GET_ACTIVITY_LIST,
			desc="获取活动信息列表")
	Object activity_get_activity_list(@InSession Long accountId);
}
