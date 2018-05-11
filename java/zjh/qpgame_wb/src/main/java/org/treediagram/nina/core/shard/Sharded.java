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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 分片
 * 
 * @author kidal
 *
 */
public class Sharded<T, S extends ShardInfo<T>> {
	/**
	 * 哈希算法
	 */
	private final Hashing hashing;
	/**
	 * 节点树
	 */
	private final TreeMap<Long, S> nodes = new TreeMap<Long, S>();
	/**
	 * 目标速查表
	 */
	private final Map<S, T> targets = new LinkedHashMap<S, T>();

	/**
	 * 创建分片
	 * 
	 * @param shards 分片信息列表
	 */
	public Sharded(List<S> shards) {
		this(new MurmurHashing(), shards);
	}

	/**
	 * 创建分片
	 * 
	 * @param hashing 哈希算法
	 * @param shards 分片信息列表
	 */
	public Sharded(Hashing hashing, List<S> shards) {
		this.hashing = hashing;
		this.initialize(shards);
	}

	/**
	 * 获取分片
	 * 
	 * @param key 片键
	 * @return 如果找到分片返回分片;否则返回null
	 */
	public T getShard(byte[] key) {
		return targets.get(getShardInfo(key));
	}

	/**
	 * 获取分片
	 * 
	 * @param key 片键
	 * @return 分片目标
	 */
	public T getShard(String key) {
		return targets.get(getShardInfo(key));
	}

	/**
	 * 获取分片信息
	 * 
	 * @param key 片键
	 * @return 分片信息
	 */
	public S getShardInfo(byte[] key) {
		SortedMap<Long, S> tail = nodes.tailMap(hashing.hash(key));
		if (tail.isEmpty()) {
			return nodes.get(nodes.firstKey());
		}
		return tail.get(tail.firstKey());
	}

	/**
	 * 获取分片信息
	 * 
	 * @param key 片键
	 * @return 分片信息
	 */
	public S getShardInfo(String key) {
		try {
			return getShardInfo(key.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 初始化
	 */
	private void initialize(List<S> shards) {
		for (int i = 0; i != shards.size(); i++) {
			final S shard = shards.get(i);

			if (shard.getName() == null) {
				for (int n = 0; n < 160 * shard.getWeight(); n++) {
					nodes.put(hashing.hash("SHARD-" + i + "-NODE-" + n), shard);
				}
			} else {
				for (int n = 0; n < 160 * shard.getWeight(); n++) {
					nodes.put(hashing.hash(shard.getName() + "*" + shard.getWeight() + n), shard);
				}
			}

			targets.put(shard, shard.getTarget());
		}
	}
}
