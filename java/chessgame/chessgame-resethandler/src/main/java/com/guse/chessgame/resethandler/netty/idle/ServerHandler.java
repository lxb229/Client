package com.guse.chessgame.resethandler.netty.idle;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;

public class ServerHandler extends ChannelInboundHandlerAdapter {
	@Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            
            String idleType = null;
            switch(event.state()){
                case READER_IDLE:
                    idleType = "读空闲";
                    ctx.channel().write(new PingWebSocketFrame());
                    break;
                case WRITER_IDLE:
                    idleType = "写空闲";
                    break;
                case ALL_IDLE:
                    idleType = "读写空闲";
                    break;
            }
            String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
            System.out.println(time +ctx.channel().remoteAddress() + " " + idleType);
            
//            ctx.channel().close();
        }
    }
}
