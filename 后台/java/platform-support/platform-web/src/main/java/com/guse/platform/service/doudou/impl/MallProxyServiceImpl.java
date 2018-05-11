package com.guse.platform.service.doudou.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.common.utils.HttpClientUtil;
import com.guse.platform.common.utils.PropertyConfigurer;
import com.guse.platform.dao.doudou.MallProxyMapper;
import com.guse.platform.service.doudou.MallProxyService;
import com.guse.platform.entity.doudou.MallProxy;

/**
 * mall_proxy
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class MallProxyServiceImpl extends BaseServiceImpl<MallProxy, java.lang.Integer> implements MallProxyService{

	private static Logger logger = LoggerFactory.getLogger(MallProxyServiceImpl.class);
	
	@Autowired
	private  PropertyConfigurer configurer;
	@Autowired
	private MallProxyMapper  mallProxyMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(mallProxyMapper);
	}
	
	private Gson gson = new Gson();

	@Override
	public List<MallProxy> getMallProxyList() {
		List<MallProxy> proxyList = null;
		
		String lisboaAddress = configurer.getProperty("ddmjAddress");
		String cmd="gm_mall_get_proxy_list";
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
            	String proxyItems = contentObj.getString("proxyItems");
            	proxyList = gson.fromJson(proxyItems, new TypeToken<List<MallProxy>>(){}.getType());
        		for (int i = 0; i < proxyList.size(); i++) {
					MallProxy proxy = proxyList.get(i);
					proxy.setId(i+1);
					proxyList.set(i, proxy);
				}
            	return proxyList;
            } else {
            	return null; 
            }
        } else {
        	return null;
        }
	}

	@Override
	public Result<PageResult<MallProxy>> queryMallProxyList(MallProxy mallProxy, PageBean pageBean) {
		if (pageBean == null) {
			logger.info("获取商城代理失败，pageBean is null ！");
			return new Result<PageResult<MallProxy>>(00000, "获取商城代理列表失败！");
		}
		
		List<MallProxy> list = getMallProxyList();
		if(list != null) {
			PageResult<MallProxy> pageResult = null;
			pageResult = new PageResult<>(pageBean.getPageNo(), pageBean.getPageSize(), 9L, "");
			pageResult.setList(list);
			return new Result<PageResult<MallProxy>>(pageResult);
		} else {
			return new Result<PageResult<MallProxy>>(00000,"返回结果超时!");
		}
	}

	@Override
	public Result<Integer> setMallProxy(MallProxy mallProxy, int type) throws UnsupportedEncodingException {
		List<MallProxy> list = getMallProxyList();
		switch (type) {
		// 新增商城代理
		case 1:
			if(list != null && list.size() < 3) {
				list.add(mallProxy);
			} else {
				return new Result<Integer>(00000,"不能超过3条数据!");
			}
			break;
		case 2:
			for (int i = 0; i < list.size(); i++) {
				MallProxy proxy = list.get(i);
				if(proxy.getId() == mallProxy.getId()) {
					list.set(i, mallProxy);
					break;
				}
			}
			break;
		case 3:
			for (int i = 0; i < list.size(); i++) {
				MallProxy proxy = list.get(i);
				if(proxy.getId() == mallProxy.getId()) {
					list.remove(i);
					break;
				}
			}
			break;
		default:
			break;
		}
		
		String lisboaAddress = configurer.getProperty("ddmjAddress");
		String cmd = "gm_mall_set_proxy_list";
		for (int i = 0; i < list.size(); i++) {
			MallProxy proxy = list.get(i);
			proxy.setId(null);
			list.set(i, proxy);
		}
		String jsonProxy = URLEncoder.encode(JSON.toJSONString(list), "UTF-8");
		String result = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			result = HttpClientUtil.httpGet(lisboaAddress+cmd+"%20"+jsonProxy);
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
	public Result<Integer> saveOrUpdateProxy(MallProxy mallProxy) {
		ValidataBean validata = mallProxy.validateModel();
		if(!validata.isFlag()){
			return new Result<Integer>(00000,validata.getMsg());
		}
		if(null != mallProxy.getId()){
			try {
				return setMallProxy(mallProxy, 2);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else{
			try {
				return setMallProxy(mallProxy, 1);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return new Result<Integer>(00000,"返回结果超时!");
	}

	@Override
	public Result<Integer> deleteProxy(MallProxy mallProxy) {
		if(null == mallProxy){
			return new Result<Integer>(00000,"删除商城代理失败，参数异常！");
		}
		try {
			return setMallProxy(mallProxy, 3);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new Result<Integer>(00000,"返回结果超时!");
	}
}
