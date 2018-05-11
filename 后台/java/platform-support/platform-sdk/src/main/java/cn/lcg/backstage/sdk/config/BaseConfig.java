package cn.lcg.backstage.sdk.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseConfig {
	protected static Logger logger = LoggerFactory.getLogger(BaseConfig.class);
	
	protected static int getSetInt(Properties pro,String name){
		int setting = -1;
		try {
			String sett = pro.getProperty(name);
			setting = Integer.parseInt(sett);
		} catch (Exception e) {
			logger.warn("获取"+name+"配置失败：\n"+e.getMessage(), e);
		}
		return setting;
	}
	
	protected static long getSetLong(Properties pro,String name){
		long setting = -1;
		try {
			String sett = pro.getProperty(name);
			setting = Long.parseLong(sett);
		} catch (Exception e) {
			logger.warn("获取"+name+"配置失败：\n"+e.getMessage(), e);
		}
		return setting;
	}
	
	protected static boolean getSetBool(Properties pro,String name){
		boolean setting = false ;
		try {
			String sett = pro.getProperty(name);
			setting = Boolean.parseBoolean(sett);
		} catch (Exception e) {
			logger.warn("获取"+name+"配置失败：\n"+e.getMessage(), e);
		}
		return setting;
	}
}
