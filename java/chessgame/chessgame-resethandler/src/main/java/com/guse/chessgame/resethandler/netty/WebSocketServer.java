package com.guse.chessgame.resethandler.netty;


import com.guse.chessgame.resethandler.common.Global;
import com.guse.chessgame.resethandler.dispose.DisposeType;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/** 
* @ClassName: WebSocketServer 
* @Description: netty WebSocket服务启动器
* @author Fily GUSE
* @date 2017年8月11日 下午2:56:11 
*  
*/
public class WebSocketServer {
    public void run(int port) throws Exception {
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
                            // 请求和应答的消息编码或者解码为HTTP消息
                            pipeline.addLast("http-codec", new HttpServerCodec());
                            // HTTP消息的多个部分组合成一条完整的HTTP消息
                            pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                            // 向客户端发送HTML5文件，主要用于支持浏览器和服务端进行websocket通信
                            pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                            // 增加消息的Handler处理类WebSocketServerHandler
                            pipeline.addLast("handler", new WebSocketServerHandler());
                        }
                    });

            Channel ch = b.bind(port).sync().channel();
			System.out.println("Web socket server started at port " + port  + '.');

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        
    	// 初始化消息处理
        DisposeType.initDisposes();
        
        // 服务器心跳
        Thread t =  new Global();
        t.start();
        
    	
    	int port = 8989;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new WebSocketServer().run(port);
        
    }
}