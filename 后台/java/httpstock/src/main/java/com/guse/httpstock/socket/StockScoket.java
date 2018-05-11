package com.guse.httpstock.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.commons.lang3.StringUtils;

import com.guse.httpstock.socket.Message;
import com.guse.httpstock.socket.MessageCoding;
import com.guse.httpstock.socket.zk.CuratorUtil;

/**
 * @ClassName: StockScoket
 * @Description: 库存服务器scoket信息
 * @author Fily GUSE
 * @date 2017年9月6日 下午2:23:21
 * 
 */
public class StockScoket {

	public static String sendMsg(Message msg) {
		// 获取stock服务器信息
		String stockAdds = CuratorUtil.doSelect();
		
		if(StringUtils.isBlank(stockAdds)) {
			return "插件服务器未启动";
		}
		
		String ip_addr = stockAdds.split(":")[0];
		int port = Integer.parseInt(stockAdds.split(":")[1]);
		
		// 返回消息
		String result = null;
		// scoket连接信息
		Socket socket = null;
		try {
			// 创建一个流套接字并将其连接到指定主机上的指定端口号
			socket = new Socket(ip_addr, port);
			// 设置超时时间 一分钟
			socket.setSoTimeout(60000);

			// 向服务器端发送数据
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			String str = MessageCoding.encryptMsg(msg);
			out.write(str.getBytes());

			// 读取服务器端数据
			DataInputStream input = new DataInputStream(socket.getInputStream());
			byte[] buff = new byte[1024];
			input.read(buff);
			result = new String(buff, "UTF-8");

			// 关闭消息推送和接收管道
			out.close();
			input.close();
			
		} catch (Exception e) {
			System.out.println("客户端异常:" + e.getMessage());
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					socket = null;
					System.out.println("客户端 finally 异常:" + e.getMessage());
				}
			}
		}
		
		return result;
	}
	
	
	public static void main(String[] args) {
		Message msg = new Message(0,"");
		System.out.println(sendMsg(msg));
	}

}
