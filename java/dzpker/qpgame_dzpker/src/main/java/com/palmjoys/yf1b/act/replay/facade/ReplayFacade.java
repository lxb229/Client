package com.palmjoys.yf1b.act.replay.facade;

import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;

import com.palmjoys.yf1b.act.replay.model.ReplayDefine;

@NetworkFacade
public interface ReplayFacade {
	
	@NetworkApi(value = ReplayDefine.REPLAY_COMMAND_QUERY_RECORD_LIST,
			desc="查询战绩")
	Object replay_query_record(@InSession Long accountId,
			@InBody(value = "type", desc = "查询类型(1=个人,2=俱乐部)") int type,
			@InBody(value = "query", desc = "查询参数(个人=玩家帐号Id,俱乐部=俱乐部Id)") String query);
	
	@NetworkApi(value = ReplayDefine.REPLAY_COMMAND_QUERY_RECORD_DETAILED,
			desc="查询详细战绩数据")
	Object replay_query_detailed_record(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子Id") int tableId);
}
