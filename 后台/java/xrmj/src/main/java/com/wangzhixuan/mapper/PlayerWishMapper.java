package com.wangzhixuan.mapper;

import com.wangzhixuan.model.PlayerWish;
import com.wangzhixuan.model.vo.PlayerWishVo;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 玩家祝福值 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public interface PlayerWishMapper extends BaseMapper<PlayerWish> {
	
	List<PlayerWishVo> selectPlayerWishVoPage(Pagination page, Map<String, Object> params);
	
	/**
	 * 清空玩家祝福值
	 * @return
	 */
	int clearWish();

}