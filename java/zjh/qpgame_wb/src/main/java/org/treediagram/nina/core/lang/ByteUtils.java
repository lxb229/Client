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

package org.treediagram.nina.core.lang;

/**
 * 字节工具类
 * 
 * @author kidal
 * 
 */
public final class ByteUtils {

	/**
	 * 将 byte[4] 转换为 int
	 */
	public static final int intFromByte(byte[] array) {
		return intFromByte(array, 0);
	}

	/**
	 * 将 byte[] 中指定位置开始的4个 byte 转换为 int
	 */
	public static final int intFromByte(byte[] array, int offset) {
		return array[offset] << 24 | (array[offset + 1] & 0xFF) << 16 | (array[offset + 2] & 0xFF) << 8
				| (array[offset + 3] & 0xFF);
	}

	/**
	 * 将 int 值转换 byte[4]
	 */
	public final static byte[] intToByte(int number) {
		return intToByte(number, new byte[4], 0);
	}

	/**
	 * 将 int 值转换到 byte[] 中的指定位置开始的4个 byte 元素中
	 */
	public final static byte[] intToByte(int number, byte[] array, int offset) {
		array[offset + 3] = (byte) number;
		array[offset + 2] = (byte) (number >> 8);
		array[offset + 1] = (byte) (number >> 16);
		array[offset] = (byte) (number >> 24);
		return array;
	}

	/**
	 * 将 byte[8] 转换为 long
	 */
	public static long longFromByte(byte[] b) {
		return longFromByte(b, 0);
	}

	/**
	 * 将 byte[] 中指定位置开始的8个 byte 转换为 long
	 */
	public static long longFromByte(byte[] array, int offset) {
		long s0 = array[offset] & 0xFF;
		long s1 = array[offset + 1] & 0xFF;
		long s2 = array[offset + 2] & 0xFF;
		long s3 = array[offset + 3] & 0xFF;
		long s4 = array[offset + 4] & 0xFF;
		long s5 = array[offset + 5] & 0xFF;
		long s6 = array[offset + 6] & 0xFF;
		long s7 = array[offset + 7] & 0xFF;
		return s0 << 56 | s1 << 48 | s2 << 40 | s3 << 32 | s4 << 24 | s5 << 16 | s6 << 8 | s7;
	}

	/**
	 * 将 long 值转换 byte[8]
	 */
	public static byte[] longToByte(long number) {
		return longToByte(number, new byte[8], 0);
	}

	/**
	 * 将 long 值转换到 byte[] 中的指定位置开始的4个 byte 元素中
	 */
	public final static byte[] longToByte(long number, byte[] array, int offset) {
		array[offset + 7] = (byte) number;
		array[offset + 6] = (byte) (number >> 8);
		array[offset + 5] = (byte) (number >> 16);
		array[offset + 4] = (byte) (number >> 24);
		array[offset + 3] = (byte) (number >> 32);
		array[offset + 2] = (byte) (number >> 40);
		array[offset + 1] = (byte) (number >> 48);
		array[offset] = (byte) (number >> 56);
		return array;
	}

	/**
	 * 将 byte[2] 转换为 short
	 */
	public static final short shortFromByte(byte[] array) {
		return shortFromByte(array, 0);
	}

	/**
	 * 将 byte[] 中指定位置开始的2个 byte 转换为 short
	 */
	public static final short shortFromByte(byte[] array, int offset) {
		return (short) ((array[offset] & 0xFF) << 8 | (array[offset + 1] & 0xFF));
	}

	/**
	 * 将 short 值转换 byte[2]
	 */
	public final static byte[] shortToByte(short number) {
		return shortToByte(number, new byte[2], 0);
	}

	/**
	 * 将 short 值转换到 byte[] 中的指定位置开始的4个 byte 元素中
	 */
	public final static byte[] shortToByte(short number, byte[] array, int offset) {
		array[offset + 1] = (byte) number;
		array[offset] = (byte) (number >> 8);
		return array;
	}
}
