package com.wangzhixuan.mapper;

import com.wangzhixuan.model.SilverJackpot;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 银币抽奖奖池 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public interface SilverJackpotMapper extends BaseMapper<SilverJackpot> {
	
	List<SilverJackpot> selectSilverJackpotPage(Pagination page, Map<String, Object> params);
	
	/**
	 * 重置一个最新的银币抽奖
	 * @return
	 */
	int setSilverJackot();
	
	/**
	 * 获取当前银币奖池剩余
	 * @return
	 */
	List<SilverJackpot> getSilverJackpotResidue();

}