package com.guse.four_one_nine.service.util;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.util.BaseDao;

/** 
* @ClassName: BaseService 
* @Description: 基础数据操作 服务类
* @author Fily GUSE
* @date 2018年1月19日 下午5:50:11 
*  
*/
@Service
public class BaseService {
	
	@Autowired
	BaseDao dao;
	
	/** 
	* @Description: 插入数据 
	* @param @param t 数据
	* @param @param table 表名
	* @param @param id 主键
	* @return void 
	* @throws 
	*/
//	public void insert(Object t, String table, String id) {
//		// 获取对象所有属性
//		StringBuffer keys = new StringBuffer();
//		StringBuffer values = new StringBuffer();
//		int index = 0;
//		Field[] fields = t.getClass().getDeclaredFields();
//		for(Field f : fields) {
//			String fieldName = f.getName(); // 属性名
//			f.setAccessible(true); // 设置属性是可以访问的
//			if(fieldName.equals(id)) {
//				continue;
//			}
//			try {
//				// 获取值
//				Object o = f.get(t);
//				if(o != null) {
//					if(index > 0) {
//						keys.append(",");
//						values.append(",");
//					}
//					keys.append(fieldName);
//					values.append("#{t."+fieldName+"}");
//					index ++;
//				}
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			} 
//		}
//		
//		String script = "INSERT INTO `"+table+"`("+keys.toString()+") VALUES("+values.toString()+")";
//		dao.addT(t, script, id);
//	}
	
	/** 
	* @Description: 修改数据信息 
	* @param @param t 数据对象
	* @param @param table 表名
	* @param @param id 主键名
	* @param @return
	* @return int 
	* @throws 
	*/
	public int update(Object t, String table, String id) {
		// 获取对象所有属性
		StringBuffer setSql = new StringBuffer();
		int index = 0;
		Field[] fields = t.getClass().getDeclaredFields();
		for(Field f : fields) {
			String fieldName = f.getName(); // 属性名
			f.setAccessible(true); // 设置属性是可以访问的
			if(fieldName.equals(id)) {
				continue;
			}
			try {
				// 获取值
				Object o = f.get(t);
				if(o != null) {
					if(index > 0) {
						setSql.append(",");
					}
					setSql.append(fieldName+"=#{t."+fieldName+"}");
					index ++;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} 
		}
		
		String script = "UPDATE `"+table+"` SET "+setSql+" WHERE "+id+"=#{t."+id+"}";
		return dao.updateT(t, script);
	}

}
