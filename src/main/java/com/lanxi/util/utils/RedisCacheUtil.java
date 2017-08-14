package com.lanxi.util.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.lanxi.util.entity.LogFactory;

import redis.clients.jedis.Jedis;
/**
 * redis缓存工具类
 * 若真实需要,使用dubbo中的redisCacheService来缓存
 * @author yangyuanjian
 *
 */
public class RedisCacheUtil {
	/**redis 连接*/
	private static Jedis jedis;
	public static void setConn(Jedis jedis){
		RedisCacheUtil.jedis=jedis;
	}

	private static void checkConn(){
		if(jedis==null)
			throw new RuntimeException("jedis conn can't be null ! please invoke method 'setConn' to init jedis connection ! ");
	}
	
	public Long delete(String key) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试删除key["+key+"]");
			conn=jedis;
			Long result= conn.del(key);
			LogFactory.info(this,"删除["+key+"]的操作结果为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("删除key["+key+"]时发生异常", e);
		}
	}
	public Long delete(byte[] key) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试删除key["+key+"]");
			conn=jedis;
			Long result= conn.del(key);
			LogFactory.info(this,"删除["+key+"]的操作结果为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("删除key["+key+"]时发生异常", e);
		}
	}

 
	public Boolean has(String key) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试检查是否有key["+key+"]");
			conn=jedis;
			Boolean result= conn.exists(key);
			LogFactory.info(this, "检查key["+key+"]的结果为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("检查是否有key["+key+"]时发生异常", e);
		}
	}

 
	public Boolean has(byte[] key) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试检查是否有key["+key+"]");
			conn=jedis;
			Boolean result= conn.exists(key);
			LogFactory.info(this, "检查key["+key+"]的结果为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("检查是否有key["+key+"]时发生异常", e);
		}
	}

 
	public Long setKeyLife(String key, long lifeTime) {
			checkConn();
			Jedis conn=null;
		try {
			LogFactory.info(this, "尝试设置key["+key+"]的生命周期为["+lifeTime+"]毫秒");
			conn=jedis;
			Long result = conn.pexpire(key, lifeTime);
			LogFactory.info(this, "设置key["+key+"]的生命周期为["+lifeTime+"]毫秒的操作结果为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("设置key["+key+"]的生命周期为["+lifeTime+"]毫秒时发生异常", e);
		}
	}

 
	public Long setKeyLife(byte[] key, long lifeTime) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试设置key["+key+"]的生命周期为["+lifeTime+"]毫秒");
			conn=jedis;
			Long result = conn.pexpire(key, lifeTime);
			LogFactory.info(this, "设置key["+key+"]的生命周期为["+lifeTime+"]毫秒的操作结果为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("设置key["+key+"]的生命周期为["+lifeTime+"]毫秒时发生异常", e);
		}
	}

 
	public String set(String key, String value) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试设置key["+key+"]的值为["+value+"]");
			conn=jedis;
			String result=conn.set(key, value);
			LogFactory.info(this, "设置key["+key+"]的值为["+value+"]的操作结果为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("设置key["+key+"]的值为["+value+"]时发生异常", e);
		}
	}

 
	public String set(byte[] key, byte[] value) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试设置key["+key+"]的值为["+value+"]");
			conn=jedis;
			String result=conn.set(key, value);
			LogFactory.info(this, "设置key["+key+"]的值为["+value+"]的操作结果为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("设置key["+key+"]的值为["+value+"]时发生异常", e);
		}
	}
	
 
	public String set(String key, String value, long lifeTime) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试设置key["+key+"]的值为["+value+"]");
			conn=jedis;
			String result= conn.psetex(key, lifeTime, value);
			LogFactory.info(this, "设置key["+key+"]的值为["+value+"]的操作结果为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("设置key["+key+"]的值为["+value+"]生命周期["+lifeTime+"]毫秒时发生异常", e);
		}
	}



 
	public String set(byte[] key, byte[] value, long lifeTime) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试设置key["+key+"]的值为["+value+"]");
			conn=jedis;
			String result= conn.psetex(key, lifeTime, value);
			LogFactory.info(this, "设置key["+key+"]的值为["+value+"]的操作结果为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("设置key["+key+"]的值为["+value+"]生命周期["+lifeTime+"]毫秒时发生异常", e);
		}
	}

 
	public String get(String key) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试获取key["+key+"]的值");
			conn=jedis;
			String result= conn.get(key);
			LogFactory.info(this, "获取key["+key+"]的值为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("获取key["+key+"]的值时发生异常", e);
		}
	}

 
	public byte[] get(byte[] key) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试获取key["+key+"]的值");
			conn=jedis;
			byte[] result= conn.get(key);
			LogFactory.info(this, "获取key["+key+"]的值为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("获取key["+key+"]的值时发生异常", e);
		}
	}

 
	public String setMap(String key, Map<String, String> map) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试设置key["+key+"]的值为map["+map+"]");
			conn=jedis;
			String result=conn.hmset(key, map);
			LogFactory.info(this, "设置key["+key+"]的值为map["+map+"]的操作结果为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("设置key["+key+"]的值为map["+map+"]时发生异常", e);
		}
	}

 
	public String setMap(byte[] key, Map<byte[], byte[]> map) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试设置key["+key+"]的值为map["+map+"]");
			conn=jedis;
			String result=conn.hmset(key, map);
			LogFactory.info(this, "设置key["+key+"]的值为map["+map+"]的操作结果为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("设置key["+key+"]的值为map["+map+"]时发生异常", e);
		}
	}

 
	public Long setMapOne(String key, String field, String value) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试设置key为["+key+"]的map中的键["+field+"]值为["+value+"]");
			conn=jedis;
			Long result=conn.hset(key, field, value);
			LogFactory.info(this, "设置key为["+key+"]的map中的键["+field+"]值为["+value+"]的操作结果为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("设置key为["+key+"]的map中的键["+field+"]值为["+value+"]时发生异常", e);
		}
	}

 
	public Long setMapOne(byte[] key, byte[] field, byte[] value) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试设置key为["+key+"]的map中的键["+field+"]值为["+value+"]");
			conn=jedis;
			Long result=conn.hset(key, field, value);
			LogFactory.info(this, "设置key为["+key+"]的map中的键["+field+"]值为["+value+"]的操作结果为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("设置key为["+key+"]的map中的键["+field+"]值为["+value+"]时发生异常", e);
		}
	}

 
	public List<String> getMap(String key, String... fields) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试获取key为["+key+"]的map中的键为["+Arrays.asList(fields)+"]的值");
			conn=jedis;
			List<String> result=conn.hmget(key, fields);
			LogFactory.info(this, "获取到的key为["+key+"]的map中的键为["+Arrays.asList(fields)+"]的值为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("获取key为["+key+"]的map中的键为["+Arrays.asList(fields)+"]的值时发生异常", e);
		}
	}

 
	public List<byte[]> getMap(byte[] key, byte[]... fields) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试获取key为["+key+"]的map中的键为["+Arrays.asList(fields)+"]的值");
			conn=jedis;
			List<byte[]> result=conn.hmget(key, fields);
			LogFactory.info(this, "获取到的key为["+key+"]的map中的键为["+Arrays.asList(fields)+"]的值为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("获取key为["+key+"]的map中的键为["+Arrays.asList(fields)+"]的值时发生异常", e);
		}
	}

 
	public String getMapOne(String key, String field) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试获取key为["+key+"]的map中的键为["+field+"]的值");
			conn=jedis;
			String result=conn.hget(key, field);
			LogFactory.info(this, "获取到的key为["+key+"]的map中的键为["+field+"]的值为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("获取key为["+key+"]的map中的键为["+field+"]的值时发生异常", e);
		}
	}

 
	public byte[] getMapOne(byte[] key, byte[] field) {
		checkConn();
		Jedis conn=null;
		try {
			LogFactory.info(this, "尝试获取key为["+key+"]的map中的键为["+field+"]的值");
			conn=jedis;
			byte[] result=conn.hget(key, field);
			LogFactory.info(this, "获取到的key为["+key+"]的map中的键为["+field+"]的值为["+result+"]");
			return result;
		} catch (Exception e) {
			throw new RuntimeException("获取key为["+key+"]的map中的键为["+field+"]的值时发生异常", e);
		}
	}

}
