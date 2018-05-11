package com.guse.stock.netty.handler.service;

import java.util.Date;

import io.netty.channel.Channel;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guse.stock.common.Base64Util;
import com.guse.stock.dao.IGameAutomaticlyOrderDao;
import com.guse.stock.dao.IGameDao;
import com.guse.stock.dao.IGameParDao;
import com.guse.stock.dao.IGameUserInfoDao;
import com.guse.stock.dao.IUserDao;
import com.guse.stock.dao.model.Game;
import com.guse.stock.dao.model.GameAutomaticlyOrder;
import com.guse.stock.dao.model.GameUserInfo;
import com.guse.stock.netty.handler.ISession;
import com.guse.stock.netty.handler.MessageCode;
import com.guse.stock.netty.handler.WSSession;
import com.guse.stock.netty.handler.service.UserInfo;


/** 
 * 业务处理服务类
* @ClassName: HandlerService 
* @author Fily GUSE
* @date 2017年9月28日 下午4:17:38 
*  
*/
@Component
public class HandlerService {
	private final static Logger logger = LoggerFactory.getLogger(HandlerService.class);
	
	// 心跳类型
	public static final int TYPE_IDLE = 0;
	// 登录类型
	public static final int TYPE_LOGIN = 1;
	// 保存信息类型
	public static final int TYPE_SAVE = 2;
	// 下单后返回消息
	public static final int TYPE_RESULT_ORDER = 3;
	// uid注册
	public static final int TYPE_UIDREGISTER = 5;
	// 单个登录，通知下线
	public static final int TYPE_OFF_LINE = 6;
	
	// 操作类型
	public int actionType;
	// 连接信息
	public Channel channel;
	
	@Autowired
	IGameDao gameDao;
	@Autowired
	IGameUserInfoDao guiDao;
	@Autowired
	IGameAutomaticlyOrderDao autoOrderDao;
	@Autowired
	IGameParDao gameParDao;
	@Autowired
	IUserDao userDao;
	
	
	/** 
	* @Title: idle 
	* @Description: 心跳 
	* @param @param data
	* @return void 
	* @throws 
	*/
	public void idle(JSONObject data) {
		response(MessageCode.SUCCESS);
	}
	
	/** 
	* @Title: uidRegister 
	* @Description: 用户id注册 
	* @param @param data
	* @return void 
	* @throws 
	*/
	public void uidRegister(JSONObject data) {
		// 获取参数
		String hash = data.getString("hash");
		String device_code = data.getString("device_code");
		
		// 判断用户是否存在
		Integer uid = userDao.getUidByHash(hash);
		if(uid == null) {
			response(MessageCode.NOTUSER);
			return;
		}
		
		data.clear();
		data.put("code", "0");
		// 通知 其他正在使用的设备下线
		Channel oldChannel = ISession.getChannelByuid((long)uid);
		if(oldChannel != null) {
			sendStockMsg(oldChannel, TYPE_OFF_LINE, data);
		}
		// 添加当前连接信息到session
		UserInfo userInfo = new UserInfo();
		userInfo.uid = (long)uid;
		userInfo.hash = hash;
		userInfo.device_number = device_code;
		logger.info("client register. uid:{},hash:{},device_number:{} ", uid, hash, device_code);
		// 更新用户信息到管道
		ISession.updateUser(channel, userInfo);
		// 返回信息
		sendStockMsg(this.channel, TYPE_LOGIN, data);
	}
	
