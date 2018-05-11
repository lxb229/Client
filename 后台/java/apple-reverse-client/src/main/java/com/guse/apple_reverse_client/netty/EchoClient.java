package com.guse.apple_reverse_client.netty;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.guse.apple_reverse_client.Main.ServiceStart;
import com.guse.apple_reverse_client.netty.handler.EchoClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @ClassName: EchoClient
 * @Description: 客服端
 * @author Fily GUSE
 * @date 2017年11月23日 上午11:20:36
 * 
 */
public class EchoClient {

	private String name;
	private String host;
	private int port;

	// 连接时间
	public static String connectTime;
	/** 
	* @Title: start 
	* @Description: 启动nett服务 
	* @param 
	* @return void 
	* @throws 
	*/
	public void start() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group) // 注册线程池
					.channel(NioSocketChannel.class) // 使用NioSocketChannel来作为连接用的channel类
					.remoteAddress(new InetSocketAddress(host, port)) // 绑定连接端口和host信息
					.handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
								@Override
								protected void initChannel(SocketChannel ch) throws Exception {
									// 消息包编码
									ch.pipeline().addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));  
		                            // 消息包解码
		                            ch.pipeline().addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));
		                            // 消息处理
									ch.pipeline().addLast(new EchoClientHandler());
								}
							});
			ServiceStart.CONSOLE_LOG.info("Socket Client Start:{}. server-host:{}, server-port:{}" , name, host, port);
			ChannelFuture cf = b.connect().sync(); // 异步连接服务器
			connectTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			cf.channel().closeFuture().sync(); // 异步等待关闭连接channel
		} catch(Exception e) {
			ServiceStart.CONSOLE_LOG.error("与服务器断开连接");
		} finally {
			try {
				// 释放线程池资源
				group.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			// 10秒后重连
			try {
				Thread.sleep(1000 * 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			start();
		}
	}
	
	/* get/set */
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
