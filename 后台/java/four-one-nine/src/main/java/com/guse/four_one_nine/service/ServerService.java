package com.guse.four_one_nine.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


//import com.guse.four_one_nine.common.DateUtil;
import com.guse.four_one_nine.common.JSONUtil;
import com.guse.four_one_nine.controller.page.ResponseAjax;
import com.guse.four_one_nine.dao.ClassifyDao;
import com.guse.four_one_nine.dao.DayDealCountDao;
import com.guse.four_one_nine.dao.ServerCommentDao;
import com.guse.four_one_nine.dao.ServerDao;
import com.guse.four_one_nine.dao.ServerOrderDao;
import com.guse.four_one_nine.dao.model.Classify;
import com.guse.four_one_nine.dao.model.Server;
import com.guse.four_one_nine.dao.model.Union;

/** 
* @ClassName: ServerService 
* @Description: 服务业务类
* @author Fily GUSE
* @date 2018年1月10日 下午2:31:14 
*  
*/
@Service
public class ServerService {
	public final static Logger logger = LoggerFactory.getLogger(ServerService.class);
	
	@Autowired
	ServerDao dao;
	@Autowired
	DayDealCountDao dayDao;
	@Autowired
	ClassifyDao classifyDao;
	@Autowired
	ServerOrderDao orderDao;
	@Autowired
	ServerCommentDao commentDao;
	@Autowired
	AppInformService appService;

	/** 
	* @Description: 服务统计信息 
	* @param 
	* @return void 
	* @throws 
	*/
	public Map<String, Integer> getCount() {
		
		return dayDao.count();
	}
	
	/** 
	* @Description: 分类统计信息 
	* @param 
	* @return void 
	* @throws 
	*/
	public List<Map<String, Object>> getClassifyCount() {
		// 分类基础统计
		List<Map<String, Object>> list = classifyDao.findClassifyCount();
		// 本周流水
		if(list != null && list.size() > 0) {
			for(Map<String, Object> map : list) {
				// 默认销量
				Object [] sales = {0,0,0,0,0,0,0};
				// 本周销量统计
				List<Map<String, Integer>> weekCount = classifyDao.findWeekSales(Integer.parseInt(map.get("id").toString()));
				if(weekCount != null && weekCount.size() > 0) {
					for(Map<String, Integer> dayCount : weekCount) {
						int dayNum = Integer.parseInt(dayCount.get("weekNum").toString());
						sales[dayNum] = dayCount.get("serverMoney");
					}
				}
				map.put("sales", JSONUtil.toJSONArray(sales));
			}
		}
		
		return list;
	}
	
	/** 
	* @Description: 获取未分类的服务 
	* @param 
	* @return void 
	* @throws 
	*/
	public List<Server> getUnClassify() {
		return dao.findUnClassify();
	}
	
	/** 
	* @Description: 添加分类 
	* @param @param name
	* @param @param ids
	* @param @return
	* @return String 
	* @throws 
	*/
	@Transactional
	public void addClassify(String name, String code, ResponseAjax response) {
		name = name.trim();
		Classify classify = classifyDao.getByName(name);
		if(classify == null) {
			// 新建分类
			classify = new Classify();
			classify.setClassify_name(name);
			classify.setClassify_code(code);
			classify.setCreater(response.getUser().getName());
			classify.setCreate_time(new Date());
			classifyDao.addClassify(classify);
		}
		//dao.updateClassify(classify);
		
		// 通知app
		try {
			pushServerClassfly(classify);
		} catch (Exception e) {
			response.setFailure(e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return;
		}
	}
	/** 
	* @Description: 服务归类
	* @param @param name
	* @param @param ids
	* @param @return
	* @return String 
	* @throws 
	*/
	@Transactional
	public void addServerClassify(String name,String ids, ResponseAjax response) {
		name = name.trim();
		Classify classify = classifyDao.getByName(name);
		dao.updateClassify(classify.getId(),ids);
		// 通知app
		try {
			pushServerClassfly(classify,ids);
		} catch (Exception e) {
			response.setFailure(e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return;
		}
	}
	
	/** 
	* @Description: 获取全部分类信息 
	* @param @return
	* @return List<Classify> 
	* @throws 
	*/
	public List<Classify> getClassifys() {
		
		return classifyDao.findAll();
	}
	
	/** 
	* @Description: 获取服务信息 
	* @param @param id
	* @param @return
	* @return Server 
	* @throws 
	*/
	public Server getService(long id) {
		
		return dao.findById(id);
	}
	
	/** 
	* @Description: 服务统计信息 
	* @param @param id
	* @param @return
	* @return Map<String,Integer> 
	* @throws 
	*/
	public Map<String, Integer> getServerCount(long id) {
		
		return dao.serverCount(id);
	}
	
	/** 
	* @Title: getOrders 
	* @Description: 获取服务订单 
	* @param @param id
	* @param @return
	* @return List<Map<String,Object>> 
	* @throws 
	*/
	public List<Map<String, Object>> getOrders(long id) {
		
		return orderDao.findByServerId(id);
	}
	
	/** 
	* @Title: getComments 
	* @Description: 获取服务评价 
	* @param @param id
	* @param @return
	* @return List<Map<String,Object>> 
	* @throws 
	*/
	public List<Map<String, Object>> getComments(long id) {
		
		return commentDao.findByServerId(id);
	}
	
	
	
	/************************************************
	 ***************** 推送app数据组装 ******************
	 ***********************************************/
	/** 
	* @Description: 服务分类 
	* @param @param classify 分类信息
	* @param @param ids 服务id集合
	* @param @throws Exception
	* @return void 
	* @throws 
	*/
	private void pushServerClassfly(Classify classify) throws Exception{
		String business = "gs-cloud-web-moc/serviceLabel/action";
		JSONObject params = new JSONObject();
		params.put("labelId", classify.getId());
		params.put("labelName", classify.getClassify_name());
		params.put("labelCode", classify.getClassify_code());
		params.put("action", 1);
	/*	params.put("servers", ids);
		params.put("action_time", DateUtil.formatCurrentDate(new Date()));*/
		
		params = appService.sendMessage(business, params);
		if(params.getInt("code") != 0) {
			String msg = params.getString("msg");
			logger.error("推送消息到app出现异常,接口：[{}], 异常提示：[{}], 接口参数:[{}]", business, msg, params.toString());
			throw new Exception(msg);
		}
	}
	

	/** 
	* @Description: 服务分类 
	* @param @param classify 分类信息
	* @param @param ids 服务id集合
	* @param @throws Exception
	* @return void 
	* @throws 
	*/
	private void pushServerClassfly(Classify classify,String ids) throws Exception{
		String business = "gs-cloud-web-moc/service/classfly";
		JSONObject params = new JSONObject();
		params.put("labelCode", classify.getClassify_code());
		params.put("servers", ids);
		
		params = appService.sendMessage(business, params);
		if(params.getInt("code") != 0) {
			String msg = params.getString("msg");
			logger.error("推送消息到app出现异常,接口：[{}], 异常提示：[{}], 接口参数:[{}]", business, msg, params.toString());
			throw new Exception(msg);
		}
	}
}
