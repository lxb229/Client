package com.palmjoys.yf1b.act.zjh.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.utils.MessagePushQueueUtils;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;
import com.palmjoys.yf1b.act.zjh.manager.GameCfgManager;
import com.palmjoys.yf1b.act.zjh.manager.GameDataManager;
import com.palmjoys.yf1b.act.zjh.manager.GameLogicManager;
import com.palmjoys.yf1b.act.zjh.manager.TableStateNNManager;
import com.palmjoys.yf1b.act.zjh.manager.TableStateZJHManager;

public class TableAttrib {
	//桌子Id
	public int tableId;
	//桌子类型(1=牛牛,2=金花)
	public int gameType;
	//游戏桌子配置表Id
	public int cfgId;
	//桌子座位列表
	public List<SeatAttrib> seats;
	//游戏状态
	public int gameState;
	//游戏动作到期时间
	public long waitTime;
	//动作总时间
	public int actTotalTime;
	//游戏动作是否执行
	public boolean bExec;
	//游戏下一动作
	public int nextState;
	//庄家位置
	public int bankerIndex;
	//当前表态座位下标
	public int btIndex;
	//桌子创建玩家(0=系统创建)
	public long createPlayer;
	//桌子上不看牌需下注金额
	public int unLookBetMoney;
	//桌子上看牌需下注金额
	public int lookBetMoney;
	//金花桌子上个下注玩家的座位下标
	public int prevSeatIndex;
	//金花桌子上个下注玩家的下注时看牌状态(0=未看牌,1=已看牌)
	public int prevSeatBetLookState;
	//全下的座位下注金额
	public long prevAllBetMoney;
	//结算结果
	public SettlementAttrib settlement;
	//压注池(KEY=玩家Id,VALUE=总压注金币)
	public Map<Long, Long> betMap;
	//金花桌子游戏轮数
	public int roundNum;
	//比牌数据
	public ZjhCompareCardAttrib compareCardAttrib;
	//本局是否机器人赢
	public boolean bRobotWin;
	
	//桌子状态运行接口
	private TableStateNNManager tableStateNNManager;
	private TableStateZJHManager tableStateZJHManager;
	public AccountManager accountManager;
	public GameLogicManager logicManager;
	public GameCfgManager cfgManager;
	public WalletManager walletManager;
	public GameDataManager gameDataManager;
	public SessionManager sessionManager;
	
	
	public TableAttrib(int tableId, int gameType, int cfgId, long createPlayer, TableStateNNManager tableStateNNManager, 
			TableStateZJHManager tableStateZJHManager, AccountManager accountManager, 
			GameLogicManager logicManager, GameCfgManager cfgManager, WalletManager walletManager,
			GameDataManager gameDataManager, SessionManager sessionManager){
		this.tableId = tableId;
		this.gameType = gameType;
		this.cfgId = cfgId;
		this.tableStateNNManager = tableStateNNManager;
		this.tableStateZJHManager = tableStateZJHManager;
		this.gameState = GameDefine.STATE_TABLE_IDLE;
		this.waitTime = 0;
		this.bExec = false;
		this.nextState = GameDefine.STATE_TABLE_IDLE;
		this.bankerIndex = -1;
		this.btIndex = 0;
		this.accountManager = accountManager;
		this.logicManager = logicManager;
		this.cfgManager = cfgManager;
		this.walletManager = walletManager;
		this.gameDataManager = gameDataManager;
		this.sessionManager = sessionManager;
		this.roundNum = 0;
		this.compareCardAttrib = new ZjhCompareCardAttrib();
		this.prevSeatIndex = -1;
		this.prevAllBetMoney = 0;
		this.prevSeatBetLookState = 0;
		this.createPlayer = createPlayer;
		this.unLookBetMoney = 0;
		this.lookBetMoney = 0;
		
		this.settlement = new SettlementAttrib();
		this.betMap = new HashMap<>();
		
		this.seats = new ArrayList<>();
		if(this.gameType == GameDefine.GAME_TYPE_NN){
			for(int seatIndex=0; seatIndex<GameDefine.SEAT_NUM_NN; seatIndex++){
				SeatAttrib seat = new SeatAttrib(seatIndex);
				this.seats.add(seat);
			}
		}else if(this.gameType == GameDefine.GAME_TYPE_ZJH){
			for(int seatIndex=0; seatIndex<GameDefine.SEAT_NUM_ZJH; seatIndex++){
				SeatAttrib seat = new SeatAttrib(seatIndex);
				this.seats.add(seat);
			}
		}
	}

