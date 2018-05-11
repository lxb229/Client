package com.wangzhixuan.mapper;

import com.wangzhixuan.model.RedPacketLog;
import com.wangzhixuan.model.vo.RedPacketLogVo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 红包抽奖记录 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-27
 */
public interface RedPacketLogMapper extends BaseMapper<RedPacketLog> {
	
	
	List<RedPacketLogVo> selectRedPacketLogVoPage(Pagination page, Map<String, Object> params);
	/**
	 * 获取该玩家指定轮数的指定号数的红包
	 * @param startNo 玩家明星号
	 * @param redPacketNo 红包号数
	 * @param round 轮数
	 * @return
	 */
	RedPacketLog getAlreadyRed(@Param("startNo")String startNo, @Param("redPacketNo")int redPacketNo, @Param("round")Integer round);
	
	/**
	 * 获取玩家指定轮数的红包记录
	 * @param startNo 玩家明星号
	 * @param round 轮数
	 * @return
	 */
	List<RedPacketLog> getRedByRound(@Param("startNo")String startNo,@Param("round") Integer round);
}