package com.ktp.project.util.redis;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Title: Redis处理方法
 * Description: 自定义工具类
 * Company: Copyright @
 *
 * @version 1.0 初稿
 * @author: maihj
 * @date: 2015-6-11 01:02:03
 */
public class RedisClientTemplate {

	public static void main(String[] arg) {
		//System.out.println("开始写入。。。。");
		//for(int i = 0; i < 1000; i++){
		//	set("a"+i,String.valueOf(i));
		//}
		//System.out.println("写入结束。。。。");
		set("a", "abc");
		System.out.println(get("a"));
		//List<String> list = lrange("WMS:INSTR:CK:RESULT", 0, -1);
		//for(String s : list){
		//	System.out.println(s);
		//rpop("WMS:INSTR:CK");
		//}
//		List<String> list1 = lrange("WMS:INSTR:RK", 0, -1);
//		for(String s : list1){
//			System.out.println(s);
//			rpop("WMS:INSTR:RK");
//		}
//		List<String> list2 = lrange("WMS:INSTR:YK", 0, -1);
//		for(String s : list2){
//			System.out.println(s);
//			rpop("WMS:INSTR:YK");
//		}
//		List<String> list3 = lrange("WMS:INSTR:KH", 0, -1);
//		for(String s : list3){
//			System.out.println(s);
//			rpop("WMS:INSTR:KH");
//		}
		//List<String> list4 = lrange("WMS:INSTR:SP:RESULT", 0, -1);
		//for(String s : list4){
		//	System.out.println(s);
		//rpop("WMS:INSTR:SP");
		//}
//		List<String> list5 = lrange("WMS:INSTR:QX", 0, -1);
//		for(String s : list5){
//			System.out.println(s);
//			rpop("WMS:INSTR:QX");
//		}
//		List<String> list6 = lrange("WMS:INSTR:FY", 0, -1);
//		for(String s : list6){
//			System.out.println(s);
//			rpop("WMS:INSTR:FY");
//		}

	}

	public static void disconnect() {
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		shardedJedis.disconnect();
	}

