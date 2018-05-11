package com.palmjoys.yf1b.act.account.command;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.network.filter.session.SessionManager;
import org.treediagram.nina.network.model.Request;
import org.treediagram.nina.network.model.Result;
import com.palmjoys.yf1b.act.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.account.entity.RoleEntity;
import com.palmjoys.yf1b.act.account.manager.AccountManager;
import com.palmjoys.yf1b.act.account.manager.RoleEntityManager;
import com.palmjoys.yf1b.act.account.model.AccountAttribVo;
import com.palmjoys.yf1b.act.account.model.AccountDefine;
import com.palmjoys.yf1b.act.account.model.AccountGmVo;

@Component
@ConsoleBean
public class AccountCommand {
	@Autowired
	private Querier querier;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private RoleEntityManager roleManager;
	@Autowired
	private SessionManager sessionManager;
	
	
	/**
	 * 查询帐号列表
	 * start 开始位置
	 * num 查询数量
	 * sortType 排序类型(1=升序,2=降序)
	 * sortCondition 排序条件(1=性别,2=帐号状态,3=创建时间,4=房卡,5=金币,6=钻石,默认=6钻石排序)
	 * match 匹配类型,数组下标位置值>0表示有匹配值(0=明星号,1=角色呢称,2=创建时间)
	 * matchParam 匹配参数值(0=明星号,1=角色呢称,2=查询开始时间,查询结束时间)
	 * */
	@ConsoleCommand(name = "gm_account_query", description = "查询玩家帐号数据")
	public Object gm_account_query(int start, int num, int sortType, int sortCondition,
			int []match, Object []matchParam){
		if(match.length != 3){
			return Result.valueOfError(-1, "匹配类型参数错误", null);
		}		
		
		AccountGmVo retVo = new AccountGmVo();
		
		if(start < 0){
			start = 0;
		}
		if(num <= 0){
			num = 50;
		}
		String sortTypeSql = "ASC"; 
		if(sortType == 2){
			sortTypeSql = "DESC";
		}
				
		String cntSql = "";
		String querySql = "";
		String filterSql = "";
		String sortSql = "";
		if(sortCondition == 1){
			//性别排序
			sortSql = " ORDER BY B.sex " + sortTypeSql;
		}else if(sortCondition == 2){
			//帐号状态
			sortSql = " ORDER BY A.state " + sortTypeSql;
		}else if(sortCondition == 3){
			//创建时间
			sortSql = " ORDER BY A.createTime " + sortTypeSql;
		}else if(sortCondition == 4){
			//房卡
			sortSql = " ORDER BY C.roomCard " + sortTypeSql;
		}else if(sortCondition == 5){
			//金币
			sortSql = " ORDER BY C.goldMoney " + sortTypeSql;
		}else{
			//钻石
			sortSql = " ORDER BY C.diamond " + sortTypeSql;
		}		
		int paramIndex = 0;
		for(int i=0; i<match.length; i++){
			if(match[i] > 0){
				switch(i){
				case 0://明星号
					filterSql += " AND B.starNO like '%" + matchParam[paramIndex] + "%'";
					paramIndex++;
					break;
				case 1://角色呢称
					filterSql += " AND B.nick like '%" + matchParam[paramIndex] + "%'";
					paramIndex++;
					break;
				case 2://创建时间
					filterSql += " AND A.createTime>=" + matchParam[paramIndex];
					paramIndex++;
					filterSql += " AND A.createTime<" + matchParam[paramIndex];
					paramIndex++;
					break;
				}
			}
		}
		
		querySql = "SELECT A.accountId FROM AccountEntity AS　A, RoleEntity AS B, WalletEntity AS C"
				+ " WHERE A.accountId=B.accountId AND A.accountId=C.accountId" + filterSql;
		cntSql = "SELECT COUNT(A.accountId) FROM AccountEntity AS　A, RoleEntity AS B, WalletEntity AS C"
				+ " WHERE A.accountId=B.accountId AND A.accountId=C.accountId" + filterSql;
		querySql += sortSql;
		
		List<Object> retObjects = querier.listBySqlLimit(AccountEntity.class, Object.class, cntSql, 0, 1);
		for(Object obj : retObjects){
			if(null != obj){
				if(obj instanceof Long){
					Long tmpObj = (Long) obj;
					retVo.totalNum = tmpObj.intValue();
				}else if(obj instanceof Integer){
					retVo.totalNum = (Integer)obj;
				}
				break;
			}
		}
		
		retObjects = querier.listBySqlLimit(AccountEntity.class, Object.class, querySql, start, num);
		for(Object obj : retObjects){
			Long accountId = (Long) obj;
			AccountEntity accountEntity = accountManager.findOf_accountId(accountId);
			if(null != accountEntity){
				AccountAttribVo attribVo = accountManager.Account2AccountAttrib(accountEntity);
				retVo.items.add(attribVo);
			}
		}
		
		return Result.valueOfSuccess(retVo);
	}
	
	/**
	 * 修改帐号状态
	 * starNO 明星号
	 * state 状态(0=正常,-1=冻结)
	 * */
	@ConsoleCommand(name = "gm_account_modfiy_state", description = "修改帐号状态")
	public Object gm_account_modfiy_state(String starNO, int state){
		if(state != 0 && state != -1){
			return Result.valueOfError(-1, "命令接口参数错误", null);
		}
		RoleEntity roleEntity = roleManager.findOf_starNO(starNO);
		if(null == roleEntity){
			return Result.valueOfError(-1, "未找到指定玩家", null);
		}
		AccountEntity accountEntity = accountManager.findOf_accountId(roleEntity.getAccountId());
		if(null == accountEntity){
			return Result.valueOfError(-1, "未找到指定玩家", null);
		}
		accountEntity.setState(state);
		if(state == -1){
			IoSession ioSession = sessionManager.getSession(accountEntity.getAccountId());
			if(null != ioSession){
				@SuppressWarnings("rawtypes")
				Request pushMsg = Request.valueOf(AccountDefine.ACCOUNT_COMMAND_KICK_NOTIFY, 
						Result.valueOfSuccess("帐号已禁止登录"));
				sessionManager.send(pushMsg, ioSession);
			}
		}
		return Result.valueOfSuccess();
	}
}
