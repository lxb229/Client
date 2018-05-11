package com.wangzhixuan.mapper;

import com.wangzhixuan.model.InsuranceLog;

import java.util.Date;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  * 保险记录 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
public interface InsuranceLogMapper extends BaseMapper<InsuranceLog> {

	
	/**
	 * 获取房间的保险流水总数：PS：如果playId不为空即查询该玩家在这个房间中的保险流水
	 * @param playId 玩家id
	 * @param houseOwner 房主id
	 * @param roomId 房间id
	 * @param createTime 房间创建时间
	 * @param roomDays 有效天数
	 * @return
	 */
	Map<String, Object> getInsuranceWater(@Param("playId")String playId, @Param("houseOwner")String houseOwner, @Param("roomId") int roomId, @Param("createTime") Date createTime, @Param("roomDays") String roomDays);
	
	
}