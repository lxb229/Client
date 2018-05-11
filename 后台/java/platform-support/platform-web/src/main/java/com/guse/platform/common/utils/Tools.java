package com.guse.platform.common.utils;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.apache.commons.lang.StringUtils;


public class Tools {

	/**
	 * 检测字符串是否不为空(null,"","null")
	 * 
	 * @param s
	 * @return 不为空则返回true，否则返回false
	 */
	public static boolean notEmpty(String s) {
		return s != null && !"".equals(s) && !"null".equals(s);
	}

	/**
	 * 检测字符串是否为空(null,"","null")
	 * 
	 * @param s
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean isEmpty(String s) {
		return s == null || "".equals(s) || "null".equals(s);
	}

	/**
	 * 字符串转换为字符串数组
	 * 
	 * @param str
	 *            字符串
	 * @param splitRegex
	 *            分隔符
	 * @return
	 */
	public static String[] str2StrArray(String str, String splitRegex) {
		if (isEmpty(str)) {
			return null;
		}
		return str.split(splitRegex);
	}
	
	/**
	 * string 转化为 int
	 *
	 * @param @param str
	 * @param @return   
	 * @return int[]  
	 * @throws
	 * @author NIUBIN
	 * @date 2016年1月5日
	 */
	public static Integer[] StringtoInt(String str) {
		Integer[] intArr = null;
		if(StringUtils.isNotBlank(str)){
			String[] strArr =  str.split(",");
			intArr = new Integer[strArr.length];
			for (int i = 0; i <strArr.length; i++){
				intArr[i] = Integer.parseInt(strArr[i]);
			}
		}
		return intArr;
	 }
	
	/**
	 * 用默认的分隔符(,)将字符串转换为字符串数组
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static String[] str2StrArray(String str) {
		return str2StrArray(str, ",\\s*");
	}

	/**
	 * 百分数转小数
	 * 
	 * @return
	 */
	public static Float todecimal(String str) {
		Float f = null;
		if (str.contains("%")) {
			str = str.replaceAll("%", "");
			f = (Float.valueOf(str)) / 100;
		}
		return f;
	}
	
	
	
	/**
	 * 把对象的空串""转换为null
	 * nbin
	 * @Title: converNullString 
	 * @date 2017年8月22日 上午10:20:29 
	 * @version V1.0
	 */
	public static void converNullString(Object obj){ 
		String s = obj.getClass().getName();
		Class<?> clazz = null;
		try {
			clazz = Class.forName(s);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Field fields[] = clazz.getDeclaredFields(); 
		for (Field field : fields) { 
			try { 
				 if (field.getType() == String.class) { 
					Method m = clazz.getMethod("get" + change(field.getName())); 
					Object name = m.invoke(obj);
					if (null == name){
						continue;
					}
					if (name == "") { 
						Method mtd = clazz.getMethod("set" + change(field.getName()), 
						new Class[] { String.class });
						mtd.invoke(obj, new Object[] {null});
					} 
				} 
				 
			} catch (Exception e) { 
				e.printStackTrace(); 
			} 
		} 
	} 
	private static String change(String src) { 
		if (src != null) { 
			StringBuffer sb = new StringBuffer(src); 
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0))); 
			return sb.toString(); 
		} else { 
			return null; 
		} 
	}

}
