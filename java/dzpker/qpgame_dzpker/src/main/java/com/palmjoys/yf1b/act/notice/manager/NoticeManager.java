package com.palmjoys.yf1b.act.notice.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.framework.utils.MessagePushQueueUtils;
import com.palmjoys.yf1b.act.notice.entity.NoticeEntity;
import com.palmjoys.yf1b.act.notice.model.NoticeAttrib;
import com.palmjoys.yf1b.act.notice.model.NoticeDefine;

import timer.TimerCenter;
import timer.TimerEvent;
import timer.TimerListener;

@Component
public class NoticeManager implements TimerListener {
	@Inject
	private EntityMemcache<Long, NoticeEntity> noticeEntityCache;
	@Autowired
	private Querier querier;
	@Autowired
	private SessionManager sessionManager;
	// 公告表Id
	private AtomicLong atomicLong;
	// 数据操作锁
	private Lock _lock = new ReentrantLock();
	
	@PostConstruct
	protected void init() {
		Long maxId = querier.unique(NoticeEntity.class, Long.class, NoticeEntity.NQ_NOTICEMAX_UID);
		if (maxId == null) {
			atomicLong = new AtomicLong(1);
		} else {
			atomicLong = new AtomicLong(maxId + 1);
		}
		// 开启秒级定时器,检查推送公告消息
		TimerEvent timerEvent = new TimerEvent(this, 2, 10000);
		TimerCenter.getSecondTimer().add(timerEvent);
	}

	public NoticeEntity loadOrCreate() {
		Long id = atomicLong.incrementAndGet();
		return noticeEntityCache.loadOrCreate(id, new EntityBuilder<Long, NoticeEntity>() {
			@Override
			public NoticeEntity createInstance(Long pk) {
				return NoticeEntity.valueOf(pk);
			}
		});
	}

	public NoticeEntity load(Long Id) {
		return noticeEntityCache.load(Id);
	}

	public void remove(Long Id) {
		noticeEntityCache.remove(Id);
	}

	public void lock() {
		_lock.lock();
	}

	public void unLock() {
		_lock.unlock();
	}

	/**
	 * 获取未过期的所有消息
	 * gmflag 1=gm请求
	 */
	public List<NoticeAttrib> getNoticeList(int gmflag) {
		List<NoticeAttrib> retList = new ArrayList<NoticeAttrib>();
		lock();
		try {
			long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
			String sqlQuery = "SELECT A.noticeId, A.startTime FROM NoticeEntity AS A WHERE " + currTime
					+ " >= A.startTime AND " + currTime + " < A.endTime";
			if(gmflag == 1){
				sqlQuery = "SELECT A.noticeId, A.startTime FROM NoticeEntity AS A WHERE"
						+ " A.endTime > " + currTime;
			}
			List<Object> retObjects = querier.listBySqlLimit(NoticeEntity.class, Object.class, sqlQuery, 0, 30);
			List<Long> Ids = new ArrayList<Long>();
			for (Object obj : retObjects) {
				Object[] objArry = (Object[]) obj;
				Long noticeId = (Long) objArry[0];
				NoticeEntity theNotice = load(noticeId);
				if (null != theNotice) {
					if (theNotice.getStartTime() > currTime) {
						continue;
					}

					if (currTime >= theNotice.getEndTime()) {
						continue;
					}
					NoticeAttrib noticeAttrib = NoticeAttrib.valueOf(theNotice);
					retList.add(noticeAttrib);

					Ids.add(noticeId);
				}
			}
			List<NoticeEntity> entityList = noticeEntityCache.getFinder()
					.find(NoticeFilterManager.Instance().createFilter(gmflag, false, currTime));
			for (NoticeEntity entry : entityList) {
				if (Ids.contains(entry.getNoticeId()) == false) {
					NoticeAttrib noticeAttrib = NoticeAttrib.valueOf(entry);
					retList.add(noticeAttrib);
				}
			}
		} finally {
			unLock();
		}

		return retList;
	}

