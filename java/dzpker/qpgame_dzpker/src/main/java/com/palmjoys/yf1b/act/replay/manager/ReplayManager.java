package com.palmjoys.yf1b.act.replay.manager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;
import com.palmjoys.yf1b.act.framework.utils.HttpClientUtils;
import com.palmjoys.yf1b.act.replay.entity.ReplayPlayerEntity;
import com.palmjoys.yf1b.act.replay.entity.ReplayTableEntity;
import com.palmjoys.yf1b.act.replay.model.RecordScoreAttrib;
import com.palmjoys.yf1b.act.replay.model.VideoFrameAttrib;
import com.palmjoys.yf1b.act.replay.model.VideoSubmitAttrib;
import com.palmjoys.yf1b.act.replay.resource.RecordConfig;

@Component
public class ReplayManager {
	@Autowired
	private ReplayPlayerManager replayPlayerManager;
	@Autowired
	private ReplayTableManager replayTableManager;
	@Static
	private Storage<Integer, RecordConfig> recordCfgs;
	
	//录像帧数据保存线程
	private Thread _thread = null;
	//数据帧数据队列
	private BlockingQueue<VideoFrameAttrib> _frameDataQueue = new LinkedBlockingQueue<>();
	//战绩数据访问同步锁
	private Lock _recordDataLock = new ReentrantLock(); 
	
	
	@PostConstruct
	protected void init() {
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
		
	public void lock(){
		this._recordDataLock.lock();
	}
	
	public void unLock(){
		this._recordDataLock.unlock();
	}
	
	/**
	 * 战绩记录开始
	 * tableId 桌子号
	 * corpsId 帮会Id("0"=无帮会)
	 * gamedPlayers 参与的玩家(第一个是房主)
	 * gameNum 当前局数
	 * */
	public void recordStart(int tableId, String corpsId, List<Long> gamedPlayers, int gameNum){
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		ReplayTableEntity replayTableEntity = replayTableManager.loadOrCreate(tableId);
		Map<Integer, RecordScoreAttrib> recordMaps = replayTableEntity.getRecordMap();
		if(gameNum == 1){
			replayTableEntity.setCorpsId(corpsId);
			
			replayTableEntity.setRecordTime(currTime);
			List<Long> tmpGamedPlayers = replayTableEntity.getTablePlayerList();
			tmpGamedPlayers.clear();
			tmpGamedPlayers.addAll(gamedPlayers);
			replayTableEntity.setTablePlayerList(tmpGamedPlayers);
			recordMaps.clear();
			
			for(Long accountId : gamedPlayers){
				ReplayPlayerEntity replayPlayerEntity = replayPlayerManager.loadOrCreate(accountId);
				List<Integer> gamedtablesList = replayPlayerEntity.getGamedtablesList();
				if(gamedtablesList.contains(tableId) == false){
					gamedtablesList.add(tableId);
					replayPlayerEntity.setGamedtablesList(gamedtablesList);
				}
			}
		}
		RecordScoreAttrib scoreAttrib = recordMaps.get(gameNum);
		if(null == scoreAttrib){
			scoreAttrib = new RecordScoreAttrib();
		}
		scoreAttrib.gameNum = gameNum;
		scoreAttrib.recordTime = String.valueOf(currTime);
		scoreAttrib.recordFile = ""+currTime + "_" + tableId + "_" + gameNum;
		for(Long accountId : gamedPlayers){
			scoreAttrib.scores.put(accountId, 0);
		}
		recordMaps.put(gameNum, scoreAttrib);
		replayTableEntity.setRecordMap(recordMaps);
	}
	
	/**
	 * 记录单局成绩
	 * tableId 桌子Id
	 * gameNum 当前局数
	 * scores 分数
	 * */
	public void recordOverOnce(int tableId, int gameNum, Map<Long, Integer> scores){
		ReplayTableEntity replayTableEntity = replayTableManager.load(tableId);
		if(null == replayTableEntity){
			return;
		}
		Map<Integer, RecordScoreAttrib> recordMap = replayTableEntity.getRecordMap();
		RecordScoreAttrib scoreAttrib = recordMap.get(gameNum);
		if(null == scoreAttrib){
			return;
		}
		scoreAttrib.scores.putAll(scores);
		recordMap.put(gameNum, scoreAttrib);
		replayTableEntity.setRecordMap(recordMap);
	}
	
	/**
	 * 记录回放帧数据
	 * tableId 桌子Id
	 * gameNum 游戏局数
	 * frameData 帧数据
	 * */
	public void recordReplayFrame(int tableId, int gameNum, Object frameData){
		long startTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		VideoFrameAttrib frame = new VideoFrameAttrib();
		frame.tableId = tableId;
		frame.gameNum = gameNum;
		frame.frameData = frameData;
		frame.startTime = String.valueOf(startTime);
		try{
			_frameDataQueue.offer(frame);
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
			ReplayTableEntity replayTableEntity = replayTableManager.load(frame.tableId);
			if(null == replayTableEntity){
				return;
			}
			Map<Integer, RecordScoreAttrib> recordMap = replayTableEntity.getRecordMap();
			RecordScoreAttrib scoreAttrib = recordMap.get(frame.gameNum);
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
			//提交到录像服
			String videoSubmitUrl = recordCfgs.get(1, false).getReplayUrl();
			HttpClientUtils.executeByPost(videoSubmitUrl, submitData_str);
		}catch(Exception e){
		}
	}
}
