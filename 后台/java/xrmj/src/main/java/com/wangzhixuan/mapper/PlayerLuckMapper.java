package com.wangzhixuan.mapper;

import com.wangzhixuan.model.PlayerLuck;
import com.wangzhixuan.model.vo.PlayerLuckVo;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 玩家幸运值 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-18
 */
public interface PlayerLuckMapper extends BaseMapper<PlayerLuck> {
	
	List<PlayerLuckVo> selectPlayerLuckVoPage(Pagination page, Map<String, Object> params);
	
}