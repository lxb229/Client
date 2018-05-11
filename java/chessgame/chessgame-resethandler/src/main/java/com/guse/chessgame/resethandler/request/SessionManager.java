package com.guse.chessgame.resethandler.request;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.guse.chessgame.commonm.redis.JedisUtils;

/**
 * @ClassName: SessionManager
 * @Description: TODO
 * @author Fily GUSE
 * @date 2017年8月17日 上午10:38:58
 * 
 */
public class SessionManager {


	// 将Session放入redis
	public static void pushSession2Redis(Session session) {
		// 先删除原有session
		removeSession(session.getId());
		// 保存数据到redis
		JedisUtils jedis = new JedisUtils();

		try {
			jedis.set(session.getId().getBytes(), serialize(session));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 从redis获取指定session
	public static Session findById(String sessionId) {
		JedisUtils jedis = new JedisUtils();
		Session session = null;
		try {
			byte[] data = jedis.get(sessionId.getBytes());
			
			session = (Session)unserialize(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return session;
		
	}

	// 删除连接对象
	public static void removeSession(String sessionId) {
		JedisUtils jedis = new JedisUtils();
		try {
			Session session = findById(sessionId);
			if(session != null) {
				if(session.getChannel() != null) {
					session.getChannel().close();
				}
				
				jedis.del(sessionId.getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	* @Title: serialize 
	* @Description: 对象序列化 
	* @param @param object
	* @param @return
	* @return byte[] 
	* @throws 
	*/
	private static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
		}
		return null;
	}

	/** 
	* @Title: unserialize 
	* @Description: 对象反序列化 
	* @param @param bytes
	* @param @return
	* @return Object 
	* @throws 
	*/
	private static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
		}
		return null;
	}

	public static void main(String[] args) {
		Session s = new Session();
		s.setId("hello");
		
		pushSession2Redis(s);
		
//		jedis.set("key001","value001");
//		JedisUtils jedis = new JedisUtils();
//		try {
//			jedis.set("key", "sdfsd");
//			System.out.println(jedis.get("key"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}
}
