package com.palmjoys.yf1b.act.framework.account.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.entity.AuthenticationEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.manager.AuthenticationManager;
import com.palmjoys.yf1b.act.framework.account.manager.LoginManager;
import com.palmjoys.yf1b.act.framework.account.manager.StarNOManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.framework.account.model.AccountGmAttribVo;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

@Component
@ConsoleBean
public class AccountCommand {
	@Autowired
	private StarNOManager starNOManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private Querier querier; 
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private LoginManager loginManager;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	/**
	 * 创建机器人帐号
	 * start 开始位置
	 * num 创建数量
	 * */
	@ConsoleCommand(name = "gm_account_create_robot", description = "创建机器人帐号")
	public Object gm_account_create_robot(int start, int num){
		String uuid = "";
		String headImg = ""; 
		String nick = "";
		int sex = 1;
		
		for(int i=start; i<num; i++){
			String sNO = ""+i;
			if(i<10){
				sNO = "00"+i;
			}else if(i < 100){
				sNO = "0"+i;
			}
			uuid = "robot-" + sNO;
			int nHeadImg = (int) (Math.random()*1000%15 + 1);
			headImg = String.valueOf(nHeadImg);
			nick = accountManager.getRandomNickName();
			String clientIP = "127.0.0.1";
			String starNO = starNOManager.getStarNO();
			accountManager.create(uuid, "", starNO, headImg, sex, nick, clientIP, 1);
		}		
		return Result.valueOfSuccess();
	}
	
	/**
	 * 查询玩家帐号
	 * start 查询开始位置
	 * num 查询数量
	 * sortStyle 排序方式(1=小到大,2=大到小)
	 * sortType 排序(1=金币,2=时间)
	 * */
	@ConsoleCommand(name = "gm_account_query_account_list", description = "查询玩家帐号")
	public Object gm_account_query_account_list(int start, int num, int sortStyle, int sortType){
		if(start < 0){
			start = 0;
		}
		if(num <= 0){
			num = 50;
		}
		long totalNum = 0;
		String cntSql = "SELECT COUNT(A.accountId) FROM AuthenticationEntity AS A";
		String querySql = "SELECT A.phone, B.starNO, B.nick, B.state, B.createTime, C.outTime, D.roomCard"
				+ " FROM AuthenticationEntity AS A, AccountEntity AS B, LoginEntity AS C, WalletEntity AS D"
				+ " WHERE A.accountId=B.accountId AND A.accountId=C.accountId AND A.accountId=D.accountId"
				+ " ORDER BY ";
		
		String sAsc = "ASC";
		if(sortStyle == 2){
			sAsc = "DESC";
		}
		
		String sType = "D.roomCard ";
		if(sortType == 2){
			sType = "C.outTime ";
		}
		querySql += sType;
		querySql += sAsc;
		
		AccountGmAttribVo retVo = new AccountGmAttribVo();
		
		List<Object> retObjs = querier.listBySqlLimit(AuthenticationEntity.class, Object.class, cntSql, 0, 1);
		for(Object obj : retObjs){
			if(null != obj){
				if(obj instanceof Integer){
					totalNum = (Integer) obj;
				}else if(obj instanceof Long){
					totalNum = (Long) obj;
				}
			}
		}
		retVo.totalNum = (int) totalNum;
		
		retObjs = querier.listBySqlLimit(AccountEntity.class, Object.class, querySql, start, num);
		for(Object obj : retObjs){
			Object []objArry = (Object[]) obj;
			String phone = (String) objArry[0];
			String starNO = (String) objArry[1];
			String nick = (String) objArry[2];
			int state = (Integer) objArry[3];
			long createTime = (Long) objArry[4];
			long outTime = (Long) objArry[5];
			long roomCard = (long) objArry[6];
			retVo.addItem(starNO, nick, state, ""+createTime, roomCard, phone, ""+outTime);
		}
		return Result.valueOfSuccess(retVo);
	}
	
	/**
	 * 查询玩家
	 * */
	@ConsoleCommand(name = "gm_account_query_by_phone", description = "通过电话查询玩家帐号")
	public Object gm_account_query_by_phone(String phone){
		AccountEntity accountEntity = accountManager.findOf_phone(phone);
		if(null == accountEntity){
			return Result.valueOfError(-1, "未找到指定电话号码的玩家", null);
		}
		
		AccountGmAttribVo retVo = new AccountGmAttribVo();
		retVo.totalNum = 1;
		
		String starNO = accountEntity.getStarNO();
		String nick = accountEntity.getNick();
		int state = accountEntity.getState();
		long createTime = accountEntity.getCreateTime();
		long outTime = loginManager.loadOrCreate(accountEntity.getId()).getOutTime();
		long roomCard = walletManager.getRoomCard(accountEntity.getId());
		retVo.addItem(starNO, nick, state, ""+createTime, roomCard, phone, ""+outTime);
		
		return Result.valueOfSuccess(retVo);
	}
	
	/**
	 * 查询玩家
	 * */
	@ConsoleCommand(name = "gm_account_query_by_starNO", description = "通过明显号查询玩家帐号")
	public Object gm_account_query_by_starNO(String starNO){
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);
		if(null == accountEntity){
			return Result.valueOfError(-1, "未找到指定电话号码的玩家", null);
		}
		
		AccountGmAttribVo retVo = new AccountGmAttribVo();
		retVo.totalNum = 1;
		
		String phone = authenticationManager.loadOrCreate(accountEntity.getId()).getPhone();
		String nick = accountEntity.getNick();
		int state = accountEntity.getState();
		long createTime = accountEntity.getCreateTime();
		long outTime = loginManager.loadOrCreate(accountEntity.getId()).getOutTime();
		long roomCard = walletManager.getRoomCard(accountEntity.getId());
		retVo.addItem(starNO, nick, state, ""+createTime, roomCard, phone, ""+outTime);
		
		return Result.valueOfSuccess(retVo);
	}
	
	/**
	 * 获取客服信息
	 * */
	@ConsoleCommand(name = "gm_account_query_customerinfo", description = "获取客服信息")
	public Object gm_account_query_customerinfo(){
		String customer = starNOManager.getCustomer();
		return Result.valueOfSuccess(customer);
	}
	
	/**
	 * 设置客户信息
	 * customer 客户信息
	 * */
	@ConsoleCommand(name = "gm_account_set_customerinfo", description = "设置客服信息")
	public Object gm_account_set_customerinfo(String customer){
		starNOManager.setCustomer(customer);
		return Result.valueOfSuccess(customer);
	}
	
	/**
	 * 修改密码
	 * */
	@ConsoleCommand(name = "gm_account_modfiy_password", description = "修改密码")
	public Object gm_account_modfiy_password(String starNO, String newPassword){
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_ABNORMAL, "指定的帐号不存在", null);
		}
		accountEntity.setPassword(newPassword);
		return Result.valueOfSuccess();
	}
	
}
