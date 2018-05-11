package com.wangzhixuan.mapper;

import com.wangzhixuan.model.LoginLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  * 玩家登陆日志 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
public interface LoginLogMapper extends BaseMapper<LoginLog> {
	/**
	 * 获取玩家连续登录天数 Ps：playeId不为空查询指定玩家的数据； createDate不为空时，查询玩家2日、3日、7日存留
	 * @param playeId 玩家id
	 * @param createDate 注册时间
	 * @return
	 */
	List<Map<String, Object>> getContinuousLogin(@Param("playId") String playId, @Param("createDate") Date createDate);
}