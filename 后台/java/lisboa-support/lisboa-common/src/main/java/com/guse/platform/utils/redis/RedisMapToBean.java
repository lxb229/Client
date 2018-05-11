package com.guse.platform.utils.redis;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.guse.platform.utils.date.DateUtils;


/**
 * redis转换javabean
 * @author nbin
 * @date 2017年7月25日 下午8:54:48 
 * @version V1.0
 */
public class RedisMapToBean {
	
	/**
	 * 哈希转换成pojo
	 * @Title: mapToPojo 
	 * @param @param m
	 * @param @param obj 
	 * @return void
	 */
	public void mapToPojo(Map<String, String> m, Object obj) {

        String s = obj.getClass().getName();
        Class<?> clazz = null;
		try {
			clazz = Class.forName(s);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
        for (Entry<String, String> entry : m.entrySet()) {
        	try {

 	           Field field = clazz.getDeclaredField((String)entry.getKey());
 	           field.setAccessible(true);
 	           
 	           if (field.getType() == Integer.class) {
 	        	   Integer valueInt = 0;
 	        	   String value = entry.getValue();
 	        	   if(!StringUtils.isEmpty(value)){
 	        		   if(value.contains(".")){
 	        			   value = value.substring(0, value.indexOf("."));
 		        	   }
 	        	   }
 	        	   valueInt = Integer.valueOf(value);
 	              field.set(obj, valueInt);
 	           } else if (field.getType() == String.class) {
 	              field.set(obj, (String) entry.getValue());
 	           } else if (field.getType() == Date.class) {
 	        	   String value = entry.getValue();
 	        	   if(value.length()>10){
 	        		   field.set(obj, DateUtils.StrToDate(entry.getValue(), DateUtils.sdf));
 	        	   }else{
 	        		   field.set(obj, DateUtils.StrToDate(entry.getValue(), DateUtils.format));
 	        	   }
 	              
 	           } else if (field.getType() == Double.class) {          
 	              field.set(obj,Double.parseDouble((String)entry.getValue()));      
 	           } else if (field.getType() == Float.class) {
 	              field.set(obj, Float.parseFloat((String)entry.getValue()));
 	           } else if (field.getType() == Long.class) {
 	              field.set(obj, Long.parseLong((String)entry.getValue()));
 	           } else if (field.getType() == Boolean.class) {
 	              field.set(obj,Boolean.parseBoolean((String)entry.getValue()));
 	           } else if (field.getType() == Short.class) {
 	              field.set(obj, Short.parseShort((String)entry.getValue()));
 	           } else if (field.getType() == BigDecimal.class) {
 	        	  field.set(obj, entry.getValue().toString());
 	           }
 	        
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
}
