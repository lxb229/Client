package com.guse.stock.netty.handler.service;

import io.netty.channel.Channel;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guse.stock.common.CertificateHelper;
import com.guse.stock.common.JSONUtils;
import com.guse.stock.common.StockCerUtil;
import com.guse.stock.dao.IGameAutomaticlyOrderDao;
import com.guse.stock.dao.IGameParDao;
import com.guse.stock.dao.IGameUserInfoDao;
import com.guse.stock.dao.IStockDao;
import com.guse.stock.dao.IStockUseLogDao;
import com.guse.stock.dao.model.GameAutomaticlyOrder;
import com.guse.stock.dao.model.GamePar;
import com.guse.stock.dao.model.GameUserInfo;
import com.guse.stock.dao.model.Stock;
import com.guse.stock.dao.model.StockCer;
import com.guse.stock.dao.model.StockUseLog;
import com.guse.stock.mongo.MongoStockCer;
import com.guse.stock.netty.handler.ISession;
import com.guse.stock.netty.handler.WSSession;

/**
 * @ClassName: HttpHandlerService
 * @Description: http 请求处理
 * @author Fily GUSE
 * @date 2017年9月28日 下午8:19:14
 * 
 */
@Component
public class HttpHandlerService {
	private final static Logger logger = LoggerFactory.getLogger(HttpHandlerService.class);

	@Autowired
	IGameAutomaticlyOrderDao autoOrderDao;
	@Autowired
	IGameUserInfoDao guiDao;
	@Autowired
	IGameParDao gameParDao;
	@Autowired
	IStockDao stockDao;
	@Autowired
	IStockUseLogDao stockUseLogDao;
	@Autowired
	MongoStockCer mongoStock;
	
	// 订单信息
	private long oId;
	// 被执行标识，默认未执行
	public boolean is_execute = false;
	
