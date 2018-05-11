package com.guse.apple_reverse.netty;

import org.springframework.stereotype.Service;

import com.guse.apple_reverse.Main.ServiceStart;
import com.guse.apple_reverse.netty.handler.HttpServerInboundHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/** 
* @ClassName: HttpServer 
* @Description: netty http服务
* @author Fily GUSE
* @date 2017年9月28日 下午7:00:43 
*  
*/
@Service
public class HttpServer {
	
	/** 
	* @Fields port : 服务端口
	*/
	public int port = 9010;

    /** 
    * @Title: run 
    * @Description: 服务启动 
    * @param @throws Exception
    * @return void 
    * @throws 
    */
    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)  
        EventLoopGroup workerGroup = new NioEventLoopGroup();  
        try {  
            ServerBootstrap b = new ServerBootstrap(); // (2)  
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)  
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)  
                                @Override  
                                public void initChannel(SocketChannel ch) throws Exception {  
                                    // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码  
                                    ch.pipeline().addLast(new HttpResponseEncoder());  
                                    // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码  
                                    ch.pipeline().addLast(new HttpRequestDecoder());  
                                    
                                    ch.pipeline().addLast(new HttpServerInboundHandler());  
                                }  
                            }).option(ChannelOption.SO_BACKLOG, 128) // (5)  
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)  
  
            ChannelFuture f = b.bind(port).sync(); // (7)  
            ServiceStart.CONSOLE_LOG.info("HTTP Server Start. name:{}, port:{}" , Thread.currentThread().getStackTrace()[1].getClassName(), port);
  
            f.channel().closeFuture().sync();  
        } finally {  
            workerGroup.shutdownGracefully();  
            bossGroup.shutdownGracefully();  
        }  
    }

}
