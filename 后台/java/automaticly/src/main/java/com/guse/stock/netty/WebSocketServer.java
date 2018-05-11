package com.guse.stock.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guse.stock.netty.handler.HttpServerInboundHandler;
import com.guse.stock.netty.handler.WebSocketServerHandler;

/** 
* @ClassName: WebSocketServer 
* @Description: webSocket 服务
* @author Fily GUSE
* @date 2017年10月17日 下午8:17:40 
*  
*/
public class WebSocketServer {
	
private final static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
	
	/** 
	* @Fields port : 服务端口
	*/
	public int port;

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
                                	// 将请求和应答的消息编码或者解码为HTTP消息
                                	ch.pipeline().addLast("http-codec",new HttpServerCodec());
                                	// 将HTTP消息的多个部分组合成一条完整的HTTP消息
                                	ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));
                                	// 向客户端发送HTML5文件，主要用于支持浏览器和服务端进行websocket通信
                                	ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                                	
                                	ch.pipeline().addLast("handler",new WebSocketServerHandler());  
                                }  
                            }).option(ChannelOption.SO_BACKLOG, 128) // (5)  
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)  
  
            ChannelFuture f = b.bind(port).sync(); // (7)  
            logger.info("HTTP Server Start. name:{}, port:{}" , Thread.currentThread().getStackTrace()[1].getClassName(), port);
  
            f.channel().closeFuture().sync();  
        } finally {  
            workerGroup.shutdownGracefully();  
            bossGroup.shutdownGracefully();  
        }  
    }

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
