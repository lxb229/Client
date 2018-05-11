package com.wangzhixuan.mapper;

import com.wangzhixuan.model.Lottery;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  * 百轮抽奖 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public interface LotteryMapper extends BaseMapper<Lottery> {

	/**
	 * 重置一个最新的百轮抽奖
	 * @return
	 */
	int setNewLottery();
	
	/**
	 * 清空指定id集合的奖项
	 * @param idList
	 * @return
	 */
	int emptyLottery(@Param("idList")List<Integer> idList);
	
	/**
	 * 减少奖池指定等级奖项数量
	 * @param awardsLv 指定奖项等级
	 * @param number 减少数量
	 * @return
	 */
	int lessenLottery(@Param("awardsLv")Integer awardsLv,@Param("number")Integer number);
	
	/**
	 * 获取对应祝福值对应的剩余的百轮抽奖
	 * @return
	 */
	List<Lottery> getWishLvLottery(@Param("lvList")List<Integer> lvList);
	
	/**
	 * 获取指定等级的剩余的百轮抽奖
	 * @return
	 */
	List<Lottery> getAwardsLvLottery(@Param("awardsLv")Integer awardsLv);
	
	/**
	 * 获取指定等级的剩余的百轮抽奖
	 * @return
	 */
	Lottery getLvLottery(@Param("awardsLv")Integer awardsLv);
	
}