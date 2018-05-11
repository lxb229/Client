package com.palmjoys.yf1b.act.chat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.chat.model.ChatDefine;
import com.palmjoys.yf1b.act.chat.model.ChatVo;
import com.palmjoys.yf1b.act.dzpker.manager.GameLogicManager;
import com.palmjoys.yf1b.act.dzpker.model.SeatAttrib;
import com.palmjoys.yf1b.act.dzpker.model.TableAttrib;
import com.palmjoys.yf1b.act.framework.utils.MessagePushQueueUtils;

@Service
public class ChatServiceImp implements ChatService{
	@Autowired
	private GameLogicManager logicManager;
	@Autowired
	private SessionManager sessionManager;
	
	@Override
	public Object chat_send(Long accountId, int tableId, int type, int content) {
		logicManager.lock();
		try{
			while(true){
				TableAttrib table = logicManager.getTable(tableId);
				if(null == table){
					break;
				}
				SeatAttrib mySeat = table.getPlayerSeat(accountId);
				if(null == mySeat){
					break;
				}
				
				ChatVo vo = new ChatVo();
				vo.accountId = String.valueOf(accountId);
				vo.type = type;
				vo.content = content;
				
				@SuppressWarnings("rawtypes")
				Request pushMsg = Request.valueOf(ChatDefine.CHAT_COMMAND_CHAT_NOTIFY, 
						Result.valueOfSuccess(vo));
				MessagePushQueueUtils.getPushQueue(sessionManager).push(table.tablePlayers.keySet(), pushMsg);
				break;
			}
		}finally{
			logicManager.unlock();
		}
		return Result.valueOfSuccess();
	}

}