	public static void closeJedis(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	public static Jedis getJedis() {
		return RedisDataSourceUtil.getRedisClient();
	}

	/**
	 * 设置单个值
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static String set(String key, String value) {
		String result = null;

		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	/**
	 * 设置单个值和存活时间
	 *
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 * @author: ronghua
	 * @date: 2014年11月20日上午10:32:48
	 */
	public static String set(String key, String value, int seconds) {
		String result = null;

		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.set(key, value);
			shardedJedis.expire(key, seconds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	/**
	 * 获取单个值
	 *
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}


		try {
			result = shardedJedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Boolean exists(String key) {
		Boolean result = false;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String type(String key) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.type(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	/**
	 * 在某段时间后实现
	 *
	 * @param key
	 * @param seconds
	 * @return
	 */
	public static Long expire(String key, int seconds) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.expire(key, seconds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	/**
	 * 在某个时间点失效
	 *
	 * @param key
	 * @param unixTime
	 * @return
	 */
	public static Long expireAt(String key, long unixTime) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.expireAt(key, unixTime);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	/**
	 * 获取生存时间
	 * 存活时间
	 * @param key
	 * @return
	 */
	public static Long ttl(String key) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.ttl(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static boolean setbit(String key, long offset, boolean value) {

		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		boolean result = false;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.setbit(key, offset, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static boolean getbit(String key, long offset) {
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		boolean result = false;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getbit(key, offset);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static long setrange(String key, long offset, String value) {
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		long result = 0;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.setrange(key, offset, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String getrange(String key, long startOffset, long endOffset) {
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		String result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getrange(key, startOffset, endOffset);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String getSet(String key, String value) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getSet(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long setnx(String key, String value) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.setnx(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String setex(String key, int seconds, String value) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.setex(key, seconds, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long decrBy(String key, long integer) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.decrBy(key, integer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long decr(String key) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.decr(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long incrBy(String key, long integer) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.incrBy(key, integer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long incr(String key) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.incr(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long append(String key, String value) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.append(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String substr(String key, int start, int end) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.substr(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long hset(String key, String field, String value) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hset(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String hget(String key, String field) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hget(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long hsetnx(String key, String field, String value) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hsetnx(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String hmset(String key, Map<String, String> hash) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hmset(key, hash);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static List<String> hmget(String key, String... fields) {
		List<String> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hmget(key, fields);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long hincrBy(String key, String field, long value) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hincrBy(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Boolean hexists(String key, String field) {
		Boolean result = false;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hexists(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long del(String key) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long del(byte[] key) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long hdel(String key, String field) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hdel(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long hlen(String key) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hlen(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<String> hkeys(String key) {
		Set<String> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hkeys(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static List<String> hvals(String key) {
		List<String> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hvals(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Map<String, String> hgetAll(String key) {
		Map<String, String> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hgetAll(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long rpush(String key, String... string) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.rpush(key, string);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long lpush(String key, String... strings) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lpush(key, strings);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long llen(String key) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.llen(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static List<String> lrange(String key, long start, long end) {
		List<String> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String ltrim(String key, long start, long end) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.ltrim(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String lindex(String key, long index) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lindex(key, index);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String lset(String key, long index, String value) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lset(key, index, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long lrem(String key, long count, String value) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lrem(key, count, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String lpop(String key) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lpop(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static List<String> brpop(int timeout, String key) {
		List<String> results = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return results;
		}

		try {
			results = shardedJedis.brpop(timeout, key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return results;
	}

	public static List<byte[]> brpop(int timeout, byte[] key) {
		List<byte[]> results = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return results;
		}

		try {
			results = shardedJedis.brpop(timeout, key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return results;
	}

	public static List<String> blpop(int timeout, String key) {
		List<String> results = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return results;
		}

		try {
			results = shardedJedis.blpop(timeout, key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return results;
	}

	public static List<byte[]> blpop(int timeout, byte[] key) {
		List<byte[]> results = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return results;
		}

		try {
			results = shardedJedis.blpop(timeout, key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return results;
	}

	public static String rpop(String key) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.rpop(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long sadd(String key, String member) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.sadd(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		//return 1 add a not exist value ,
		//return 0 add a exist value
		return result;
	}

	public static Set<String> smembers(String key) {
		Set<String> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.smembers(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long srem(String key, String member) {
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();

		Long result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.srem(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String spop(String key) {
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		String result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.spop(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long scard(String key) {
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		Long result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.scard(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Boolean sismember(String key, String member) {
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		Boolean result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.sismember(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String srandmember(String key) {
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		String result = null;
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.srandmember(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long zadd(String key, double score, String member) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zadd(key, score, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<String> zrange(String key, int start, int end) {
		Set<String> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long zrem(String key, String member) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrem(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Double zincrby(String key, double score, String member) {
		Double result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zincrby(key, score, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long zrank(String key, String member) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrank(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long zrevrank(String key, String member) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrank(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<String> zrevrange(String key, int start, int end) {
		Set<String> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<Tuple> zrangeWithScores(String key, int start, int end) {
		Set<Tuple> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrangeWithScores(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
		Set<Tuple> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrangeWithScores(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long zcard(String key) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zcard(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Double zscore(String key, String member) {
		Double result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zscore(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static List<String> sort(String key) {
		List<String> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.sort(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static List<String> sort(String key, SortingParams sortingParameters) {
		List<String> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.sort(key, sortingParameters);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long zcount(String key, double min, double max) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zcount(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<String> zrangeByScore(String key, double min, double max) {
		Set<String> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrangeByScore(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<String> zrevrangeByScore(String key, double max, double min) {
		Set<String> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrangeByScore(key, max, min);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		Set<String> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrangeByScore(key, min, max, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		Set<String> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		Set<Tuple> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrangeByScoreWithScores(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
		Set<Tuple> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrangeByScoreWithScores(key, max, min);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		Set<Tuple> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
		Set<Tuple> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long zremrangeByRank(String key, int start, int end) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zremrangeByRank(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long zremrangeByScore(String key, double start, double end) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zremrangeByScore(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long linsert(String key, LIST_POSITION where, String pivot, String value) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.linsert(key, where, pivot, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String set(byte[] key, byte[] value) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	/**
	 * 设置单个值和存活时间
	 *
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 * @author: ronghua
	 * @date: 2014年11月20日上午10:32:48
	 */
	public static String set(byte[] key, byte[] value, int seconds) {
		String result = null;

		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.set(key, value);
			shardedJedis.expire(key, seconds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static byte[] get(byte[] key) {
		byte[] result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Boolean exists(byte[] key) {
		Boolean result = false;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String type(byte[] key) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.type(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long expire(byte[] key, int seconds) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.expire(key, seconds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long expireAt(byte[] key, long unixTime) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.expireAt(key, unixTime);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long ttl(byte[] key) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.ttl(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static byte[] getSet(byte[] key, byte[] value) {
		byte[] result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.getSet(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long setnx(byte[] key, byte[] value) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.setnx(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String setex(byte[] key, int seconds, byte[] value) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.setex(key, seconds, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long decrBy(byte[] key, long integer) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.decrBy(key, integer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long decr(byte[] key) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.decr(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long incrBy(byte[] key, long integer) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.incrBy(key, integer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long incr(byte[] key) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.incr(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long append(byte[] key, byte[] value) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.append(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static byte[] substr(byte[] key, int start, int end) {
		byte[] result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.substr(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long hset(byte[] key, byte[] field, byte[] value) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hset(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static byte[] hget(byte[] key, byte[] field) {
		byte[] result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hget(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long hsetnx(byte[] key, byte[] field, byte[] value) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hsetnx(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String hmset(byte[] key, Map<byte[], byte[]> hash) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hmset(key, hash);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static List<byte[]> hmget(byte[] key, byte[]... fields) {
		List<byte[]> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hmget(key, fields);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long hincrBy(byte[] key, byte[] field, long value) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hincrBy(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Boolean hexists(byte[] key, byte[] field) {
		Boolean result = false;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hexists(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long hdel(byte[] key, byte[] field) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hdel(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long hlen(byte[] key) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hlen(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<byte[]> hkeys(byte[] key) {
		Set<byte[]> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hkeys(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Collection<byte[]> hvals(byte[] key) {
		Collection<byte[]> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hvals(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Map<byte[], byte[]> hgetAll(byte[] key) {
		Map<byte[], byte[]> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.hgetAll(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long rpush(byte[] key, byte[] string) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.rpush(key, string);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long lpush(byte[] key, byte[]... string) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lpush(key, string);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long llen(byte[] key) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.llen(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static List<byte[]> lrange(byte[] key, int start, int end) {
		List<byte[]> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String ltrim(byte[] key, int start, int end) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.ltrim(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static byte[] lindex(byte[] key, int index) {
		byte[] result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lindex(key, index);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static String lset(byte[] key, int index, byte[] value) {
		String result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lset(key, index, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long lrem(byte[] key, int count, byte[] value) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lrem(key, count, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static byte[] lpop(byte[] key) {
		byte[] result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.lpop(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static byte[] rpop(byte[] key) {
		byte[] result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.rpop(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long sadd(byte[] key, byte[] member) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.sadd(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<byte[]> smembers(byte[] key) {
		Set<byte[]> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.smembers(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long srem(byte[] key, byte[] member) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.srem(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static byte[] spop(byte[] key) {
		byte[] result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.spop(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long scard(byte[] key) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.scard(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Boolean sismember(byte[] key, byte[] member) {
		Boolean result = false;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.sismember(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static byte[] srandmember(byte[] key) {
		byte[] result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.srandmember(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long zadd(byte[] key, double score, byte[] member) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zadd(key, score, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<byte[]> zrange(byte[] key, int start, int end) {
		Set<byte[]> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long zrem(byte[] key, byte[] member) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrem(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Double zincrby(byte[] key, double score, byte[] member) {
		Double result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zincrby(key, score, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long zrank(byte[] key, byte[] member) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrank(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long zrevrank(byte[] key, byte[] member) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrank(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<byte[]> zrevrange(byte[] key, int start, int end) {
		Set<byte[]> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
		Set<Tuple> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrangeWithScores(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {
		Set<Tuple> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrangeWithScores(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long zcard(byte[] key) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zcard(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Double zscore(byte[] key, byte[] member) {
		Double result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zscore(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static List<byte[]> sort(byte[] key) {
		List<byte[]> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.sort(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
		List<byte[]> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.sort(key, sortingParameters);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long zcount(byte[] key, double min, double max) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zcount(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
		Set<byte[]> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrangeByScore(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
		Set<byte[]> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrangeByScore(key, min, max, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
		Set<Tuple> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrangeByScoreWithScores(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
		Set<Tuple> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
		Set<byte[]> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrangeByScore(key, max, min);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
		Set<byte[]> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
		Set<Tuple> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrangeByScoreWithScores(key, max, min);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
		Set<Tuple> result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long zremrangeByRank(byte[] key, int start, int end) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zremrangeByRank(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long zremrangeByScore(byte[] key, double start, double end) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.zremrangeByScore(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	public static Long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
		Long result = null;
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		try {
			result = shardedJedis.linsert(key, where, pivot, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
		return result;
	}

	/**
	 * 根据前缀删除
	 * @param param
	 */
	public static void batchDel(String param) {
		Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
		if (shardedJedis == null) {
			return;
		}
		try {
			Set<String> keys = shardedJedis.keys(param + "*");
			for (String key : keys) {
				shardedJedis.del(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(shardedJedis);
		}
	}
/*
	@SuppressWarnings("deprecation")
	public static List<Object> pipelined(ShardedJedisPipeline shardedJedisPipeline) {
    	Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
        List<Object> result = null;
        if (shardedJedis == null) {
            return result;
        }
        
        try {
            result = shardedJedis.pipelined(shardedJedisPipeline);
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
            closeJedis(shardedJedis);
        }
        return result;
    }

    public static Jedis getShard(byte[] key) {
    	Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
        Jedis result = null;
        if (shardedJedis == null) {
            return result;
        }
        
        try {
            result = shardedJedis.getShard(key);
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
            closeJedis(shardedJedis);
        }
        return result;
    }

    public static Jedis getShard(String key) {
    	Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
        Jedis result = null;
        if (shardedJedis == null) {
            return result;
        }
        
        try {
            result = shardedJedis.getShard(key);
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
            closeJedis(shardedJedis);
        }
        return result;
    }

    public static JedisShardInfo getShardInfo(byte[] key) {
    	Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
        JedisShardInfo result = null;
        if (shardedJedis == null) {
            return result;
        }
        
        try {
            result = shardedJedis.getShardInfo(key);
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
            closeJedis(shardedJedis);
        }
        return result;
    }

    public static JedisShardInfo getShardInfo(String key) {
    	Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
        JedisShardInfo result = null;
        if (shardedJedis == null) {
            return result;
        }
        
        try {
            result = shardedJedis.getShardInfo(key);
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
            closeJedis(shardedJedis);
        }
        return result;
    }

    public static String getKeyTag(String key) {
    	Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
        String result = null;
        if (shardedJedis == null) {
            return result;
        }
        
        try {
            result = shardedJedis.getKeyTag(key);
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
            closeJedis(shardedJedis);
        }
        return result;
    }

    public static Collection<JedisShardInfo> getAllShardInfo() {
    	Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
        Collection<JedisShardInfo> result = null;
        if (shardedJedis == null) {
            return result;
        }
        
        try {
            result = shardedJedis.getAllShardInfo();

        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
            closeJedis(shardedJedis);
        }
        return result;
    }

    public static Collection<Jedis> getAllShards() {
    	Jedis shardedJedis = RedisDataSourceUtil.getRedisClient();
        Collection<Jedis> result = null;
        if (shardedJedis == null) {
            return result;
        }
        
        try {
            result = shardedJedis.getAllShards();

        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
            closeJedis(shardedJedis);
        }
        return result;
    }*/
}
