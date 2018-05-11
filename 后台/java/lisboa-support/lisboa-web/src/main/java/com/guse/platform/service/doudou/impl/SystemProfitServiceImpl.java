package com.guse.platform.service.doudou.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.common.utils.HttpClientUtil;
import com.guse.platform.common.utils.PropertyConfigurer;
import com.guse.platform.dao.doudou.SystemProfitMapper;
import com.guse.platform.service.doudou.SystemProfitService;
import com.guse.platform.entity.doudou.SystemProfit;

/**
 * system_profit
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class SystemProfitServiceImpl extends BaseServiceImpl<SystemProfit, java.lang.Integer> implements SystemProfitService{
	private static Logger logger = LoggerFactory.getLogger(SystemProfitServiceImpl.class);
	@Autowired
	private SystemProfitMapper  systemProfitMapper;
	@Autowired
	private  PropertyConfigurer configurer;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(systemProfitMapper);
	}

	@Override
	public Result<Integer> updateProfit(SystemProfit profit) {
		if (profit == null) {
			logger.info("设置游戏赔率池子，profit is null ！");
			return new Result<Integer>(00000, "设置游戏赔率池子失败！");
		}
		String lisboaAddress = configurer.getProperty("lisboaAddress");
		String cmd = "gm_game_set_profitpool";
		int totalWinNum = profit.getProfit();
		int minWinRate = profit.getMinPercentum();
		int maxWinRate = profit.getMaxPercentum();
		int mutiRatePower = profit.getMutiRatePower();
		int betRatePower = profit.getBetRatePower();
		String result = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			result = HttpClientUtil.httpGet(lisboaAddress+cmd+"%20"+totalWinNum+"%20"+minWinRate+"%20"+maxWinRate+"%20"+mutiRatePower+"%20"+betRatePower);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
		}
		if (result != null && result != "") {
			 //JSON格式转换
           JSONObject obj = JSONObject.parseObject(result);
           int success = obj.getIntValue("code");
           if(success == 0) {
	       		return new Result<Integer>(success);
           } else {
           	return new Result<Integer>(00000,obj.getString("content")); 
           }
       } else {
       	return new Result<Integer>(00000,"返回结果超时!");
       }
	}

	@Override
	public Result<PageResult<SystemProfit>> queryProfitList(SystemProfit profit, PageBean pageBean) {
		if (pageBean == null) {
			logger.info("获取游戏赔率池子失败，pageBean is null ！");
			return new Result<PageResult<SystemProfit>>(00000, "获取赔率池子列表失败！");
		}
		String lisboaAddress = configurer.getProperty("lisboaAddress");
		String cmd="gm_game_get_profitpool";
		lisboaAddress = lisboaAddress+cmd;
		
		String result = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			result = HttpClientUtil.httpGet(lisboaAddress);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
		}
		if (result != null && result != "") {
			 //JSON格式转换
            JSONObject obj = JSONObject.parseObject(result);
            int success = obj.getIntValue("code");
            if(success == 0) {
            	String content = obj.getString("content");
            	JSONObject contentObj = JSONObject.parseObject(content);
            	Long count = 1L;
        		List<SystemProfit> list = new ArrayList<>();
        		SystemProfit jsonProfit = new SystemProfit(); 
        		jsonProfit.setId(1);
        		jsonProfit.setProfit(contentObj.getInteger("totalWinNum"));
        		jsonProfit.setCurrWinNum(contentObj.getInteger("currWinNum"));
        		jsonProfit.setMinPercentum(contentObj.getInteger("minWinRate"));
        		jsonProfit.setMaxPercentum(contentObj.getInteger("maxWinRate"));
        		jsonProfit.setMutiRatePower(contentObj.getInteger("mutiRatePower"));
        		jsonProfit.setBetRatePower(contentObj.getInteger("betRatePower"));
        		list.add(jsonProfit);
        		PageResult<SystemProfit> pageResult = null;
        		pageResult = new PageResult<>(pageBean.getPageNo(), pageBean.getPageSize(), count, "");
        		pageResult.setList(list);
        		return new Result<PageResult<SystemProfit>>(pageResult);
        		
            } else {
            	return new Result<PageResult<SystemProfit>>(00000,obj.getString("content")); 
            }
            
        } else {
        	return new Result<PageResult<SystemProfit>>(00000,"返回结果超时!");
        }
	}
	
}
