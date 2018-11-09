/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Primary
@Service
public class RedisClusterService {

  private RedisTemplate<String, Object> redisTemplate;

  @Autowired
  public RedisClusterService(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public String get(String key) {
    return (String) redisTemplate.opsForValue().get(key);
  }

  public String set(String key, String value) {
    redisTemplate.opsForValue().set(key, value);

    return get(key);
  }

  public String set(String key, String value, int second) {
    redisTemplate.opsForValue().set(key, value, second, TimeUnit.MINUTES);

    return get(key);
  }

  public String setObject(String key, Object value) {
    return null;
  }

  public String hget(String key, String field) {
    return (String) redisTemplate.opsForHash().get(key, field);
  }

  /**
   * HSET key field value
   */
  public String hset(String key, String field, String value) {
    redisTemplate.opsForHash().put(key, field, value);
    return get(key);
  }

  public String hsetObject(String key, String field, Object value) {
    redisTemplate.opsForHash().put(key, field, value);
    return get(key);
  }

  public Boolean existKey(String key) {
    return redisTemplate.hasKey(key);
  }

  public Long incr(String key) {
    return redisTemplate.opsForValue().increment(key, 1L);
  }

  public Boolean expire(String key, int second) {
    return redisTemplate.expire(key, second, TimeUnit.MINUTES);
  }

  public Long ttl(String key) {
    return redisTemplate.getExpire(key);
  }

  public Boolean del(String key) {
    return redisTemplate.delete(key);
  }

  public Long hdel(String key, String field) {
    return redisTemplate.opsForHash().delete(key, field);
  }
}
