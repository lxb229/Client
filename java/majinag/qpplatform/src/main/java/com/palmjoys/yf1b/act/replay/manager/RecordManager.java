package com.palmjoys.yf1b.act.replay.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.utils.HttpClientUtils;
import com.palmjoys.yf1b.act.majiang.manager.PlayerGamedRecordManager;
import com.palmjoys.yf1b.act.majiang.model.SeatAttrib;
import com.palmjoys.yf1b.act.majiang.model.TableAttrib;
import com.palmjoys.yf1b.act.majiang.model.TableVo;
import com.palmjoys.yf1b.act.replay.entity.RecordEntity;
import com.palmjoys.yf1b.act.replay.model.RecordDetailedVo;
import com.palmjoys.yf1b.act.replay.model.RecordItemVo;
import com.palmjoys.yf1b.act.replay.model.RecordScoreAttrib;
import com.palmjoys.yf1b.act.replay.model.RecordVo;
import com.palmjoys.yf1b.act.replay.model.VideoFrameAttrib;
import com.palmjoys.yf1b.act.replay.model.VideoSubmitAttrib;
import com.palmjoys.yf1b.act.replay.resource.RecordConfig;

@Component
public class RecordManager {
	@Inject
	private EntityMemcache<Long, RecordEntity> recordCache;
	@Static
	private Storage<Integer, RecordConfig> recordCfgs;
	@Autowired
	private Querier querier;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private PlayerGamedRecordManager playerGamedRecordManager;
	
	//录像帧数据保存线程
	private Thread _thread = null;
	//数据帧数据队列
	private ConcurrentLinkedQueue<VideoFrameAttrib> _frameDataQueue = new ConcurrentLinkedQueue<>();
	//战绩数据访问同步锁
	private Lock _recordDataLock = new ReentrantLock(); 
	// 数据记录Id计数器
	private AtomicLong atomicLong = null;
	//队列最大数
	private int MAX_RECORD_FRAM_NUM = 1000000;
	
	@PostConstruct
	protected void init() {
		long maxId = 0;
		String querySql = "SELECT MAX(A.recordId) FROM RecordEntity AS A";
		List<Object> retObjects = querier.listBySqlLimit(RecordEntity.class, Object.class, querySql, 0, 1);
		for(Object obj : retObjects){
			if(null != obj){
				if(obj instanceof Long){
					Long tmpObj = (Long)obj;
					maxId = tmpObj.longValue();
				}else if(obj instanceof Integer){
					Integer tmpObj = (Integer)obj;
					maxId = tmpObj.intValue();
				}
			}
		}
		if(0 == maxId){
			atomicLong = new AtomicLong(1);
		}else{
			atomicLong = new AtomicLong(maxId+1);
		}
		
		_thread = new Thread(){
			@Override
			public void run() {
				super.run();
				while(true){
					try{
						recordSaveRun();
						Thread.sleep(1);
					}catch(Exception e){
					}
				}
			}
		};
		_thread.setName("游戏录像保存线程");
		_thread.setDaemon(true);
		_thread.start();
	}
	
	public RecordEntity loadOrCreate(Integer tableId){
		Long id = atomicLong.incrementAndGet();
		return recordCache.loadOrCreate(id, new EntityBuilder<Long, RecordEntity>(){
			@Override
			public RecordEntity createInstance(Long pk) {
				return RecordEntity.valueOf(id, tableId);
			}
		});
	}
	
	public RecordEntity load(long recordId){
		return recordCache.load(recordId);
	}
	
	public void remove(long recordId){
		recordCache.remove(recordId);
	}
	
	public void lock(){
		this._recordDataLock.lock();
	}
	
	public void unLock(){
		this._recordDataLock.unlock();
	}
	
