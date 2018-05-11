package com.guse.stock.netty.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

import com.guse.stock.Main.ServiceStart;
import com.guse.stock.common.Base64Util;
import com.guse.stock.common.JSONUtils;
import com.guse.stock.netty.handler.service.HandlerService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @ClassName: WebSocketServerHandler
 * @Description: 在服务端处理类中需要处理两种类型的消息，一种的HTTP请求，一种是WebSocket请求；
 *               因为WebSocket在建立连接时需要HTTP协议的参与，所有第一次请求消息是由HTTP消息承载
 * @author Fily GUSE
 * @date 2017年8月11日 下午2:58:47
 * 
 */
public class SocketServerHandler extends ChannelInboundHandlerAdapter {
	private final static Logger logger = LoggerFactory.getLogger(SocketServerHandler.class);
	
	// 定义消息头和消息尾
	private static String startStr = "[";
	private static String endStr = "]";
	
	// 消息对象
	private String message = "";
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object obj)
			throws Exception {
		clientOvertimeMap.remove(ctx);//只要接受到数据包，则清空超时次数
		// 消息累加
		message += obj.toString();
		
		do {
			// 判断是否有完整消息
			int end = message.indexOf(endStr);
			if(end == -1) {
				return;
			}
			// 获取一条完整的消息
			String temp = message.substring(0, end +1);
			int start = temp.lastIndexOf(startStr);
			String dateMessage = temp.substring(start);
			String data = dateMessage.substring(1, dateMessage.length() - 1);
			// 清除已处理的消息
			message = message.replace(dateMessage, "");
			
			// 消息解码
			data = new String(Base64Util.decode(data));
			logger.info("Socket reception Message. channelAddress:[{}]; data:{}" , ctx.channel().remoteAddress(), data);
			JSONObject json = JSONUtils.toJSONObject(data);
			if(json.isEmpty()) {
				continue;
			}
			// 业务处理类
			HandlerService service = ServiceStart.factory.getBean(HandlerService.class);
			// 获取操作类型
			int actionType = Integer.parseInt(json.getString("type"));
			service.actionType = actionType;
			service.channel = ctx.channel();
			
			try {
				// 处理其他业务
				switch (actionType) {
					case HandlerService.TYPE_IDLE:
						service.idle(json);
						break;
					case HandlerService.TYPE_SAVE: // 保存用户信息
						service.saveUserInfo(json);
						break;
					case HandlerService.TYPE_RESULT_ORDER: // 下单
						service.resultOrder(json);
						break;
					case HandlerService.TYPE_UIDREGISTER: // 用户注册
						service.uidRegister(json);
						break;
					default:
						service.response(MessageCode.UNKNOWNAGREEMENT);
						break;
				}
			} catch(Exception e) {
				e.printStackTrace();
				service.response(MessageCode.SERVERERROR);
			}
			
		} while(true);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	/**
	 * 客户端连接时添加到用户管理
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ISession.addChannel(ctx.channel());
		String client = ctx.channel().remoteAddress().toString();
		System.out.println(client + "->connect.....");
	}
	
	/**
	 * 客服端不活跃
	 */
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		//channel失效处理,客户端下线或者强制退出等任何情况都触发这个方法
		String client = ctx.channel().remoteAddress().toString();
		
		logger.info(client + "->leave.....");
		ISession.delChannel(ctx.channel());
		
		super.channelInactive(ctx);
    }
	
	/* 超时设置 */
	//客户端超时次数  
    private Map<ChannelHandlerContext,Integer> clientOvertimeMap = new ConcurrentHashMap<>();  
    private final int MAX_OVERTIME  = 3;  //超时次数超过该值则注销连接 
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		//心跳包检测读超时  
        if (evt instanceof IdleStateEvent) {  
            IdleStateEvent e = (IdleStateEvent) evt;  
            if (e.state() == IdleState.READER_IDLE) {  
                int overtimeTimes = clientOvertimeMap.containsKey(ctx) ? clientOvertimeMap.get(ctx) : 0;  
                if(overtimeTimes < MAX_OVERTIME){  
                	clientOvertimeMap.put(ctx, (int)(overtimeTimes+1));
                }else{  
                	// 超时过多，直接关闭连接
                	ctx.close();
                }  
            }   
        }  
	}

}
