package com.atguigu.gmall.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

    private JedisPool jedisPool;

    public void initJedisPool(String host, int port, int database){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(200);//最大连接数
        jedisPoolConfig.setMaxWaitMillis(10*1000);//最长等待时间
        jedisPoolConfig.setMinIdle(10); // 最少剩余数
        jedisPoolConfig.setBlockWhenExhausted(true); // 如果到最大数，设置等待
        jedisPoolConfig.setTestOnBorrow(true);// 在获取连接时，检查是否有效

        jedisPool = new JedisPool(jedisPoolConfig, host, port, 20*1000);
    }
    public Jedis getJedis(){
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }
}
