package com.palmjoys.yf1b.act.corps.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.corps.service.CorpsService;

@Component
public class CorpsFacadeImp implements CorpsFacade{

	@Autowired
	private CorpsService corpsService;

	@Override
	public Object corps_get_corps_list(Long accountId, int type) {
		return corpsService.corps_get_corps_list(accountId, type);
	}

	@Override
	public Object corps_create(Long accountId, String corpsName, String wxNO) {
		return corpsService.corps_create(accountId, corpsName, wxNO);
	}

	@Override
	public Object corps_corps_search(Long accountId, String query) {
		return corpsService.corps_corps_search(accountId, query);
	}
	
	@Override
	public Object corps_quest_join(Long accountId, String corpsId, String reson) {
		return corpsService.corps_quest_join(accountId, corpsId, reson);
	}
		
	@Override
	public Object corps_corps_givecard(Long accountId, String corpsId, int cardNum) {
		return corpsService.corps_corps_givecard(accountId, corpsId, cardNum);
	}
	
	@Override
	public Object corps_table_list(Long accountId, String corpsId) {
		return corpsService.corps_table_list(accountId, corpsId);
	}

	@Override
	public Object corps_member_list(Long accountId, String corpsId, int type) {
		return corpsService.corps_member_list(accountId, corpsId, type);
	}
	
	@Override
	public Object corps_kick_member(Long accountId, String corpsId, String kickAccountId, int type) {
		return corpsService.corps_kick_member(accountId, corpsId, kickAccountId, type);
	}
	
	@Override
	public Object corps_quest_join_bt(Long accountId, String corpsId, String btAccountId, int bt, String reson) {
		return corpsService.corps_quest_join_bt(accountId, corpsId, btAccountId, bt, reson);
	}
	
	@Override
	public Object corps_member_blacklist_unlock(Long accountId, String corpsId, String btAccountId) {
		return corpsService.corps_member_blacklist_unlock(accountId, corpsId, btAccountId);
	}
	
	@Override
	public Object corps_set_roomcard_state(Long accountId, String corpsId, int state) {
		return corpsService.corps_set_roomcard_state(accountId, corpsId, state);
	}
	
	@Override
	public Object corps_zhuanrang_player(Long accountId, String corpsId, String btStarNO) {
		return corpsService.corps_zhuanrang_player(accountId, corpsId, btStarNO);
	}
	
	@Override
	public Object corps_destory(Long accountId, String corpsId) {
		return corpsService.corps_destory(accountId, corpsId);
	}
	
	@Override
	public Object corps_exit(Long accountId, String corpsId) {
		return corpsService.corps_exit(accountId, corpsId);
	}
	
	@Override
	public Object corps_modfiy_notice(Long accountId, String corpsId, String notice) {
		return corpsService.corps_modfiy_notice(accountId, corpsId, notice);
	}
	
	@Override
	public Object corps_modfiy_wxno(Long accountId, String corpsId, String wxno) {
		return corpsService.corps_modfiy_wxno(accountId, corpsId, wxno);
	}
	
	@Override
	public Object corps_modfiy_hidde(Long accountId, String corpsId, int hidde) {
		return corpsService.corps_modfiy_hidde(accountId, corpsId, hidde);
	}
	
	@Override
	public Object corps_member_rankinfo(Long accountId, String corpsId, int type) {
		return corpsService.corps_member_rankinfo(accountId, corpsId, type);
	}	

	@Override
	public Object corps_get_corps_detailed(Long accountId, String corpsId) {
		return corpsService.corps_get_corps_detailed(accountId, corpsId);
	}

	@Override
	public Object corps_leave_sence(Long accountId, String corpsId) {
		return corpsService.corps_leave_sence(accountId, corpsId);
	}

	@Override
	public Object corps_yaoqing_join(Long accountId, String corpsId, String btStarNO) {
		return corpsService.corps_yaoqing_join(accountId, corpsId, btStarNO);
	}

	@Override
	public Object corps_destory_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object corps_add_member_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object corps_kick_kmember_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object corps_table_create_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object corps_table_data_chanage_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object corps_zhanrang_corps_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object corps_notice_modfiy_notify() {
		return Result.valueOfSuccess();
	}

	@Override
	public Object corps_wallet_modfiy_notify() {
		return Result.valueOfSuccess();
	}

}
