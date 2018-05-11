package com.palmjoys.yf1b.act.majiang.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.cooltime.manger.CheckResetTimeManager;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeConfigType;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeResult;
import com.palmjoys.yf1b.act.corps.entity.CorpsEntity;
import com.palmjoys.yf1b.act.corps.entity.PlayerCorpsEntity;
import com.palmjoys.yf1b.act.corps.manager.CorpsManager;
import com.palmjoys.yf1b.act.corps.manager.PlayerCorpsManager;
import com.palmjoys.yf1b.act.corps.model.CorpsMemberAttrib;
import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;
import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventAttrib_Corps_GameData_Score;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.utils.MessagePushQueueUtils;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptDefine;
import com.palmjoys.yf1b.act.majiang.manager.CardAnalysisManager;
import com.palmjoys.yf1b.act.majiang.manager.GameDataManager;
import com.palmjoys.yf1b.act.majiang.manager.GameLogicManager;
import com.palmjoys.yf1b.act.majiang.manager.MajiangCfgManager;
import com.palmjoys.yf1b.act.majiang.manager.MajiangFaPaiManager;
import com.palmjoys.yf1b.act.majiang.manager.RobotManager;
import com.palmjoys.yf1b.act.majiang.model.notify.seat.Notify_Seat_Vo_BreakCard;
import com.palmjoys.yf1b.act.majiang.model.notify.seat.Notify_Seat_Vo_Destory;
import com.palmjoys.yf1b.act.majiang.model.notify.seat.Notify_Seat_Vo_DingQue;
import com.palmjoys.yf1b.act.majiang.model.notify.seat.Notify_Seat_Vo_Swap;
import com.palmjoys.yf1b.act.majiang.model.notify.table.Notify_Table_GameStateVo;
import com.palmjoys.yf1b.act.majiang.model.notify.table.Notify_Table_GameStateVo_BaoJiao;
import com.palmjoys.yf1b.act.majiang.model.notify.table.Notify_Table_GameStateVo_BreakCard;
import com.palmjoys.yf1b.act.majiang.model.notify.table.Notify_Table_GameStateVo_Destory;
import com.palmjoys.yf1b.act.majiang.model.notify.table.Notify_Table_GameStateVo_DingQue;
import com.palmjoys.yf1b.act.majiang.model.notify.table.Notify_Table_GameStateVo_FaiPai;
import com.palmjoys.yf1b.act.majiang.model.notify.table.Notify_Table_GameStateVo_MoPai;
import com.palmjoys.yf1b.act.majiang.model.notify.table.Notify_Table_GameStateVo_OutCard;
import com.palmjoys.yf1b.act.majiang.model.notify.table.Notify_Table_GameStateVo_OverAll;
import com.palmjoys.yf1b.act.majiang.model.notify.table.Notify_Table_GameStateVo_OverOnce;
import com.palmjoys.yf1b.act.majiang.model.notify.table.Notify_Table_GameStateVo_PiaoPai;
import com.palmjoys.yf1b.act.majiang.model.notify.table.Notify_Table_GameStateVo_Ready;
import com.palmjoys.yf1b.act.majiang.model.notify.table.Notify_Table_GameStateVo_SwapCard;
import com.palmjoys.yf1b.act.majiang.resource.RoomRuleConfig;
import com.palmjoys.yf1b.act.replay.entity.RecordEntity;
import com.palmjoys.yf1b.act.replay.manager.RecordManager;
import com.palmjoys.yf1b.act.task.entity.TaskEntity;
import com.palmjoys.yf1b.act.task.manager.TaskManager;
import com.palmjoys.yf1b.act.task.model.TaskStatisticsAttrib;
import com.palmjoys.yf1b.act.wallet.entity.WalletEntity;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

//桌子属性类
public class TableAttrib {
	//桌子配置条目Id
	public int cfgId;
	//桌子Id
	public int tableId;
	//桌子创建玩家
	public long createPlayer;
	//桌子所属帮会Id("0"=无帮会)
	public String corpsId;
	//当前庄家位置
	public int bankerIndex;
	//座位列表
	public List<SeatAttrib> seats = new ArrayList<SeatAttrib>();
	//桌子上的牌
	public List<CardAttrib> tableCards = new ArrayList<CardAttrib>();
	//游戏状态
	public int gameState;
	//下一游戏状态
	public int nextGameState;
	//游戏等待时间
	public long waitTime;
	//当前表态位置
	public int btIndex;
	//上个表态座位下标
	public int prevBtIndex;
	//桌子是否删除
	public boolean bRemove;
	//桌子规则定义
	public TableRuleAttrib ruleAttrib;
	//规则展示说明
	public String ruleShowDesc;
	//游戏状态是否已执行
	public boolean bActExec;
	//当前游戏局数
	public int currGameNum;
	//胡杠碰吃中断的游戏状态来源
	public int breakStateSource;
	//胡杠碰吃打断处理结果状态(0=胡,1=杠,2=碰,3=吃)
	public int breakState;
	//胡杠碰吃响应座位下标
	public List<Integer> breakSeats;
	//换牌方式(0=[0->3,3->2,2->1,1->0],1=[0->1,1->2,2->3,3->0],2=[0->2,1->3])
	public int swapCardType;
	//桌子最大有效到期时间
	public long maxVaildTime;
	//创建房间消耗的房卡数
	public int createCost;
	//解散桌子中断状态保存(桌子游戏状态保存)
	public int gameStateSave; 
	//解散桌子中断状态保存(动作执行状态保存)
	public boolean bActExecSave;
	//解散桌子中断状态保存(动作执行余下时间保存)
	public long waitTimeSave;
	//解散桌子中断状态保存(当前表态玩家保存)
	public int btIndexSave;
	//解散桌子的申请人
	public long destoryQuestPlayer;
	//是否还有下一局(0=无,1=有)
	public boolean bGameNext;
	//是否发送总结算消息
	public boolean bSendAllOverNotify;
	//桌子录像记录Id
	public long recordId;
	//桌子密码
	public String password;
	//桌子游戏名称
	public String gameName;
	//桌子类型(预留,1=玩家创建桌子,2=玩家匹配桌子,3=系统创建桌子)
	public int tableType;
				
	public SessionManager sessionManager;
	public AccountManager accountManager;
	public GameLogicManager logicManager;
	public MajiangFaPaiManager fapaiManager;
	public GameDataManager gameDataManager;
	public MajiangCfgManager cfgManager;
	public RecordManager recordManager;
	public WalletManager walletManager;
	public CorpsManager corpsManager;
	public EventTriggerManager eventTriggerManager;
	public TaskManager taskManager;
	public HotPromptManager hotPromptManager;
	public PlayerCorpsManager playerCorpsManager;
	public CheckResetTimeManager resetTimeManager;
	public RobotManager robotManager;
	
	public TableAttrib(int tableId, int tableCfgId, List<Integer> ruleIds, String corpsId, long createPlayerId,
			String password,
			SessionManager sessionManager, AccountManager accountManager, GameLogicManager logicManager,
			MajiangFaPaiManager fapaiManager, GameDataManager gameDataManager, MajiangCfgManager cfgManager,
			RecordManager recordManager, EventTriggerManager eventTriggerManager){
		this.cfgId = tableCfgId;
		this.tableId = tableId;
		this.createPlayer = createPlayerId;
		this.corpsId = corpsId;
		this.sessionManager = sessionManager;
		this.accountManager = accountManager;
		this.logicManager = logicManager;
		this.fapaiManager = fapaiManager;
		this.gameDataManager = gameDataManager;
		this.cfgManager = cfgManager;
		this.recordManager = recordManager;
		this.eventTriggerManager = eventTriggerManager;
		
		this.ruleAttrib = new TableRuleAttrib();
		this.gameState = GameDefine.STATE_TABLE_IDLE;
		this.nextGameState = this.gameState;
		this.waitTime = 0;
		this.bRemove = false;
		this.ruleShowDesc = "";
		this.bActExec = false;
		this.currGameNum = 1;
		this.swapCardType = -1;
		this.breakState = GameDefine.ACT_INDEX_DROP;
		this.breakSeats = null;
		this.maxVaildTime = 2*60*60*1000;
		this.maxVaildTime += DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		this.createCost = 0;
		this.destoryQuestPlayer = 0;
		this.bGameNext = true;
		this.password = password;
		
		this.setTableRule(tableCfgId, ruleIds);
		for(int seatIndex=0; seatIndex<this.ruleAttrib.gamePlayerNum; seatIndex++){
			SeatAttrib seat = new SeatAttrib(seatIndex);
			seats.add(seat);
		}
		
		//将创建玩家随机加入到座位中
		SeatAttrib emptySeat = this.getEmptySeat();
		emptySeat.accountId = createPlayerId;
		this.btIndex = emptySeat.seatIndex;
		this.bankerIndex = this.btIndex;
		RecordEntity recordEntity = this.recordManager.loadOrCreate(this.tableId);
		this.recordId = recordEntity.getId();
	}
	//获取空座位
	public SeatAttrib getEmptySeat(){
		for(SeatAttrib seatAttrib : seats){
			if(0 == seatAttrib.accountId){
				return seatAttrib;
			}
		}
		return null;
	}
	//获取玩家座位
	public SeatAttrib getPlayerSeatIndex(long accountId){
		for(SeatAttrib seatAttrib : seats){
			if(accountId == seatAttrib.accountId){
				return seatAttrib;
			}
		}
		return null;
	}
	
	//下一个座位
	public int getNextSeatIndex(int currIndex){
		int playerNum = this.seats.size();
		int findIndex = (currIndex+1)%playerNum;
		
		return findIndex;
	}
	
	//下一个未胡牌的座位
	public int getNextUnHuSeatIndex(int currIndex){
		int playerNum = this.seats.size();
		int findIndex = (currIndex+1)%playerNum;
		while(true){
			SeatAttrib seat = this.seats.get(findIndex);
			if(seat.huPaiType == GameDefine.HUPAI_TYPE_NONE){
				break;
			}
			findIndex = (findIndex+1)%playerNum;
		}
		return findIndex;
	}
	
	public void resetSeatBtState(){
		for(SeatAttrib seat : this.seats){
			seat.btState = GameDefine.ACT_STATE_WAIT;
		}
	}
	
