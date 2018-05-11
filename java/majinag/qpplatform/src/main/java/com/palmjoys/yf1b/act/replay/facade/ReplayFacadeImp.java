package com.palmjoys.yf1b.act.replay.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palmjoys.yf1b.act.replay.service.ReplayService;


@Component
public class ReplayFacadeImp implements ReplayFacade{
	@Autowired
	private ReplayService replayService;

	@Override
	public Object replay_query_record(Long accountId, int type, String query) {
		return replayService.replay_query_record(accountId, type, query);
	}

	@Override
	public Object replay_query_detailed_record(Long accountId, String recordId) {
		return replayService.replay_query_detailed_record(accountId, Long.valueOf(recordId));
	}

	@Override
	public Object replay_delete_record(Long accountId, String recordId) {
		return replayService.replay_delete_record(accountId, Long.valueOf(recordId));
	}

	@Override
	public Object replay_realname_authentication(Long accountId, String name, String cardId) {
		return replayService.replay_realname_authentication(accountId, name, cardId);
	}

	@Override
	public Object replay_phone_get_smsCode(Long accountId, String phone) {
		return replayService.replay_phone_get_smsCode(accountId, phone);
	}

	@Override
	public Object replay_phone_bind(Long accountId, String phone, String vaildCode) {
		return replayService.replay_phone_bind(accountId, phone, vaildCode);
	}

}
