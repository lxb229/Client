package com.palmjoys.yf1b.act.framework.account.manager;

import java.util.ArrayList;
import java.util.Collection;
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
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.entity.AuthenticationEntity;
import com.palmjoys.yf1b.act.framework.account.entity.LoginEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountFilterManager.AccountFilter;
import com.palmjoys.yf1b.act.framework.account.manager.AccountFilterManager.AccountFilter_Phone;
import com.palmjoys.yf1b.act.framework.account.model.AccountAttribVo;
import com.palmjoys.yf1b.act.framework.account.model.OtherAccountAttribVo;
import com.palmjoys.yf1b.act.framework.account.resource.NickNameConfig;
import com.palmjoys.yf1b.act.zjh.manager.GameDataManager;
import com.palmjoys.yf1b.act.zjh.manager.GameLogicManager;
import com.palmjoys.yf1b.act.zjh.model.GameDataAttrib;
import com.palmjoys.yf1b.act.zjh.model.TableAttrib;
import com.palmjoys.yf1b.act.zjh.model.ZJHMessageDefine;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.mail.manager.MailManager;
import com.palmjoys.yf1b.act.wallet.entity.WalletEntity;
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
	private MailManager mailManager;
	@Autowired
	private HotPromptManager hotPromptManager;
	@Autowired
	private GameDataManager gameDataManager;
	@Autowired
	private GameLogicManager logicManager;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private AccountLuckyManager accountLuckyManager;
	@Static
	private Storage<Integer, NickNameConfig> nickNameCfgs;
	
	
	//服务器与客户端ping值数据
	private Map<Long, Integer> pingMap = new ConcurrentHashMap<Long, Integer>();
	
	@PostConstruct
	protected void init() {
		multiServerIdGeneratorManager.init(AccountEntity.class, AccountEntity.ACCOUNT_MAXID);
	}
	
	public AccountEntity create(String uuid, String password, String starNO,
			String headImg, int sex, String nick, String createIP, int robot){
		short channel = 1;
		short zone = 1;
		final Long id = multiServerIdGeneratorManager.getNext(AccountEntity.class, channel, zone);
		return accountCache.loadOrCreate(id, new EntityBuilder<Long, AccountEntity>(){
			@Override
			public AccountEntity createInstance(Long pk) {
				return AccountEntity.valueOf(id, uuid, password, starNO, headImg, sex, nick, createIP, robot);
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
			AccountFilter filter = AccountFilterManager.Instance().createAccountFilter(uuid, "");
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
			AccountFilter filter = AccountFilterManager.Instance().createAccountFilter("", starNO);
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
		WalletEntity walletEntity = walletManager.loadOrCreate(accountEntity.getId());
		retVo.roomCard = String.valueOf(walletEntity.getRoomCard());
		GameDataAttrib gameData = gameDataManager.getGameDataClone(accountEntity.getId());
		retVo.tableId = gameData.tableId;
		AuthenticationEntity authenticationEntity = authenticationManager.loadOrCreate(accountEntity.getId());
		retVo.phone = authenticationManager.phoneEncryption(authenticationEntity.getPhone());
		retVo.authenticationFlag = 0;
		if(authenticationEntity.getCardId().length() > 0){
			retVo.authenticationFlag = 1;
		}
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
	}
	
	public void accountOffLine(long accountId){
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		LoginEntity loginLog = loginLogManager.load(accountId);
		loginLog.setOutTime(currTime);
		logicManager.lock();
		try{
			GameDataAttrib gameDataAttrib = gameDataManager.getGameData(accountId);
			gameDataAttrib.onLine = 0;
			if(gameDataAttrib.tableId > 0){
				TableAttrib table = logicManager.getTable(gameDataAttrib.tableId);
				if(null != table){
					table.sendNotify(ZJHMessageDefine.ZJHGAME_COMMAND_SEAT_NOTIFY);
				}else{
					gameDataAttrib.tableId = 0;
				}
			}
		}finally{
			logicManager.unLock();
		}		
		
		//Session关闭时从缓存中卸载实体,减少内存占用
		this.clearOfCache(accountId);
		mailManager.clearOfCache(accountId);
		walletManager.clearOfCache(accountId);
		hotPromptManager.clearOfCache(accountId);
		loginLogManager.clearOfCache(accountId);
		accountLuckyManager.clearOfCache(accountId);
	}
	
	public String getRandomNickName(){
		List<String> firstNames = new ArrayList<>();
		List<String> lastNames = new ArrayList<>();
		List<String> secondNames = new ArrayList<>();
		
		Collection<NickNameConfig> allCfgs = nickNameCfgs.getAll();
		for(NickNameConfig cfg : allCfgs){
			String firstName = cfg.getFirstName();
			String lastName = cfg.getLastName();
			String secondName = cfg.getSecondName();
			if(null != firstName && firstName.isEmpty() == false){
				firstNames.add(firstName);
			}
			if(null != lastName && lastName.isEmpty() == false){
				lastNames.add(lastName);
			}
			if(null != secondName && secondName.isEmpty() == false){
				secondNames.add(secondName);
			}
		}
		int total = 100000000;
		int firstNameIndex = (int) ((Math.random()*total)%firstNames.size());
		int lastNameIndex = (int) ((Math.random()*total)%lastNames.size());
		int secondNameIndex = (int) ((Math.random()*total)%secondNames.size());
		
		String retNick = "";
		int randVal = (int) (Math.random()*100);
		if(randVal >=0 && randVal < 40){
			//姓+双名
			retNick = firstNames.get(firstNameIndex) + lastNames.get(lastNameIndex);
		}else if(randVal >=40 && randVal < 60){
			//姓+单名
			retNick = firstNames.get(firstNameIndex) + secondNames.get(secondNameIndex);
		}else{
			//姓+单名+双名
			String tmpNick = firstNames.get(firstNameIndex) + lastNames.get(lastNameIndex);
			if(tmpNick.length() > 3){
				randVal = (int) (Math.random()*100%2);
				if(randVal == 0){
					retNick = firstNames.get(firstNameIndex) + lastNames.get(lastNameIndex);
				}else{
					retNick = firstNames.get(firstNameIndex) + secondNames.get(secondNameIndex);
				}
			}else{
				retNick = firstNames.get(firstNameIndex) + secondNames.get(secondNameIndex) + lastNames.get(lastNameIndex);
			}
		}		
		firstNames = null;
		lastNames = null;
		secondNames = null;
		return retNick;
	}
}
