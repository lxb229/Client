package com.guse.chessgame.resethandler.common;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.springframework.data.convert.ThreeTenBackPortConverters;

import com.guse.chessgame.common.json.JSONUtils;
import com.guse.chessgame.common.util.HMacMD5;
import com.guse.chessgame.resethandler.request.Message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @ClassName: Global
 * @Description: 全局客服端连接信息
 * @author Fily GUSE
 * @date 2017年8月10日 下午5:39:43
 * 
 */
public class Global extends Thread {

	public static ChannelGroup group = new DefaultChannelGroup(
			GlobalEventExecutor.INSTANCE);
	
	private static int INTERVAL = 5000;

	@Override
	public void run() {

		while (true) {
			// 间隔时间
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			for (Channel channel : group) {

				// 心跳信息
				Message msg = new Message();
				msg.setPid(20000);
				msg.setData("");

				try {
					// 生成HMAC-MD5信息
					String md5 = HMacMD5.getHmacMd5Str(msg.getPid().toString(),
							msg.getData()).toLowerCase();
					msg.setMd5(md5);
					// 添加服务器时间
					msg.setTime(new Date().getTime() + "");

					// 数据转换
					String request = JSONUtils.toJSONString(msg);
					byte[] bytes;

					bytes = request.getBytes("UTF-8");
					ByteBuf buf = Unpooled.wrappedBuffer(bytes);
					// 推送消息到客服端
					channel.writeAndFlush(new BinaryWebSocketFrame(buf));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

			}

		}

	}
}
