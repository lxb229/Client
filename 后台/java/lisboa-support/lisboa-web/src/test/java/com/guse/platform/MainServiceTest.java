package com.guse.platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.guse.platform.common.utils.JedisUtils;
import com.guse.platform.utils.redis.RedisKeys;

import redis.clients.jedis.Tuple;

public class MainServiceTest extends TestBase {
	
	@Autowired
	private JedisUtils          jedisUtils;
	@Test
	public void test(){
			
//		Set<String> redisData = jedisUtils.zrange(RedisKeys.OPERATION_MONEY_RANKING+":20170805", 0L, 20L);
	
//		for (String str : redisData) {
//		      System.out.println(str+"\n");
//		}
//		System.out.println("----------------------------");
//		
		Set<Tuple> redisData1 = jedisUtils.zRangeWhitScores(0,RedisKeys.OPERATION_MONEY_RANKING+":20170805", 0L, 20L);
		List<Map<String, String>> listmap = new ArrayList<>();
		for (Tuple t : redisData1) {
			Map<String, String> map = jedisUtils.getmap(3, RedisKeys.OPERATION_BASE_USER+":"+new String(t.getElement()));
			listmap.add(map);
		}
		System.out.println(JSON.toJSONString(listmap));
		
//		System.out.println(jedisUtils.getmap(3, RedisKeys.OPERATION_BASE_USER+":"+100010).toString());
		
//		List<Map<String, String>> listmap = jedisUtils.getListMapForZsortSetKeys(3, redisData1, RedisKeys.OPERATION_BASE_USER,new String[]{"todayTotalAmount"});
//		
//		for (int i = 0; i < listmap.size(); i++) {
//			System.out.println(JSON.toJSONString(listmap.get(i)));
//		}
		
	}
	
}