	/** 
	* @Title: saveUserInfo 
	* @Description: 保存用户信息 
	* @param @param data
	* @return void 
	* @throws 
	*/
	public void saveUserInfo(JSONObject data) {
		try {
			// 获取参数信息
			Integer uid = userDao.getUidByHash(data.getString("hash"));
			if(uid == null) {
				response(MessageCode.NOTUSER);
				return;
			}
			String game_id = data.getString("bid"); //游戏id
			int client_type = Integer.parseInt(data.getString("client_type")); //登录类型.1微信，2qq
			String account = data.getString("account"); //登录帐号
			data.remove("type");
			data.remove("hash");
			data.remove("client_type");
			data.remove("account");
			String info = data.toString();
			
			// 获取游戏信息
			Game game = gameDao.findByIdentify(game_id);
			// 保存用户信息
			String param = (client_type==1?"weixin" : "qq") +" is not null";
			GameUserInfo gui = guiDao.getUserInfo(uid, game.getGame_id(), param);
			if(gui == null) {
				gui = new GameUserInfo();
				gui.setUser_id((long)uid);
				gui.setGame_id(game.getGame_id());
				if(client_type == 1) {
					gui.setWeixin(account);
				} else {
					gui.setQq(account);
				}
				gui.setInfo(info);
				
				guiDao.addGameUserInfo(gui);
			} else {
				if(client_type == 1) {
					gui.setWeixin(account);
				} else {
					gui.setQq(account);
				}
				gui.setInfo(info);
				
				guiDao.updateInfo(gui);
			}
		} catch(Exception e) {
			logger.error("信息错误:[{}]", data);
		}
		// 返回客服端
		response(MessageCode.SUCCESS);
	}
	
	/** 
	* @Title: resultOrder 
	* @Description: 下单后返回消息 
	* @param @param data
	* @return void 
	* @throws 
	*/
	public void resultOrder(JSONObject data) {
		// 获取参数
		int order_id = Integer.parseInt(data.getString("order_id")); // 订单id
		int status = Integer.parseInt(data.getString("status")); // 下单状态
		String status_coment = data.get("status_coment")==null ? null:data.getString("status_coment"); // 下单状态说明
		
		// 跟新订单状态
		GameAutomaticlyOrder autoOrder = autoOrderDao.getByOrderId(order_id);
		autoOrder.setStatus(status);
		autoOrder.setStatus_coment(status_coment);
		autoOrder.setSuccess_time(new Date().getTime() / 1000);
		autoOrderDao.updateOrderStatus(autoOrder);
		
		// 更新用户为空闲状态
		Channel channel = ISession.getChannelByuid(autoOrder.getUser_id());
		if(channel != null){
			UserInfo userinfo = ISession.getUserInfo(channel);
			if(userinfo != null) {
				userinfo.status = 0;
				WSSession.informClientByUid(userinfo.uid, order_id, status, status_coment);
			}
		}
		
	}
	
	
	/* 响应客服端  */
	public void response(MessageCode msg) {
		JSONObject json = new JSONObject();
		json.put("code", msg.getCode() +"");
		if(!msg.equals(MessageCode.SUCCESS)) {
			json.put("msg", msg.getDes());
		}
		
		sendStockMsg(this.channel, this.actionType, json);
	}
	/* 响应客服端  */
	public void response(JSONObject data) {
		JSONObject json = new JSONObject();
		json.put("code", MessageCode.SUCCESS.getCode()+"");
		
		json.putAll(data);
		
		sendStockMsg(this.channel, this.actionType, json);
	}
	
	
	/** 
	* @Title: sendStockMsg 
	* @Description: 发送消息到客服端 
	* @param @param channel
	* @param @param type
	* @param @param data
	* @return void 
	* @throws 
	*/
	public static void sendStockMsg(Channel channel, int type, JSONObject data) {
		data.put("type", type+"");
		String msg = data.toString();
		logger.info("send client[{}] msg===>:{}",channel.remoteAddress().toString(), msg);
		msg = Base64Util.encode(msg.getBytes());
		msg = msg.replaceAll("[\\s*\t\n\r]", "");
		msg = "["+msg+"]";
		// 默认长度
		int size = 500;
		int nowSize = 0;
		
		int length = msg.length();
		while (nowSize < length) {
			int getSize = ((length-nowSize) < size ? (length-nowSize) : size) + nowSize;
			channel.writeAndFlush(msg.substring(nowSize, getSize));
			nowSize += size;
		}
	}
}
