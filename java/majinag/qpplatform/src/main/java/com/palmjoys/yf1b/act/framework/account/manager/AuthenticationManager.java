package com.palmjoys.yf1b.act.framework.account.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;
import org.treediagram.nina.memcache.service.Filter;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.framework.account.entity.AuthenticationEntity;
import com.palmjoys.yf1b.act.framework.account.model.PhoneVaildCodeAttrib;
import com.palmjoys.yf1b.act.framework.common.resource.ConfigValue;

@Component
public class AuthenticationManager {
	@Inject
	private EntityMemcache<Long, AuthenticationEntity> authenticationCache;
	@Static("SMS_UNAME")
	private ConfigValue<String> sms_uName;
	@Static("SMS_PWD")
	private ConfigValue<String> sms_uPwd;
	@Static("SMS_URL")
	private ConfigValue<String> sms_url;
	@Static("SMS_TEMPLETE")
	private ConfigValue<String> sms_templete;
	
	//手机短信数据
	private Map<String, PhoneVaildCodeAttrib> phoneVailCodeMap = new HashMap<>();
	
	
	public AuthenticationEntity loadOrCreate(long accountId){
		return authenticationCache.loadOrCreate(accountId, new EntityBuilder<Long, AuthenticationEntity>(){
			@Override
			public AuthenticationEntity createInstance(Long pk) {
				return AuthenticationEntity.valueOf(accountId);
			}
		});
	}
	
	//获取手机短信
	public String getSMSCode(String phone){
		String smsCode = null;
		PhoneVaildCodeAttrib codeAttrib = phoneVailCodeMap.get(phone);
		if(null == codeAttrib){
			codeAttrib = new PhoneVaildCodeAttrib();
		}		
		try{
			long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
			if(currTime < codeAttrib.vaildTime){
				return null;
			}
			smsCode = "";
			for(int index=0; index<6; index++){
				int code = (int) ((Math.random()*1000)%10);
				smsCode += ""+code;
			}
			//smsCode = "12345";
			boolean bSendOK = this.sendSMS(phone, smsCode);
			if(bSendOK){
				codeAttrib.vaildTime = currTime + 5*60*60*1000;
				codeAttrib.vaildCode = smsCode;
				phoneVailCodeMap.put(phone, codeAttrib);
			}else{
				smsCode = "";
			}
		}catch(Exception e){
			smsCode = "";
		}		
		return smsCode;
	}
	
	//验证SMS
	public boolean smsCodeVaild(String phone, String vaildCode){
		PhoneVaildCodeAttrib codeAttrib = phoneVailCodeMap.get(phone);
		if(null == codeAttrib){
			return false;
		}		
		if(codeAttrib.vaildCode.equalsIgnoreCase(vaildCode) == false){
			return false;
		}
		long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
		if(codeAttrib.vaildTime < currTime){
			return false;
		}
		return true;
	}
	
	public String phoneEncryption(String phone){
		if(phone.isEmpty() || phone.length() != 11){
			return phone;
		}
		String s = phone.substring(0, 3);
		String e = phone.substring(8, 11);
		String newS = s+"*****"+e;
		return newS;
	}
	
	private boolean sendSMS(String phone, String smsCode){
		boolean bRet = true;
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		URL realUrl=null;
		try{
			//发送内容
			String smsTemplete = sms_templete.getContent();
			String sContent = smsTemplete.replace("XX", smsCode);
			String send_content = URLEncoder.encode(
					sContent.replaceAll("<br/>", " "), "GBK");
			//接口提交的URL地址
			String urlString = sms_url.getContent();
			String urlParam = "";			
			urlParam = "CorpID=" + sms_uName.getContent() + "&Pwd=" + sms_uPwd.getContent()
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
	
	public List<AuthenticationEntity> findCache(Filter<AuthenticationEntity> filter){
		return authenticationCache.getFinder().find(filter);
	}
}
