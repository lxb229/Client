package com.guse.stock.netty.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guse.stock.Main.ServiceStart;
import com.guse.stock.netty.handler.service.HttpHandlerService;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.codec.http.multipart.MemoryAttribute;

public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {
	private static Logger logger = LoggerFactory
			.getLogger(HttpServerInboundHandler.class);
	// private ByteBufToBytes reader;

	private HttpRequest request;
	private String orderId;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 处理业务
		if (msg instanceof HttpRequest) {
			request = (HttpRequest) msg;
			Map<String, String> params = getRequestParams(ctx, request);
			logger.info("http Message:{}", params);
			orderId = params.get("order_id");
			if ("order".equals(params.get("action")) && orderId != null) {
		 		Thread t = new Thread(new Runnable(){  
		             public void run(){  
		            	 HttpHandlerService service = ServiceStart.factory.getBean(HttpHandlerService.class);
		 				service.start(Integer.parseInt(orderId.toString()));
		             }});  
		         t.start(); 
			}
		}
		// 响应客服端浏览器
		if (msg instanceof LastHttpContent) {
			String res = "OK";
			FullHttpResponse response = new DefaultFullHttpResponse(
					HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
					Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
			response.headers().set(Names.CONTENT_TYPE, "text/plain");
			response.headers().set(Names.CONTENT_LENGTH,
					response.content().readableBytes());
			if (HttpHeaders.isKeepAlive(request)) {
				response.headers().set(Names.CONNECTION, Values.KEEP_ALIVE);
			}
			ctx.writeAndFlush(response);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.info("HttpServerInboundHandler.channelReadComplete");
		ctx.flush();
	}

	/** 
	* @Title: getRequestParams 
	* @Description: 获取请求参数 
	* @param @param ctx
	* @param @param req
	* @param @return
	* @return Map<String,String> 
	* @throws 
	*/
	private Map<String, String> getRequestParams(ChannelHandlerContext ctx,
			HttpRequest req) {
		Map<String, String> requestParams = new HashMap<>();
		// 处理get请求
		if (req.getMethod() == HttpMethod.GET) {
			QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());
			Map<String, List<String>> parame = decoder.parameters();
			Iterator<Entry<String, List<String>>> iterator = parame.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, List<String>> next = iterator.next();
				requestParams.put(next.getKey(), next.getValue().get(0));
			}
		}
		// 处理POST请求
		if (req.getMethod() == HttpMethod.POST) {
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(
					new DefaultHttpDataFactory(false), req);
			List<InterfaceHttpData> postData = decoder.getBodyHttpDatas(); //
			for (InterfaceHttpData data : postData) {
				if (data.getHttpDataType() == HttpDataType.Attribute) {
					MemoryAttribute attribute = (MemoryAttribute) data;
					requestParams
							.put(attribute.getName(), attribute.getValue());
				}
			}
		}
		return requestParams;
	}
}
