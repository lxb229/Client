package com.guse.chessgame.commonm.redis;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

/** 
* @ClassName: JedisDao 
* @Description: redis数据库操作工具类
* @author Fily GUSE
* @date 2017年8月14日 下午5:15:31 
*  
*/
public class JedisUtils {

	private static final Logger log = Logger.getLogger(JedisUtils.class);

	/** 
	* @Title: getLpop 
	* @Description: 移除并返回列表 key 的头元素 
	* @param @param key
	* @param @return
	* @param @throws Exception
	* @return String 
	* @throws 
	*/
	public String getLpop(String key) throws Exception {
		String result = null;

		Jedis jedis = JedisDataSource.getJedis();
		
		if (jedis == null) {
			return result;
		}
		try {
			result = jedis.lpop(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}
		return result;
	}

	/** 
	* @Title: set 
	* @Description: 字符串存储模式，key为redis的key值，value为存储的字符串
	* @param @param key
	* @param @param value
	* @param @return
	* @param @throws Exception    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String set(String key, String value) throws Exception {
		String result = null;

		Jedis jedis = JedisDataSource.getJedis();

		if (jedis == null) {
			return result;
		}
		try {
			result = jedis.set(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}
		return result;
	}
	
	/** 
	* @Title: set 
	* @Description: 字节码存储模式，key为redis的key值，value为存储的字符串
	* @param @param key
	* @param @param value
	* @param @return
	* @param @throws Exception    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String set(byte[] key, byte[] value) throws Exception {
		String result = null;

		Jedis jedis = JedisDataSource.getJedis();

		if (jedis == null) {
			return result;
		}
		try {
			result = jedis.set(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}
		return result;
	}

	/** 
	* @Title: get 
	* @Description: 根据key值获取value
	* @param @param key
	* @param @return
	* @param @throws Exception    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String get(String key) throws Exception {
		String result = null;
		Jedis jedis = JedisDataSource.getJedis();
		if (jedis == null) {
			return result;
		}

		try {
			result = jedis.get(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}
		return result;
	}
	
	/** 
	* @Title: get 
	* @Description: 根据key值获取value
	* @param @param key
	* @param @return
	* @param @throws Exception    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public byte[] get(byte[] key) throws Exception {
		byte[] result = null;
		Jedis jedis = JedisDataSource.getJedis();
		if (jedis == null) {
			return result;
		}

		try {
			result = jedis.get(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}
		return result;
	}
	
	/** 
	* @Title: del 
	* @Description: 删除key信息 
	* @param @param key
	* @param @return
	* @param @throws Exception
	* @return Boolean 
	* @throws 
	*/
	public Boolean del(byte[] key) throws Exception{
		Boolean result = false;
		Jedis jedis = JedisDataSource.getJedis();
		if(jedis != null) {
			try {
				long n = jedis.del(key);
				result = (n>0);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
				JedisDataSource.returnResource(jedis);
			}
		}
		
		return result;
	}

	/** 
	* @Title: exists 
	* @Description: 验证key值是否存在
	* @param @param key
	* @param @return
	* @param @throws Exception    设定文件 
	* @return Boolean    返回类型 
	* @throws 
	*/
	public Boolean exists(String key) throws Exception {
		Boolean result = false;
		Jedis jedis = JedisDataSource.getJedis();
		if (jedis == null) {
			return result;
		}
		try {
			result = jedis.exists(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}
		return result;
	}
	
	/** 
	* @Title: exists 
	* @Description: 验证字节码key值是否存在
	* @param @param key
	* @param @return
	* @param @throws Exception    设定文件 
	* @return Boolean    返回类型 
	* @throws 
	*/
	public Boolean exists(byte[] key) throws Exception {
		Boolean result = false;
		Jedis jedis = JedisDataSource.getJedis();
		if (jedis == null) {
			return result;
		}
		try {
			result = jedis.exists(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}
		return result;
	}

	/** 
	* @Title: type 
	* @Description: 获取key的存储类型
	* @param @param key
	* @param @return
	* @param @throws Exception    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String type(String key) throws Exception {
		String result = null;
		Jedis jedis = JedisDataSource.getJedis();
		if (jedis == null) {
			return result;
		}
		try {
			result = jedis.type(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}
		return result;
	}

	/** 
	* @Title: setRange 
	* @Description: list类型设置值，从offset指定位置插入
	* @param @param key
	* @param @param offset
	* @param @param value
	* @param @return
	* @param @throws Exception    设定文件 
	* @return long    返回类型 
	* @throws 
	*/
	public long setRange(String key, long offset, String value) throws Exception {
		Jedis jedis = JedisDataSource.getJedis();
		long result = 0;
		if (jedis == null) {
			return result;
		}
		try {
			result = jedis.setrange(key, offset, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}
		return result;
	}

	/** 
	* @Title: getRange 
	* @Description: list类型获取指定范围的值
	* @param @param key
	* @param @param startOffset
	* @param @param endOffset
	* @param @return
	* @param @throws Exception    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String getRange(String key, long startOffset, long endOffset) throws Exception {
		Jedis jedis = JedisDataSource.getJedis();
		String result = null;
		if (jedis == null) {
			return result;
		}
		try {
			result = jedis.getrange(key, startOffset, endOffset);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}
		return result;
	}

	/** 
	* @Title: getMapValue 
	* @Description: 获取map一行的值
	* @param @param key
	* @param @param field
	* @param @return
	* @param @throws Exception    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String getMapValue(String key, String field) throws Exception {
		Jedis jedis = JedisDataSource.getJedis();
		String result = null;
		if (jedis == null) {
			return result;
		}
		try {
			result = jedis.hget(key, field);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}
		return result;
	}

	/** 
	* @Title: listrpush 
	* @Description: 向list类型的后端增加值
	* @param @param key
	* @param @param data
	* @param @throws Exception    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public void listrpush(String key, String data) throws Exception {
		Jedis jedis = JedisDataSource.getJedis();
		if (jedis == null) {
			return;
		}
		try {
			jedis.rpush(key, data);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}
	}

	/** 
	* @Title: listlrange 
	* @Description: 从list头开始取值，取完删除已经取过的值
	* @param @param key
	* @param @param begin
	* @param @param end
	* @param @return
	* @param @throws Exception    设定文件 
	* @return List    返回类型 
	* @throws 
	*/
	@SuppressWarnings("rawtypes")
	public List listlrange(String key, long begin, long end) throws Exception {
		List list = null;
		Jedis jedis = JedisDataSource.getJedis();
		if (jedis == null) {
			return null;
		}
		try {
			list = jedis.lrange(key, begin, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}

		return list;
	}

	/** 
	* @Title: listdel 
	* @Description: list类型范围删除
	* @param @param key
	* @param @param begin
	* @param @param end
	* @param @throws Exception    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public void listdel(String key, long begin, long end) throws Exception {
		Jedis jedis = JedisDataSource.getJedis();
		if (jedis == null) {
			return;
		}
		try {
			jedis.ltrim(key, begin, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}
	}

	/** 
	* @Title: mapPut 
	* @Description: map增加值
	* @param @param name
	* @param @param key
	* @param @param value
	* @param @throws Exception    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public void mapPut(String name, String key, String value) throws Exception {
		Jedis jedis = JedisDataSource.getJedis();
		if (jedis == null) {
			return;
		}
		try {
			jedis.hset(name, key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}
	}

	/** 
	* @Title: mapdel 
	* @Description: 删除map的某一条记录
	* @param @param name
	* @param @param key
	* @param @throws Exception    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public void mapdel(String name, String key) throws Exception {
		Jedis jedis = JedisDataSource.getJedis();
		if (jedis == null) {
			return;
		}
		try {
			jedis.hdel(name, key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}
	}

	/** 
	* @Title: mapgetAll 
	* @Description: 获取指定map的所有值
	* @param @param name
	* @param @return
	* @param @throws Exception    设定文件 
	* @return Map    返回类型 
	* @throws 
	*/
	@SuppressWarnings("rawtypes")
	public Map mapgetAll(String name) throws Exception {
		Jedis jedis = JedisDataSource.getJedis();
		if (jedis == null) {
			return null;
		}
		try {
			return jedis.hgetAll(name);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			JedisDataSource.returnResource(jedis);
		}
		return null;
	}

	

}
