package com.guse.platform.service.doudou.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.dao.doudou.GoldLogMapper;
import com.guse.platform.service.doudou.GoldLogService;
import com.guse.platform.service.doudou.SystemProfitService;
import com.guse.platform.entity.doudou.GoldLog;
import com.guse.platform.entity.doudou.SystemProfit;

/**
 * gold_log
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class GoldLogServiceImpl extends BaseServiceImpl<GoldLog, java.lang.Integer> implements GoldLogService{

	@Autowired
	private GoldLogMapper  goldLogMapper;
	@Autowired
	private SystemProfitService  profitService;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(goldLogMapper);
	}

	@Override
	public Result<Integer> saveOrUpdateGoldLog(String goldJsonStr) {
		JSONObject jsonObject = JSONObject.parseObject(goldJsonStr);
		GoldLog goldLog = new GoldLog();
		goldLog.setStarNo(jsonObject.getString("starNO"));
		goldLog.setTheEventTime(new Date(new Long(jsonObject.getString("eventTime"))));
		goldLog.setScore(jsonObject.getInteger("score"));
		goldLog.setGameType(jsonObject.getInteger("gameType"));
		
		ValidataBean validata = goldLog.validateModel();
		if(!validata.isFlag()){
			return new Result<Integer>(00000,validata.getMsg());
		}
		
		Integer result = null;
		if(null != goldLog.getId()){
			result = goldLogMapper.updateByIdSelective(goldLog);
		}else{
			result = goldLogMapper.insert(goldLog);
		}
		return new Result<Integer>(result);
	}

	@Override
	public Result<List<GoldLog>> queryList(GoldLog goldLog) {
		List<GoldLog> goldLogList = goldLogMapper.select(goldLog);
		return new Result<List<GoldLog>>(goldLogList);
	}

	@Override
	public Result<Map<String, Object>> countAmount(GoldLog goldLog) {
		// 统计订单数据
		Map<String, Object> orderMap = goldLogMapper.countProfit(goldLog);
		boolean isnow = false;
		if(goldLog == null) {
			isnow = true;
		} else if(goldLog.getLuckEnd() == null && goldLog.getLuckStart() == null) {
			isnow = true;
		} else if(goldLog.getLuckEnd() != null && goldLog.getLuckEnd().getTime() > new Date().getTime()) {
			isnow = true;
		} else if(goldLog.getLuckStart() !=null && goldLog.getLuckStart().getTime() < new Date().getTime()) {
			isnow = true;
		}
		if(isnow) {
			SystemProfit profit = new SystemProfit();
			PageBean pageBean = new PageBean();
			pageBean.setPageNo(1);
			pageBean.setPageSize(10);
			Result<PageResult<SystemProfit>> profitPage = profitService.queryProfitList(profit, pageBean);
			profit = profitPage.getData().getList().get(0);
			int orderAmount = Integer.parseInt(orderMap.get("orderAmount").toString())+profit.getCurrWinNum();
			int agencyAmount = Integer.parseInt(orderMap.get("agencyAmount").toString())+profit.getCurrWinNum();
			orderMap.put("orderAmount", orderAmount);
			orderMap.put("agencyAmount", agencyAmount);
		}
		return new Result<Map<String, Object>>(orderMap);
	}

	@Override
	public boolean isNow(Date date) {
		//当前时间
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        //获取今天的日期
        String nowDay = sf.format(now);
        //对比的时间
        String day = sf.format(date);
        return day.equals(nowDay);
	}
}
