package com.wangzhixuan.mapper;

import com.wangzhixuan.model.SilverLog;
import com.wangzhixuan.model.vo.SilverLogVo;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 银币抽奖记录 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-11
 */
public interface SilverLogMapper extends BaseMapper<SilverLog> {

	List<SilverLogVo> selectSilverLogVoPage(Pagination page, Map<String, Object> params);
}