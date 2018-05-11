package com.palmjoys.yf1b.act.hotprompt.facade;

import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptDefine;

@NetworkFacade
public interface HotpromptFacade {
	
	@NetworkApi(value = HotPromptDefine.HOTPROMPT_COMMAND_GET_HOTDATA, 
			desc="获取红点数据")
	Object hotprompt_get_hotdata(@InBody(value = "accountId", desc = "帐号Id") String accountId);
	
	@NetworkApi(value = HotPromptDefine.HOTPROMPT_COMMAND_HOTDATA_NOTIFIY, 
			desc="推送消息(红点提示数据)")
	Object hotprompt_hotdata_notify();
}
