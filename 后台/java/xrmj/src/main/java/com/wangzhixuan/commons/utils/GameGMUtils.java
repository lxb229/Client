package com.wangzhixuan.commons.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.vo.AccountVo;
import com.wangzhixuan.model.vo.GmNoticeVo;

public class GameGMUtils {
	private static final Logger logger = LoggerFactory.getLogger(GameGMUtils.class);
	@Autowired
	private  PropertyConfigurer configurer;
	
	 /** GM-账号地址 */
    public  Result accountService(AccountVo accountVo) {
    	String ipAddress1 = configurer.getProperty("gameAddress1");
    	String serviceAddress = ipAddress1+"/account?cmd="+accountVo.getCmd();
    	switch(accountVo.getCmd()){
    		/** 冻结或解冻帐号*/
    		case 1:
    			serviceAddress = serviceAddress+"&starNO="+accountVo.getStarNo()+"&state="+accountVo.getState();
    			break;
    		/** 修改绑定的手机号*/
    		case 3:
    			serviceAddress = serviceAddress+"&starNO="+accountVo.getStarNo()+"&phone="+accountVo.getPhone();
    			break;
    		/** 修改实名认证信息*/
    		case 4:
    			serviceAddress = serviceAddress+"&starNO="+accountVo.getStarNo()+"&name="+accountVo.getName()+"&cardId="+accountVo.getCardId();
    			break;
    		/** 增加或减少房卡*/
    		case 5:
    			serviceAddress = serviceAddress+"&starNO="+accountVo.getStarNo()+"&money="+accountVo.getMoney()+"&type="+accountVo.getType();
    			break;
    		/** 设置玩家幸运值*/
    		case 6:
    			serviceAddress = serviceAddress+"&starNO="+accountVo.getStarNo()+"&luck="+accountVo.getLuck()
    			+"&luckStart="+accountVo.getLuckStart()+"&luckEnd="+accountVo.getLuckEnd()+"&luckNum="+accountVo.getLuckNum();
    			break;
    	}
        String result = null;
        try {
        	result = HttpClientUtil.httpGet(serviceAddress);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
			return new Result(false, "获取接口地址异常!");
		}
        logger.info("获取接口地址:{}",result);
        if (result != null && result != "") {
            //JSON格式转换
            JSONObject obj = JSONObject.parseObject(result);
            int success = obj.getIntValue("code");
            if(success == 0) {
            	return new Result("OK");
            } else {
            	return new Result(false, obj.getString("content"));
            }
            
        } else {
        	return new Result(false, "返回结果超时!");
        }
    }
    
    /** 
     * GM-公告地址 
     * 
     */
    public  Result noticeService(GmNoticeVo gmNoticeVo) {
    	
    	String ipAddress1 = configurer.getProperty("gameAddress1");
    	String serviceAddress = ipAddress1+"/notice?cmd="+gmNoticeVo.getCmd();
    	switch(gmNoticeVo.getCmd()){
    		/**
    		 * 添加公告 
    		 */
    		case 1:
    			serviceAddress = serviceAddress+"&noticeId="+gmNoticeVo.getNoticeId()+"&templateId="+gmNoticeVo.getTemplateId()+
    			"&noticeParams="+gmNoticeVo.getNoticeParams()+"&startTime="+gmNoticeVo.getStartTime()+"&vialdTime="+gmNoticeVo.getVialdTime()+"&repate="+gmNoticeVo.getRepate();
    			break;
    		/**
    		 * 修改公告
    		 */
    		case 2:
    			serviceAddress = serviceAddress+"&noticeId="+gmNoticeVo.getNoticeId()+"&noticeParams="+gmNoticeVo.getNoticeParams()+
    			"&startTime="+gmNoticeVo.getStartTime()+"&vialdTime="+gmNoticeVo.getVialdTime()+"&repate="+gmNoticeVo.getRepate();
    			break;
    		/**
    		 * 删除公告
    		 */
    		case 3:
    			serviceAddress = serviceAddress+"&noticeId="+gmNoticeVo.getNoticeId();
    			break;
    	
    	}
        String result = null;
        try {
        	result = HttpClientUtil.httpGet(serviceAddress);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
			return new Result(false,"获取接口地址异常!"); 
		}
        logger.info("获取接口地址:{}",result);
        if (result != null && result != "") {
            //JSON格式转换
            JSONObject obj = JSONObject.parseObject(result);
            int success = obj.getIntValue("code");
            if(success >= 0) {
            	return new Result("OK");
            } else {
            	return new Result(false,obj.getString("content")); 
            }
        } else {
        	return new Result(false,"返回结果超时!");
        }
    }
    
    /**
     * 发送短信验证码
     */
    public boolean sendSMS(String phone, String smsCode){
		boolean bRet = true;
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		URL realUrl=null;
		try{
			//发送内容
			String smsTemplete = configurer.getProperty("SMS_TEMPLETE");
			String sContent = smsTemplete.replace("XX", smsCode);
			String send_content = URLEncoder.encode(
					sContent.replaceAll("<br/>", " "), "GBK");
			//接口提交的URL地址
			String urlString = configurer.getProperty("SMS_URL");
			String urlParam = "";			
			urlParam = "CorpID=" + configurer.getProperty("SMS_UNAME") + "&Pwd=" + configurer.getProperty("SMS_PWD")
				+ "&Mobile=" + phone + "&Content=" + send_content + "&Cell=&SendTime="+"";
			
			realUrl = new URL(urlString);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(urlParam);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line = "";
			while ((line = in.readLine()) != null) {
				result += line;
			}
			int nRs = Integer.parseInt(result);
			if(nRs <= 0){
				bRet = false;
			}
		}catch(Exception e){
			bRet = false;
		}finally{
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch(Exception e){
			}
		}
		return bRet;
	}
}