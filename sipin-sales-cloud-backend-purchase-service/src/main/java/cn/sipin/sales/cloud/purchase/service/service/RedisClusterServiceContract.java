/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.service;

public interface RedisClusterServiceContract {

  String get(String key);
  String set(String key, String value);
  String set(String key, String value, int second);
  String setObject(String key, Object value);
  String hget(String key, String field);
  String hset(String key, String field, String value);
  String hsetObject(String key, String field, Object value);

  Boolean existKey(String key);

  Long incr(String key);
  Boolean expire(String key, int second);
  Long ttl(String key);
  Boolean del(String key);
  Long hdel(String key, String field);
}
