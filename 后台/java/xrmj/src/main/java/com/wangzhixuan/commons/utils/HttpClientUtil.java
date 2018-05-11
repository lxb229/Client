package com.wangzhixuan.commons.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http Client工具类
 * @author Administrator
 *
 */
public class HttpClientUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	private static final String CHAR_SET = "UTF-8";

	// 向服务端请求超时时间设置(单位:毫秒)
	private static int SERVER_REQUEST_TIME_OUT = 5000;

	// 服务端响应超时时间设置(单位:毫秒)
	private static int SERVER_RESPONSE_TIME_OUT = 5000;

	/**
	 * Http Post请求
	 * 
	 * @param url
	 *            URL地址
	 * @param headers
	 *            请求头设置
	 * @param params
	 *            发送的数据实体
	 * @return 成功时返回结果内容，失败时返回null
	 */
	public static String httpPost(String url, HashMap<String, String> headers, List<NameValuePair> params) {
		CloseableHttpResponse response = null;
		HttpPost post = null;
		try {
			// 请求设置信息
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SERVER_REQUEST_TIME_OUT)
					.setConnectTimeout(SERVER_RESPONSE_TIME_OUT).build();
			post = new HttpPost(url);
			post.setConfig(requestConfig);
			// 请求头
			if (headers != null) {
				for (Map.Entry<String, String> item : headers.entrySet()) {
					post.addHeader(item.getKey(), item.getValue());
				}
			}
			// 请求参数
			if (params != null) {
				UrlEncodedFormEntity httpEntity = new UrlEncodedFormEntity(params, CHAR_SET);
				post.setEntity(httpEntity);
			}

			response = HttpConnectionManager.getHttpsClient(requestConfig).execute(post);
			int status = response.getStatusLine().getStatusCode();

			String result = null;
			if (status == 200) {
				result = EntityUtils.toString(response.getEntity(), CHAR_SET);
			} else {
				logger.error("HTTP 请求失败，错误代码：" + status);
			}

			return result;

		} catch (Exception e) {
			if (e instanceof SocketTimeoutException) {
				logger.error("HTTP 服务请求超时.", e);
			} else if (e instanceof ConnectTimeoutException) {
				logger.error("HTTP 服务响应超时.", e);
			} else {
				logger.error("HTTP POST请求失败：" + e.getMessage(), e);
			}
		} finally {
			// 归还连接
			post.releaseConnection();
			
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
					response.close();
					response = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// 超时或者网络不通时返回值
		return null;
	}

	public static String httpPost(String url, HashMap<String, String> headers) {
		return httpPost(url, headers, null);
	}

	public static String httpPost(String url, List<NameValuePair> parmas) {
		return httpPost(url, null, parmas);
	}
	
	public static String httpPost(String url) {
		return httpPost(url, null, null);
	}

	public static <E> String httpPost(String url, HashMap<String, String> headers, E params) {
		if (params == null) {
			return httpPost(url, headers, null);
		}

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		//formparams.add(new BasicNameValuePair("DataContent", gson.toJson(params)));//直接传输json对象
		
		Field[] fields = params.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Object val = null;
			try {
				val = field.get(params);
			} catch (Exception e) {
			}
			if (val != null) {
				formparams.add(new BasicNameValuePair(field.getName(), val.toString()));
			}
		}

		return httpPost(url, headers, formparams);
	}

	public static <E> String httpPost(String url, E params) {
		return HttpClientUtil.<E>httpPost(url, null, params);
	}
	
    public static String httpPost(String url,Map<String, String> params){
        List<NameValuePair> nvps = new ArrayList <NameValuePair>();
        Set<String> keySet = params.keySet();
        for(String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }
        return httpPost(url,nvps);
    }

	/**
	 * Http Get 请求
	 * 
	 * @param url
	 *            url地址
	 * @param headers
	 *            请求头
	 * @param params
	 *            参数列表
	 * @return 成功返回结果，失败返回null
	 */
	public static String httpGet(String url, HashMap<String, String> headers, List<NameValuePair> params) {
		CloseableHttpResponse response = null;
		HttpGet get = null;
		String urlParam = "";
		try {
			// 准备请求设置
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SERVER_REQUEST_TIME_OUT)
					.setConnectTimeout(SERVER_RESPONSE_TIME_OUT).build();

			// 准备请求参数
			if (params != null) {
				urlParam = EntityUtils.toString(new UrlEncodedFormEntity(params, CHAR_SET));
				if (urlParam != null && !urlParam.equals("")) {
					urlParam = "?" + urlParam;
				}
			}
			get = new HttpGet(url + urlParam);
			get.setConfig(requestConfig);

			// 准备请求头
			if (headers != null) {
				for (Map.Entry<String, String> item : headers.entrySet()) {
					get.addHeader(item.getKey(), item.getValue());
				}
			}

			response = HttpConnectionManager.getHttpsClient(requestConfig).execute(get);
			int status = response.getStatusLine().getStatusCode();

			String result = null;
			if (status == 200) {
				result = EntityUtils.toString(response.getEntity(), CHAR_SET);
			} else {
				logger.error("HTTP Get请求失败，错误代码：" + status);
			}

			return result;

		} catch (Exception e) {
			if (e instanceof SocketTimeoutException) {
				logger.error("HTTP Get 服务请求超时.", e);
			} else if (e instanceof ConnectTimeoutException) {
				logger.error("HTTP Get 服务响应超时.", e);
			} else {
				logger.error("HTTP Post 请求失败：" + e.getMessage(), e);
			}
		} finally {
			// 归还连接
			get.releaseConnection();
			
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
					response.close();
					response = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		// 超时或者网络不通时返回值
		return null;
	}

	public static String httpGet(String url, HashMap<String, String> headers) {
		return HttpClientUtil.httpGet(url, headers, null);
	}
	
	public static String httpGet(String url, List<NameValuePair> params) {
		return HttpClientUtil.httpGet(url, null, params);
	}
	
	public static String httpGet(String url) {
		return HttpClientUtil.httpGet(url, null, null);
	}
}
