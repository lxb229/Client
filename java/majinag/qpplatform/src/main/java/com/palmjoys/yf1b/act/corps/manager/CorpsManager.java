package com.palmjoys.yf1b.act.corps.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

import com.palmjoys.yf1b.act.corps.entity.CorpsEntity;
import com.palmjoys.yf1b.act.corps.model.CorpsDefine;
import com.palmjoys.yf1b.act.corps.model.CorpsMemberNotifyVo;
import com.palmjoys.yf1b.act.corps.model.CorpsNotifyVo;
import com.palmjoys.yf1b.act.corps.model.CorpsTableCreateNotifyVo;
import com.palmjoys.yf1b.act.corps.model.CorpsTableDataChanageNotifyVo;
import com.palmjoys.yf1b.act.corps.model.CorpsTablePlayerAttribVo;
import com.palmjoys.yf1b.act.corps.model.CorpsWalletNotifyVo;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.manager.SencePlayerManager;
import com.palmjoys.yf1b.act.framework.utils.MessagePushQueueUtils;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptDefine;
import com.palmjoys.yf1b.act.mail.entity.MailEntity;
import com.palmjoys.yf1b.act.mail.manager.MailManager;
import com.palmjoys.yf1b.act.mail.model.MailAttrib;
import com.palmjoys.yf1b.act.majiang.model.SeatAttrib;
import com.palmjoys.yf1b.act.majiang.model.TableAttrib;

@Component
public class CorpsManager {
	@Inject
	private EntityMemcache<String, CorpsEntity> corpsEntityCache;
	@Autowired
	private Querier querier;
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private SencePlayerManager sencePlayerManager;
	@Autowired
	private HotPromptManager hotPromptManager;
	@Autowired
	private MailManager mailManager;
	
	//帮会桌子变化类型定义
	//增加桌子
	public final static int CORPS_TABLE_CHANAGE_TYPE_ADD = 1;
	//修改桌子
	public final static int CORPS_TABLE_CHANAGE_TYPE_MODFIY = 2;
	//删除桌子
	public final static int CORPS_TABLE_CHANAGE_TYPE_DEL = 3;
	
	//帮会数据修改锁
	private Lock _lockCorps = new ReentrantLock();
	
	public CorpsEntity loadOrCreate(String corpsId, String corpsName, long createPlayer){
		return corpsEntityCache.loadOrCreate(corpsId, new EntityBuilder<String, CorpsEntity>(){
			@Override
			public CorpsEntity createInstance(String pk) {
				return CorpsEntity.valueOf(corpsId, corpsName, createPlayer);
			}
		});
	}
		
	public void removeCorps(String corpsId){
		corpsEntityCache.remove(corpsId);
	}
	
	public void lock(){
		_lockCorps.lock();
	}
	
	public void unLock(){
		_lockCorps.unlock();
	}
	
	public CorpsEntity findOf_CorpsId(String corpsId){
		return corpsEntityCache.load(corpsId);
	}
	
	public CorpsEntity findOf_CorpsName(String corpsName){
		//先查全表,后查缓存
		String querySql = "SELECT A.corpsId FROM CorpsEntity AS A WHERE A.corpsName='" + corpsName + "'";
		List<Object> retObjects = querier.listBySqlLimit(CorpsEntity.class, Object.class, querySql, 0, 1);
		for(Object obj : retObjects){
			String corpsId = (String) obj;
			return this.findOf_CorpsId(corpsId);
		}
		List<CorpsEntity> retEntitys = corpsEntityCache.getFinder().find(
				CorpsFilterManager.Instance().createFilter_Name(corpsName));
		for(CorpsEntity corpsEntity : retEntitys){
			return corpsEntity;
		}
		return null;
	}
	
	public List<String> findOf_CreatePlayer(long createPlayer){
		//先查全表,后查缓存
		List<String> retList = new ArrayList<>();
		String querySql = "SELECT A.corpsId FROM CorpsEntity AS A WHERE A.createPlayer=" + createPlayer + "";
		List<Object> retObjects = querier.listBySqlLimit(CorpsEntity.class, Object.class, querySql, 0, 100);
		for(Object obj : retObjects){
			String corpsId = (String) obj;
			retList.add(corpsId);
		}
		List<CorpsEntity> retEntitys = corpsEntityCache.getFinder().find(
				CorpsFilterManager.Instance().createFilter_CreatePlayer(createPlayer));
		for(CorpsEntity corpsEntity : retEntitys){
			if(retList.contains(corpsEntity.getCorpsId()) == false){
				retList.add(corpsEntity.getCorpsId());
			}
		}		
		return retList;
	}
	
