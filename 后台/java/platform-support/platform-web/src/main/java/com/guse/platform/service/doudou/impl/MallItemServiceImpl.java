package com.guse.platform.service.doudou.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.common.utils.HttpClientUtil;
import com.guse.platform.common.utils.PropertyConfigurer;
import com.guse.platform.dao.doudou.MallItemMapper;
import com.guse.platform.service.doudou.MallItemService;
import com.guse.platform.entity.doudou.MallItem;

/**
 * mall_item
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class MallItemServiceImpl extends BaseServiceImpl<MallItem, java.lang.Integer> implements MallItemService{
	
	private static Logger logger = LoggerFactory.getLogger(MallItemServiceImpl.class);
	
	@Autowired
	private  PropertyConfigurer configurer;
	@Autowired
	private MallItemMapper  mallItemMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(mallItemMapper);
	}
	
	private Gson gson = new Gson();

	@Override
	public Result<PageResult<MallItem>> queryMallItemList(MallItem mallItem, PageBean pageBean) {
		if (pageBean == null) {
			logger.info("获取商城配置失败，pageBean is null ！");
			return new Result<PageResult<MallItem>>(00000, "获取商城配置列表失败！");
		}
		String lisboaAddress = configurer.getProperty("ddmjAddress");
		String cmd="gm_mall_get_mall_list";
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
            	String mallItems = contentObj.getString("mallItems");
            	List<MallItem> list = gson.fromJson(mallItems, new TypeToken<List<MallItem>>(){}.getType());
        		PageResult<MallItem> pageResult = null;
        		pageResult = new PageResult<>(pageBean.getPageNo(), pageBean.getPageSize(), 10L, "");
        		pageResult.setList(list);
        		return new Result<PageResult<MallItem>>(pageResult);
        		
            } else {
            	return new Result<PageResult<MallItem>>(00000,obj.getString("content")); 
            }
            
        } else {
        	return new Result<PageResult<MallItem>>(00000,"返回结果超时!");
        }
	}

	@Override
	public Result<Integer> setMallItem(MallItem mallItem) {
		// TODO Auto-generated method stub
		return null;
	}
}
