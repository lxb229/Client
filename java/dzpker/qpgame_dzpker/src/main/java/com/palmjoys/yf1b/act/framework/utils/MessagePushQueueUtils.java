package com.palmjoys.yf1b.act.framework.utils;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Request;

/**
 * 消息推 送队列工具类
 * */
public class MessagePushQueueUtils {
	private static MessagePushQueueUtils _instance = new MessagePushQueueUtils();
	private SessionManager sessionManager = null;
	//每个玩家玩家消息队列(KEY=玩家帐号Id,VALUE=消息队列)
	private BlockingQueue<PushMsgAttrib> _msgQueue = new LinkedBlockingQueue<>();
	//消息推送处理线程数
	private final int MESSAGE_PUSH_THREAD_NUM = 3;
	//消息推送处理线程
	private Thread []threads = null;
	
	private MessagePushQueueUtils(){
		threads = new Thread[MESSAGE_PUSH_THREAD_NUM];
		for(int index=0; index<MESSAGE_PUSH_THREAD_NUM; index++){
			threads[index] = new Thread(){
				@Override
				public void run() {
					super.run();
					try{
						while(true){
							pushThread();
							Thread.sleep(1);
						}
					}catch(Exception e){
					}
				}
			};
			threads[index].setName("游戏消息推送线程[" + index + "]");
			threads[index].setDaemon(true);
			threads[index].start();
		}
	}
	
	public static MessagePushQueueUtils getPushQueue(SessionManager sessionManager){
		_instance.sessionManager = sessionManager;
		return _instance;
	}

	/**
	 * recvIds 接收人
	 * msg 消息
	 * */
	@SuppressWarnings("rawtypes")
	public void push(Collection<Long> recvIds, Request msg){
		if(recvIds == null || recvIds.isEmpty())
			return;
		try{
			for(Long accountId : recvIds){
				PushMsgAttrib msgAttrib = new PushMsgAttrib();
				msgAttrib.pushMsg = msg;	
				msgAttrib.recvId = accountId;
				_msgQueue.put(msgAttrib);
			}			
		}catch(Exception e){
		}
	}
	
	/**
	 * recvIds 接收人
	 * msg 消息
	 * */
	@SuppressWarnings("rawtypes")
	public void push2(Long recvId, Request msg){
		if(recvId == null)
			return;
		try{
			PushMsgAttrib msgAttrib = new PushMsgAttrib();
			msgAttrib.pushMsg = msg;	
			msgAttrib.recvId = recvId;
			_msgQueue.put(msgAttrib);
		}catch(Exception e){
		}
	}
	
	private void pushThread(){
		PushMsgAttrib msgAttrib = null;
		try{
			msgAttrib = _msgQueue.take();
		}catch(Exception e){
		}				
		if(null == msgAttrib){
			return;
		}		
		try{
			this.sessionManager.send(msgAttrib.pushMsg, msgAttrib.recvId);
		}catch(Exception e){
		}
	}
			
	public class PushMsgAttrib{
		//接收者
		public Long recvId=null;
		//消息
		@SuppressWarnings("rawtypes")
		public Request pushMsg=null;
	}
}
