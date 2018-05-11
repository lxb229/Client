package com.palmjoys.yf1b.act.corps.facade;

import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;
import com.palmjoys.yf1b.act.corps.model.CorpsDefine;

@NetworkFacade
public interface CorpsFacade {
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_CORPS_LIST,
			desc="获取帮会列表")
	Object corps_get_corps_list(@InSession Long accountId,
			@InBody(value = "type", desc = "类型(1=已加入,2=推荐,3=所有公开)") int type);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_CORPS_CREATE,
			desc="创建帮会")
	Object corps_create(@InSession Long accountId,
			@InBody(value = "corpsName", desc = "帮会名称(具有唯一性)") String corpsName,
			@InBody(value = "wxNO", desc = "馆主微信号") String wxNO);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_CORPS_SEARCH,
			desc="搜索帮会指定帮会")
	Object corps_corps_search(@InSession Long accountId,
			@InBody(value = "query", desc = "搜索KEY") String query);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_JOIN_QUEST,
			desc="申请加入帮会")
	Object corps_quest_join(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId,
			@InBody(value = "reson", desc = "加入理由") String reson);
		
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_CORPS_GIVE_CARD,
			desc="捐赠房卡")
	Object corps_corps_givecard(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId,
			@InBody(value = "cardNum", desc = "房卡数量") int cardNum);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_CORPS_TABLE_LIST,
			desc="获取帮会所有游戏桌子")
	Object corps_table_list(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_CORPS_MEMBER_LIST,
			desc="获取帮会所有成员信息")
	Object corps_member_list(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId,
			@InBody(value = "type", desc = "类型(1=成员列表,2=申请加入列表,3=黑名单)") int type);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_KICK_MEMBER,
			desc="踢出帮会成员")
	Object corps_kick_member(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId,
			@InBody(value = "kickAccountId", desc = "踢出的玩家帐号Id") String kickAccountId,
			@InBody(value = "type", desc = "类型(0=不加入黑名单,1=加入黑名单)") int type);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_JOIN_BT,
			desc="帮会同意或拒绝玩家加入帮会")
	Object corps_quest_join_bt(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId,
			@InBody(value = "btAccountId", desc = "玩家帐号Id") String btAccountId,
			@InBody(value = "bt", desc = "表态(0=拒绝,1=同意)") int bt,
			@InBody(value = "reson", desc = "拒绝原因") String reson);	
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_BLACKLIST_UNLOCK,
			desc="解锁黑名单玩家")
	Object corps_member_blacklist_unlock(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId,
			@InBody(value = "btAccountId", desc = "解锁的玩家帐号Id") String btAccountId);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_SET_ROOMCARD_STATE,
			desc="设置房卡使用状态")
	Object corps_set_roomcard_state(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId,
			@InBody(value = "state", desc = "0=关闭,1=打开") int state);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_CORPS_ZHUANRANG,
			desc="帮会转让")
	Object corps_zhuanrang_player(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId,
			@InBody(value = "btStarNO", desc = "转让的明星号") String btStarNO);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_DESTORY,
			desc="解散帮会")
	Object corps_destory(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_EXIT,
			desc="成员退出帮会")
	Object corps_exit(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_MODFIY_NOTICE,
			desc="修改帮会公告")
	Object corps_modfiy_notice(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId,
			@InBody(value = "notice", desc = "帮会公告内容") String notice);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_MODFIY_WXNO,
			desc="修改帮会微信号")
	Object corps_modfiy_wxno(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId,
			@InBody(value = "wxno", desc = "新的微信号") String wxno);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_MODFIY_HIDDE,
			desc="修改帮会可见状态")
	Object corps_modfiy_hidde(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId,
			@InBody(value = "hidde", desc = "0=可见,1=隐藏") int hidde);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_RANK_AINFO,
			desc="获取帮会成员排行榜")
	Object corps_member_rankinfo(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId,
			@InBody(value = "type", desc = "1=活跃度排行,2=战绩排行,3=房卡捐献排行") int type);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_DETAILED,
			desc="获取帮会详细信息")
	Object corps_get_corps_detailed(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_LEAVE_SENCE,
			desc="离开帮会场景")
	Object corps_leave_sence(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId);
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_YAOQING,
			desc="帮主邀请加入帮会")
	Object corps_yaoqing_join(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id") String corpsId,
			@InBody(value = "btStarNO", desc = "邀请玩家明星号") String btStarNO);
		
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_DESTORY_NOTIFY,
			desc="推送消息(麻将馆解散通知)")
	Object corps_destory_notify();
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_ADDMEMBER_NOTIFY,
			desc="推送消息(麻将馆成员添加通知)")
	Object corps_add_member_notify();
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_KICKMEMBER_NOTIFY,
			desc="推送消息(麻将馆成员移除通知)")
	Object corps_kick_kmember_notify();
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_TABLE_CREATE_NOTIFY,
			desc="推送消息(帮会房间创建)")
	Object corps_table_create_notify();
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_TABLE_DATA_CHANAGE_NOTIFY,
			desc="推送消息(帮会房间数据更改)")
	Object corps_table_data_chanage_notify();
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_ZHUANRANG_NOTIFY,
			desc="推送消息(帮会转让通知)")
	Object corps_zhanrang_corps_notify();
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_NOTICE_MODFIY_NOTIFY,
			desc="推送消息(帮会微信公告修改通知)")
	Object corps_notice_modfiy_notify();
	
	@NetworkApi(value = CorpsDefine.CORPS_COMMAND_CORPS_WALLET_NOTIFY,
			desc="推送消息(帮会钱包变化通知)")
	Object corps_wallet_modfiy_notify();
}
