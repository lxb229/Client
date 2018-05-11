/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.treediagram.nina.network.codec;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * 消息编码器
 * 
 * @author kidal
 * 
 */
public class MessageEncoder extends ProtocolEncoderAdapter {
	/**
	 * 编码
	 */
	@Override
	public void encode(IoSession session, Object in, ProtocolEncoderOutput out) throws Exception {
		if (in == null) {
			return;
		}
		if (in instanceof Message) {
			writeMessage((Message) in, out);
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
		out.write(buffer);
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
