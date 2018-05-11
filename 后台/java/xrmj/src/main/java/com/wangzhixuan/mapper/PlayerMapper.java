package com.wangzhixuan.mapper;

import com.wangzhixuan.model.Player;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 玩家表 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
public interface PlayerMapper extends BaseMapper<Player> {

	List<Player> selectPlayerPage(Pagination page, Map<String, Object> params);
	
}