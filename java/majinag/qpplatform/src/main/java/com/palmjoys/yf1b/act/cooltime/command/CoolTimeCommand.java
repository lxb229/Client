package com.palmjoys.yf1b.act.cooltime.command;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.cooltime.entity.PlayerCoolTimeEntity;
import com.palmjoys.yf1b.act.cooltime.entity.SysCoolTimeEntity;
import com.palmjoys.yf1b.act.cooltime.manger.PlayerCoolTimeManager;
import com.palmjoys.yf1b.act.cooltime.manger.SysCoolTimeManager;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;

@Component
@ConsoleBean
public class CoolTimeCommand {
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private PlayerCoolTimeManager playerCoolTimeManager;
	@Autowired
	private SysCoolTimeManager sysCoolTimeManager;
	
	/**
	 * 重置指定角色指定功能时间点
	 * starNO 玩家明星号
	 * resetId 时间功能Id
	 * resetTime 重设到的时间点(毫秒)
	 * */
	@ConsoleCommand(name = "gm_cooltime_set_player_cooltime", description = "重置指定角色指定功能时间点")
	public Object gm_cooltime_set_player_cooltime(String starNO, int resetId, long resetTime){
		if(resetTime < 0){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "参数错误", null);
		}
		
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "未找到指定玩家", null);
		}
		
		PlayerCoolTimeEntity playerCoolTimeEntity = playerCoolTimeManager.loadOrCreate(accountEntity.getId());
		Map<Integer, Long> resetTimeMap = playerCoolTimeEntity.getResetTimeMap(); 
		Long time = resetTimeMap.get(resetId);
		if(null == time){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "参数错误", null);
		}
		resetTimeMap.put(resetId, resetTime);
		playerCoolTimeEntity.setResetTimeMap(resetTimeMap);
		
		return Result.valueOfSuccess();
	}
	
	/**
	 * 重置系统指定功能时间点
	 * resetId 时间功能Id
	 * resetTime 重设到的时间点(毫秒)
	 * */
	@ConsoleCommand(name = "gm_cooltime_set_sys_cooltime", description = "重置指定角色指定功能时间点")
	public Object gm_cooltime_set_sys_cooltime(int resetId, long resetTime){
		if(resetTime < 0){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "参数错误", null);
		}
		SysCoolTimeEntity sysCoolTimeEntity = sysCoolTimeManager.loadOrCreate();
		Map<Integer, Long> resetTimeMap = sysCoolTimeEntity.getResetTimeMap(); 
		Long time = resetTimeMap.get(resetId);
		if(null == time){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "参数错误", null);
		}
		resetTimeMap.put(resetId, resetTime);
		sysCoolTimeEntity.setResetTimeMap(resetTimeMap);
		
		return Result.valueOfSuccess();
	}
}
