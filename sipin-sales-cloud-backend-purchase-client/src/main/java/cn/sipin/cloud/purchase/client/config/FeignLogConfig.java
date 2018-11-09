/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import feign.Logger.Level;

@Configuration
@Profile("develop") // 只有开发环境develop才能打印Feign日志
public class FeignLogConfig {

  @Bean Level feignLoggerLevel() {
    return Level.FULL;
  }

}
