package org.treediagram.nina.network.codec;

import java.security.MessageDigest;
import org.apache.commons.codec.binary.Base64;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treediagram.nina.network.util.IpUtils;

/**
 * webSocket消息解码器
 * */
public class WSMessageDecoder extends CumulativeProtocolDecoder{
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(WSMessageDecoder.class);
	
	/**
	 * 会话上下文属性键
	 */
	private static final AttributeKey CONTEXT = new AttributeKey(WSMessageDecoder.class, "context");
	
	/**
	 * 数据包解码状态
	 * 
	 * @author kidal
	 * 
	 */
	enum DecodeState {
		/**
		 * 解码数据头
		 */
		HEADER,

		/**
		 * 解码数据体
		 */
		BODY,
	}

	/**
	 * 数据包解码上下文
	 * 
	 * @author kidal
	 * 
	 */
	public static class DecodeContext {
		/**
		 * 数据长度
		 */
		private int length = 0;

		/**
		 * 数据包解码状态
		 */
		private DecodeState decodeState = DecodeState.HEADER;
		
		/**
		 * 握手状态,0=未握手,1=已握手
		 * */
		private int handshake = 0;

		/**
		 * 数据存放区
		 * */
		private byte []data;
		
		/**
		 * 重置上下文状态
		 */
		public void reset(int handshake) {
			this.length = 0;
			this.decodeState = DecodeState.HEADER;
			this.handshake = handshake;
			this.data = null;
		}

		// getters and setters

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public DecodeState getDecodeState() {
			return decodeState;
		}

		public void setDecodeState(DecodeState decodeState) {
			this.decodeState = decodeState;
		}

		public int getHandshake() {
			return handshake;
		}

