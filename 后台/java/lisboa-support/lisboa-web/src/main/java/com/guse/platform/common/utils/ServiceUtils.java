package com.guse.platform.common.utils;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.guse.platform.vo.QueryVo;

/**
 * 
 * @author nbin
 * @date 2017年8月1日 上午10:34:44 
 * @version V1.0
 */
public class ServiceUtils {
	
	/**
	 * 查询日期条件
	 * @Title: queryDateterms 
	 * @param type 调用类型，(分页 1 ，导出 2 ，ECHARTS 3 )
	 * @date 2017年8月15日 上午11:45:41 
	 * @version V1.0
	 */
	public static Object initQueryDateterms(QueryVo query, Object obj,int type,String dateType) {
	      try {
	        String s = obj.getClass().getName();
	        Class<?> clazz = Class.forName(s).getSuperclass();
	        
	        /*
	         * startDate 
	         * endDate 
	         * 1.为日期返回条件，是返回startDate的 00:00:00 到 endDate的 23:59:59
	         * 2.type等于3 范围条件为空 需要默认查询前7日数据。
	         */
	        Field startDate = clazz.getDeclaredField("startDate");
	        Field endDate = clazz.getDeclaredField("endDate");
    		startDate.setAccessible(true);
	        endDate.setAccessible(true);
        	if(StringUtils.isNotBlank(query.getStartDate())){ 
        		if (startDate.getType() == Date.class) {
        			startDate.set(obj, DateUtils.parseDate(query.getStartDate()+" 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
        		}
        	}else{
        		if(type == 3){//echart默认显示7天
            		if(dateType!=null && dateType.equals("ltv")){
            			startDate.set(obj,DateUtils.parseDate(DateUtils.getBeforeDays(new Date(), 30)+" 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
            		}else{
            			startDate.set(obj,DateUtils.parseDate(DateUtils.getBeforeDays(new Date(), 8)+" 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
            		}
        		}
        	}
        	if(StringUtils.isNotBlank(query.getEndDate())){ 
        		if (endDate.getType() == Date.class) {
        			endDate.set(obj, DateUtils.parseDate(query.getEndDate()+" 23:59:59", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
        		}
        	}else{
        		if(type == 3){//echart默认显示1天
    		        endDate.set(obj,DateUtils.parseDate(DateUtils.getBeforeDays(new Date(), 1)+" 23:59:59", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
        		}
        	}
        	
        	/*
	         * 比较日期
	         * contrastBefourDate
	         * contrastAfterDate
	         * 1 比较日期为两个单独的日期 
	         * contrastBefourDateStrat 00:00:00 到 contrastBefourDateStrat 的 23:59:59
	         * contrastAfterDateStrat 00:00:00 到 contrastAfterDateEnd 的 23:59:59
	         * 
	         */
        	//前
        	Field contrastDateStart = clazz.getDeclaredField("contrastDateStart");
        	Field contrastDateEnd = clazz.getDeclaredField("contrastDateEnd");
        	contrastDateStart.setAccessible(true);
        	contrastDateEnd.setAccessible(true);
        	
        	if(StringUtils.isNotBlank(dateType) && dateType.equals("contrastBefourDate")){
        		if(StringUtils.isNotBlank(query.getContrastBefourDate())){
    		        if (contrastDateStart.getType() == Date.class) {
    		        	contrastDateStart.set(obj, DateUtils.parseDate(query.getContrastBefourDate()+" 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
    		        }
    		        if (contrastDateEnd.getType() == Date.class) {
    		        	contrastDateEnd.set(obj, DateUtils.parseDate(query.getContrastBefourDate()+" 23:59:59", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
    		        }
    	        }else{
    	        	//默认当天
    	        	if (contrastDateStart.getType() == Date.class) {
    	        		contrastDateStart.set(obj, DateUtils.getStartTime());
    		        }
    		        if (contrastDateEnd.getType() == Date.class) {
    		        	contrastDateEnd.set(obj, DateUtils.getEndTime());
    		        }
    	        }
        	}
        	//后
        	if(StringUtils.isNotBlank(dateType)  && dateType.equals("contrastAfterDate")){
    	        if(StringUtils.isNotBlank(query.getContrastAfterDate())){
    		        if (contrastDateStart.getType() == Date.class) {
    		        	contrastDateStart.set(obj, DateUtils.parseDate(query.getContrastAfterDate()+" 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
    		        }
    		        if (contrastDateEnd.getType() == Date.class) {
    		        	contrastDateEnd.set(obj, DateUtils.parseDate(query.getContrastAfterDate()+" 23:59:59", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
    		        }
    	        }else{
    	        	//对比日期默认当天推后一天
    	        	String yesterday = DateUtils.getBeforeDays(new Date(), 1);
    	        	if (contrastDateStart.getType() == Date.class) {
    	        		contrastDateStart.set(obj, DateUtils.parseDate(yesterday + " 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
    		        }
    		        if (contrastDateEnd.getType() == Date.class) {
    		        	contrastDateEnd.set(obj, DateUtils.parseDate(yesterday + " 23:59:59", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
    		        }
    	        }
        	}
        	
	        
	        
	        /*
	         * 单独查询某天
	         * 有些统计是按分钟或者秒去统计 
	         * 
	         */
	        Field somedayDateStart = clazz.getDeclaredField("somedayDateStart");
	        Field somedayDateEnd = clazz.getDeclaredField("somedayDateEnd");
        	somedayDateStart.setAccessible(true);
        	somedayDateEnd.setAccessible(true);
	        if(StringUtils.isNotBlank(query.getSomedayDate())){
	        	if (somedayDateStart.getType() == Date.class) {
	        		somedayDateStart.set(obj, DateUtils.parseDate(query.getSomedayDate()+" 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
	        	}
	        	if (somedayDateEnd.getType() == Date.class) {
	        		somedayDateEnd.set(obj, DateUtils.parseDate(query.getSomedayDate()+" 23:59:59", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
	        	}
	        }else{
	        	//默认为当天
	        	if (somedayDateStart.getType() == Date.class) {
	        		somedayDateStart.set(obj, DateUtils.getStartTime());
		        }
		        if (somedayDateEnd.getType() == Date.class) {
		        	somedayDateEnd.set(obj, DateUtils.getEndTime());
		        }
	        	//为空数据库取昨天数据
//	        	String yesterday = DateUtils.getBeforeDays(new Date(), 1);
//	        	if (somedayDateStart.getType() == Date.class) {
//	        		somedayDateStart.set(obj, DateUtils.parseDate(yesterday + " 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
//		        }
//		        if (somedayDateEnd.getType() == Date.class) {
//		        	somedayDateEnd.set(obj, DateUtils.parseDate(yesterday + " 23:59:59", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
//		        }
	        }
	       
	        return obj;
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
		return obj;
	}
	
	/**
	 * 验证条件是否含当天
	 * @Title: isExistToday 
	 * @date 2017年8月15日 下午2:01:29 
	 * @version V1.0
	 */
	public static boolean  existToday(Object obj){
		 boolean exist = false;
		 String s = obj.getClass().getName();
	     try {
			Class<?> clazz = Class.forName(s).getSuperclass();
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	     return exist;
	}
	
	
	/**
	 * 设置时间范围查询条件
	 * @Title: setDateCondition 
	 * @date 2017年8月1日 上午10:29:43 
	 * @version V1.0
	 */
	public static Date [] setDateCondition(QueryVo query,String type){
		Date [] dates = null;
		switch (type) {
			case "startDate":
				dates = new Date [1]; 
				if(StringUtils.isNotBlank(query.getStartDate())){
					dates[0] = DateUtils.parseDate(query.getStartDate()+" 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
				}else{
					dates[0] = DateUtils.parseDate(DateUtils.getBeforeDays(new Date(), 8)+" 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
				}
				break;
			case "endDate":
				dates = new Date [1]; 
				if(StringUtils.isNotBlank(query.getEndDate())){
					dates[0] = DateUtils.parseDate(query.getEndDate()+" 23:59:59", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
				}else{
					dates[0] = DateUtils.parseDate(DateUtils.getBeforeDays(new Date(), 1)+" 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
				}
				break;	
				
			case "contrastBefourDate":
				dates = new Date [2]; 
				if(StringUtils.isNotBlank(query.getContrastBefourDate())){
					dates[0] = DateUtils.parseDate(query.getContrastBefourDate()+" 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
					dates[1] = DateUtils.parseDate(query.getContrastBefourDate()+" 23:59:59", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
				}else{
					dates[0] = DateUtils.getStartTime();
					dates[1] = DateUtils.getEndTime();
				}
				break;
			case "contrastAfterDate":
				dates = new Date [2]; 
				if(StringUtils.isNotBlank(query.getContrastAfterDate())){
					dates[0] = DateUtils.parseDate(query.getContrastAfterDate()+" 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
					dates[1] = DateUtils.parseDate(query.getContrastAfterDate()+" 23:59:59", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
				}else{
					dates[0] = DateUtils.getStartTime();
					dates[1] = DateUtils.getEndTime();
				}
				break;	
			case "somedayDate":
				dates = new Date [2]; 
				if(StringUtils.isNotBlank(query.getSomedayDate())){
					dates[0] = DateUtils.parseDate(query.getSomedayDate()+" 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
					dates[1] = DateUtils.parseDate(query.getSomedayDate()+" 23:59:59", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
				}else{
					dates[0] = DateUtils.getStartTime();
					dates[1] = DateUtils.getEndTime();
				}
				break;		
		default:
			break;
		}
		return dates;
	}
	
	public static Date reportDate(String dateStr,String type){
		Date date = null;
		switch (type) {
		case "startDate":
			if(StringUtils.isNotBlank(dateStr)){
				date = DateUtils.parseDate(dateStr+" 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
			}
			break;
		case "endDate":
			if(StringUtils.isNotBlank(dateStr)){
				date = DateUtils.parseDate(dateStr+" 23:59:59", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
			}
			break;
		default:
			break;
		}
		return date;
	}
	
	/**
	 * 查询条件
	 * @Title: commonQueryCondition 
	 * @param @param query
	 * @param @return 
	 * @return OperationRealtimeDay
	 */
	public static Date dateConditionScope(QueryVo query,String type){
		Date date = null;
		switch (type) {
			case "startDate":
				if(StringUtils.isNotBlank(query.getStartDate())){
					date = DateUtils.parseDate(query.getStartDate()+" 00:00:00", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
				}
				break;
			case "endDate":
				if(StringUtils.isNotBlank(query.getEndDate())){
					date = DateUtils.parseDate(query.getEndDate()+" 23:59:59", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
				}
				break;
			default:
				break;
			}
		return date;
	}
	
}
