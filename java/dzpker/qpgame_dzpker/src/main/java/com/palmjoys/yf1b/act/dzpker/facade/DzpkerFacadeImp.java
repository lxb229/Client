package com.palmjoys.yf1b.act.dzpker.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.dzpker.service.DzpkerService;

@Component
public class DzpkerFacadeImp implements DzpkerFacade{
	@Autowired
	private DzpkerService dzpkerService;

	@Override
	public Object dzpker_table_get_cfg(Long accountId) {
		return dzpkerService.dzpker_table_get_cfg(accountId);
	}

	@Override
	public Object dzpker_table_create(Long accountId, String tableName, int small, int big, int minJoin, int vaildTime,
			int insurance, int straddle, int buyMax) {
		return dzpkerService.dzpker_table_create(accountId, tableName, small, big, 
				minJoin, vaildTime, insurance, straddle, buyMax);
	}
	
	@Override
	public Object dzpker_table_start_run(Long accountId, int tableId) {
		return dzpkerService.dzpker_table_start_run(accountId, tableId);
	}

	@Override
	public Object dzpker_table_join(Long accountId, int tableId) {
		return dzpkerService.dzpker_table_join(accountId, tableId);
	}

	@Override
	public Object dzpker_table_leave(Long accountId, int tableId) {
		return dzpkerService.dzpker_table_leave(accountId, tableId);
	}

	@Override
	public Object dzpker_seat_down(Long accountId, int tableId, int seatIndex) {
		return dzpkerService.dzpker_seat_down(accountId, tableId, seatIndex);
	}

	@Override
	public Object dzpker_seat_up(Long accountId, int tableId, int seatIndex) {
		return dzpkerService.dzpker_seat_up(accountId, tableId, seatIndex);
	}

	@Override
	public Object dzpker_buy_chip(Long accountId, int tableId, int chipNum) {
		return dzpkerService.dzpker_buy_chip(accountId, tableId, chipNum);
	}

	@Override
	public Object dzpker_table_bt(Long accountId, int tableId, int bt, int btVal) {
		return dzpkerService.dzpker_table_bt(accountId, tableId, bt, btVal);
	}

	@Override
	public Object dzpker_query_buychip_list(Long accountId) {
		return dzpkerService.dzpker_query_buychip_list(accountId);
	}

	@Override
	public Object dzpker_trans_buychip_item(Long accountId, String itemId, int bt) {
		return dzpkerService.dzpker_trans_buychip_item(accountId, itemId, bt);
	}

	@Override
	public Object dzpker_straddle_bt(Long accountId, int tableId, int seatIndex) {
		return dzpkerService.dzpker_straddle_bt(accountId, tableId, seatIndex);
	}

	@Override
	public Object dzpker_get_fighted_table_list(Long accountId) {
		return dzpkerService.dzpker_get_fighted_table_list(accountId);
	}

	@Override
	public Object dzpker_table_prev_fight(Long accountId, int tableId) {
		return dzpkerService.dzpker_table_prev_fight(accountId, tableId);
	}

	@Override
	public Object dzpker_table_player_list(Long accountId, int tableId) {
		return dzpkerService.dzpker_table_player_list(accountId, tableId);
	}

	@Override
	public Object dzpker_table_player_once_winscore(Long accountId, int tableId) {
		return dzpkerService.dzpker_table_player_once_winscore(accountId, tableId);
	}

	@Override
	public Object dzpker_table_buy_insurance(Long accountId, int tableId, int bt, int[] buyCards, int buyMoney,
			int payMoney) {
		return dzpkerService.dzpker_table_buy_insurance(accountId, tableId, bt, buyCards, buyMoney, payMoney);
	}

	@Override
	public Object dzpker_table_get_caree_info(Long accountId) {
		return dzpkerService.dzpker_table_get_caree_info(accountId);
	}

	@Override
	public Object dzpker_table_show_card_bt(Long accountId, int tableId) {
		return dzpkerService.dzpker_table_show_card_bt(accountId, tableId);
	}

	@Override
	public Object dzpker_table_get_table_info(Long accountId, int tableId) {
		return dzpkerService.dzpker_table_get_table_info(accountId, tableId);
	}

	@Override
	public Object dzpker_table_get_all_win_score_info(Long accountId, String recordId) {
		return dzpkerService.dzpker_table_get_all_win_score_info(accountId, Long.valueOf(recordId));
	}

	@Override
	public Object dzpker_table_state_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_seat_state_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_table_bt_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_table_settlement_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_table_settlement_insurance_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_table_buy_chip_sucess_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_table_show_card_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_table_straddl_bt_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object dzpker_table_seat_kick_notify() {
		return Result.valueOfSuccess();
	}

}
