package com.guse.apple_reverse.netty.handler;

import com.guse.apple_reverse.Main.ServiceStart;
import com.guse.apple_reverse.common.Base64Util;
import com.guse.apple_reverse.common.JSONUtils;
import com.guse.apple_reverse.netty.handler.session.ClientSession;
import com.guse.apple_reverse.netty.handler.session.ClientSessionUtil;
import com.guse.apple_reverse.netty.handler.session.Message;
import com.guse.apple_reverse.service.socket.SocketService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/** 
* @ClassName: SocketServerHandler 
* @Description: Socket 消息处理
* @author Fily GUSE
* @date 2017年11月28日 下午12:58:23 
*  
*/
public class SocketServerHandler extends ChannelInboundHandlerAdapter {
	
	/**
	 * 接收消息
	 */
	private String message = "";
	// 定义消息头和消息尾
	private static String startStr = "[";
	private static String endStr = "]";
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
		loss_connect_time = 0;
		// 消息累加
		message += obj.toString();
		do {
			// 判断是否有完整消息
			int end = message.indexOf(endStr);
			if (end == -1) {
				break;
			}
			// 获取一条完整的消息
			String temp = message.substring(0, end + 1);
			int start = temp.lastIndexOf(startStr);
			String dateMessage = temp.substring(start);
			String data = dateMessage.substring(1, dateMessage.length() - 1);
			// 清除已处理的消息
			message = message.replace(dateMessage, "");

			// 消息解码
			data = new String(Base64Util.decode(data));
			Message msg = JSONUtils.toBean(data, Message.class);
			if(msg == null || msg.getType() == null) {
				continue;
			}
			String clientName = ctx.channel().remoteAddress().toString();
			if(ClientSessionUtil.getClient(ctx.channel()) != null) {
				clientName = ClientSessionUtil.getClient(ctx.channel()).getName();
			}
			ServiceStart.INFO_LOG.info("reception Message. sender:[{}]; data===>:[{}]", clientName, JSONUtils.toJSONString(msg));
			
			switch(msg.getType()) {
			case Message.CLIENT_THREAD_NUM: // 当前客服端使用线程数
				ClientSession client = JSONUtils.toBean(msg.getData(), ClientSession.class);
				ClientSessionUtil.addClient(ctx.channel(), client);
				break;
			default: // 其他消息都是查询结果
				SocketService service = new SocketService(msg, ctx.channel());
				service.start();
				break;
			}
		} while(true);
	}
	
	/**
	 * 异常退出
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	/**
	 * 客户端连接时添加到客服端管理
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String client = ctx.channel().remoteAddress().toString();
		ServiceStart.CONSOLE_LOG.info("{}->connect.....", client);
	}
	
	/**
	 * 客服端不活跃
	 */
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		//channel失效处理,客户端下线或者强制退出等任何情况都触发这个方法
		ClientSessionUtil.removeClient(ctx.channel());
		String client = ctx.channel().remoteAddress().toString();
		ServiceStart.CONSOLE_LOG.info("{}->leave.....", client);
		
		super.channelInactive(ctx);
    }
	
	// 心跳监测
	private int loss_connect_time = 0;  
    @Override  
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {  
        if (evt instanceof IdleStateEvent) {  
            IdleStateEvent event = (IdleStateEvent) evt;  
            if (event.state() == IdleState.READER_IDLE) {  
                loss_connect_time++;  
                if (loss_connect_time > 2) {  
                	ServiceStart.CONSOLE_LOG.error("client[{}] can't connection", ctx.channel().remoteAddress().toString());
                    ClientSessionUtil.removeClient(ctx.channel());
                    ctx.channel().close();  
                } else {
                	Message msg = new Message(Message.CLIENT_THREAD_NUM, "");
                	Message.sendMessage(ctx.channel(), msg);
                }
            }  
        } else {  
            super.userEventTriggered(ctx, evt);  
        }  
    } 
}