	public SeatAttrib getEmptySeat(){
		for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
			SeatAttrib seat = this.getSeat(seatIndex);
			if(seat.accountId == 0){
				return seat;
			}
		}
		return null;
	}
	
	public SeatAttrib getPlayerSeat(long accountId){
		for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
			SeatAttrib seat = this.getSeat(seatIndex);
			if(seat.accountId == accountId){
				return seat;
			}
		}
		return null;
	}
	
	public List<SeatAttrib> getGamedPlayerSeats(long myAccountId){
		List<SeatAttrib> playerSeats = new ArrayList<>();
		for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
			SeatAttrib seat = this.getSeat(seatIndex);
			if(seat.accountId > 0 && seat.bGamed && myAccountId != seat.accountId 
					&& seat.btState != GameDefine.ACT_STATE_DROP){
				GameDataAttrib dataAttrib = this.gameDataManager.getGameData(seat.accountId);
				if(dataAttrib.robot == 0){
					playerSeats.add(seat);
				}
			}
		}
		return playerSeats;
	}
	
	public SeatAttrib getGamedPlayer(long myAccountId){
		List<SeatAttrib> playerSeats = this.getGamedPlayerSeats(myAccountId);
		if(playerSeats.isEmpty() == false){
			int index = (int) ((Math.random()*100)%playerSeats.size());
			return playerSeats.get(index);
		}
		
		return null;
	}
	
	public List<SeatAttrib> getGamedRobotSeats(long myAccountId){
		List<SeatAttrib> playerSeats = new ArrayList<>();
		for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
			SeatAttrib seat = this.getSeat(seatIndex);
			if(seat.accountId > 0 && seat.bGamed && myAccountId != seat.accountId
					&& seat.btState != GameDefine.ACT_STATE_DROP){
				GameDataAttrib dataAttrib = this.gameDataManager.getGameData(seat.accountId);
				if(dataAttrib.robot == 1){
					playerSeats.add(seat);
				}
			}
		}
		return playerSeats;
	}
	
	public SeatAttrib getGamedRobotPlayer(long myAccountId){
		List<SeatAttrib> playerSeats = this.getGamedRobotSeats(myAccountId);
		if(playerSeats.isEmpty() == false){
			int index = (int) ((Math.random()*100)%playerSeats.size());
			return playerSeats.get(index);
		}
		return null;
	}
	
	public SeatAttrib getNextSeat(int currIndex){
		int N = this.seats.size();
		int next = (currIndex + 1)%N;
		return this.seats.get(next);
	}
	
	public SeatAttrib getNextBtSeat(int currIndex){
		int loop = 0;
		int next = currIndex;
		while(loop < this.seats.size()){
			next = (next + 1)%this.seats.size();
			SeatAttrib seat = this.getSeat(next);
			if(seat.accountId > 0 && seat.bGamed
					&& seat.btState != GameDefine.ACT_STATE_DROP){
				return seat;
			}
			next++;
			loop++;
		}
		
		return null;
	}
	
	public long getGamedPlayerMinMoney(){
		long minMoney = 1000000000;
		for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
			SeatAttrib seat = this.getSeat(seatIndex);
			if(seat.accountId > 0 && seat.bGamed
					&& seat.btState != GameDefine.ACT_STATE_DROP){
				long theMoney = this.walletManager.getRoomCard(seat.accountId);
				if(minMoney > theMoney){
					minMoney = theMoney;
				}
			}
		}
		return minMoney;
	}
	
	public SeatAttrib getSeat(int seatIndex){
		return this.seats.get(seatIndex);
	}
	
	public void resetBtState(){
		for(SeatAttrib seat : this.seats){
			seat.btState = GameDefine.ACT_STATE_WAIT;
			seat.robotBtTime = 0;
		}
	}
	
	public void reset(){
		this.resetBtState();
		for(SeatAttrib seat : this.seats){
			seat.reset();
		}
		this.settlement.reset();
		this.betMap.clear();
		this.prevSeatIndex = -1;
		this.unLookBetMoney = this.cfgManager.getCfg(this.cfgId).getBaseScore();
		this.lookBetMoney = this.unLookBetMoney;
		this.prevAllBetMoney = 0;
		this.roundNum = 0;
		this.prevSeatBetLookState = 0;
	}
	
	public int getTablePlayerNum(){
		int N = 0;
		for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
			SeatAttrib seat = this.getSeat(seatIndex);
			if(seat.accountId > 0){
				N++;
			}
		}
		return N;
	}
	
	public void playerBetMoney(long accountId, long money){
		Long moneyObj = this.betMap.get(accountId);
		if(null == moneyObj){
			moneyObj = money;
		}else{
			moneyObj = money + moneyObj.longValue();
		}
		this.betMap.put(accountId, moneyObj);
	}
	
	public long getPlayerBetMoney(long accountId){
		SeatAttrib seat = this.getPlayerSeat(accountId);
		if(null == seat || seat.bGamed == false){
			return 0;
		}
		Long moneyObj = this.betMap.get(accountId);
		if(null == moneyObj){
			return 0;
		}else{
			return moneyObj.longValue();
		}
	}	
	
	public void sendNotify(int msg){
		TableVo tableVo = this.table2TableVo();
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(msg, Result.valueOfSuccess(tableVo));
		for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
			SeatAttrib seat = this.getSeat(seatIndex);
			if(seat.accountId > 0){
				MessagePushQueueUtils.getPushQueue(sessionManager).push2(seat.accountId, pushMsg);
			}
		}
	}
	
	public void sendBtNotify(int msg, int btIndex, long betMoney){
		TableBtVo btVo = new TableBtVo();
		btVo.btIndex = btIndex;
		btVo.betMoney = String.valueOf(betMoney);
		btVo.tableVo = this.table2TableVo();
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(msg, Result.valueOfSuccess(btVo));
		for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
			SeatAttrib seat = this.getSeat(seatIndex);
			if(seat.accountId > 0){
				MessagePushQueueUtils.getPushQueue(sessionManager).push2(seat.accountId, pushMsg);
			}
		}
	}
	
	public void sendKickNotify(long accountId, int reason){
		@SuppressWarnings("rawtypes")
		Request pushMsg = Request.valueOf(ZJHMessageDefine.ZJHGAME_COMMAND_KICK_NOTIFY, Result.valueOfSuccess(reason));
		MessagePushQueueUtils.getPushQueue(sessionManager).push2(accountId, pushMsg);
	}
	
	public void run(){
		switch(this.gameType){
		case GameDefine.GAME_TYPE_NN:
			tableStateNNManager.state_run(this);
			break;
		case GameDefine.GAME_TYPE_ZJH:
			tableStateZJHManager.state_run(this);
			break;
		}
	}
	
	public TableVo table2TableVo(){
		TableVo retVo = new TableVo();
		retVo.tableId = this.tableId;
		retVo.gameType = this.gameType;
		retVo.gameState = this.gameState;
		retVo.bankerId = "0";
		if(this.bankerIndex != -1){
			retVo.bankerId = String.valueOf(this.getSeat(this.bankerIndex).accountId);
		}
		retVo.btIndex = this.btIndex;
		retVo.svrTime = String.valueOf(DateUtils.getTime(-1, -1, -1, -1, -1, -1));
		retVo.actTime = String.valueOf(this.waitTime);
		retVo.NNOverItems.addAll(this.settlement.NNItems);
		retVo.ZJHOverItems.addAll(this.settlement.ZJHItems);
		retVo.baseScore = this.cfgManager.getCfg(this.cfgId).getBaseScore();
		retVo.onceMax = this.cfgManager.getCfg(this.cfgId).getOnceMax();
		retVo.roundNum = this.roundNum;
		retVo.actTotalTime = this.actTotalTime;
		retVo.compareSrcIndex = this.compareCardAttrib.compareScr;
		retVo.compareDstIndex = this.compareCardAttrib.compareDst;
		retVo.compareResult = this.compareCardAttrib.compareResult;
		retVo.unLookBetMoney = this.unLookBetMoney;
		retVo.lookBetMoney = this.lookBetMoney;
		retVo.prevSeatIndex = this.prevSeatIndex;
		
		for(int seatIndex=0; seatIndex<this.seats.size(); seatIndex++){
			SeatAttrib seat = this.getSeat(seatIndex);
			SeatVo seatVo = new SeatVo();
			seatVo.seatIndex = seat.seatIndex;
			seatVo.accountId = String.valueOf(seat.accountId);
			seatVo.headImg = "";
			seatVo.nick = "";
			seatVo.starNO = "";
			seatVo.money = 0;
			seatVo.btVal = seat.btVal;
			seatVo.looked = seat.bLookCard==true?1:0;
			seatVo.betMoney = 0;
			seatVo.bGamed = seat.bGamed==true?1:0;
			if(seat.accountId > 0){
				AccountEntity accountEntity = this.accountManager.load(seat.accountId);
				if(null != accountEntity){
					seatVo.headImg = accountEntity.getHeadImg();
					seatVo.nick = accountEntity.getNick();
					seatVo.starNO = accountEntity.getStarNO();
					seatVo.money = (int) this.walletManager.getRoomCard(seat.accountId);
					seatVo.betMoney = (int) this.getPlayerBetMoney(seat.accountId);
				}
			}			
			seatVo.handCards.addAll(seat.handCards);
			seatVo.btState = seat.btState;
			
			retVo.seats.add(seatVo);
			retVo.totalBetMoney += seatVo.betMoney;
		}			
		return retVo;
	}
}
