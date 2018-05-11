package com.guse.four_one_nine.service;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.guse.four_one_nine.controller.page.ResponseAjax;
import com.guse.four_one_nine.dao.SensitiveDao;
import com.guse.four_one_nine.dao.model.Sensitive;

/** 
* @ClassName: SensitiveService 
* @Description: 敏感词服务类
* @author Fily GUSE
* @date 2018年1月12日 下午1:54:31 
*  
*/
@Service
public class SensitiveService {
	public final static Logger logger = LoggerFactory.getLogger(SensitiveService.class);
	
	@Autowired
	SensitiveDao dao;
	@Autowired
	AppInformService appService;
	
	/** 
	* @Description: 保存信息 
	* @param @param sensitive
	* @param @return
	* @return String 
	* @throws 
	*/
	@Transactional
	public void save(Sensitive data, ResponseAjax response) {
		int action = SENSITIVEACTIONACTION_ADD;
		if(data.getId() == null) {
			data.setCount_no(new Long(0));
			dao.addSensitive(data);
		} else {
			dao.save(data);
			action = SENSITIVEACTIONACTION_UPD;
		}
		
		try {
			pushSensitiveAction(data, action);
		} catch (Exception e) {
			response.setFailure(e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
	}
	
	/** 
	* @Description: 根据id获取信息 
	* @param @param id
	* @param @return
	* @return Sensitive 
	* @throws 
	*/
	public Sensitive getById(long id) {
		
		return dao.getSensitive(id);
	}
	
	
	/************************************************
	 ***************** 推送app数据组装 ******************
	 ***********************************************/
	/** 
	* @Description: 敏感词操作 
	* @param @param sensitive 敏感词信息
 	* @param @param action 操作类型
	* @param @throws Exception
	* @return void 
	* @throws 
	*/
	private static final int SENSITIVEACTIONACTION_ADD = 1;
	private static final int SENSITIVEACTIONACTION_UPD = 2;
	private static final int SENSITIVEACTIONACTION_DEL = 0;
	private void pushSensitiveAction(Sensitive sensitive, int action) throws Exception{
		String business = "gs-cloud-web-moc/sensitive/action";
		JSONObject params = new JSONObject();
		params.put("id", sensitive.getId());
		params.put("action", action);
		params.put("sensitive", sensitive.getWord_group());
		params.put("replace", sensitive.getReplace_word());
		
		params = appService.sendMessage(business, params);
		if(params.getInt("code") != 0) {
			String msg = params.getString("msg");
			logger.error("推送消息到app出现异常,接口：[{}], 异常提示：[{}], 接口参数:[{}]", business, msg, params.toString());
			throw new Exception(msg);
		}
	}

}
