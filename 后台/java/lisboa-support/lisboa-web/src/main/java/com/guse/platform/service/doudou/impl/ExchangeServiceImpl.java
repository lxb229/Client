package com.guse.platform.service.doudou.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.common.utils.HttpClientUtil;
import com.guse.platform.common.utils.PropertyConfigurer;
import com.guse.platform.dao.doudou.ExchangeMapper;
import com.guse.platform.service.doudou.ExchangeService;
import com.guse.platform.entity.doudou.Exchange;

/**
 * exchange
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class ExchangeServiceImpl extends BaseServiceImpl<Exchange, java.lang.Integer> implements ExchangeService{
	private static Logger logger = LoggerFactory.getLogger(ExchangeServiceImpl.class);
	@Autowired
	private ExchangeMapper  exchangeMapper;
	@Autowired
	private  PropertyConfigurer configurer;
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(exchangeMapper);
	}

	@Override
	public Result<PageResult<Exchange>> queryExchangeList(Exchange exchange, PageBean pageBean) {
		if (pageBean == null) {
			logger.info("获取兑换比例失败，pageBean is null ！");
			return new Result<PageResult<Exchange>>(00000, "获取兑换比例列表失败！");
		}
		String lisboaAddress = configurer.getProperty("lisboaAddress");
		String cmd="gm_order_get_exchanage_percent";
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
        		List<Exchange> list = new ArrayList<>();
        		Exchange jsonExchange = new Exchange(); 
        		jsonExchange.setId(1);
        		jsonExchange.setRmb2goldMoney(contentObj.getInteger("rmb2goldMoney"));
        		jsonExchange.setGoldMoney2Rmb(contentObj.getInteger("goldMoney2Rmb"));
        		jsonExchange.setMinRmb(contentObj.getInteger("minRmb"));
        		list.add(jsonExchange);
        		PageResult<Exchange> pageResult = null;
        		pageResult = new PageResult<>(pageBean.getPageNo(), pageBean.getPageSize(), count, "");
        		pageResult.setList(list);
        		return new Result<PageResult<Exchange>>(pageResult);
        		
            } else {
            	return new Result<PageResult<Exchange>>(00000,obj.getString("content")); 
            }
            
        } else {
        	return new Result<PageResult<Exchange>>(00000,"返回结果超时!");
        }
	}

	@Override
	public Result<Integer> updateExchange(Exchange exchange) {
		if (exchange == null) {
			logger.info("设置游戏赔率池子，profit is null ！");
			return new Result<Integer>(00000, "设置游戏赔率池子失败！");
		}
		String lisboaAddress = configurer.getProperty("lisboaAddress");
		String cmd = "gm_order_set_exchanage_percent";
		int rmb2goldMoney = exchange.getRmb2goldMoney();
		int goldMoney2Rmb = exchange.getGoldMoney2Rmb();
		int minRmb = exchange.getMinRmb();
		String result = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			result = HttpClientUtil.httpGet(lisboaAddress+cmd+"%20"+rmb2goldMoney+"%20"+goldMoney2Rmb+"%20"+minRmb);
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
}
