package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Investment;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.ValidataBean;
import com.wangzhixuan.model.vo.InvestmentVo;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.mapper.InvestmentMapper;
import com.wangzhixuan.service.IInvestmentService;
import com.wangzhixuan.service.IJackpotService;
import com.wangzhixuan.service.IOperatingStatisticsService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 投资奖池 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
@Service
public class InvestmentServiceImpl extends ServiceImpl<InvestmentMapper, Investment> implements IInvestmentService {

	@Autowired
	private InvestmentMapper investmentMapper;
	@Autowired
	private IJackpotService jackpotService;
	@Autowired
	private IOperatingStatisticsService operatingStatisticsService;
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<InvestmentVo> list = investmentMapper.selectInvestmentVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
	}
	
	@Override
	public Result insertInvestment(Investment investment) {
		ValidataBean validata = investment.validateModel();
		if (!validata.isFlag()) {
			return new Result(false, validata.getMsg());
		}
		Result result = null;
		boolean success = this.insert(investment);
		/**每一笔投资都需要冲入奖池*/
		if(success) {
			result = jackpotService.supplementJackpot(investment);
		}
		/**每一笔投资都需要计入统计*/
		if(result != null && result.isSuccess()) {
			result = operatingStatisticsService.supplementOperating(investment);
		}
		return result;
	}

}
