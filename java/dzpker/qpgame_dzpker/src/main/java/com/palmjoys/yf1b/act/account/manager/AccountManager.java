package com.palmjoys.yf1b.act.account.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.id.MultiServerIdGeneratorManager;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Result;
import org.treediagram.nina.network.util.IpUtils;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.dzpker.manager.GameLogicManager;
import com.palmjoys.yf1b.act.dzpker.model.DzpkerDefine;
import com.palmjoys.yf1b.act.dzpker.model.SeatAttrib;
import com.palmjoys.yf1b.act.dzpker.model.TableAttrib;
import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;
import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.account.entity.RoleEntity;
import com.palmjoys.yf1b.act.account.manager.AccountFilterManager.AccountFilter_UUID;
import com.palmjoys.yf1b.act.account.model.AccountAttribVo;
import com.palmjoys.yf1b.act.account.model.AccountDefine;
import com.palmjoys.yf1b.act.account.model.GameDataAttrib;
import com.palmjoys.yf1b.act.account.model.OtherAccountAttribVo;
import com.palmjoys.yf1b.act.account.resource.NickNameConfig;
import com.palmjoys.yf1b.act.framework.common.resource.ConfigValue;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.mail.manager.MailManager;
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
	private MailManager mailManager;
	@Autowired
	private HotPromptManager hotPromptManager;
	@Autowired
	private RoleEntityManager roleEntityManager;
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private GameDataManager gameDataManager;
	@Autowired
	private GameLogicManager logicManager;
	@Autowired
	private EventTriggerManager eventTriggerManager;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Static
	private Storage<Integer, NickNameConfig> nickNameCfgs;
	@Static("DISABLE_ALL_LOGIN")
	private ConfigValue<Integer> disableAllLogin;
	@Static("DISABLE_LOGIN_WHITE")
	private ConfigValue<String> disableLoginWhite;
	
	
	//帐号创建方式
	//正常密码方式注册
	public static final int ACCOUNT_CREATE_TYPE_PASSWORD = 0;
	//游客方式注册
	public static final int ACCOUNT_CREATE_TYPE_TOURIST = 1;
	//微信方式注册
	public static final int ACCOUNT_CREATE_TYPE_WX = 2;
	
	
	
	@PostConstruct
	protected void init() {
		multiServerIdGeneratorManager.init(AccountEntity.class, AccountEntity.ACCOUNT_MAXID);
	}
	
	public AccountEntity create(String uuid, String password, String createIP, int accountType, int createType){
		short channel = 1;
		short zone = 1;
		final Long id = multiServerIdGeneratorManager.getNext(AccountEntity.class, channel, zone);
		AccountEntity retEntity = accountCache.loadOrCreate(id, new EntityBuilder<Long, AccountEntity>(){
			@Override
			public AccountEntity createInstance(Long pk) {
				AccountEntity theEntity = AccountEntity.valueOf(id, uuid, password, createIP, accountType, createType);
				return theEntity;
			}
		});
		
		return retEntity;
	}
	
	public AccountEntity findOf_accountId(long accountId){
		return accountCache.load(accountId);
	}
	
	//通过微信或游客身份Id或帐号名称查找帐号
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
			AccountFilter_UUID filter = AccountFilterManager.Instance().createFilter_AccountFilter_UUID(uuid);
			List<AccountEntity> retEntitys = accountCache.getFinder().find(filter);
			for(AccountEntity entity : retEntitys){
				return entity;
			}
			return null;
		}
		
		return this.findOf_accountId(accountId);
	}
	
	//从缓存中清除实体
	public void clearOfCache(long accountId){
		accountCache.clear(accountId);
	}
	
	//服务器帐号数据转客户端属性数特据
	public AccountAttribVo Account2AccountAttrib(AccountEntity accountEntity){
		RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountEntity.getAccountId());
		AccountAttribVo retVo = new AccountAttribVo();
		retVo.accountId = String.valueOf(accountEntity.getAccountId());
		retVo.uuid = accountEntity.getUuid();
		retVo.roleAttribVo.starNO = roleEntity.getStarNO();
		retVo.roleAttribVo.headImg = roleEntity.getHeadImg();
		retVo.roleAttribVo.sex = roleEntity.getSex();
		retVo.roleAttribVo.nick = roleEntity.getNick();
		retVo.roleAttribVo.state = accountEntity.getState();
		retVo.roleAttribVo.createTime = String.valueOf(accountEntity.getCreateTime());
		retVo.roleAttribVo.createIP = accountEntity.getCreateIP();
		retVo.roleAttribVo.loginIP = accountEntity.getLoginIP();
		
		long inTime = accountEntity.getInTime();
		long outTime = accountEntity.getOutTime();
		if(inTime >= outTime){
			retVo.roleAttribVo.inTime = String.valueOf(inTime);
		}else{
			retVo.roleAttribVo.inTime = String.valueOf(outTime);
		}
		
		retVo.walletVo.diamond = String.valueOf(walletManager.getDiamond(accountEntity.getAccountId()));
		retVo.walletVo.goldMoney = String.valueOf(walletManager.getGoldMoney(accountEntity.getAccountId()));
		retVo.walletVo.roomCard = String.valueOf(walletManager.getRoomCard(accountEntity.getAccountId()));
		retVo.gameDataAttribVo = gameDataManager.getAccountGameDataClone(accountEntity.getAccountId());
		retVo.authenticationVo = authenticationManager.getAuthenticationData(accountEntity.getAccountId());
		return retVo;
	}
	
	public OtherAccountAttribVo Account2OtherAttrib(AccountEntity accountEntity){
		RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountEntity.getAccountId());
		OtherAccountAttribVo retVo = new OtherAccountAttribVo();
		retVo.headImg = roleEntity.getHeadImg();
		retVo.nick = roleEntity.getNick();
		retVo.sex = roleEntity.getSex();
		retVo.starNO = roleEntity.getStarNO();
		retVo.clientIP = accountEntity.getLoginIP();
		return retVo;
	}
	
	public void accountOnLine(AccountEntity accountEntity, IoSession session){
		long accountId = accountEntity.getAccountId();
		
		if (null != session) {
			IoSession oldSession = sessionManager.getSession(accountId);
			if (oldSession == null) {
				sessionManager.bind(session, accountId);
			} else {
				if (session != oldSession) {
					// 后面的人顶了前面的人,先关闭前面的连接
					@SuppressWarnings("rawtypes")
					Request pushMsg = Request.valueOf(AccountDefine.ACCOUNT_COMMAND_KICK_NOTIFY, 
							Result.valueOfSuccess("帐号已在其它设备登录"));
					sessionManager.send(pushMsg, oldSession);
					
					sessionManager.unBind(oldSession, accountId);
					oldSession.close(false);
					sessionManager.bind(session, accountId);
				}
			}
			//检测推送红点消息
			hotPromptManager.updateHotPromptAll(accountId);
			hotPromptManager.hotPromptNotity(accountId);
		}
		logicManager.lock();
		try{
			GameDataAttrib gameDataAttrib = gameDataManager.getAccountGameData(accountId);
			gameDataAttrib.onLine = 1;
			if(gameDataAttrib.tableId > 0){
				TableAttrib table = logicManager.getTable(gameDataAttrib.tableId);
				if(null != table){
					table.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_SEAT_STATE_NOTIFY);
				}else{
					gameDataAttrib.tableId = 0;
				}
			}
		}finally{
			logicManager.unlock();
		}		
		
		//更新登录日志信息
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		accountEntity.setInTime(currTime);
		accountEntity.setLoginIP(IpUtils.getIp(session));
		
		int type = 1;
		RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountId);
		EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_ACCOUNT_LOGIN);
		eventAttrib.addEventParam(roleEntity.getStarNO());
		eventAttrib.addEventParam(type);
		eventAttrib.addEventParam(currTime);
		eventTriggerManager.triggerEvent(eventAttrib);
	}
	
	public void accountOffLine(long accountId){
		logicManager.lock();
		try{
			GameDataAttrib gameDataAttrib = gameDataManager.getAccountGameData(accountId);
			gameDataAttrib.onLine = 0;
			if(gameDataAttrib.tableId > 0){
				TableAttrib table = logicManager.getTable(gameDataAttrib.tableId);
				if(null != table){
					SeatAttrib seat = table.getPlayerSeat(accountId);
					if(null == seat){
						//不在座位上,踢出玩家
						gameDataAttrib.tableId = 0;
						table.tablePlayers.remove(accountId);
					}else{
						//在座位上通知座位状态变化
						table.sendStateNotify(DzpkerDefine.DZPKER_COMMAND_SEAT_STATE_NOTIFY);
					}
				}else{
					gameDataAttrib.tableId = 0;
				}
			}
		}finally{
			logicManager.unlock();
		}
		
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		AccountEntity accountEntity = this.findOf_accountId(accountId);
		if(null == accountEntity){
			return;
		}
		accountEntity.setOutTime(currTime);
		
		int type = 0;
		RoleEntity roleEntity = roleEntityManager.findOf_accountId(accountId);
		EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_ACCOUNT_LOGIN);
		eventAttrib.addEventParam(roleEntity.getStarNO());
		eventAttrib.addEventParam(type);
		eventAttrib.addEventParam(currTime);
		eventTriggerManager.triggerEvent(eventAttrib);
		
		
		//Session关闭时从缓存中卸载实体,减少内存占用
		this.clearOfCache(accountId);
		mailManager.clearOfCache(accountId);
		walletManager.clearOfCache(accountId);
		hotPromptManager.clearOfCache(accountId);
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
			if(tmpNick.length() > 4){
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
	
	/**
	 * 检查是否可登录
	 * */
	public boolean canLogin(String uuid){
		if(disableAllLogin.getValue() > 0){
			if(uuid.isEmpty()){
				return false;
			}
			String content = disableLoginWhite.getContent();
			String[] whites = JsonUtils.string2Array(content, String.class);
			boolean bCanLogin = false;
			for (String white : whites) {
				if (white.equalsIgnoreCase(uuid)) {
					bCanLogin = true;
					break;
				}
			}
			return bCanLogin;
		}
		
		return true;
	}
}