	public List<String> findOf_Hidde(int hidde){
		//先查全表,后查缓存
		List<String> retList = new ArrayList<>();
		String querySql = "SELECT A.corpsId FROM CorpsEntity AS A WHERE A.hidde=" + hidde + "";
		List<Object> retObjects = querier.listBySqlLimit(CorpsEntity.class, Object.class, querySql, 0, 1000000);
		for(Object obj : retObjects){
			String corpsId = (String) obj;
			retList.add(corpsId);
		}
		List<CorpsEntity> retEntitys = corpsEntityCache.getFinder().find(
				CorpsFilterManager.Instance().createFilter_Hidde(hidde));
		for(CorpsEntity corpsEntity : retEntitys){
			if(retList.contains(corpsEntity.getCorpsId()) == false){
				retList.add(corpsEntity.getCorpsId());
			}
		}		
		return retList;
	}
	
	//检查红点
	public int checkHotPromptOf_CreatePlayer(long createPlayer){
		int retN = 0;
		List<String> corpsIds = this.findOf_CreatePlayer(createPlayer);
		for(String corpsId : corpsIds){
			CorpsEntity corpsEntity = this.findOf_CorpsId(corpsId);
			if(null == corpsEntity){
				continue;
			}
			retN += corpsEntity.getJoinList().size();
		}
		
		return retN;
	}
	
	//检查红点帮会Id
	public int checkHotPromptOf_CorpsId(String corpsId){
		CorpsEntity corpsEntity = this.findOf_CorpsId(corpsId);
		if(null == corpsEntity){
			return 0;
		}
		return corpsEntity.getJoinList().size();
	}
	
