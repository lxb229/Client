package com.palmjoys.yf1b.act.zjh.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.zjh.entity.GameProfitPoolEntity;
import com.palmjoys.yf1b.act.zjh.manager.GameProfitPoolManager;
import com.palmjoys.yf1b.act.zjh.model.GMProfitPoolVo;

@Component
@ConsoleBean
public class ZjhCommand {
	@Autowired
	private GameProfitPoolManager profitPoolManager;
	/**
	 * 获取游戏赔率池子信息
	 * */
	@ConsoleCommand(name = "gm_game_get_profitpool", 
			description = "获取游戏赔率池子信息")
	Object gm_game_get_profitpool(){
		GMProfitPoolVo retVo = new GMProfitPoolVo();
		GameProfitPoolEntity entity = profitPoolManager.loadOrCreate();
		retVo.totalWinNum = String.valueOf(entity.getTotalWinNum());
		retVo.currWinNum = String.valueOf(entity.getCurrWinNum());
		retVo.minWinRate = entity.getMinWinRate();
		retVo.maxWinRate = entity.getMaxWinRate();
		retVo.mutiRatePower = entity.getMultitRatePower();
		retVo.betRatePower = entity.getBetRatePower();
		
		return Result.valueOfSuccess(retVo);
	}
	
	/**
	 * 设置游戏赔率池子
	 * totalWinNum 每日赢利数
	 * minWinRate 最小赢取概率
	 * maxWinRate 最大赢取概率
	 * mutiRatePower 倍率胜率因子
	 * betRatePower 下注胜率因子
	 * */
	@ConsoleCommand(name = "gm_game_set_profitpool", 
			description = "设置游戏赔率池子")
	Object gm_game_set_profitpool(int totalWinNum, int minWinRate, int maxWinRate,
			int mutiRatePower, int betRatePower){
		GameProfitPoolEntity entity = profitPoolManager.loadOrCreate();
		entity.setTotalWinNum(totalWinNum);
		entity.setMinWinRate(minWinRate);
		entity.setMaxWinRate(maxWinRate);
		entity.setMultitRatePower(mutiRatePower);
		entity.setBetRatePower(betRatePower);
		
		return Result.valueOfSuccess();
	}	
}
