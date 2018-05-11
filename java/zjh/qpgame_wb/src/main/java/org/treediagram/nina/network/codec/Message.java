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
import org.treediagram.nina.core.lang.ByteUtils;

/**
 * 消息
 * 
 * @author kidal
 * 
 */
public class Message {
	/**
	 * 消息头长度
	 */
	public static final int HEADER_LENGTH = 32;

	/**
	 * 获取消息长度
	 */
	public static int getFullLength(IoBuffer in) {
		return in.getInt(in.markValue() + 24) + HEADER_LENGTH;
	}
	
	/**
	 * Checksum
	 */
	public static int checksum(byte[] data, int offset, int length) {
		int hash = 0;
		for (int i = offset; i < offset + length; i++) {
			hash = hash << 7 ^ data[i];
		}
		return hash;
	}

	/**
	 * Checksum
	 */
	public static int checksum(byte[] data) {
		return checksum(data, 0, data.length);
	}

	/**
	 * 消息头校验信息
	 */
	private int headerCheck;

	/**
	 * 编码格式
	 */
	private int code;

	/**
	 * 标志
	 */
	private int flag;

	/**
	 * 编号
	 */
	private int id;

	/**
	 * 时间戳
	 */
	private int timestamp;

	/**
	 * 消息体校验信息
	 */
	private int bodyCheck;

	/**
	 * 消息体长度
	 */
	private int bodyLength;

	/**
	 * 附件长度
	 */
	private int attachmentLength;

	/**
	 * 用于校验的消息头数据
	 */
	private byte[] headerForCheck;

	/**
	 * 用于校验的消息体数据
	 */
	private byte[] bodyForCheck;

	/**
	 * 消息体
	 */
	private byte[] body;

	/**
	 * 附件
	 */
	private byte[] attachment;

	/**
	 * 创建消息
	 */
	public Message(byte[] data) {		
		this.headerCheck = ByteUtils.intFromByte(data, 0);
		this.code = ByteUtils.intFromByte(data, 4);
		this.flag = ByteUtils.intFromByte(data, 8);
		this.id = ByteUtils.intFromByte(data, 12);
		this.timestamp = ByteUtils.intFromByte(data, 16);
		this.bodyCheck = ByteUtils.intFromByte(data, 20);
		this.bodyLength = ByteUtils.intFromByte(data, 24);
		this.attachmentLength = ByteUtils.intFromByte(data, 28);

		int rblen = this.bodyLength - this.attachmentLength;
		int offset = HEADER_LENGTH;

		this.headerForCheck = ArrayUtils.subarray(data, 4, offset);
		this.bodyForCheck = ArrayUtils.subarray(data, offset, data.length);
		this.body = ArrayUtils.subarray(data, offset, offset + rblen);
		
		if (this.attachmentLength > 0) {
			this.attachment = ArrayUtils.subarray(data, offset + rblen, data.length);
		} else {
			this.attachment = new byte[0];
		}
	}

	/**
	 * 创建消息
	 */
	public Message(int code, int flag, int id, int timestamp, byte[] body, byte[] attachment) {
		this.headerCheck = 0;
		this.code = code;
		this.flag = flag;
		this.id = id;
		this.timestamp = timestamp;
		this.bodyCheck = 0;
				
		this.bodyLength = body.length;
		this.body = body;	
		this.attachmentLength = attachment == null ? 0 : attachment.length;
		this.attachment = attachment;
		this.bodyLength += this.attachmentLength;
	}

	/**
	 * 校验消息头
	 */
	public int checkHeader() {
		return checksum(headerForCheck);
	}

	/**
	 * 校验消息体
	 */
	public int checkBody() {
		return checksum(bodyForCheck);
	}

	// getters

	public int getHeaderCheck() {
		return headerCheck;
	}

	public int getCode() {
		return code;
	}

	public int getFlag() {
		return flag;
	}

	public int getId() {
		return id;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public int getBodyCheck() {
		return bodyCheck;
	}

	public int getBodyLength() {
		return bodyLength;
	}

	public byte[] getBody() {
		return body;
	}

	public int getAttachmentLength() {
		return attachmentLength;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public byte[] getHeaderForCheck() {
		return headerForCheck;
	}

	public byte[] getBodyForCheck() {
		return bodyForCheck;
	}	
}
