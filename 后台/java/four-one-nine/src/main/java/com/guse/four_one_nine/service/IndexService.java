package com.guse.four_one_nine.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.DayDealCountDao;
import com.guse.four_one_nine.dao.DayUserCountDao;
import com.guse.four_one_nine.dao.ServerOrderDao;
import com.guse.four_one_nine.dao.UserDao;

/** 
* @ClassName: IndexService 
* @Description: 首页信息服务类
* @author Fily GUSE
* @date 2018年1月17日 下午2:04:36 
*  
*/
@Service
public class IndexService {
	
	@Autowired
	DayUserCountDao userCountDao;
	@Autowired
	DayDealCountDao dealCountDao;
	@Autowired
	UserDao userDao;
	@Autowired
	ServerOrderDao orderDao;
	
	/** 
	* @Description: 用户信息统计 
	* @param @return
	* @return Map<String,Integer> 
	* @throws 
	*/
	public Map<String, Integer> userCount() {
		return userCountDao.count();
	}
	
	/** 
	* @Description: 交易信息统计 
	* @param @return
	* @return Map<String,Integer> 
	* @throws 
	*/
	public Map<String, Integer> dealCount() {
		return dealCountDao.count();
	}
	
	/** 
	* @Description: 用户来源统计 
	* @param @return
	* @return List<Map<String,Integer>> 
	* @throws 
	*/
	public List<Map<String, Integer>> countSource() {
		return userDao.countSource();
	}
	
	/** 
	* @Description: 用户分布统计
	* @param @return
	* @return List<Map<String,Integer>> 
	* @throws 
	*/
	public List<Map<String, Integer>> countCity() {
		return userDao.countCity();
	}

	/** 
	* @Description: 用户年龄统计 
	* @param @param ages 年龄段
	* @param @return
	* @return List<Map<String,Integer>> 
	* @throws 
	*/
	public List<Map<String, Integer>> countAge(String[] ages) {
		String stageSql = "";
		for(String str : ages) {
			stageSql += "WHEN age";
			String[] stage = str.split("-");
			if(stage.length == 2){
				stageSql += " BETWEEN "+stage[0]+" AND " +stage[1] + " THEN '"+str+"岁'";
			} else {
				if(str.indexOf("-") == -1) {
					stageSql += " < "+stage[0]+" THEN '"+stage[0]+"岁以下'";
				} else {
					stageSql += " > "+stage[0]+" THEN '"+stage[0]+"岁以上'";
				}
			}
		}
		return userDao.countAge(stageSql);
	}

	/** 
	* @Description: 买家交易排行榜 
	* @param @return
	* @return Object 
	* @throws 
	*/
	public List<Map<String, Object>> rankingBuy() {
		return orderDao.rankingBuy();
	}

	/** 
	* @Description: 卖家交易排行榜 
	* @param @return
	* @return List<Map<String,Object>> 
	* @throws 
	*/
	public List<Map<String, Object>> rankingSell() {
		return orderDao.rankingSell();
	}

}
