package com.guse.four_one_nine.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
* @ClassName: HpptUtil 
* @Description: http工具
* @author Fily GUSE
* @date 2017年8月31日 下午8:51:53 
*  
*/
public class HttpUtil {
	public final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	// 超时时间
	public final static int TIMEOUT = 1000 * 60 * 5;
	
	/** 
	* @Title: sendPUT 
	* @Description: 发送PUT请求 
	* @param @param url
	* @param @param data
	* @param @return
	* @return String 
	* @throws 
	*/
	public static String sendPUT(String url, String data) throws Exception {
		OutputStream out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			//设置http连接属性
			conn.setDoOutput(true); // http正文内，因此需要设为true, 默认情况下是false;
			conn.setDoInput(true); // 设置是否从httpUrlConnection读入，默认情况下是true;
			conn.setRequestMethod("PUT");// 可以根据需要 提交 GET、POST、DELETE、PUT等http提供的功能
			// 连接超时设置
			conn.setConnectTimeout(TIMEOUT);
			// 读取超时设置
			conn.setReadTimeout(TIMEOUT);
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("content-type", "application/json;charset=UTF-8");
			conn.setRequestProperty("X-Auth-Token", "token");  //设置请求的token
//			conn.setRequestProperty("Content-Length", data.getBytes().length + ""); //设置文件请求的长度  
			conn.connect();
			// 获取URLConnection对象对应的输出流
			out = conn.getOutputStream();
			// 发送请求参数
			out.write(data.getBytes("UTF-8"));
			out.flush(); //刷新对象输出流，将任何字节都写入潜在的流中  
			out.close(); //关闭流对象,此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中
			
			//读取响应 
			if(conn.getResponseCode() == 200) {
				// 定义BufferedReader输入流来读取URL的响应
				in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
				String line;
				while((line = in.readLine()) != null) {
					line = new String(line.getBytes());
					result += line;
				}
				in.close();
			} else {
				result = "{'err':'请求码："+conn.getResponseCode()+","+conn.getResponseMessage()+"'}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * @throws Exception  
	* @Title: sendGet 
	* @Description: TODO 
	* @param @param url
	* @param @param param
	* @param @return
	* @return String 
	* @throws 
	*/
	public static String sendGet(String url, String param) throws Exception {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 连接超时设置
			connection.setConnectTimeout(TIMEOUT);
			// 读取超时设置
			connection.setReadTimeout(TIMEOUT);
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");//**注意**，需要此格式，后边这个字符集可以不设置
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			throw e;
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url  发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String params) throws Exception{
		logger.info("发送POST请求，请求URL:{},请求参数:{}", url, params);
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			//设置http连接属性
			conn.setDoOutput(true); // http正文内，因此需要设为true, 默认情况下是false;
			conn.setDoInput(true); // 设置是否从httpUrlConnection读入，默认情况下是true;
			conn.setRequestMethod("POST");// 可以根据需要 提交 GET、POST、DELETE、PUT等http提供的功能
			// 连接超时设置
			conn.setConnectTimeout(TIMEOUT);
			// 读取超时设置
			conn.setReadTimeout(TIMEOUT);
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			//conn.setRequestProperty("content-type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("X-Auth-Token", "token");  //设置请求的token

            conn.connect();
//			conn.setRequestProperty("Content-Length", data.getBytes().length + ""); //设置文件请求的长度  
			conn.connect();
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			if(StringUtils.isNotBlank(params)) {
				out.write(params);
				
			}
			out.flush(); //刷新对象输出流，将任何字节都写入潜在的流中  
//			out.close(); //关闭流对象,此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中 
			//读取响应 
			if(conn.getResponseCode() == 200) {
				// 定义BufferedReader输入流来读取URL的响应
				in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
				String line;
				while((line = in.readLine()) != null) {
					line = new String(line.getBytes());
					result += line;
				}
				in.close();
			} else {
				result = "{'err':'请求码："+conn.getResponseCode()+","+conn.getResponseMessage()+"'}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
