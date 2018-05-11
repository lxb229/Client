package com.wangzhixuan.mapper;

import com.wangzhixuan.model.PropLog;
import com.wangzhixuan.model.vo.PropLogVo;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public interface PropLogMapper extends BaseMapper<PropLog> {
	
	List<PropLogVo> selectPropLogVoPage(Pagination page, Map<String, Object> params);
}