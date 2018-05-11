package com.palmjoys.yf1b.act.replay.facade;

import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;

import com.palmjoys.yf1b.act.replay.model.RecordMessageDefine;

@NetworkFacade
public interface ReplayFacade {
	
	@NetworkApi(value = RecordMessageDefine.REPLAY_COMMAND_QUERY_RECORD_LIST,
			desc="查询战绩")
	Object replay_query_record(@InSession Long accountId,
			@InBody(value = "type", desc = "查询类型(1=个人,2=麻将馆)") int type,
			@InBody(value = "query", desc = "查询参数(个人=玩家帐号Id,麻将馆=麻将馆Id)") String query);
	
	@NetworkApi(value = RecordMessageDefine.REPLAY_COMMAND_QUERY_RECORD_DETAILED,
			desc="查询详细战绩数据")
	Object replay_query_detailed_record(@InSession Long accountId,
			@InBody(value = "recordId", desc = "记录Id") String recordId);
	
	@NetworkApi(value = RecordMessageDefine.REPLAY_COMMAND_DELETE_RECORD,
			desc="馆主删除战绩数据")
	Object replay_delete_record(@InSession Long accountId,
			@InBody(value = "recordId", desc = "记录Id") String recordId);
	
	@NetworkApi(value = RecordMessageDefine.REPLAY_COMMAND_REALNAME_AUTHENTICATION,
			desc="玩家实名认证")
	Object replay_realname_authentication(@InSession Long accountId,
			@InBody(value = "name", desc = "姓名") String name,
			@InBody(value = "cardId", desc = "身份证") String cardId);
	
	@NetworkApi(value = RecordMessageDefine.REPLAY_COMMAND_PHONE_VAILDCODE,
			desc="获取手机短信码")
	Object replay_phone_get_smsCode(@InSession Long accountId,
			@InBody(value = "phone", desc = "手机号") String phone);
	
	@NetworkApi(value = RecordMessageDefine.REPLAY_COMMAND_PHONE_BIND,
			desc="手机帮定")
	Object replay_phone_bind(@InSession Long accountId,
			@InBody(value = "phone", desc = "手机号") String phone,
			@InBody(value = "vaildCode", desc = "验证码") String vaildCode);

}
