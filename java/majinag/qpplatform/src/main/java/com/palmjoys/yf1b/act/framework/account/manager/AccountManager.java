package com.palmjoys.yf1b.act.framework.account.manager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.id.MultiServerIdGeneratorManager;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;
import org.treediagram.nina.network.util.IpUtils;

import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.entity.AuthenticationEntity;
import com.palmjoys.yf1b.act.framework.account.entity.LoginEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountFilterManager.AccountFilter;
import com.palmjoys.yf1b.act.framework.account.manager.AccountFilterManager.AccountFilter_Phone;
import com.palmjoys.yf1b.act.framework.account.model.AccountAttribVo;
import com.palmjoys.yf1b.act.framework.account.model.OtherAccountAttribVo;
import com.palmjoys.yf1b.act.framework.common.manager.CommonCfgManager;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.majiang.manager.GameDataManager;
import com.palmjoys.yf1b.act.majiang.manager.GameLogicManager;
import com.palmjoys.yf1b.act.majiang.model.GameDataAttrib;
import com.palmjoys.yf1b.act.majiang.model.SeatAttrib;
import com.palmjoys.yf1b.act.majiang.model.TableAttrib;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

@Component
public class AccountManager {
	@Inject
	private EntityMemcache<Long, AccountEntity> accountCache;
	@Autowired
	private Querier querier;
	@Autowired
	private MultiServerIdGeneratorManager multiServerIdGeneratorManager;
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private LoginManager loginLogManager;
	@Autowired
	private HotPromptManager hotPromptManager;
	@Autowired
	private GameDataManager gameDataManager;
	@Autowired
	private GameLogicManager logicManager;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private CommonCfgManager commonCfgManager;
	@Autowired
	private SencePlayerManager sencePlayerManager;
	
	
	//服务器与客户端ping值数据
	private Map<Long, Integer> pingMap = new ConcurrentHashMap<Long, Integer>();
	
	@PostConstruct
	protected void init() {
		multiServerIdGeneratorManager.init(AccountEntity.class, AccountEntity.ACCOUNT_MAXID);
	}
	
	public AccountEntity create(String uuid, String starNO,
			String headImg, int sex, String nick, String createIP){
		short channel = 1;
		short zone = 1;
		final Long id = multiServerIdGeneratorManager.getNext(AccountEntity.class, channel, zone);
		return accountCache.loadOrCreate(id, new EntityBuilder<Long, AccountEntity>(){
			@Override
			public AccountEntity createInstance(Long pk) {
				return AccountEntity.valueOf(id, uuid, starNO, headImg, sex, nick, createIP);
			}
		});
	}
	
	public AccountEntity load(long id){
		return accountCache.load(id);
	}
	
	//通过微信或游客身份Id查找玩家
	public AccountEntity findOf_uuid(String uuid){
		//先查全表
		long accountId = 0;
		String querySql = "SELECT A.accountId FROM AccountEntity AS A WHERE A.uuid='" + uuid + "'";
		List<Object> retObjs = querier.listBySqlLimit(AccountEntity.class, Object.class, querySql, 0, 1);
		for(Object obj : retObjs){
			if(null != obj){
				accountId = (long) obj;
				break;
			}
		}
		//后查缓存
		if(0==accountId){
			AccountFilter filter = AccountFilterManager.Instance().createAccountFilter(uuid, "", -1);
			List<AccountEntity> retEntitys = accountCache.getFinder().find(filter);
			for(AccountEntity entity : retEntitys){
				return entity;
			}
			return null;
		}
		
		return this.load(accountId);
	}
	
	//通过明星号查找玩家
	public AccountEntity findOf_starNO(String starNO){
		//先查全表
		long accountId = 0;
		String querySql = "SELECT A.accountId FROM AccountEntity AS A WHERE A.starNO='" + starNO + "'";
		List<Object> retObjs = querier.listBySqlLimit(AccountEntity.class, Object.class, querySql, 0, 1);
		for(Object obj : retObjs){
			if(null != obj){
				accountId = (long) obj;
				break;
			}
		}
		//后查缓存
		if(0==accountId){
			AccountFilter filter = AccountFilterManager.Instance().createAccountFilter("", starNO, -1);
			List<AccountEntity> retEntitys = accountCache.getFinder().find(filter);
			for(AccountEntity entity : retEntitys){
				return entity;
			}
			return null;
		}
		
		return this.load(accountId);
	}
	
	//通过手机号查找玩家
	public AccountEntity findOf_phone(String phone){
		//先查全表,后查缓存
		long accountId = 0;
		String querySql = "SELECT A.accountId FROM AuthenticationEntity AS A WHERE A.phone='"+phone+"'";
		List<Object> retObjs = querier.listBySqlLimit(AuthenticationEntity.class, Object.class, querySql, 0, 1);
		for(Object obj : retObjs){
			if(null != obj){
				accountId = (long) obj;
				break;
			}
		}
		//后查缓存
		if(0==accountId){
			AccountFilter_Phone filter = AccountFilterManager.Instance().createAccountFilter_Phone(phone);
			List<AuthenticationEntity> retEntitys = authenticationManager.findCache(filter);
			for(AuthenticationEntity entity : retEntitys){
				accountId = entity.getId();
				break;
			}
		}
		if(0==accountId){
			return null;
		}
		return this.load(accountId);
	}

