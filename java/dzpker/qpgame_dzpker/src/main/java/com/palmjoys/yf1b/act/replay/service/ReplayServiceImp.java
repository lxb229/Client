package com.palmjoys.yf1b.act.replay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.network.model.Result;
import com.palmjoys.yf1b.act.framework.common.manager.ErrorCodeManager;
import com.palmjoys.yf1b.act.replay.manager.ReplayManager;
import com.palmjoys.yf1b.act.replay.manager.ReplayPlayerManager;
import com.palmjoys.yf1b.act.replay.manager.ReplayTableManager;
import com.palmjoys.yf1b.act.replay.model.RecordDetailedVo;
import com.palmjoys.yf1b.act.replay.model.RecordVo;
import com.palmjoys.yf1b.act.replay.model.ReplayDefine;

@Service
public class ReplayServiceImp implements ReplayService{
	@Autowired
	private ErrorCodeManager errorManager;
	@Autowired
	private ReplayPlayerManager replayPlayerManager;
	@Autowired
	private ReplayTableManager replayTableManager;
	@Autowired
	private ReplayManager replayManager;

	@Override
	public Object replay_query_record(Long accountId, int type, String query) {
		if(1 != type && 2 != type){
			return Result.valueOfError(ReplayDefine.REPLAY_ERROR_COMMAND_PARAM,
					errorManager.Error2Desc(ReplayDefine.REPLAY_ERROR_COMMAND_PARAM), null);
		}
		RecordVo retVo = null;
		replayManager.lock();
		try{
			if(1 == type){
				retVo = replayPlayerManager.replayQuery(accountId);
			}else{
				retVo = replayTableManager.replayQuery(query);
			}
		}finally{
			replayManager.unLock();
		}
		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object replay_query_detailed_record(Long accountId, int tableId) {
		RecordDetailedVo retVo = null; 
		replayManager.lock();
		try{
			retVo = replayTableManager.queryDetailed(tableId);
		}finally{
			replayManager.unLock();
		}
		return Result.valueOfSuccess(retVo);
	}
}
