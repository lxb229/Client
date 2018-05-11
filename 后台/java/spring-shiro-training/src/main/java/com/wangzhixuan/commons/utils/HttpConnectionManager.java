package com.wangzhixuan.commons.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http 连接管理类
 *
 */
public class HttpConnectionManager {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpConnectionManager.class);
	//最大连接数400
	private static int MAX_CONNECTION_NUM = 400;

	//单路由最大连接数80
	private static int MAX_PER_ROUTE = 80;

	private static Object LOCAL_LOCK = new Object();

	/**
	 * 连接池管理对象
	 */
	private static PoolingHttpClientConnectionManager connManger = null;

	/**
	 * 初始化连接池管理对象
	 */
	private static PoolingHttpClientConnectionManager getPoolManager() {
		if (null == connManger) {
			synchronized (LOCAL_LOCK) {
				if (null == connManger) {
					SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
					try {
						sslContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
						SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
								sslContextBuilder.build());
						Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
								.<ConnectionSocketFactory>create()
								.register("https", socketFactory)
								.register("http", new PlainConnectionSocketFactory()).build();
						connManger = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
						connManger.setMaxTotal(MAX_CONNECTION_NUM);
						connManger.setDefaultMaxPerRoute(MAX_PER_ROUTE);
					} catch (Exception e) {
						logger.error("init PoolingHttpClientConnectionManager Error" + e.getMessage(), e);
					}
				}
			}
		}
		return connManger;
	}

	/**
	 * 创建线程安全的HttpClient
	 */
	public static CloseableHttpClient getHttpsClient(RequestConfig config) {
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config)
				.setConnectionManager(getPoolManager()).build();
		return httpClient;
	}
	
	/**
	 * 创建线程安全的HttpClient
	 */
	public static CloseableHttpClient getHttpsClient() {
		CloseableHttpClient httpClient = HttpClients.custom()
				.setConnectionManager(getPoolManager()).build();
		return httpClient;
	}
}
