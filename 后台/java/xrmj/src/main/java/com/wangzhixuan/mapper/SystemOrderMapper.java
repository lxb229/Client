package com.wangzhixuan.mapper;

import com.wangzhixuan.model.SystemOrder;
import com.wangzhixuan.model.vo.SystemOrderVo;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 系统订单 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public interface SystemOrderMapper extends BaseMapper<SystemOrder> {

	List<SystemOrderVo> selectSystemOrderVoPage(Pagination page, Map<String, Object> params);
	
}