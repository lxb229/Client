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
	public Object replay_query_detailed_record(Long accountId, int tableId) {
		return replayService.replay_query_detailed_record(accountId, tableId);
	}
}
