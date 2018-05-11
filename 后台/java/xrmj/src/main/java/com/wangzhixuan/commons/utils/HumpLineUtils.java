package com.wangzhixuan.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 驼峰和下划线之间的互转
 * @author 不能
 *
 */
public class HumpLineUtils {
	private static Pattern linePattern = Pattern.compile("_(\\w)");  
	private static Pattern humpPattern = Pattern.compile("[A-Z]");  
    /**下划线转驼峰*/  
    public static String lineToHump(String str){  
        str = str.toLowerCase();  
        Matcher matcher = linePattern.matcher(str);  
        StringBuffer sb = new StringBuffer();  
        while(matcher.find()){  
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());  
        }  
        matcher.appendTail(sb);  
        return sb.toString();  
    } 
    /**驼峰转下划线*/  
    public static String humpToLine(String str){  
        Matcher matcher = humpPattern.matcher(str);  
        StringBuffer sb = new StringBuffer();  
        while(matcher.find()){  
            matcher.appendReplacement(sb, "_"+matcher.group(0).toLowerCase());  
        }  
        matcher.appendTail(sb);  
        return sb.toString();  
    }  
}
