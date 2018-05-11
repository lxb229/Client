package com.palmjoys.yf1b.act.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.chat.model.ChatDefine;
import com.palmjoys.yf1b.act.chat.model.ChatVo;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.framework.utils.MessagePushQueueUtils;
import com.palmjoys.yf1b.act.majiang.manager.GameDataManager;
import com.palmjoys.yf1b.act.majiang.manager.GameLogicManager;
import com.palmjoys.yf1b.act.majiang.model.GameDataAttrib;
import com.palmjoys.yf1b.act.majiang.model.SeatAttrib;
import com.palmjoys.yf1b.act.majiang.model.TableAttrib;

@Service
public class ChatServiceImp implements ChatService{
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private GameLogicManager gameLogicManager;
	@Autowired
	private GameDataManager gameDataManager;
	
	@Override
	public Object chat_send(Long accountId, int tableId, int type, int content) {
		String err = "接口参数错误";
		gameLogicManager.lock();
		try{
			do{
				long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
				GameDataAttrib gameDataAttrib = gameDataManager.getGameData(accountId);
				if(currTime < gameDataAttrib.chatInnerTime){
					err = "聊天过于频繁,请稍后再发送信息";
					break;
				}
				
				gameDataAttrib.chatInnerTime = currTime + 3*1000;
				
				TableAttrib table = gameLogicManager.getTable(tableId);
				if(null != table){
					ChatVo vo = new ChatVo();
					vo.accountId = String.valueOf(accountId);
					vo.type = type;
					vo.content = content;
					
					for(SeatAttrib seat : table.seats){
						if(0 == seat.accountId){
							continue;
						}
						@SuppressWarnings("rawtypes")
						Request pushMsg = Request.valueOf(ChatDefine.CHAT_COMMAND_CHAT_NOTIFY, 
								Result.valueOfSuccess(vo));
						MessagePushQueueUtils.getPushQueue(sessionManager).push2(seat.accountId, pushMsg);
					}
				}
				err = "";
			}while(false);
						
		}finally{
			gameLogicManager.unLock();
		}
		
		if(err.isEmpty()==false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		
		return Result.valueOfSuccess();
	}
}
