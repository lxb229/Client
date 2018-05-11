package com.guse.platform.common.utils;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.guse.platform.common.utils.serializer.Serialize;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;


/**
 * redis util
 * @author nbin
 * @date 2017年7月26日 上午10:26:31 
 * @version V1.0
 */
public final class JedisUtils {

    @Autowired
    protected ShardedJedisPool shardedJedisPool;

    @Autowired
    protected Serialize        bytesSerialize;
    
    
    /**
     * 返回set制定区间的内的成员(从小到大)
     * @Title: rangeSortSet 
     * @date 2017年8月4日 下午7:04:46 
     * @version V1.0
     */
    public Set<Tuple> zRangeWhitScores(Integer dbIndex,String key, Long start ,Long end) {
    	ShardedJedis jds = null;
        try {
        	jds = shardedJedisPool.getResource();
        	if(dbIndex != null){
           	 	Collection<Jedis> collection=jds.getAllShards();
                Iterator<Jedis> jedis = collection.iterator();
                while(jedis.hasNext()){
                    jedis.next().select(dbIndex);
                }
            }
            ShardedJedisPipeline pipeline = jds.pipelined();
            Response<Set<Tuple>> res = pipeline.zrangeWithScores(key, start, end);
            pipeline.sync();
            return res.get();
        } finally {
            if (jds != null) {
            	jds.close();
            }
        }
    }
    
    /**
     * 返回set制定区间的内的成员（倒序）
     * @Title: rangeSortSet 
     * @date 2017年8月4日 下午7:04:46 
     * @version V1.0
     */
    public Set<Tuple> zRevrangeWithScores(Integer dbIndex,String key, Long start ,Long end) {
    	ShardedJedis jds = null;
        try {
        	jds = shardedJedisPool.getResource();
        	if(dbIndex != null){
           	 	Collection<Jedis> collection=jds.getAllShards();
                Iterator<Jedis> jedis = collection.iterator();
                while(jedis.hasNext()){
                    jedis.next().select(dbIndex);
                }
            }
            ShardedJedisPipeline pipeline = jds.pipelined();
            Response<Set<Tuple>> res = pipeline.zrevrangeWithScores(key, start, end);
            pipeline.sync();
            return res.get();
        } finally {
            if (jds != null) {
            	jds.close();
            }
        }
    }
    
    
    /**
     * 返回set制定区间的内的成员
     * @Title: rangeSortSet 
     * @date 2017年8月4日 下午7:04:46 
     * @version V1.0
     */
    public Set<String> zrange (String key, Long start ,Long end) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.zrange(key, start, end);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    
    
    /**
     * 新增
     * @Title: addSortSet 
     * @date 2017年8月4日 下午6:53:38 
     * @version V1.0
     */
    public Long zadd(String key, Double score ,String member) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.zadd(key, score, member);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    
    
    /**
     * 获得list
     * get list
     * @Title: batchGetList 
     * @param @param dbIndex
     * @param @param key
     * @param @param star
     * @param @param end 
     * @return void
     */
    public List<String> getlist(Integer dbIndex, String key,int start , int end){
    	ShardedJedis jds = null;
        try {
        	jds = shardedJedisPool.getResource();
        	if(dbIndex != null){
           	 	Collection<Jedis> collection=jds.getAllShards();
                Iterator<Jedis> jedis = collection.iterator();
                while(jedis.hasNext()){
                    jedis.next().select(dbIndex);
                }
            }
            ShardedJedisPipeline pipeline = jds.pipelined();
            Response<List<String>> rList = pipeline.lrange(key, start, end);
            pipeline.sync();
//            List<Object> list = pipeline.syncAndReturnAll();
            return rList.get();
        } finally {
            if (jds != null) {
            	jds.close();
            }
        }
    }
    
   
    /**
     * 添加list
     * @Title: addlist 
     * @param @param dbIndex
     * @param @param key
     * @param @param value 
     * @return void
     */
    public void addlist(int dbIndex, String key,String value){
    	ShardedJedis shardedJedis = null;
        try {
        	shardedJedis = shardedJedisPool.getResource();
            Collection<Jedis> collection=shardedJedis.getAllShards();
            Iterator<Jedis> jedis = collection.iterator();
            while(jedis.hasNext()){
                jedis.next().select(dbIndex);
            }
            ShardedJedisPipeline pipeline = shardedJedis.pipelined();
            pipeline.lpush(key, value);
            pipeline.sync();
        } finally {
            if (shardedJedis != null) {
            	shardedJedis.close();
            }
        }
    }
    

    /**
     * 获取map
     * @Title: getmap 
     * @param @param dbIndex
     * @param @param key
     * @param @return 
     * @return Map<String,String>
     */
    public Map<String, String> getmap(Integer dbIndex,String key){
    	ShardedJedis jds = null;
        try {
        	jds = shardedJedisPool.getResource();
        	if(dbIndex != null){
           	 	Collection<Jedis> collection=jds.getAllShards();
                Iterator<Jedis> jedis = collection.iterator();
                while(jedis.hasNext()){
                    jedis.next().select(dbIndex);
                }
            }
            ShardedJedisPipeline pipeline = jds.pipelined();
            Response<Map<String, String>> rs =  pipeline.hgetAll(key);
            pipeline.sync();
//            List<Object> list = pipeline.syncAndReturnAll();
            return rs.get();
        } finally {
            if (jds != null) {
            	jds.close();
            }
        }
    }
    
