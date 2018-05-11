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

package org.treediagram.nina.memcache.id;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 主键生成器<br>
 * [保留位:1][渠道:3][分区:3][主键自增位:9]
 * 
 * @author kidal
 */
public final class IdGenerator {
	/**
	 * 渠道标识
	 */
	private final short channel;

	/**
	 * 分区标识
	 */
	private final short zone;

	/**
	 * 当前自增值
	 */
	private final AtomicLong current;

	/**
	 * 溢出边界
	 */
	private final long limit;

	/**
	 * 构造主键生成器
	 * 
	 * @param channel 渠道标识
	 * @param zone 分区标识
	 * @param current 当前的主键值
	 */
	public IdGenerator(short channel, short zone, Long current) {
		if (!vaildValue(channel, 12)) {
			throw new IllegalArgumentException("渠道标识[" + channel + "]超过了12位二进制数的表示范围");
		}

		if (!vaildValue(zone, 12)) {
			throw new IllegalArgumentException("分区标识[" + zone + "]超过了12位二进制数的表示范围");
		}

		this.channel = channel;
		this.zone = zone;

		final long[] limits = getLimits(channel, zone);

		if (current != null) {
			if (current < limits[0] || current > limits[1]) {
				throw new IllegalArgumentException("当前主键值[" + current + "],不符合渠道标识[" + channel + "]分区标识[" + zone
						+ "]的要求");
			}
			this.current = new AtomicLong(current);
		} else {
			this.current = new AtomicLong(limits[0]);
		}

		this.limit = limits[1];
	}

	/**
	 * 获取当前的主键值
	 */
	public long getCurrent() {
		return current.get();
	}

	/**
	 * 获取下一个主键值
	 * 
	 * @throws IllegalStateException 到达边界值时会抛出该异常
	 */
	public long getNext() {
		long result = current.incrementAndGet();
		if (result > limit) {
			throw new IllegalStateException("主键值[" + result + "]已经超出了边界[" + limit + "]");
		}
		return result;
	}

	// Getter and Setter ...

	public short getZone() {
		return zone;
	}

	public short getChannel() {
		return channel;
	}

	public long getLimit() {
		return limit;
	}

	// Static Method's ...

	/**
	 * 获取主键中的分区标识
	 * 
	 * @param id 主键值
	 */
	public static short toZone(long id) {
		if ((0xF000000000000000L & id) != 0) {
			throw new IllegalArgumentException("无效的ID标识值:" + id);
		}
		// 将高位置0(保留位+渠道位+分区位)
		return (short) ((id >> 36) & 0x0000000000000FFFL);
	}

	/**
	 * 获取主键中的渠道标识
	 * 
	 * @param id 主键值
	 */
	public static short toChannel(long id) {
		if ((0xF000000000000000L & id) != 0) {
			throw new IllegalArgumentException("无效的ID标识值:" + id);
		}
		// 将高位置0(保留位+渠道位+分区位)
		return (short) ((id >> 48) & 0x0000000000000FFFL);
	}

	/**
	 * 检查值是否超过 byte 的表示范围
	 * 
	 * @param value 被检查的值
	 * @return true:合法,false:非法或超过范围
	 */
	public static boolean vaildByteValue(short value) {
		if (value >= 0 && value <= 255) {
			return true;
		}
		return false;
	}

	/**
	 * 检查值是否超过指定位数的2进制表示范围
	 * 
	 * @param value 被检查的值
	 * @param digit 2进制位数
	 * @return true:合法,false:非法或超过范围
	 */
	public static boolean vaildValue(short value, int digit) {
		if (digit <= 0 || digit > 64) {
			throw new IllegalArgumentException("位数必须在1-64之间");
		}
		int max = (1 << digit) - 1;
		if (value >= 0 && value <= max) {
			return true;
		}
		return false;
	}

	/**
	 * 获取ID值边界
	 * 
	 * @param channel 渠道值
	 * @param zone 分区值
	 * @return [0]:最小值,[1]:最大值
	 */
	public static long[] getLimits(short channel, short zone) {
		if (!vaildValue(channel, 12)) {
			throw new IllegalArgumentException("渠道标识[" + channel + "]超过了12位二进制数的表示范围");
		}

		if (!vaildValue(zone, 12)) {
			throw new IllegalArgumentException("分区标识[" + zone + "]超过了12位二进制数的表示范围");
		}

		long min = (((long) channel) << 48) + (((long) zone) << 36);
		long max = min | 0x0000000FFFFFFFFFL;

		return new long[] { min, max };
	}

	/**
	 * 获取id
	 */
	public static long toId(short channel, short zone, long id) {
		if (id != (id & 0x0000000FFFFFFFFFL)) {
			throw new IllegalArgumentException("id[" + id + "]超过范围");
		} else {
			return id + ((long) channel << 48) + ((long) zone << 36);
		}
	}

	/**
	 * 获取ID值边界
	 * 
	 * @param channel 渠道值
	 * @return [0]:最小值,[1]:最大值
	 */
	public static long[] getLimits(short channel) {
		if (!vaildValue(channel, 12)) {
			throw new IllegalArgumentException("渠道标识[" + channel + "]超过了12位二进制数的表示范围");
		}

		long min = (((long) channel) << 48);
		long max = min | 0x0000FFFFFFFFFFFFL;

		return new long[] { min, max };
	}

	/**
	 * ID信息
	 */
	public static final class IdInfo {
		/**
		 * 渠道标识
		 */
		private final short channel;

		/**
		 * 分区标识
		 */
		private final short zone;

		/**
		 * 标识
		 */
		private final long id;

		/**
		 * 构造方法
		 */
		public IdInfo(long id) {
			if ((0xF000000000000000L & id) != 0) {
				throw new IllegalArgumentException("无效的ID标识值:" + id);
			}

			this.id = id & 0x0000000FFFFFFFFFL; // 将高位置0(保留位+渠道位+分区位)
			this.zone = (short) ((id >> 36) & 0x0000000000000FFFL);
			this.channel = (short) ((id >> 48) & 0x0000000000000FFFL);
		}

		// Getter and Setter ...

		/**
		 * 获取分区标识值
		 */
		public short getZone() {
			return zone;
		}

		/**
		 * 获取渠道标识值
		 */
		public short getChannel() {
			return channel;
		}

		/**
		 * 获取去除渠道标识和分区标识的ID值
		 */
		public long getId() {
			return id;
		}
	}
}