		public void setHandshake(int handshake) {
			this.handshake = handshake;
		}
	}
	
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, 
			ProtocolDecoderOutput out) throws Exception {
		// 获取数据包解码上下文
		DecodeContext decodeContext = getDecodeContext(session);
		if(decodeContext.getHandshake() == 0){
			doHandshake(session, in, out, decodeContext);
			return false;
		}else{
			while(in.hasRemaining()){
					//websocket消息一定是分片的
					if(in.remaining() < 2){
						//不够消息头长
						return false;
					}
					in.mark();
					
					byte head1 = in.get();
					byte head2 = in.get();
					int nHead1 = head1&0xff;
					int nHead2 = head2&0xff;
					
					int finFlag = nHead1 >> 7;
					int opcode = nHead1&0xf;
					if(opcode == 0x8){
						decodeContext.reset(0);
						session.close(true);
						String ip = IpUtils.getIp(session);
						logger.error("opcode=8 断开连接[{}](解码错误)", ip);
						return false;
					}else if(opcode != 0x02){
						decodeContext.reset(0);
						session.close(true);
						String ip = IpUtils.getIp(session);
						logger.error("数据帧不是二进制帧数据, 断开连接[{}](解码错误)", ip);
						return false;
					}
					
					int dataTotalLen = 0;
					int isMask = nHead2 >> 7;
					int payloadlen = nHead2 & 0x7f;
					if(payloadlen < 0x7E){
						dataTotalLen = payloadlen;
					}else if(payloadlen == 0x7E){
						//还读2字节
						if(in.remaining() < 2){
							in.reset();
							return false;
						}
						byte []exbuf = new byte[2];
						in.get(exbuf);
						dataTotalLen += ((exbuf[0]&0xff) << 8);
						dataTotalLen += (exbuf[1]&0xff);
						
					}else if(payloadlen == 0x7F){
						//还读8字节
						if(in.remaining() < 8){
							in.reset();
							return false;
						}
						byte []exbuf = new byte[8];
						in.get(exbuf);
						dataTotalLen += ((exbuf[0]&0xff) << 56);
						dataTotalLen += ((exbuf[1]&0xff) << 48);
						dataTotalLen += ((exbuf[2]&0xff) << 40);
						dataTotalLen += ((exbuf[3]&0xff) << 32);
						dataTotalLen += ((exbuf[4]&0xff) << 24);
						dataTotalLen += ((exbuf[5]&0xff) << 16);
						dataTotalLen += ((exbuf[6]&0xff) << 8);
						dataTotalLen += (exbuf[7]&0xff);
					}
					
					byte []maskBuf = new byte[4];
					if(isMask == 1){
						//取4字节mask
						if(in.remaining() < 4){
							in.reset();
							return false;
						}
						in.get(maskBuf);
					}
					
					if(in.remaining() < dataTotalLen){
						in.reset();
						return false;
					}
					//读取本次分片数据
					byte []oneBuf = new byte[dataTotalLen];
					in.get(oneBuf);
					for(int k=0; k<dataTotalLen; k++){
						int orgByte = (oneBuf[k] & 0xff);
						orgByte = orgByte ^ (maskBuf[k%4]&0xff);
						oneBuf[k] = (byte) orgByte;
					}
					
					DecodeState decodeState = decodeContext.getDecodeState();
					if(null == decodeContext.data){
						decodeContext.data = new byte[dataTotalLen];
						System.arraycopy(oneBuf, 0, decodeContext.data, 0, dataTotalLen);
					}else{
						int oldLen = decodeContext.data.length;
						byte []cloneBuf = new byte[oldLen + dataTotalLen];
						//将原数据复制到目标buf
						System.arraycopy(decodeContext.data, 0, cloneBuf, 0, oldLen);
						//将本次数据复制到目标buf
						System.arraycopy(oneBuf, 0, cloneBuf, oldLen, dataTotalLen);
						decodeContext.data = cloneBuf;
					}					
					
					if(decodeState == DecodeState.HEADER){
						decodeContext.setDecodeState(DecodeState.BODY);
						decodeContext.setLength(decodeContext.data.length);
					}
					
					if(finFlag == 1){
						//本分片数据包数据已结束,形成完整的数据包
						try{
							// 构造消息
							Message message = new Message(decodeContext.data);
		
							// 校验消息头
							int headerChecksum = message.checkHeader();
							if (headerChecksum != message.getHeaderCheck()) {
								throw new Exception("消息头校验码错误[client=" + message.getHeaderCheck() + ",server=" + headerChecksum
										+ "]");
							}
		
							// 校验消息体
							int bodyChecksum = message.checkBody();
							if (bodyChecksum != message.getBodyCheck()) {
								throw new Exception("消息体校验码错误[client=" + message.getBodyCheck() + ",server=" + bodyChecksum
										+ "]");
							}
		
							// 写入消息
							out.write(message);
							
							//byte []body = ArrayUtils.subarray(decodeContext.data, 32, decodeContext.data.length);
							//String ss = new String(body);
							//logger.error("收到客户端数据消息:" + ss);
						
							decodeContext.reset(1);
						}catch (Exception e) {
							decodeContext.reset(0);
							String ip = IpUtils.getIp(session);
							session.close(true);
							logger.error("断开连接[{}](解码错误)", ip, e);
						}
					}
				}
			}		
		return true;
	}

	/**
	 * 获取数据包解码上下文
	 */
	private DecodeContext getDecodeContext(IoSession session) {
		DecodeContext decodeContext = (DecodeContext) session.getAttribute(CONTEXT);
		if (decodeContext == null) {
			decodeContext = new DecodeContext();
			decodeContext.reset(0);
			session.setAttribute(CONTEXT, decodeContext);
		}
		return decodeContext;
	}
	
	private void doHandshake(IoSession session, IoBuffer in, ProtocolDecoderOutput out, DecodeContext decodeContext){
		byte[] inBytes = new byte[in.limit()];
		in.get(inBytes);
		String keyStr = new String(inBytes);
		
		keyStr = keyStr.substring(0, keyStr.indexOf("==") + 2);
		keyStr = keyStr.substring(keyStr.indexOf("Key") + 4, keyStr.length()).trim();
		keyStr += "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
		try {
			MessageDigest msgDig = MessageDigest.getInstance("SHA-1");
			msgDig.update(keyStr.getBytes("UTF8"));
			byte[] sha1Hash = msgDig.digest();
			keyStr = new String(Base64.encodeBase64(sha1Hash));
			String retStr = "";
			retStr += "HTTP/1.1 101 Switching Protocols";
			retStr += "\r\n";
			retStr += "Upgrade: websocket";
			retStr += "\r\n";
			retStr += "Connection: Upgrade";
			retStr += "\r\n";
			retStr += "Sec-WebSocket-Accept: " + keyStr;
			retStr += "\r\n";
			retStr += "\r\n";
			byte []retByte = retStr.getBytes();
			
			IoBuffer resp = IoBuffer.allocate(retByte.length);
			resp.put(retByte);
			resp.flip();
			session.write(resp);
			decodeContext.setHandshake(1);
			session.setAttribute(CONTEXT, decodeContext);
		} catch (Exception e) {
			decodeContext.setHandshake(0);
			session.close(true);
			String ip = IpUtils.getIp(session);
			logger.error("断开连接[{}](WebSocket握手错误)", ip, e);
		}
	}
}
