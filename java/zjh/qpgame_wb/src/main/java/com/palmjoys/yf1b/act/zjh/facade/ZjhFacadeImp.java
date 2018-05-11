package com.palmjoys.yf1b.act.zjh.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.zjh.service.ZjhService;

@Component
public class ZjhFacadeImp implements ZjhFacade{
	@Autowired
	private ZjhService zjhService;

	@Override
	public Object zjh_get_room_list(Long accountId, int gameType) {
		return zjhService.zjh_get_room_list(accountId, gameType);
	}

	@Override
	public Object zjh_get_table_list(Long accountId, int cfgId) {
		return zjhService.zjh_get_table_list(accountId, cfgId);
	}

	@Override
	public Object zjh_quick_join(Long accountId, int cfgId) {
		return zjhService.zjh_quick_join(accountId, cfgId);
	}

	@Override
	public Object zjh_jion_tableId(Long accountId, int tableId, int type) {
		return zjhService.zjh_jion_tableId(accountId, tableId, type);
	}

	@Override
	public Object zjh_table_create(Long accountId, int cfgId) {
		return zjhService.zjh_table_create(accountId, cfgId);
	}

	@Override
	public Object zjh_table_leave(Long accountId, int tableId) {
		return zjhService.zjh_table_leave(accountId, tableId);
	}

	@Override
	public Object zjh_table_bet(Long accountId, int tableId, int seatIndex, int bt, int btVal) {
		return zjhService.zjh_table_bet(accountId, tableId, seatIndex, bt, btVal);
	}

	@Override
	public Object zjh_table_call_banker(Long accountId, int tableId, int bt) {
		return zjhService.zjh_table_call_banker(accountId, tableId, bt);
	}

	@Override
	public Object zjh_table_nn_game_ready(Long accountId, int tableId) {
		return zjhService.zjh_table_nn_game_ready(accountId, tableId);
	}

	@Override
	public Object zjh_gamestate_chanage_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object zjh_seat_chanage_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object zjh_table_bet_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object zjh_table_kick_player_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object zjh_table_lookcard_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object zjh_table_nn_callbanker_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object zjh_table_nn_gameready_notify() {
		return Result.valueOfSuccess();
	}

}