	//获取客户端ping
	public int getClientPing(long accountId){
		Integer ping = 0;
		ping = pingMap.get(accountId);
		if(null == ping){
			return 0;
		}
		return ping.intValue();
	}
	
	//更新ping
	public void updatPing(long accountId, int ping){
		pingMap.put(accountId, ping);
	}
	
	//删除玩家ping数据
	public void deletePing(long accountId){
		pingMap.remove(accountId);
	}
	
	//从缓存中清除实体
	public void clearOfCache(long accountId){
		accountCache.clear(accountId);
		deletePing(accountId);
	}
	
	//服务器帐号数据转客户端属性数特据
	public AccountAttribVo Account2AccountAttrib(AccountEntity accountEntity){
		AccountAttribVo retVo = new AccountAttribVo();
		retVo.accountId = ""+accountEntity.getId();
		retVo.uuid = accountEntity.getUuid();
		retVo.starNO = accountEntity.getStarNO();
		retVo.headImg = accountEntity.getHeadImg();
		retVo.sex = accountEntity.getSex();
		retVo.nick = accountEntity.getNick();
		retVo.wallet = walletManager.getWallet(accountEntity.getId());
		GameDataAttrib gameData = gameDataManager.getGameData(accountEntity.getId());
		retVo.tableId = gameData.tableId;
		AuthenticationEntity authenticationEntity = authenticationManager.loadOrCreate(accountEntity.getId());
		retVo.phone = authenticationManager.phoneEncryption(authenticationEntity.getPhone());
		retVo.authenticationFlag = 0;
		if(authenticationEntity.getCardId().length() > 0){
			retVo.authenticationFlag = 1;
		}
		retVo.wxNO = commonCfgManager.getPlatformWXNO();
		gameData = null;
		
		return retVo;
	}
	
	public OtherAccountAttribVo Account2OtherAttrib(AccountEntity accountEntity){
		OtherAccountAttribVo retVo = new OtherAccountAttribVo();
		retVo.headImg = accountEntity.getHeadImg();
		retVo.nick = accountEntity.getNick();
		retVo.sex = accountEntity.getSex();
		retVo.starNO = accountEntity.getStarNO();
		LoginEntity loginEntity = loginLogManager.load(accountEntity.getId());
		retVo.clientIP = loginEntity.getLoginIP();
		return retVo;
	}
	
	public void accountOnLine(long accountId, IoSession session){
		//更新登录日志信息
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		LoginEntity loginLog = loginLogManager.loadOrCreate(accountId);
		loginLog.setInTime(currTime);
		loginLog.setLoginIP(IpUtils.getIp(session));
		GameDataAttrib gameDataAttrib = gameDataManager.getGameData(accountId);
		gameDataAttrib.offlineTime = 0;
		
		gameDataAttrib.onLine = 1;
		if(gameDataAttrib.tableId > 0){
			logicManager.lock();
			try{
				TableAttrib table = logicManager.getTable(gameDataAttrib.tableId);
				if(null != table){
					SeatAttrib mySeat = table.getPlayerSeatIndex(accountId);
					if(null != mySeat){
						table.sendSeatNotify(mySeat.seatIndex);
					}
				}else{
					gameDataAttrib.tableId = 0;
				}
			}finally{
				logicManager.unLock();
			}
		}
		
		//检测推送红点消息
		hotPromptManager.updateHotPromptAll(accountId);
		hotPromptManager.notifyHotPrompt(accountId);
	}
	
	public void accountOffLine(long accountId){
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		LoginEntity loginLog = loginLogManager.load(accountId);
		loginLog.setOutTime(currTime);
		
		GameDataAttrib gameDataAttrib = gameDataManager.getGameData(accountId);
		gameDataAttrib.onLine = 0;
		if(gameDataAttrib.tableId > 0){
			logicManager.lock();
			try{
				gameDataAttrib.offlineTime = 0;
				TableAttrib table = logicManager.getTable(gameDataAttrib.tableId);
				if(null != table){
					SeatAttrib mySeat = table.getPlayerSeatIndex(accountId);
					if(null != mySeat){
						table.sendSeatNotify(mySeat.seatIndex);
						if(table.currGameNum == 1){
							gameDataAttrib.offlineTime = currTime + 10*60*1000;
						}
						
						if(null != table.corpsId && table.corpsId.equalsIgnoreCase("0") == false ){
							//如果是帮会桌子
							sencePlayerManager.leaveSence(SencePlayerManager.SENCE_CORPS, table.corpsId, accountId);
						}
					}
				}else{
					gameDataAttrib.tableId = 0;
				}
			}finally{
				logicManager.unLock();
			}
		}else{
			gameDataManager.remove(accountId);
		}
	}
}
