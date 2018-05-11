package com.palmjoys.yf1b.act.majiang.service;

import java.util.List;

public interface MajiangService {
	//获取麻将条目规则配置
	public Object majiang_get_rulecfg(Long accountId, String corpsId);
	//创建桌子
	public Object majiang_room_create(Long accountId, String corpsId,
			int roomItemId, List<Integer> rules, String password);
	//加入桌子
	public Object majiang_room_join(Long accountId, int tableId, String password);
	//退出桌子
	public Object majiang_room_leav(Long accountId, int tableId);
	//请求桌子信息
	public Object majiang_room_get_roominfo(Long accountId, int tableId);
	//换牌表态
	public Object majiang_room_swap_card(Long accountId, int tableId, int[] cardIds);
	//定缺表态
	public Object majiang_room_dinque(Long accountId, int tableId, int bt);
	//出牌表态
	public Object majiang_room_out_card(Long accountId, int tableId, int cardId);
	//胡杠碰吃过表态
	public Object majiang_room_otherbreak_card(Long accountId, int tableId, int bt, int cardId);
	//申请解散桌子
	public Object majiang_room_quest_delete(Long accountId, int tableId);
	//桌子解散表态
	public Object majiang_room_delete_bt(Long accountId, int tableId, int bt);
	//点击进入下一局游戏
	public Object majiang_room_next_game(Long accountId);
	//躺牌表态
	public Object majiang_room_tang_card_bt(Long accountId, int tableId, int[] btcards, int[] hucards, int outcard);
	//报叫表态
	public Object majiang_room_baojiao_bt(Long accountId, int tableId, int btVal);
	//乐山麻将胡碰杠表态
	public Object majiang_room_otherbreak_card_lsmj_bt(Long accountId, int tableId, int bt, int cardId, int gangType,
			int replace);
	//南充麻将漂牌表态
	public Object majiang_room_ncmj_piaopai_bt(Long accountId, int tableId, int btVal);
	//获取桌子密码标识状态
	public Object majiang_room_password_state(Long accountId, int tableId);
	//设置托管
	public Object majiang_room_trusteeship(Long accountId, int tableId, int bt);
}
