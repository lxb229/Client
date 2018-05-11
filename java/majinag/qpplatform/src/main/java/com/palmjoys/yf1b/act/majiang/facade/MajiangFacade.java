package com.palmjoys.yf1b.act.majiang.facade;

import java.util.List;

import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;
import com.palmjoys.yf1b.act.majiang.model.MajiangDefine;

@NetworkFacade
public interface MajiangFacade {
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_RULECFG,
			desc="获取麻将条目配置")
	Object majiang_get_rulecfg(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id,0=无帮会") String corpsId);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_ROOM_CREATE,
			desc="创建桌子")
	Object majiang_room_create(@InSession Long accountId,
			@InBody(value = "corpsId", desc = "帮会Id,0=无帮会") String corpsId,
			@InBody(value = "roomItemId", desc = "桌子配置条目Id") int roomItemId,
			@InBody(value = "rules", desc = "桌子规则") List<Integer> rules,
			@InBody(value = "password", desc = "桌子密码,0=无密码") String password);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_ROOM_JOIN,
			desc="加入桌子")
	Object majiang_room_join(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子号") int tableId,
			@InBody(value = "password", desc = "桌子密码") String password);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_ROOM_LEAV,
			desc="退出桌子")
	Object majiang_room_leav(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子号") int tableId);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_ROOM_INFO,
			desc="获取桌子信息")
	Object majiang_room_get_roominfo(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子号") int tableId);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_SWAP_CARD,
			desc="换牌表态")
	Object majiang_room_swap_card(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子号") int tableId,
			@InBody(value = "cardIds", desc = "要换的牌") int[] cardIds);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_DINQUE,
			desc="定缺表态")
	Object majiang_room_dinque(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子号") int tableId,
			@InBody(value = "bt", desc = "表态结果(1=万,2=筒,3=条)") int bt);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_OUT_CARD,
			desc="出牌表态")
	Object majiang_room_out_card(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子号") int tableId,
			@InBody(value = "cardId", desc = "要出的牌") int cardId);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_BREAK_CARD,
			desc="胡杠碰吃表态")
	Object majiang_room_otherbreak_card(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子号") int tableId,
			@InBody(value = "bt", desc = "表态类型(0=胡,1=杠,2=碰,3=吃,4=过)") int bt,
			@InBody(value = "cardId", desc = "表态的牌") int cardId);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_TABLE_DESTORY_QUEST,
			desc="申请解散桌子")
	Object majiang_room_quest_delete(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子号") int tableId);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_TABLE_DESTORY_BT,
			desc="解散桌子表态")
	Object majiang_room_delete_bt(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子号") int tableId,
			@InBody(value = "bt", desc = "表态结果(1=拒绝,2=同意)") int bt);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_TABLE_NEXT_GAME,
			desc="点击下一局游戏")
	Object majiang_room_next_game(@InSession Long accountId);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_TABLE_TANG_BT,
			desc="躺牌表态")
	Object majiang_room_tang_card_bt(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子号") int tableId,
			@InBody(value = "btcards", desc = "表态的牌") int []btcards,
			@InBody(value = "hucards", desc = "躺牌后胡的牌") int []hucards,
			@InBody(value = "outcard", desc = "躺牌后要打出的牌") int outcard);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_TABLE_BAOJIAO_BT,
			desc="报叫表态")
	Object majiang_room_baojiao_bt(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子号") int tableId,
			@InBody(value = "btVal", desc = "表态的结果(0=不报叫,1=报叫)") int btVal);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_BREAK_CARD_LSMJ_BT,
			desc="乐山麻将胡杠碰吃表态")
	Object majiang_room_otherbreak_card_lsmj_bt(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子号") int tableId,
			@InBody(value = "bt", desc = "表态类型(0=胡,1=杠,2=碰,3=吃,4=过)") int bt,
			@InBody(value = "cardId", desc = "表态的牌(目标牌,如幺鸡当3条用,表态牌为3条)") int cardId,
			@InBody(value = "gangType", desc = "杠类型(1=巴杠,2=暗杠或直杠)") int gangType,
			@InBody(value = "replace", desc = "表态的牌是否是幺鸡") int replace);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_TABLE_PIAOPAI_BT,
			desc="南充麻将漂牌表态")
	Object majiang_room_ncmj_piaopai_bt(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子号") int tableId,
			@InBody(value = "btVal", desc = "表态的结果(漂几个)") int btVal);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_TABLE_PASSWORD_STATE,
			desc="获取桌子密码标识")
	Object majiang_room_password_state(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子号") int tableId);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_TABLE_TRUSTEESHIP,
			desc="设置托管")
	Object majiang_room_trusteeship(@InSession Long accountId,
			@InBody(value = "tableId", desc = "桌子号") int tableId,
			@InBody(value = "bt", desc = "0=取消托管,1=设置托管") int bt);
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_TABLE_STATE_NOTIFY,
			desc="推送消息(游戏状态变化)")
	Object majiang_room_gamestate_notify();
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_TABLE_SEAT_NOTIFY,
			desc="推送消息(座位数据变化)")
	Object majiang_room_seat_notify();
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_DINQUE_NOTIFY,
			desc="推送消息(定缺数据通知)")
	Object majiang_room_dinque_notify();
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_SWAPCARD_NOTIFY,
			desc="推送消息(换牌数据通知)")
	Object majiang_room_swapcard_notify();
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_OUTCARD_NOTIFY,
			desc="推送消息(出牌数据通知)")
	Object majiang_room_outcard_notify();
		
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_BREAKCARD_NOTIFY,
			desc="推送消息(胡杠碰吃过表态通知)")
	Object majiang_room_breakcard_notify();
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_TABLE_DESTORY_NOTIFY,
			desc="推送消息(桌子已解散通知)")
	Object majiang_room_destory_notify();
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_TABLE_DESTORY_BT_NOTIFY,
			desc="推送消息(桌子解散表态通知)")
	Object majiang_room_destory_bt_notify();

	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_TABLE_BAOJIAO_BT_NOTIFY,
			desc="推送消息(座位报叫表态通知)")
	Object majiang_room_baojiao_bt_notify();
	
	@NetworkApi(value = MajiangDefine.MAJIANGGAME_COMMAND_TABLE_PIAOPAI_BT_NOTIFY,
			desc="推送消息(南充麻将漂牌数据通知)")
	Object majiang_room_ncmj_piaopai_bt_notify();
}
