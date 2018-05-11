package com.palmjoys.yf1b.act.corps.service;

public interface CorpsService {
	//获取帮会列表信息
	public Object corps_get_corps_list(Long accountId, int type);
	//创建帮会(有最大创建数限制)
	public Object corps_create(Long accountId, String corpsName, String wxNO);
	//搜索帮会
	public Object corps_corps_search(Long accountId, String query);
	//申请加入帮会
	public Object corps_quest_join(Long accountId, String corpsId, String reson);
	//捐赠房卡
	public Object corps_corps_givecard(Long accountId, String corpsId, int cardNum);
	//获取指定帮会所有等待房间
	public Object corps_table_list(Long accountId, String corpsId);
	//获取群成员信息
	public Object corps_member_list(Long accountId, String corpsId, int type);
	//踢出帮会成员
	public Object corps_kick_member(Long accountId, String corpsId, String kickAccountId, int type);
	//帮会同意或拒绝玩家加入帮会
	public Object corps_quest_join_bt(Long accountId, String corpsId, String btAccountId, int bt, String reson);
	//解锁黑名单玩家
	public Object corps_member_blacklist_unlock(Long accountId, String corpsId, String btAccountId);
	//馆主设置房卡使用状态
	public Object corps_set_roomcard_state(Long accountId, String corpsId, int state);
	//帮会转让
	public Object corps_zhuanrang_player(Long accountId, String corpsId, String btStarNO);
	//解散帮会
	public Object corps_destory(Long accountId, String corpsId);
	//退出帮会
	public Object corps_exit(Long accountId, String corpsId);
	//修改帮会公告
	public Object corps_modfiy_notice(Long accountId, String corpsId, String notice);
	//修改帮会微信号
	public Object corps_modfiy_wxno(Long accountId, String corpsId, String wxno);
	//修改帮会可见状态
	public Object corps_modfiy_hidde(Long accountId, String corpsId, int hidde);
	//获取帮会成员排行
	public Object corps_member_rankinfo(Long accountId, String corpsId, int type);
	//获取帮会详细信息
	public Object corps_get_corps_detailed(Long accountId, String corpsId);
	//离开帮会场景
	public Object corps_leave_sence(Long accountId, String corpsId);
	//帮主邀请加入帮会
	public Object corps_yaoqing_join(Long accountId, String corpsId, String btStarNO);
}
