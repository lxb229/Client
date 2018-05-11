package com.guse.apple_reverse.netty.handler;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.guse.apple_reverse.Main.ServiceStart;
import com.guse.apple_reverse.common.JSONUtils;
import com.guse.apple_reverse.dao.model.AppleIdTable;
import com.guse.apple_reverse.dao.model.AppleSercurity;
import com.guse.apple_reverse.netty.handler.session.ClientSession;
import com.guse.apple_reverse.netty.handler.session.ClientSessionUtil;
import com.guse.apple_reverse.service.ChangeSercurityService;
import com.guse.apple_reverse.service.QueryBillService;
import com.guse.apple_reverse.service.ReverseService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object>{
	
	private  WebSocketServerHandshaker handshaker;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        /**
         * HTTP接入，WebSocket第一次连接使用HTTP连接,用于握手
         */
        if(msg instanceof FullHttpRequest){
            handleHttpRequest(ctx, (FullHttpRequest)msg);
        }

        /**
         * Websocket 接入
         */
        else if(msg instanceof WebSocketFrame){
            
            WebSocketFrame frame = (WebSocketFrame)msg;
            /**
             * 判断是否关闭链路的指令
             */
            if (frame instanceof CloseWebSocketFrame) {
                handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
                return;
            }

            /**
             * 本例程仅支持文本消息，不支持二进制消息
             */
            if (frame instanceof BinaryWebSocketFrame) {
                throw new UnsupportedOperationException(String.format(
                        "%s frame types not supported", frame.getClass().getName()));
            }
            if(frame instanceof TextWebSocketFrame){
                // 返回应答消息
                String request = ((TextWebSocketFrame) frame).text();
                ServiceStart.CONSOLE_LOG.info("WebSocket reception Message. channelAddress:[{}]; data:[{}]",  ctx.channel().remoteAddress(), request);
                try {
	                JSONObject reqJson =  JSONObject.fromObject(request);
	                switch(reqJson.getString("type")) {
	                case "changeSecurity": //修改密码和密保
	                {
	                	ctx.channel().writeAndFlush(new TextWebSocketFrame("ok"));
	                	List<AppleSercurity> list = ServiceStart.factory.getBean(ChangeSercurityService.class).start();
	                	if(!list.isEmpty()) {
		                	for(AppleSercurity apple : list) {
		                		JSONObject json = new JSONObject();
		                		json.put("id", apple.getId());
		                    	json.put("status", apple.getStatus());
		                    	ctx.channel().writeAndFlush(new TextWebSocketFrame(json.toString()));
		                	}
		                } else {
		                	 ctx.channel().writeAndFlush(new TextWebSocketFrame("{\"id\":\"\",\"status\":\"\"}"));
		                }
	                }
	                	break;
	                case "queryApple": //查询详情
	                {
                		ctx.channel().writeAndFlush(new TextWebSocketFrame("ok"));
		                List<AppleIdTable> list = ServiceStart.factory.getBean(ReverseService.class).importRecord();
		                if(!list.isEmpty()) {
		                	for(AppleIdTable apple : list) {
		                		JSONObject json = new JSONObject();
		                		json.put("id", apple.getId());
		                    	json.put("status", apple.getQuery_status());
		                    	ctx.channel().writeAndFlush(new TextWebSocketFrame(json.toString()));
		                	}
		                } else {
		                	 ctx.channel().writeAndFlush(new TextWebSocketFrame("{'id':'','status':''}"));
		                }
	                }
		                break;
	                case "queryBill": //查询账单
	                {	
	                	String result = ServiceStart.factory.getBean(QueryBillService.class).service(reqJson.getInt("id"));
	                	ctx.channel().writeAndFlush(new TextWebSocketFrame(result));
	                }
	                	break;
	                case "removeClient": //手动移除客服端
	                {
	                	Iterator<Map.Entry<Channel, ClientSession>> it = ClientSessionUtil.entrySetIterator();
	                	while(it.hasNext()) {
	    					Map.Entry<Channel, ClientSession> entry = it.next();
	    					Channel channel = entry.getKey();
	    					if(channel.remoteAddress().toString().indexOf(reqJson.getString("ip")) > -1) {
	    						channel.close();
	    						it.remove();
	    					}
	    				}
	                	ctx.channel().writeAndFlush(new TextWebSocketFrame("OK"));
	                }
	                	break;
	                case "clientInfo" : //获取客服端详情信息
	                {
	                	JSONObject json = new JSONObject();
	                	Iterator<Map.Entry<Channel, ClientSession>> it = ClientSessionUtil.entrySetIterator();
	                	while(it.hasNext()) {
	    					Map.Entry<Channel, ClientSession> entry = it.next();
	    					Channel channel = entry.getKey();
	    					json.put(channel.remoteAddress().toString(), JSONUtils.toJSONString(entry.getValue()));
	    				}
	                	ctx.channel().writeAndFlush(new TextWebSocketFrame(json.toString()));
	                }
	                	break;
	                default:
	                {
	                	JSONObject json = new JSONObject();
	                	Iterator<Map.Entry<Channel, ClientSession>> it = ClientSessionUtil.entrySetIterator();
	                	while(it.hasNext()) {
	                		Map.Entry<Channel, ClientSession> entry = it.next();
	                		ClientSession client = entry.getValue();
	                		String info = "threadNum:"+client.getThreadNum()+",useNum:"+client.getUseNum()+",action:[";
	                		for(String v : client.getQueryType().values()) {
	                			info += ","+v;
	                		}
	    					json.put(client.getName(), info+"]");
	    				}
	                	ctx.channel().writeAndFlush(new TextWebSocketFrame(json.toString()));
	                }
	                break;
	                }
	                
                }catch(Exception e) {
                	e.printStackTrace();
                	ctx.channel().writeAndFlush(new TextWebSocketFrame(e.getMessage()));
                }
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
    }


    private void handleHttpRequest(ChannelHandlerContext ctx,FullHttpRequest req){
        if (!req.getDecoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://"+req.headers().get(HttpHeaders.Names.HOST)+"/ws", null, false);
        
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx,
            FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static boolean isKeepAlive(FullHttpRequest req) {
        return false;
    }

}
