package com.guse.stock.netty.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

import com.guse.stock.Main.ServiceStart;
import com.guse.stock.common.HttpUtil;
import com.guse.stock.common.JSONUtils;
import com.guse.stock.netty.handler.dispose.AbstractDispose;
import com.guse.stock.netty.handler.dispose.DisposeType;
import com.guse.stock.netty.handler.dispose.UserInfo;

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
	private static String endStr = "}";
	// 消息对象
	private String message = "";
	// 当前用户对象
	public UserInfo userInfo;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object obj)
			throws Exception {
		clientOvertimeMap.remove(ctx);//只要接受到数据包，则清空超时次数
		logger.info("Socket reception Message. channelAddress:{}, data:{}" , ctx.channel().remoteAddress(), obj.toString());
		String body = (String)obj;
		boolean end = body.indexOf(endStr) > -1;
		message += body;
		if(!end) {
			return;
		}
		
		
		logger.info("Socket Service working.....");
		// 客户端数据转成业务对象
		logger.info("Socket dispose Message. msg:{}", message.toString());
		Message msg = JSONUtils.toBean(message.toString(), Message.class);
		// 业务处理
		Class<? extends AbstractDispose> disposeClass = DisposeType.getDisposeClassBy(msg.getPt());
		if(disposeClass == null){
			msg.setError(MessageCode.UNKNOWNAGREEMENT);
			// 推送消息到客服端
			ctx.channel().writeAndFlush(JSONUtils.toJSONString(msg));
			return;
		}
		
		// 从spring获取处理对象
		AbstractDispose dispose = ServiceStart.factory.getBean(disposeClass);
		dispose.msg = msg;
		dispose.channel = ctx.channel();
		dispose.handler = this;
		// 执行方法
		dispose.dispose();
		// 重置消息对象
		message = "";
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	/**
	 * 客户端活跃
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ISession.addChannel(ctx.channel());
	}
	
	/**
	 * 客服端不活跃
	 */
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		//channel失效处理,客户端下线或者强制退出等任何情况都触发这个方法
		// 这里需要通知php用户已经下线了
		String client = ctx.channel().remoteAddress().toString();
		if(userInfo != null) {
			client += ":{uid:"+userInfo.getUid();
			client += "}";
			JSONObject json = new JSONObject();
			json.put("hash", userInfo.hash);
			json.put("device_number", userInfo.device_number);
			HttpUtil.phpApi("logout",json);
		}
		System.out.println(client + "->leave.....");
		
		ISession.delChannel(ctx.channel());
		
		super.channelInactive(ctx);
    }
	
	
	/****
	 * 
	 * 超时设置
	 * 
	 */
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
//                    ServerManager.sendPacketTo(new ClientHeartBeat(), ctx);  
                	clientOvertimeMap.put(ctx, (int)(overtimeTimes+1));
                }else{  
                	// 超时过多，直接关闭连接
                	ctx.close();
                }  
            }   
        }  
	}

}
