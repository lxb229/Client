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

package org.treediagram.nina.core.codec;

/**
 * Crc32工具类
 * 
 * @author kidal
 */
public final class Crc32 {
	private static int[] table = new int[256];

	/**
	 * 初始化
	 */
	static {
		for (int i = 0; i < 256; i++) {
			table[i] = (i << 24);
			for (int j = 0; j < 8; j++) {
				int h = table[i] & 0x80000000;
				table[i] <<= 1;
				if (h != 0) {
					table[i] ^= 0x04c11db7;
				}
			}
		}
	}

	/**
	 * 快速校验
	 */
	public static int crc(byte[] data) {
		Crc32 crc32 = new Crc32();
		crc32.update(data);
		return crc32.getValue();
	}

	/**
	 * 快速校验
	 */
	public static int crc(byte[] data, int offset, int length) {
		Crc32 crc32 = new Crc32();
		crc32.update(data, offset, length);
		return crc32.getValue();
	}

	/**
	 * 校验值
	 */
	private int crc = 0;

	/**
	 * 创建
	 */
	public Crc32() {

	}

	/**
	 * 重置
	 */
	public void reset() {
		this.crc = 0;
	}

	/**
	 * 更新CRC32值
	 * 
	 * @param data 数据
	 */
	public void update(byte[] data) {
		this.update(data, 0, data.length);
	}

	/**
	 * 更新CRC32值
	 * 
	 * @param data 数据
	 * @param offset 其实位置
	 * @param length 长度
	 */
	public void update(byte[] data, int offset, int length) {
		this.crc = ~this.crc;
		for (int i = offset; i < (offset + length); i++) {
			int tableIndex = (this.crc >> 24) ^ data[i];
			if (tableIndex < 0) {
				tableIndex = tableIndex + 256;
			}
			this.crc = (this.crc << 8) ^ table[tableIndex];
		}
		this.crc = ~this.crc;
	}

	/**
	 * 获取CRC32值
	 */
	public int getValue() {
		return this.crc;
	}
}