    /**
     * 获取all map
     * @Title: hgetAll 
     * @param @param key
     * @param @return 
     * @return Map<String,String>
     */
    public Map<String,String> hgetAll(String key){
    	ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.hgetAll(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    
    /**
     * 获得mapkey个数
     * @Title: hlen 
     * @param @param key
     * @param @return 
     * @return Long
     */
    public Long hdel(Integer index,String key,String... fieids){
    	ShardedJedis jds = null;
        try {
        	jds = shardedJedisPool.getResource();
            if(index != null){
           	 Collection<Jedis> collection=jds.getAllShards();
                Iterator<Jedis> jedis = collection.iterator();
                while(jedis.hasNext()){
                    jedis.next().select(index);
                }
            }
            return jds.hdel(key, fieids);
        } finally {
            if (jds != null) {
            	jds.close();
            }
        }
    }
    
    /**
     * 存map
     * @Title: hmset 
     * @param @param key
     * @param @param hash
     * @param @return 
     * @return String
     */
    public String hmset(Integer dbIndex,String key,Map<String,String> hash){
    	 ShardedJedis jds  = null;
         try {
        	 jds  = shardedJedisPool.getResource();
             if(dbIndex != null){
            	 Collection<Jedis> collection=jds.getAllShards();
                 Iterator<Jedis> jedis = collection.iterator();
                 while(jedis.hasNext()){
                     jedis.next().select(dbIndex);
                 }
             }
             ShardedJedisPipeline pipeline = jds.pipelined();
             Response<String> rs = pipeline.hmset(key, hash);
             pipeline.sync();
             return rs.toString();
         } finally {
             if (jds  != null) {
            	 jds .close();
             }
         }
    }
    /**
     * 获得mapkey个数
     * @Title: hlen 
     * @param @param key
     * @param @return 
     * @return Long
     */
    public Long hlen(String key){
    	ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.hlen(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    /**
     * map中的所有键值
     * @Title: hkeys 
     * @param @param key
     * @param @return 
     * @return Set<String>
     */
    public Set<String> hkeys(Integer dbIndex,String key){
    	ShardedJedis jds = null;
//        try {
//            jedis = shardedJedisPool.getResource();
//            return jedis.hkeys(key);
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
        try {
       	 jds  = shardedJedisPool.getResource();
            if(dbIndex != null){
           	 Collection<Jedis> collection=jds.getAllShards();
                Iterator<Jedis> jedis = collection.iterator();
                while(jedis.hasNext()){
                    jedis.next().select(dbIndex);
                }
            }
            ShardedJedisPipeline pipeline = jds.pipelined();
            Response<Set<String>> rs = pipeline.hkeys(key);
            pipeline.sync();
            return rs.get();
        } finally {
            if (jds  != null) {
           	 jds .close();
            }
        }
        
    }
    /**
     * map中的所有value
     * @Title: hvals 
     * @param @param key
     * @param @return 
     * @return List<String>
     */
    public List<String> hvals(String key){
    	ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.hvals(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    /**
     * 取出map中的某些字段值
     * @Title: hmget 
     * @param @param key
     * @param @param fields
     * @param @return 
     * @return List<String>
     */
    public List<String> hmget(String key,String ... fields){
    	ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.hmget(key, fields);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    
    
    
    public String get(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.get(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public byte[] get(byte[] key) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.get(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String set(byte[] key, byte[] value, int seconds) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
           /* String str = jedis.set(key, value);
            jedis.expire(key, seconds);*/
            String str = jedis.setex(key, seconds,value);
            return str;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 
     * 
     * @param key
     * @return
     */
    public Long hincrby(String key, String field, long value) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.hincrBy(key, field, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 
     * 
     * @param key
     * @return
     */
    public String hget(String key, String field) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.hget(key, field);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long hset(String key, String field, String value) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.hset(key, field, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 往集合中添加元素
     * @param key
     * @param members
     * @return
     */
    public Long sadd(String key, String... members) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.sadd(key, members);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 删除集合的元素
     * @param key
     * @param member
     * @return
     */
    public Long srem(String key, String... members) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.srem(key, members);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 返回集合中所有的元素
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            Set<String> set = jedis.smembers(key);
            if (set == null) {
                return Collections.emptySet();
            }
            return set;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    
    /**
     * 获取set集合
     * @param dbIndex
     * @param key
     * @return
     */
    public Set<String>  smembers(Integer dbIndex,String key){
    	ShardedJedis jds = null;
        try {
        	jds = shardedJedisPool.getResource();
        	if(dbIndex != null){
           	 	Collection<Jedis> collection=jds.getAllShards();
                Iterator<Jedis> jedis = collection.iterator();
                while(jedis.hasNext()){
                    jedis.next().select(dbIndex);
                }
            }
        	 Set<String> set = jds.smembers(key);
             if (set == null) {
                 return Collections.emptySet();
             }
             return set;
        } finally {
            if (jds != null) {
            	jds.close();
            }
        }
    }

    /**
     * 
     * 
     * @param key
     * @param field
     * @return
     */
    public String hincrby(String key, String field) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.hget(key, field);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long incr(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.incr(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long incrBy(String key, long integer) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.incrBy(key, integer);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String set(String key, String value) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.set(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String set(String key, String value, int seconds) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            String str = jedis.set(key, value);
            jedis.expire(key, seconds);
            return str;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long expire(String key, int seconds) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.expire(key, seconds);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long del(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            return jedis.del(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