	public void resetBreakCardState(){
		for(SeatAttrib seat : this.seats){
			seat.resetBreakState();
		}
	}
	
	public int getHuPaiIndex(){
		int max = 0;
		for(SeatAttrib seat : this.seats){
			if(seat.huPaiIndex > max){
				max = seat.huPaiIndex;
			}
		}
		return max;
	}
	
	void tableReset(){
		for(SeatAttrib seat : this.seats){
			seat.resetAll();
		}
		//桌子上的牌
		this.tableCards.clear();
		this.bActExec = false;
		this.breakState = GameDefine.ACT_INDEX_DROP;
		this.breakSeats = null;
		this.destoryQuestPlayer = 0;
		this.bGameNext = true;
		this.swapCardType = -1;
	}
		
	public void run(){
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		if(currTime > this.maxVaildTime){
			if(this.gameState >= GameDefine.STATE_TABLE_READY
					&& this.gameState != GameDefine.STATE_TABLE_OVER_ONCE
					&& this.gameState != GameDefine.STATE_TABLE_OVER_ALL){
				this.bActExec = false;
				this.gameState = GameDefine.STATE_TABLE_OVER_ONCE;
				return;
			}else if(this.gameState < GameDefine.STATE_TABLE_READY){
				this.bActExec = false;
				this.gameState = GameDefine.STATE_TABLE_OVER_ALL;
				return;
			}
		}
		switch(this.gameState){
		case GameDefine.STATE_TABLE_IDLE:
			run_idle(currTime);
			break;
		case GameDefine.STATE_TABLE_READY:
			run_ready(currTime);
			break;
		case GameDefine.STATE_TABLE_FAPAI:
			run_fapai(currTime);
			break;
		case GameDefine.STATE_TABLE_SWAPCARD:
			run_swappai(currTime);
			break;
		case GameDefine.STATE_TABLE_DINGQUE:
			run_dingque(currTime);
			break;
		case GameDefine.STATE_TABLE_MOPAI:
			run_mopai(currTime);
			break;
		case GameDefine.STATE_TABLE_OUTCARD:
			run_outcard(currTime);
			break;
		case GameDefine.STATE_TABLE_BREAKCARD:
			run_breakcard(currTime);
			break;
		case GameDefine.STATE_TABLE_OVER_ONCE:
			run_over_once(currTime);
			break;
		case GameDefine.STATE_TABLE_OVER_ALL:
			run_over_all(currTime);
			break;
		case GameDefine.STATE_TABLE_DESTORY:
			run_destory(currTime);
			break;
		case GameDefine.STATE_TABLE_BAOJIAO:
			run_baojiao(currTime);
			break;
		case GameDefine.STATE_TABLE_PIAOPAI:
			run_piaopai(currTime);
			break;
		case GameDefine.STATE_TABLE_WAIT_EX:
			run_wait_ex(currTime);
			break;
		}
	}
	
	private void run_idle(long currTime){
		boolean bDestory = false;
		if(this.corpsId.equalsIgnoreCase("0") == false){
			CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(this.corpsId);
			if(null == corpsEntity || corpsEntity.getCorpsState() < 0){
				//如果是帮会桌子,帮会被冻结了,强制结束桌子
				if(this.currGameNum > 1){
					this.bSendAllOverNotify = true;
					this.bActExec = false;
					this.gameState = GameDefine.STATE_TABLE_OVER_ALL;
					return;
				}else{
					bDestory = true;
				}
			}
		}
		
		if(this.currGameNum == 1){
			for(SeatAttrib seatAttrib : seats){
				if(seatAttrib.accountId <= 0){
					continue;
				}
				//桌子从未开始过游戏,掉线时间检查防止挂机房间
				GameDataAttrib gameDataAttrib = gameDataManager.getGameData(seatAttrib.accountId);
				if(gameDataAttrib.onLine <= 0
						&& gameDataAttrib.offlineTime > 0
						&& currTime >= gameDataAttrib.offlineTime){
					//从座位上踢出玩家
					if(seatAttrib.accountId == this.createPlayer){
						//是房主,解散房间
						bDestory = true;
						break;
					}else{
						//是玩家
						seatAttrib.resetAll();
						seatAttrib.accountId = 0;
						this.sendSeatNotify(seatAttrib.seatIndex);
						gameDataAttrib.tableId = 0;
					}
				}
			}
			if(bDestory){
				this.bRemove = true;
				List<Long> notifyIds = new ArrayList<>();
				for(SeatAttrib theSeat : this.seats){
					if(0 == theSeat.accountId){
						continue;
					}
					GameDataAttrib gameData = gameDataManager.getGameData(theSeat.accountId);
					gameData.tableId = 0;
					notifyIds.add(theSeat.accountId);
					theSeat.accountId = 0;
				}
				//桌子未开始游戏返回房卡
				if(this.corpsId.equalsIgnoreCase("0") == false){
					//是帮会房间
					corpsManager.lock();
					try{
						CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(this.corpsId);
						if(null != corpsEntity){
							long roomCard = corpsEntity.getRoomCard() + this.createCost;
							corpsEntity.setRoomCard(roomCard);
						}
					}finally{
						corpsManager.unLock();
					}
				}				
				//推送房间解散消息
				this.sendTableDestoryNotify(notifyIds);
				return;
			}
		}
		
		for(SeatAttrib seatAttrib : this.seats){
			if(seatAttrib.accountId <= 0){
				return;
			}
		}
		
		this.bActExec = false;
		this.gameState = GameDefine.STATE_TABLE_READY;
		this.waitTime = currTime + GameDefine.TIME_TABLE_READY;
		
		this.btIndex = this.bankerIndex;
		this.prevBtIndex = this.btIndex;
		
		this.sendGameStateNotify();
	}
	
	private void run_ready(long currTime){
		//准备状态,所有人都点击了准备,等待指定时间后发牌
		for(SeatAttrib seatAttrib : seats){
			if(seatAttrib.btState != GameDefine.ACT_STATE_BT){
				return;
			}
		}
		if(this.bActExec == false){
			recordManager.recordStart(this, currTime);
			this.bActExec = true;
			this.waitTime = currTime + GameDefine.TIME_TABLE_READY;
			this.sendGameStateNotify();
			logicManager.log("桌子进入准备状态");
		}
		if(currTime < this.waitTime){
			return;
		}
		
		tableReset();
		this.bActExec = false;
		if(this.ruleAttrib.maxPiaoNum > 0){
			this.gameState = GameDefine.STATE_TABLE_PIAOPAI;
		}else{
			this.gameState = GameDefine.STATE_TABLE_FAPAI;
		}
	}
	
	private void run_fapai(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			this.waitTime = currTime + GameDefine.TIME_TABLE_FAPAI;
			logicManager.log("桌子进入发牌状态");
			//调用发牌接口发牌
			fapaiManager.fapai(this);
			
			//推送桌子状态变化消息
			this.sendGameStateNotify();
		}
		if(currTime < this.waitTime){
			return;
		}
		
