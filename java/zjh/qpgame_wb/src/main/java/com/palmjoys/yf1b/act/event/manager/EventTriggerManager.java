package com.palmjoys.yf1b.act.event.manager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.treediagram.nina.memcache.orm.Querier;

import com.palmjoys.yf1b.act.event.entity.EventEntity;
import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.service.EventService;

@Component
public class EventTriggerManager implements ApplicationContextAware{

	@Autowired
	private EventEntityManager eventEntityManager;
	@Autowired
	private Querier querier;
	
	private Queue<EventAttrib> eventQueue = new LinkedList<EventAttrib>();
	private Lock evtQueueLock = new ReentrantLock();
	private Thread evtNotfiyThread = null;
	private boolean bRun = false;
	private Map<Integer, EventService> serviceList = new HashMap<Integer, EventService>();
	private ApplicationContext applicationContext;
	private Logger logger = LoggerFactory.getLogger(EventTriggerManager.class);
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}
	
	@PostConstruct
	private void init(){
		for(EventService service : applicationContext.getBeansOfType(EventService.class).values()){
			if(serviceList.containsKey(service.getEventId()) == false){
				serviceList.put(service.getEventId(), service);
			}
		}
		evtNotfiyThread = new Thread(){
			@Override
			public void run() {
				super.run();
				try{
					while(bRun){
						eventExec();
						sleep(1);
					}
					appExitExec();
				}catch(Exception e){
				}
			}
		};
		bRun = true;
		evtNotfiyThread.setDaemon(true);
		evtNotfiyThread.setName("事件上报运行逻辑线程");
		evtNotfiyThread.start();
	}
	
	@PreDestroy
	private void destory(){
		bRun = false;
	}
			
	/**
	 * 事件触发
	 * eventAttrib 事件属性
	 * */
	public boolean triggerEvent(EventAttrib eventAttrib){
		if(null == eventAttrib)
			return false;
		EventService service = serviceList.get(eventAttrib.eventId);
		if(null == service)
			return false;
		evtQueueLock.lock();
		try{
			eventQueue.add(eventAttrib);
		}finally{
			evtQueueLock.unlock();
		}
		return true;
	}
	
	/**
	 * 从事件队列中获取事件执行
	 * */
	private void eventExec(){
		EventAttrib eventAttrib = null;
		evtQueueLock.lock();
		try{
			if(eventQueue.size() > 0){
				eventAttrib = eventQueue.poll();
			}
		}finally{
			evtQueueLock.unlock();
		}
		if(null != eventAttrib){
			try{
				EventService service = serviceList.get(eventAttrib.eventId);
				if(null != service){
					boolean bOK = service.execEvent(eventAttrib);
					if(bOK == false){
						eventAttrib.fail = eventAttrib.fail - 1;
						if(eventAttrib.fail <= 0){
							saveFialEvent(eventAttrib);
						}else{
							evtQueueLock.lock();
							try{
								eventQueue.add(eventAttrib);
							}finally{
								evtQueueLock.unlock();
							}
						}
					}
				}else{
					logger.error("未找到=[" + eventAttrib.eventId + "]的事件服务");
				}
			}catch(Exception e){
				logger.error("事件=[" + eventAttrib.eventId + "]的执行异常!!!");
			}
			eventAttrib = null;
		}		
	}
	
	/**
	 * 主应用程序退出时执行,确保队列全部执行完成
	 * */
	private void appExitExec(){
		int queueSize = 1;
		while(queueSize > 0){
			eventExec();
			queueSize = getQueueSize();
		}
	}
	
	public int getQueueSize(){
		int size = 0;
		evtQueueLock.lock();
		try{
			size = eventQueue.size();
		}finally{
			evtQueueLock.unlock();
		}
		
		return size;
	}
	
	/**
	 * 保存上传失败的事件信息
	 * */
	private void saveFialEvent(EventAttrib eventAttrib){
		EventEntity entry = eventEntityManager.loadOrCreate();
		entry.setEventId(eventAttrib.eventId);
		List<Object> params = entry.getParamsList();
		params.clear();
		params.addAll(eventAttrib.params);
		entry.setParamsList(params);
		entry.setCreateTime(eventAttrib.eventTime);
	}
	
	/**
	 * 更新上传失败事件
	 * */
	public void tryUpLoadFialEvent(){
		String strQuery = "SELECT A.id, A.createTime FROM EventEntity AS A ORDER BY A.createTime DESC";
		List<Object> retObjs = querier.listBySqlLimit(EventEntity.class, Object.class, strQuery, 0, 100000);
		for(Object obj : retObjs){
			Object []objArray = (Object[]) obj;
			Long Id = (Long) objArray[0];
			EventEntity evtEntity = eventEntityManager.load(Id);
			if(null == evtEntity){
				continue;
			}
			int evtId = evtEntity.getEventId();
			EventService service = serviceList.get(evtId);
			if(null == service){
				eventEntityManager.remove(Id);
				continue;
			}
			
			EventAttrib evtAttrib = new EventAttrib(evtId);
			evtAttrib.eventTime = evtEntity.getCreateTime();
			evtAttrib.params.addAll(evtEntity.getParamsList());
			boolean bOK = service.execEvent(evtAttrib);
			if(bOK){
				eventEntityManager.remove(Id);
			}
		}
	}
}
