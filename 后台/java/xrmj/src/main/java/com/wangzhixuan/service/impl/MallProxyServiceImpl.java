package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.MallProxy;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.ValidataBean;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.HttpClientUtil;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.mapper.MallProxyMapper;
import com.wangzhixuan.service.IMallProxyService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商城代理微信 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-19
 */
@Service
public class MallProxyServiceImpl extends ServiceImpl<MallProxyMapper, MallProxy> implements IMallProxyService {

	private static Logger logger = LoggerFactory.getLogger(MallProxyServiceImpl.class);
	private Gson gson = new Gson();
	@Autowired
	private  PropertyConfigurer configurer;
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<MallProxy> list = this.getMallProxyList();
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
	}

	@Override
	public List<MallProxy> getMallProxyList() {
		
		List<MallProxy> proxyList = null;
		
		String lisboaAddress = configurer.getProperty("ddmjAddress");
		String cmd="gm_mall_get_proxy_list";
		lisboaAddress = lisboaAddress+cmd;
		
		String result = null;
		try {
			if(StringUtils.isBlank(lisboaAddress)){
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
	public Result setMallProxy(MallProxy mallProxy, Integer type) throws UnsupportedEncodingException {
		List<MallProxy> list = getMallProxyList();
		switch (type) {
			/**
			 *  新增商城代理
			 */
			case 1:
				if(list != null && list.size() < 3) {
					list.add(mallProxy);
				} else {
					return new Result(false,"不能超过3条数据!");
				}
				break;
			/**
			 * 编辑商城代理
			 */
			case 2:
				for (int i = 0; i < list.size(); i++) {
					MallProxy proxy = list.get(i);
					if(proxy.getId() == mallProxy.getId()) {
						list.set(i, mallProxy);
						break;
					}
				}
				break;
			/**
			 * 删除商城代理
			 */
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
		/**
		 * 通知游戏服务器
		 */
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
	       		return new Result("OK");
           } else {
           		return new Result(false,obj.getString("content")); 
           }
           
       } else {
    	   return new Result(false,"返回结果超时!");
       }
	}

	@Override
	public Result addMallProxy(MallProxy mallProxy) {
		ValidataBean validata = mallProxy.validateModel();
		if(!validata.isFlag()){
			return new Result(false,validata.getMsg());
		}
		try {
			return setMallProxy(mallProxy, 1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Result updateMallProxy(MallProxy mallProxy) {
		ValidataBean validata = mallProxy.validateModel();
		if(!validata.isFlag()){
			return new Result(false,validata.getMsg());
		}
		try {
			return setMallProxy(mallProxy, 2);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Result deleteMallProxy(MallProxy mallProxy) {
		try {
			return setMallProxy(mallProxy, 3);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
