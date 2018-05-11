package cn.lcg.backstage.sdk.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ActiveMQConfig extends BaseConfig {
	protected static Properties pro;
	static {
		try {
			pro = new Properties();
			InputStream fs = ActiveMQConfig.class.getClassLoader().getResourceAsStream("platform-sdk.properties");
			//FileInputStream fs = new FileInputStream(new File(System.getProperty("user.dir") + "/conf/platform-sdk.properties"));
			pro.load(fs);
		} catch (IOException e) {
			logger.info("加载配置文件platform-sdk.properties失败.",e);
		}
	}
	
	/**
	 * 获取客户端ID
	 * @return
	 */
	public static String getClientID(){
		return pro.getProperty("activemq.ClientID");
	}
	
	/**
	 * 获取MQ连接地址
	 * @return
	 */
	public static String getMqURI(){
		return pro.getProperty("activemq.URI");
	}
	
	/**
	 * 获取MQ队列用户名
	 * @return
	 */
	public static String getUid(){
		return pro.getProperty("activemq.Uid");
	}
	
	/**
	 * 获取MQ队列密码
	 * @return
	 */
	public static String getPwd(){
		return pro.getProperty("activemq.Pwd");
	}
}