	//战绩记录开始
	public void recordStart(TableAttrib table, long currTime){
		this.lock();
		try{
			do{
				RecordEntity recordEntity = this.load(table.recordId);
				if(null == recordEntity){
					break;
				}
				
				List<Long> delRecordIds = new ArrayList<>();
				
				long recordTime = recordEntity.getRecordTime();
				int currGameNum = table.currGameNum;
				if(0 == recordTime){
					//还没有记录过
					recordEntity.setRecordTime(currTime);
					recordEntity.setCorpsId(table.corpsId);
				}
				
				Map<Long, Long> seatPlayerMap = recordEntity.getSeatPlayerMap();
				seatPlayerMap.clear();
				
				long saveTime = recordCfgs.get(1, false).getRecordSaveTime();
				saveTime = saveTime*60*60*1000;
				for(SeatAttrib seat : table.seats){
					seatPlayerMap.put(seat.accountId, seat.accountId);
					List<Long> playerGamedRecordList = playerGamedRecordManager.getPlayerGamedRecordList(seat.accountId);
					if(playerGamedRecordList.contains(table.recordId) == false){
						playerGamedRecordList.add(table.recordId);
					}
					
					for(Long tmpId : playerGamedRecordList){
						RecordEntity tmpRecordEntity = this.load(tmpId);
						if(null == tmpRecordEntity){
							playerGamedRecordList.remove(tmpId);
							delRecordIds.add(tmpId);
							continue;
						}
						
						long endTime = tmpRecordEntity.getRecordTime() + saveTime;
						if(currTime > endTime){
							playerGamedRecordList.remove(tmpId);
							delRecordIds.add(tmpId);
							continue;
						}
					}
					playerGamedRecordManager.setPlayerGamedRecordList(seat.accountId, playerGamedRecordList);
				}
				recordEntity.setSeatPlayerMap(seatPlayerMap);
				
				Map<Integer, RecordScoreAttrib> recordMap = recordEntity.getRecordMap();
				RecordScoreAttrib scoreAttrib = recordMap.get(currGameNum);
				if(scoreAttrib == null){
					scoreAttrib = new RecordScoreAttrib();
				}
				scoreAttrib.gameNum = currGameNum;
				scoreAttrib.recordTime = String.valueOf(currTime);
				scoreAttrib.recordFile = ""+currTime+"_"+table.tableId+"_" + currGameNum;
				scoreAttrib.scoreList.clear();
				recordMap.put(currGameNum, scoreAttrib);
				recordEntity.setRecordMap(recordMap);
				
				for(Long tmpId : delRecordIds){
					this.remove(tmpId);
				}
			}while(false);
		}finally{
			this.unLock();
		}
	}
	
	//记录单局战绩
	public void recordOverOnce(TableAttrib table, long currTime){
		RecordEntity racordEntity = this.load(table.recordId);
		if(null == racordEntity){
			return;
		}
		Map<Integer, RecordScoreAttrib> recordMap = racordEntity.getRecordMap();
		RecordScoreAttrib scoreAttrib = recordMap.get(table.currGameNum);
		if(null == scoreAttrib){
			return;
		}
		scoreAttrib.recordTime = String.valueOf(currTime);
		for(SeatAttrib seat : table.seats){
			scoreAttrib.scoreList.put(seat.accountId, seat.statistAttrib.onceScore);
		}
		recordMap.put(table.currGameNum, scoreAttrib);
		racordEntity.setRecordMap(recordMap);
	}
	
	//记录回放帧数据
	public void recordReplayFrame(TableAttrib table){
		TableVo tableVo = table.logicManager.Table2TableVo(table);
		long startTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		
		VideoFrameAttrib frame = new VideoFrameAttrib();
		frame.frameData = tableVo;
		frame.startTime = String.valueOf(startTime);
		try{
			if(_frameDataQueue.size() < MAX_RECORD_FRAM_NUM){
				_frameDataQueue.offer(frame);
			}
		}catch(Exception e){
		}		
	}
		
	//数据帧保存
	public void recordSaveRun(){
		VideoFrameAttrib frame = null;
		try{
			frame = _frameDataQueue.poll();
			if(null == frame){
				return;
			}
			long recordId = Long.parseLong(frame.frameData.tableBaseVo.recordId);
			RecordEntity racordEntity = this.load(recordId);
			if(null == racordEntity){
				return;
			}
			Map<Integer, RecordScoreAttrib> recordMap = racordEntity.getRecordMap();
			RecordScoreAttrib scoreAttrib = recordMap.get(frame.frameData.tableBaseVo.currGameNum);
			if(null == scoreAttrib){
				return;
			}
			
			String fileName = scoreAttrib.recordFile;
			String frameData = JsonUtils.object2String(frame);
			
			VideoSubmitAttrib submitData = new VideoSubmitAttrib();
			submitData.fileName = fileName;
			submitData.compress = 0;
			submitData.frameData = frameData;
			
			String submitData_str = JsonUtils.object2String(submitData);
			submitData = null;
			if(null == submitData_str || submitData_str.length() == 0){
				return;
			}
			//提交到录像服
			String videoSubmitUrl = recordCfgs.get(1, false).getReplayUrl();
			HttpClientUtils.executeByPost(videoSubmitUrl, submitData_str);
		}catch(Exception e){
		}
	}
	