		this.bActExec = false;
		if(this.ruleAttrib.bBaoJia){
			//检查报叫
			for(SeatAttrib seat : this.seats){
				List<CardAttrib> tingCards = GameDefine.isTingPai(this, seat.handCards, seat);
				if(tingCards != null && tingCards.isEmpty() == false){
					//可以报叫
					seat.baoJiaoState = 0;
				}
			}
			this.gameState = GameDefine.STATE_TABLE_BAOJIAO;
		}else{
			if(this.ruleAttrib.bSwapCard){
				this.gameState = GameDefine.STATE_TABLE_SWAPCARD;
			}else{
				if(this.ruleAttrib.bDingQue){
					this.gameState = GameDefine.STATE_TABLE_DINGQUE;
				}else{
					this.gameState = GameDefine.STATE_TABLE_MOPAI;
				}
			}
		}
	}
	
	private void run_swappai(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			this.waitTime = currTime + GameDefine.TIME_TABLE_SWAPCARD;
			this.resetSeatBtState();
			//推送桌子状态变化消息
			this.sendGameStateNotify();
			logicManager.log("桌子进入换牌状态");
		}
		
		//必须所有座位都已上报换牌数据
		boolean bNeedWait = false;
		for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
			SeatAttrib seat = this.seats.get(seatIndex);
			//检查托管
			this.robotManager.ai_trusteeship_swapcard(this, seat, currTime);
						
			if(GameDefine.ACT_STATE_BT != seat.btState){
				bNeedWait = true;
			}
		}
		if(bNeedWait){
			return;
		}				
		
		String sLog = "";
		int randN = 2;
		switch(this.ruleAttrib.gamePlayerNum){
		case 2:
			randN = 2;
			break;
		case 3:
			randN = 2;
			break;
		case 4:
			randN = 3;
			break;
		}
		
		this.swapCardType = (int) ((Math.random()*10000)%randN);
		if(this.swapCardType == 0){
			//顺时针换牌
			for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
				SeatAttrib seat = this.seats.get(seatIndex);
				int prevIndex = seatIndex-1;
				if(prevIndex < 0){
					prevIndex = this.seats.size()-1;
				}
				SeatAttrib prevSeat = this.seats.get(prevIndex);
				
				seat.swapCards.clear();
				seat.swapCards.addAll(prevSeat.swapBtCards);
				
			}
		}else if(this.swapCardType == 1){
			//逆时针换牌
			for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
				SeatAttrib seat = this.seats.get(seatIndex);
				int nextIndex = (seatIndex+1)%this.seats.size();
				SeatAttrib nextSeat = this.seats.get(nextIndex);
				
				seat.swapCards.clear();
				seat.swapCards.addAll(nextSeat.swapBtCards);
			}
		}else if(this.swapCardType == 2){
			//对面换牌,一定是4个玩家
			SeatAttrib seat1 = this.seats.get(0);
			SeatAttrib seat2 = this.seats.get(1);
			SeatAttrib seat3 = this.seats.get(2);
			SeatAttrib seat4 = this.seats.get(3);
			
			seat1.swapCards.clear();
			seat1.swapCards.addAll(seat3.swapBtCards);
			
			seat3.swapCards.clear();
			seat3.swapCards.addAll(seat1.swapBtCards);
			
			seat2.swapCards.clear();
			seat2.swapCards.addAll(seat4.swapBtCards);
			
			seat4.swapCards.clear();
			seat4.swapCards.addAll(seat2.swapBtCards);
		}
		
		int playerNum = seats.size();
		for(int i=0; i<playerNum; i++){
			SeatAttrib seatMe = seats.get(i);
			sLog += ",座位[" + seatMe.seatIndex + "]换到手的牌=";
			sLog += GameDefine.cards2String(seatMe.swapCards);
		}
		logicManager.log(sLog);
		for(SeatAttrib seatAttrib : seats){
			seatAttrib.handCards.addAll(seatAttrib.swapCards);
			GameDefine.sortCard(seatAttrib.handCards);
			seatAttrib.swapBtCards.clear();
		}
		
		this.bActExec = false;
		//通知玩家换牌数据
		this.sendSwapCardNotify();
		this.gameState = GameDefine.STATE_TABLE_WAIT_EX;
		this.waitTime = (long) (currTime + GameDefine.TIME_TABLE_WAIT_EX + 1.2*1000);
		if(this.ruleAttrib.bDingQue){
			this.nextGameState = GameDefine.STATE_TABLE_DINGQUE;
		}else{
			this.nextGameState = GameDefine.STATE_TABLE_MOPAI;
		}
	}
	
	private void run_dingque(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			this.waitTime = currTime + GameDefine.TIME_TABLE_DINGQUE;
			this.resetSeatBtState();
			//推送桌子状态变化消息
			this.sendGameStateNotify();
			logicManager.log("桌子进入定缺状态");
		}
		String sLog = "";
		//必须所有座位都已上报定缺数据
		boolean bNeedWait = false;
		for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
			SeatAttrib seat = this.seats.get(seatIndex);
			//检查托管
			this.robotManager.ai_trusteeship_dinque(this, seat, currTime);
						
			if(GameDefine.ACT_STATE_BT != seat.btState){
				bNeedWait = true;
			}else{
				sLog += ",座位="+seat.seatIndex+"缺=["+seat.unSuit+"]";
			}
		}
		if(bNeedWait){
			return;
		}		
		logicManager.log("桌子定缺"+sLog);
		
		
		this.bActExec = false;
		this.gameState = GameDefine.STATE_TABLE_MOPAI;
	}
	
	private void run_mopai(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			this.waitTime = currTime + GameDefine.TIME_TABLE_ZHUAPAI;
			this.resetSeatBtState();
			
			//检查是否还有牌可摸,没有了就结算
			if(this.tableCards.isEmpty()){
				this.bActExec = false;
				this.gameState = GameDefine.STATE_TABLE_WAIT_EX;
				this.waitTime = currTime + GameDefine.TIME_TABLE_WAIT_EX;
				this.nextGameState = GameDefine.STATE_TABLE_OVER_ONCE;
				return;
			}
			
			int unHu = 0;
			for(SeatAttrib seat : this.seats){
				if(seat.huPaiType == GameDefine.HUPAI_TYPE_NONE){
					unHu = unHu + 1;
				}
			}
			
			if(this.ruleAttrib.bXueZhaoDaoDi || this.ruleAttrib.bXueLiuChengHe){
				//是血战到底或血流成河
				if(unHu == 1){
					//只有一家没有胡牌结算
					this.bActExec = false;
					this.gameState = GameDefine.STATE_TABLE_WAIT_EX;
					this.waitTime = currTime + GameDefine.TIME_TABLE_WAIT_EX;
					this.nextGameState = GameDefine.STATE_TABLE_OVER_ONCE;
					return;
				}
			}else{
				//是推到胡
				if(this.seats.size()-unHu >= 1){
					//只要有人胡牌就结算
					this.bActExec = false;
					this.gameState = GameDefine.STATE_TABLE_WAIT_EX;
					this.waitTime = currTime + GameDefine.TIME_TABLE_WAIT_EX;
					this.nextGameState = GameDefine.STATE_TABLE_OVER_ONCE;
					return;
				}
			}
						
			//取出一张牌,放到座位摸牌中
			SeatAttrib seat = this.seats.get(this.btIndex);
			seat.moPaiCard = this.tableCards.get(0);
			this.tableCards.remove(0);
			
			seat.moPaiHandNum++;
			
			logicManager.log("桌子摸牌,座位="+seat.seatIndex + "摸牌="+ seat.moPaiCard.toString());
			logicManager.log("座位="+seat.seatIndex+"手牌="+GameDefine.cards2String(seat.handCards));
			//推送桌子状态变化消息
			this.sendGameStateNotify();
		}
		if(currTime < this.waitTime){
			return;
		}
		
		this.bActExec = false;
		//分析牌是否有胡杠碰吃,没有则出牌状态
		this.resetBreakCardState();
		SeatAttrib seat = this.seats.get(this.btIndex);
		seat.breakCard = seat.moPaiCard;
		int ret = GameDefine.AnalysisBreakCard(this, this.btIndex, this.btIndex, GameDefine.STATE_TABLE_MOPAI);
		if(ret > 0){
			this.gameState = GameDefine.STATE_TABLE_BREAKCARD;
			this.breakStateSource = GameDefine.STATE_TABLE_MOPAI;
			logicManager.log("桌子摸牌,座位="+this.btIndex + "有中断表态,座位=" + seat.seatIndex);
		}else{
			//摸牌的人没有表态动作,出牌
			this.gameState = GameDefine.STATE_TABLE_OUTCARD;
		}
	}
	
	private void run_outcard(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			this.waitTime = currTime + GameDefine.TIME_TABLE_OUTCARD;
			this.resetSeatBtState();
			
			logicManager.log("桌子轮到出牌,座位="+this.btIndex);
			//推送桌子状态变化消息
			this.sendGameStateNotify();
		}
		//必须此座位上报了出牌数据
		SeatAttrib btSeat = seats.get(this.btIndex);
		if(this.ruleAttrib.bTangPai && btSeat.tangCardState > 0
				|| this.ruleAttrib.bBaoJia && btSeat.baoJiaoState > 0){
			//躺牌或报叫的座位,系统代出牌
			GameDataAttrib gameDataAttrib = this.gameDataManager.getGameData(btSeat.accountId);
			if(0 == gameDataAttrib.autoBtTime){
				gameDataAttrib.autoBtTime = currTime + 800;
			}
			if(currTime < gameDataAttrib.autoBtTime){
				return;
			}
			gameDataAttrib.autoBtTime = 0;
			
			CardAttrib findCard = null;
			if(btSeat.tangOutCard > 0){
				findCard = GameDefine.findOnceByCardId(btSeat.handCards, btSeat.tangOutCard);
				if(null != findCard){
					btSeat.tangOutCard = 0;					
				}
			}else{
				//打的是摸到的牌
				findCard = btSeat.moPaiCard;
			}
			
			if(null != findCard){
				if(null != btSeat.moPaiCard){
					btSeat.handCards.add(btSeat.moPaiCard);
				}
				btSeat.moPaiCard = null;
				
				btSeat.outUnUseCards.add(findCard);
				btSeat.outCard = findCard;
				GameDefine.removeOnceByCardId(btSeat.handCards, findCard.cardId);
				btSeat.btState = GameDefine.ACT_STATE_BT;
				btSeat.outHandNum++;
			}else{
				//未找到需要打的牌,错误,由玩家手动表态
			}			
		}else{
			//检查托管AI
			robotManager.ai_trusteeship_outcard(this, btSeat, currTime);
		}
		
		if(GameDefine.ACT_STATE_WAIT == btSeat.btState){
			return;
		}
		//玩家已出牌解除过手胡限制
		btSeat.guoShouHuCardList.clear();
		logicManager.log("桌子轮到出牌,座位="+btSeat.seatIndex+"出牌="+btSeat.outCard.toString());
		logicManager.log("座位="+btSeat.seatIndex+"手牌="+GameDefine.cards2String(btSeat.handCards));
		//通知所有人出牌数据
		this.prevBtIndex = this.btIndex;
		this.sendOutCardNotify(btSeat);
		
		//分析牌是否有胡杠碰吃
		//不然下一家摸牌,如果没有牌可摸,则只检查是否有胡碰[可以碰下叫]吃[可以吃下叫]
		boolean bHaveBreakState = false;
		boolean bClearGangType = true;
		this.resetBreakCardState();
		btSeat.breakCard = btSeat.outCard;
		String breakSeatIndex = "";
		
		for(SeatAttrib seat : this.seats){
			if(seat.seatIndex == this.btIndex){
				//是出牌的人跳过
				continue;
			}
			if(seat.huPaiType != GameDefine.HUPAI_TYPE_NONE){
				//是已经胡了牌的玩家,不是血流成河,跳过
				continue;
			}
			
			GameDefine.AnalysisBreakCard(this, seat.seatIndex, this.btIndex, GameDefine.STATE_TABLE_OUTCARD);
			if(seat.haveBreakState()){
				bHaveBreakState = true;
				seat.breakCard = btSeat.outCard;
				breakSeatIndex += "" + seat.seatIndex;
			}
			if(seat.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
				bClearGangType = false;
			}
		}
		if(bClearGangType){
			//打出的牌没人胡,清空杠牌类型
			btSeat.gangType = GameDefine.GANG_TYPE_NONE;
			btSeat.gangSeatIndex = 0;
		}
		if(bHaveBreakState){
			this.nextGameState = GameDefine.STATE_TABLE_BREAKCARD;
			this.breakStateSource = GameDefine.STATE_TABLE_OUTCARD;
			logicManager.log("桌子轮到出牌,座位="+this.btIndex+"有中断表态,座位="+breakSeatIndex);
		}else{
			//没有胡杠碰,下一个玩家摸牌
			this.btIndex = this.getNextUnHuSeatIndex(this.btIndex);
			this.nextGameState = GameDefine.STATE_TABLE_MOPAI;
		}
		this.bActExec = false;
		this.gameState = GameDefine.STATE_TABLE_WAIT_EX;
		this.waitTime = currTime + GameDefine.TIME_TABLE_WAIT_EX;
	}
	
	private void run_breakcard(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			this.waitTime = currTime + GameDefine.TIME_TABLE_BREAKCARD;
			this.resetSeatBtState();
			for(SeatAttrib seatAttrib : seats){
				if(seatAttrib.haveBreakState() == false){
					//没有中断表态状态的座位都设置为表态放弃状态
					seatAttrib.breakBtState = GameDefine.ACT_INDEX_DROP;
					seatAttrib.btState = GameDefine.ACT_STATE_DROP;
				}
			}
			this.breakState = GameDefine.ACT_INDEX_DROP;
			this.breakSeats = null;		
			//推送桌子状态变化消息
			this.sendGameStateNotify();
		}
		
		boolean bChkAutoHu1 = false;
		boolean bChkAutoHu2 = false;
		if(this.ruleAttrib.bTangPai){
			//躺牌的自动胡
			bChkAutoHu1 = true;
		}
		
		if(this.cfgId == 8){
			//南充麻将小于8张牌有胡必须胡,策划20180508由12张改为8张
			if(this.tableCards.size() < 8){
				bChkAutoHu2 = true;
			}
		}
		
		SeatAttrib btSeat = this.seats.get(this.btIndex);
		for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
			SeatAttrib seatAttrib = this.seats.get(seatIndex);
			if(seatAttrib.breakCardState[GameDefine.ACT_INDEX_HU] > 0){
				//可以胡牌
				if(bChkAutoHu1 || bChkAutoHu2){
					if((seatAttrib.tangCardState > 0 || bChkAutoHu2) && seatAttrib.btState != GameDefine.ACT_STATE_BT){
						//躺牌,可以胡的,由系统表态自动胡
						GameDataAttrib gameDataAttrib = this.gameDataManager.getGameData(seatAttrib.accountId);
						if(0 == gameDataAttrib.autoBtTime){
							gameDataAttrib.autoBtTime = currTime + 800;
						}
						if(currTime > gameDataAttrib.autoBtTime){
							seatAttrib.breakBtState = GameDefine.ACT_INDEX_HU;
							seatAttrib.btState = GameDefine.ACT_STATE_BT;
							gameDataAttrib.autoBtTime = 0;
							seatAttrib.breakCard = btSeat.breakCard;
						}
					}else{
						//检查托管AI
						robotManager.ai_trusteeship_breakcard(this, seatAttrib, currTime);
					}
				}else{
					//检查托管AI
					robotManager.ai_trusteeship_breakcard(this, seatAttrib, currTime);
				}
			}else{
				//检查托管AI
				robotManager.ai_trusteeship_breakcard(this, seatAttrib, currTime);
			}
		}		
		
		TableBreakStateManager.run_breakcard(currTime, this);
	}
		
	private void run_over_once(long currTime){
		TableOnceOverStateManager.run_over_once(currTime, this);
	}
	
	private void run_over_all(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			this.logicManager.log("桌子进入总结算状态");
			
			TableVo overVo = this.logicManager.Table2TableVo(this);
			this.bRemove = true;
			for(SeatAttrib seat : this.seats){
				GameDataAttrib dataAttrib = gameDataManager.getGameData(seat.accountId);
				dataAttrib.tableId = 0;
				dataAttrib.allOverVo = overVo;
			}
			if(this.bSendAllOverNotify){
				this.sendGameStateNotify();
			}
			
			//扣除房卡消耗
			if(this.currGameNum > 1){
				long payAccountId = 0;
				int payRoomCard = 0;
				
				if(this.ruleAttrib.roomCardPayType == 0){
					//帮会支付
					payRoomCard = this.createCost;
					corpsManager.lock();
					try{
						CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(this.corpsId);
						if(null != corpsEntity){
							payAccountId = corpsEntity.getCreatePlayer();
						}
					}finally{
						corpsManager.unLock();
					}					
				}else if(this.ruleAttrib.roomCardPayType == 1){
					//创建人付房卡
					payAccountId = this.createPlayer;
				}else if(this.ruleAttrib.roomCardPayType == 2){
					//随机付房卡
					int seatIndex = (int) (Math.random()*1000%this.seats.size());
					payAccountId = this.seats.get(seatIndex).accountId;
				}else if(this.ruleAttrib.roomCardPayType == 3){
					//赢家付房卡
					int maxScore = 0;
					for(SettlementAllVo settlementAllVo : overVo.settlementAll){
						if(settlementAllVo.score > maxScore){
							maxScore = settlementAllVo.score;
						}
					}
					List<Long> payPlayers = new ArrayList<>();
					for(SettlementAllVo settlementAllVo : overVo.settlementAll){
						if(settlementAllVo.score == maxScore){
							payPlayers.add(Long.valueOf(settlementAllVo.accountId));
						}
					}
					//多个赢家随机一个支付
					int playerIndex = (int) (Math.random()*1000%payPlayers.size());
					payAccountId = payPlayers.get(playerIndex);
				}else if(this.ruleAttrib.roomCardPayType == 4){
					//输家付房卡
					int minScore = 100000;
					for(SettlementAllVo settlementAllVo : overVo.settlementAll){
						if(settlementAllVo.score < minScore){
							minScore = settlementAllVo.score;
						}
					}
					List<Long> payPlayers = new ArrayList<>();
					for(SettlementAllVo settlementAllVo : overVo.settlementAll){
						if(settlementAllVo.score == minScore){
							payPlayers.add(Long.valueOf(settlementAllVo.accountId));
						}
					}
					//多个输家随机一个支付
					int playerIndex = (int) (Math.random()*1000%payPlayers.size());
					payAccountId = payPlayers.get(playerIndex);
				}
				
				if(payAccountId > 0 && this.ruleAttrib.roomCardPayType != 0){
					//1=创建者付,2=随机付,3=赢家付,4=输家付
					int roomCard = 0;
					int replaceCard = 0;					
					WalletEntity walletEntity = walletManager.loadOrCreate(payAccountId);
					walletEntity.lock();
					try{
						roomCard = walletEntity.getRoomCard();
						replaceCard = walletEntity.getReplaceCard();
						if(replaceCard >= this.createCost){
							//可以体验卡付
							walletEntity.setReplaceCard(replaceCard-this.createCost);
						}else if(roomCard >= this.createCost){
							//用真实房卡付
							payRoomCard = this.createCost;
							walletEntity.setRoomCard(roomCard-this.createCost);
						}else{
							//混合付
							payRoomCard = this.createCost - replaceCard;
							walletEntity.setReplaceCard(0);
							walletEntity.setRoomCard(roomCard-payRoomCard);
						}
						//通知客户端个人房卡数据变化
						walletManager.sendWalletNotify(payAccountId);
					}finally{
						walletEntity.unLock();
					}
				}
				if(payRoomCard > 0 && payRoomCard==this.createCost){
					//消耗的是充值房卡,且数量是创建桌子所需数量
					//上报游戏数据事件
					String createPlayerStarNO = "unkown";
					AccountEntity accountEntity = accountManager.load(this.createPlayer);
					if(null != accountEntity){
						createPlayerStarNO = accountEntity.getStarNO();
					}
					
					EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_GAME_DATA);
					eventAttrib.addEventParam(createPlayerStarNO);
					eventAttrib.addEventParam(String.valueOf(currTime));
					
					String payPlayerStarNO = "unkown";
					if(payAccountId > 0){
						accountEntity = accountManager.load(payAccountId);
						if(null != accountEntity){
							payPlayerStarNO = accountEntity.getStarNO();
						}
					}
					eventAttrib.addEventParam(payPlayerStarNO);
					eventAttrib.addEventParam(String.valueOf(payRoomCard));
					List<EventAttrib_Corps_GameData_Score> corpsGameDataScoreList = new ArrayList<>();										
					List<String> playerStarNOList = new ArrayList<>();
					int maxScore = 0;
					
					if(null != overVo){
						for(SettlementAllVo settlementAllVo : overVo.settlementAll){
							String playerStarNO = "unkown";
							accountEntity = accountManager.load(Long.valueOf(settlementAllVo.accountId));
							if(null != accountEntity){
								playerStarNO = accountEntity.getStarNO();
							}
							playerStarNOList.add(playerStarNO);
							
							if(settlementAllVo.score > maxScore){
								maxScore = settlementAllVo.score;
							}
							EventAttrib_Corps_GameData_Score corpsGameDataScore = new EventAttrib_Corps_GameData_Score();
							corpsGameDataScore.start_no = playerStarNO;
							corpsGameDataScore.integral = String.valueOf(settlementAllVo.score);
							
							corpsGameDataScoreList.add(corpsGameDataScore);
						}
					}
					
					eventAttrib.addEventParam(playerStarNOList);
					eventTriggerManager.triggerEvent(eventAttrib);
					
					if(this.corpsId.equalsIgnoreCase("0") == false){
						//是帮会桌子上报帮会桌子游戏数据事件
						eventAttrib = new EventAttrib(EventDefine.EVENT_CORPS_GAME_DATA);
						eventAttrib.addEventParam(String.valueOf(this.corpsId));
						eventAttrib.addEventParam(String.valueOf(this.cfgId));
						eventAttrib.addEventParam(String.valueOf(payRoomCard));
						eventAttrib.addEventParam(String.valueOf(overVo.settlementAll.size()));
						eventAttrib.addEventParam(corpsGameDataScoreList);
						eventAttrib.addEventParam(String.valueOf(eventAttrib.eventTime));
						eventTriggerManager.triggerEvent(eventAttrib);
					}
					
					if(null != overVo){
						if(this.corpsId.equalsIgnoreCase("0") == false){
							corpsManager.lock();
							try{
								//检查排行榜重置数据
								CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(this.corpsId);
								if(null != corpsEntity){
									long corpsCreatePlayer = corpsEntity.getCreatePlayer();
									CoolTimeResult coolTimeResult = resetTimeManager.checkPlayerResetTime(corpsCreatePlayer, 
											CoolTimeConfigType.PLAYERTIME_RESET_RANK, true);
									if(coolTimeResult.bReset){
										Set<Long> memberList = corpsEntity.getMemberList().keySet();
										for(Long memberId : memberList){
											PlayerCorpsEntity playerCorpsEntity = playerCorpsManager.loadOrCreate(memberId);
											Map<String, CorpsMemberAttrib> corpsDataList = playerCorpsEntity.getCorpsDataList();
											CorpsMemberAttrib corpsMemberAttrib = corpsDataList.get(this.corpsId);
											if(null != corpsMemberAttrib){
												corpsMemberAttrib.reset();
												corpsDataList.put(this.corpsId, corpsMemberAttrib);
												playerCorpsEntity.setCorpsDataList(corpsDataList);
											}
										}
									}
								}
							}finally{
								corpsManager.unLock();
							}
						}
						
						for(SettlementAllVo settlementAllVo : overVo.settlementAll){
							//记录统计数据
							boolean bMaxWin = false;
							long nAccountId = Long.valueOf(settlementAllVo.accountId);
							TaskEntity taskEntity = taskManager.load(nAccountId);
							if(null != taskEntity){
								TaskStatisticsAttrib taskStatistics = taskEntity.getTaskStatistics();
								CoolTimeResult coolTimeResult = resetTimeManager.checkPlayerResetTime(nAccountId, 
										CoolTimeConfigType.PLAYERTIME_RESET_DAYTASK, true);
								if(coolTimeResult.bReset){
									taskStatistics.reset();
								}
								
								taskStatistics.dayCreateTable++;
								taskStatistics.totalCreateTable++;
								if(maxScore >0 && maxScore == settlementAllVo.score){
									//是大赢家
									taskStatistics.dayWinMaxNum++;
									bMaxWin = true;
								}
								taskStatistics.dayZiMoNum += settlementAllVo.zimo;
								taskStatistics.dayJiePaoNum += settlementAllVo.otherHuPai;
								taskStatistics.dayAnGangNum += settlementAllVo.angang;
								taskStatistics.dayBaGangNum += settlementAllVo.bagang;							
								taskEntity.setTaskStatistics(taskStatistics);
							}
							
							if(hotPromptManager.updateHotPrompt(nAccountId, HotPromptDefine.HOT_KEY_DAYTASK) > 0){
								hotPromptManager.notifyHotPrompt(nAccountId);
							}
							
							if(this.corpsId.equalsIgnoreCase("0") == false){
								//是帮会桌子,记录帮会游戏数据
								corpsManager.lock();
								try{									
									PlayerCorpsEntity playerCorpsEntity = playerCorpsManager.loadOrCreate(nAccountId);
									Map<String, CorpsMemberAttrib> corpsDataList = playerCorpsEntity.getCorpsDataList();
									CorpsMemberAttrib corpsMemberAttrib = corpsDataList.get(this.corpsId);
									if(null != corpsMemberAttrib){										
										corpsMemberAttrib.activeValue++;
										corpsMemberAttrib.gameRoundNum++;
										if(bMaxWin){
											corpsMemberAttrib.winMaxNum++;
										}
										corpsMemberAttrib.score += settlementAllVo.score;
										
										corpsDataList.put(this.corpsId, corpsMemberAttrib);
										playerCorpsEntity.setCorpsDataList(corpsDataList);
									}
								}finally{
									corpsManager.unLock();
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void run_destory(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			this.waitTime = currTime + GameDefine.TIME_TABLE_DESTORY;
			this.sendGameStateNotify();
		}
		if(currTime >= this.waitTime){
			//超时的算同意解散
			for(SeatAttrib seat : this.seats){
				if(seat.btState == GameDefine.ACT_STATE_WAIT){
					seat.btState = GameDefine.ACT_STATE_BT;
					this.sendDestoryBtNotify(seat.seatIndex);
				}
			}
		}
		//如果有超过半数以上座位同意就可以解散桌子了
		int okNum = 0;
		int unOkNum = 0;
		int waitNum = 0;
		for(SeatAttrib seat : this.seats){
			if(seat.btState == GameDefine.ACT_STATE_WAIT){
				boolean bOnLine = sessionManager.isOnline(seat.accountId);
				if(bOnLine == false){
					seat.btState = GameDefine.ACT_STATE_BT;
					this.sendDestoryBtNotify(seat.seatIndex);
				}
			}
			if(seat.btState == GameDefine.ACT_STATE_BT){
				okNum++;
			}else if(seat.btState == GameDefine.ACT_STATE_DROP){
				unOkNum++;
			}else if(seat.btState == GameDefine.ACT_STATE_WAIT){
				waitNum++;
			}
		}
		int N=1;
		if(this.seats.size() == 2){
			//两人房
			N=2;
		}else if(this.seats.size() == 3){
			//三人房
			N=2;
		}else if(this.seats.size() == 4){
			//四人房
			N=3;
		}
		
		if(okNum >= N){
			//超过半数以上座位同意解散,走结算流程
			if((this.gameStateSave > GameDefine.STATE_TABLE_READY && this.gameStateSave < GameDefine.STATE_TABLE_OVER_ONCE)
					|| (this.gameStateSave == GameDefine.STATE_TABLE_OVER_ONCE && this.bActExecSave == false)){
				this.gameState = GameDefine.STATE_TABLE_OVER_ONCE;
			}else{
				this.bSendAllOverNotify = true;
				this.gameState = GameDefine.STATE_TABLE_OVER_ALL;
			}
			this.bActExec = false;
			this.breakStateSource = GameDefine.STATE_TABLE_DESTORY;
			this.bGameNext = false;
		}else{
			if(unOkNum >= N){
				//半数或以上座位不同意解散走原来的游戏流程
				this.bActExec = this.bActExecSave;
				this.gameState = this.gameStateSave;
				this.btIndex = this.btIndexSave;
				this.waitTime = currTime + this.waitTimeSave; 
				for(SeatAttrib seat : this.seats){
					seat.btState = seat.btStateSave;
				}
				this.sendGameStateNotify();
			}else{
				if(waitNum == 0){
					//所有人都表完了态,不解散房间
					this.bActExec = this.bActExecSave;
					this.gameState = this.gameStateSave;
					this.btIndex = this.btIndexSave;
					this.waitTime = currTime + this.waitTimeSave; 
					for(SeatAttrib seat : this.seats){
						seat.btState = seat.btStateSave;
					}
					this.sendGameStateNotify();
				}
			}
		}
	}
	
	private void run_baojiao(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			this.waitTime = currTime + GameDefine.TIME_TABLE_BAOJIAO;
			this.sendGameStateNotify();
		}
		for(SeatAttrib seat : this.seats){
			if(seat.baoJiaoState == 0){
				return;
			}
		}
		
		this.bActExec = false;
		this.gameState = GameDefine.STATE_TABLE_MOPAI;
	}
	
	public void run_piaopai(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
			this.waitTime = currTime + GameDefine.TIME_TABLE_DINGQUE;
			this.resetSeatBtState();
			this.sendGameStateNotify();
		}
		//等待所有玩家漂牌表态
		for(SeatAttrib seat : this.seats){
			if(seat.btState == GameDefine.ACT_STATE_WAIT){
				return;
			}
		}
		
		this.bActExec = false;
		this.gameState = GameDefine.STATE_TABLE_FAPAI;
	}
	
	private void run_wait_ex(long currTime){
		if(this.bActExec == false){
			this.bActExec = true;
		}
		if(currTime < this.waitTime){
			return;
		}
		this.bActExec = false;
		this.gameState = this.nextGameState;
	}
	
	//计算单局结算数据
	public List<SettlementOnceVo> calculation_OverOnce(){
		if(this.gameState != GameDefine.STATE_TABLE_OVER_ONCE){
			return null;
		}
		List<SettlementOnceVo> retVo = new ArrayList<>();
		for(int seatIndex1=0; seatIndex1<this.seats.size(); seatIndex1++){
			SeatAttrib seatAttrib = this.seats.get(seatIndex1);
			if(0 == seatAttrib.accountId){
				continue;
			}
			SeatVo seatVo = this.logicManager.Seat2SeatVo(this, seatAttrib, seatAttrib.seatIndex);
			
			SettlementOnceVo onceItem = new SettlementOnceVo();
			onceItem.accountId = seatVo.accountId;
			onceItem.headImg = seatVo.headImg;
			onceItem.nick = seatVo.nick;
			onceItem.banker = (this.bankerIndex==seatAttrib.seatIndex) ? 1:0;
			for(CardAttrib card : seatAttrib.handCards){
				onceItem.handCards.add(card.cardId);
			}
			for(List<CardAttrib> cardList : seatAttrib.anGangCards){
				List<Integer> tmpList = new ArrayList<>();
				for(CardAttrib card : cardList){
					tmpList.add(card.cardId);
				}
				onceItem.anGangCards.add(tmpList);
			}
			for(List<CardAttrib> cardList : seatAttrib.baGangCards){
				List<Integer> tmpList = new ArrayList<>();
				for(CardAttrib card : cardList){
					tmpList.add(card.cardId);
				}
				onceItem.baGangCards.add(tmpList);
			}
			for(List<CardAttrib> cardList : seatAttrib.dianGangCards){
				List<Integer> tmpList = new ArrayList<>();
				for(CardAttrib card : cardList){
					tmpList.add(card.cardId);
				}
				onceItem.dianGangCards.add(tmpList);
			}
			for(List<CardAttrib> cardList : seatAttrib.pengCards){
				List<Integer> tmpList = new ArrayList<>();
				for(CardAttrib card : cardList){
					tmpList.add(card.cardId);
				}
				onceItem.pengCards.add(tmpList);
			}
			for(CardAttrib card : seatAttrib.huCards){
				onceItem.huCards.add(card.cardId);
			}
			onceItem.rate = seatAttrib.statistAttrib.huPaiRate;
			onceItem.score = seatAttrib.statistAttrib.onceScore;
			onceItem.huPaiIndex = seatAttrib.huPaiIndex;
			StringBuilder strBuilder = new StringBuilder();
			String huPaiTypeDesc = CardAnalysisManager.huPaiType2Desc(seatAttrib.huPaiType);
			if(huPaiTypeDesc != ""){
				strBuilder.append(huPaiTypeDesc);
				String huPaiStyleDesc = CardAnalysisManager.huPaiStyle2Desc(this, seatAttrib);
				strBuilder.append("(").append(huPaiStyleDesc).append(") ");
			}				
			strBuilder.append(seatAttrib.statistAttrib.outDesc).append(" ");
			if(seatAttrib.statistAttrib.anGangNum > 0){
				strBuilder.append("暗杠X").append(seatAttrib.statistAttrib.anGangNum).append(" ");
			}
			if(seatAttrib.statistAttrib.baGangNum > 0){
				strBuilder.append("巴杠X").append(seatAttrib.statistAttrib.baGangNum).append(" ");
			}
			if(seatAttrib.statistAttrib.dianGangNum > 0){
				strBuilder.append("直杠X").append(seatAttrib.statistAttrib.dianGangNum).append(" ");
			}
			if(seatAttrib.statistAttrib.jiajiaYouNum > 0){
				strBuilder.append("巴到烫X").append(seatAttrib.statistAttrib.jiajiaYouNum).append(" ");
			}
			if(seatAttrib.statistAttrib.outAnGang > 0){
				strBuilder.append("被暗杠X").append(seatAttrib.statistAttrib.outAnGang).append(" ");
			}
			if(seatAttrib.statistAttrib.outBaGang > 0){
				strBuilder.append("被巴杠X").append(seatAttrib.statistAttrib.outBaGang).append(" ");
			}
			if(seatAttrib.statistAttrib.outDianGang > 0){
				strBuilder.append("被直杠X").append(seatAttrib.statistAttrib.outDianGang).append(" ");
			}
			if(seatAttrib.statistAttrib.outJiajiaYouNum > 0){
				strBuilder.append("被巴到烫X").append(seatAttrib.statistAttrib.outJiajiaYouNum).append(" ");
			}
			if(seatAttrib.statistAttrib.daiGengNum > 0){
				strBuilder.append("根X").append(seatAttrib.statistAttrib.daiGengNum).append(" ");
			}			
			onceItem.huPaiDesc = strBuilder.toString();
			onceItem.myPiaoNum = seatAttrib.piaoNum;
			
			
			for(int seatIndex2=0; seatIndex2<this.seats.size(); seatIndex2++){
				SeatAttrib tmpSeat = this.seats.get(seatIndex2);
				if(0 == tmpSeat.accountId){
					continue;
				}
				if(seatIndex1 == seatIndex2){
					continue;
				}
				SettementSeatScore settementSeatScore = seatAttrib.seatScore.get(tmpSeat.accountId);
				if(null == settementSeatScore){
					settementSeatScore = new SettementSeatScore();
				}
				if(this.ruleAttrib.bTangPai == false){
					settementSeatScore.tangNum = -1;
				}
				if(this.ruleAttrib.bBaoJia == false){
					settementSeatScore.baoJiaoNum = -1;
				}
				onceItem.seatScore.add(settementSeatScore);
			}
			
			retVo.add(onceItem);
			strBuilder = null;
		}
		return retVo;
	}
	
	//计算总结算数据
	public List<SettlementAllVo> calculation_OverAll(){
		if(this.gameState != GameDefine.STATE_TABLE_OVER_ALL){
			return null;
		}
		List<SettlementAllVo> retVo = new ArrayList<>();
		for(SeatAttrib seatAttrib : this.seats){
			if(0 == seatAttrib.accountId){
				continue;
			}			
			SeatVo seatVo = this.logicManager.Seat2SeatVo(this, seatAttrib, seatAttrib.seatIndex);
			SettlementAllVo allItem = new SettlementAllVo();
			allItem.accountId = seatVo.accountId;
			allItem.headImg = seatVo.headImg;
			allItem.nick = seatVo.nick;
			allItem.starNO = accountManager.load(seatAttrib.accountId).getStarNO();
			allItem.zimo = seatAttrib.statistAttrib.totalZimo;
			allItem.otherHuPai = seatAttrib.statistAttrib.totalDianPaoHu;
			allItem.dianPao = seatAttrib.statistAttrib.totalMyDianPao;
			allItem.angang = seatAttrib.statistAttrib.totalAngang;
			allItem.bagang = seatAttrib.statistAttrib.totalBagang;
			allItem.chajiao = seatAttrib.statistAttrib.totalChaJiao;
			allItem.score = seatAttrib.score;
			retVo.add(allItem);
		}
		return retVo;
	}
//---------------------------------------------------------------------------------------		
	//推送桌子游戏状态变化
	public void sendGameStateNotify(){
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		for(int seatIndex1=0; seatIndex1<this.seats.size(); seatIndex1++){
			SeatAttrib mySeat = this.seats.get(seatIndex1); 
			if(mySeat.accountId <= 0){
				//无人座位跳过
				continue;
			}
			
			Notify_Table_GameStateVo notifyVo = new Notify_Table_GameStateVo();
			notifyVo.state = this.gameState;
			notifyVo.svrTime = String.valueOf(currTime);
			notifyVo.stateVaildTime = String.valueOf(this.waitTime);
			notifyVo.stateTime = "0";
			notifyVo.currGameNum = this.currGameNum;
			notifyVo.tableCardNum = this.tableCards.size();
			notifyVo.btIndex = this.btIndex;
			notifyVo.prevBtIndex = this.prevBtIndex;
			for(SeatAttrib seat : this.seats){
				for(CardAttrib canHuCard : seat.tangCanHuList){
					notifyVo.tangCanHuList.add(canHuCard.cardId);
				}
			}
			notifyVo.stateData = null;
			
			switch(this.gameState){
			case GameDefine.STATE_TABLE_IDLE:
				break;
			case GameDefine.STATE_TABLE_READY:
				{
					Notify_Table_GameStateVo_Ready data_reay = new Notify_Table_GameStateVo_Ready();
					data_reay.bankerIndex = this.bankerIndex;
					notifyVo.stateData = data_reay;
				}
				break;
			case GameDefine.STATE_TABLE_FAPAI:
				{
					Notify_Table_GameStateVo_FaiPai data_faipai = new Notify_Table_GameStateVo_FaiPai();
					for(int seatIndex2=0; seatIndex2<this.seats.size(); seatIndex2++){
						SeatAttrib theSeat = this.seats.get(seatIndex2);
						if(seatIndex1 == seatIndex2){
							//是自已
							List<Integer> handCards = new ArrayList<>();
							for(CardAttrib card : theSeat.handCards){
								handCards.add(card.cardId);
							}
							data_faipai.addSeatPai(seatIndex2, theSeat.handCards.size(), handCards);
						}else{
							//不是自已
							data_faipai.addSeatPai(seatIndex2, theSeat.handCards.size(), null);
						}
					}
					notifyVo.stateData = data_faipai;
				}
				break;
			case GameDefine.STATE_TABLE_SWAPCARD:
				{
					Notify_Table_GameStateVo_SwapCard data_swap = new Notify_Table_GameStateVo_SwapCard();
					for(int seatIndex2=0; seatIndex2<this.seats.size(); seatIndex2++){
						SeatAttrib theSeat = this.seats.get(seatIndex2);
						if(theSeat.accountId <= 0){
							data_swap.addSeat(seatIndex2, 0);
						}else{
							data_swap.addSeat(seatIndex2, theSeat.btState);
						}
					}
					notifyVo.stateData = data_swap;
				}
				break;
			case GameDefine.STATE_TABLE_DINGQUE:
				{
					Notify_Table_GameStateVo_DingQue data_dinque = new Notify_Table_GameStateVo_DingQue();
					for(int seatIndex2=0; seatIndex2<this.seats.size(); seatIndex2++){
						SeatAttrib theSeat = this.seats.get(seatIndex2);
						if(theSeat.accountId <= 0){
							data_dinque.addSeat(seatIndex2, 0);
						}else{
							data_dinque.addSeat(seatIndex2, theSeat.btState);
						}
					}
					notifyVo.stateData = data_dinque;
				}
				break;
			case GameDefine.STATE_TABLE_MOPAI:
				{
					Notify_Table_GameStateVo_MoPai data_mopai = new Notify_Table_GameStateVo_MoPai();
					SeatAttrib theSeat = this.seats.get(this.btIndex);
					data_mopai.seatIndex = this.btIndex;
					data_mopai.moPaiCard = theSeat.moPaiCard.cardId;
					data_mopai.btState = theSeat.btState;
					notifyVo.stateData = data_mopai;
				}
				break;
			case GameDefine.STATE_TABLE_OUTCARD:
				{
					Notify_Table_GameStateVo_OutCard data_outcard = new Notify_Table_GameStateVo_OutCard();
					SeatAttrib theSeat = this.seats.get(this.btIndex);
					data_outcard.seatIndex = this.btIndex;
					data_outcard.btState = theSeat.btState;
					notifyVo.stateData = data_outcard;
				}
				break;
			case GameDefine.STATE_TABLE_BREAKCARD:
				{
					Notify_Table_GameStateVo_BreakCard data_breakcard = new Notify_Table_GameStateVo_BreakCard();
					for(int seatIndex2=0; seatIndex2<this.seats.size(); seatIndex2++){
						SeatAttrib theSeat = this.seats.get(seatIndex2);
						List<Integer> breakCardStates = new ArrayList<>();
						for(int breakCardState : theSeat.breakCardState){
							breakCardStates.add(breakCardState);
						}
						
						SeatAttrib breakSeat = this.seats.get(this.btIndex);
						data_breakcard.addSeat(seatIndex2, theSeat.btState, breakCardStates, breakSeat.breakCard.cardId);
					}
					notifyVo.stateData = data_breakcard;
				}
				break;
			case GameDefine.STATE_TABLE_OVER_ONCE:
				{
					Notify_Table_GameStateVo_OverOnce data_overonce = new Notify_Table_GameStateVo_OverOnce();
					data_overonce.settlementOnce = this.calculation_OverOnce();
					data_overonce.nextGame = this.bGameNext==true?1:0;
					notifyVo.stateData = data_overonce;
				}
				break;
			case GameDefine.STATE_TABLE_OVER_ALL:
				{
					Notify_Table_GameStateVo_OverAll data_overall = new Notify_Table_GameStateVo_OverAll();
					data_overall.settlementAll = this.calculation_OverAll();
					notifyVo.stateData = data_overall;
				}
				break;
			case GameDefine.STATE_TABLE_DESTORY:
				{
					Notify_Table_GameStateVo_Destory data_destory = new Notify_Table_GameStateVo_Destory();
					for(int seatIndex2=0; seatIndex2<this.seats.size(); seatIndex2++){
						SeatAttrib theSeat = this.seats.get(seatIndex2);
						data_destory.addSeat(seatIndex2, theSeat.btState);
					}
					data_destory.destoryQuestPlayer = String.valueOf(this.destoryQuestPlayer);
					notifyVo.stateData = data_destory;
				}
				break;
			case GameDefine.STATE_TABLE_BAOJIAO:
				{
					Notify_Table_GameStateVo_BaoJiao data_baojiao = new Notify_Table_GameStateVo_BaoJiao();
					for(int seatIndex2=0; seatIndex2<this.seats.size(); seatIndex2++){
						SeatAttrib theSeat = this.seats.get(seatIndex2);
						if(theSeat.accountId <= 0){
							data_baojiao.addSeat(seatIndex2, 0);
						}else{
							data_baojiao.addSeat(seatIndex2, theSeat.baoJiaoState);
						}
					}
					notifyVo.stateData = data_baojiao;
				}
				break;
			case GameDefine.STATE_TABLE_PIAOPAI:
				{
					Notify_Table_GameStateVo_PiaoPai data_paiopai = new Notify_Table_GameStateVo_PiaoPai();
					for(int seatIndex2=0; seatIndex2<this.seats.size(); seatIndex2++){
						SeatAttrib theSeat = this.seats.get(seatIndex2);
						if(theSeat.accountId <= 0){
							data_paiopai.addSeat(seatIndex2, 0);
						}else{
							data_paiopai.addSeat(seatIndex2, theSeat.btState);
						}
					}
					notifyVo.stateData = data_paiopai;
				}
				break;
			}
			
			@SuppressWarnings("rawtypes")
			Request pushMsg = Request.valueOf(MajiangDefine.MAJIANGGAME_COMMAND_TABLE_STATE_NOTIFY, 
					Result.valueOfSuccess(notifyVo));			
			MessagePushQueueUtils.getPushQueue(sessionManager).push2(mySeat.accountId, pushMsg);
		}		
		
		//记录回放帧数据
		recordManager.recordReplayFrame(this);
	}
	
	//推送桌子座位数据变化
	//seatIndex=座位下标号
	public void sendSeatNotify(int seatIndex){
		SeatAttrib actSeat = this.seats.get(seatIndex);
		for(int seatIndex1=0; seatIndex1<this.seats.size(); seatIndex1++){
			SeatAttrib theSeat = this.seats.get(seatIndex1);
			if(0 == theSeat.accountId){
				continue;
			}
			SeatVo seatVo = this.logicManager.Seat2SeatVo(this, actSeat, seatIndex1);
			
			@SuppressWarnings("rawtypes")
			Request pushMsg = Request.valueOf(MajiangDefine.MAJIANGGAME_COMMAND_TABLE_SEAT_NOTIFY, 
					Result.valueOfSuccess(seatVo));
			
			MessagePushQueueUtils.getPushQueue(sessionManager).push2(theSeat.accountId, pushMsg);
		}
		
		//记录回放帧数据
		recordManager.recordReplayFrame(this);
	}
	//推送玩家换牌数据
	public void sendSwapCardNotify(){
		for(int seatIndex1=0; seatIndex1<this.seats.size(); seatIndex1++){
			SeatAttrib mySeat = this.seats.get(seatIndex1);
			Notify_Seat_Vo_Swap notifyVo = new Notify_Seat_Vo_Swap();
			for(int seatIndex2=0; seatIndex2<this.seats.size(); seatIndex2++){
				SeatAttrib otherSeat = this.seats.get(seatIndex2);
				if(seatIndex1 == seatIndex2){
					List<Integer> handCards = new ArrayList<>();
					for(CardAttrib card : mySeat.handCards){
						handCards.add(card.cardId);
					}
					List<Integer> swapCards = new ArrayList<>();
					for(CardAttrib card : mySeat.swapCards){
						swapCards.add(card.cardId);
					}
					
					notifyVo.addSeat(seatIndex2, otherSeat.handCards.size(), handCards, swapCards, otherSeat.btState);
				}else{
					notifyVo.addSeat(seatIndex2, otherSeat.handCards.size(), null, null, otherSeat.btState);
				}
			}
			notifyVo.swapCardType = this.swapCardType;
			@SuppressWarnings("rawtypes")
			Request pushMsg = Request.valueOf(MajiangDefine.MAJIANGGAME_COMMAND_SWAPCARD_NOTIFY, 
					Result.valueOfSuccess(notifyVo));			
			MessagePushQueueUtils.getPushQueue(sessionManager).push2(mySeat.accountId, pushMsg);
		}
		//记录回放帧数据
		recordManager.recordReplayFrame(this);
	}
	//推送定缺信息
	public void sendDinQueNotify(int seatIndex){
		Notify_Seat_Vo_DingQue notifyVo = new Notify_Seat_Vo_DingQue();
		SeatAttrib actSeat = this.seats.get(seatIndex);
		
		notifyVo.seatIndex = seatIndex;
		notifyVo.btState = actSeat.btState;
		notifyVo.unSuit = actSeat.unSuit;
		
		for(int seatIndex1=0; seatIndex1<this.seats.size(); seatIndex1++){
			SeatAttrib mySeat = this.seats.get(seatIndex1);
			
			@SuppressWarnings("rawtypes")
			Request pushMsg = Request.valueOf(MajiangDefine.MAJIANGGAME_COMMAND_DINQUE_NOTIFY, 
					Result.valueOfSuccess(notifyVo));			
			MessagePushQueueUtils.getPushQueue(sessionManager).push2(mySeat.accountId, pushMsg);
		}
		//记录回放帧数据
		recordManager.recordReplayFrame(this);
	}
	//推送出牌数据
	public void sendOutCardNotify(SeatAttrib seatAttrib){		
		for(int seatIndex1=0; seatIndex1<this.seats.size(); seatIndex1++){
			SeatAttrib mySeat = this.seats.get(seatIndex1);
			SeatVo seatVo = this.logicManager.Seat2SeatVo(this, seatAttrib, seatIndex1);
			
			@SuppressWarnings("rawtypes")
			Request pushMsg = Request.valueOf(MajiangDefine.MAJIANGGAME_COMMAND_OUTCARD_NOTIFY, 
					Result.valueOfSuccess(seatVo));			
			MessagePushQueueUtils.getPushQueue(sessionManager).push2(mySeat.accountId, pushMsg);
		}
		//记录回放帧数据
		recordManager.recordReplayFrame(this);
	}
	//推送胡杠碰吃数据
	public void sendBreakCardNotify(List<SeatAttrib> breakBtSeats, int btState){
		List<Integer> tmpBreakBtSeats = new ArrayList<>();
		for(SeatAttrib seat : breakBtSeats){
			tmpBreakBtSeats.add(seat.seatIndex);
		}
		
		for(int seatIndex1=0; seatIndex1<this.seats.size(); seatIndex1++){
			SeatAttrib mySeat = this.seats.get(seatIndex1);
			if(0 == mySeat.accountId){
				continue;
			}
			
			Notify_Seat_Vo_BreakCard notifyVo = new Notify_Seat_Vo_BreakCard();
			notifyVo.breakState = btState;
			notifyVo.breakSeats = tmpBreakBtSeats;
			
			for(int seatIndex2=0; seatIndex2<this.seats.size(); seatIndex2++){
				SeatAttrib theSeat = this.seats.get(seatIndex2);
				SeatVo seatVo = this.logicManager.Seat2SeatVo(this, theSeat, seatIndex1);
				notifyVo.seats.add(seatVo);
			}
			
			@SuppressWarnings("rawtypes")
			Request pushMsg = Request.valueOf(MajiangDefine.MAJIANGGAME_COMMAND_BREAKCARD_NOTIFY, 
					Result.valueOfSuccess(notifyVo));			
			MessagePushQueueUtils.getPushQueue(sessionManager).push2(mySeat.accountId, pushMsg);
		}
		//记录回放帧数据
		recordManager.recordReplayFrame(this);
	}
	//推送房间解散消息
	public void sendTableDestoryNotify(List<Long> Ids){
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(MajiangDefine.MAJIANGGAME_COMMAND_TABLE_DESTORY_NOTIFY, 
				Result.valueOfSuccess());
		MessagePushQueueUtils.getPushQueue(sessionManager).push(Ids, pushMsg);
	}
	
	//推送桌子解散表态消息
	public void sendDestoryBtNotify(int seatIndex){
		Notify_Seat_Vo_Destory notifyVo = new Notify_Seat_Vo_Destory();
		 
		 SeatAttrib theSeat = this.seats.get(seatIndex);
		 notifyVo.seatIndex = seatIndex;
		 notifyVo.btState = theSeat.btState;
			
		for(int seatIndex1=0; seatIndex1<this.seats.size(); seatIndex1++){
			SeatAttrib mySeat = this.seats.get(seatIndex1);
			if(theSeat.accountId <= 0){
				continue;
			}
			@SuppressWarnings("rawtypes")
			Request pushMsg = Request.valueOf(MajiangDefine.MAJIANGGAME_COMMAND_TABLE_DESTORY_BT_NOTIFY, 
					Result.valueOfSuccess(notifyVo));			
			MessagePushQueueUtils.getPushQueue(sessionManager).push2(mySeat.accountId, pushMsg);
		}
		//记录回放帧数据
		recordManager.recordReplayFrame(this);
	}
	
	//设置桌子规则
	private void setTableRule(int cfgId, List<Integer> ruleIds){
		StringBuilder builder = new StringBuilder();
		
		for(Integer ruleId : ruleIds){
			RoomRuleConfig ruleCfg = cfgManager.getRuleItemCfg(ruleId);
			if(null == ruleCfg){
				continue;
			}
			if(null != ruleCfg.getDesc() && ruleCfg.getDesc().isEmpty() == false){
				builder.append(ruleCfg.getDesc()).append(" ");
			}
			
			switch(ruleId){
			case 1001://一般房
				this.ruleAttrib.chatType = 0;
				break;
			case 1002://语音房
				this.ruleAttrib.chatType = 1;
				break;
			case 1003://视频房
				this.ruleAttrib.chatType = 2;
				break;
			case 2001://4局(房卡x2)
				this.ruleAttrib.maxGameNum = 4;
				break;
			case 2002://8局(房卡x3)
				this.ruleAttrib.maxGameNum = 8;
				break;
			case 2003://12局(房卡x4)
				this.ruleAttrib.maxGameNum = 12;
				break;
			case 2004://16局(房卡x5)
				this.ruleAttrib.maxGameNum = 16;
				break;
			case 3001://2番
				this.ruleAttrib.maxRate = 2;
				break;
			case 3002://3番
				this.ruleAttrib.maxRate = 3;
				break;
			case 3003://4番
				this.ruleAttrib.maxRate = 4;
				break;
			case 3004://5番
				this.ruleAttrib.maxRate = 5;
				break;
			case 3005://6番
				this.ruleAttrib.maxRate = 6;
				break;
			case 3006://8番
				this.ruleAttrib.maxRate = 8;
				break;
			case 3007://16番
				this.ruleAttrib.maxRate = 16;
				break;
			case 3008://不封顶
				this.ruleAttrib.maxRate = 10000;
				break;
			case 3009://血战到底
				this.ruleAttrib.bXueZhaoDaoDi = true;
				this.ruleAttrib.bXueLiuChengHe = false;
				break;
			case 3010://血流成河
				this.ruleAttrib.bXueZhaoDaoDi = false;
				this.ruleAttrib.bXueLiuChengHe = true;
				break;
			case 4001://2房牌
				this.ruleAttrib.cardTypeNum = 2;
				break;
			case 4002://3房牌
				this.ruleAttrib.cardTypeNum = 3;
				break;
			case 5001://发7张手牌
				this.ruleAttrib.handCardNum = 7;
				break;
			case 5002://发10张手牌
				this.ruleAttrib.handCardNum = 10;
				break;
			case 5003://发13张手牌
				this.ruleAttrib.handCardNum = 13;
				break;
			case 6001://自摸加底
				this.ruleAttrib.bZiMoAddBase = true;
				this.ruleAttrib.bZiMoAddRate = false;
				this.ruleAttrib.bZiMoUnAdd = false;
				break;
			case 6002://自摸加番
				this.ruleAttrib.bZiMoAddRate = true;
				this.ruleAttrib.bZiMoAddBase = false;
				this.ruleAttrib.bZiMoUnAdd = false;
				break;
			case 6003://点杠花(点炮)
				this.ruleAttrib.bDianGangHuaOnce = true;
				this.ruleAttrib.bDianGangHuaAll = false;
				this.ruleAttrib.bDianGangHuaOnceZiMo = true;
				break;
			case 6004://点杠花(自摸)
				this.ruleAttrib.bDianGangHuaOnce = false;
				this.ruleAttrib.bDianGangHuaAll = true;
				this.ruleAttrib.bDianGangHuaOnceZiMo = false;
				break;
			case 6005://换三张
				this.ruleAttrib.bSwapCard = true;
				break;
			case 6006://幺九将对
				this.ruleAttrib.bYaoJiu = true;
				this.ruleAttrib.bJiangDui = true;
				break;
			case 6007://门清中张
				this.ruleAttrib.bMengQin = true;
				this.ruleAttrib.bZhongZhang = true;
				break;
			case 6008://天地胡
				this.ruleAttrib.bTianDiHu = true;
				break;
			case 6009://听牌提示
				this.ruleAttrib.bTingTips = true;
				break;
			case 6010://幺九
				this.ruleAttrib.bYaoJiu = true;
				break;
			case 6011://将对
				this.ruleAttrib.bJiangDui = true;
				break;
			case 6012://门清
				this.ruleAttrib.bMengQin = true;
				break;
			case 6013://中张
				this.ruleAttrib.bZhongZhang = true;
				break;
			case 6101://对对胡2番
				this.ruleAttrib.bDuiduiHu2Fan = true;
				break;
			case 6102://夹心5胡
				this.ruleAttrib.bJiaXin5Hu = true;
				break;
			case 6103://点炮可平胡
				this.ruleAttrib.canHuPaiFanShu = 0;
				this.ruleAttrib.canHuPaiScore = 0;
				break;
			case 6104://0番起胡
				this.ruleAttrib.canHuPaiFanShu = 0;
				this.ruleAttrib.canHuPaiScore = 0;
				break;
			case 6105://1番起胡
				this.ruleAttrib.canHuPaiFanShu = 1;
				this.ruleAttrib.canHuPaiScore = 0;
				break;
			case 6106://2番起胡
				this.ruleAttrib.canHuPaiFanShu = 2;
				this.ruleAttrib.canHuPaiScore = 0;
				break;
			case 6201://2分起胡
				this.ruleAttrib.canHuPaiScore = 2;
				this.ruleAttrib.canHuPaiFanShu = 0;
				break;
			case 6202://1分起胡
				this.ruleAttrib.canHuPaiScore = 1;
				this.ruleAttrib.canHuPaiFanShu = 0;
				break;
			case 6203://2分起胡
				this.ruleAttrib.canHuPaiScore = 2;
				this.ruleAttrib.canHuPaiFanShu = 0;
				break;
			case 6204://3分起胡
				this.ruleAttrib.canHuPaiScore = 3;
				this.ruleAttrib.canHuPaiFanShu = 0;
				break;
			case 6301://自摸不加
				this.ruleAttrib.bZiMoAddRate = false;
				this.ruleAttrib.bZiMoAddBase = false;
				this.ruleAttrib.bZiMoUnAdd = true;
				break;
			case 6302://点杠花(一人自摸)
				this.ruleAttrib.bDianGangHuaOnce = true;
				this.ruleAttrib.bDianGangHuaAll = false;
				this.ruleAttrib.bDianGangHuaOnceZiMo = true;
				break;
			case 6303://幺鸡任用
				this.ruleAttrib.bYaoJiRenYong = true;
				break;
			case 6304://四幺鸡
				break;
			case 6305://软碰可杠
				this.ruleAttrib.bRuanPengKeGang = true;
				break;
			case 6306://家家有
				this.ruleAttrib.bJiaJiaYou = true;
				break;
			case 6401://两家不躺
				this.ruleAttrib.bLiangJiaBuTang = true;
				break;
			case 6501://关死
				this.ruleAttrib.bGuanShi = true;
				break;
			case 6502://转雨
				this.ruleAttrib.bZhuanYu = true;
				break;
			case 6503://查叫退税
				this.ruleAttrib.bChaJiaoTuiShui = true;
				break;
			case 6504://过水加番可胡
				this.ruleAttrib.bGeShuiJiaFanHu = true;
				break;
			case 6601://不飘
				this.ruleAttrib.maxPiaoNum = 0;
				break;
			case 6602://飘1个
				this.ruleAttrib.maxPiaoNum = 1;
				break;
			case 6603://飘2个
				this.ruleAttrib.maxPiaoNum = 2;
				break;
			case 6604://飘3个
				this.ruleAttrib.maxPiaoNum = 3;
				break;
			case 6605://飘4个
				this.ruleAttrib.maxPiaoNum = 4;
				break;
			case 6606://飘5个
				this.ruleAttrib.maxPiaoNum = 5;
				break;
			case 6701://不定缺
				this.ruleAttrib.bDingQue = false;
				break;
			case 6702://定缺
				this.ruleAttrib.bDingQue = true;
				break;
			case 6801://躺牌
				this.ruleAttrib.bTangPai = true;
				break;
			case 6802://不躺牌
				this.ruleAttrib.bTangPai = false;
				break;
			case 6901://游戏2人
				this.ruleAttrib.gamePlayerNum = 2;
				break;
			case 6902://游戏3人
				this.ruleAttrib.gamePlayerNum = 3;
				if(cfgId == 6){
					//自贡麻将特殊3人必须13张
					this.ruleAttrib.handCardNum = 13;
				}
				break;
			case 6903://游戏4人
				this.ruleAttrib.gamePlayerNum = 4;
				if(cfgId == 6){
					//自贡麻将特殊4人必须7张
					this.ruleAttrib.handCardNum = 7;
				}
				break;
			case 7001://查小叫
				this.ruleAttrib.chaDaJiao = 0;
				break;
			case 7002://查大叫
				this.ruleAttrib.chaDaJiao = 1;
				break;
			case 7101://刮风下雨
				this.ruleAttrib.bGuaFengXiaYu = true;
				break;
			case 7102://卡二条胡
				this.ruleAttrib.bJiaXin2Hu = true;
				break;
			case 7103://报叫
				this.ruleAttrib.bBaoJia = true;
				break;
			case 7104://摆牌
				this.ruleAttrib.bTangPai = true;
				break;
			case 7201://创建者支付房卡
				this.ruleAttrib.roomCardPayType = 1;
				break;
			case 7202://随机支付房卡
				this.ruleAttrib.roomCardPayType = 2;
				break;
			case 7203://大赢家支付房卡
				this.ruleAttrib.roomCardPayType = 3;
				break;
			case 7204://输家支付房卡
				this.ruleAttrib.roomCardPayType = 4;
				break;
			case 8001://大对胡
				this.ruleAttrib.bDuiDuiHu = true;
				break;
			case 8002://清一色
				this.ruleAttrib.bQinYiShe = true;
				break;
			case 8003://暗七对
				this.ruleAttrib.bAnQiDui = true;
				break;
			case 8004://龙七对
				this.ruleAttrib.bLongQiDui = true;
				break;
			case 8005://清大对
				this.ruleAttrib.bQinDaDui = true;
				break;
			case 8006://清暗七对
				this.ruleAttrib.bQinAnQiDui = true;
				break;
			case 8007://清龙七对
				this.ruleAttrib.bQinLongQiDui = true;
				break;
			case 8008://金钩钓
				this.ruleAttrib.bJinGouDiao = true;
				break;
			case 8009://海底捞
				this.ruleAttrib.bHaiDiLao = true;
				break;
			case 8010://海底炮
				this.ruleAttrib.bHaiDiPao = true;
				break;
			case 8011://摆独张
				this.ruleAttrib.bBaiDuZhang = true;
				break;
			case 8012://缺一门
				this.ruleAttrib.bQueYiMeng = true;
				break;
			case 8013://一般高
				this.ruleAttrib.bYiBanGao = true;
				break;
			}
		}
		
		if(this.ruleAttrib.canHuPaiScore > 0){
			builder.append(""+this.ruleAttrib.canHuPaiScore+"分起胡");
		}else{
			builder.append(""+this.ruleAttrib.canHuPaiFanShu+"番起胡");
		}
		
		this.ruleShowDesc = builder.toString();
		builder = null;
	}
	
}



