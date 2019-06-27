package com.ktp.project.util.redis;

import com.ktp.project.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Properties;

public class RedisDataSourceUtil {
	static JedisPool pool;

	static {
		Properties prop = PropertiesUtil.readConfig("/properties/redis.properties");
		// Jedis池配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(Integer.valueOf(prop.getProperty("redis.maxTotal")));
		// 对象最大空闲时间
		config.setMaxIdle(Integer.valueOf(prop.getProperty("redis.maxIdle")));
		config.setMinIdle(Integer.valueOf(prop.getProperty("redis.minIdle")));
		config.setTestOnBorrow(Boolean.valueOf(prop.getProperty("redis.testOnBorrow")));
		config.setTestOnReturn(Boolean.valueOf(prop.getProperty("redis.testOnReturn")));
		String host = prop.getProperty("redis.host");
		String password = prop.getProperty("redis.pass");
		int port = port = Integer.valueOf(prop.getProperty("redis.port"));

		if (password == null || "".equals(password.trim())) {
			password = null;
		}

		pool = new JedisPool(config, host, port, Integer.valueOf(prop.getProperty("redis.timeout")), password);
	}

	public static Jedis getRedisClient() {
		try {
			Jedis jedis = pool.getResource();
			return jedis;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void destroyPool() {
		pool.destroy();
	}

	public static void main(String[] args) {
		getRedisClient();
	}

}
