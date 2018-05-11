package com.guse.platform.service.doudou.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.dao.doudou.GameActivityRotorDetailMapper;
import com.guse.platform.service.doudou.GameActivityRotorDetailService;
import com.guse.platform.entity.doudou.GameActivityRotorDetail;

/**
 * game_activity_rotor_detail
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class GameActivityRotorDetailServiceImpl extends BaseServiceImpl<GameActivityRotorDetail, java.lang.String> implements GameActivityRotorDetailService{
	private static final Logger logger = LoggerFactory.getLogger(GameActivityRotorDetailServiceImpl.class);
	@Autowired
	private GameActivityRotorDetailMapper  gameActivityRotorDetailMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(gameActivityRotorDetailMapper);
	}

	@Override
	public Result<PageResult<GameActivityRotorDetail>> queryLuckyWheelDetailPageList(GameActivityRotorDetail gameActivityRotorDetail, PageBean pageBean) {
		Long count = gameActivityRotorDetailMapper.countByParam(gameActivityRotorDetail);
		
		if (count <= 0 ) {
			return new Result<PageResult<GameActivityRotorDetail>>(new PageResult<GameActivityRotorDetail>().initNullPage());
		}

		List<GameActivityRotorDetail> list = gameActivityRotorDetailMapper.selectPageByParam(gameActivityRotorDetail,
				new PageResult<GameActivityRotorDetail>(pageBean.getPageNo(), pageBean.getPageSize(), count, "gard_prize_time"));
		PageResult<GameActivityRotorDetail> pageResult = new PageResult<GameActivityRotorDetail>(pageBean.getPageNo(),
				pageBean.getPageSize(), count, "");
		
		pageResult.setList(list);
		return new Result<PageResult<GameActivityRotorDetail>>(pageResult);
	}
}
