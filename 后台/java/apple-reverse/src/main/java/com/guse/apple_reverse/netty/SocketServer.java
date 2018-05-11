package com.guse.apple_reverse.netty;

import java.nio.charset.Charset;

import com.guse.apple_reverse.Main.ServiceStart;
import com.guse.apple_reverse.netty.handler.SocketServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/** 
* @ClassName: WebSocketServer 
* @Description: netty服务启动
* @author Fily GUSE
* @date 2017年8月24日 上午11:35:59 
*  
*/
public class SocketServer {
	
	/** 
	* @Fields port : 服务端口
	*/
	private int port;
	
    /** 
    * @Title: run 
    * @Description: 服务启动 
    * @param @throws Exception
    * @return void 
    * @throws 
    */
    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 服务器心跳
                            pipeline.addLast(new IdleStateHandler(10, 0, 0));
                            // 消息包编码
                            pipeline.addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));  
                            // 消息包解码
                            pipeline.addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));
                            // 消息处理
                            pipeline.addLast("handler", new SocketServerHandler());
                        }
                    })
                    // 握手成功缓存大小
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 2小时无数据激活心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            Channel ch = b.bind(port).sync().channel();
            ServiceStart.CONSOLE_LOG.info("Socket Server Start. name:{}, port:{}" , Thread.currentThread().getStackTrace()[1].getClassName(), port);

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
    
}