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
import com.guse.platform.dao.doudou.WechatMapper;
import com.guse.platform.service.doudou.WechatService;
import com.guse.platform.entity.doudou.Wechat;

/**
 * wechat
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class WechatServiceImpl extends BaseServiceImpl<Wechat, java.lang.Integer> implements WechatService{
	private static Logger logger = LoggerFactory.getLogger(SystemProfitServiceImpl.class);
	@Autowired
	private  PropertyConfigurer configurer;
	@Autowired
	private WechatMapper  wechatMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(wechatMapper);
	}

	@Override
	public Result<PageResult<Wechat>> queryWechatList(Wechat wechat, PageBean pageBean) {
		if (pageBean == null) {
			logger.info("获取客服信息失败，pageBean is null ！");
			return new Result<PageResult<Wechat>>(00000, "获取客服信息失败！");
		}
		String lisboaAddress = configurer.getProperty("lisboaAddress");
		String cmd="gm_account_query_customerinfo";
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
            	Long count = 1L;
        		List<Wechat> list = new ArrayList<>();
        		Wechat chat = new Wechat(); 
        		chat.setId(1);
        		chat.setWechat(obj.getString("content"));
        		list.add(chat);
        		PageResult<Wechat> pageResult = null;
        		pageResult = new PageResult<>(pageBean.getPageNo(), pageBean.getPageSize(), count, "");
        		pageResult.setList(list);
        		return new Result<PageResult<Wechat>>(pageResult);
        		
            } else {
            	return new Result<PageResult<Wechat>>(00000,obj.getString("content")); 
            }
            
        } else {
        	return new Result<PageResult<Wechat>>(00000,"返回结果超时!");
        }
	}

	@Override
	public Result<Integer> updateWechat(Wechat wechat) {
		if (wechat == null) {
			logger.info("设置客服信息，wechat is null ！");
			return new Result<Integer>(00000, "设置客服信息失败！");
		}
		String lisboaAddress = configurer.getProperty("lisboaAddress");
		String cmd = "gm_account_set_customerinfo";
		String customer = wechat.getWechat();
		String result = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			result = HttpClientUtil.httpGet(lisboaAddress+cmd+"%20"+customer);
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
