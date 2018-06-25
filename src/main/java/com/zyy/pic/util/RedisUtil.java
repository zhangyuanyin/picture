package com.zyy.pic.util;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component  
public class RedisUtil {
	private static final Logger logger = Logger.getLogger(RedisUtil.class);
  
    @SuppressWarnings("rawtypes")
	@Autowired  
    private RedisTemplate redisTemplate;  
  
    /** 
     * 批量删除对应的value 
     * @param keys 
     */  
    public void remove(final String... keys) {  
        for (String key : keys) {  
            remove(key);  
        }  
    }  
  
    /** 
     * 批量删除key 
     */  
    @SuppressWarnings("unchecked")  
    public void removePattern(final String pattern) {  
        Set<Serializable> keys = redisTemplate.keys(pattern);  
        if (keys.size() > 0)  
            redisTemplate.delete(keys);
    }  
  
    /** 
     * 删除对应的value 
     * @param key 
     */  
    @SuppressWarnings("unchecked")  
    public void remove(final String key) {  
        if (exists(key)) {  
            redisTemplate.delete(key);  
        }  
    }  
  
    /** 
     * 判断缓存中是否有对应的value 
     * @param key 
     */  
    @SuppressWarnings("unchecked")  
    public boolean exists(final String key) {  
        return redisTemplate.hasKey(key);  
    }  
  
    /** 
     * 读取缓存 
     * @param key 
     */  
    @SuppressWarnings("unchecked")  
    public Object get(final String key) {  
        Object result = null;  
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();  
        result = operations.get(key);  
        return result;  
    }  
  
    /** 
     * 写入缓存 
     * @param key 
     * @param value 
     */  
    @SuppressWarnings("unchecked")  
    public boolean set(final String key, Object value) {  
        boolean result = false;  
        try {  
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();  
            operations.set(key, value);  
            result = true;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  
  
    /** 
     * 写入缓存 
     * @param key 
     * @param value 
     */  
    @SuppressWarnings("unchecked")  
    public boolean set(final String key, Object value, Long expireTime) {  
        boolean result = false;  
        try {  
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();  
            operations.set(key, value);  
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);  
            result = true;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  
    
    @SuppressWarnings("unchecked")
	public boolean setNX(final String key, final String value) {
        Object obj = null;
        try {
            obj = redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    Boolean success = connection.setNX(serializer.serialize(key), serializer.serialize(value));
                    connection.close();
                    return success;
                }
            });
        } catch (Exception e) {
        	e.printStackTrace();
			logger.error("setNX redis error, key : " + key + ", error: " + e.getMessage());
        }
        return obj != null ? (Boolean) obj : false;
    }
}