	@Override
	public void onTimer(TimerEvent e) {
		// 获取未过期的所有公告,推送到客户端
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		String sqlQuery = "SELECT A.noticeId, A.startTime FROM NoticeEntity AS A" + " WHERE " + currTime
				+ " >= A.startTime" + " AND " + currTime + " < A.endTime " + " AND A.nextTime < " + currTime;
		List<Object> retObjects = querier.listBySqlLimit(NoticeEntity.class, Object.class, sqlQuery, 0, 100);
		lock();
		try {
			List<Long> Ids = new ArrayList<Long>();
			for (Object obj : retObjects) {
				Object[] objArry = (Object[]) obj;
				Long noticeId = (Long) objArry[0];
				NoticeEntity theNotice = load(noticeId);
				if (null != theNotice) {
					if (theNotice.getStartTime() > currTime) {
						continue;
					}
					if (currTime > theNotice.getEndTime()) {
						continue;
					}
					if (theNotice.getNextTime() > currTime) {
						continue;
					}

					Ids.add(theNotice.getNoticeId());

					long innerTime = theNotice.getIntervalTime();
					if (innerTime == 0) {
						// 只发送1次的公告
						innerTime = (theNotice.getEndTime() - theNotice.getStartTime()) / 1000 + 10;
					}
					long nextTime = theNotice.getNextTime() + innerTime * 1000;
					theNotice.setNextTime(nextTime);

					NoticeAttrib noticeAttrib = NoticeAttrib.valueOf(theNotice);
					@SuppressWarnings("rawtypes")
					Request pushMsg = Request.valueOf(NoticeDefine.NOTICE_COMMAND_NOTIFY, 
							Result.valueOfSuccess(noticeAttrib));
					@SuppressWarnings("unchecked")
					Collection<Long> allPlayers = (Collection<Long>) sessionManager.getOnlineIdentities();
					MessagePushQueueUtils.getPushQueue(sessionManager).push(allPlayers, pushMsg);
				}
			}

			// 从缓存中查找
			List<NoticeEntity> entityList = noticeEntityCache.getFinder()
					.find(NoticeFilterManager.Instance().createFilter(0, false, currTime));
			for (NoticeEntity theNotice : entityList) {
				if (Ids.contains(theNotice.getNoticeId())) {
					continue;
				}
				if (theNotice.getStartTime() > currTime) {
					continue;
				}
				if (currTime > theNotice.getEndTime()) {
					continue;
				}
				if (theNotice.getNextTime() > currTime) {
					continue;
				}

				long innerTime = theNotice.getIntervalTime();
				if (innerTime == 0) {
					// 只发送1次的公告
					innerTime = (theNotice.getEndTime() - theNotice.getStartTime()) / 1000 + 10;
				}
				long nextTime = theNotice.getNextTime() + innerTime * 1000;
				theNotice.setNextTime(nextTime);

				NoticeAttrib noticeAttrib = NoticeAttrib.valueOf(theNotice);
				@SuppressWarnings("rawtypes")
				Request pushMsg = Request.valueOf(NoticeDefine.NOTICE_COMMAND_NOTIFY, 
						Result.valueOfSuccess(noticeAttrib));
				@SuppressWarnings("unchecked")
				Collection<Long> allPlayers = (Collection<Long>) sessionManager.getOnlineIdentities();
				MessagePushQueueUtils.getPushQueue(sessionManager).push(allPlayers, pushMsg);
			}

			// 删除已过期的公告
			sqlQuery = "SELECT A.noticeId, A.startTime FROM NoticeEntity AS A" + " WHERE A.endTime <" + currTime;
			retObjects = querier.listBySqlLimit(NoticeEntity.class, Object.class, sqlQuery, 0, 10000);
			for (Object obj : retObjects) {
				Object[] objArry = (Object[]) obj;
				Long noticeId = (Long) objArry[0];
				NoticeEntity theNotice = load(noticeId);
				if (null != theNotice) {
					noticeEntityCache.remove(noticeId);
				}
			}
			entityList = noticeEntityCache.getFinder()
					.find(NoticeFilterManager.Instance().createFilter(0, true, currTime));
			for (NoticeEntity theNotice : entityList) {
				noticeEntityCache.remove(theNotice.getNoticeId());
			}
		} finally {
			unLock();
		}
	}
	
	public long findOfGmNoticeId(int noticeGmId){
		long noticeId = 0;
		String sqlQuery = "SELECT A.noticeId FROM NoticeEntity AS A WHERE A.notice_gm_Id=" + noticeGmId;
		List<Object> retObjects = querier.listBySqlLimit(NoticeEntity.class, Object.class, sqlQuery, 0, 1);
		for(Object obj : retObjects){
			if(null != obj){
				noticeId = (long) obj;
			}
		}		
		return noticeId;
	}
}
