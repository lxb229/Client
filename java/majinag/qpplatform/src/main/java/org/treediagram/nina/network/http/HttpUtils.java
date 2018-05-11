/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.treediagram.nina.network.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Htpp工具类
 * 
 * @author kidal
 *
 */
public abstract class HttpUtils {
	/**
	 * http get
	 */
	public static HttpUtilsResponse get(String url, Map<String, Object> queryStrings, int connectionRequestTimeout,
			int socketTimeout) throws IOException {
		// 编码查询字符串
		String queryString = null;
		if (queryStrings != null && queryStrings.size() > 0) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>(queryStrings.size());
			for (Entry<String, Object> entry : queryStrings.entrySet()) {
				String name = entry.getKey();
				String value = String.valueOf(entry.getValue());
				BasicNameValuePair pair = new BasicNameValuePair(name, value);
				pairs.add(pair);
			}

			queryString = URLEncodedUtils.format(pairs, "UTF-8");
		}

		// 拼接url
		String fullUrl = (queryString != null) ? (url + "?" + queryString) : url;

		// 创建http客户端
		CloseableHttpClient httpClient = HttpClients.createDefault();

		// 创建请求
		HttpGet get = new HttpGet(fullUrl);

		// 配置
		Builder builder = RequestConfig.custom();
		builder.setConnectionRequestTimeout(connectionRequestTimeout);
		builder.setSocketTimeout(socketTimeout);
		RequestConfig requestConfig = builder.build();
		get.setConfig(requestConfig);

		// 发出请求
		HttpUtilsResponse httpResponse = httpClient.execute(get, new Handler());

		// 完毕
		return httpResponse;
	}

	/**
	 * http post
	 */
	public static HttpUtilsResponse post(String url, Map<String, Object> queryStrings, int connectionRequestTimeout,
			int socketTimeout) throws IOException {
		// 创建查询字符串对
		UrlEncodedFormEntity entity = null;
		if (queryStrings != null && queryStrings.size() > 0) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>(queryStrings.size());
			for (Entry<String, Object> entry : queryStrings.entrySet()) {
				String name = entry.getKey();
				String value = String.valueOf(entry.getValue());
				BasicNameValuePair pair = new BasicNameValuePair(name, value);
				pairs.add(pair);
			}
			entity = new UrlEncodedFormEntity(pairs);
		}

		// 创建http客户端
		CloseableHttpClient httpClient = HttpClients.createDefault();

		// 创建请求
		HttpPost post = new HttpPost(url);
		if (entity != null) {
			post.setEntity(entity);
		}

		// 配置
		Builder builder = RequestConfig.custom();
		builder.setConnectionRequestTimeout(connectionRequestTimeout);
		builder.setSocketTimeout(socketTimeout);
		RequestConfig requestConfig = builder.build();
		post.setConfig(requestConfig);

		// 发出请求
		HttpUtilsResponse httpResponse = httpClient.execute(post, new Handler());

		// 完毕
		return httpResponse;
	}

	/**
	 * 答复处理器
	 */
	private static class Handler implements ResponseHandler<HttpUtilsResponse> {
		@Override
		public HttpUtilsResponse handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			// 获取状态
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			// 获取答复字符串
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");

			// 完成
			return new HttpUtilsResponse(statusCode, responseString);
		}
	}

	/**
	 * 静态类
	 */
	private HttpUtils() {
	}
}
