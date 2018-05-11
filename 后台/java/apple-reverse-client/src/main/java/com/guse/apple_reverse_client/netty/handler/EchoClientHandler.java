package com.guse.apple_reverse_client.netty.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.guse.apple_reverse_client.Main.ServiceStart;
import com.guse.apple_reverse_client.common.Base64Util;
import com.guse.apple_reverse_client.common.JSONUtils;
import com.guse.apple_reverse_client.netty.QueryLineService;
import com.guse.apple_reverse_client.netty.handler.service.QueryService;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class EchoClientHandler extends SimpleChannelInboundHandler<String> {
	// 线程池
	ExecutorService pool = Executors.newCachedThreadPool();
	
	/**
	 * 接收到消息在这里处理
	 */
	private String message = "";
	// 定义消息头和消息尾
	private static String startStr = "[";
	private static String endStr = "]";
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String object)
			throws Exception {
		// 消息累加
		message += object;
		do{
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
			ServiceStart.INFO_LOG.info("reception Message. sender:[{}]; data===>:[{}]",ctx.channel().remoteAddress(), data);
			if(msg.getType() >= 200) {
				// 业务方法发送一条回执信息
				msg.setResutlCode(Message.RESULT_RESP);
				Message.sendMessage(msg);
				
				pool.submit(new QueryService(ServiceStart.factory.getBean(QueryLineService.class).getLine(), msg));
			}else if(msg.getType() == Message.CLIENT_THREAD_NUM && msg.getResutlCode() == Message.RESULT_FAIL) {
				// 发送当前线程数和使用线程数
				ServiceStart.factory.getBean(QueryLineService.class).sendClientThreadNum();
			}
		}while(true);
	}
	
	/**
	 * 初始化消息
	 */
	public static Channel channel = null; // 服务器连接信息
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		channel = ctx.channel();
		// 连接成功时，发送本客服端可一次处理查询数
		ServiceStart.factory.getBean(QueryLineService.class).sendClientThreadNum();
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
