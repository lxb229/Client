package com.wangzhixuan.mapper;

import com.wangzhixuan.model.Investment;
import com.wangzhixuan.model.vo.InvestmentVo;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 投资奖池 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public interface InvestmentMapper extends BaseMapper<Investment> {
	
	List<InvestmentVo> selectInvestmentVoPage(Pagination page, Map<String, Object> params);

}