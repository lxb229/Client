package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.Lottery;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 百轮抽奖 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public interface ILotteryService extends IService<Lottery> {
	
	/**
	 * 重置一个最新的百轮抽奖
	 * @return
	 */
	Result setNewLottery();
	
	/**
	 * 清空指定id集合的奖项
	 * @param idList
	 * @return
	 */
	Result emptyLottery(List<Integer> idList);
	
	/**
	 * 更新已经被领取的奖项
	 * @param idList
	 * @return
	 */
	Result updateLottery(List<Integer> lvList);
	
	/**
	 * 获取符合对应祝福值的剩余的百轮奖池
	 * @return
	 */
	List<Lottery> getWishLvLottery(List<Integer> lvList);
	
	/**
	 * 获取符合对应祝福值的剩余的百轮奖池
	 * @return
	 */
	List<Lottery> getWishLvLottery(Integer wishLv);
	
	/**
	 * 获取符合对应祝福值的剩余的百轮奖池
	 * @return
	 */
	List<Lottery> getAwardsLvLottery(Integer awardsLv);
	
	/**
	 * 获取指定等级的剩余的百轮抽奖
	 * @return
	 */
	Lottery getLvLottery(Integer awardsLv);
	
	/**
	 * 抽奖获取的奖池,祝福值不足100的抽法
	 * @param wishLv 祝福值
	 * @param type 查询方式(>0向下查询抽奖 <0向上查询抽奖)
	 * @param number 查询次数
	 * @param receiveNumber 抽奖次数
	 * @param awardList 返回的抽奖结果集
	 * @return
	 */
	List<Integer> getAwardLottery(Integer wishLv, Integer type, int number, Integer receiveNumber, List<Integer> awardList);
	
	/**
	 * 抽奖获取的奖池,祝福值满100的抽法
	 * @param awardsLv 奖品等级，祝福值满100的抽奖是从一等奖依次往下扫荡
	 * @param number 查询次数
	 * @param receiveNumber 抽奖次数
	 * @param awardList 返回的抽奖结果集
	 * @return
	 */
	List<Integer> getLuckyLottery(Integer awardsLv, int number, Integer receiveNumber, List<Integer> awardList);
	
	
	/**
	 * 随机抽奖
	 * @param list 抽奖奖池
	 * @param receiveNumber 抽奖次数
	 * @return
	 */
	List<Integer> randomLottery(List<Lottery> list, Integer receiveNumber);
	
	/**
	 * 抽奖-全部领取奖池
	 * @param list 抽奖奖池
	 * @return
	 */
	List<Integer> randomLottery(List<Lottery> list);
	
	/**
	 * 获取百轮抽奖奖池的剩余数量
	 * @param list 抽奖奖池
	 * @return
	 */
	Integer getLotteryResidue(List<Lottery> list);
}