	/**
	 * 查询战绩
	 * */
	public RecordVo queryRecord(int type, String typeValue){
		RecordVo retVo = new RecordVo();
		List<Long> findIds = new ArrayList<>();
		//先查库再查缓存
		String querySql = "";
		if(1 == type){
			//个人
			long nAccountId = Long.valueOf(typeValue);
			List<Long> playerGamedRecordList = playerGamedRecordManager.getPlayerGamedRecordList(nAccountId);
			findIds.addAll(playerGamedRecordList);
		}else{
			//帮会
			querySql = "SELECT A.recordId FROM RecordEntity AS A WHERE A.corpsId='" + typeValue + "'";
			List<Object> retObjects = querier.listBySqlLimit(RecordEntity.class, Object.class, 
					querySql, 0, 10000);
			for(Object obj : retObjects){
				if(null != obj){
					Long recordId = (Long) obj;
					findIds.add(recordId);
				}
			}
			List<RecordEntity> retEntitys = recordCache.getFinder().find(
					RecordFilterManager.Instance().createFilter(typeValue));
			for(RecordEntity entity : retEntitys){
				if(findIds.contains(entity.getId()) == false){
					findIds.add(entity.getId());
				}
			}
		}		
		
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		long saveTime = recordCfgs.get(1, false).getRecordSaveTime();
		saveTime = saveTime*60*60*1000;
		
		List<Long> delIds = new ArrayList<>();
		this.lock();
		try{
			for(long recordId : findIds){
				RecordEntity recordEntity = this.load(recordId);
				if(null == recordEntity){
					delIds.add(recordId);
					continue;
				}
				long endTime = recordEntity.getRecordTime() + saveTime;
				if(currTime >= endTime){
					delIds.add(recordId);
					continue;
				}
				
				List<Long> seatPlayerList = new ArrayList<>();
				seatPlayerList.addAll(recordEntity.getSeatPlayerMap().values());
				RecordItemVo itemVo = new RecordItemVo();
				itemVo.tableId = recordEntity.getTableId();
				itemVo.recordTime = String.valueOf(recordEntity.getRecordTime());
				itemVo.recordId = String.valueOf(recordId);
				Map<Integer, RecordScoreAttrib> recordMap = recordEntity.getRecordMap();
				Set<Integer> keys = recordMap.keySet();
				
				for(long playerId : seatPlayerList){
					String nick = "";
					int score = 0;
					AccountEntity accountEntity = accountManager.load(playerId);
					if(null != accountEntity){
						nick = accountEntity.getNick();
					}
					for(int key : keys){
						RecordScoreAttrib recordScoreAttrib = recordMap.get(key);
						Integer scorObj = recordScoreAttrib.scoreList.get(playerId);
						if(null != scorObj){
							score += scorObj.intValue();
						}
					}					
					itemVo.addSeatVo(playerId, nick, score);
				}
				retVo.items.add(itemVo);
			}
			
			if(delIds.isEmpty() == false){
				for(Long recordId : delIds){
					RecordEntity recordEntity = this.load(recordId);
					if(null == recordEntity){
						continue;
					}
					Collection<Long> seatPlayers = recordEntity.getSeatPlayerMap().values();
					for(Long playerId : seatPlayers){
						List<Long> playerGamedRecordList = playerGamedRecordManager.getPlayerGamedRecordList(playerId);
						playerGamedRecordList.remove(recordId);
						playerGamedRecordManager.setPlayerGamedRecordList(playerId, playerGamedRecordList);
					}
					
					this.remove(recordId);
				}
			}
		}finally{
			this.unLock();
		}
		findIds = null;
		delIds = null;
		
		return retVo;
	}
	
	public RecordDetailedVo queryDetailed(long recordId){
		RecordDetailedVo retVo = null;
		this.lock();
		try{
			while(true){
				RecordEntity recordEntity = this.load(recordId);
				if(null == recordEntity){
					break;
				}
				retVo = new RecordDetailedVo();
				retVo.tableId = recordEntity.getTableId();
				
				Collection<Long> seatPlayerList = recordEntity.getSeatPlayerMap().values();
				for(Long playerId : seatPlayerList){
					AccountEntity accountEntity = accountManager.load(playerId);
					retVo.nicks.add(accountEntity.getNick());
				}
				
				Map<Integer, RecordScoreAttrib> recordMap = recordEntity.getRecordMap();
				for(int key=1; key<128; key++){
					RecordScoreAttrib scoreAttrib = recordMap.get(key);
					if(null == scoreAttrib){
						break;
					}
					
					Integer scoreObj = null;
					List<Integer> scores = new ArrayList<>();
					for(Long playerId : seatPlayerList){
						scoreObj = scoreAttrib.scoreList.get(playerId);
						scores.add(scoreObj==null ? 0 : scoreObj);
					}					
					retVo.addItem(scoreAttrib.gameNum, scoreAttrib.recordTime,
							scores, scoreAttrib.recordFile);
				}
				break;
			}
		}finally{
			this.unLock();
		}
		
		return retVo;
	}
}