	/** 
	* @Title: service 
	* @Description: 服务方法 
	* @param @param order_id
	* @return void 
	* @throws 
	*/
	public void start(long order_id) {
		oId = order_id;
		is_execute = false;
		logger.info("start execute order:{}", oId);
		
		// 执行下单
		GameAutomaticlyOrder autoOrder = execute(true);
		// 订单存在，并且未执行
		if(!this.is_execute) {
			// 添加到排队队列
			OrderQueueService.addQueue(autoOrder.getUser_id(), this);
			// 当前排队号
			int index = OrderQueueService.indexQueue(autoOrder.getUser_id(), this) + 1;
			// 重试次数
			int tryCount = 0;
			while(!this.is_execute) {
				// 重试超时，2分钟
				tryCount ++;
				if(tryCount >= (22 * index)) {
					// 排队失败
					updateOrderStatus(autoOrder, GameAutomaticlyOrder.STATUS_FAILURE, "系统繁忙，请稍后再试");
					OrderQueueService.removeQueue(autoOrder.getUser_id(), this);
					return;
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				autoOrder = execute(false);
			}
		}
	}
	
	/** 
	* @Title: execute 
	* @Description: 执行下单 
	* @param 
	* @return void 
	* @throws 
	*/
	private GameAutomaticlyOrder execute(boolean frist) {
		// 获取订单信息
		GameAutomaticlyOrder autoOrder = autoOrderDao.getByOrderId(oId);
		if (autoOrder != null && (autoOrder.getStatus() == null || autoOrder.getStatus() != 2)) {
			// 获取登录用户信息
			Channel channel = ISession.getChannelByuid(autoOrder.getUser_id());
			UserInfo user = ISession.getUserInfo(channel);
			if(channel != null && user != null) {
				// 空闲时直接处理
				if(user.status == 0) {
					// 未排队或第一顺位才执行
					if(frist || (OrderQueueService.indexQueue(autoOrder.getUser_id(), this) == 0)){
						clientService(user, channel);
					}
				}
			} else {
				// 设置为用户不在线
				updateOrderStatus(autoOrder, GameAutomaticlyOrder.STATUS_OFF_LINE, "客服端未登录");
			}
		} else {
			this.is_execute = true;
		}
		return autoOrder;
	}
	
	/** 
	* @Title: clientService 
	* @Description: 通知客服端处理信息 
	* @param @param autoOrder
	* @param @param user
	* @param @param channel
	* @return void 
	* @throws 
	*/
	public void clientService(UserInfo user, Channel channel) {
		OrderQueueService.removeQueue(user.uid, this);
		this.is_execute = true;
		// 获取订单信息
		GameAutomaticlyOrder autoOrder = autoOrderDao.getByOrderId(oId);
		if (autoOrder == null || autoOrder.getStatus() == null || autoOrder.getStatus() == 2) {
			return;
		}
		// 更新用户为繁忙状态
		user.status = 1;
		
		JSONObject data = new JSONObject();
		// 获取用户游戏信息
		GameUserInfo gui = guiDao.getById(autoOrder.getGame_user_id());
		// 设置下单参数
		data.put("order_id", autoOrder.getId());
		// 设置价格信息
		insertParInfo(data, autoOrder);
		// 游戏信息提取
		JSONObject info = JSONUtils.toJSONObject(gui.getInfo());
		data.put("udid", info.get("udid")); 	data.put("key", info.get("key"));
		data.put("appid", info.get("appid")); 	data.put("uin", info.get("uin"));
		data.put("key_time", info.get("key_time"));
		info.remove("udid"); info.remove("key"); info.remove("appid");
		info.remove("uin"); info.remove("key_time");
		data.put("info", info.toString()); data.put("code", "0");
		// 提取凭证
		boolean if_success = true;
		if_success = insertIOS6(data, autoOrder);
		// 返回信息处理
		if(if_success) {
			HandlerService.sendStockMsg(channel, HandlerService.TYPE_RESULT_ORDER, data);
		} else {
			// 解除用户繁忙状态
			user.status = 0;
			// 用户凭证不存在
			updateOrderStatus(autoOrder, GameAutomaticlyOrder.STATUS_FAILURE, "用户凭证不存在");
		}
	}
	
	/** 
	* @Title: setParInfo 
	* @Description: 设置游戏面值信息 
	* @param @param data
	* @param @param autoOrder
	* @return void 
	* @throws 
	*/
	private void insertParInfo(JSONObject data, GameAutomaticlyOrder autoOrder) {
		// 获取面值信息
		GamePar gamePar = null;
		// 自动充值，获取该游戏最大面值
		if(autoOrder.getThird_party() == 1) {
			String param = " game_id="+autoOrder.getGame_id()+" AND par=(SELECT MAX(par) FROM pl_game_par where game_id="+autoOrder.getGame_id()+")";
			gamePar = gameParDao.findByParam(param);
			// 判断原单是否最大面额
			if (gamePar.getPar_id() == autoOrder.getPar_id()) {
				param = " game_id="+autoOrder.getGame_id()
						+" AND par=(SELECT MAX(par) FROM pl_game_par where game_id="+autoOrder.getGame_id()+" AND par_id <> "+gamePar.getPar_id()+")";
				gamePar = gameParDao.findByParam(param);
			}
		} else {
			gamePar = gameParDao.findById(autoOrder.getPar_id());
		}
		String price = "0.00"; // 价格
		if(gamePar.getPar() != null) {
			price = new DecimalFormat("######0.00").format(gamePar.getPar());
		}
		String peyitem = "0"; // 价值，只保留数字
		if(StringUtils.isNotBlank(gamePar.getVirtual_currency())) {
			String regEx="[^0-9]";
			Pattern p = Pattern.compile(regEx);   
			Matcher m = p.matcher(gamePar.getVirtual_currency());
			String number = m.replaceAll("").trim();
			if(StringUtils.isNotBlank(number)) {
				peyitem = number;
			}
		}
		data.put("productid", gamePar.getIdentify());
		data.put("price", price);
		data.put("payitem", peyitem);
	}
	
	/** 
	* @Title: insertIOS6 
	* @Description: 插入ios6凭证 
	* @param @param data
	* @param @param autoOrder
	* @param @return
	* @return boolean 
	* @throws 
	*/
	private boolean insertIOS6(JSONObject data, GameAutomaticlyOrder autoOrder) {
		// 凭证主表信息
		Stock stock = null;
		boolean updateCount = true;
		if(autoOrder.getStock_id() == null) {
			stock = stockDao.getStock(autoOrder);
			if(stock == null) {
				logger.error("in no stock. user_id:{},game_id:{},par_id:{}"
						, autoOrder.getUser_id(), autoOrder.getGame_id(), autoOrder.getPar_id());
				return false;
			}
			autoOrderDao.updateStock(stock.getStock_id(), autoOrder.getId());
		} else {
			stock = stockDao.getStockById(autoOrder.getStock_id());
			updateCount = false;
		}
		if(stock != null) {
			// 拼装出凭证对象集合名称
			String cerColletion = "pl_stock_cer"+(stock.getCer_table_num()==0? "" : stock.getCer_table_num());
			StockCer stockCer = null;
			try {
				stockCer = mongoStock.seachStockCer(cerColletion,stock.getStock_id());
				if(stockCer != null) {
					// 将数据库中的ios_6库存解密之后返回客户端
					String ios6 = stockCer.getIos_6();
					if(stock.getIs_encrypted() == 1) {
						CertificateHelper helper = new CertificateHelper();
						ios6 = helper.decrypt(ios6);
					}
					ios6 = ios6.replaceAll("/r/n", "");
					data.put("receipt", ios6);
					if(stock.getIs_use() == 0){
						//保存出库记录
						StockUseLog stockUseLog = new StockUseLog();
						stockUseLog.setNumber(new StockCerUtil().randomOrder());
						stockUseLog.setUser_id(autoOrder.getUser_id());
						stockUseLog.setStock_id(stock.getStock_id());
						stockUseLog.setStatus(1);
						stockUseLog.setPoundage(0d);
						stockUseLog.setMark("脱机:"+autoOrder.getOrder_no());
						stockUseLog.setCreate_time(System.currentTimeMillis()/1000);
						stockUseLogDao.addStockUseLog(stockUseLog);
						// 更新库存统计
						if(updateCount) {
							stockDao.updateCount(stock);	
						}
					}
					// 修改库存主表状态为已使用
					stockDao.updateStatus(stock.getStock_id());
				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
	
	
	/** 
	* @Title: updateOrderStatus 
	* @Description: 更订单状态状态 
	* @param @param autoOrder
	* @param @param status
	* @param @param coment
	* @return void 
	* @throws 
	*/
	private void updateOrderStatus(GameAutomaticlyOrder autoOrder, int status, String coment) {
		this.is_execute = true;
		autoOrder.setStatus(status);
		autoOrder.setStatus_coment(coment);
		autoOrder.setSuccess_time(new Date().getTime() / 1000);
		autoOrderDao.updateOrderStatus(autoOrder);
		logger.error("order failure id:{}. user_id:{},game_id:{},par_id:{}, coment:{}" ,autoOrder.getId()
				, autoOrder.getUser_id(), autoOrder.getGame_id(), autoOrder.getPar_id(), autoOrder.getStatus_coment());
		// 通知页面刷新
		WSSession.informClientByUid(autoOrder.getUser_id(), autoOrder.getId(), autoOrder.getStatus(), autoOrder.getStatus_coment());
	}
}
