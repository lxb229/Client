package com.wangzhixuan.mapper;

import com.wangzhixuan.model.BetLog;

import java.util.Date;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  * 下注记录 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
public interface BetLogMapper extends BaseMapper<BetLog> {
	
	/**
	 * 获取房间的流水总数：PS：如果playId不为空即查询该玩家在这个房间中的流水
	 * @param playId 玩家id
	 * @param houseOwner 房主id
	 * @param roomId 房间id
	 * @param createTime 房间创建时间
	 * @param roomDays 有效天数
	 * @return
	 */
	Map<String, Object> getFinancialWater(@Param("playId")String playId, @Param("houseOwner")String houseOwner, @Param("roomId") int roomId, @Param("createTime") Date createTime, @Param("roomDays") String roomDays);
	
	/**
	 * 获取流水数据汇总
	 * @return
	 */
	Map<String, Object> selectBetData();
	
}