	//发送帮会桌子创建消息
	public void sendCorpsCreateTableNotify(long accountId, String corpsId, int tableId, String gameName, String password){	
		List<Long> players = sencePlayerManager.getSencePlayers(SencePlayerManager.SENCE_CORPS, corpsId);
		if(players.isEmpty()){
			return;
		}
		CorpsTableCreateNotifyVo notifyVo = new CorpsTableCreateNotifyVo();
		notifyVo.corpsId = corpsId;
		notifyVo.tableId = tableId;
		notifyVo.gameName = gameName;
		if(password.isEmpty() == false){
			notifyVo.password = 1;
		}
		notifyVo.nick = "unkwon";
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null != accountEntity){
			notifyVo.nick = accountEntity.getNick();
		}
		
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(CorpsDefine.CORPS_COMMAND_TABLE_CREATE_NOTIFY, 
				Result.valueOfSuccess(notifyVo));			
		MessagePushQueueUtils.getPushQueue(sessionManager).push(players, pushMsg);
	}
	
	//发送帮会桌子变化消息
	public void sendCorpsTableDataChanageNotify(TableAttrib table, String gameName, int type){
		List<Long> players = sencePlayerManager.getSencePlayers(SencePlayerManager.SENCE_CORPS, table.corpsId);
		if(players.isEmpty()){
			return;
		}
		CorpsTableDataChanageNotifyVo notifyVo = new CorpsTableDataChanageNotifyVo();
		notifyVo.type = type;
		
		notifyVo.table.tableId = table.tableId;
		if(type != CorpsManager.CORPS_TABLE_CHANAGE_TYPE_DEL){
			notifyVo.table.gameName = gameName;
			notifyVo.table.ruleShowDesc = table.ruleShowDesc;
			notifyVo.table.password = 0;
			if(null != table.password && table.password.isEmpty() == false){
				notifyVo.table.password = 1;
			}
			
			for(SeatAttrib seat : table.seats){
				CorpsTablePlayerAttribVo seatPlayer = new CorpsTablePlayerAttribVo();
				if(0!=seat.accountId){
					AccountEntity accountEntity = table.accountManager.load(seat.accountId);
					if(null != accountEntity){
						seatPlayer.accountId = ""+seat.accountId;
						seatPlayer.nick = accountEntity.getNick();
						seatPlayer.headImg = accountEntity.getHeadImg();
						seatPlayer.pingVal = table.accountManager.getClientPing(seat.accountId);
					}
				}
				
				notifyVo.table.seats.add(seatPlayer);
			}
		}
		
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(CorpsDefine.CORPS_COMMAND_TABLE_DATA_CHANAGE_NOTIFY, 
				Result.valueOfSuccess(notifyVo));			
		MessagePushQueueUtils.getPushQueue(sessionManager).push(players, pushMsg);
	}
	
	//通知麻将馆解散
	public void sendCorpsDestoryNotify(Collection<Long> notifyIds, String corpsId, String corpsName){
		CorpsMemberNotifyVo notifyVo = new CorpsMemberNotifyVo();
		notifyVo.corpsId = corpsId;
		notifyVo.content = "您所在的麻将馆["+corpsName+"]已被馆主解散";
		
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(CorpsDefine.CORPS_COMMAND_DESTORY_NOTIFY, 
				Result.valueOfSuccess(notifyVo));
		MessagePushQueueUtils.getPushQueue(sessionManager).push(notifyIds, pushMsg);
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		//发送邮件通知
		for(Long notifyId : notifyIds){
			MailEntity mailEntity = mailManager.loadOrCreate(notifyId);
			mailEntity.lock();
			try{
				MailAttrib mailAttrib = new MailAttrib();
				mailAttrib.sender = 0;
				mailAttrib.title = "麻将馆解散";
				mailAttrib.content = notifyVo.content;
				mailAttrib.recvTime = currTime;
				mailAttrib.read = 0;
				
				mailEntity.addNewMail(mailAttrib, mailManager.getMailMaxCfg());
			}finally{
				mailEntity.unLock();
			}
			//检查推送红点消息
			if(hotPromptManager.updateHotPrompt(notifyId, HotPromptDefine.HOT_KEY_MAIL) > 0){
				hotPromptManager.notifyHotPrompt(notifyId);
			}
		}
	}
	
	//通知麻将馆成员添加
	public void sendCorpsAddNotify(long notifyId, String corpsId, long createPlayer, String corpsName, int type){
		String title = "麻将馆申请";
		String content = ""+corpsName + "麻将馆已通过了您的加入申请";
		if(type == 2){
			//邀请加入
			title = "麻将馆邀请";
			content = "馆主邀请您加入了["+corpsName+"]麻将馆";
		}
		
		CorpsMemberNotifyVo notifyVo = new CorpsMemberNotifyVo();
		notifyVo.corpsId = corpsId;
		notifyVo.createPlayer = String.valueOf(createPlayer);
		notifyVo.content = content;
		
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(CorpsDefine.CORPS_COMMAND_ADDMEMBER_NOTIFY, 
				Result.valueOfSuccess(notifyVo));
		MessagePushQueueUtils.getPushQueue(sessionManager).push2(notifyId, pushMsg);
		
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		//发送邮件通知
		MailEntity mailEntity = mailManager.loadOrCreate(notifyId);
		mailEntity.lock();
		try{
			
			MailAttrib mailAttrib = new MailAttrib();
			mailAttrib.sender = 0;
			mailAttrib.title = title;
			mailAttrib.content = notifyVo.content;
			mailAttrib.recvTime = currTime;
			mailAttrib.read = 0;
			
			mailEntity.addNewMail(mailAttrib, mailManager.getMailMaxCfg());
		}finally{
			mailEntity.unLock();
		}
		//检查推送红点消息
		if(hotPromptManager.updateHotPrompt(notifyId, HotPromptDefine.HOT_KEY_MAIL) > 0){
			hotPromptManager.notifyHotPrompt(notifyId);
		}
	}
	
	//通知麻将馆成员踢出
	public void sendCorpsKickNotify(long notifyId, String corpsId, long createPlayer, String corpsName){
		CorpsMemberNotifyVo notifyVo = new CorpsMemberNotifyVo();
		notifyVo.corpsId = corpsId;
		notifyVo.createPlayer = String.valueOf(createPlayer);
		notifyVo.content = "你已被馆主移出了["+corpsName+"]麻将馆";
		
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(CorpsDefine.CORPS_COMMAND_KICKMEMBER_NOTIFY, 
				Result.valueOfSuccess(notifyVo));
		MessagePushQueueUtils.getPushQueue(sessionManager).push2(notifyId, pushMsg);
		
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		//发送邮件通知
		MailEntity mailEntity = mailManager.loadOrCreate(notifyId);
		mailEntity.lock();
		try{
			MailAttrib mailAttrib = new MailAttrib();
			mailAttrib.sender = 0;
			mailAttrib.title = "麻将馆移除";
			mailAttrib.content = notifyVo.content;
			mailAttrib.recvTime = currTime;
			mailAttrib.read = 0;
			
			mailEntity.addNewMail(mailAttrib, mailManager.getMailMaxCfg());
		}finally{
			mailEntity.unLock();
		}
		//检查推送红点消息
		if(hotPromptManager.updateHotPrompt(notifyId, HotPromptDefine.HOT_KEY_MAIL) > 0){
			hotPromptManager.notifyHotPrompt(notifyId);
		}
	}
	
	//麻将馆转让通知
	public void sendCorpsZhuanRangNotify(long createPlayer, String corpsId, String corpsName){
		CorpsMemberNotifyVo notifyVo = new CorpsMemberNotifyVo();
		notifyVo.corpsId = corpsId;
		notifyVo.createPlayer = String.valueOf(createPlayer);
		notifyVo.content = "麻将馆[" + corpsName + "]转让您为麻将馆馆主";
		
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(CorpsDefine.CORPS_COMMAND_ZHUANRANG_NOTIFY, 
				Result.valueOfSuccess(notifyVo));
		MessagePushQueueUtils.getPushQueue(sessionManager).push2(createPlayer, pushMsg);
		
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		//发送邮件通知
		MailEntity mailEntity = mailManager.loadOrCreate(createPlayer);
		mailEntity.lock();
		try{
			MailAttrib mailAttrib = new MailAttrib();
			mailAttrib.sender = 0;
			mailAttrib.title = "转让麻将馆";
			mailAttrib.content = notifyVo.content;
			mailAttrib.recvTime = currTime;
			mailAttrib.read = 0;
			
			mailEntity.addNewMail(mailAttrib, mailManager.getMailMaxCfg());
		}finally{
			mailEntity.unLock();
		}
		//检查推送红点消息
		if(hotPromptManager.updateHotPrompt(createPlayer, HotPromptDefine.HOT_KEY_MAIL) > 0){
			hotPromptManager.notifyHotPrompt(createPlayer);
		}
	}
	
	//发送帮会修改微信号,公告推送消息
	public void sendCorpsModfiyNotify(String corpsId, String corpsNotice, String wxNO){
		List<Long> notifyIds = sencePlayerManager.getSencePlayers(SencePlayerManager.SENCE_CORPS, corpsId);
		if(notifyIds.isEmpty()){
			return;
		}
		CorpsNotifyVo notifyVo = new CorpsNotifyVo();
		
		notifyVo.corpsId = corpsId;
		notifyVo.corpsNotice = corpsNotice;
		notifyVo.wxNO = wxNO;
				
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(CorpsDefine.CORPS_COMMAND_NOTICE_MODFIY_NOTIFY, 
				Result.valueOfSuccess(notifyVo));
		MessagePushQueueUtils.getPushQueue(sessionManager).push(notifyIds, pushMsg);
	}
	
	//发送帮会钱包变化消息
	public void sendCorpsWalletNotify(String corpsId, long roomCard){
		List<Long> notifyIds = sencePlayerManager.getSencePlayers(SencePlayerManager.SENCE_CORPS, corpsId);
		if(notifyIds.isEmpty()){
			return;
		}
		CorpsWalletNotifyVo notifyVo = new CorpsWalletNotifyVo();
		
		notifyVo.corpsId = corpsId;
		notifyVo.roomCard = String.valueOf(roomCard);
				
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(CorpsDefine.CORPS_COMMAND_CORPS_WALLET_NOTIFY, 
				Result.valueOfSuccess(notifyVo));
		MessagePushQueueUtils.getPushQueue(sessionManager).push(notifyIds, pushMsg);
	}
}
