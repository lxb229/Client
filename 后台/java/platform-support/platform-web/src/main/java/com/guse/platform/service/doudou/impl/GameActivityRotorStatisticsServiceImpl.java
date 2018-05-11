package com.guse.platform.service.doudou.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.common.utils.DateUtils;
import com.guse.platform.common.utils.ServiceUtils;
import com.guse.platform.dao.doudou.GameActivityRotorStatisticsMapper;
import com.guse.platform.service.doudou.GameActivityRotorStatisticsService;
import com.guse.platform.service.task.GameActivityRotorStatisticsTask;
import com.guse.platform.vo.QueryVo;
import com.guse.platform.entity.doudou.GameActivityRotorStatistics;
import com.guse.platform.entity.system.Users;

/**
 * game_activity_rotor_statistics
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class GameActivityRotorStatisticsServiceImpl extends BaseServiceImpl<GameActivityRotorStatistics, java.lang.Long> implements GameActivityRotorStatisticsService{

	private static final Logger logger = LoggerFactory.getLogger(GameActivityRotorStatisticsServiceImpl.class);
	@Autowired
	private GameActivityRotorStatisticsMapper gameActivityRotorStatisticsMapper;
	@Autowired
	private GameActivityRotorStatisticsTask gameActivityRotorStatisticsTask;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(gameActivityRotorStatisticsMapper);
	}

	@Override
	public Result<PageResult<GameActivityRotorStatistics>> queryLuckyWheelPageList(PageBean pageBean, Users user) {
		// 今日实时统计数据
		GameActivityRotorStatistics today = gameActivityRotorStatisticsTask.getGameActivityRotorStatistics(DateUtils.formatCurrentDate(DateUtils.DATE_FORMAT_YYYYMMDD));
		
		if (pageBean == null) {
			logger.info("幸运转盘统计获取失败，pageBean is null ！");
			return new Result<PageResult<GameActivityRotorStatistics>>(00000, "幸运转盘统计信息获取失败！");
		}
		GameActivityRotorStatistics gameActivityRotorStatistics = new GameActivityRotorStatistics();
		//ServiceUtils.initQueryDateterms(query, gameDzpkRoom, 1, null);
		Long count = gameActivityRotorStatisticsMapper.countByParam(gameActivityRotorStatistics, user);
		
		if (count <= 0 && today==null) {
			return new Result<PageResult<GameActivityRotorStatistics>>(new PageResult<GameActivityRotorStatistics>().initNullPage());
		}

		List<GameActivityRotorStatistics> list = gameActivityRotorStatisticsMapper.selectPageByParam(gameActivityRotorStatistics,
				new PageResult<GameActivityRotorStatistics>(pageBean.getPageNo(), pageBean.getPageSize(), count, "gars_date"), user);
		PageResult<GameActivityRotorStatistics> pageResult = new PageResult<GameActivityRotorStatistics>(pageBean.getPageNo(),
				pageBean.getPageSize(), count, "");
		
		if (today!=null) {
			list.add(0, today);
		}else{
			pageResult.setList(list);
		}
		return new Result<PageResult<GameActivityRotorStatistics>>(pageResult);
	}
}
