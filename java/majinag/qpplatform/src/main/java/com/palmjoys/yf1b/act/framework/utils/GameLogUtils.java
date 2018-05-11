package com.palmjoys.yf1b.act.framework.utils;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.treediagram.nina.core.lang.JsonUtils;

import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;

/**
 * 游戏动作日志工具
 * */
public class GameLogUtils {
	private static GameLogUtils _instance = new GameLogUtils();
	private Queue<LogMsgAttrib> _msgQueue = new LinkedList<LogMsgAttrib>();
	private Lock msgQueueLock = new ReentrantLock();
	private Thread _logRunThread;
	private List<Integer> filterCommand;
	
	
	private GameLogUtils(){
		filterCommand = new ArrayList<Integer>();
		filterCommand.add(AccountDefine.ACCOUNT_HEART);
		filterCommand.add(AccountDefine.ACCOUNT_PING);
		
		_logRunThread = new Thread(){

			@Override
			public void run() {
				super.run();
				
				while(true){
					try{
						runLogWriter();
						Thread.sleep(1);
					}catch(Exception e){
					}
				}
			}
		};
		
		_logRunThread.setDaemon(true);
		_logRunThread.setName("系统日志线程");
		_logRunThread.start();
	}
	
	public static GameLogUtils Instance(){
		return _instance;
	}
	
	public void writeLog(int flag, int commandId, String starNO, String commandName, 
			String commandParam, int roomCard, int diamond){
		LogMsgAttrib msgAttrib = new LogMsgAttrib();
		msgAttrib.commandId = commandId;
		msgAttrib.starNO = starNO;
		msgAttrib.commandName = commandName;
		msgAttrib.commandParam = commandParam;
		msgAttrib.roomCard = roomCard;
		msgAttrib.diamond = diamond;
		msgAttrib.flag = "命令执行前";
		if(flag == 1){
			msgAttrib.flag = "命令执行后";
		}
		
		msgQueueLock.lock();
		try{
			_msgQueue.add(msgAttrib);
		}finally{
			msgQueueLock.unlock();
		}
	}
	
	/**
	 * 是否过虑命令
	 * true=过虑
	 * */
	public boolean filterCommand(int commandId){
		if(filterCommand.contains(commandId)){
			return true;
		}
		return false;
	}
	
	private void runLogWriter(){
		LogMsgAttrib msgAttrib = null;
		msgQueueLock.lock();
		try{
			if(_msgQueue.size() > 0){
				msgAttrib = _msgQueue.poll();
			}
		}finally{
			msgQueueLock.unlock();
		}
		if(msgAttrib == null){
			return;
		}
		
		try{
			long currTime = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String timeStr = sdf.format(new Date(currTime));
			
			String filePath = "GameActLog/"+timeStr;
			File myFolderPath = new File(filePath);
			if(myFolderPath.exists() == false){
				myFolderPath.mkdirs();
			}
			
			String fileFullPath = filePath + "/" + msgAttrib.starNO + ".log";
			File myFile = new File(fileFullPath);
			if(myFile.exists() == false){
				myFile.createNewFile();
			}
			
			String ss = JsonUtils.object2String(msgAttrib);
			FileWriter fileWriter = null; 
			try{
				fileWriter = new FileWriter(myFile, true); 
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String writeStr = sdf.format(new Date(currTime)) + ": " + ss + "\n";
				fileWriter.write(writeStr);
				fileWriter.flush();
				sdf = null;
			}finally{
				if(fileWriter != null){
					fileWriter.close();
					fileWriter = null;
				}
				
				myFile = null;
				myFolderPath = null;
			}
		}catch(Exception e){
			System.out.println("写游戏动作日志文件异常!!!");
		}
		msgAttrib = null;
	}	
		
	public class LogMsgAttrib{
		//执行标识(方法执行前还是后)
		public String flag; 
		//执行的命令Id
		public int commandId;
		//请求的玩家
		public String starNO;
		//执行的动作名称
		public String commandName;
		//传递的命令参数
		public String commandParam;
		//玩家执行此命令时身上房卡
		public long roomCard;
		//玩家执行此命令时钻石
		public long diamond;	
	}
}
