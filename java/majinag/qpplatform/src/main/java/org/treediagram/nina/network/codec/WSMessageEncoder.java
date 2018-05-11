package org.treediagram.nina.network.codec;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
public class WSMessageEncoder extends ProtocolEncoderAdapter{
	@Override
	public void encode(IoSession session, Object in, ProtocolEncoderOutput out) throws Exception {
		if (in == null) {
			return;
		}
		if (in instanceof Message) {
			writeMessage((Message)in, out);
		} else if (in instanceof byte[]) {
			writeBytes((byte[]) in, out);
		}
		out.flush();		
	}

	/**
	 * 写入消息
	 */
	private void writeMessage(Message message, ProtocolEncoderOutput out) {
		int bodyChecksum = 0;
		if (message.getAttachment() == null) {
			bodyChecksum = Message.checksum(message.getBody());
		} else {
			byte[] data = ArrayUtils.addAll(message.getBody(), message.getAttachment());
			bodyChecksum = Message.checksum(data);
		}
		IoBuffer bufferForCheck = IoBuffer.allocate(Message.HEADER_LENGTH - 4);
		bufferForCheck.putInt(message.getCode());
		bufferForCheck.putInt(message.getFlag());
		bufferForCheck.putInt(message.getId());
		bufferForCheck.putInt(message.getTimestamp());
		bufferForCheck.putInt(bodyChecksum);
		bufferForCheck.putInt(message.getBodyLength());
		bufferForCheck.putInt(message.getAttachmentLength());
		byte[] bytesForCheck = new byte[Message.HEADER_LENGTH - 4];
		bufferForCheck.flip();
		bufferForCheck.get(bytesForCheck);
		int headerChecksum = Message.checksum(bytesForCheck);

		IoBuffer buffer = IoBuffer.allocate(Message.HEADER_LENGTH + message.getBodyLength()
				+ message.getAttachmentLength());
		buffer.putInt(headerChecksum);
		buffer.putInt(message.getCode());
		buffer.putInt(message.getFlag());
		buffer.putInt(message.getId());
		buffer.putInt(message.getTimestamp());
		buffer.putInt(bodyChecksum);
		buffer.putInt(message.getBodyLength());
		buffer.putInt(message.getAttachmentLength());
		buffer.put(message.getBody());
		if (message.getAttachment() != null) {
			buffer.put(message.getAttachment());
		}
		
		buffer.flip();
		int bodyLen = buffer.remaining();
		byte []msgBytes = new byte[bodyLen];
		buffer.get(msgBytes);
		int webSocketMsgLen = 0;
		int start = 0;
		if(bodyLen < 126){
			start = 2;
		}else if(bodyLen < 65535){
			start = 4;
		}else{
			start = 10;
		}
		webSocketMsgLen = start + bodyLen;
		
		byte []sentBuffer = new byte[webSocketMsgLen];
		sentBuffer[0] = (byte)0x82;
		if(bodyLen < 126){
			sentBuffer[1] = (byte) bodyLen;
		}else if(bodyLen < 65535){
			sentBuffer[1] = 126;
			sentBuffer[2] = (byte) (bodyLen>>8);
			sentBuffer[3] = (byte) bodyLen;
		}else{
			//最多32位的数据
			sentBuffer[1] = 127;
			sentBuffer[2] = 0;
			sentBuffer[3] = 0;
			sentBuffer[4] = 0;
			sentBuffer[5] = 0;
			sentBuffer[6] = (byte) (bodyLen>>24);
			sentBuffer[7] = (byte) (bodyLen>>16);
			sentBuffer[8] = (byte) (bodyLen>>8);
			sentBuffer[9] = (byte) bodyLen;
		}
		System.arraycopy(msgBytes, 0, sentBuffer, start, bodyLen);
		
		IoBuffer sentIoBuf = IoBuffer.allocate(webSocketMsgLen);		
		sentIoBuf.put(sentBuffer);
		sentIoBuf.flip();
		out.write(sentIoBuf);
	}

	/**
	 * 写入数据
	 */
	private void writeBytes(byte[] data, ProtocolEncoderOutput out) {
		IoBuffer buffer = IoBuffer.allocate(data.length);
		buffer.setAutoExpand(true);
		buffer.put(data);
		buffer.flip();
		out.write(buffer);
	}
}
