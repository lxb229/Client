package com.guse.platform.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.guse.platform.common.base.Result;
import com.guse.platform.vo.doudou.AccountVo;
import com.guse.platform.vo.doudou.CorpsVo;
import com.guse.platform.vo.doudou.NoticeVo;
@Component
public class GameGMUtil {
	private static final Logger logger = LoggerFactory.getLogger(GameGMUtil.class);
	
	@Autowired
	private  PropertyConfigurer configurer;
	
	/** GM-俱乐部地址 */
    public  Result<CorpsVo> cropsService(CorpsVo corpsVo) {
    	String ipAddress1 = configurer.getProperty("gameAddress1");
    	String serviceAddress = ipAddress1+"/crops?cmd="+corpsVo.getCmd();
    	switch(corpsVo.getCmd()){
    		case 1:
    			break;
    		case 2:
    			serviceAddress = serviceAddress+"&roomCardLimit="+corpsVo.getRoomCardLimit()+"&createMax="+corpsVo.getCreateMax()+
    					"&maxMemberNum="+corpsVo.getMaxMemberNum();
    			break;
    	}
        String result = null;
        try {
        	result = HttpClientUtil.httpGet(serviceAddress);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
			return new Result<CorpsVo>(00000,"获取接口地址异常!"); 
		}
        logger.info("获取接口地址:{}",result);
        if (result != null && result != "") {
            //JSON格式转换
            JSONObject obj = JSONObject.parseObject(result);
            CorpsVo vo = new CorpsVo();
            int success = obj.getIntValue("code");
            if(success == 0) {
            	switch(corpsVo.getCmd()){
	        		case 1:
	        			vo.setCode(success);
	        			vo.setCmd(1);
	        			String content = obj.getString("content");
	        			//JSON格式转换
	                    JSONObject contentObj = JSONObject.parseObject(content);
	        			vo.setRoomCardLimit(contentObj.getInteger("roomCardLimit"));
	        			vo.setCreateMax(contentObj.getInteger("createMax"));
	        			vo.setMaxMemberNum(contentObj.getInteger("maxMemberNum"));
	        			break;
	        		case 2:
	        			vo.setCode(success);
	        			vo.setCmd(1);
	        			break;
	        	}
            	return new Result<CorpsVo>(vo);
            } else {
            	return new Result<CorpsVo>(00000,obj.getString("content")); 
            }
            
        } else {
        	return new Result<CorpsVo>(00000,"返回结果超时!");
        }
    }
	/** GM-公告地址 */
    public  Result<Integer> noticeService(NoticeVo noticeVo) {
    	
    	String ipAddress1 = configurer.getProperty("gameAddress1");
    	String serviceAddress = ipAddress1+"/notice?cmd="+noticeVo.getCmd();
    	switch(noticeVo.getCmd()){
    		case 1:
    			serviceAddress = serviceAddress+"&noticeId="+noticeVo.getNoticeId()+"&templateId="+noticeVo.getTemplateId()+
    			"&noticeParams="+noticeVo.getNoticeParams()+"&startTime="+noticeVo.getStartTime()+"&vialdTime="+noticeVo.getVialdTime()+"&repate="+noticeVo.getRepate();
    			break;
    		case 2:
    			serviceAddress = serviceAddress+"&noticeId="+noticeVo.getNoticeId()+"&noticeParams="+noticeVo.getNoticeParams()+
    			"&startTime="+noticeVo.getStartTime()+"&vialdTime="+noticeVo.getVialdTime()+"&repate="+noticeVo.getRepate();
    			break;
    		case 3:
    			serviceAddress = serviceAddress+"&noticeId="+noticeVo.getNoticeId();
    			break;
    	
    	}
        String result = null;
        try {
        	result = HttpClientUtil.httpGet(serviceAddress);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
			return new Result<Integer>(00000,"获取接口地址异常!"); 
		}
        logger.info("获取接口地址:{}",result);
        if (result != null && result != "") {
            //JSON格式转换
            JSONObject obj = JSONObject.parseObject(result);
            int success = obj.getIntValue("code");
            if(success >= 0) {
            	return new Result<Integer>(success);
            } else {
            	return new Result<Integer>(00000,obj.getString("content")); 
            }
            
        } else {
        	return new Result<Integer>(00000,"返回结果超时!");
        }
    }
    
    /** GM-账号地址 */
    public  Result<Integer> accountService(AccountVo accountVo) {
    	String ipAddress1 = configurer.getProperty("gameAddress1");
    	String serviceAddress = ipAddress1+"/account?cmd="+accountVo.getCmd();
    	switch(accountVo.getCmd()){
    		case 1:
    			serviceAddress = serviceAddress+"&starNO="+accountVo.getStarNo()+"&state="+accountVo.getState();
    			break;
    		case 2:
    			serviceAddress = serviceAddress+"&starNO="+accountVo.getStarNo()+"&type="+accountVo.getType()+"&wxNO="+accountVo.getWxNO();
    			break;
    		case 3:
    			serviceAddress = serviceAddress+"&starNO="+accountVo.getStarNo()+"&phone="+accountVo.getPhone();
    			break;
    		case 4:
    			serviceAddress = serviceAddress+"&starNO="+accountVo.getStarNo()+"&name="+accountVo.getName()+"&cardId="+accountVo.getCardId();
    			break;
    		case 5:
    			serviceAddress = serviceAddress+"&starNO="+accountVo.getStarNo()+"&money="+accountVo.getMoney();
    			break;
    		case 6:
    			serviceAddress = serviceAddress+"&starNO="+accountVo.getStarNo()+"&luck="+accountVo.getLuck()
    			+"&luckStart="+accountVo.getLuckStart()+"&luckEnd="+accountVo.getLuckEnd()+"&luckNum="+accountVo.getLuckNum();
    			break;
    		case 7:
    			serviceAddress = serviceAddress+"&starNO1="+accountVo.getStarNO1()+"&starNO2="+accountVo.getStarNO2()
    			+"&num="+accountVo.getScAmount();
    			break;
    	
    	}
        String result = null;
        try {
        	result = HttpClientUtil.httpGet(serviceAddress);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
			return new Result<Integer>(00000,"获取接口地址异常!"); 
		}
        logger.info("获取接口地址:{}",result);
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
