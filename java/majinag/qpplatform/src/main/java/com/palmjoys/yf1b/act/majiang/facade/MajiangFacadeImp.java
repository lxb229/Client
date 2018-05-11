package com.palmjoys.yf1b.act.majiang.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.network.annotation.InSession;
import org.treediagram.nina.network.model.Result;
import com.palmjoys.yf1b.act.majiang.service.MajiangService;

@Component
public class MajiangFacadeImp implements MajiangFacade{
	@Autowired
	private MajiangService majiangService;

	@Override
	public Object majiang_get_rulecfg(@InSession Long accountId, String corpsId) {
		return majiangService.majiang_get_rulecfg(accountId, corpsId);
	}

	@Override
	public Object majiang_room_create(Long accountId, String corpsId,
			int roomItemId, List<Integer> rules, String password) {
		return majiangService.majiang_room_create(accountId, corpsId, roomItemId, rules, password);
	}

	@Override
	public Object majiang_room_join(Long accountId, int tableId, String password) {
		return majiangService.majiang_room_join(accountId, tableId, password);
	}

	@Override
	public Object majiang_room_leav(Long accountId, int tableId) {
		return majiangService.majiang_room_leav(accountId, tableId);
	}

	@Override
	public Object majiang_room_get_roominfo(Long accountId, int tableId) {
		return majiangService.majiang_room_get_roominfo(accountId, tableId);
	}

	@Override
	public Object majiang_room_swap_card(Long accountId, int tableId, int[] cardIds) {
		return majiangService.majiang_room_swap_card(accountId, tableId, cardIds);
	}

	@Override
	public Object majiang_room_dinque(Long accountId, int tableId, int bt) {
		return majiangService.majiang_room_dinque(accountId, tableId, bt);
	}

	@Override
	public Object majiang_room_out_card(Long accountId, int tableId, int cardId) {
		return majiangService.majiang_room_out_card(accountId, tableId, cardId);
	}	

	@Override
	public Object majiang_room_otherbreak_card(Long accountId, int tableId, int bt, int cardId) {
		return majiangService.majiang_room_otherbreak_card(accountId, tableId, bt, cardId);
	}
	
	@Override
	public Object majiang_room_quest_delete(Long accountId, int tableId) {
		return majiangService.majiang_room_quest_delete(accountId, tableId);
	}
	
	@Override
	public Object majiang_room_delete_bt(Long accountId, int tableId, int bt) {
		return majiangService.majiang_room_delete_bt(accountId, tableId, bt);
	}

	@Override
	public Object majiang_room_next_game(Long accountId) {
		return majiangService.majiang_room_next_game(accountId);
	}

	@Override
	public Object majiang_room_tang_card_bt(Long accountId, int tableId, int[] btcards, int[] hucards, int outcard) {
		return majiangService.majiang_room_tang_card_bt(accountId, tableId, btcards, hucards, outcard);
	}

	@Override
	public Object majiang_room_baojiao_bt(Long accountId, int tableId, int btVal) {
		return majiangService.majiang_room_baojiao_bt(accountId, tableId, btVal);
	}

	@Override
	public Object majiang_room_otherbreak_card_lsmj_bt(Long accountId, int tableId, int bt, int cardId, int gangType,
			int replace) {
		return majiangService.majiang_room_otherbreak_card_lsmj_bt(accountId, tableId, bt, cardId, gangType, replace);
	}

	@Override
	public Object majiang_room_ncmj_piaopai_bt(Long accountId, int tableId, int btVal) {
		return majiangService.majiang_room_ncmj_piaopai_bt(accountId, tableId, btVal);
	}	

	@Override
	public Object majiang_room_password_state(Long accountId, int tableId) {
		return majiangService.majiang_room_password_state(accountId, tableId);
	}

	@Override
	public Object majiang_room_trusteeship(Long accountId, int tableId, int bt) {
		return majiangService.majiang_room_trusteeship(accountId, tableId, bt);
	}

	@Override
	public Object majiang_room_gamestate_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_seat_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_dinque_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_swapcard_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_outcard_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_breakcard_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_destory_notify() {
		return Result.valueOfSuccess();
	}	

	@Override
	public Object majiang_room_destory_bt_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object majiang_room_baojiao_bt_notify() {
		return Result.valueOfSuccess();
	}
	
	@Override
	public Object majiang_room_ncmj_piaopai_bt_notify() {
		return Result.valueOfSuccess();
	}

}
