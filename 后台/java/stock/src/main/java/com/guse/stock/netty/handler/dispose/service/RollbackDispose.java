package com.guse.stock.netty.handler.dispose.service;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.guse.stock.common.JSONUtils;
import com.guse.stock.dao.IStockCountsDao;
import com.guse.stock.dao.IStockDao;
import com.guse.stock.dao.IStockUseLogDao;
import com.guse.stock.dao.IUserAuthoriseDao;
import com.guse.stock.dao.model.Stock;
import com.guse.stock.dao.model.StockCounts;
import com.guse.stock.dao.model.StockUseLog;
import com.guse.stock.netty.handler.MessageCode;
import com.guse.stock.netty.handler.dispose.AbstractDispose;

/** 
* @ClassName: OutputDispose 
* @Description: 回滚
* @author Fily GUSE
* @date 2017年8月28日 下午5:44:31 
*  
*/
@Component
public class RollbackDispose extends AbstractDispose {
	/** 
	* @Fields pt : 协议号
	*/
	public Integer pt = 8;
	
	@Autowired
	private IUserAuthoriseDao dao;
	
	@Autowired
	private IStockUseLogDao stockUseLogDao;
	
	@Autowired
	private IStockDao stockDao;
	
	@Autowired
	private IStockCountsDao stockCountsDao;
	
	
	@Override
	@Transactional
	public void processing() {
		// 获取客户端请求参数
		String data  = msg.getData();
		JSONObject object = JSONUtils.toJSONObject(data);
		String user_id = object.getString("user_id"); //用户id
		String orderid = object.getString("orderid"); //订单编号
		String status = object.getString("status"); //状态
		String stockid = object.getString("stockid"); //库存id
		
		if(StringUtils.isBlank(user_id)) {
			// 如果用户为空,直接返回用户为空数据错误信息
			msg.setCode(MessageCode.NULLUSER);
			return;
		} else if(StringUtils.isBlank(stockid)) {
			// 如果库存id为空,直接返回库存id为空数据错误信息
			msg.setCode(MessageCode.NULLSTOCK);
			return;
		} else if(StringUtils.isBlank(orderid)) {
			// 如果订单为空,直接返回订单为空数据错误信息
			msg.setCode(MessageCode.NULLORDERID);
			return;
		} else if(StringUtils.isBlank(status)) {
			// 如果状态为空,直接返回状态为空数据错误信息
			msg.setCode(MessageCode.NULLSTATUS);
			return;
		}
		// 获取出库记录
		StockUseLog stockUseLog = stockUseLogDao.findByOrder(orderid);
		if(stockUseLog == null) {
			// 如果订单不存在，直接返回订单不存在数据错误信息
			msg.setCode(MessageCode.NOORDERID);
			return;
		}
		int state = Integer.parseInt(status);
		if(state == 0) {
			// 如果状态为0，直接返回状态为0数据错误信息
			msg.setCode(MessageCode.ZEROSTATUS);
			return;
		} else if (stockUseLog.getStatus() == state ) {
			// 如果状态与数据库状态一致，直接返回状态与数据库状态一致数据错误信息
			msg.setCode(MessageCode.DBEQULSTATUS);
			return;
		} else if(state != 1 && state != 2 && state != 3) {
			// 如果不是1,2,3则是未知状态，直接返回状态与数据库状态一致数据错误信息
			msg.setCode(MessageCode.UNKNOWNSTATUS);
			return;
		}
		// 根据订单获取库存
		Stock stock = stockDao.findByStock(stockUseLog.getStock_id());
		if(stock == null) {
			// 如果订单获取库存失败，直接返回订单获取库存失败数据错误信息
			msg.setCode(MessageCode.GETERRORSTOCKBYORDER);
			return;
		} 
		
		// 获取回滚次数
		int roll_num = stock.getBack_rolling_num();
		// 获取库存统计
		StockCounts stockCounts = stockCountsDao.findByUserGamePer(stock.getUser_id(), stock.getGame_id(), stock.getPar_id());
		if(stockCounts == null) {
			// 如果库存统计为空，直接返回库存统计为空数据错误信息
			msg.setCode(MessageCode.NULLSTOCKCOUNTS);
			return;
		}
		// 获取库存统计中的已使用数量
		long useCount = stockCounts.getUsed_cnt();
		// 1：已使用  3：可能未到账 更新库存状态为已使用，出库记录为对应状态，已使用数量+1
		if(status.equals("1") || status.equals("3")) {
			stock.setIs_use(1);
			stockUseLog.setStatus(state);
			stockCounts.setUsed_cnt(useCount+1);
			//回滚次数超过3次了的，将库存状态设置为已使用，出库记录设置为可能未到账，已使用数量+1
		} else if(status.equals("2") && stock.getBack_rolling_num() >= 3) {
			stock.setIs_use(1);
			stockUseLog.setStatus(3);
			stockCounts.setUsed_cnt(useCount+1);
			// 如果回滚超3次，直接返回回滚超3次数据错误信息
			msg.setCode(MessageCode.OVERSTEPTHREE);
			//2：未到账 更新出库记录对象，同时将回滚次数增加
		} else if(status.equals("2")) {
			stockUseLog.setStatus(state);
			stock.setBack_rolling_num(roll_num+1);
		}
		
		// 更新库存信息
		int stockResult = stockDao.updateStock(stock);
		if(stockResult < 1) {
			// 如果更新库存失败，直接返回更新库存失败数据错误信息
			msg.setCode(MessageCode.UPDATEERRORSTOCK);
			return ;
		}
		// 更新出库记录信息
		int useLogResult  = stockUseLogDao.updateStockUseLog(stockUseLog);
		if(useLogResult < 1) {
			// 如果更新出库记录失败，直接返回更新出库记录失败数据错误信息
			msg.setCode(MessageCode.UPDATEERRORSTOCKUSELOG);
			return;
		}
		// 更新库存统计信息
		int useCountResult  = stockCountsDao.updateStockCounts(stockCounts);
		if(useCountResult < 1) {
			// 如果更新库存统计失败，直接返回更新库存统计失败数据错误信息
			msg.setCode(MessageCode.UPDATEERRORSTOCKCOUNTS);
			return;
		}
		
		// 设置响应参数
		msg.setData("");
	}

}
