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

package org.treediagram.nina.core.shard;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * MurMurHash算法<br>
 * http://murmurhash.googlepages.com
 * 
 * @author kidal
 *
 */
public class MurmurHashing implements Hashing {
	/**
	 * 哈希
	 * 
	 * @param data 数据
	 * @param seed 种子
	 * @return 哈希值
	 */
	public static int hash(byte[] data, int seed) {
		return hash(ByteBuffer.wrap(data), seed);
	}

	/**
	 * 哈希
	 * 
	 * @param data 数据
	 * @param offset 数据起始便宜
	 * @param length 长度
	 * @param seed 种子
	 * @return 哈希值
	 */
	public static int hash(byte[] data, int offset, int length, int seed) {
		return hash(ByteBuffer.wrap(data, offset, length), seed);
	}

	/**
	 * 哈希
	 * 
	 * @param buf 字节缓存
	 * @param seed 种子
	 * @return 哈希值
	 */
	public static int hash(ByteBuffer buf, int seed) {
		// save byte order for later restoration
		ByteOrder byteOrder = buf.order();
		buf.order(ByteOrder.LITTLE_ENDIAN);

		int m = 0x5bd1e995;
		int r = 24;

		int h = seed ^ buf.remaining();

		int k;
		while (buf.remaining() >= 4) {
			k = buf.getInt();

			k *= m;
			k ^= k >>> r;
			k *= m;

			h *= m;
			h ^= k;
		}

		if (buf.remaining() > 0) {
			ByteBuffer finish = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
			// for big-endian version, use this first:
			// finish.position(4-buf.remaining());
			finish.put(buf).rewind();
			h ^= finish.getInt();
			h *= m;
		}

		h ^= h >>> 13;
		h *= m;
		h ^= h >>> 15;

		buf.order(byteOrder);
		return h;
	}

	/**
	 * 哈希
	 * 
	 * @param data 数据
	 * @param seed 种子
	 * @return 哈希值
	 */
	public static long hash64A(byte[] data, int seed) {
		return hash64A(ByteBuffer.wrap(data), seed);
	}

	/**
	 * 哈希
	 * 
	 * @param data 数据
	 * @param offset 数据起始便宜
	 * @param length 长度
	 * @param seed 种子
	 * @return 哈希值
	 */
	public static long hash64A(byte[] data, int offset, int length, int seed) {
		return hash64A(ByteBuffer.wrap(data, offset, length), seed);
	}

	/**
	 * 哈希
	 * 
	 * @param buf 字节缓存
	 * @param seed 种子
	 * @return 哈希值
	 */
	public static long hash64A(ByteBuffer buf, int seed) {
		ByteOrder byteOrder = buf.order();
		buf.order(ByteOrder.LITTLE_ENDIAN);

		long m = 0xc6a4a7935bd1e995L;
		int r = 47;

		long h = seed ^ (buf.remaining() * m);

		long k;
		while (buf.remaining() >= 8) {
			k = buf.getLong();

			k *= m;
			k ^= k >>> r;
			k *= m;

			h ^= k;
			h *= m;
		}

		if (buf.remaining() > 0) {
			ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
			// for big-endian version, do this first:
			// finish.position(8-buf.remaining());
			finish.put(buf).rewind();
			h ^= finish.getLong();
			h *= m;
		}

		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;

		buf.order(byteOrder);
		return h;
	}

	/**
	 * {@link Hashing#hash(byte[])}
	 */
	public long hash(byte[] key) {
		return hash64A(key, 0x1234ABCD);
	}

	/**
	 * {@link Hashing#hash(String)}
	 */
	public long hash(String key) {
		try {
			return hash(key.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
