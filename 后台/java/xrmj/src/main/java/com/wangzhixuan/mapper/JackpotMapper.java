package com.wangzhixuan.mapper;

import com.wangzhixuan.model.Jackpot;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 奖池 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public interface JackpotMapper extends BaseMapper<Jackpot> {
	
	List<Jackpot> selectJackpotPage(Pagination page, Map<String, Object> params);
}