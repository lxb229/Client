package com.palmjoys.yf1b.act.framework.socket;

import java.lang.reflect.Method;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.handler.Listener;
import org.treediagram.nina.network.handler.ServerHandler;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Response;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.account.entity.RoleEntity;
import com.palmjoys.yf1b.act.account.manager.RoleEntityManager;
import com.palmjoys.yf1b.act.framework.common.resource.ConfigValue;
import com.palmjoys.yf1b.act.framework.utils.GameLogUtils;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

/**
 * 服务器处理器侦听器
 */
@Component
public class ServerHandlerListener implements Listener {
	
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private RoleEntityManager roleEntityManager;
	@Static("DISABLE_GAME_LOG")
	private ConfigValue<Integer> disableGameLog;
	@Resource
	private ServerHandler serverHandler;

	//日志
	private Logger logger = LoggerFactory.getLogger(ServerHandlerListener.class);
	/**
	 * 初始化
	 */
	@PostConstruct
	protected void init() {
		serverHandler.addListener(this);
	}

	@Override
	public void received(Request<?> request, IoSession... sessions) {
	}

	@Override
	public void sent(Response<?> response, IoSession... sessions) {
	}	

	@Override
	public void beforeResponse(IoSession session) {
	}

	@Override
	public void caughtException(IoSession session, Request<?> request, int errorCode, Throwable e) {
	}

	@Override
	public Object beforeInvokeFacdeMethod(IoSession session, Object target, Method method, Request<?> request,
			Object value) {
		try{
			if(disableGameLog.getValue() > 0){
				//不记录操作日志,原样返回
				return value;
			}
			
			int commandId = request.getId();
			boolean bfilter = GameLogUtils.Instance().filterCommand(commandId);
			if(bfilter){
				//被过滤的命令不记录
				return value;
			}			
			Object accountIdObj = sessionManager.getOnlineIdentity(session);
			if(accountIdObj == null){
				//玩家还未在服务器认证
				return value;
			}
			Long accountId = (Long) accountIdObj;
			String s_accountId = String.valueOf(accountId);
			if(s_accountId.length() < 10){
				//非玩家连接不记录
				return value;
			}
			
			RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountId);
			if(null == roleEntity){
				//未找到此玩家
				return value;
			}
			String paramJson = "无";
			int flag = 0;
			String commandName = method.getName();
			Object paramObj = request.getBody();
			if(null != paramObj){
				paramJson = JsonUtils.object2String(paramObj);
			}
			long roomCard = walletManager.getRoomCard(accountId);
			long goldMoney = walletManager.getGoldMoney(accountId);
			long diamond = walletManager.getDiamond(accountId);
			GameLogUtils.Instance().writeLog(flag, commandId, roleEntity.getStarNO(), commandName,
					paramJson, roomCard, goldMoney, diamond);
		}catch(Exception e){
			logger.error("beforeInvokeFacdeMethod 异常!!!");
		}
		
		return value;
	}

	@Override
	public Object afterInvokeFacdeMethod(IoSession session, Object target, Method method, Request<?> request,
			Object value) {
		try{
			if(disableGameLog.getValue() > 0){
				//不记录操作日志,原样返回
				return value;
			}			
			int commandId = request.getId();
			boolean bfilter = GameLogUtils.Instance().filterCommand(commandId);
			if(bfilter){
				//被过滤的命令不记录
				return value;
			}			
			Object accountIdObj = sessionManager.getOnlineIdentity(session);
			if(accountIdObj == null){
				//玩家还未在服务器认证
				return value;
			}
			Long accountId = (Long) accountIdObj;
			String s_accountId = String.valueOf(accountId);
			if(s_accountId.length() < 10){
				//非玩家连接不记录
				return value;
			}
			
			RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountId);
			if(null == roleEntity){
				//未找到此玩家
				return value;
			}
			
			String paramJson = "无";
			int flag = 1;
			String commandName = method.getName();
			Object paramObj = request.getBody();
			if(null != paramObj){
				paramJson = JsonUtils.object2String(paramObj);
			}
			long roomCard = walletManager.getRoomCard(accountId);
			long goldMoney = walletManager.getGoldMoney(accountId);
			long diamond = walletManager.getDiamond(accountId);
			GameLogUtils.Instance().writeLog(flag, commandId, roleEntity.getStarNO(), commandName,
					paramJson, roomCard, goldMoney, diamond);
		}catch(Exception e){
			logger.error("afterInvokeFacdeMethod 异常!!!");
		}
		
		return value;
	}

}
