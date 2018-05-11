package com.guse.chessgame.common;

import com.guse.chessgame.commonm.redis.JedisDataSource;

import redis.clients.jedis.Jedis;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Jedis jedis = JedisDataSource.getJedis();
    	jedis.set("xiaomin", "fufkd");
        System.out.println(jedis.get("xiaomin"));
    }
}
