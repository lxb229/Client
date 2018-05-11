package com.guse.chessgame.resethandler.request;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import com.guse.chessgame.common.json.JSONUtils;
import com.guse.chessgame.common.util.AesCBC;
import com.guse.chessgame.common.util.HMacMD5;
import com.guse.chessgame.resethandler.dispose.AbstractDispose;
import com.guse.chessgame.resethandler.dispose.DisposeType;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @ClassName: MessageDispose
 * @Description: 消息处理
 * @author Fily GUSE
 * @date 2017年8月11日 下午5:40:58
 * 
 */
public class MessageDispose {

	/**
	 * @Fields ctx : 连接信息
	 */
	private ChannelHandlerContext ctx;

	/**
	 * @Fields key : 加密Key
	 */
	private String key = "87473920";

	/**
	 * @Fields frame : 消息信息
	 */
	private BinaryWebSocketFrame frame;

	public MessageDispose(ChannelHandlerContext ctx, BinaryWebSocketFrame frame) {
		this.ctx = ctx;
		this.frame = frame;
	}

	public void dispose() {

		try {
			String request = null;
			// 二进制消息
			if (frame instanceof BinaryWebSocketFrame) {
				ByteBuf buf = frame.content();
				byte[] req = new byte[buf.readableBytes()];
				buf.readBytes(req);
				try {
					request = new String(req, "UTF-8");
//					request = new String(request.getBytes(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				throw new UnsupportedOperationException(String.format(
						"%s frame types not supported", frame.getClass()
								.getName()));
			}

			// 客户端数据转成业务对象
			Message msg = JSONUtils.toBean(request, Message.class);
			// 验证hmac-md5
			msg.setFlag(1);
			// 获取md5验证信息
			String md5 = HMacMD5.getHmacMd5Str(msg.getPid().toString(),msg.getData());
			if (!md5.toUpperCase().equals(msg.getMd5().toUpperCase())) {
				msg.setErrorData(2, "非法数据");
			}
			
			Class<? extends AbstractDispose> disposeClass = DisposeType.getDisposeClassBy(msg.getPid());
			
			if(disposeClass == null){
				throw new Exception("类型为"+msg.getPid()+"的包定义不存在");
			}
			AbstractDispose dispose = null;
			try {
				// 初始化对象
				dispose = (AbstractDispose)disposeClass.newInstance();
				dispose.msg = msg;
				dispose.channel = ctx.channel();
				
				// 执行方法
				dispose.dispose();
				
			} catch (InstantiationException | IllegalAccessException e) {
				throw new Exception("类型为"+msg.getPid()+"的包实例化失败");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

}
