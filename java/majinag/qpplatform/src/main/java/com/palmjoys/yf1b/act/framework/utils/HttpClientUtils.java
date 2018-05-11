package com.palmjoys.yf1b.act.framework.utils;

import java.text.MessageFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.java_websocket.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treediagram.nina.network.http.HttpUtilsResponse;


/**
 * @author 骆桂桦
 * @version 1.0
 * @date 2016年8月15日 下午3:31:28
 */
public class HttpClientUtils {

	//初始化HttpClient
	private static HttpClient httpClient = null;
	private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	//生产HttpClient实例
    //公开，静态的工厂方法，需要使用时才去创建该单体
	public static HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = HttpClients.createDefault();
        }
        return httpClient;
    }
    
    /**
     * POST方式调用
     * @param params 参数为JSONObject对象
     * @return 响应字符串
     */
	public static String executeByPost(String url, String params) {
    	//得到HttpClient实例
        HttpClient httpclient = getHttpClient();
        //创建post请求
        HttpPost post = new HttpPost(url);
        //设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
        post.setConfig(requestConfig);
        //创建响应处理器处理服务器响应内容 
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        //返回结果
        String responseJson = null;
        try {
            if (params != null) {
            	StringEntity entity = new StringEntity(params,"utf-8");//解决中文乱码问题    
                entity.setContentType("application/json");    
                post.setEntity(entity);
            }
            //执行请求并获取结果
            responseJson = httpclient.execute(post, responseHandler);
        } catch (Exception e) {
        	//logger.error("executeByPost---请求失败--------"+e.getMessage());
        } finally {
        	 //当不再需要HttpClient实例时,关闭连接管理器以确保释放所有占用的系统资源
        	 post.abort();
        }
        return responseJson;
    }
	    
    /**
     * GET方式调用
     * @param url 带参数占位符的URL，例：http://192.168.0.117:7890/texas_gm/index/editsPwd?codes={0}&email={1}
     * @param params 参数为Object[]对象
     * @return 响应字符串
     */
	public static String executeByGet(String url, Object[] params) {
		if(null == params){
			return null;
		}
		
    	//得到HttpClient实例
        HttpClient httpclient = getHttpClient();
        //url中数字对应传入的参数数组中的索引(必须key/value对应)
        String messages = MessageFormat.format(url, params);
        //创建get请求   messages
        HttpGet get = new HttpGet(messages);
        //设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).build();
        get.setConfig(requestConfig);
        //创建响应处理器处理服务器响应内容 
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        //返回结果
        String responseJson = null;
        try {
            //执行请求并获取结果
            responseJson = httpclient.execute(get, responseHandler);
        } catch (Exception e) {
        	logger.error("executeByGet---请求失败--------"+e.getMessage());
        } finally {
        	//当不再需要HttpClient实例时,关闭连接管理器以确保释放所有占用的系统资源
        	get.abort();
        }
        return responseJson;
    }

    
}
