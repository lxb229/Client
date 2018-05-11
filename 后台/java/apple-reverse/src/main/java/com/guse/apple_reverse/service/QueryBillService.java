package com.guse.apple_reverse.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.apple_reverse.Main.ServiceStart;
import com.guse.apple_reverse.dao.AppleAccountRecordDao;
import com.guse.apple_reverse.dao.AppleIdTableDao;
import com.guse.apple_reverse.dao.model.AppleAccountRecord;
import com.guse.apple_reverse.dao.model.AppleIdTable;
import com.guse.apple_reverse.netty.handler.session.Message;
import com.guse.apple_reverse.service.query.QueryService;

/** 
* @ClassName: QueryBillService 
* @Description: 查询账单详情
* @author Fily GUSE
* @date 2017年11月21日 下午7:23:24 
*  
*/
@Service
public class QueryBillService {
	
	@Autowired
	AppleIdTableDao appleDao;
	@Autowired
	AppleAccountRecordDao recordDao;
	
	/** 
	* @Title: service 
	* @Description: 业务处理 
	* @param @param id
	* @param @return
	* @return String 
	* @throws 
	*/
	public synchronized String service(int id) {
		AppleIdTable apple = appleDao.getById(id);
		if(apple == null) {
			return "信息不存在";
		}
		
		ServiceStart.CONSOLE_LOG.info("start exector query apple bill....");
		Date start = new Date();
		
		// 初始化查询结果信息
		QUERY_BILL_RESULT.clear();
		QUERY_BILL_RESULT.put(apple.getId(), "waiting");
		
		AppleAccountRecord record = recordDao.findById(apple.getId());
		if(record == null) {
			QUERY_BILL_RESULT.put(apple.getId(), "帐号信息不存在");
		} else if(StringUtils.isBlank(record.getBill_content())) {
			
			Message msg = Message.createQuery(apple, Message.SERVER_QUERY_BILL);
			// 开始查询
			new Thread(new QueryService(msg)).start();
			// 超时回调控制
			int count = 0;
			while("waiting".equals(QUERY_BILL_RESULT.get(apple.getId()))) {
				if(count > 33) {
					QUERY_BILL_RESULT.put(apple.getId(), "查询服务器繁忙，请稍后再试");
					break;
				}
				count ++;
				try {
					Thread.sleep(1000 * 5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		} else {
			QUERY_BILL_RESULT.put(apple.getId(), "OK");
		}
		
		ServiceStart.CONSOLE_LOG.info("exector query bill finish. result:{} . elapsed time:{}",QUERY_BILL_RESULT.get(apple.getId()) , (new Date().getTime() - start.getTime()));
		return QUERY_BILL_RESULT.get(apple.getId());
	}
	
	// 账单查询结果
	private static Map<Integer, String> QUERY_BILL_RESULT = new HashMap<Integer, String>();
	public static void setBillResult(int id, String result) {
		QUERY_BILL_RESULT.put(id, result);
	}
	
	
}
